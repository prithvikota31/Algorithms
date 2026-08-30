import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //recipe , ingredients
        //graph edge
        //ingredient -> recipe
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        //indegree ingreident/recipe -> count

        for(int i = 0; i < ingredients.size(); i++)
        {
            String recipe = recipes[i];
            for(String ingredient: ingredients.get(i))
            {
                graph.computeIfAbsent(ingredient, k -> new ArrayList<>()).add(recipe);

            }
            inDegree.putIfAbsent(recipe, ingredients.get(i).size());

        }

        Deque<String> q = new ArrayDeque<>();

        for(String supply: supplies)
        {
            q.offer(supply);
        }
        List<String> result = new ArrayList<>();
        while(!q.isEmpty())
        {
            String cur = q.poll();
            //initial supplies should not be added to result;
            for(String nei: graph.getOrDefault(cur, new ArrayList<>()))
            {
                inDegree.put(nei, inDegree.get(nei) - 1);
                if(inDegree.get(nei) == 0)
                {
                    result.add(nei);
                    q.offer(nei);
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
