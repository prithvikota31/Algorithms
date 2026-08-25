class Solution {
    public String alienOrder(String[] words) {

        HashMap<Character, Integer> indegree = new HashMap<>();
        HashMap<Character, Set<Character>> graph = new HashMap<>();

        int l = words.length;

        // create nodes in graph

        for(String word : words)
        {
            for(int i = 0; i < word.length(); i++)
            {
                graph.putIfAbsent(word.charAt(i), new HashSet<>());
                indegree.putIfAbsent(word.charAt(i), 0);
                
            }
        }

        // iterate through words and fill both maps

        for(int i = 0; i < l - 1; i++)
        {
            String w1 = words[i];
            String w2 = words[i+1];

            if(w1.length() > w2.length() && w1.startsWith(w2))
            {
                return "";
            }

            int l1 = Math.min(w1.length(), w2.length());

            for(int j = 0; j < l1; j++)
            {
                char c1 = w1.charAt(j);
                char c2 = w2.charAt(j);

                if(c1 != c2)
                {
                    boolean added = graph.get(c1).add(c2);
                    if(added)
                    {
                        indegree.put(c2, indegree.get(c2) + 1);
                    }
                    
                    break;
                }
            }
        }

        //take a queue for top sort

        Queue<Character> q = new LinkedList<>();

        StringBuilder sb = new StringBuilder();

        for(char c : indegree.keySet())
        {
            if(indegree.get(c) == 0)
            {
                q.offer(c);
            }
        }

        while(!q.isEmpty())
        {
            char curr = q.poll();

            sb.append(curr);

            for(char c : graph.get(curr))
            {
                indegree.put(c, indegree.get(c) - 1);
                if(indegree.get(c) == 0)
                {
                    q.offer(c);
                }
            }
        }

        if(sb.length() != indegree.size())
        {
            return "";
        }

        return sb.toString();

    }
}