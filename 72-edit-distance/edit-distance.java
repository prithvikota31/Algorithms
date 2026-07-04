class Solution {
    public int minDistance(String word1, String word2) {

        //f(i, j) represents min operations required to covert from word1 at ith index to word2 at jth index
        //f(i, j) = {      //replace
        //}

        int m = word1.length();
        int n = word2.length();
        int[] cur = new int[n + 1];
        int[] prev = new int[n + 1];

        //first row
        for(int j = 0; j <= n; j++)
        {
            prev[j] = j;
        }

        for(int i = 1; i <= m; i++)
        {
            cur[0] = i;
            for(int j = 1; j <= n; j++)
            {
                if(word1.charAt(i - 1) == word2.charAt(j - 1))
                {
                    cur[j] = prev[j - 1];
                }
                else
                {
                    //replace, insert, delete
                    cur[j] = 1 + Math.min(prev[j - 1], Math.min(cur[j - 1], prev[j]));
                }
            }
            prev = cur.clone();

        }
        //prev -> a
        //cur -> b
        // prev -> b

        return prev[n];
    }
}