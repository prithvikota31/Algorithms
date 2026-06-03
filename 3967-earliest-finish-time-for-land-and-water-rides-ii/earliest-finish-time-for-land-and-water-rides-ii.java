class Solution {
    public int earliestFinishTime(int[] landStartTime, int[] landDuration, int[] waterStartTime, int[] waterDuration) {
        int landFirst = solve(landStartTime, landDuration, waterStartTime, waterDuration);
        int waterFirst = solve(waterStartTime, waterDuration, landStartTime, landDuration);

        return Math.min(landFirst, waterFirst);
    }

    private int solve(int[] start1, int[] duration1, int[] start2, int[] duration2)
    {
        //earliest finish time of set 1
        int finished1 = Integer.MAX_VALUE;
        for(int i = 0; i < start1.length; i++)
        {
            finished1 = Math.min(finished1, start1[i] + duration1[i]);
        }

        //now get second ride best possibility
        int finished2 = Integer.MAX_VALUE;
        for(int i = 0; i < start2.length; i++)
        {
            finished2 = Math.min(finished2, (Math.max(finished1, start2[i]) +  duration2[i]));
        }

        return finished2;
    }
}