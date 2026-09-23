class Solution {
    public int leastInterval(char[] tasks, int n) {
        Map<Character, Integer> taskFreq = new HashMap<>();
        //char -> count
        for(char task: tasks)
        {
            taskFreq.put(task, taskFreq.getOrDefault(task, 0) + 1);
        }

        //add all values into maxheap, we will try to finish maxfreq tasks first as they tend to create more gaps

        PriorityQueue<Integer> maxHeapTaskFreq = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        for(int val: taskFreq.values())
        {
            maxHeapTaskFreq.offer(val);
        }

        //int[] - (taskCount, time after which it can be done)
        Deque<int[]> tasksWaiting = new ArrayDeque<>();
        int time = 0;
        while(!maxHeapTaskFreq.isEmpty() || !tasksWaiting.isEmpty())
        {
            time++;
            //pick a task
            if(!maxHeapTaskFreq.isEmpty())
            {
                int curTaskCount = maxHeapTaskFreq.poll();
                curTaskCount--; //reduce count and put in waitingQ
                if(curTaskCount > 0)
                {
                    tasksWaiting.offer(new int[]{curTaskCount, time + n});
                }
            }

            while(!tasksWaiting.isEmpty() && tasksWaiting.peekFirst()[1] <= time)
            {
                maxHeapTaskFreq.offer(tasksWaiting.pollFirst()[0]);
            }
        }
        return time;
    }
}