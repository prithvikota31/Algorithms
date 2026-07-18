# Google L4 Interview Prep — Problem List

> Ordering is by **observed recurrence and similarity** across the public corpus (2022–2025 Google reports), not exact occurrence counts. Distinct concrete formulations are kept **separate** even when the underlying technique is the same.

**Plan:** Solve the **56 priority problems** first (Phase 1). Then, if time permits, work through the remaining problems (Phase 2).

**Progress (Phase 1): 9 / 56 solved.**

---

## Phase 1 — The 56 Priority Problems (solve these first)

| # | Status | Problem |
|---|--------|---------|
| 1 | ☐ | Evaluate nested function expressions such as `mul(2, add(3,5))`; follow-ups: `sub`, `div`, `pow`, decimals, spaces, and invalid expressions. |
| 2 | ☑ | Grid source-to-target reachability; follow-up: maximize a safety metric such as the minimum distance from a cat or danger source. → [reachability](2-grid-reachability/GridReachability.java), [safest-path](2-grid-reachability/MaximumSafetyPath.java) |
| 3 | ☑ | Multi-source BFS: compute distance from every grid cell to its nearest source, boundary, or special object. → [solution](3-multi-source-bfs/MultiSourceBFS.java) |
| 4 | ☑ | Router signal propagation: determine whether a source router can reach a destination through routers within transmission range; follow-up: each router has its own radius. → [uniform-range](4-router-signal-propagation/RouterSignalPropagation.java), [per-router-radius](4-router-signal-propagation/RouterRadiusReachability.java) |
| 5 | ☐ | Time-aware flight or package routing: determine whether a package can reach a destination while respecting flight departure and arrival times. _(base done; return-the-path follow-up pending)_ → [solution](5-time-aware-routing/TimeAwarePackageRouting.java) |
| 6 | ☐ | Multiple people travel to one destination and may share parts of their routes; minimize total cost or number of distinct edges used. |
| 7 | ☐ | Given multiple preference or dependency orderings, construct one valid global topological ordering or determine that none exists. |
| 8 | ☑ | Given pairwise character relationships such as `a > b` and `b > c`, reconstruct a valid character order or report impossibility. → [solution](8-character-order-from-pairs/CharacterOrderFromPairs.java) |
| 9 | ☐ | Perform recursive placeholder substitution where replacement values may reference other placeholders; follow-up: detect cycles. |
| 10 | ☐ | Build a filesystem, URL, or path hierarchy and support queries involving descendants, prefixes, subtree properties, or aggregated values. |
| 11 | ☐ | Given files represented as arrays of lines, find the maximum common-prefix length between any pair of files. |
| 12 | ☐ | Find the longest increasing subsequence where adjacent difference is exactly 1; follow-up: difference at most D; follow-up: reconstruct the indices or path. |
| 13 | ☑ | Maintain or compute the Top K elements from a stream, such as users, words, scores, or records. → [solution](13-top-k-from-stream/TopKFromStream.java) |
| 14 | ☑ | Given strings containing `L`, `R`, and `_`, determine whether the start can reach the target when `L` moves only left and `R` moves only right. → [solution](14-move-pieces-to-string/MovePiecesToString.java) |
| 15 | ☐ | Interval progression: determine whether two intervals overlap; follow-ups: whether any pair overlaps, count overlaps, insert an interval, and merge overlaps. |
| 16 | ☑ | Design a stream data structure supporting `add(x)` and querying a product, mean, or statistic over the last K values; follow-up: dynamic K. → [fixed-K](16-product-last-k-stream/LastKProduct.java), [dynamic-K](16-product-last-k-stream/ProductOfNumbers.java) |
| 17 | ☐ | Evaluate arithmetic expressions represented in infix or postfix form using stacks; follow-ups may modify the supported grammar. |
| 18 | ☐ | Merge two trees or N-ary trees by recursively combining children with matching names while following value-conflict rules. |
| 19 | ☐ | Process tree leaves based on maximum ancestor values, root-to-leaf properties, or iterative leaf removal. |
| 20 | ☐ | Given a binary tree whose nodes contain 0 or 1, count connected components of 1 nodes or find the largest such component. |
| 21 | ☐ | Maintain points on a 2D plane; determine whether rectangles exist and follow up by finding the maximum rectangle area. |
| 22 | ☐ | Given rectangles on a plane, find a vertical line that divides their total area equally; variation: rectangles may overlap. |
| 23 | ☑ | Find the longest non-decreasing contiguous subarray; follow-up: change one value arbitrarily to maximize the result. → [solution](23-longest-non-decreasing-subarray/LongestNonDecreasingSubarray.java) |
| 24 | ☐ | Remove adjacent invalid character pairs, such as the same letter in opposite cases; follow-up: solve without an explicit stack. |
| 25 | ☐ | Given a dictionary of special words, determine whether qualifying subsequences of an input string belong to the dictionary. |
| 26 | ☐ | Given an array, from index i either skip or take the element; taking it adds a score and jumps according to `arr[i]`; maximize total score. |
| 27 | ☐ | Find arithmetic subarrays where every adjacent difference is exactly +1 or exactly -1. |
| 28 | ☐ | Given three sorted arrays, find triples containing one value from each array such that every pairwise difference is at most D. |
| 29 | ☐ | Logger rate limiter: suppress duplicate messages that occur again within a specified time window. |
| 30 | ☐ | Design a random music shuffler where no song may repeat within the previous K plays while maintaining correct random selection. |
| 31 | ☑ | Given friends located on graph nodes and cafés on other nodes, choose the café minimizing the maximum distance traveled by any friend. → [solution](31-best-cafe-for-friends/FindBestCafe.java) |
| 32 | ☐ | Given a movie-similarity graph and a starting movie, find the Top N reachable movies by rating. |
| 33 | ☐ | Find the shortest path through broken teleporters; follow-up: repaired teleporters introduce 0/1 edge costs. |
| 34 | ☐ | Given currency exchange rates, determine whether an arbitrage cycle exists. |
| 35 | ☐ | Determine how broadcast signals can propagate through a network of connected or reachable transmitters. |
| 36 | ☐ | Given an issue-blocker or dependency graph, find all dependency cycles. |
| 37 | ☐ | Given recipes, required ingredients, and available supplies, determine which recipes can eventually be produced. |
| 38 | ☐ | Given pairs of similar sentences or words, determine similarity using transitive relationships. |
| 39 | ☐ | Given multiple subsequences describing ordering constraints, determine whether a target sequence is uniquely reconstructible. |
| 40 | ☐ | Build a translator using mappings or dependency relationships between language tokens. |
| 41 | ☐ | Given parent-child relationships, construct the corresponding tree. |
| 42 | ☐ | Merge two N-ary trees while resolving matching children and field conflicts according to specified rules. |
| 43 | ☐ | Repeatedly delete leaves from a multi-tree according to the required deletion process. |
| 44 | ☐ | Return tree leaves grouped by the round in which they would be removed. |
| 45 | ☐ | Count connected components of 1-valued nodes in a tree. |
| 46 | ☐ | Find the largest connected component of 1-valued nodes in a tree. |
| 47 | ☐ | Given an undirected tree where node degree is bounded, choose the best root so the rooted structure satisfies binary-tree constraints. |
| 48 | ☐ | Reroot a tree while satisfying alternating node-color constraints by level. |
| 49 | ☐ | A mouse moves from index i to a later index j and earns `(j - i) * nums[j]`; maximize the total score to reach the end. |
| 50 | ☐ | Given F1 tyres with initial lap time and degradation factor, find the minimum race time using one tyre. |
| 51 | ☐ | Extend the F1 tyre problem by allowing tyre changes with a fixed replacement cost; minimize total race time. |
| 52 | ☐ | Given a microwave keypad and target cooking time, choose an input near the target while minimizing keypresses and finger-movement cost. |
| 53 | ☐ | Given horizontal and vertical line segments, count the number of squares they form. |
| 54 | ☐ | Given rectangles, find the vertical line that splits their total area equally. |
| 55 | ☐ | Maintain a set of 2D points and answer whether a rectangle can be formed from stored points. |
| 56 | ☐ | Given a set of 2D points, find the maximum-area rectangle that can be formed. |

> Note: #54 and #22 describe the same core "split rectangle area with a vertical line" idea; they are kept separate here to preserve the original formulations. Numbering matches the originally selected 56 (the earlier duplicate router-reachability item and the router-activation-time item were dropped; the two point-set rectangle problems are #55–#56).

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
- ☐ Find all dependency cycles in an issue/blocker relationship graph.
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
