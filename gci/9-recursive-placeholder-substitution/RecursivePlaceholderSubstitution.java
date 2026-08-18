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
 *   Let N = input length, R = total raw length of reachable replacements,
 *   E = total expanded-cache length, K = reachable keys, D = max depth,
 *   and L = final output length.
 *   Time : O(N + R + E + L)   Auxiliary space: O(K + E + D)
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
        //do dfs from string along with pathvisited, so it detects cycle and also computes 
        //assume keys are like nodes here
        Set<String> pathVisitedKeys = new HashSet<>();
        Map<String, String> keyCache = new HashMap<>(); //key -> final value
        return dfs(input, replacements, pathVisitedKeys, keyCache);
    }

    private String dfs(String input, Map<String, String> replacements, 
        Set<String> pathVisitedKeys, Map<String, String> keyCache)
    {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < input.length(); i++)
        {
            char ch = input.charAt(i);
            if(ch != '%')
            {
                sb.append(ch);
                continue;
            }
            //found first %
            int end = input.indexOf('%', i + 1);
            //assume we always find end//we will write if not later
            if(end == -1)
            {
                sb.append(ch); //% as normal character
                continue;
            }
            String key = input.substring(i + 1, end);
            //if replacements doesn't contains key, treat it as normal string
            if(!replacements.containsKey(key))
            {
                sb.append(input.substring(i, end + 1));
            }
            else
            {
                sb.append(dfsHelper(key, replacements, pathVisitedKeys, keyCache));
            }
            i = end;
        }
        return sb.toString();
    }

    private String dfsHelper(String key, Map<String, String> replacements, 
        Set<String> pathVisitedKeys, Map<String, String> keyCache)
    {
        
        String unchangedValue = replacements.get(key);

        if(keyCache.containsKey(key))
        {
            return keyCache.get(key);
        }
        if(pathVisitedKeys.contains(key))
        {
            throw new IllegalArgumentException(
                        "Cycle detected involving key: " + key);
        }
        pathVisitedKeys.add(key);

        String finalValue = dfs(unchangedValue, replacements, pathVisitedKeys, keyCache);

        keyCache.put(key, finalValue);
        
        pathVisitedKeys.remove(key);
        return finalValue;
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
