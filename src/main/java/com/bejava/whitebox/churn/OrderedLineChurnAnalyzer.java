package com.bejava.whitebox.churn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Computes ordered line churn via LCS line alignment (insert/delete unit operations). */
public final class OrderedLineChurnAnalyzer {

    private OrderedLineChurnAnalyzer() {}

    public static CodeChurnMetrics analyze(String baselineText, String revisionText) {
        Objects.requireNonNull(baselineText, "baselineText");
        Objects.requireNonNull(revisionText, "revisionText");
        List<String> baselineLines = splitLines(baselineText);
        List<String> revisionLines = splitLines(revisionText);
        int lcs = longestCommonSubsequenceLength(baselineLines, revisionLines);
        long removed = baselineLines.size() - (long) lcs;
        long added = revisionLines.size() - (long) lcs;
        return CodeChurnMetrics.fromCounts(added, removed, baselineLines.size());
    }

    static List<String> splitLines(String text) {
        String normalized = text.replace("\r\n", "\n").replace('\r', '\n');
        List<String> lines = new ArrayList<>();
        int start = 0;
        int len = normalized.length();
        while (start <= len) {
            int nl = normalized.indexOf('\n', start);
            int end = nl >= 0 ? nl : len;
            lines.add(normalized.substring(start, end));
            if (nl < 0) {
                break;
            }
            start = nl + 1;
        }
        return lines;
    }

    static int longestCommonSubsequenceLength(List<String> a, List<String> b) {
        int n = a.size();
        int m = b.size();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (Objects.equals(a.get(i - 1), b.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }
}
