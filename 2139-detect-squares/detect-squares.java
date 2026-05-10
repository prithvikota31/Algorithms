class DetectSquares {
    Map<String, Integer> map;
    List<int[]> points;
    public DetectSquares() {
        points = new ArrayList<>();
        map = new HashMap<>(); // map represents, point string format to count of duplciates
    }
    
    public void add(int[] point) {
        points.add(point);
        String s = point[0] + "," + point[1];
        map.put(s, map.getOrDefault(s, 0) + 1);
    }
    
    public int count(int[] point) {
        
        int x = point[0];
        int y = point[1];

        int result = 0;
        for(int[] p: points)
        {
            //assume p is for diagnol point and confirm its validity
            int nx = p[0];
            int ny = p[1];
            if((Math.abs(x - nx) != Math.abs(y - ny)) || x == nx || y == ny)
            {
                continue;
            }

            //check for (x, ny) and (nx, y)
            //normally have to loop in, but create a hashmap for the same
            //(x, ny count)
            String xAndny = x + "," + ny;
            String nxAndy = nx + ","  + y;

            result += (map.getOrDefault(xAndny, 0)) * (map.getOrDefault(nxAndy, 0));
        }
        return result;
    }
}

/**
 * Your DetectSquares object will be instantiated and called as such:
 * DetectSquares obj = new DetectSquares();
 * obj.add(point);
 * int param_2 = obj.count(point);
 */