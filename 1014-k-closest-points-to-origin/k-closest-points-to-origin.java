class Solution {
    public int[][] kClosest(int[][] points, int k) {
        int[][] result = new int[k][2];

        // use minheap to keep k size and k closest points
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) 
                                    -> ((b[1] * b[1] + b[0] * b[0]) - (a[1] * a[1] + a[0] * a[0])));
        
        for(int[] point: points)
        {
            pq.offer(point);
            if(pq.size() > k)
            {
                pq.poll();
            }
        }

        for(int i = 0; i < k; i++)
        {
            result[i] = pq.poll();
        }

        return result;

    }
}