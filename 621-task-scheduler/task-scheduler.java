class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];

        for(char t: tasks)
        {
            freq[t - 'A']++;
        }

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

        for(int f: freq)
        {
            if(f > 0)
            {
                maxHeap.offer(f);
            }
        }

        //“Continue the simulation until there are no runnable tasks and no waiting tasks.”
        Queue<int[]> q = new LinkedList<>(); // to store items which needs cooldown, int[] = {a, b}
        int time = 0;
        while(!q.isEmpty() || !maxHeap.isEmpty())
        {
            time++;
            if(!maxHeap.isEmpty())
            {
                int elementFreq = maxHeap.poll();
                elementFreq--;
                if(elementFreq > 0) // needs cooldown
                {
                    q.offer(new int[]{elementFreq, time + n});
                }
            }

            while(!q.isEmpty() && time >= q.peek()[1])
            {
                int cooledDownFreq = q.poll()[0];
                maxHeap.offer(cooledDownFreq);
            }

        }

        return time;
    }
}