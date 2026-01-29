class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();

        for(int num: nums)
        {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = 
                            new PriorityQueue<>(k, (a,b) -> a.getValue() - b.getValue());

        for(Map.Entry<Integer, Integer> entry: freq.entrySet())
        {
            minHeap.offer(entry);

            if(minHeap.size() > k)
            {
                minHeap.poll();
            }
        }

        int[] ans = new int[k];

        for(int i = 0; i < k; i++)
        {
            ans[i] = minHeap.poll().getKey();
        }

        return ans;
    }
}