class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>(); //integer frequency

        for(int num: nums)
        {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        List<Integer> freqList = new ArrayList<>(map.keySet());
        PriorityQueue<Integer> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(map.get(a), map.get(b)));

        for(int freq: freqList)
        {
            minHeap.offer(freq);

            if(minHeap.size() > k)
            {
                minHeap.poll();
            }
        }

        int[] ans = new int[k];

        for(int i = 0; i < k; i++)
        {
            ans[i] = minHeap.poll();
        }

        return ans;
    }
}