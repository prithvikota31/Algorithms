/*
 * ============================================================================
 * Problem 57 (Google L4 prep) - Domain Leaf Scores
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given a map from domains to integer scores, return only leaf domains. A
 * domain's total score is its own score plus the scores of every registered
 * ancestor suffix. A domain is not a leaf when another input domain exists
 * below it.
 *
 * EXAMPLES
 * --------
 *   {com=20, test.com=-10, mail.test.com=10}
 *       -> {mail.test.com=20}
 *   {z=5, x.y.z=10}
 *       -> {x.y.z=15}
 *   {a.com=10, b.org=20}
 *       -> {a.com=10, b.org=20}
 *
 * INTUITION
 * ---------
 * A parent domain is the suffix after removing the leftmost label. For each
 * domain, walk that suffix chain, add every registered score, and mark each
 * registered proper suffix as non-leaf. Anything never marked is a leaf.
 *
 * ALGORITHM
 * ---------
 * 1. For every input domain, walk from the full domain toward its suffixes.
 * 2. Add the score of each suffix that appears in the input map.
 * 3. Mark every registered proper suffix as a non-leaf domain.
 * 4. Return totals only for domains that were never marked non-leaf.
 *
 * COMPLEXITY
 * ----------
 * Time: O(C), where C is the total number of characters visited across all
 *       domain suffix chains.
 * Space: O(D), where D is the number of input domains.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DomainLeafScores {

    public Map<String, Integer> leafDomainScores(Map<String, Integer> scores) {
        Map<String, Integer> totalScores = new HashMap<>();
        Set<String> nonLeafDomains = new HashSet<>();

        for (String domain : scores.keySet()) {
            String currentDomain = domain;
            int total = 0;

            while (true) {
                total += scores.getOrDefault(currentDomain, 0);

                int dotIndex = currentDomain.indexOf('.');
                if (dotIndex == -1) {
                    break;
                }

                currentDomain = currentDomain.substring(dotIndex + 1);
                if (scores.containsKey(currentDomain)) {
                    nonLeafDomains.add(currentDomain);
                }
            }

            totalScores.put(domain, total);
        }

        Map<String, Integer> leafScores = new LinkedHashMap<>();
        for (String domain : scores.keySet()) {
            if (!nonLeafDomains.contains(domain)) {
                leafScores.put(domain, totalScores.get(domain));
            }
        }
        return leafScores;
    }

    public static void main(String[] args) {
        DomainLeafScores solution = new DomainLeafScores();

        Map<String, Integer> scores1 = new LinkedHashMap<>();
        scores1.put("test.mydomain.com", 10);
        scores1.put("mail.test.mydomain.com", 15);
        scores1.put("test.com", -10);
        scores1.put("com", 20);
        scores1.put("mydomain.com", 5);
        scores1.put("www.mydomain.com", 10);
        scores1.put("mail.test.com", 10);
        scores1.put("www.test.com", -5);
        Map<String, Integer> expected1 = new LinkedHashMap<>();
        expected1.put("mail.test.mydomain.com", 50);
        expected1.put("www.mydomain.com", 35);
        expected1.put("mail.test.com", 20);
        expected1.put("www.test.com", 5);
        check("example hierarchy", solution.leafDomainScores(scores1), expected1);

        check("missing intermediate",
                solution.leafDomainScores(scores("x.y.z", 10, "z", 5)),
                scores("x.y.z", 15));

        check("independent domains",
                solution.leafDomainScores(scores("a.com", 10, "b.org", 20, "c.net", 30)),
                scores("a.com", 10, "b.org", 20, "c.net", 30));

        check("deep chain",
                solution.leafDomainScores(scores("com", 1, "google.com", 2,
                        "mail.google.com", 3, "x.mail.google.com", 4)),
                scores("x.mail.google.com", 10));

        check("shared ancestors",
                solution.leafDomainScores(scores("com", 10, "abc.com", 20,
                        "x.abc.com", 5, "y.abc.com", 7)),
                scores("x.abc.com", 35, "y.abc.com", 37));

        check("negative scores",
                solution.leafDomainScores(scores("com", 20, "test.com", -10,
                        "mail.test.com", 5)),
                scores("mail.test.com", 15));

        check("empty", solution.leafDomainScores(new HashMap<>()), new HashMap<>());
        System.out.println("all passed");
    }

    private static Map<String, Integer> scores(Object... entries) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            result.put((String) entries[i], (Integer) entries[i + 1]);
        }
        return result;
    }

    private static void check(String name, Map<String, Integer> actual,
            Map<String, Integer> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual
                    + " expected " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
