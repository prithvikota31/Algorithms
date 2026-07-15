---
name: solve-coding-problem
description: "A repeatable methodology for solving ANY coding/DSA interview problem with strong first-principles intuition, then writing a clean, well-documented solution file in the Algorithms repo's house style. USE WHEN: asked to solve a LeetCode/interview problem, add a solution to the Algorithms folder, explain the intuition behind an algorithm, or pick the right data structure/algorithm for a problem. This is a LIVING skill — improve it after every problem we solve together."
---

# Solve Coding Problem — Intuition-First Methodology

Goal: given any coding problem, arrive at the **best** solution by reasoning from
first principles (not pattern-memorization), then persist it as a clean file that
matches this repo's conventions. Keep improving this skill after each problem.

## Communication style (IMPORTANT)

> ROUTING: For a **follow-up question** about an already-solved problem (e.g.
> "what is X?", "explain this line", "why this DS?"), load ONLY this
> Communication style section — do NOT pull in the rest of the skill. Read the
> full skill only when asked to **solve / add / explain a whole problem** from
> scratch.

- Keep answers SHORT. For follow-up questions (e.g. "what is X?", "explain this
  part"), reply in a few tight sentences or a small bullet list — just enough to
  understand, no walls of text.
- No repeated preamble, no re-explaining the whole problem, no exhaustive
  edge-case tours unless explicitly asked.
- Prefer one small concrete example over long prose. Expand into depth ONLY when
  the user asks "explain in detail" / "go deep".

## 0. Repo conventions (Algorithms folder)

- LeetCode problems live in `Algorithms/<number>-<slug>/<slug>.java` with a
  `README.md` describing the problem.
- Google-prep / custom problems live in `Algorithms/gci/<N>-<slug>/<ClassName>.java`
  where `N` is the problem's number in `gci/google-l4-prep.md`.
- Every solution file starts with a **header comment block** containing, in order:
  1. Title line (problem number + short name)
  2. `PROMPT` — the problem restated concisely
  3. `EXAMPLES` — 2-3 input/output pairs incl. an edge/impossible case
  4. `INTUITION` — the key insight, why this reduces to a known technique
  5. `ALGORITHM` — numbered steps
  6. `COMPLEXITY` — Time / Space with variable definitions
- Include a small `main` self-test when the problem isn't a bare LeetCode class.
- Java is the default language in this repo (`class Solution { ... }` for LC).
- After solving, tick the box in `gci/google-l4-prep.md` (☐ → ☑) if it's a prep item.

Note: `javac`/`java` may NOT be installed in the environment. If compilation
fails with "command not found", verify correctness by hand-tracing the examples
instead of installing a JDK (don't install toolchains unless asked).

## 1. Understand (restate before coding)

- Restate the problem in one sentence. Identify **inputs, outputs, constraints**.
- Nail the exact semantics of ambiguous words (e.g. does `a > b` mean "a before b"
  or "a is larger"? does "subarray" mean contiguous?).
- Enumerate edge cases up front: empty input, single element, duplicates,
  cycles, disconnected components, overflow, negative numbers, ties.

## 2. Find the reduction (the core skill)

Ask: **"What known structure is this secretly?"** Map surface story → abstraction.

Common reductions (story cue → technique):
- "x must come before y" / "prerequisites" / "order from pairs" → **topological
  sort** (Kahn's BFS gives order + cycle detection for free). DAG ⇔ order exists.
- "reach from A to B" on grid/graph, unweighted → **BFS**; weighted → **Dijkstra**;
  0/1 weights → **0-1 BFS (deque)**; negative → **Bellman-Ford** (also arbitrage).
- "distance from every cell to nearest source" → **multi-source BFS** (seed queue
  with all sources at once).
- "min of max" / "max of min" / "can we do it within K?" → **binary search on the
  answer** + feasibility check.
- "last K / window" → **sliding window** or **monotonic deque**; "top K" → **heap**.
- "overlaps / merge / earliest" on intervals → **sort by start/end + sweep**.
- "count/optimize over choices with overlapping subproblems" → **DP** (define the
  state as the smallest thing that makes the future independent of the past).
- "next greater / spans / valid parentheses" → **stack** (often monotonic).
- "group things that share a property transitively" → **union-find / DFS components**.
- "prefix / dictionary / path hierarchy" → **trie**.

If no clean reduction: brute force first, then look at what's recomputed → cache
it (DP/memo) or what's re-scanned → precompute/index it (prefix sums, sorting).

## 3. Justify optimality

- State brute-force complexity, then the target complexity you're aiming for.
- Argue the chosen approach hits a natural lower bound (e.g. if you must read the
  whole input once, linear time is optimal).
- Prefer the algorithm whose **failure/edge behavior also answers a likely
  follow-up**, so the same code generalizes instead of being rewritten.

## 4. Implement cleanly

- Model the state/structure explicitly and completely (don't drop elements that
  only appear implicitly, e.g. isolated nodes, empty buckets, boundary cells).
- Normalize/deduplicate inputs when repeats could corrupt counts or aggregates.
- Use names tied to the intuition so the code reads like the ALGORITHM steps.
- Keep the code a direct transcription of the ALGORITHM block in the header.

## 5. Verify

- Hand-trace each EXAMPLE, including the edge/impossible case.
- Check that every non-happy-path return (empty / -1 / null / false) is reachable
  and correct.
- If the language's toolchain is present, compile & run the `main` self-test.

## 6. Record & iterate

- Tick the prep list; commit only the new solution file(s) (repo has many unrelated
  in-progress edits — never `git add -A`, add explicit paths).
- **Update this skill**: if the problem revealed a new reduction cue, a pitfall, or
  a cleaner template, append it to section 2 or the Pitfalls list below.

## Pitfalls log (generic lessons — append as we learn)

- Implicit elements: register everything the answer must cover, including items
  that appear only indirectly (isolated nodes, unseen keys, boundary cells).
- Duplicate inputs: dedupe before counting/aggregating when repeats would skew a
  count, degree, or sum and break a termination condition.
- Termination/size checks: compare against the count of **distinct entities**, not
  the raw input length.
- Off-by-one & boundaries: inclusive vs exclusive ranges, empty and single-element
  inputs, first/last iteration.
- Overflow & types: sums/products may exceed `int`; pick the right width early.
- Don't install compilers/toolchains just to "verify" — hand-trace if the toolchain
  is absent (see the note in section 0).
- NEVER run `git checkout -- .` / `git reset --hard` to clean line-ending or
  whitespace noise: it also discards the user's UNCOMMITTED work. Scope the
  cleanup (specific paths) or commit/stash the user's file first. For CRLF noise,
  prefer `git config core.autocrlf input` + `git add --renormalize .` and only
  discard the renormalized files, not the working edits.

## Solved log (append one line per problem)

<!-- Format: <id> — <one-line problem> → <technique>. File: <path>. -->
- gci #10 — character order from `a>b` pairs → topological sort (Kahn's BFS);
  cycle ⇒ "". File: `gci/10-character-order-from-pairs/`.
- gci #14 — longest increasing subsequence, adjacent diff exactly 1 (follow-ups:
  diff ≤ D, path reconstruction) → value-keyed DP; O(n) hashmap for diff 1,
  segment-tree range-max for diff ≤ D, parent pointers for the path.
  File: `gci/14-longest-inc-subseq-adjacent-diff/`.
