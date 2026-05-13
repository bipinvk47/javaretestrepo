#!/usr/bin/env python3
"""Emit platform testing JSON for the Maven/Spring Boot fixture (bundle, deps, imports, cycles, build time)."""

from __future__ import annotations

import json
import re
import subprocess
import sys
import time
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MVN = ROOT / ("mvnw.cmd" if sys.platform.startswith("win") else "mvnw")


def _run_mvn(args: list[str]) -> subprocess.CompletedProcess[str]:
    cmd = [str(MVN), "-B"] + args
    return subprocess.run(cmd, cwd=ROOT, text=True, capture_output=True)


def _pick_fat_jar() -> Path | None:
    jars = list((ROOT / "target").glob("*.jar"))
    if not jars:
        return None
    return max(jars, key=lambda p: p.stat().st_size)


def _unused_declared_dependencies(text: str) -> int:
    m = re.search(
        r"Unused declared dependencies found:\s*\n((?:[ \t]+[^\n]+\n)+)",
        text,
        flags=re.IGNORECASE,
    )
    if not m:
        return 0
    lines = [ln.strip() for ln in m.group(1).splitlines() if ln.strip()]
    return len(lines)


def _unused_imports(build_log: str) -> int:
    return len(re.findall(r"unused import", build_log, flags=re.IGNORECASE))


def _jdepend_cycle_count() -> int:
    candidates = [
        ROOT / "target" / "jdepend-report.xml",
        ROOT / "target" / "site" / "jdepend-report.xml",
    ]
    xml_path = next((p for p in candidates if p.is_file()), None)
    if xml_path is None:
        return -1
    text = xml_path.read_text(encoding="utf-8", errors="ignore")
    m = re.search(r"<Cycles[^>]*>(.*?)</Cycles>", text, flags=re.IGNORECASE | re.DOTALL)
    if not m:
        return 0
    inner = m.group(1).strip()
    if not inner:
        return 0
    return len(re.findall(r"<Package\b", inner, flags=re.IGNORECASE))


def main() -> int:
    out = ROOT / "metrics" / "platform_dependency_report.json"
    out.parent.mkdir(parents=True, exist_ok=True)

    categories = {
        "performance_testing": {
            "dependency_analysis": {
                "bundle_size_analysis": True,
                "unused_dependency_detection": True,
                "unused_import_count": True,
                "build_performance": True,
                "dependency_graph_analysis": True,
            }
        }
    }

    t0 = time.perf_counter()
    build = _run_mvn(["clean", "package", "-DskipTests"])
    build_log = (build.stdout or "") + (build.stderr or "")
    if build.returncode != 0:
        sys.exit(build.returncode)
    build_s = round(time.perf_counter() - t0, 3)

    jar = _pick_fat_jar()
    bundle = jar.stat().st_size if jar else 0

    dep = _run_mvn(["dependency:analyze"])
    dep_text = (dep.stdout or "") + (dep.stderr or "")
    unused_deps = _unused_declared_dependencies(dep_text)

    jd = _run_mvn(["jdepend:generate"])
    if jd.returncode != 0:
        print(jd.stderr[:2000], file=sys.stderr)

    cycle_count = _jdepend_cycle_count()
    unused_imp = _unused_imports(build_log)

    report = {
        "schema": "platform_dependency_metrics/v1",
        "language": "java",
        "repo_root": str(ROOT),
        "metrics": {
            "bundle_size_analysis": {
                "bundle_size_bytes": bundle,
                "primary_jar": jar.name if jar else None,
            },
            "unused_dependency_detection": {
                "unused_dependency_count": unused_deps,
            },
            "unused_import_count": {
                "unused_import_count": unused_imp,
            },
            "build_performance": {
                "build_duration_seconds": build_s,
                "build_kind": "maven_clean_package",
            },
            "dependency_graph_analysis": {
                "circular_dependency_count": cycle_count,
            },
        },
        "categories": categories,
    }

    out.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(report, indent=2))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
