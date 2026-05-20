class Solution {
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        for(int stone: stones)
        {
            pq.offer(stone);
        }

        while(pq.size() >= 2)
        {
            int first = pq.poll();
            int second= pq.poll();
            int result = Math.abs(first - second);

            if(result != 0) pq.offer(result);
        }

        if(pq.size() == 1)  return pq.poll();
        else return 0;
    }
}