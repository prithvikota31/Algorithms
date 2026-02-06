class Solution {
    public int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) ->
                                                            ((b[0] * b[0] - a[0] * a[0]) + (b[1] * b[1] - a[1] * a[1])));
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
        for(int[] r : result)
        {
            r[0] = maxHeap.peek()[0];
            r[1] = maxHeap.peek()[1];
            maxHeap.poll();
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