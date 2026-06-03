class Solution {
    public int leastInterval(char[] tasks, int n) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b)-> b - a);

        int[] freqArray = new int[26];

        for(char c : tasks)
        {
            freqArray[c - 'A']++;
        }

        for(int freq : freqArray)
        {
            if(freq > 0)
            {
                maxHeap.offer(freq);
            }
        }

        Queue<int[]> cooldown = new LinkedList<>();
        int time = 0;

        while(!maxHeap.isEmpty() || !cooldown.isEmpty())
        {
            time++;
            if(!maxHeap.isEmpty())
            {
                int cnt = maxHeap.poll();
                cnt--;
                if(cnt > 0)
                {
                    cooldown.offer(new int[]{cnt, time + n});
                }
            }

            if(!cooldown.isEmpty() && cooldown.peek()[1] == time)
            {
                maxHeap.offer(cooldown.poll()[0]);
            }

        }

        return time;
        
    }
}