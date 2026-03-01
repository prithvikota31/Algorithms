class Solution {
    public int subarraySum(int[] nums, int k) {
        //use prefix sum
        Map<Integer, Integer> map = new HashMap<>();
        int prefixSum = 0;
        map.put(0, 1); //this implies we take all the times of the prefix till now
        int count = 0;
        for(int i = 0; i < nums.length; i++)
        {
            prefixSum += nums[i];

            if(map.containsKey(prefixSum - k))
            {
                count += map.get(prefixSum - k);
            }

            map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
        } 


        return count;
        
    }
}