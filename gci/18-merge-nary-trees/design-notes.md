# Problem 18 — Merge N-ary Trees: Design-Only Follow-ups

Code follow-ups live as `.java` files in this folder. The ones below are
architecture/discussion answers (usually no implementation required in an
interview).

---

## Follow-up 3 — Very large trees / memory optimization

### The problem
The base solution builds a `Map<String, Node>` of a node's children to match by
name in O(1). For a node with millions/billions of children, that per-node
HashMap is a huge memory cost.

### Optimization: sorted children + two-pointer merge
If each node's children are **sorted by name**, merge them like the merge step
of merge-sort — two pointers, no HashMap:

```
Tree1 children:  A B D F
Tree2 children:    B C D E

i -> Tree1, j -> Tree2
A < B      -> take A, i++
B == B     -> recurse-merge, i++ j++
C < D      -> take C, j++
D == D     -> recurse-merge, i++ j++
E < F      -> take E, j++
F (rest)   -> take F
=> A, B(merged), C, D(merged), E, F
```

### Complexity
| Approach            | Time            | Extra space per level |
|---------------------|-----------------|-----------------------|
| HashMap (base)      | O(N)            | O(K)  (K = max children) |
| Sorted two-pointer  | O(N)            | O(1) indices; O(height) recursion |
| Children not sorted | O(N + ΣK log K) | sorting cost added    |

### The honest caveat
If **both** billion-node trees are already fully in memory, two pointers do
**not** shrink that footprint — they only drop the auxiliary maps. The real win
needs **streaming**:

- Store trees on disk/DB with children sorted by name.
- Stream children of the current node only; merge sorted streams (external
  merge-sort style).
- Resident memory becomes `current node's children + recursion stack` instead of
  `entire tree + per-node HashMaps`.

### Interview answer (short)
> "HashMap is optimal for normal in-memory trees. For extremely large trees I'd
> avoid materializing a child lookup map — if children are sorted by name I merge
> them with two pointers, like merge-sort. If the tree is external, I stream
> children from storage so only the current portion is resident."

### Extreme case — a single node's children don't fit in memory
If even *one* node has more children than fit in RAM, the model itself must
change: you can't hold `List<Node> children` at all, so neither HashMap nor an
in-memory two-pointer merge works.

- Store children **externally** (disk/DB), **sorted by name**.
- Expose a **sorted iterator** per node: `Iterator<Node> childrenIterator()`.
- Streaming merge keeps only the *current* child from each side + the output:

```
Tree1 stream:  A -> B -> D -> F
Tree2 stream:  B -> C -> D -> E
resident:      p1 (one node), p2 (one node), current merged output
compare p1 vs p2, emit smaller / merge-on-equal, advance that iterator
```

This is exactly **external merge-sort**: don't "load the whole file then sort",
instead "read in sorted order and merge streams incrementally". Memory becomes
`O(1)` nodes per level (plus recursion stack), independent of child count.

Interview framing:
> "If a single node's children can't fit in memory, the children can't be an
> in-memory list. I'd model them as a sorted external iterator and do a streaming
> merge, keeping only the current child from each side. I wouldn't implement the
> storage layer unless asked — at that point it's a system-design discussion."

### What "stream" means here
Not Java's `Stream` — the general systems concept: **read data piece by piece,
sequentially, instead of loading it all into memory.**

```
Disk:  B C D E F G ... (1 billion)     resident: current child = B  (then C, then D, ...)
```

It behaves like an iterator — you only ever hold the current item:
```
interface ChildStream { Node next(); boolean hasNext(); }
while (stream.hasNext()) { process(stream.next()); }
```

Analogy: reading a book. ❌ photocopy all 10,000 pages into your hands first;
✅ open it and read one page at a time. Streaming is how you process data larger
than memory.

---

## Follow-up 4 — Input isn't a tree (cycles / shared references)

### The problem
The base merge relies on the **tree invariant** (no cycles, one parent). If the
inputs are graphs/DAGs with cycles or shared nodes, plain recursion loops
forever → `StackOverflowError`:

```
merge(A1,A2) -> merge(B1,B2) -> merge(A1,A2) -> ...   (cycle A -> B -> A)
```

### Fix: memoize on the PAIR of input nodes
Track already-merged input pairs and reuse the result:

```
Map<Pair<Node,Node>, Node> memo;   // (nodeFromTree1, nodeFromTree2) -> mergedNode

merged = memo.get((r1, r2));
if (merged != null) return merged;        // cycle closed -> return existing
merged = new Node(r1.name, combine(...)); // create BEFORE recursing
memo.put((r1, r2), merged);               // so children that loop back find it
// ... then merge children, attaching into `merged`
```

Creating and memoizing the node **before** recursing into children is what
actually breaks the cycle — when a descendant loops back to `(A1,A2)`, the entry
already exists and we return it instead of recursing again.

### Why a plain `visited` set of single nodes is NOT enough
We're merging **two** graphs. `A` from tree1 and `A` from tree2 are *different
objects*, and the same tree1 node can pair with different tree2 nodes across the
merge. The identity that must be unique is the **pair** `(node1, node2)`, not
either node alone.

### Interview answer
> "The base solution assumes a tree — no cycles. If the inputs are graphs I'd
> memoize on the pair of input nodes: before recursing into a `(n1, n2)` pair,
> check the memo; if present, return the already-built merged node, otherwise
> create it, store it, then recurse. Key idea: DFS on a graph needs
> visited/memoization; DFS on a tree doesn't — and here the visited key is the
> node *pair*, since we're traversing two graphs at once."

---

## Follow-up 5 — Inputs are PATHS, not trees (build + merge)

### The change
You no longer get `Node` roots. You get flat lists of paths and must build the
trees first, then merge.

```
Tree 1: /home, /home/docs, /home/docs/a.txt, /home/photos
Tree 2: /home, /home/docs, /home/docs/b.txt, /home/videos
```

### What's tested
Moving between two representations of the same hierarchical data:
`path parsing  +  trie/tree construction  +  the existing DFS merge`.
The merge logic itself is unchanged.

### Step 1 — build a tree from paths (this is a trie insert)
For each path: split on `/`, walk from root creating any missing child by name,
reusing existing children when present.

```
insert("/home/docs/a.txt"):  split -> ["home","docs","a.txt"]
  cur = root
  for name in parts:
      cur = cur.children.computeIfAbsent(name, k -> new Node(k))
```

Inserting `/home/photos` next reuses the existing `home` node and only adds
`photos` — exactly the "reuse if name matches, else create" rule, applied
within a single input.

### Step 2 — merge
Once both roots exist, call the base `mergeTrees(root1, root2)` unchanged.

### Complexity
- Build: `O(P * L)` — `P` paths, `L` avg components per path.
- Merge: `O(N)` — `N` total nodes across both trees.

### Interview answer
> "Since the input is paths, I'd first build an N-ary tree by splitting each
> path and creating missing nodes along the way — that's a trie insert. Then I
> reuse the DFS + HashMap merge from the base problem. The only new part is
> parsing paths into a tree; the merge is identical."

Implementation only needed if explicitly asked.

---

## Follow-up 6 — Merge N trees (not just two)

### The change
Input is `List<Node> trees` instead of `(root1, root2)`. Merge all of them.

```
T1: A(10)-B(5)   T2: A(20)-C(7)   T3: A(30)-B(2)-D(9)
Result: A(60) with children B(7), C(7), D(9)
```

### Naive: fold pairwise
```
result = trees[0];
for (i = 1; i < trees.size(); i++) result = mergeTrees(result, trees[i]);
```
Correct but the intermediate `result` grows as you go, so early nodes get
re-walked on every subsequent merge → worst case `O(T * N)`.

### Better: aggregate all N at once per node
Generalize the merge to take a **list** of nodes that represent the same logical
entity, and bucket their children by name in one pass:

```
Node mergeAll(List<Node> group) {          // all nodes here share a name
    Node merged = new Node(group.get(0).name, sum of values in group);
    Map<String, List<Node>> byName = new LinkedHashMap<>();
    for (Node n : group)
        for (Node c : n.children)
            byName.computeIfAbsent(c.name, k -> new ArrayList<>()).add(c);
    for (List<Node> childGroup : byName.values())
        merged.children.add(mergeAll(childGroup));
    return merged;
}
// entry: mergeAll(trees)  — all roots are the same logical root
```

### The invariant shift (this is the whole insight)
- Base problem: *merge exactly two matching nodes*.
- N-tree: *merge a **list** of nodes representing the same entity*.

Two-tree is just the N=2 case. The map value goes from a single `Node` to a
`List<Node>`, and the recursion consumes a group instead of a pair.

### Complexity
- Aggregate approach: `O(N)` total — each node is bucketed and visited once.
- Space: `O(width)` for the per-level name→list map.

### Interview answer
> "I'd generalize the merge to take a list of same-name nodes. At each level I
> bucket every tree's children into a `Map<String, List<Node>>` by name, then
> recurse once per bucket. That's O(N) and avoids the re-walking you get from
> folding `mergeTrees` pairwise. Two-tree merge is just the N=2 special case."



