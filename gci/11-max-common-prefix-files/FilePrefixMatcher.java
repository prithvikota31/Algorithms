/*
 * ============================================================================
 * Problem 11 (Google L4 prep) — Maximum Common Prefix Across Files
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Each file is an array of lines. For every pair of files, count how many
 * consecutive lines match from the beginning (a whole-line prefix, not a
 * character prefix). Return the maximum such count over all pairs.
 *
 * EXAMPLES
 *   file1 = ["hello","world","abc","xyz"]
 *   file2 = ["hello","world","abc","xyz1"]   file1&file2 share 3
 *   file3 = ["hello","world","abc2","xyz"]   file1&file3 share 2
 *   -> answer = 3
 *
 *   Fewer than two files            -> 0  (no pair to compare)
 *   All files differ on line 0      -> 0
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * A Trie, but each EDGE is one whole line instead of one character. A file is a
 * path root -> line0 -> line1 -> ...; two files that walk the same node at depth
 * k share their first k lines.
 *
 * This variant needs NO per-node counter. Insert files one at a time and, while
 * walking a file's path, count how many nodes ALREADY EXISTED:
 *   - an existing node  = a line some earlier file already laid down here,
 *   - a fresh node      = this file diverging from everything seen so far.
 * The instant you create one fresh node, every node after it on this path is
 * also fresh (a new node has no children). So the existing nodes are always a
 * contiguous prefix from the root, and their count is exactly this file's
 * longest shared prefix with any earlier-inserted file. Take the max over all
 * files -> the best pairwise prefix.
 *
 * WHY per-file "existing-node count" == max pairwise prefix: any earlier file
 * sharing k lines with the current one already created those k nodes, so the
 * walk stays on existing nodes at least k deep; and it leaves existing nodes the
 * moment it diverges from ALL earlier files. Hence it measures the longest
 * prefix shared with the best-matching earlier file.
 *
 * APPROACHES
 *   Brute force : compare every pair line-by-line. O(N^2 * L * S).
 *   Optimal     : line-Trie, count reused nodes per insert (below).
 *                 Time O(C) expected (each line hashed once), Space O(T) nodes,
 *                 where C = total characters, T = total lines across all files.
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class FilePrefixMatcher {

    private static class TrieNode {
        // Each edge represents one complete line.
        Map<String, TrieNode> children = new HashMap<>();
    }

    public static int maxCommonPrefix(List<String[]> files) {
        if (files == null || files.size() < 2) {
            return 0;
        }

        TrieNode root = new TrieNode();
        int maxPrefix = 0;

        for (String[] file : files) {
            TrieNode current = root;
            int existingPrefixLength = 0;

            /*
             * Follow the file's lines through the Trie.
             * Existing nodes represent a prefix shared with an earlier file.
             */
            for (String line : file) {
                TrieNode next = current.children.get(line);

                if (next == null) {
                    next = new TrieNode();
                    current.children.put(line, next);
                } else {
                    existingPrefixLength++;
                }

                current = next;
            }

            maxPrefix = Math.max(maxPrefix, existingPrefixLength);
        }

        return maxPrefix;
    }

    public static void main(String[] args) {
        String[] file1 = {"hello", "world", "abc", "xyz"};
        String[] file2 = {"hello", "world", "abc", "xyz1"};
        String[] file3 = {"hello", "world", "abc2", "xyz"};
        String[] file4 = {"hello", "world", "abc1", "xyz2"};

        List<String[]> files = Arrays.asList(
                file1,
                file2,
                file3,
                file4
        );

        System.out.println(maxCommonPrefix(files)); // 3
    }
}
