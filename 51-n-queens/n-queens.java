class Solution {
    public List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];

        for(int i = 0; i < n; i++)
        {
            Arrays.fill(board[i], '.');
        }
        List<List<String>> ans = new ArrayList<>();
        Set<Integer> rows = new HashSet<>();
        Set<Integer> d1 = new HashSet<>();
        Set<Integer> d2 = new HashSet<>();
        getAllPossibilities(board, 0, ans, rows, d1, d2);
        return ans;
    }
    private void getAllPossibilities(char[][] board, int col, List<List<String>> ans,
                                     Set<Integer> rows, Set<Integer> d1, Set<Integer> d2)
    {
        if(col == board[0].length)
        {
            ans.add(createBoard(board));
            return;
        }

        for(int row = 0; row < board.length; row++)
        {
            //row should not contain
            //diagnorals d1 ad d2 should not contain
            if(!rows.contains(row) && !d1.contains(row + col) && !d2.contains(row - col))
            {
                rows.add(row);
                d1.add(row + col);
                d2.add(row - col);
                board[row][col] = 'Q';
                getAllPossibilities(board, col + 1, ans, rows, d1, d2);
                board[row][col] = '.';
                rows.remove(row);
                d1.remove(row + col);
                d2.remove(row - col);
            }
        }
    }

    private List<String> createBoard(char[][] board)
    {
        List<String> output = new ArrayList<>();
        for(int i = 0; i < board.length; i++)
        {
            output.add(new String(board[i]));
        }
        return output;
    }
}