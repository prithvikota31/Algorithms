# Google L4 Coding Interview Revision Notes

Suggested email subject: **Google L4 Coding Revision - Patterns, Invariants, and Interview Scripts**

These notes combine the interview discussions, repository solutions, earlier chats, and the detailed mistake log from `google-l4-prep.md`.

Do not memorize complete solutions. Memorize what each data structure represents and why its update is correct.

---

## 1. The interview answer structure

Start with this:

> My approach is **X**. The key invariant is **Y**. I will use **Z** data structure to maintain that. This should take **O(...)** time and **O(...)** space. I will start coding and call out implementation details as I introduce them.

Then use this transition:

> I think the high-level approach is clear. Unless you see an issue with this direction, I will code it and refine the bookkeeping as I go.

Pause briefly. If the interviewer does not interrupt, start coding.

### What must be clear before coding

1. **Approach:** BFS, DFS, heap, dynamic programming, sweep line, and so on.
2. **Invariant:** the fact that remains true throughout the algorithm.
3. **Data structure:** what it stores, not merely its type.
4. **Update:** how each input changes that stored state.
5. **Complexity:** why each item is processed only a limited number of times.

### What can wait until coding

Variable names such as `left`, `right`, `count`, `current`, and `previous` are bookkeeping. Explain them when they appear.

Example:

> I need `visited` here so each cell enters the queue at most once.

### End every solution with

1. One successful dry run.
2. One failure or unreachable case.
3. One boundary case.
4. A final complexity statement.

---

## 2. Core definitions

### Invariant

An invariant is a fact that stays true while the algorithm runs.

Example:

> Every grid cell in the BFS queue is reachable from the source.

### Bookkeeping

Bookkeeping is state used to maintain the algorithm.

Examples include:

- `visited`
- parent pointers
- queue contents
- current window boundaries
- running counts
- best values seen so far

### State meaning

For every data structure, be able to finish this sentence:

> This structure stores ...

Examples:

- `distance[node]` stores the shortest known distance from the source.
- `parent[node]` stores the predecessor used to reach that node.
- `best[value]` stores the best chain ending with that value.
- A min-heap of size `K` stores the current `K` largest candidates.

---

## 3. Complexity quick reference

| Pattern | Typical time | Typical extra space |
|---|---:|---:|
| BFS or DFS | `O(V + E)` | `O(V)` |
| Grid BFS or DFS | `O(R*C)` | `O(R*C)` |
| Multi-source BFS | `O(V + E)` | `O(V)` |
| Dijkstra with a heap | `O((V + E) log V)` | `O(V + E)` |
| 0-1 BFS | `O(V + E)` | `O(V)` |
| Topological sort | `O(V + E)` | `O(V + E)` |
| Union-Find | `O((V + E) alpha(V))` | `O(V)` |
| Size-`K` heap over `N` items | `O(N log K)` | `O(K)` |
| Sort plus scan | `O(N log N)` | depends on sort |
| Segment tree processing | `O(N log M)` | `O(M)` |
| Postorder tree DFS | `O(N)` | `O(H)` |

Important qualifications:

- Path output adds time proportional to the returned path data.
- Recursive expansion is output-sensitive.
- Recursion does not automatically mean exponential time.
- Heap loops can still be `O(N log N)` when each item enters and leaves once.

---

## 4. Graph and grid patterns

### 4.1 Grid reachability

**Trigger:** Can a source reach a target through allowed cells?

**Approach:** Treat cells as graph nodes and run BFS or DFS.

**Data structures:**

- Queue: reachable cells waiting to be processed.
- `visited`: cells already discovered.

**Invariant:**

> Every queued cell is reachable from the source.

Mark visited when enqueueing. This prevents duplicate queue entries.

**Complexity:** `O(R*C)` time and `O(R*C)` space.

**Memory line:** Flood from the source and check whether the flood reaches the target.

---

### 4.2 Distance to the nearest source

**Trigger:** Every node or cell wants its distance to the nearest one among many sources.

**Approach:** Multi-source BFS.

**Data structures:**

- Queue initially contains every source at distance zero.
- Distance array or matrix stores first discovery distance.

**Invariant:**

> The first time a node is reached, its distance is the minimum over all sources.

**Complexity:** `O(V + E)` or `O(R*C)`.

**Memory line:** Reverse the search and expand from every source together.

---

### 4.3 Corruption spreads `K` hops, then find a safe path

**Approach:** Two BFS runs.

1. Multi-source BFS from all corrupt nodes, stopping expansion at distance `K`.
2. Ordinary BFS from the source through safe nodes only.

**State:**

- `dangerDistance[node]`: distance from the nearest corruption.
- `pathDistance[node]`: distance from the source through safe nodes.

**Invariant:**

> The first BFS builds the danger map. The second BFS navigates the safe map.

**Complexity:** `O(V + E)` time and `O(V + E)` total storage.

---

### 4.4 Safest path

**Goal:** Maximize the minimum distance from danger along the path.

**Approach:**

1. Multi-source BFS computes each cell's distance from its nearest danger source.
2. A max-heap finds the maximum-bottleneck path.

**State:**

- `safety[cell]`: nearest-danger distance.
- `bestSafety[cell]`: largest path bottleneck found for that cell.
- Heap entry: current path bottleneck and cell.

**Update:**

```text
nextPathSafety = min(currentPathSafety, safety[nextCell])
```

**Invariant:**

> Each heap entry represents a real path whose score is its weakest cell.

When the target is removed from the max-heap, its score is optimal.

**Complexity:** `O(R*C log(R*C))`.

---

### 4.5 Router reachability

#### Shared radius

- Router is a node.
- Add an undirected edge when squared distance is within the shared radius.
- Run BFS or DFS.
- Time is `O(N^2)` because every pair may need checking.

#### Per-router radius

- The sender's radius decides the edge.
- An edge from `A` to `B` does not imply `B` to `A`.
- Scan candidate neighbors during BFS for `O(N^2)` time and `O(N)` extra space.

Use squared distances and `long`.

**Memory line:** Coordinates define the graph; the sender defines edge direction.

---

### 4.6 Time-aware flights

**Trigger:** Reaching the same node at different times changes future choices.

**Approach:** Earliest-arrival Dijkstra-style search.

**State:**

- `earliestArrival[airport]`: earliest known arrival time.
- Min-heap: airport states ordered by arrival time.

**Invariant:**

> Arriving earlier is always at least as useful because the package can wait.

An outgoing flight is usable when:

```text
departureTime >= currentArrivalTime
```

The candidate is the flight's fixed arrival time.

**Complexity:** `O((A + F) log F)` time and `O(A + F)` space.

**Trap:** A boolean visited set loses the difference between arriving at time `4` and time `10`.

---

### 4.7 Shared route for two people

**Goal:** Minimize distinct edges used by two routes to one destination.

Run BFS from Alice, Bob, and the destination.

For every possible meeting node `M`, evaluate:

```text
distAlice[M] + distBob[M] + distDestination[M]
```

Choose the minimum reachable sum.

**Invariant:**

> For a fixed meeting node, each branch should be a shortest path.

**Complexity:** `O(V + E)` because three BFS runs are still constant.

**Trap:** More than two starting people can become a Steiner tree problem.

---

### 4.8 Best cafe for all friends

**Goal:** Minimize the farthest friend's distance.

For each cafe:

1. Run BFS.
2. Verify every friend is reachable.
3. Record the maximum friend distance.

Choose the cafe with the smallest maximum.

**Trap:** Multi-source BFS gives the nearest friend, not the farthest friend.

**Complexity:** `O(C*(V + E))` when running from each cafe.

---

### 4.9 Top `N` reachable movies

**Approach:** Graph traversal plus a size-`N` min-heap.

- BFS or DFS enumerates reachable movies.
- `visited` prevents cycles.
- The heap stores the best `N` ratings seen among reachable movies.
- The root is the weakest current winner.

**Complexity:** `O(V + E + V log N)` time and `O(V + N)` space.

---

### 4.10 Teleporter paths

#### Base

- Equal connection costs mean BFS.
- Skip broken teleporters.
- `parent[node]` reconstructs the path.
- First discovery is shortest.

#### Repair-cost follow-up

- Costs are only zero or one.
- Use 0-1 BFS.
- Zero-cost relaxation goes to the deque front.
- One-cost relaxation goes to the deque back.

**Complexity:** `O(V + E)`.

**Memory line:**

```text
equal costs -> queue
0 or 1 costs -> deque
arbitrary nonnegative costs -> priority queue
```

---

### 4.11 Token translator

**Reduction:** Transitive mappings are directed graph reachability.

- Token: node.
- Mapping: directed edge.
- BFS answers reachability.
- Parent pointers return the shortest mapping chain.

The parent map can also serve as visited:

```text
parent contains token -> token was already discovered
```

**Complexity:** `O(V + E)`.

---

## 5. Ordering, dependencies, and connectivity

### 5.1 Character ordering

`x > y` means `x` appears before `y`, so create `x -> y`.

Run Kahn's topological sort:

- Adjacency list stores outgoing constraints.
- Indegree stores unmet prerequisites.
- Queue stores nodes currently free to place.

Deduplicate edges before increasing indegree.

**Complexity:** `O(V + E)`.

---

### 5.2 Unique sequence reconstruction

This is topological sorting with a uniqueness requirement.

**Invariant:**

> At every position, exactly one node must be available, and it must equal the target value.

Checks:

1. Validate every sequence value, including singleton sequences.
2. Deduplicate repeated edges.
3. Require `queue.size() == 1` at each step.
4. Compare each removed node with the target sequence.

**Complexity:** `O(V + E)`.

---

### 5.3 Sentence similarity

**Trigger:** Similarity is transitive and symmetric.

Use Union-Find:

- `parent[word]`: component representative.
- `size[root]`: component size for balanced union.

Two unequal words are similar only when both exist and have the same root.

Identical unknown words are already equal and need no Union-Find entry.

**Complexity:** Almost linear, `O((P + N) alpha(W))`.

---

### 5.4 Recipes and prerequisites

Use a dependency graph and indegrees:

- Ingredient or recipe availability removes prerequisites.
- A recipe becomes available when its indegree reaches zero.

This is another Kahn-style process.

---

## 6. Tree patterns

### 6.1 Build a tree from parent-child pairs

**Data structures:**

- `nodes[value]`: the unique node object for that value.
- `childValues`: every value that appeared as a child.

For each pair:

1. Create or reuse both nodes.
2. Connect parent to child.
3. Mark the child value.

The root is the only value never seen as a child.

**Invariant:**

> The map owns node identity, so input order does not matter.

**Complexity:** `O(R)` expected time and `O(V)` space.

Validate multiple roots, cycles, repeated edges, and multiple parents when required.

---

### 6.2 Merge N-ary trees

**Approach:** DFS plus a child-name map.

At matching nodes:

1. Combine values.
2. Map the first node's children by name.
3. Recursively merge matching children.
4. Keep unmatched children from both sides.

**Invariant:**

> Children with the same name represent the same logical position.

**Complexity:** `O(N)` expected time.

---

### 6.3 Root-to-leaf paths with a property

Carry two kinds of downward state:

- Summary state, such as maximum ancestor value.
- Path state, stored as a list used like a stack.

Push on entry. Pop on exit. Copy the path only when producing output.

**Complexity:** `O(N + S)` time, where `S` is returned path data.

**Trap:** Use `long` sentinels when an `int` node may equal `Integer.MIN_VALUE`.

---

### 6.4 Longest all-one path

This is the tree-diameter pattern restricted to valid nodes.

Postorder returns:

```text
longest downward one-chain from this node
```

At each one-node:

```text
candidateThroughNode = leftArm + 1 + rightArm
return 1 + max(leftArm, rightArm)
```

For actual nodes, store arm lengths first. Reconstruct only once from the best turning node.

**Complexity:** `O(N)` time.

---

### 6.5 Remove leaves in rounds

Do not repeatedly delete and rescan.

Ask:

> In which round will this node disappear?

- Leaf round is `0`.
- Parent round is `1 + max(child rounds)`.
- Postorder is required because children answer first.

Group node values by computed round.

**Memory line:** Repeated boundary removal often becomes distance from the boundary.

---

### 6.6 Best root for a binary tree

When rooting an undirected tree:

- Root children count equals its degree.
- Every other node's children count equals `degree - 1`.

Therefore:

- Any node with degree greater than `3` makes the answer impossible.
- A valid root must have degree at most `2`.

For minimum height, find diameter endpoints with BFS. A node's height is the maximum distance to either endpoint.

---

## 7. Dynamic programming patterns

### 7.1 Exact adjacent difference of one

**Goal:** Longest subsequence where each next value is exactly one larger.

General DP:

```text
dp[i] = best chain ending at index i
```

Optimization:

```text
bestLength[value] = best chain ending with this value
bestLength[x] = max(bestLength[x], bestLength[x - 1] + 1)
```

**Complexity:** `O(N)` expected time and `O(N)` space.

---

### 7.2 Reconstruct that subsequence

Use:

- `bestValueIndex[value]`: index ending the best chain for that value.
- `length[i]`: best chain length ending at index `i`.
- `parent[i]`: predecessor selected for index `i`.

Why all three?

- The map finds the best predecessor in `O(1)`.
- `length` compares chain quality.
- `parent` preserves historical choices after the map changes.

**Memory line:** Score chooses the best state; parent remembers how that state was formed.

---

### 7.3 Difference at most `D`

For value `x`, legal predecessor values lie in:

```text
[x - D, x - 1]
```

Use a segment tree:

- Stored value: best chain length ending in that value range.
- Query: maximum in the predecessor range.
- Update: best chain ending exactly at `x`.

**Complexity:** `O(N log M)` time.

Use coordinate compression for large or negative values.

---

### 7.4 Longest non-decreasing contiguous subarray

Base:

- Extend when `nums[i] >= nums[i - 1]`.
- Reset when the condition fails.
- Track the longest run.

**Complexity:** `O(N)` time and `O(1)` space.

One-change follow-up:

- `left[i]`: longest valid run ending at `i`.
- `right[i]`: longest valid run starting at `i`.
- Bridge around changed index `i` when `nums[i - 1] <= nums[i + 1]`.

**Complexity:** `O(N)` time and `O(N)` space.

---

### 7.5 Mouse jump maximum score

A jump to index `j` gives `nums[j]` for every crossed unit gap.

For each gap, choose the maximum landing value to its right.

Scan right to left:

- Maintain the suffix maximum.
- Add it once for every gap.

**Complexity:** `O(N)` time and `O(1)` space.

**Memory line:** Rewrite a jump score as contributions from the gaps it crosses.

---

## 8. Heaps, streams, and rolling state

### 8.1 Top `K` integers

Use a min-heap of size `K`.

**Meaning:**

> The heap stores the `K` largest values seen so far.

The root is the weakest winner and the current `K`-th largest.

Insert every value. Pop when size exceeds `K`.

**Complexity:** `O(log K)` per insert and `O(K)` space.

---

### 8.2 Top `K` users by final frequency

Two phases:

1. Build `user -> count`.
2. Run size-`K` min-heap over the unique users.

**Complexity:** `O(N + U log K)`.

Clarify tie-breaking.

---

### 8.3 Continuous top `K` frequencies

A user outside the top `K` may later enter it. Keep every user's count.

Use:

- Map: `user -> count`.
- TreeSet: all users ordered by count and tie-breaker.

For an update:

1. Remove the user's old ranking.
2. Increment the count.
3. Reinsert the user.

Never mutate a key while it remains inside a TreeSet comparator ordering.

**Complexity:** `O(log U)` per message and `O(K)` to read the top `K`.

---

### 8.4 Product of the last `K` values

Use:

- Queue: exactly the last `K` values.
- `nonZeroProduct`: product of non-zero window values.
- `zeroCount`: zeros currently inside the window.

When adding:

1. Evict first when the queue already has `K` values.
2. Divide out a non-zero outgoing value.
3. Decrement for an outgoing zero.
4. Enqueue every incoming value, including zero.
5. Multiply non-zero input or increment `zeroCount`.

Query:

- Fewer than `K` values -> `null`.
- Any zero -> `0`.
- Otherwise -> `nonZeroProduct`.

**Complexity:** `O(1)` per operation and `O(K)` space.

---

### 8.5 Music shuffle with cooldown

Use:

- Queue: last `K` played songs in order.
- Set: the same songs for membership checks.

**Invariant:**

> The blocked set equals exactly the songs in the cooldown queue.

Base selection scans unblocked songs and chooses uniformly.

Weighted selection:

1. Sum available weights.
2. Draw a target in `[0, totalWeight)`.
3. Walk cumulative weights.
4. Choose the first cumulative sum greater than the target.

---

## 9. Interval and sweep-line patterns

### 9.1 Does any pair overlap?

Sort intervals by start.

Maintain `previousEnd` or `maxEnd`.

For closed intervals:

```text
currentStart <= previousEnd -> overlap
```

Equality counts because both intervals contain the touching point.

**Complexity:** `O(N log N)`.

---

### 9.2 Maximum simultaneous overlap

Sort by start. Keep active end times in a min-heap.

For each interval:

1. Remove every end strictly less than the current start.
2. Add the current end.
3. Maximize heap size.

For closed intervals, an end equal to the current start remains active.

Use `while`, not `if`, when the heap must represent only active intervals.

Each interval enters once and leaves at most once.

**Complexity:** `O(N log N)`.

---

### 9.3 Incremental rectangle existence

An axis-aligned rectangle exists when two distinct `x` columns share the same pair of `y` values.

Use:

- `x -> set of y` values.
- `(y1, y2) -> set of x` columns containing both heights.
- Cached boolean answer.

When adding `(x, y)`, pair `y` with every earlier height in column `x`.

The second distinct column for a pair creates a rectangle.

**Complexity:** `O(K)` update, where `K` is current heights at `x`; query is `O(1)`.

---

### 9.4 Vertical equal-area split

Each rectangle contributes active height across its horizontal span.

Create events:

```text
startX -> +height
endX   -> -height
```

Between event coordinates, active height is constant. Area grows linearly.

Sweep left to right:

1. Compute strip area.
2. If half the total lies inside, interpolate the exact `x`.
3. Otherwise consume the strip.
4. Apply all events sharing the coordinate.

**Complexity:** `O(N log N)` time and `O(N)` space.

---

## 10. Strings, tries, and encoded hierarchies

### 10.1 Recursive placeholders

Treat placeholder references as an implicit dependency graph.

Use:

- Memo: fully expanded value for each key.
- Active set: keys in the current recursive chain.

**Invariant:**

> Once cached, a key's value is fully expanded.

The active set detects cycles. It is not a permanent visited set.

Complexity is output-sensitive. It becomes exponential only when the required output is exponential.

---

### 10.2 Filesystem hierarchy

Use a trie keyed by path components.

Each node stores:

- Child map.
- Whether it is a file.
- Cached subtree size.

**Invariant:**

> A node's cached size equals the sum of every file in its subtree.

For overwrite:

```text
delta = newSize - oldSize
```

Add the delta to every node along the path.

For deletion, subtract the target subtree total from its ancestors and unlink it.

**Complexity:** `O(P)` per operation, where `P` is path components.

---

### 10.3 Maximum common line prefix across files

Use a trie where each edge is one complete line.

Insert files one at a time.

The existing nodes followed before the first new edge equal the longest prefix shared with any earlier file.

Create a fresh root for each independent invocation.

---

### 10.4 Dictionary words as subsequences

Preprocess the source:

```text
character -> sorted source positions
```

For each word character, binary-search for the first position strictly greater than the previous match.

**Invariant:**

> The processed word prefix is matched in order, ending at the stored source index.

Choosing the earliest valid position leaves the most room for later characters.

**Complexity:** `O(S + T log S)`.

---

### 10.5 Move pieces to obtain a string

Ignore blanks and match real pieces with two pointers.

Required conditions:

1. Non-blank piece order must be identical.
2. `L` may not end farther right.
3. `R` may not end farther left.

**Complexity:** `O(N)` time and `O(1)` space.

---

### 10.6 Domain leaf scores

Domains form an implicit suffix hierarchy:

```text
mail.test.com -> test.com -> com
```

For each registered domain:

1. Walk every suffix.
2. Add scores only for registered suffixes.
3. Mark registered proper suffixes as non-leaves.

Anything never marked is a leaf.

**Important:** Missing intermediate suffixes do not stop the walk.

Use:

```java
Set<String> nonLeafDomains;
scores.getOrDefault(currentDomain, 0);
```

---

## 11. Common correctness traps

1. **Visited timing:** Mark visited when enqueueing, not when dequeueing.
2. **State is not always a node:** Time-aware routing needs node plus arrival quality.
3. **Min versus max:** Multi-source BFS gives a minimum, not a fair maximum.
4. **Closed intervals:** End equal to start still overlaps.
5. **Active heap:** Remove all expired entries with `while`.
6. **Path reconstruction:** A score alone does not remember choices.
7. **Map changes:** Parent pointers preserve historical predecessors.
8. **Duplicate edges:** Deduplicate before increasing indegree.
9. **Unknown singleton constraints:** Validate every value, not only adjacent pairs.
10. **Zeros in rolling products:** Count zeros and enqueue them.
11. **Overflow:** Widen before subtraction or multiplication.
12. **Output complexity:** Include copied path or expanded text size.
13. **Static state:** Reset tries and caches between independent calls.
14. **Malformed hierarchies:** Check multiple roots, parents, cycles, and disconnected nodes.
15. **Tie-breaking:** Define deterministic behavior for heaps and ordered sets.

---

## 12. Google L4 priority order

### Tier 1: Must be automatic

1. BFS and DFS reachability.
2. Multi-source BFS.
3. Dijkstra and 0-1 BFS selection.
4. Parent-pointer path reconstruction.
5. Topological sort and cycle detection.
6. Union-Find.
7. Tree postorder DP.
8. Size-`K` heaps.
9. Sort-and-scan intervals.
10. Basic one-dimensional DP.

### Tier 2: Strong L4 follow-ups

1. Maximum-bottleneck path.
2. Earliest-arrival routing.
3. Three-distance meeting point.
4. Segment tree for DP range maximum.
5. Prefix and suffix DP.
6. Sweep-line area accumulation.
7. Dynamic ranking with TreeSet.
8. Two-pass path reconstruction in trees.

### Tier 3: Understand the idea

1. Incremental rectangle detection.
2. Weighted random selection.
3. Domain suffix aggregation.
4. Large-output recursive substitution analysis.
5. Multi-terminal Steiner-tree follow-ups.

---

## 13. Thirty-minute interview plan

### Minutes 0-3

1. Restate the contract.
2. Clarify one meaningful ambiguity.
3. Give one small example.

### Minutes 3-6

1. Name the pattern.
2. State the invariant.
3. State each data structure's meaning.
4. Give complexity.

### Minutes 6-20

1. Code the base solution.
2. Introduce bookkeeping only when needed.
3. Keep types safe.
4. Handle failure states explicitly.

### Minutes 20-25

1. Dry-run a normal example.
2. Test an unreachable or invalid example.
3. Test one boundary case.

### Minutes 25-30

1. Fix discovered bugs.
2. Explain a follow-up.
3. Code the follow-up only if time allows.

Path reconstruction can take five minutes only after practicing its template. Otherwise, explain it correctly after completing the base.

---

## 14. Final spoken checklist

Before coding, say:

> My approach is ...
>
> The key invariant is ...
>
> I will use ... to store ...
>
> On each step, I update it by ...
>
> This is correct because ...
>
> The complexity is ...

While coding:

> I need this variable because ...

After coding:

> Let me test one normal case, one failure case, and one boundary case.

The main goal is not to mentally compile every line before typing. The goal is to choose the correct invariant and maintain it with clear state.

---

## 15. Personalized mistake ledger from `google-l4-prep.md`

This section records mistakes that appeared during independent rewrites. Review these before another timed session.

### 15.1 Your recurring mistake patterns

#### 1. Numeric operations happened in `int` before assignment to `long`

This appeared in geometry, graph distances, dynamic programming, and counting.

Bad:

```java
long value = (a - b) * c;
```

The subtraction and multiplication may already overflow.

Better:

```java
long value = ((long) a - b) * c;
```

Apply this to:

- squared coordinate distances
- path costs
- suffix differences
- jump scores
- range bounds such as `anchor + D`
- large subarray counts

#### 2. Initialization did not match the operation

Common examples:

- Product must start at `1`, not `0`.
- Unknown minimum distance must start at infinity, not negative infinity.
- Unreachable DP states need a large sentinel, not Java's default `0`.
- Maximum friend distance starts at `0`, not `-1`.

Ask:

> What is the identity or unreachable value for this operation?

#### 3. State was updated in the wrong order

Examples:

- Evict the outgoing product value before multiplying the incoming value.
- Check whether the area target lies inside a strip before consuming the strip.
- Remove a TreeSet ranking before changing the value used by its comparator.
- Update `dist[neighbor]` together with the parent during relaxation.

Ask:

> Which old state must be removed before the new state is added?

#### 4. A structure was used with the wrong meaning

Examples:

- Reading neighbors from a ratings map instead of an adjacency map.
- Storing a `Character` key as an `Integer`.
- Using a raw next value instead of the DP answer at that next state.
- Treating a pointer as a character instead of calling `charAt(pointer)`.

Before coding, say:

> This map stores ...
>
> This array index means ...
>
> This queue entry is ordered as ...

#### 5. Boundary conditions were reversed or incomplete

Examples:

- Closed intervals use `end < nextStart` for expiration.
- Binary-search `high` starts at `size - 1`.
- A bridge after one replacement needs `leftValue <= rightValue`.
- A valid upper range advances while `value <= upperBound`.
- Exact-boundary area checks need `>=`, not `>`.

Test equality explicitly.

#### 6. Guards came after unsafe access

Examples:

- Reading the last array element before checking empty input.
- Starting BFS from node `0` before checking an empty graph.
- Handling `source == destination` before rejecting a broken endpoint.
- Calling `graph.get(node)` when the node may be a sink.

Use this order:

1. Validate null and size.
2. Validate forbidden endpoints.
3. Handle trivial cases.
4. Enter the main algorithm.

#### 7. Parent reconstruction direction was wrong

Reconstruction normally walks backward.

Store:

```text
parent[child] = predecessor
```

Then:

1. Start at the destination.
2. Follow parents to the source.
3. Reverse the result.

Check reachability before reconstructing.

#### 8. Nodes or edges were incompletely registered

Examples:

- Topological sort omitted source-only or singleton nodes.
- Duplicate edges incremented indegree more than once.
- Sequence validation inspected only adjacent pairs.
- A graph builder allocated empty lists but never populated them.

Register every node first when singleton or isolated nodes matter.

#### 9. Mutable object ordering was changed in place

A TreeSet assumes an element's ordering key remains stable while stored.

For live rankings:

1. Remove the old ranking.
2. Update the count.
3. Reinsert the new ranking.

#### 10. Implementation complexity did not match the claim

Examples:

- Building every router edge uses `O(N^2)` space.
- Copying every path contributes output-sized work.
- Allocating a character array is not `O(1)` extra space.
- Dynamic output expansion cannot be bounded only by input length.

State complexity for the code actually written.

---

### 15.2 High-risk problem flashcards

#### Router signal

- Do not use `else if` when source may equal destination.
- Mark visited when enqueueing.
- Widen coordinate arithmetic before squaring.
- A full adjacency list may require `O(N^2)` space.

#### Shared route

- Keep heap entries consistently ordered as `(distance, node)`.
- Use `long` for weighted distances.
- Do not use an arbitrary small infinity sentinel.

#### Filesystem

- Apply the size delta exactly once to every path node.
- File identity cannot depend on whether a node currently has children.
- Root deletion needs a separate reset case.

#### Exact-difference subsequence

- The answer is the maximum over all ending states, not only the last index.
- Compute differences with `long`.
- A `Map<Long, ...>` must be queried with `long` expressions.
- Reverse the parent chain before returning it.

#### Product stream

- Initialize the product to `1`.
- Evict before multiplying the incoming value.
- Enqueue zeros as real window values.
- Keep a separate zero count.
- For dynamic `K`, the leading `1` is the empty-prefix identity.

#### Rectangle points

- Decode `x` and `y` from different fields.
- A diagonal requires different `x` and different `y`.
- Incremental y-pairs must be normalized.
- Distinct x-columns require a set, not a raw occurrence count.

#### Vertical area split

- Sort events by `x`.
- Check whether the target lies inside the current strip before consuming it.
- Use `areaSoFar + stripArea >= targetArea`.
- Apply all events sharing one coordinate together.
- Use `long` for widths and active heights.

#### Longest non-decreasing subarray

- Guard null, empty, and short arrays first.
- Build prefix and suffix lengths from input comparisons.
- Consider left-only, right-only, and full-bridge replacements.

#### Dictionary subsequence

- Use `char` for `Map<Character, ...>` lookups.
- Binary-search bounds are `[0, size - 1]`.
- Return the source position, not the position-list index.

#### Logger rate limiter

- Update the timestamp only when a message actually prints.
- Suppressed messages must not extend the cooldown.

#### Music shuffler

- The queue and set must contain the same cooldown songs.
- Weighted selection stops at the first cumulative sum above the target.
- Forgetting `break` biases every pick toward the last song.

#### Intervals

- Use `Integer.compare`, not subtraction, in comparators.
- Decide whether touching endpoints overlap before choosing `<` or `<=`.
- Use `while` to remove every expired active interval.

#### Top K

- A comparator returns a negative, zero, or positive integer.
- Validate `K` before collecting results.
- Batch frequency and live frequency require different state.

#### Mouse jump

- Guard input before indexing.
- Keep score multiplication and accumulation in `long`.
- The linear solution sums one suffix maximum per gap.

#### Topological ordering

- Register every endpoint and singleton.
- Increment the dependent node's indegree.
- Deduplicate edges only when indegree increments are also deduplicated.
- Use an empty neighbor collection for sink nodes.

#### Time-aware routing

- Missing adjacency means no outgoing flights, not an exception.
- Unknown arrival time starts at infinity.
- Store `parent[child] = currentAirport`.
- Include the source after reversing the reconstructed path.

#### Recursive placeholders

- Continue after copying an ordinary character.
- Extract the key with `substring(open + 1, close)`.
- Preserve delimiters for unknown placeholders.
- Return the fully expanded cached value on a cache hit.

#### Move pieces

- Each pointer reads its own string.
- Compare `charAt(pointer)`, not the pointer integer.
- Piece order and movement direction are separate checks.

#### Connected one-components

- A root-valued `1` can start a component.
- A `0` node ends the current component.
- Collection must stop at every `0` boundary.

#### Remove adjacent pairs

- Start the StringBuilder stack empty.
- Read input from the original string.
- Pop with index `length - 1`.
- With a write pointer, survivors occupy `[0, top)`.

#### Array jump take-or-skip

- Size DP for every referenced state.
- Taking uses the future DP result, not the next raw value.
- Record decisions separately for path reconstruction.
- Use `long` for scores and jump arithmetic.

#### Arithmetic difference subarrays

- Compute differences in `long`.
- A direction change starts a new length-two run.
- `streak` counts valid subarrays ending at the current index.

#### Triples within `D`

- Reduce pairwise constraints to `max - min <= D`.
- High pointers move while values remain `<= anchor + D`.
- Use the correct pointer for each array.
- Normalize tie ownership so each triple is counted once.

#### Best cafe

- Reachability is only a filter.
- Score a cafe by its farthest friend.
- Minimize that maximum.
- Initialize a cafe's maximum distance to `0`.

#### Top reachable movies

- Traverse neighbors from the adjacency map.
- Mark visited when enqueueing.
- The size-`N` heap contains only reachable movies.

#### Teleporters

- Reject broken endpoints before the trivial same-node case.
- A successful 0-1 relaxation updates distance and parent.
- Zero-cost moves go to the front; one-cost moves go to the back.

#### Broadcast propagation

- Actually call the graph-building helper.
- Use boolean visited state consistently.
- Compute squared distances with `long`.

#### Union-Find

- Compare `find(word1)` with `find(word2)`.
- Attach component roots, not the original words.
- Clear instance maps between independent public calls.

#### Sequence reconstruction

- Validate every value, including singleton sequences.
- Increment indegree only when a new edge is inserted.
- Require exactly one available node at every position.

#### Translator

- Unknown adjacency is an empty set.
- Parent pointers also serve as visited state.
- Reconstruct backward, then reverse.

#### F1 race DP

- Arrays indexed by lap count must be sized by lap count.
- Initialize unreachable minima to a large `long` sentinel.
- Keep degrading lap times and totals in `long`.

#### Domain leaf scores

- The non-leaf set stores strings, not integers.
- Missing intermediate suffixes do not stop the suffix walk.
- Use `getOrDefault` for unregistered suffixes.

---

### 15.3 Additional solved patterns from the prep file

#### Logger rate limiter

```text
lastPrintedTime[message] = latest successful print time
```

Print when unseen or when:

```text
timestamp - lastPrintedTime >= window
```

Update only after a successful print.

**Complexity:** `O(1)` expected time per event and `O(M)` space.

#### Remove adjacent opposite-case pairs

The surviving prefix behaves like a stack.

- Current character cancels only with the last survivor.
- Pop exposes possible cascading cancellation.
- A write pointer uses `[0, top)` as the cleaned prefix.

**Complexity:** `O(N)` time.

#### Array jump: take or skip

Define:

```text
dp[i] = maximum score obtainable starting at index i
```

Choices:

```text
skip = dp[i + 1]
take = arr[i] + dp[i + arr[i]]
```

Compute right to left. Record `took[i]` for reconstruction.

**Complexity:** `O(N)` time and `O(N)` space.

#### Arithmetic adjacent-difference subarrays

Maintain:

```text
streak = number of valid subarrays ending at this index
```

- Same `+1` or `-1` direction -> increment.
- New valid direction -> reset to `1`.
- Invalid difference -> reset to `0`.

Add `streak` to the answer at every index.

**Complexity:** `O(N)` time and `O(1)` space.

#### Triples from three sorted arrays

Three values satisfy all pairwise limits exactly when:

```text
maximum - minimum <= D
```

Anchor each array as the minimum in separate passes. Maintain monotonic valid ranges in the other two arrays.

Use a tie priority so each triple has one owner.

**Complexity:** `O(A + B + C)` time, excluding returned triples.

#### Broadcast signal propagation

Build the directed geometric graph once.

Run DFS or BFS from every possible source with fresh visited state.

**Complexity:** `O(N^3)` worst-case time and `O(N^2)` graph space.

#### Currency arbitrage

Transform each exchange rate:

```text
weight = -log(rate)
```

A profitable product becomes a negative-sum cycle.

Run Bellman-Ford with every initial distance set to zero. This acts like a virtual source reaching every currency.

An improvement on the `V`-th relaxation round proves a negative cycle.

**Complexity:** `O(V*E)`, or `O(V^3)` for a dense rate matrix.

#### F1 single-tyre race

For each tyre, simulate its geometric lap sequence and keep the minimum complete-race sum.

**Complexity:** `O(T*L)` time and `O(1)` extra space.

#### F1 tyre-change race

Precompute:

```text
best[stintLength] = cheapest fresh-tyre stint of that length
```

Then use partition DP:

```text
dp[laps] = minimum cost to complete exactly that many laps
```

Try every final-stint length.

**Complexity:** `O(T*L + L^2)` time and `O(L)` space.

---

### 15.4 Remaining Phase 1 gaps from the prep tracker

The prep file records `51 / 57` solved.

#### Learn and implement

1. Nested function-expression parser.
2. Infix and postfix expression evaluation.
3. Dependency-cycle enumeration using strongly connected components.
4. Microwave keypad optimization.

#### Understand only unless time remains

1. Alternating-color tree rerooting.
2. Counting squares from horizontal and vertical segments.

For dependency cycles, learn Kosaraju or Tarjan after ordinary topological cycle detection is automatic.

---

### 15.5 Personalized pre-submit checklist

Before saying the solution is complete, check:

1. Did I guard null and empty input before indexing?
2. Did I widen arithmetic before subtraction or multiplication?
3. Does every map key use the declared boxed type?
4. Does every queue or heap entry use one consistent field order?
5. Did I update all state during relaxation?
6. Did I register singleton and sink nodes?
7. Did I deduplicate edge counts consistently?
8. Did I choose `<` versus `<=` from the exact boundary contract?
9. Did I reconstruct with child-to-parent links and reverse?
10. Did I reset mutable instance state between calls?
11. Does my complexity match the structure I actually built?
12. Did I test a missing key, duplicate input, zero, extreme integer, and unreachable case?

---

## 16. Top 10 problems for one final week

This list optimizes for reusable pattern coverage and the mistakes recorded during revision. Treat a base problem and its listed follow-up as one practice slot.

### 1. Grid reachability and safest path

Files:

- [GridReachability.java](2-grid-reachability/GridReachability.java)
- [MaximumSafetyPath.java](2-grid-reachability/MaximumSafetyPath.java)

Practice:

- Ordinary grid BFS.
- Multi-source BFS for danger distances.
- Maximum-bottleneck Dijkstra with a max-heap.

### 2. Teleporter shortest path

File: [TeleporterShortestPath.java](33-teleporter-shortest-path/TeleporterShortestPath.java)

Practice:

- BFS parent reconstruction.
- Excluding blocked nodes.
- 0-1 BFS relaxation with a deque.

### 3. Time-aware package routing

File: [TimeAwarePackageRouting.java](5-time-aware-routing/TimeAwarePackageRouting.java)

Practice:

- Earliest-arrival state.
- Min-heap ordering.
- Stale-entry checks.
- Child-to-parent path reconstruction.

### 4. Sequence reconstruction

File: [SequenceReconstruction.java](39-sequence-reconstruction/SequenceReconstruction.java)

Practice:

- Topological sorting.
- Unique-order detection.
- Edge deduplication.
- Singleton and unknown-value validation.

### 5. Transitive sentence similarity

File: [SentenceSimilarityTwo.java](38-sentence-similarity-transitive/SentenceSimilarityTwo.java)

Practice:

- Union-Find.
- Path compression.
- Union by size.
- Resetting state between independent calls.

### 6. Longest path through one-valued tree nodes

File: [LongestOnePathNodes.java](20-connected-components-binary-tree/LongestOnePathNodes.java)

Practice:

- Postorder tree DP.
- Combining two downward arms.
- Finding a turning point.
- Reconstructing an actual tree path.

### 7. Longest subsequence with constrained adjacent difference

File: [LongestSubseqAdjacentDiff.java](12-longest-inc-subseq-adjacent-diff/LongestSubseqAdjacentDiff.java)

Practice:

- Quadratic subsequence DP.
- Value-based `O(N)` optimization.
- Best-ending-index maps.
- Parent reconstruction.
- Segment-tree range maximum as an advanced follow-up.

### 8. Interval progression

Folder: [15-interval-overlap](15-interval-overlap)

Practice:

- Pair overlap.
- Any-pair overlap after sorting.
- Merge and insert intervals.
- Maximum simultaneous overlap with a min-heap.
- Closed-endpoint equality rules.

### 9. Top K from a stream

File: [TopKFromStream.java](13-top-k-from-stream/TopKFromStream.java)

Practice:

- Size-`K` min-heap.
- Batch frequency counting.
- Continuous ranking with a map and TreeSet.
- Comparator and tie-breaking rules.

### 10. Recursive placeholder substitution

File: [RecursivePlaceholderSubstitution.java](9-recursive-placeholder-substitution/RecursivePlaceholderSubstitution.java)

Practice:

- Recursive parsing.
- Memoization.
- Current-path cycle detection.
- Output-sensitive complexity.

### Seven-day schedule

1. **Day 1:** Grid reachability and safest path.
2. **Day 2:** Teleporters and time-aware routing.
3. **Day 3:** Sequence reconstruction and Union-Find.
4. **Day 4:** Longest one-path and constrained subsequence DP.
5. **Day 5:** Interval progression and Top K.
6. **Day 6:** Recursive placeholders, then redo the weakest problem.
7. **Day 7:** Complete two timed 45-minute mocks without notes.

For every attempt:

1. State the approach.
2. State the invariant.
3. Explain what each data structure represents.
4. Explain the update.
5. Give time and space complexity.
6. Code without reading the solution.
7. Test one normal, one failure, and one boundary case.
