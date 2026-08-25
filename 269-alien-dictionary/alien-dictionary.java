class Solution {
    public String alienOrder(String[] words) {
        //build an empty graph
        //indegree and graph
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        for(String word: words)
        {
            for(int i = 0; i < word.length(); i++)
            {
                char ch = word.charAt(i);
                graph.putIfAbsent(ch, new HashSet<>());
                inDegree.putIfAbsent(ch, 0);
            }
        }

        // now build graph
        for(int i = 0; i <= words.length - 2; i++)
        {
            String w1 = words[i];
            String w2 = words[i + 1];
            //eg: abcd, ab
            if(w1.length() > w2.length() && w1.startsWith(w2))
            {
                return "";
            }

            int minlen = Math.min(w1.length(), w2.length());
            for(int j = 0; j < minlen; j++)
            {
                char c1 = w1.charAt(j);
                char c2 = w2.charAt(j);
                if(c1 != c2)
                {
                    //check if already this edge is added
                    boolean newlyAdded = graph.get(c1).add(c2);
                    if(newlyAdded)
                    {
                        inDegree.put(c2, inDegree.get(c2) + 1);
                    }
                    break;
                }
            }
        }
        //graph and indegree built

        
        //now kahns
        Deque<Character> q = new ArrayDeque<>();

        for(char ch: inDegree.keySet())
        {
            int count = inDegree.get(ch);
            if(count == 0)
            {
                q.offer(ch);
            }
        }
        StringBuilder topo = new StringBuilder();
        while(!q.isEmpty())
        {
            char ch = q.poll();
            topo.append(ch);

            for(char nei: graph.get(ch))
            {
                inDegree.put(nei, inDegree.get(nei) - 1);
                if(inDegree.get(nei) == 0)
                {
                    q.offer(nei);
                }
            }
        }


        if(topo.length() == inDegree.size())
        {
            return topo.toString();
        }

        return "";
    }
}