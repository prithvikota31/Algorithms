class Solution {
    public List<List<Integer>> pacificAtlantic(int[][] heights) {

        int m = heights.length;
        int n = heights[0].length;

        List<List<Integer>> ans = new ArrayList<>();

        boolean[][] canPacific = new boolean[m][n];
        boolean[][] canAtlantic = new boolean[m][n];

        for(int i = 0; i < m; i++)
        {
            if(!canPacific[i][0])
            {
                dfs(heights, i, 0, canPacific);
            }
            if(!canAtlantic[i][n - 1])
            {
                dfs(heights, i, n-1, canAtlantic);
            }
        }

        for(int j = 0; j < n; j++)
        {
            if(!canPacific[0][j])
            {
                dfs(heights, 0, j, canPacific);
            }

            if(!canAtlantic[m - 1][j])
            {
                dfs(heights, m-1, j, canAtlantic);
            }
        }

        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {

                if(canPacific[i][j] && canAtlantic[i][j])
                {
                    ans.add(Arrays.asList(i,j));
                }
            }
            
        }

        return ans;
    }

    public void dfs(int[][] heights, int row, int col, boolean[][] vis)
    {
        int m = heights.length;
        int n = heights[0].length; 
        int[] dr = {1,-1,0,0};
        int[] dc = {0,0,1,-1};

        vis[row][col] = true;

        for(int i = 0; i < 4; i++)
        {
            int r = dr[i] + row;
            int c = dc[i] + col;

            if(r >= 0 && r < m && c >= 0 && c < n && !vis[r][c] && heights[r][c] >= heights[row][col])
            {
                dfs(heights, r, c, vis);
            }
        }

    }
}