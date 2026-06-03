class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for(char task: tasks)
        {
            freq[task - 'A']++;
        }

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> (b - a));
        for(int count: freq)
        {
            if(count > 0)
                maxHeap.offer(count);
        }
        Queue<int[]> queue = new LinkedList<>();
        //use a queue to store tasks which are in cooldown
        int time = 0;
        while(!maxHeap.isEmpty() || !queue.isEmpty())
        {
            time++;
            if(!maxHeap.isEmpty())
            {
                int elementCount = maxHeap.poll();
                elementCount--;
                if(elementCount > 0) // still we need to schedule later, so keep it in cooldown
                {
                    queue.offer(new int[]{elementCount, time + n}); // at the time we can add it into the heap
                }
            }

            //now check queue if any cooldown element can be added
            while(!queue.isEmpty() && time >= queue.peek()[1])
            {
                maxHeap.offer(queue.poll()[0]);
            }
        } 

        return time;
    }
}