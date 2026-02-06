class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;

        Arrays.sort(freq);

        int maxFreq = freq[25];
        int maxCount = 1;

        for (int i = 24; i >= 0; i--) {
            if (freq[i] == maxFreq) maxCount++;
            else break;
        }

        int partCount = maxFreq - 1;
        int partLength = n + 1;
        int minTime = partCount * partLength + maxCount;

        return Math.max(tasks.length, minTime);
    }
}
