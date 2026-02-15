class Solution {
    public List<List<Integer>> pacificAtlantic(int[][] heights) {

        int m = heights.length;
        int n = heights[0].length;

        List<List<Integer>> ans = new ArrayList<>();

        boolean[][] atlanticReachable = new boolean[m][n];
        boolean[][] pacificReachable = new boolean[m][n];

        for(int i = 0; i < m; i++) {
            dfs(heights, m, n, i, 0, pacificReachable);
            dfs(heights, m, n, i, n-1, atlanticReachable);  
        }

        for(int j = 0; j < n; j++) {
            dfs(heights, m, n, 0, j, pacificReachable);
            dfs(heights, m, n, m-1, j, atlanticReachable);  
        }


        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(pacificReachable[i][j] && atlanticReachable[i][j]) {
                    ans.add(Arrays.asList(i, j));
                }
            }
        }

        return ans;  
    }

    public void dfs(int[][] heights, int m, int n, int row, int col, boolean[][] vis) {
        vis[row][col] = true;

        int[] delR = {1,0,-1,0};
        int[] delC = {0,1,0,-1};

        for(int i = 0; i < 4; i++) {
            int r = row + delR[i];
            int c = col + delC[i];

            if(r >= 0 && r < m && c >= 0 && c < n && !vis[r][c] && heights[r][c] >= heights[row][col]) {
                dfs(heights, m, n, r, c, vis);
            }
        }
    }
}