class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();

        for(int num: nums)
        {
            set.add(num);
        }
        int ans = 0;
        int count = 0;

        for(int num: set)
        {
            if(set.contains(num + 1))
            {
                continue;
            }

            while(set.contains(num))
            {
                count++;
                num -= 1;
            }

            ans = Math.max(count, ans);
            count = 0;
        }

        return ans;

    }
}