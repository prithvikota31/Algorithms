/*
 * ============================================================================
 * Problem 9 (Google L4 prep) — Recursive Placeholder Substitution
 * ============================================================================
 *
 * PROMPT
 * ------
 * You are given a text containing placeholders of the form %KEY%, and a map of
 * replacements KEY -> value. Expand every placeholder in the text. A replacement
 * value may itself contain placeholders that reference OTHER keys, so expansion
 * is RECURSIVE (nested).
 *
 *   Input : a String, and a Map<String,String> of replacements.
 *   Output: the fully expanded String.
 *
 * EXAMPLE
 * -------
 *   replacements:
 *       USER -> admin
 *       HOME -> /%USER%/home
 *   input : "I am %USER%. My home is %HOME%."
 *   output: "I am admin. My home is /admin/home."
 *
 * ASSUMPTIONS (base problem)
 * --------------------------
 *   - References are ACYCLIC (cycle detection is a separate follow-up).
 *   - Placeholders are delimited by %KEY%.
 *   - An UNKNOWN placeholder (key not in the map) is left unchanged.
 *   - A lone/unmatched '%' is treated as an ordinary character.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * This is DFS + MEMOIZATION on an IMPLICIT dependency graph.
 *   HOME -> USER   (to resolve HOME, first resolve USER)
 * Resolve a key by expanding its raw value, recursively resolving any
 * placeholders inside it, then CACHE the finished string.
 *
 * Key invariant:
 *   Once resolved.get(key) is stored, it contains NO resolvable placeholders.
 * Memoization matters because many placeholders can depend on the same key.
 *
 * APPROACHES
 *   Brute force : re-scan and replace the whole string until nothing changes.
 *                 Rebuilds large strings repeatedly; loops forever on a cycle.
 *   Optimal     : resolve(key) helper with a memo, scan input once.
 *
 * COMPLEXITY (optimal)
 *   Let E = total size of all expanded values produced, D = max dependency depth.
 *   Time : O(E + outputLength)     Space : O(E + D)
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class RecursivePlaceholderSubstitution {

    /**
     * Expand every %KEY% placeholder in {@code input}, recursively resolving
     * placeholders that appear inside replacement values.
     *
     * @param input        text possibly containing %KEY% placeholders
     * @param replacements map of KEY -> value (values may contain placeholders)
     * @return the fully expanded text
     */
    public String substitute(String input, Map<String, String> replacements) {
        Map<String, String> cache = new HashMap<>();
        Set<String> currentPath = new HashSet<>();
        return expand(input, replacements, cache, currentPath);
    }

    /*
     * Scan the string.
     * When we find %KEY%, resolve KEY recursively.
     */
    private String expand(
            String text,
            Map<String, String> replacements,
            Map<String, String> cache, Set<String> currentPath) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {

            // Normal character: copy it.
            if (text.charAt(i) != '%') {
                result.append(text.charAt(i));
                continue;
            }

            // Find the ending '%' of %KEY%.
            int end = text.indexOf('%', i + 1);

            // No matching '%': treat it as a normal character.
            if (end == -1) {
                result.append('%');
                continue;
            }

            String key = text.substring(i + 1, end);

            if (replacements.containsKey(key)) {
                result.append(resolve(key, replacements, cache, currentPath));
            } else {
                // Unknown placeholder stays unchanged.
                result.append(text, i, end + 1);
            }

            // Skip everything inside %KEY%.
            i = end;
        }

        return result.toString();
    }

    /*
     * Resolve one key completely.
     * Example: HOME -> /%USER%/home -> /admin/home
     */
    private String resolve(
            String key,
            Map<String, String> replacements,
            Map<String, String> cache, Set<String> currentPath) {

        // Reuse previously resolved value.
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        if(currentPath.contains(key))
        {
            throw new IllegalArgumentException("Cycle detected involving key: " + key);
        }

        currentPath.add(key);

        String rawValue = replacements.get(key);

        // Recursively resolve placeholders inside this value.
        String resolvedValue = expand(rawValue, replacements, cache, currentPath);
        currentPath.remove(key);

        cache.put(key, resolvedValue);
        return resolvedValue;
    }

    // ---------------------------------------------------------------------
    // Quick self-test.
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        RecursivePlaceholderSubstitution sol = new RecursivePlaceholderSubstitution();

        Map<String, String> map = new HashMap<>();
        map.put("USER", "admin");
        map.put("HOME", "/%USER%/home");

        // 1) Nested expansion: HOME references USER.
        System.out.println(sol.substitute("I am %USER%. My home is %HOME%.", map));
        // expected: I am admin. My home is /admin/home.

        // 2) Single nested placeholder.
        System.out.println(sol.substitute("Path: %HOME%", map));
        // expected: Path: /admin/home

        // 3) Unknown placeholder stays unchanged.
        System.out.println(sol.substitute("Hi %NAME%", map));
        // expected: Hi %NAME%

        // 4) A bare '%' greedily pairs with the next '%', so " done by " is
        //    read as an (unknown) key and left as-is.
        System.out.println(sol.substitute("50% done by %USER%", map));
        // expected: 50% done by %USER%

        // 5) Cycle detection: A -> %B% -> %A% -> ...  (follow-up)
        Map<String, String> cyclic = new HashMap<>();
        cyclic.put("A", "%B%");
        cyclic.put("B", "%A%");
        try {
            sol.substitute("start %A% end", cyclic);
            System.out.println("no cycle detected (unexpected)");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        // expected: Cycle detected involving key: A
    }
}
