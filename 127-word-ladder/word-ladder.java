class Solution {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);

        if(!wordSet.contains(endWord))
        {
            return 0;
        }

        Deque<Pair> q = new ArrayDeque<>();
        q.offer(new Pair(1, beginWord));
        wordSet.remove(beginWord);

        while(!q.isEmpty())
        {
            Pair cur = q.poll();
            int cDist = cur.distance;
            String cWord = cur.word;
            if(cWord.equals(endWord))
            {
                return cDist;
            }

            //try every char with a to z
            char[] cWordArray = cWord.toCharArray();
            for(int i = 0; i < cWordArray.length; i++)
            {
                char initChar = cWordArray[i];
                for(char c = 'a'; c <= 'z'; c++)
                {
                    cWordArray[i] = c;

                    String nextPossibleWord = new String(cWordArray);
                    if(wordSet.contains(nextPossibleWord))
                    {
                        q.offer(new Pair(cDist + 1, nextPossibleWord));
                        wordSet.remove(nextPossibleWord);
                    }
                }
                cWordArray[i] = initChar;
            }
        }

        return 0;
    }
}

class Pair
{
    int distance;
    String word;

    public Pair(int d, String w)
    {
        word = w;
        distance = d;
    }

}