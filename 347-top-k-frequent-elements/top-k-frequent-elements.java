class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // 1. Use a Counter collection for frequency counts - this produces the same outcome as our verbose implementation in the brute force approach.
        Map<Integer, Integer> counts = new HashMap<>();
        for (int num : nums) {
            counts.put(num, counts.getOrDefault(num, 0) + 1);
        }
        
        // 2. Initialize a min heap
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(k, (a, b) -> a.getValue() - b.getValue());
        
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
        
            // 3. Manage the heap
            // Add each element to the heap
            minHeap.offer(entry);
            
            // If the size of the heap is larger than k, we pop the element at the top.
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        
        // 4. What remains in the heap are the top k most frequent
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll().getKey();
        }
        
        return result;
    }
}