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

**Revised: 5 / 56.**

| # | Revised | Problem |
|---|---------|---------|
| 1 | ☐ | Nested function expression evaluation |
| 2 | ☑ | Grid source-to-target reachability |
| 3 | ☑ | Multi-source BFS — distance to nearest source |
| 4 | ☐ | Router signal propagation |
| 5 | ☐ | Time-aware flight / package routing |
| 6 | ☐ | Shared-route meeting point |
| 7 | ☐ | Merge orderings via topological sort |
| 8 | ☑ | Character order from pairs |
| 9 | ☐ | Recursive placeholder substitution |
| 10 | ☐ | Filesystem / path hierarchy |
| 11 | ☑ | Max common prefix across files |
| 12 | ☐ | Longest increasing subsequence, adjacent diff |
| 13 | ☐ | Top K from a stream |
| 14 | ☐ | Move pieces to string (`L`/`R`/`_`) |
| 15 | ☐ | Interval overlap progression |
| 16 | ☐ | Product over last K of a stream |
| 17 | ☐ | Infix / postfix expression evaluation |
| 18 | ☐ | Merge two N-ary trees |
| 19 | ☑ | Tree leaves with max ancestor |
| 20 | ☐ | Connected components of 1-nodes in a binary tree |
| 21 | ☐ | Rectangle from 2D points |
| 22 | ☐ | Vertical line splitting rectangle area |
| 23 | ☐ | Longest non-decreasing subarray |
| 24 | ☐ | Remove adjacent character pairs |
| 25 | ☐ | Subsequence dictionary match |
| 26 | ☐ | Array jump — take or skip |
| 27 | ☐ | Arithmetic adjacent-diff subarrays |
| 28 | ☐ | Triples within max difference |
| 29 | ☐ | Logger rate limiter |
| 30 | ☐ | Music shuffler with no repeat in K |
| 31 | ☐ | Best café for friends |
| 32 | ☐ | Movie similarity Top N |
| 33 | ☐ | Teleporter shortest path |
| 34 | ☐ | Currency arbitrage |
| 35 | ☐ | Broadcast signal propagation |
| 36 | ☐ | Dependency cycles (SCC) |
| 37 | ☐ | Recipes from supplies |
| 38 | ☐ | Sentence similarity (transitive) |
| 39 | ☐ | Sequence reconstruction |
| 40 | ☐ | Token translator |
| 41 | ☐ | Build tree from parent-child pairs |
| 42 | ☐ | Merge N-ary trees with conflict rules |
| 43 | ☐ | Delete N-ary tree leaves |
| 44 | ☐ | Leaves grouped by removal round |
| 45 | ☐ | Count connected 1-components |
| 46 | ☐ | Largest connected 1-component |
| 47 | ☐ | Best root for a binary tree |
| 48 | ☐ | Reroot tree with color constraints |
| 49 | ☐ | Mouse jump max score |
| 50 | ☐ | F1 single-tyre race time |
| 51 | ☐ | F1 tyre-change DP |
| 52 | ☐ | Microwave keypad target time |
| 53 | ☐ | Count squares from segments |
| 54 | ☐ | Vertical area split |
| 55 | ☐ | Rectangle exists (incremental) |
| 56 | ☐ | Max rectangle area |

### Revision notes

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
