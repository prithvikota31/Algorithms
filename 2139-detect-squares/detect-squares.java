class DetectSquares {
    Map<String, Integer> pointMap = new HashMap<>();
    public DetectSquares() {
        
    }
    
    public void add(int[] point) {
        String pointStr = encode(point[0], point[1]);
        pointMap.put(pointStr, pointMap.getOrDefault(pointStr, 0) + 1);
    }
    
    public int count(int[] point) {
        int x1 = point[0];
        int y1 = point[1];
       
        int count = 0;
        for(String p: pointMap.keySet())
        {
            int[] p2 = decode(p);
            int x2 = p2[0];
            int y2 = p2[1];

            //trying to find points such that p2 is diagonal to p1
            if(x1 == x2 || y1 == y2 || Math.abs(x1 - x2) != Math.abs(y1 - y2))
            {
                continue;
            } 
            String p3 = encode(x1, y2); 
            String p4 = encode(x2, y1);
            if(!pointMap.containsKey(p3) || !pointMap.containsKey(p4))
            {
                continue;
            }

            int c2 = pointMap.get(p);
            int c3 = pointMap.get(p3);
            int c4 = pointMap.get(p4);

            count += (c2 * c3 * c4);

        }
        return count;
    }

    private String encode(int x, int y)
    {
        return x + "," + y;
    }

    private int[] decode(String p)
    {
        String[] pXY = p.split(",");
        return new int[]{Integer.parseInt(pXY[0]), Integer.parseInt(pXY[1])};
    }
}

/**
 * Your DetectSquares object will be instantiated and called as such:
 * DetectSquares obj = new DetectSquares();
 * obj.add(point);
 * int param_2 = obj.count(point);
 */