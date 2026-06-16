class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> ans = new ArrayList<>();
        ans.add(intervals[0]);

        int n = intervals.length;
        int ind = 0;
        while(ind < n)
        {
            int[] lastInterval = ans.get(ans.size() - 1);
            if(intervals[ind][0] <= lastInterval[1]) //overlap
            {
                lastInterval[1] = Math.max(intervals[ind][1], lastInterval[1]);
            }
            else
            {
                ans.add(intervals[ind]);
            }
            ind++;
        }

        return ans.toArray(new int[0][]);
    }
}