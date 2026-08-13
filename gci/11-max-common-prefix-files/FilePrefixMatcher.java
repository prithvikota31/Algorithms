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
    static class TrieNode
    {
        Map<String, TrieNode> links = new HashMap<>();
        boolean isEnd = false;

        public boolean isEnd()
        {
            return isEnd;
        }

        public void setEnd()
        {
            isEnd = true;
        }

        public boolean containsKey(String s)
        {
            if(links.containsKey(s))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public void setKey(String s)
        {
            links.put(s, new TrieNode());
        }

        public TrieNode getKey(String s)
        {
            return links.get(s);
        }

    }
    private static TrieNode root = new TrieNode();

    private static int addFile(String[] file) //returns count till what depth it matched for exisiting trie
    {
        TrieNode cur = root;
        int lineCount = 0;
        for(int i = 0; i < file.length; i++)
        {
            String cLine = file[i];
            if(cur.containsKey(cLine))
            {
                lineCount++;
            }
            else
            {
                cur.setKey(cLine);
            }
            cur = cur.getKey(cLine);
        }
        cur.setEnd();
        return lineCount;
    }


    public static int maxCommonPrefix(List<String[]> files) 
    {
        int maxCount = 0;
        for(int i = 0; i < files.size(); i++)
        {
            maxCount = Math.max(maxCount, addFile(files.get(i)));
        }
        return maxCount;
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
