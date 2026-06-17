class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();

        for(int num: nums)
        {
            set.add(num);
        }
        int max = 0;

        for(int num: set)
        {
            if(set.contains(num - 1))
            {
                continue;
            }

            int count = 0;
            while(set.contains(num))
            {
                count++;
                num++;
            }

            max = Math.max(max, count);
        }
        return max;
    }
}