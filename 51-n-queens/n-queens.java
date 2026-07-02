class Solution {
    public List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];
        //create empty board
        for(int i = 0; i < n; i++)
        {
            Arrays.fill(board[i], '.');
        }
        List<List<String>> ans = new ArrayList<>();
        Set<Integer> rows = new HashSet<>();
        Set<Integer> d1 = new HashSet<>();
        Set<Integer> d2 = new HashSet<>();

        //do backtrack each col is filled and proceeded to next column
        backtrack(0, rows, d1, d2, board, ans);
        return ans;


    }

    private void backtrack(int col, Set<Integer> rows, Set<Integer> d1, Set<Integer> d2,
                            char[][] board, List<List<String>> ans)
    {
        if(col == board.length) // it successful fiulled col 0 -> n -1
        {
            ans.add(createResultBoard(board));
            return;
        }

        for(int i = 0; i < board.length; i++) // for current col try all possibilites
        {
            int row = i;
            if(rows.contains(row) || d1.contains(row + col) || d2.contains(row - col))
            {
                continue;
            }

            board[row][col] = 'Q';
            rows.add(row);
            d1.add(row + col);
            d2.add(row - col);
            backtrack(col + 1, rows, d1, d2, board, ans);
            rows.remove(row);
            d1.remove(row + col);
            d2.remove(row - col);
            board[row][col] = '.';    
        }
    }

    private List<String> createResultBoard(char[][] board)
    {
        //board has char Arrays
        List<String> result = new ArrayList<>();
        for(int i = 0; i < board.length; i++)
        {
            result.add(new String(board[i]));
        }
        return result;
    }
}