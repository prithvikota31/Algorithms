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
        Map<String, Node> children = new HashMap<>();
        boolean isFile;
        int totalFilesize; //for single file it's only file size, directory total size
    }

    private Node root = new Node();

    private void addFile(String path, int size)
    {
        String[] parts = splitPath(path);   // e.g. docs/photos/a.txt

        Node current = root;
        List<Node> nodePath = new ArrayList<>();
        nodePath.add(current);

        for(String part: parts)
        {
            current.children.putIfAbsent(part, new Node());
            current = current.children.get(part);
            nodePath.add(current);
        }

        // Overwrite-safe delta: new size - old size (old = 0 for a fresh file).
        int delta = 0;
        if(current.isFile)
        {
            delta = size - current.totalFilesize;
        }
        else
        {
            delta = size;
        }

        current.isFile = true;

        // Push the delta to every node on the path so ancestor totals stay correct.
        for(Node node: nodePath)
        {
            node.totalFilesize += delta;
        }
    }

    private void removeFile(String path)
    {
        String[] parts = splitPath(path);   // e.g. docs/photos/a.txt

        Node current = root;
        List<Node> nodePath = new ArrayList<>();
        nodePath.add(current);

        for(String part: parts)
        {
            current = current.children.get(part);
            if(current == null) return;   // path doesn't exist -> no-op
            nodePath.add(current);
        }

        // Deleting the root ("/") wipes the whole tree.
        if(nodePath.size() == 1)
        {
            current.totalFilesize = 0;
            current.children.clear();
            return;
        }

        // A node's cached total IS its subtree size, so this works for a single
        // file OR a whole directory: subtract it from every ancestor, then unlink.
        int delta = -current.totalFilesize;

        Node parent = nodePath.get(nodePath.size() - 2);
        String name = parts[parts.length - 1];
        parent.children.remove(name);

        nodePath.remove(nodePath.size() - 1);   // drop the removed node itself
        for(Node node: nodePath)
        {
            node.totalFilesize += delta;
        }
    }

    private int getSize(String path)
    {
        Node node = findNode(path);
        if(node == null)    return -1;
        return node.totalFilesize;
    }

    private Node findNode(String path)
    {
        Node current = root;

        String[] parts = splitPath(path);

        for(String part: parts)
        {
            current = current.children.get(part);
            if(current == null)
            {
                return null;
            }
        }
        return current;
    }


    private String[] splitPath(String path)
    {
        // Root/empty path has no components -> return an empty array so
        // findNode() lands on root itself (getSize("/") = whole-tree total).
        // Without this, "".split("/") yields [""], and findNode looks up a
        // child named "" and wrongly returns null.
        if (path == null || path.isEmpty() || path.equals("/")) {
            return new String[0];
        }

        String cleanPath = (path.charAt(0) == '/')? path.substring(1):path;
        // split() takes a String regex, not a char literal: use "/" not '/'.
        return cleanPath.split("/");
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
