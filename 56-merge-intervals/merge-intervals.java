class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> (a[0] - b[0]));
        List<int[]> trackingList = new ArrayList<>();

        trackingList.add(intervals[0]); // minimum 1 interval is present per constraint

        int ind = 1;

        while(ind < intervals.length)
        {
            int[] lastinterval = trackingList.get(trackingList.size() - 1);
            //lastinterval.start >= intervals.end 
            if(lastinterval[0] <= intervals[ind][1] &&
                             lastinterval[1] >= intervals[ind][0])
            {
                lastinterval[0] = Math.min(lastinterval[0], intervals[ind][0]);
                lastinterval[1] = Math.max(lastinterval[1], intervals[ind][1]);
            }
            else
            {
                trackingList.add(intervals[ind]);
            }
            ind++;
        }

        int[][] result = trackingList.toArray(new int[0][]);
        return result;
    }
}