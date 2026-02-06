class Solution {
    public int leastInterval(char[] tasks, int n) {
        Map<Character, Integer> map = new HashMap<>();

        for(char t: tasks)
        {
            map.put(t, map.getOrDefault(t, 0) + 1);
        }
        PriorityQueue<Integer> maxHeap =
            new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.addAll(map.values());

        
        int time = 0;
        while(!maxHeap.isEmpty())
        {
            List<Integer> temp = new ArrayList<>();
            for(int i = 0; i <= n; i++) //n + 1 values
            {
                if(!maxHeap.isEmpty())  
                {
                    temp.add(maxHeap.poll() - 1); // 0s can go into it
                }
            }

            for(int i = 0; i < temp.size(); i++)
            {
                if(temp.get(i) != 0)
                    maxHeap.offer(temp.get(i));
            }
            time +=  (maxHeap.isEmpty())? temp.size() : n + 1; // n + 1 or if maxheap is empty then temp.size()
        }


        return time;

    }
}