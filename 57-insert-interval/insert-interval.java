class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        //check overlap, if overlap, remove that from list and merge again

        List<int[]> ans = new ArrayList<>();

        //completely before overlap
        //during overlap
        //completely after overlap
        int n = intervals.length;
        int ind = 0;

        //nonoverlap front
        while(ind < n && intervals[ind][1] < newInterval[0])
        {
            ans.add(intervals[ind]);
            ind++;
        }

        //during overlap
        //but already [ind][1] > newInterval[0]; covered above
        //(interval.end, new.start) condition for overlap
        //newInterval[0] <= intervals[ind][1] is already satisfied above
        while(ind < n && newInterval[1] >= intervals[ind][0] && 
                newInterval[0] <= intervals[ind][1])
        {
            newInterval[0] = Math.min(newInterval[0], intervals[ind][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[ind][1]);
            ind++;
        }

        ans.add(newInterval);

        //after overlap
        while(ind < n)
        {
            ans.add(intervals[ind]);
            ind++;
        }

        return ans.toArray(new int[0][]);

    }
}