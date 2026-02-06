class Solution {
    public int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b)->Integer.compare(
                                                                        b[0]*b[0] + b[1]*b[1],
                                                                        a[0]*a[0] + a[1]*a[1]
                                                                    ));
        for(int[] point: points)
        {
            maxHeap.offer(new int[]{point[0], point[1]});
            if(maxHeap.size() > k)
            {
                maxHeap.remove();
            }
        }

        int[][] result = new int[k][2];

        //k <= points.length
        for(int i = 0;  i < k; i++)
        {
            result[i] = maxHeap.poll();
        }

        return result;
        

    }


}

class Pair{
    int x;
    int y;
    public Pair(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}