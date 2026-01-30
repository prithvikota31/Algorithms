class Solution {
    public int longestConsecutive(int[] nums) {
        int result = 0;

        Set<Integer> set = new HashSet<>();

        //maintain a unique value set
        for(int num: nums)
        {
            set.add(num);
        } 

        for(int val: set)
        {
            if(set.contains(val - 1))
            {
                continue;
            }
            else //start point
            {
                int cur = val;
                int len = 1;
                while(set.contains(cur + 1))
                {
                    cur++;
                    len++;
                }
                result = Math.max(result, len);
            }
        }

        return result;

        
    }
}