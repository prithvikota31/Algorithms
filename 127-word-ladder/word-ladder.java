class Solution {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordDict = new HashSet<>(wordList);

        if(!wordDict.contains(endWord))
        {
            return 0;
        }
        Deque<Node> q = new ArrayDeque<>();
        q.offer(new Node(beginWord, 1));
        wordDict.remove(beginWord);


        while(!q.isEmpty())
        {
            Node cur = q.poll();
            if(endWord.equals(cur.word))
            {
                return cur.distance;
            }

            char[] curChars = cur.word.toCharArray();
            //replace each letter from a - z
            for(int i = 0; i < curChars.length; i++)
            {
                char original = curChars[i];
                for(char c = 'a'; c <= 'z'; c++)
                {
                    if(c == original)
                    {
                        continue;
                    }
                    curChars[i] = c;

                    String nextStr = new String(curChars);
                    if(wordDict.contains(nextStr))
                    {
                        q.offer(new Node(nextStr, cur.distance + 1));
                        wordDict.remove(nextStr);
                    }
                }

                curChars[i] = original;
            }
        }

        return 0;
    }
}

class Node {
    String word;
    int distance;

    public Node(String word, int distance)
    {
        this.word = word;
        this.distance = distance;
    }
}