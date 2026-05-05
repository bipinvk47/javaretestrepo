package com.bejava.whitebox.coverage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/** Loads JaCoCo XML report {@code report/counter} rows into {@link AllUsesCoverageSummary}. */
public final class JacocoXmlReportLoader {

    private JacocoXmlReportLoader() {}

    public static AllUsesCoverageSummary load(Path jacocoXml) throws Exception {
        Objects.requireNonNull(jacocoXml, "jacocoXml");
        try (InputStream in = Files.newInputStream(jacocoXml)) {
            return load(in);
        }
    }

    public static AllUsesCoverageSummary load(InputStream jacocoXml) throws Exception {
        Objects.requireNonNull(jacocoXml, "jacocoXml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        Document doc = factory.newDocumentBuilder().parse(jacocoXml);
        Element root = doc.getDocumentElement();
        if (!"report".equals(root.getTagName())) {
            throw new IllegalArgumentException("Expected root element <report>");
        }
        AllUsesCoverageSummary fromPackages = summarizeCounters(root.getElementsByTagName("counter"), "package");
        if (hasTotals(fromPackages)) {
            return fromPackages;
        }
        AllUsesCoverageSummary fromGroup = summarizeCounters(root.getElementsByTagName("counter"), "group");
        if (hasTotals(fromGroup)) {
            return fromGroup;
        }
        return summarizeCounters(root.getElementsByTagName("counter"), "report");
    }

    static AllUsesCoverageSummary summarizeCounters(NodeList counters, String parentTag) {
        CounterTriple instructions = CounterTriple.EMPTY;
        CounterTriple branches = CounterTriple.EMPTY;
        CounterTriple lines = CounterTriple.EMPTY;
        for (int i = 0; i < counters.getLength(); i++) {
            Element c = (Element) counters.item(i);
            String parentName = c.getParentNode().getNodeName();
            if (!parentTag.equals(parentName)) {
                continue;
            }
            String type = c.getAttribute("type");
            long missed = Long.parseLong(c.getAttribute("missed"));
            long covered = Long.parseLong(c.getAttribute("covered"));
            CounterTriple slice = new CounterTriple(missed, covered);
            switch (type) {
                case "INSTRUCTION" -> instructions = instructions.merge(slice);
                case "BRANCH" -> branches = branches.merge(slice);
                case "LINE" -> lines = lines.merge(slice);
                default -> { /* ignore METHOD, CLASS, COMPLEXITY for this summary */ }
            }
        }
        return new AllUsesCoverageSummary(instructions, branches, lines);
    }

    private static boolean hasTotals(AllUsesCoverageSummary summary) {
        return summary.instructions().total() > 0
                || summary.branches().total() > 0
                || summary.lines().total() > 0;
    }
}
