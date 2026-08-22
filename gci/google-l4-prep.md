# Google L4 Interview Prep — Problem List

> Ordering is by **observed recurrence and similarity** across the public corpus (2022–2025 Google reports), not exact occurrence counts. Distinct concrete formulations are kept **separate** even when the underlying technique is the same.

**Plan:** Solve the **56 priority problems** first (Phase 1). Then, if time permits, work through the remaining problems (Phase 2).

**Progress (Phase 1): 49 / 56 solved.**

**Quality gate:** Run `./verify.ps1` from this directory before marking work complete. A checkbox records coverage; the [practice protocol](practice/README.md) measures independent recall, and the [three-year roadmap](../../three-year-engineering-roadmap.md) defines mastery and career evidence.

---

## Phase 1 — The 56 Priority Problems (solve these first)

| # | Status | Problem |
|---|--------|---------|
| 1 | ☐ | Evaluate nested function expressions such as `mul(2, add(3,5))`; follow-ups: `sub`, `div`, `pow`, decimals, spaces, and invalid expressions. |
| 2 | ☑ | Grid source-to-target reachability; follow-up: maximize a safety metric such as the minimum distance from a cat or danger source. → [reachability](2-grid-reachability/GridReachability.java), [safest-path](2-grid-reachability/MaximumSafetyPath.java) |
| 3 | ☑ | Multi-source BFS: compute distance from every grid cell to its nearest source, boundary, or special object. → [solution](3-multi-source-bfs/MultiSourceBFS.java) |
| 4 | ☑ | Router signal propagation: determine whether a source router can reach a destination through routers within transmission range; follow-up: each router has its own radius. → [uniform-range](4-router-signal-propagation/RouterSignalPropagation.java), [per-router-radius](4-router-signal-propagation/RouterRadiusReachability.java) |
| 5 | ☑ | Time-aware flight or package routing: determine whether a package can reach a destination while respecting flight departure and arrival times; return-the-path follow-up done. **<span style="color:red">Future follow-up (not solving today): reach-before-deadline — can the destination be reached with arrivalTime ≤ T; one report phrases it as returning the shortest valid path before the deadline.</span>** → [solution](5-time-aware-routing/TimeAwarePackageRouting.java) |
| 6 | ☑ | Multiple people travel to one destination and may share parts of their routes; minimize total cost or number of distinct edges used. _(2-person unweighted 3×BFS + weighted 3×Dijkstra (≈ LC 2203) both done. Multi-source intentionally NOT coded — general case is a Steiner Tree (NP-hard); only special structure is tractable, noted in file.)_ → [unweighted-BFS](6-shared-route-meeting-point/SharedRouteMeetingPoint.java), [weighted-Dijkstra](6-shared-route-meeting-point/SharedRouteMeetingPointWeighted.java) |
| 7 | ☑ | Given multiple preference or dependency orderings, construct one valid global topological ordering or determine that none exists. → [solution](7-merge-orderings-topo-sort/MergeOrderingsTopoSort.java) |
| 8 | ☑ | Given pairwise character relationships such as `a > b` and `b > c`, reconstruct a valid character order or report impossibility. → [solution](8-character-order-from-pairs/CharacterOrderFromPairs.java) |
| 9 | ☑ | Perform recursive placeholder substitution where replacement values may reference other placeholders; follow-up: detect cycles. → [solution](9-recursive-placeholder-substitution/RecursivePlaceholderSubstitution.java) |
| 10 | ☑ | Build a filesystem, URL, or path hierarchy and support queries involving descendants, prefixes, subtree properties, or aggregated values. → [solution](10-filesystem-path-hierarchy/FileSystem.java) |
| 11 | ☑ | Given files represented as arrays of lines, find the maximum common-prefix length between any pair of files. → [solution](11-max-common-prefix-files/FilePrefixMatcher.java) |
| 12 | ☑ | Find the longest increasing subsequence where adjacent difference is exactly 1; follow-up: difference at most D; follow-up: reconstruct the indices or path. → [solution](12-longest-inc-subseq-adjacent-diff/LongestSubseqAdjacentDiff.java) **<span style="color:red">TODO: revisit segment-tree implementation (LC 2407 diff≤D, `lengthDiffAtMostDOptimal`) — internalize range-max query + point update, partial-overlap recursion, and the negatives fix (offset shift vs coordinate compression).</span>** |
| 13 | ☑ | Maintain or compute the Top K elements from a stream, such as users, words, scores, or records. → [solution](13-top-k-from-stream/TopKFromStream.java) |
| 14 | ☑ | Given strings containing `L`, `R`, and `_`, determine whether the start can reach the target when `L` moves only left and `R` moves only right. → [solution](14-move-pieces-to-string/MovePiecesToString.java) |
| 15 | ☑ | Interval progression: determine whether two intervals overlap; follow-ups: whether any pair overlaps, count overlaps, insert an interval, and merge overlaps. → [overlap](15-interval-overlap/IntervalOverlap.java), [any-pair](15-interval-overlap/AnyPairOverlap.java), [max-simultaneous](15-interval-overlap/MaxSimultaneousOverlap.java), [insert](15-interval-overlap/InsertInterval.java), [merge](15-interval-overlap/MergeIntervals.java) |
| 16 | ☑ | Design a stream data structure supporting `add(x)` and querying a product, mean, or statistic over the last K values; follow-up: dynamic K. → [fixed-K](16-product-last-k-stream/LastKProduct.java), [dynamic-K](16-product-last-k-stream/ProductOfNumbers.java) |
| 17 | ☐ | Evaluate arithmetic expressions represented in infix or postfix form using stacks; follow-ups may modify the supported grammar. |
| 18 | ☑ | Merge two trees or N-ary trees by recursively combining children with matching names while following value-conflict rules. → [base solution](18-merge-nary-trees/MergeNaryTrees.java). **Follow-ups:** (1) pluggable conflict resolver → [MergeTreesConfigurable.java](18-merge-nary-trees/MergeTreesConfigurable.java); (2) immutable inputs / deep-copy → [MergeTreesImmutable.java](18-merge-nary-trees/MergeTreesImmutable.java); (3)–(6) huge/streaming trees, cycles/graphs, build-from-paths, merge-N-trees → [design-notes.md](18-merge-nary-trees/design-notes.md). |
| 19 | ☑ | Process tree leaves based on maximum ancestor values, root-to-leaf properties, or iterative leaf removal. → [base solution](19-tree-leaves-max-ancestor/TreeLeavesMaxAncestor.java) (DFS carrying `maxSoFar` downward as a summary; a root-only leaf has no ancestors and is excluded). **Follow-up:** return the full root-to-leaf paths → [TreeLeafPaths.java](19-tree-leaves-max-ancestor/TreeLeafPaths.java) (same DFS but also carries the live path as a stack — push on entry, pop on exit, snapshot a copy at a qualifying leaf). Iterative leaf removal is covered by #43/#44. |
| 20 | ☑ | Given a binary tree whose nodes contain 0 or 1, count connected components of 1 nodes or find the largest such component. → [base + largest-component-nodes](20-connected-components-binary-tree/ConnectedOneComponents.java), [all-components](20-connected-components-binary-tree/AllConnectedComponents.java), [longest-1-path-length](20-connected-components-binary-tree/LongestOnePath.java), [longest-1-path-nodes](20-connected-components-binary-tree/LongestOnePathNodes.java), [max-weighted-component](20-connected-components-binary-tree/MaxWeightedComponent.java), [graph-with-cycles](20-connected-components-binary-tree/ConnectedComponentsGraph.java) |
| 21 | ☑ | Maintain points on a 2D plane; determine whether rectangles exist and follow up by finding the maximum rectangle area. → [base](21-rectangle-points/RectangleExists.java), [O(1)-query-follow-up](21-rectangle-points/RectangleExistsIncremental.java), [max-area-follow-up](21-rectangle-points/MaxRectangleArea.java) |
| 22 | ☑ | Given rectangles on a plane, find a vertical line that divides their total area equally; variation: rectangles may overlap. → [sweep line, overlaps counted separately](22-rectangle-area-split/VerticalAreaSplit.java). Each rectangle becomes `+height` at `x1` and `-height` at `x2`; between events the active height is constant so the cut inside a strip is a direct division. **<span style="color:red">Union-area follow-up (overlap counted ONCE) marked optional/conceptual only (not implemented): needs sweep-line + coordinate compression + segment tree tracking active y-coverage — niche geometry technique, low reuse across other L4 problems. Know the one-liner: "can't just add heights; sweep x-events, maintain active y-coverage via segment tree."</span>** |
| 23 | ☑ | Find the longest non-decreasing contiguous subarray; follow-up: change one value arbitrarily to maximize the result. → [solution](23-longest-non-decreasing-subarray/LongestNonDecreasingSubarray.java) |
| 24 | ☑ | Remove adjacent invalid character pairs, such as the same letter in opposite cases; follow-up: solve without an explicit stack. → [stack (StringBuilder)](24-remove-adjacent-char-pairs/RemoveAdjacentPairs.java), [in-place follow-up](24-remove-adjacent-char-pairs/RemoveAdjacentPairsInPlace.java) |
| 25 | ☑ | Given a dictionary of special words, determine whether qualifying subsequences of an input string belong to the dictionary. → [base solution](25-subsequence-dictionary-match/SubsequenceDictionaryMatch.java). **Follow-ups:** (1) longest matching word, tie-broken lexicographically → [LongestSubsequenceDictionaryWord.java](25-subsequence-dictionary-match/LongestSubsequenceDictionaryWord.java); (2) Trie-based optimization for huge dictionaries with shared prefixes → [TrieDictionarySubsequenceMatch.java](25-subsequence-dictionary-match/TrieDictionarySubsequenceMatch.java). |
| 26 | ☑ | Given an array, from index i either skip or take the element; taking it adds a score and jumps according to `arr[i]`; maximize total score. → [base solution](26-array-jump-take-or-skip/ArrayJumpMaxScore.java). **Follow-up:** return the selected indices, not just the score → `maxScoreIndices` in the same file. |
| 27 | ☑ | Find arithmetic subarrays where every adjacent difference is exactly +1 or exactly -1. → [solution](27-arithmetic-adjacent-diff-subarrays/ArithmeticAdjacentDiffSubarrays.java). **Follow-up:** return the longest valid subarray's indices → `longestArithmeticSubarray` in the same file. |
| 28 | ☑ | Given three sorted arrays, find triples containing one value from each array such that every pairwise difference is at most D. → [base solution](28-triples-within-max-difference/TriplesWithinMaxDifference.java). **Follow-up:** return the actual triples → `findValidTriples` in the same file. |
| 29 | ☑ | Logger rate limiter: suppress duplicate messages that occur again within a specified time window. → [solution](29-logger-rate-limiter/LoggerRateLimiter.java). **Follow-up:** thread-safe version → `LoggerRateLimiter.ThreadSafe` in the same file. |
| 30 | ☑ | Design a random music shuffler where no song may repeat within the previous K plays while maintaining correct random selection. → [solution](30-music-shuffler-no-repeat-k/MusicShuffler.java). **Follow-up:** weighted random selection → `MusicShuffler.Weighted` in the same file. |
| 31 | ☑ | Given friends located on graph nodes and cafés on other nodes, choose the café minimizing the maximum distance traveled by any friend. → [solution](31-best-cafe-for-friends/FindBestCafe.java) |
| 32 | ☑ | Given a movie-similarity graph and a starting movie, find the Top N reachable movies by rating. → [solution](32-movie-similarity-top-n/TopNSimilarMovies.java). |
| 33 | ☑ | Find the shortest path through broken teleporters; follow-up: repaired teleporters introduce 0/1 edge costs. → [solution](33-teleporter-shortest-path/TeleporterShortestPath.java). **Follow-up:** partially repaired teleporters (0/1 cost) via 0-1 BFS → `findMinRepairDaysPath` in the same file. |
| 34 | ☑ | Given currency exchange rates, determine whether an arbitrage cycle exists. → [solution](34-currency-arbitrage/CurrencyArbitrage.java). **Follow-up:** return the actual arbitrage cycle → `findArbitrageCycle` in the same file. |
| 35 | ☑ | Determine how broadcast signals can propagate through a network of connected or reachable transmitters. → [solution](35-broadcast-signal-propagation/BroadcastSignalPropagation.java). **Follow-up:** return the actual reachable transmitters from a given start → `reachableTransmitters` in the same file. |
| 36 | ☐ | Given an issue-blocker or dependency graph, find all dependency cycles. **<span style="color:red">TODO (must learn first): don't know Kosaraju's algorithm (SCC detection) yet — study it before attempting this one.</span>** |
| 37 | ☑ | Given recipes, required ingredients, and available supplies, determine which recipes can eventually be produced. → [solution](37-recipes-from-supplies/RecipesFromSupplies.java) (topological BFS; production order and cycle/missing-ingredient exclusion both fall out of the base algorithm with no extra code). |
| 38 | ☑ | Given pairs of similar sentences or words, determine similarity using transitive relationships. → [solution](38-sentence-similarity-transitive/SentenceSimilarityTwo.java) (union-find with **union by size** + path compression; `size[root]` also answers "how many words are similar to X?" for free). |
| 39 | ☑ | Given multiple subsequences describing ordering constraints, determine whether a target sequence is uniquely reconstructible. → [solution](39-sequence-reconstruction/SequenceReconstruction.java) (Kahn's BFS; uniqueness = exactly one zero-indegree node at every step, so `queue.size() != 1` decides it). |
| 40 | ☑ | Build a translator using mappings or dependency relationships between language tokens. → [solution](40-token-translator/Translator.java) (transitive mappings = graph reachability; BFS with `visited` so mapping cycles terminate). **Follow-up:** return the actual translation chain → `translationPath` in the same file. **<span style="color:red">Weighted mappings (cost/confidence) follow-up marked conceptual only (not implemented): same adjacency list, swap the queue for a priority queue ⇒ Dijkstra; maximise a product of confidences via -log weights.</span>** |
| 41 | ☑ | Given parent-child relationships, construct the corresponding tree. → [solution](41-build-tree-from-parent-child/TreeBuilder.java) (value→node map so out-of-order pairs resolve to the same object; root = the only value never seen as a child). **<span style="color:red">Invalid-input follow-up marked conceptual only (not implemented): multiple parents silently duplicate a subtree, a cycle returns null, and a FOREST returns a non-deterministic root — collect all non-child values and assert exactly one.</span>** |
| 42 | ☑ | Merge two N-ary trees while resolving matching children and field conflicts according to specified rules. → [solution](42-merge-nary-trees/MergeNaryTrees.java) (recursive DFS + `LinkedHashMap` keyed by child name; tree2's value wins for matching nodes while tree1 child order remains stable). |
| 43 | ☑ | Repeatedly delete leaves from a multi-tree according to the required deletion process. → [solution](43-delete-nary-tree-leaves/NaryTreeLeafRemoval.java) (one postorder DFS; bottom-up height is the zero-based round in which each node becomes a leaf). |
| 44 | ☑ | Return tree leaves grouped by the round in which they would be removed. → [solution](43-delete-nary-tree-leaves/NaryTreeLeafRemoval.java) (same problem as #43: one postorder DFS groups nodes by their zero-based removal round). |
| 45 | ☑ | Count connected components of 1-valued nodes in a tree. → **[ConnectedOneComponents.java](20-connected-components-binary-tree/ConnectedOneComponents.java)** (same problem as #20: count each component once at its highest `1` node). |
| 46 | ☑ | Find the largest connected component of 1-valued nodes in a tree. → **[ConnectedOneComponents.java](20-connected-components-binary-tree/ConnectedOneComponents.java)** (already implemented with the #20 base solution by returning each connected `1`-subtree size). |
| 47 | ☑ | Given an undirected tree where node degree is bounded, choose the best root so the rooted structure satisfies binary-tree constraints. → **[BestRootForBinaryTree.java](47-best-root-binary-tree/BestRootForBinaryTree.java)** (degree reasoning: root degree ≤ 2 and every non-root degree ≤ 3; includes the minimum-height valid-root follow-up via diameter endpoints). |
| 48 | ☐ | Reroot a tree while satisfying alternating node-color constraints by level. **<span style="color:red">Skip for Google L4 prep: niche rerooting + coloring constraint with low reusable value compared with standard tree DFS/BFS, reroot DP, or tree-center problems.</span>** |
| 49 | ☑ | A mouse moves from index i to a later index j and earns `(j - i) * nums[j]`; maximize the total score to reach the end. → [solution](49-mouse-jump-max-score/MouseJumpMaxScore.java) (greedy: decompose jumps into unit gaps and assign each gap the maximum landing value in its suffix). |
| 50 | ☑ | Given F1 tyres with initial lap time and degradation factor, find the minimum race time using one tyre. → [solution](50-f1-single-tyre-race-time/F1SingleTyreRaceTime.java) (simulate each tyre's geometric lap-time growth and keep the smallest complete-race total). |
| 51 | ☑ | Extend the F1 tyre problem by allowing tyre changes with a fixed replacement cost; minimize total race time. → [solution](51-f1-tyre-change-dp/F1TyreChangeDP.java) (precompute the cheapest fresh-tyre stint of each useful length, then DP over partitions of the race into stints). |
| 52 | ☐ | Given a microwave keypad and target cooking time, choose an input near the target while minimizing keypresses and finger-movement cost. |
| 53 | ☐ | Given horizontal and vertical line segments, count the number of squares they form. **<span style="color:red">SKIP CODING / UNDERSTAND ONLY: group segments by coordinate → merge touching or overlapping intervals → enumerate candidate axis-aligned squares → verify continuous coverage of all four sides. The full implementation is niche geometry/interval plumbing with low value for limited Google L4 prep time.</span>** → [reference solution](53-count-squares-from-segments/CountSquaresFromSegments.java) |
| 54 | ☑ | Given rectangles, find the vertical line that splits their total area equally. → **[VerticalAreaSplit.java](22-rectangle-area-split/VerticalAreaSplit.java)** (same problem as #22: sweep x-events, keep the active height, solve for the cut inside the strip that crosses half the total area). |
| 55 | ☑ | Maintain a set of 2D points and answer whether a rectangle can be formed from stored points. → **[RectangleExistsIncremental.java](21-rectangle-points/RectangleExistsIncremental.java)** (same problem as the #21 follow-up: a rectangle exists exactly when two x-columns share the same pair of y-values, so `addPoint` records each new y-pair against its column and flips an O(1) flag the moment a pair appears at a second x; duplicates are skipped so they cannot fake a pair). |
| 56 | ☑ | Given a set of 2D points, find the maximum-area rectangle that can be formed. → **[MaxRectangleArea.java](21-rectangle-points/MaxRectangleArea.java)** (same problem as the #21 follow-up; two forms in one file: batch `MaxRectangleAreaBatch` treats every point pair as a diagonal and checks whether the other two corners `(x1,y2)`/`(x2,y1)` exist — O(N²) time, O(N) space — while the incremental class keeps `yPair -> x list` so `getMaxArea()` stays O(1). Areas use `long`: coordinates near ±10⁹ overflow `int`). **<span style="color:red">Rotated (non-axis-aligned) rectangles marked understand-only: the diagonal-corner trick no longer applies — it becomes a vector/geometry problem.</span>** |

> Note: #54 and #22 describe the same core "split rectangle area with a vertical line" idea; they are kept separate here to preserve the original formulations. Numbering matches the originally selected 56 (the earlier duplicate router-reachability item and the router-activation-time item were dropped; the two point-set rectangle problems are #55–#56).

---

## Revision Tracker (Phase 1)

Separate from the solve checkbox above: a problem is **revised** only when it is re-solved from scratch without looking at the existing file.

**Revised: 27 / 56.**

| # | Revised | Problem |
|---|---------|---------|
| 1 | ☐ | Nested function expression evaluation |
| 2 | ☑ | Grid source-to-target reachability |
| 3 | ☑ | Multi-source BFS — distance to nearest source |
| 4 | ☑ | Router signal propagation |
| 5 | ☑ | Time-aware flight / package routing |
| 6 | ☑ | Shared-route meeting point |
| 7 | ☑ | Merge orderings via topological sort |
| 8 | ☑ | Character order from pairs |
| 9 | ☑ | Recursive placeholder substitution |
| 10 | ☑ | Filesystem / path hierarchy |
| 11 | ☑ | Max common prefix across files |
| 12 | ☐ | Longest increasing subsequence, adjacent diff |
| 13 | ☑ | Top K from a stream |
| 14 | ☑ | Move pieces to string (`L`/`R`/`_`) |
| 15 | ☐ | Interval overlap progression |
| 16 | ☐ | Product over last K of a stream |
| 17 | ☐ | Infix / postfix expression evaluation |
| 18 | ☑ | Merge two N-ary trees |
| 19 | ☑ | Tree leaves with max ancestor |
| 20 | ☑ | Connected components of 1-nodes in a binary tree |
| 21 | ☐ | Rectangle from 2D points |
| 22 | ☐ | Vertical line splitting rectangle area |
| 23 | ☑ | Longest non-decreasing subarray |
| 24 | ☑ | Remove adjacent character pairs |
| 25 | ☐ | Subsequence dictionary match |
| 26 | ☑ | Array jump — take or skip |
| 27 | ☐ | Arithmetic adjacent-diff subarrays |
| 28 | ☑ | Triples within max difference |
| 29 | ☐ | Logger rate limiter |
| 30 | ☐ | Music shuffler with no repeat in K |
| 31 | ☑ | Best café for friends |
| 32 | ☐ | Movie similarity Top N |
| 33 | ☑ | Teleporter shortest path |
| 34 | ☐ | Currency arbitrage |
| 35 | ☑ | Broadcast signal propagation |
| 36 | ☐ | Dependency cycles (SCC) |
| 37 | ☐ | Recipes from supplies |
| 38 | ☐ | Sentence similarity (transitive) |
| 39 | ☐ | Sequence reconstruction |
| 40 | ☐ | Token translator |
| 41 | ☐ | Build tree from parent-child pairs |
| 42 | ☑ | Merge N-ary trees with conflict rules |
| 43 | ☐ | Delete N-ary tree leaves |
| 44 | ☐ | Leaves grouped by removal round |
| 45 | ☑ | Count connected 1-components |
| 46 | ☑ | Largest connected 1-component |
| 47 | ☑ | Best root for a binary tree |
| 48 | ☐ | Reroot tree with color constraints |
| 49 | ☑ | Mouse jump max score |
| 50 | ☐ | F1 single-tyre race time |
| 51 | ☐ | F1 tyre-change DP |
| 52 | ☐ | Microwave keypad target time |
| 53 | ☐ | Count squares from segments |
| 54 | ☐ | Vertical area split |
| 55 | ☐ | Rectangle exists (incremental) |
| 56 | ☐ | Max rectangle area |

### Revision notes

**#4 — Router signal propagation (uniform and per-router radius)**

Mistakes made:
- In the base rewrite, initially used `else if` while locating the endpoints, so source equal to destination left the destination index unset.
- Enqueued neighbours without marking them visited, which could repeatedly cycle between reached routers when the destination was unreachable.
- Initially squared coordinates and the radius as `int` in both variants, so large distances could overflow and create false edges.
- The base rewrite materializes the adjacency list, making its worst-case space O(N²), while its original O(N)-space claim described on-demand neighbour discovery.

What's correct:
- Uniform range creates an undirected graph; per-router radii create directed edges because `u -> v` uses only `u`'s radius.
- Mark every router visited when it is enqueued. This prevents duplicate queue entries and automatically skips the current router when the neighbour loop scans all indices.
- Compare squared distances with `long` arithmetic to avoid square roots and ordinary `int` overflow.
- The base adjacency-list implementation runs in O(N²) time and O(N²) worst-case space. The follow-up discovers neighbours during BFS, retaining O(N²) time with O(N) auxiliary space.
- Verified the base with 12 targeted and 20,000 randomized cases, and the directed-radius follow-up with 14 targeted and 20,000 randomized cases, each against an independent exact-distance BFS oracle.

**#6 — Shared-route meeting point (unweighted BFS + weighted Dijkstra)**

Mistakes made:
- In the weighted rewrite, initially enqueued priority-queue entries as `(node, distance)` even though polling interpreted them as `(distance, node)`, causing an out-of-bounds node access.
- After correcting that order, kept queue distances and `currentDistance` as `int` and cast each computed `long` distance back to `int`; a valid 3,000,000,000-cost path overflowed to a negative result.
- Used `1e12` as infinity, which incorrectly classified valid paths whose cost reached or exceeded that arbitrary sentinel as unreachable.

What's correct:
- The optimal union for two sources and one destination is a tree with one branch node M, so minimizing `dist(A,M) + dist(B,M) + dist(D,M)` over every M gives the minimum distinct-edge cost.
- Use three BFS runs for an unweighted graph and three Dijkstra runs for an undirected graph with positive weights.
- Weighted queue entries are `(long distance, node)`. Skip stale entries, keep relaxation arithmetic in `long`, and use one shared unreachable sentinel with overflow-safe addition.
- Both variants run in O((V + E) log V) or better: O(V + E) for the unweighted BFS version and O((V + E) log V) for the weighted Dijkstra version, with O(V + E) space.
- Verified the unweighted solution over 137,341 cases against a brute-force selected-edge-subset oracle. Verified the weighted solution over 13,669 targeted, exhaustive, and randomized cases against an independent minimum-cost selected-edge-subset oracle, including costs above `Integer.MAX_VALUE` and the old `1e12` sentinel.

**#10 — Filesystem path hierarchy (path trie + cached subtree sizes)**

Mistakes made:
- Initially assigned a new file's size directly and then applied the same delta to every node on the complete path, including the file node, so file sizes were counted twice.
- Root deletion used the normal parent-unlink path, which tried to access a nonexistent parent and threw an index error.
- Initially identified a directory only by whether it currently had children, allowing an empty directory to be silently converted into a file.
- The first rewrite exposed the operations only as private methods, preventing normal callers from using the filesystem API.

What's correct:
- Represent path components as a trie and cache each node's complete subtree size. Adding or overwriting a file applies `newSize - oldSize` to every node from root through the file.
- Removing a file or directory subtracts that node's cached subtree size from its ancestors and unlinks the entire subtree in O(P); removing `/` replaces the root with an empty directory.
- File/directory identity is independent of child count: an existing empty directory cannot become a file, root cannot become a file, and attempting either invalid operation leaves state unchanged.
- `addFile`, `removeFile`, and `getSize` each take O(P) time for P path components, with O(total path components) storage, assuming aggregate sizes fit in `int`.
- Verified all 18 built-in scenarios plus 200,000 deterministic stateful operations and 9,654,232 full-state size comparisons against an independent flat file/directory model.

**#23 — Longest non-decreasing contiguous subarray (one pass + one-change prefix/suffix runs)**

Mistakes made:
- In the base rewrite, initially incremented an undefined loop variable instead of the right endpoint and returned 1 for an empty array before adding the null/empty guard.
- In the follow-up, initially compared uninitialized `end` and `start` lengths instead of adjacent input values while constructing the two run arrays.
- Initially omitted null, empty, and short-array guards, so singleton input indexed beyond the array.
- Initially joined both neighboring runs without checking whether one integer could sit between them; after adding that bridge condition, considered only a full bridge and missed changing an interior value to attach to just one side.

What's correct:
- The base scan tracks the start of the current valid run; a decrease resets that start, while every other pair extends the run. It takes O(N) time and O(1) space.
- For the follow-up, `end[i]` stores the non-decreasing run ending at i and `start[i]` stores the run starting at i.
- Changing index i can attach to the left run, attach to the right run, or bridge both only when `nums[i - 1] <= nums[i + 1]`. Taking the best over all indices covers changing at most one value.
- The follow-up takes O(N) time and O(N) space.
- Verified both methods on all 11 built-ins, 97,657 exhaustive arrays, 5 targeted boundary cases, and 10,000 full-range randomized arrays against independent direct-run and explicit-replacement oracles.

**#13 — Top K from a stream (min-heap + frequency rankings)**

Mistakes made:
- While rewriting the continuous-frequency follow-up, initially used invalid comparator syntax and returned a boolean for the username tie-break; a Java comparator must return a negative, zero, or positive `int`.
- Initially mixed the batch follow-up's `User` type with the continuous follow-up's `UserCount` type.
- In `getTopK`, initially added an entry before checking the limit without guarding `k <= 0`, so zero or negative K returned every user.

What's correct:
- Base stream: keep a min-heap of at most K values; its root is the weakest current member and therefore the K-th largest. `add` is O(log K), `getTopK` is O(K log K), and space is O(K).
- Batch frequency follow-up: count all users first, then run a size-K min-heap over the U unique users. Counts cannot be discarded early because later messages may change the ranking. Time is O(N + U log K), space is O(U + K).
- Continuous frequency follow-up needs two indexes: a map for O(1) user-to-count lookup and a `TreeSet` for ranked iteration. Remove the old `(user, count)` entry before updating, then reinsert the new immutable ranking key.
- The `TreeSet` comparator orders count descending and username ascending; comparator result zero means same count and same username, which also lets removal use a newly constructed `UserCount`.
- The batch heap is temporary and may be drained to build a result; the continuous `TreeSet` is persistent state, so queries iterate without removing entries.
- Continuous updates cost O(log U), Top K queries cost O(K), and space is O(U).
- Verified the base API over 5,000 randomized streams, batch frequency over 10,000 randomized cases, and continuous frequency over 5,000 randomized streams containing 376,503 live updates with repeated queries after every update.

**#49 — Mouse jump maximum score (quadratic DP + suffix-maximum greedy)**

Mistakes made:
- The first DP version indexed `nums.length` before guarding null/empty input.
- Used `int[]` DP state and performed `(j - i) * nums[j]` as `int`, so large valid scores overflowed before the method returned `long`.
- Renamed the DP API to `maxScoreDP` while the existing tests still called `maxScore`, temporarily making the file fail compilation.
- The linear greedy version initially accumulated into `int score`, causing the same overflow on large totals.

What's correct:
- DP state `dp[j]` is the maximum score upon reaching index `j`; try every earlier source `i` and add `(j - i) * nums[j]` using `long` arithmetic.
- A jump to `j` assigns `nums[j]` to every unit gap it crosses. The best contribution for each gap is therefore the maximum value to its right, and suffix-maximum landing indices make all those choices achievable in one path.
- The DP solution runs in O(N²) time and O(N) space; the suffix-maximum greedy solution runs in O(N) time and O(1) space.
- Both APIs handle null, empty, single-element, negative, and overflow-boundary inputs.
- Verified each API independently with 10 targeted cases, 97,655 exhaustive small arrays, and 10,000 randomized arrays against a quadratic `long` DP oracle.

**#47 — Best root for a binary tree (degree constraints + tree diameter)**

Mistakes made:
- In the minimum-height follow-up, initially selected a better root without updating `minHeight`, so nearly every subsequent node appeared better and the method returned the last candidate.
- Initially considered nodes with degree greater than 2 as roots even though all of a root's incident edges become child edges.
- Initially omitted the impossible-tree check for a node with degree greater than 3 and the empty-tree guard before starting BFS from node 0.

What's correct:
- For any non-root node, one incident edge becomes its parent edge, so it has `degree - 1` children; therefore every node must have degree at most 3.
- A root has no parent edge, so only nodes with degree at most 2 are valid root candidates.
- For the minimum-height follow-up, two BFS sweeps find diameter endpoints `A` and `B`; any node's height is `max(distanceToA, distanceToB)`.
- Choose the valid root candidate with the smallest computed height.
- Verified `findRoot` with 7 targeted cases and 10,000 randomized trees; verified `findMinimumHeightRoot` separately with 7 targeted cases and 10,000 randomized trees against brute-force all-roots BFS oracles.
- Complexity: O(N) time and O(N) space because a tree has N - 1 edges and each degree/BFS pass is linear.

**#7 — Merge multiple orderings (Kahn's topological sort)**

Mistakes made:
- Initially built every iteration from a new empty list instead of `orderings.get(i)`, so no nodes or edges were registered and every input returned `[]`.
- Registered nodes only while processing adjacent pairs, which omitted singleton orderings such as `[A]` from the result.
- Temporarily incremented `x` for an edge `x -> y`; the dependent node `y` gains the prerequisite, so `inDegree[y]` must increase.

What's correct:
- Register every item before adding edges so isolated and singleton nodes participate in the global order.
- Convert each adjacent pair in an ordering into an edge; Kahn's queue contains exactly the nodes whose prerequisites have all been emitted.
- A `List` adjacency is correct here because duplicate edges are retained and receive matching in-degree increments and decrements; using a `Set` would instead require incrementing only when `add` succeeds.
- If the result contains fewer nodes than the in-degree map, unprocessed nodes are trapped in a cycle, so return `[]`.
- Verified with 7 targeted cases and 5,000 randomized cases checked against a brute-force permutation oracle.
- Complexity: O(V + E) time and O(V + E) space, where E counts all adjacent constraints, including duplicates.

**#5 — Time-aware flight / package routing (earliest-arrival Dijkstra)**

Mistakes made:
- Used direct adjacency lookup for the current airport, which could throw when the source had no outgoing flights; use an empty-list default.
- Initialized an unknown earliest arrival with `Integer.MIN_VALUE`; an unknown minimum should start at infinity (`Integer.MAX_VALUE`).
- Temporarily mixed old `CurrentPos` field names with the rewritten state object, causing compilation errors.
- Stored path links as parent-to-child even though reconstruction walks backward from destination; the map must store child-to-parent.
- Reconstructed without first checking whether the destination was reached and initially omitted the source from the returned path.

What's correct:
- `earliestTimes[airport]` stores the earliest reachable arrival; arriving earlier dominates arriving later because waiting is allowed.
- Process states in a min-heap, skip stale entries, and take a flight only when `currentArrival <= departureTime`.
- Update the earliest-arrival map when offering an improved state so known-worse arrivals are not enqueued again.
- For path reconstruction, update `parent[destination] = currentAirport` whenever its earliest arrival improves, then walk backward and reverse.
- Complexity: O((A + F) log F) time and O(A + F) space for A airports and F flights.

**#8 — Character order from pairs (Kahn's topological sort)**

Mistakes made:
- Only registered `pair[1]` in the in-degree map, so source-only characters never existed as keys, nothing had in-degree 0, the queue never seeded, and every input returned `""`.
- Called `graph.get(cur)` directly in the BFS; sink nodes have no adjacency entry, so the first sink threw a `NullPointerException`.
- Compared `sb.length()` against `graph.size()`, which counts only nodes **with outgoing edges**, not all distinct characters.
- Incremented in-degree on every pair while the adjacency `Set` silently deduped, so a repeated pair made a node unreachable and reported a false cycle.

What's correct:
- `x > y` is a directed edge `x -> y`; one valid total order = one topological sort; a cycle means no order exists.
- Register **every** endpoint before seeding: targets via the increment, sources via `putIfAbsent(pair[0], 0)` — `putIfAbsent` because a plain `put` would reset a real count to 0 and create a fake root.
- `inDegree.size()` is the distinct-node count and the only correct denominator for the completeness check.
- Bump the counter only when `Set.add` returns `true`, so each `+1` has exactly one matching `-1` during traversal.
- Guard neighbour lookup with `graph.getOrDefault(cur, Collections.emptySet())` for sinks.
- A `visited` set is unnecessary — in-degree reaches 0 exactly once per node.
- Complexity: O(V + E) time and space.

**#9 — Recursive placeholder substitution (DFS + memoization)**

Mistakes made:
- Continued placeholder parsing after copying a normal character, so text without a following `%` reached `substring(..., -1)`.
- Extracted `substring(i, end)`, which included the opening `%` and omitted the closing one, instead of extracting only the map key with `substring(i + 1, end)`.
- Appended only the extracted key for an unknown placeholder, dropping its `%` delimiters.
- Returned the raw replacement on a cache hit instead of the cached, fully expanded value.

What's correct:
- Treat replacement keys as nodes in an implicit dependency graph and recursively expand each key's raw value.
- `pathVisitedKeys` contains keys active in the current DFS chain, so seeing one again proves a cycle.
- `keyCache` contains completed keys and their fully expanded values; it is both the completed-state set and the reusable answer store.
- Preserve unknown placeholders unchanged, treat an unmatched `%` as ordinary text, and advance past the closing delimiter after processing `%KEY%`.
- Complexity: O(N + R + E + L) time and O(K + E + D) auxiliary space, where N is input length, R is reachable raw-value length, E is expanded-cache length, L is output length, K is reachable keys, and D is maximum dependency depth.

**#14 — Move pieces to string (two pointers)**

Mistakes made:
- Compared the pointer index itself with `'_'` instead of reading `start.charAt(s)` or `target.charAt(t)`, so blanks were never skipped.
- Initially used `start.charAt(t)` while advancing the target pointer; each pointer must inspect its own string.

What's correct:
- Ignore blanks and match the non-blank pieces in order; pieces cannot cross, so their `L`/`R` sequence must be identical.
- For each matched `L`, require `targetIndex <= startIndex`; `L` can move only left.
- For each matched `R`, require `targetIndex >= startIndex`; `R` can move only right.
- If either pointer is exhausted after skipping blanks, the remaining suffix checks ensure no unmatched piece remains.
- Complexity: O(n) time and O(1) extra space.

**#18 — Merge two N-ary trees by child name (DFS + map)**

What's correct:
- The recursive contract is that `mergeTrees(a, b)` merges two nodes representing the same logical name; null checks establish only whether each subtree exists, not whether names match.
- Build a name-to-node map for one node's children, then scan the other node's children: recursively merge matching names and directly retain unmatched children.
- Remove each matched name from the map so the remaining entries are exactly the children unique to the first tree.
- Different initial root names return `null` under the chosen contract; throwing an exception would be another valid explicit contract.
- For an immutable merge, matched nodes are newly constructed and every unmatched or sole-side subtree is deep-cloned, so the output shares no node identity with either input.
- Problem #42 is the same core N-ary merge formulation with a value-conflict rule, so the #18 independent re-solve covers it.
- Expected complexity: O(N) time and O(N) space across both trees.

**#20 — Connected components of 1-nodes in a binary tree (DFS)**

Mistakes made:
- Used a virtual parent value of `-1` while counting a component only when `parent == 0`, so a root-valued `1` was not counted.
- The largest-component collection pass initially traversed through `0` nodes and included separate `1` components below them.

What's correct:
- Count a component exactly once at its highest `1` node: the current node is `1` and its parent is not `1`.
- A `1` node returns `1 + leftSize + rightSize`; a `0` node returns `0`, which breaks connectivity.
- Track the largest returned size during postorder DFS.
- To return the largest component's nodes in O(N), first identify its top node using sizes, then collect from only that node while stopping at every `0` boundary.
- Problems #45 and #46 are duplicate formulations of #20's component-count and largest-component outputs, so the same independent re-solve covers them.
- Complexity: O(N) time and O(H) recursion space; returning the largest component also needs O(M) output space for M returned nodes.

**#24 — Remove adjacent opposite-case pairs (stack + write pointer)**

Mistakes made:
- Initially created the `StringBuilder` with the full input and then treated it as an empty stack, mixing unprocessed input with surviving output.
- Read the current character from the changing builder instead of the original string, so deletions could invalidate later indices.
- Passed the last character's numeric value to `deleteCharAt` instead of passing its index `length - 1`.
- Had Java syntax errors: `chat` instead of `char`, a missing boolean return type, and `char` instead of `chars` in the `String` constructor.
- In the write-pointer follow-up, initially checked `i > 0` and compared against `chars[top]`; the cleaned prefix may be empty even when input has been read, and its last survivor is at `top - 1`.

What's correct:
- The only possible new cancellation is between the current character and the last surviving character, so the surviving prefix behaves exactly like a stack.
- The base solution uses `StringBuilder` as that stack: append to push and delete `length - 1` to pop. A pop naturally exposes any cascading cancellation.
- In the write-pointer version, `top` is the next free position and the cleaned prefix is `[0, top)`. Pop with `top--`; push with `chars[top++] = current`.
- `new String(chars, 0, top)` takes a start index and a length, so it returns exactly indices 0 through `top - 1`.
- Both methods run in O(N) time. The base uses O(N) stack space. The String-based follow-up still allocates an O(N) `char[]` and output String; it is truly O(1) extra only when given a mutable `char[]` and returning the valid length.
- Verified both methods with 6 targeted cases and 20,000 randomized English-letter strings against an independent repeated-removal oracle.

**#26 — Array jump: take or skip (bottom-up DP + path reconstruction)**

Mistakes made:
- Initially allocated `dp` with length N but iterated from N - 1 while reading `dp[i + 1]`, so the first iteration accessed `dp[N]`; either use an N + 1 sentinel or seed the last state and start at N - 2.
- Initially used `arr[jumpIndex]` after taking, which adds only the next raw value instead of the best future score stored in `dp[jumpIndex]`.
- Temporarily removed the null/empty guard and indexed the final element before checking the input, so an empty array failed.
- Kept accumulated scores and `i + arr[i]` in `int`; returning `long` does not repair overflow that already happened inside the method.
- In path reconstruction, initially declared the decision array as `int[]` while storing booleans and wrote an invalid ternary expression, so the method did not compile.
- Reconstructed a taken jump with `i += arr[i]` in `int`; a very large jump could wrap negative instead of leaving the array.

What's correct:
- Define `dp[i]` as the maximum score starting at index i: skip gives `dp[i + 1]`, while take gives `arr[i] + dp[i + arr[i]]` when the landing index remains inside the array.
- All values are positive, so both choices move right. Compute the states from right to left because every needed future state is already known.
- Record `took[i]` when choosing `dp[i]`, then reconstruct from index 0: skip moves by one; take records the index and jumps. Remember: compute the best value backward, then replay the winning choices forward.
- `take >= dontTake` deliberately chooses take on a tie; either tied path is optimal, but a fixed rule makes reconstruction deterministic.
- Keep scores and jump addition in `long`. Cast a jump back to `int` only after proving it is still inside the array.
- Both APIs run in O(N) time and O(N) space; returning the selected indices also uses O(K) output space.
- Verified `maxScore` with 4 targeted cases and 10,000 randomized arrays against an independent brute-force oracle. Verified `maxScoreIndices` with null, empty, ordinary, and overflow-boundary cases plus 20,000 randomized arrays, checking both path legality and optimal score against exhaustive search.

**#28 — Triples from three sorted arrays within D (minimum anchor + monotonic ranges)**

Mistakes made:
- Initially redeclared pointer variables inside the same method scope instead of resetting them between anchor passes, so the file did not compile.
- Advanced upper pointers while values were `>= anchor + D`; the pointer must instead move across every valid value `<= anchor + D` and stop at the first larger value.
- In the C-anchor pass, indexed B with `cHi` instead of `bHi`, causing an out-of-bounds failure.
- Initially computed `anchor + maxDifference` as `int`, so a large positive anchor could overflow and create an incorrect upper bound.
- Temporarily omitted the null, empty-array, and negative-D guard.

What's correct:
- Three values satisfy every pairwise difference limit exactly when `maximum - minimum <= D`; once minimum x is fixed, both other values only need to lie in `[x, x + D]`.
- Every valid triple has a minimum in A, B, or C, so trying each array as the minimum anchor cannot miss a triple.
- Use tie priority A, then B, then C to count each triple once: A allows both ties; B requires `A > B` but allows `C == B`; C requires both `A > C` and `B > C`.
- Each low pointer marks the first allowed value. Each high pointer marks the first value greater than `anchor + D`, making the valid range `[low, high)`.
- Sorted anchors make both range boundaries non-decreasing, so every pointer moves only forward. Counting takes O(A + B + C) time and O(1) auxiliary space.
- Count index triples, not distinct value triples. Keep the total and each computed upper bound in `long` so duplicates and integer boundaries remain correct.
- The follow-up uses the same three passes and ranges, but emits every index combination instead of multiplying range lengths. Its time is O(A + B + C + T) and output space is O(T), where T is the number of returned triples.
- Verified the count API with 6 targeted cases and 20,000 randomized arrays against a cubic oracle. Verified both count and returned index triples over another 10,000 randomized cases, including duplicates, negatives, empty arrays, ties, and integer boundaries.

**#31 — Best café for friends (BFS from each café)**

Mistakes made:
- Initially misspelled `Deque`, omitted its import, and called `isEmpty()` with the wrong capitalization, so the rewrite did not compile.
- Initially reversed the best-distance comparison, which would replace a better café only when the new maximum distance was larger.
- Initialized the farthest-friend distance to -1. This preserved the chosen café for nonempty distinct friends, but 0 is the true score when the café itself is the only friend.
- The header still described BFS from each friend even though the rewrite intentionally runs BFS from each café.

What's correct:
- In an undirected graph, distance is symmetric. A BFS from one café therefore gives that café's shortest distance to every reachable friend.
- During each BFS, count reached friend nodes and track their maximum distance. A café is valid only when the count equals the number of distinct friends.
- Choose the valid café with the smallest maximum distance. Strict improvement means the first café in the input wins a tie.
- Under the stated assumption that there are C cafés and C < F friends, the solution runs in O(C(V + E)) time and O(V + E) space, including the graph.
- Verified all 3 built-in cases, a targeted café-at-friend case, and 20,000 randomized graphs against an independent Floyd-Warshall oracle, including 17,455 cases where a café was also a friend node.

**#33 — Shortest path through teleporters (BFS + 0-1 BFS)**

Mistakes made:
- In the base rewrite, initially called `isEmpty()` with the wrong capitalization and read neighbors from an undefined `graph` variable instead of the `connections` parameter.
- Initially checked `source == destination` before rejecting broken endpoints, which returned a path containing a broken teleporter when both endpoints were the same node.
- Misspelled `ArrayList` while adding the broken-endpoint guard, so the base method did not compile.
- In the 0-1 BFS follow-up, initially used malformed or unqualified enum constants and assigned `distance[source]` even though the array was named `dist`.
- The first follow-up draft updated the parent and deque but not `dist[neighbor]`. Without recording the better cost, the same node still appears infinitely expensive and can be enqueued repeatedly.

What's correct:
- In the base problem every usable directed edge costs one, so ordinary BFS discovers each teleporter through a minimum-hop path. Parent pointers reconstruct that path backward.
- Broken teleporters are excluded entirely. A broken source or destination is rejected before handling the zero-edge `source == destination` case.
- In the follow-up, leaving a working teleporter costs 0 and leaving a partially repaired teleporter costs 1. The destination itself is not charged because the route never leaves it.
- A successful relaxation must update both `dist[neighbor]` and `parent[neighbor]`. A 0-cost move goes to the deque front because its total cost is unchanged; a 1-cost move goes to the back because every unchanged-cost candidate should run first.
- Both methods run in O(V + E) time and O(V) auxiliary space for valid graph inputs.
- Verified the base with all 4 built-in cases, a targeted broken-source case, and 20,000 randomized directed graphs against a Floyd-Warshall oracle. Verified the follow-up with its built-in case and 20,000 randomized 0/1 graphs against an independent Dijkstra oracle, checking path legality and total repair cost.

**#35 — Broadcast signal propagation (directed graph + DFS)**

Mistakes made:
- Initially created an empty adjacency list inside `maximumReach` instead of calling `buildReachabilityGraph`, so DFS had no node lists to traverse.
- Declared the DFS visited state as `int[]` while assigning and testing boolean values, so the rewrite did not compile.
- Initially calculated coordinate differences and squares as `int`, so overflow could create false reachability edges. A coordinate gap of 65,536 squared to zero and incorrectly connected two transmitters.

What's correct:
- Each ordered pair `(i, j)` is checked separately because reachability is directed. The edge `i -> j` depends only on transmitter `i`'s radius.
- Squared-distance arithmetic uses `long` before subtraction and multiplication, avoiding square roots and ordinary `int` overflow.
- Build the directed graph once. Then run DFS from every possible starting transmitter with a fresh visited array and keep the largest reached count.
- Mark a node visited before recursing. This terminates cycles and ensures every activated transmitter is counted or collected exactly once.
- The follow-up uses the same graph and DFS but collects visited indices instead of only returning their count.
- The solution runs in O(N^3) worst-case time and O(N^2) space. Verified all 12 built-in DFS/BFS cases, the overflow boundary, 20,000 randomized maximum-reach cases, and 20,000 randomized collection cases against independent geometric BFS oracles.

---

## Phase 2 — Remaining Problems (after the 56)

### Tier 1 — Highest priority / recurring concrete formulations
- ☐ Rectangle queries over point sets: insert/query points, detect rectangles, then compute maximum rectangle area (2025 report progression).
- ☐ Area split by a vertical line across rectangles, sometimes with overlaps requiring union-area handling.
- ☐ Number of islands in a binary tree, including component-size or uniqueness follow-ups.

### Tier 2 — Strong Google-style preparation value
- ☐ `pi` as a string: return 1-based indices `i` where the digits of `i` match the substring at position `i`.
- ☐ Map IP-address ranges to countries; linear search → sorted ranges + binary search.
- ☐ Watchtower profit: choose tower height balancing construction cost against revenue from houses covered; follow-up arbitrary tower location.
- ☐ Determine whether two sentences differ only by insertion of one contiguous phrase.
- ☐ Group duplicate entities where entities sharing any property belong to the same connected group.
- ☐ Array jumping: from index i, take or skip; taking adds score and jumps according to `arr[i]`; maximize score.
- ☐ Android unlock patterns generalized to an n × m point grid.
- ☐ Hierarchical tasks with subtasks: compute parent completion time according to child completion-time rules.
- ☐ Maximum-length arithmetic subarrays where consecutive difference is exactly +1 or -1.
- ☐ Sum the contribution of all valid arithmetic subarrays.
- ☐ Find triples from three sorted arrays where all pairwise absolute differences are at most D.
- ☐ Find three numbers in one collection lying within distance D.
- ☐ Product of the last K numbers in a stream.
- ☐ Mean of the last N values.
- ☐ Mean of the last N excluding the largest K.
- ☐ MK Average-style streaming statistics.
- ☐ Logger rate limiter / suppress duplicates within a time window.
- ☐ Google Photos acknowledgements arriving out of order; return largest continuously acknowledged prefix.
- ☐ Sequence-number stream: add values and return the smallest missing sequence number.
- ☐ Random music shuffler where a song cannot repeat within the previous K plays.
- ☐ Top K chat users by number of messages.
- ☐ Top K users by words spoken.
- ☐ Top K frequent words in a document; follow-up streaming document.
- ☐ K-th highest accumulated weight for a given category/color.
- ☐ Maximum stream value less than a given threshold.
- ☐ Infinite stream: return a value around bounds surrounding the current median.
- ☐ Queue of customers and counters with different processing times; determine when person K completes.
- ☐ Patient-priority queue processing.
- ☐ Schedule jobs over machines while minimizing makespan.
- ☐ Blocking document-processing jobs with expensive status APIs; minimize completion time and calls.

### Tier 3 — Graph, tree, trie, and dependency variants
- ☐ Café meeting point minimizing the maximum graph distance traveled by any friend.
- ☐ Movie-similarity graph: return top N reachable movies by rating.
- ☐ Broken teleporter shortest path; follow-up: repaired teleporters creating 0/1 edge weights.
- ☐ Currency-arbitrage cycle detection.
- ☐ Broadcast-signal chaining.
- ☐ Water-flow reachability to cities.
- ☐ Bipartite graph test and application variant.
- ☐ Cat and Mouse graph game.
- ☐ Safest Path in a Grid.
- ☐ Pacific Atlantic-style reverse reachability.
- ☐ Shortest path after consuming/collecting required objects.
- ☐ Find all dependency cycles in an issue/blocker relationship graph. **<span style="color:red">TODO (must learn first): don't know Kosaraju's algorithm (SCC detection) yet — study it before attempting this one.</span>**
- ☐ Find recipes possible from supplies and recipe dependencies.
- ☐ Sentence Similarity II / equivalence through transitive relationships.
- ☐ Sequence Reconstruction.
- ☐ Generic language translator using dependency/mapping relationships.
- ☐ Build a tree from parent-child relationships.
- ☐ Merge two N-ary trees with field-specific conflict rules.
- ☐ Recursively delete leaf nodes from a multi-tree.
- ☐ Return leaves grouped by removal round.
- ☐ Count connected components of 1 nodes inside a tree.
- ☐ Find largest connected 1 component in a tree.
- ☐ Find the best root of an undirected degree-3 tree so it becomes a binary tree.
- ☐ Same rerooting problem with alternating node colors by level (Google L5 report).
- ☐ Subtree-size queries with nodes/files added dynamically.
- ☐ File-system tree startup is slow; redesign preprocessing versus incremental updates.
- ☐ URL/path reachability through a hierarchical tree.
- ☐ From a URL/path node, find maximum distance to any reachable leaf.
- ☐ Domain/path weighted hierarchy: accumulate prefix contributions through a trie.
- ☐ Dictionary prefix search.
- ☐ Maximum common prefix across any pair of files.
- ☐ Print filesystem folder structure from file paths.
- ☐ Crossword placement in a matrix.
- ☐ Word search in a board.
- ☐ Decode String.
- ☐ Regular-expression tree: implement `match()`.
- ☐ Boolean-expression object model and satisfiability evaluation.
- ☐ Spreadsheet cells with formulas and dependency evaluation.

### Tier 4 — Arrays, strings, DP, geometry, scheduling
- ☐ Mouse jumping through array: moving i → j earns `(j-i) * nums[j]`; maximize score.
- ☐ Partition Equal Subset Sum modification.
- ☐ Shortest Common Supersequence.
- ☐ Longest String Chain.
- ☐ Burst Balloons.
- ☐ Job sequencing.
- ☐ Elevator DP.
- ☐ F1 tyres: degradation per lap; minimum race time using one tyre.
- ☐ F1 tyres follow-up: tyre changes allowed with replacement cost.
- ☐ Microwave keypad: enter a time close to target while minimizing presses and finger movement.
- ☐ Apartment Hunting.
- ☐ Assign people to apartments.
- ☐ Remove common elements from prefixes of length K in two arrays.
- ☐ Fixed sorted-array query problem.
- ☐ Find first missing subsequence.
- ☐ Subset selection involving LCM.
- ☐ Generate substrings and append characters.
- ☐ Balance parentheses after deletions.
- ☐ Expression Add Operators.
- ☐ Unique pairing of balanced-parentheses strings.
- ☐ Difference of sets of flights / itinerary-related set processing.
- ☐ Browser tab reordering from a starting position.
- ☐ Gmail label-string processing.
- ☐ Match APKs with compatible devices.
- ☐ Cut cake vertically without destroying/intersecting toppings.
- ☐ Count houses inside a circle.
- ☐ Rectangle Area.
- ☐ Maximum Number of Visible Points.
- ☐ Count squares formed by horizontal and vertical segments.
- ☐ Vertical line splitting rectangle area equally.
- ☐ Point insertion + rectangle-existence query.
- ☐ Maximum rectangle from stored points.
- ☐ Merge two scrolling screenshots using maximum suffix-prefix overlap.
- ☐ Date minus offset days, handling month/year/leap-year boundaries.
- ☐ Two-string inserted-phrase detection.
- ☐ Check whether all long-enough subsequences are in a dictionary.
- ☐ Remove bad adjacent case pairs.
- ☐ Evaluate bracket-pair substitutions in a string.
- ☐ Longest non-decreasing subarray.
- ☐ Longest non-decreasing subarray after changing one element.
- ☐ Bottle-splitting problem solvable by sliding window (2026 report).
- ☐ Find a square of 1s in a binary matrix under additional constraints.
