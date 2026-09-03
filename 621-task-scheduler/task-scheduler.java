class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];

        for(char task: tasks)
        {
            freq[task - 'A']++;
        }

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

        for(int count: freq)
        {
            if(count != 0)
            {
                maxHeap.offer(count);
            }
        }

        Queue<int[]> q = new LinkedList<>();
        int time = 0;
        while(!maxHeap.isEmpty() || !q.isEmpty())
        {
            time++;
            if(!maxHeap.isEmpty())
            {
                int curTaskCount = maxHeap.poll();
                //decrement
                curTaskCount = curTaskCount - 1;
                if(curTaskCount > 0)
                    q.offer(new int[]{curTaskCount, time + n});
            }

            while(!q.isEmpty() && time >= q.peek()[1])
            {
                maxHeap.offer(q.poll()[0]);
            }
        }

        return time;
    }
}