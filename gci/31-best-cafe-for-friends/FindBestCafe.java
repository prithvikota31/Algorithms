/*
 * ============================================================================
 * Problem 31 (Google L4 prep) — Best Café for All Friends
 *                               (minimize the MAX friend distance)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Unweighted undirected graph. Some nodes hold friends, some hold cafés.
 * Choose the café where the farthest-traveling friend walks the least:
 *      score(cafe)  = max over friends of dist(friend, cafe)
 *      answer       = café with the minimum score
 * Return -1 if no café is reachable by EVERY friend.
 *
 * EXAMPLE
 *   friends=[1,7]  cafes=[5,6]
 *   edges: 1-2,2-3,3-4,4-5,3-6,7-5,7-3
 *              cafe5  cafe6
 *   friend1     4      3
 *   friend7     1      2
 *   max         4      3   -> answer = cafe 6
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * CRITICAL distinction: a single multi-source BFS from all friends gives the
 * MINIMUM friend distance to each café — wrong here. We need the MAXIMUM across
 * friends. e.g. friend distances [1, 10] -> multi-source BFS says 1, but the
 * fair score is 10. One shared visited[] loses the other friends' info.
 *
 * Assuming there are fewer cafés than friends, BFS separately FROM EACH CAFÉ.
 * During that BFS, count reached friends and track the farthest one's distance.
 * Reject a café unless every friend is reached. Among the remaining cafés,
 * choose the one with the smallest farthest-friend distance.
 *
 * APPROACHES
 *   Brute force : enumerate paths / Floyd-Warshall O(V^3) — computes all-pairs
 *                 though we only care about friends x cafés.
 *   Chosen      : one BFS per café because C < F.
 *                 Time O(C*(V+E))   Space O(V+E).
 *
 * Memory trick: nearest source -> MIN; fair meeting place -> track the MAX,
 * then minimize it.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class FindBestCafe {
    // Assumption: cafés are fewer than friends, so BFS starts from each café.
    public int findBestCafe(int n, int[][] edges, int[] friends, int[] cafes) {
        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int[] edge: edges)
        {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        int[] friendMap = new int[n];
        for(int i = 0; i < friends.length; i++)
        {
            int index = friends[i];
            friendMap[index] = 1;
        }
        //all 1s in friendMap are friends
        int bestCafe = -1;
        int bestDistance = Integer.MAX_VALUE;
        for(int cafe: cafes)
        {
            int[] distanceAndFriendVisitedCount = bfs(graph, cafe, friendMap);
            if(distanceAndFriendVisitedCount[1] != friends.length)
            {
                continue;
            }

            if(bestDistance > distanceAndFriendVisitedCount[0])
            {
                bestDistance = distanceAndFriendVisitedCount[0];
                bestCafe = cafe;
            }
        }

        return bestCafe;
    }

    // Result contains maximum friend distance and visited friend count.
    private int[] bfs(List<List<Integer>> graph, int cafe, int[] friendMap)
    {
        //lets start from cafe and when we visit friend increase the count
        //finally save the maxdistance of friend and also did count == totalfriends
        int tempFriendCount = 0;
        int[] distance = new int[graph.size()];
        Arrays.fill(distance, -1);
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(cafe);
        distance[cafe] = 0;
        int maxFriendDistanceFromCafe = 0;
        while(!q.isEmpty())
        {
            int cur = q.poll();

            if (friendMap[cur] == 1) {
                tempFriendCount++;
                maxFriendDistanceFromCafe =
                        Math.max(maxFriendDistanceFromCafe, distance[cur]);
            }

            for(int nei: graph.get(cur))
            {
                if(distance[nei] == -1)
                {
                    distance[nei] = 1 + distance[cur];
                    q.offer(nei);
                }
            }
        }

        return new int[]{maxFriendDistanceFromCafe, tempFriendCount};

    }
    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        FindBestCafe sol = new FindBestCafe();

        int[][] edges = {
            {1, 2}, {2, 3}, {3, 4}, {4, 5}, {3, 6}, {7, 5}, {7, 3}
        };
        System.out.println(sol.findBestCafe(8, edges, new int[] {1, 7}, new int[] {5, 6})); // 6

        // Single friend -> just the nearest café.
        System.out.println(sol.findBestCafe(8, edges, new int[] {1}, new int[] {5, 6}));    // 6 (dist 3 < 4)

        // A café unreachable by one friend is rejected.
        int[][] edges2 = {
            {0, 1}, {1, 2},          // friend 0 side
            {3, 4}                   // isolated component holds café 4
        };
        System.out.println(sol.findBestCafe(5, edges2, new int[] {0, 3}, new int[] {2, 4})); // -1
    }
}
