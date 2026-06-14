class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];

        // Count how many times each task appears.
        for (char task : tasks) {
            freq[task - 'A']++;
        }

        // Max heap = tasks that are available to run now.
        // We always pick the task with the highest remaining count.
        PriorityQueue<Integer> maxHeap =
            new PriorityQueue<>((a, b) -> Integer.compare(b, a));

        for (int count : freq) {
            if (count > 0) {
                maxHeap.offer(count);
            }
        }

        // cooldownQueue = tasks waiting for cooldown to finish.
        // int[] = {remainingCount, readyTime}
        Queue<int[]> cooldownQueue = new LinkedList<>();

        int time = 0;

        // Continue until no runnable tasks and no cooling tasks remain.
        while (!maxHeap.isEmpty() || !cooldownQueue.isEmpty()) {
            time++;

            // Run the most frequent available task.
            if (!maxHeap.isEmpty()) {
                int remainingCount = maxHeap.poll();
                remainingCount--;

                // If same task still remains, put it into cooldown.
                if (remainingCount > 0) {
                    cooldownQueue.offer(new int[] {remainingCount, time + n});
                }
            }

            // Move tasks whose cooldown is finished back into available heap.
            while (!cooldownQueue.isEmpty() && time >= cooldownQueue.peek()[1]) {
                int cooledDownCount = cooldownQueue.poll()[0];
                maxHeap.offer(cooledDownCount);
            }
        }

        return time;
    }
}