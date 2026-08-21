/*
 * ============================================================================
 * Problem 10 (Google L4 prep) — Filesystem Path Hierarchy + Subtree Size
 * ============================================================================
 *
 * PROMPT
 * ------
 * Build a directory structure, store file sizes, and return the total size of
 * a file or folder. A folder's size is the sum of every file beneath it.
 *   Reports: https://leetcode.com/discuss/post/6982323/
 *            https://leetcode.com/discuss/interview-experience/7346486/
 *
 *   addFile(path, size)  — add (or overwrite) a file with the given size
 *   getSize(path)        — total size of that file/folder's subtree, or -1
 *   removeFile(path)     — delete a file OR a whole directory subtree ("/" wipes all)
 *
 * EXAMPLE
 * -------
 *   addFile("/docs/a.txt", 10)
 *   addFile("/docs/photos/b.jpg", 20)
 *   getSize("/docs/a.txt")   -> 10
 *   getSize("/docs/photos")  -> 20
 *   getSize("/docs")         -> 30
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: TRIE / N-ARY TREE keyed by path component (docs -> photos -> b.jpg).
 * Each node caches the total size of its whole subtree.
 *
 * Key invariant:
 *   node.subtreeSize = node's own file size + sizes of ALL descendant files
 *
 * On add/update, push the size DELTA to every node along the path, so every
 * ancestor's cached total stays correct in O(P).
 *
 * APPROACHES
 *   Brute force : store flat "fullPath -> size"; getSize scans all files and
 *                 prefix-matches. Query O(files * pathLen); prefix bugs
 *                 (/doc must not match /documents).
 *   Optimal     : path Trie + maintained subtreeSize. add/get both O(P).
 *
 * COMPLEXITY (optimal, P = number of components in the path)
 *   addFile : O(P)     getSize : O(P)     Space : O(total path components)
 *
 * DRY RUN
 *   add /docs/a.txt=10        -> root=10, docs=10, a.txt=10
 *   add /docs/photos/b.jpg=20 -> root=30, docs=30, photos=20, b.jpg=20
 *   getSize("/docs") -> 30
 *
 * IMPLEMENTED: add, overwrite (delta), getSize, removeFile (file/dir/root).
 *
 * REPORTED FOLLOW-UPS (not implemented yet)
 *   - Lazily cache folder sizes on first query instead of eager maintenance.
 *   - Prune now-empty parent folders after a delete.
 *   - Validate malformed structures: cycles, missing children, multiple parents.
 *   - Return top N collections by aggregate size when collections nest/share.
 *     https://leetcode.com/discuss/interview-question/4327785/
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class FileSystem {

    private static class Node{
        Map<String, Node> links = new HashMap<>();
        boolean isFile;
        int totalSize = 0;

        public boolean isFile()
        {
            return isFile;
        }
        public void setFile()
        {
            isFile = true;
        }

        public Node getKey(String key)
        {

            return links.get(key);

        }

        public void setKey(String key)
        {
            links.put(key, new Node());
        }

        public boolean containsKey(String key)
        {
            return links.containsKey(key);
        }
    }

    private Node root = new Node();

    public void addFile(String path, int size)
    {
        if(path.equals("/"))
        {
            throw new IllegalArgumentException("root cannot be replaced by a file");
        }

        // /docs/aaron/personal/file.txt
        Node cur = root;

        String[] pathStrings = path.split("/");
        List<Node> completePath = new ArrayList<>();
        completePath.add(cur);
        boolean targetAlreadyExists = false;

        for(int i = 0; i < pathStrings.length; i++)
        {
            if(cur.isFile())
            {
                throw new IllegalArgumentException("can't create another file under file");
            }
            String s = pathStrings[i];
            if(i == pathStrings.length - 1)
            {
                targetAlreadyExists = cur.containsKey(s);
            }
            if(cur.containsKey(s))
            {
                cur = cur.getKey(s);
            }
            else
            {
                cur.setKey(s);
                cur = cur.getKey(s);
            }

            completePath.add(cur);
        }

        int delta = 0;

        if(targetAlreadyExists && !cur.isFile())
        {
            throw new IllegalArgumentException("A directory cannot be replaced by a file");
        }

        if(cur.isFile())
        {
            delta = size - cur.totalSize;
        }
        else
        {
            delta = size;
        }


        cur.setFile();

        for(Node node: completePath)
        {
            node.totalSize += delta;
        }
    }

    public void removeFile(String path)
    {
        if (path.equals("/")) {
            root = new Node();
            return;
        }
         // /docs/aaron/personal/file.txt
        Node cur = root;

        String[] pathStrings = path.split("/");
        List<Node> completePath = new ArrayList<>();
        completePath.add(cur);

        for(int i = 0; i < pathStrings.length; i++)
        {
            String s = pathStrings[i];
            if(cur.containsKey(s))
            {
                cur = cur.getKey(s);
            }
            else
            {
                //there is no such path
                return;
            }

            completePath.add(cur);
        }

        int delta = -cur.totalSize;

        Node parent = completePath.get(completePath.size() - 2);
        parent.links.remove( pathStrings[pathStrings.length - 1]);
        completePath.remove(completePath.size() - 1);
        for(Node node: completePath)
        {
            node.totalSize += delta;
        }
    }

    public int getSize(String path)
    {
        Node node = find(path);
        if(node == null)
        {
            return -1;
        }
        else
        {
            return node.totalSize;
        }
    }

    private Node find(String path)
    {
        String[] pathStrings = path.split("/");

        Node cur = root;
        for(int i = 0; i < pathStrings.length; i++)
        {
            String s = pathStrings[i];
            if(cur.containsKey(s))
            {
                cur = cur.getKey(s);
            }
            else
            {
                return null;
            }
        }
        return cur;
    }




    // ---------------------------------------------------------------------
    // Quick self-test.
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        fs.addFile("/docs/a.txt", 10);
        fs.addFile("/docs/photos/b.jpg", 20);

        System.out.println(fs.getSize("/docs/a.txt"));   // expected: 10
        System.out.println(fs.getSize("/docs/photos"));  // expected: 20
        System.out.println(fs.getSize("/docs"));         // expected: 30
        System.out.println(fs.getSize("/"));             // expected: 30 (root)
        System.out.println(fs.getSize("/missing"));      // expected: -1

        // Aggregation with a brand-new file: docs 30 -> 35.
        fs.addFile("/docs/notes.txt", 5);
        System.out.println(fs.getSize("/docs"));         // expected: 35

        // ----- UPDATE (overwrite via delta) -----
        // a.txt 10 -> 25 pushes delta +15 to every ancestor.
        fs.addFile("/docs/a.txt", 25);
        System.out.println(fs.getSize("/docs/a.txt"));   // expected: 25
        System.out.println(fs.getSize("/docs"));         // expected: 50 (35 + 15)

        // ----- DELETE (file: subtract size, unlink leaf) -----
        // remove a.txt (25) -> delta -25 to ancestors, node unlinked.
        fs.removeFile("/docs/a.txt");
        System.out.println(fs.getSize("/docs/a.txt"));   // expected: -1 (gone)
        System.out.println(fs.getSize("/docs"));         // expected: 25 (50 - 25)

        // ----- DELETE (directory: whole subtree removed, ancestors reduced by its total) -----
        // Tree now: docs{ notes.txt=5, photos{ b.jpg=20 } }, docs total = 25.
        // The cached photos.totalFilesize (20) IS the delta -> O(P), no subtree walk.
        fs.removeFile("/docs/photos");
        System.out.println(fs.getSize("/docs/photos"));      // expected: -1 (subtree gone)
        System.out.println(fs.getSize("/docs/photos/b.jpg"));// expected: -1 (descendant gone too)
        System.out.println(fs.getSize("/docs"));             // expected: 5 (25 - 20; only notes.txt left)

        // Delete a MISSING path -> no-op.
        fs.removeFile("/nope");
        System.out.println(fs.getSize("/docs"));             // expected: 5 (unchanged)

        // ----- DELETE ROOT (whole-tree wipe: total zeroed AND children cleared) -----
        fs.removeFile("/");
        System.out.println(fs.getSize("/"));                 // expected: 0
        System.out.println(fs.getSize("/docs"));             // expected: -1 (children cleared, not orphaned)

        // ----- RE-ADD after wipe: tree rebuilds cleanly (no stale nodes/sizes) -----
        fs.addFile("/docs/x.txt", 7);
        System.out.println(fs.getSize("/docs"));             // expected: 7
        System.out.println(fs.getSize("/"));                 // expected: 7
    }
}
