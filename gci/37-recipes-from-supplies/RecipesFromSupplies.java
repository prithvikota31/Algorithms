import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/*
 * ============================================================================
 * Problem 37 (Google L4 prep) — Recipes From Supplies
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * recipes[i] requires ingredients.get(i) (a list of ingredient/recipe names).
 * You start with a set of raw `supplies`. Making a recipe successfully turns
 * it INTO a supply, which other recipes may then depend on. Return every
 * recipe that can eventually be produced, IN A VALID PRODUCTION ORDER.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   An ingredient name that never appears in `supplies` and is never itself
 *   a producible recipe means every recipe depending on it (transitively)
 *   can never be made — it should simply be left out of the result, not
 *   throw an error.
 *
 * EXAMPLE
 *   recipes = ["bread", "sandwich"]
 *   ingredients: bread -> [flour], sandwich -> [bread, ham]
 *   supplies = [flour, ham]
 *   flour lets you make bread; bread+ham lets you make sandwich.
 *   -> ["bread", "sandwich"]
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: TOPOLOGICAL SORT (Kahn's / BFS), same shape as Course Schedule —
 * except a "completed course" (finished recipe) can act as a brand-new
 * supply for other recipes.
 *
 *   remaining[recipe] = number of ingredients NOT YET available
 *   dependents[ingredient] = recipes waiting on that ingredient
 *
 * Seed the queue with the initial `supplies` (treat them as already
 * "finished prerequisites"). Whenever an item becomes available, decrement
 * `remaining` for every recipe waiting on it; once a recipe's `remaining`
 * hits 0, every ingredient it needs is available, so it can be made — add
 * it to the result AND enqueue it (it may unlock further recipes).
 *
 * The BFS processing order already IS a valid production order (nothing is
 * ever enqueued before all of its own ingredients were dequeued first), so
 * "return the recipes in a valid production order" needs no extra code —
 * it falls directly out of the base BFS.
 *
 * A recipe stuck in a dependency CYCLE, or depending on an ingredient that's
 * never supplied, simply never reaches remaining == 0, so it's naturally
 * excluded from the result without any special-case code.
 *
 * APPROACHES
 *   Brute force : repeatedly scan all recipes, checking whether each one's
 *                 ingredients are all currently available, until a full pass
 *                 makes no progress. O(n^2) worst case, no queue.
 *   Optimal     : Kahn's-style topological BFS (below). O(total ingredient
 *                 references).
 *
 * COMPLEXITY
 *   Time O(R + total ingredient references), where R = number of recipes
 *   Space O(R + total ingredient references)
 * ----------------------------------------------------------------------------
 */
public class RecipesFromSupplies {

    public List<String> findAllRecipes(String[] recipes, List<List<String>> ingredients,
            String[] supplies) {
        Map<String, List<String>> dependents = new HashMap<>();
        Map<String, Integer> remaining = new HashMap<>();

        for (int i = 0; i < recipes.length; i++) {
            String recipe = recipes[i];
            remaining.put(recipe, ingredients.get(i).size());

            for (String ingredient : ingredients.get(i)) {
                dependents.computeIfAbsent(ingredient, k -> new ArrayList<>()).add(recipe);
            }
        }

        Queue<String> queue = new ArrayDeque<>();
        for (String supply : supplies) {
            queue.offer(supply);
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String availableItem = queue.poll();

            if (!dependents.containsKey(availableItem)) {
                continue;
            }

            for (String recipe : dependents.get(availableItem)) {
                int stillNeeded = remaining.get(recipe) - 1;
                remaining.put(recipe, stillNeeded);

                if (stillNeeded == 0) {
                    result.add(recipe);
                    queue.offer(recipe);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        RecipesFromSupplies solution = new RecipesFromSupplies();

        check("worked example",
                solution.findAllRecipes(
                        new String[] { "bread", "sandwich" },
                        List.of(List.of("flour"), List.of("bread", "ham")),
                        new String[] { "flour", "ham" }),
                List.of("bread", "sandwich"));

        // sandwich needs ham, which never arrives -> only bread is producible.
        check("missing ingredient blocks only its dependents",
                solution.findAllRecipes(
                        new String[] { "bread", "sandwich" },
                        List.of(List.of("flour"), List.of("bread", "ham")),
                        new String[] { "flour" }),
                List.of("bread"));

        // a depends on b, b depends on a -> neither ever becomes producible.
        check("dependency cycle excludes both recipes",
                solution.findAllRecipes(
                        new String[] { "a", "b" },
                        List.of(List.of("b"), List.of("a")),
                        new String[] {}),
                List.of());

        // Order must respect dependencies even with unrelated supplies mixed in.
        check("multi-step chain stays in valid production order",
                solution.findAllRecipes(
                        new String[] { "bread", "toast", "sandwich" },
                        List.of(List.of("flour"), List.of("bread"), List.of("toast", "ham")),
                        new String[] { "flour", "ham" }),
                List.of("bread", "toast", "sandwich"));

        check("no recipes producible from empty supplies",
                solution.findAllRecipes(
                        new String[] { "bread" },
                        List.of(List.of("flour")),
                        new String[] {}),
                List.of());

        System.out.println("all passed");
    }

    private static void check(String name, List<String> actual, List<String> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
