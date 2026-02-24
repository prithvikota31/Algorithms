class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int n = intervals.length;
        //first part non overlapping intervals[end] < newInterval[start]
        int idx = 0;
        while(idx < n && intervals[idx][1] < newInterval[0])
        {
            result.add(intervals[idx]);
            idx++;
        }
        //so intervals[end] >= newInterval[start]
        //second part overlapping newInterval[end] > intervals[start]
        //eg [4, 6], [4, 7] => [6, 9]
        while(idx < n && intervals[idx][0] <= newInterval[1])
        {
            newInterval[0] = Math.min(intervals[idx][0], newInterval[0]);
            newInterval[1] = Math.max(intervals[idx][1], newInterval[1]);
            idx++;
        }
        result.add(newInterval);


        while(idx < n && intervals[idx][0] > newInterval[1])
        {
            result.add(intervals[idx]);
            idx++;
        }



        return result.toArray(new int[result.size()][]);
    }
}