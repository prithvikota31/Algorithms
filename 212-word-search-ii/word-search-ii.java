class Solution {

    Node root = new Node();
    public void addWord(String word)
    {
        Node node = root;
        for(int i = 0; i < word.length(); i++)
        {
            char ch = word.charAt(i);
            if(!node.containsKey(ch))
            {
                node.setKey(ch, new Node());
            }

            node = node.getKey(ch);
        }

        node.word = word;
    }
    public List<String> findWords(char[][] board, String[] words) {
        //create a trie, and then do dfs from each cell
        for(String s: words)
        {
            addWord(s);
        }

        int m = board.length;
        int n = board[0].length;
        
        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {-1, 0, 1, 0};

        Set<String> ans = new HashSet<>();
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                dfs(board, ans, i, j, root, delRow, delCol);
            }
        }

        return new ArrayList<>(ans);
    }

    public void dfs(char[][] board, Set<String> ans, int row, int col, Node node,
                int[] delRow, int[] delCol) {

        char ch = board[row][col];

        // If current board char is not a valid next trie path, stop.
        if (!node.containsKey(ch)) {
            return;
        }

        // Move trie pointer to the node representing this board character.
        node = node.getKey(ch);

        // If a word ends here, collect it.
        if (node.word != null) {
            ans.add(node.word);
        }

        // Mark visited so same cell is not reused in this DFS path.
        board[row][col] = '#';

        for (int i = 0; i < 4; i++) {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if (nRow >= 0 && nRow < board.length &&
                nCol >= 0 && nCol < board[0].length &&
                board[nRow][nCol] != '#') {

                dfs(board, ans, nRow, nCol, node, delRow, delCol);
            }
        }

        // Restore board for other DFS paths.
        board[row][col] = ch;
    }
}

class Node{
    Node[] links = new Node[26];
    String word = null;

    public boolean containsKey(char ch)
    {
        return links[ch - 'a'] != null;
    }

    public Node getKey(char ch)
    {
        return links[ch - 'a'];
    }

    public void setKey(char ch, Node node)
    {
        links[ch - 'a'] = node;
    }


}