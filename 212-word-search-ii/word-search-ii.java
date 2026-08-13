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

        Set<String> result = new HashSet<>();
        // we already have a trie with words
        
        //from each position on matrix try to traverse whole matrix and see if that word is present in trie
        int m = board.length;
        int n = board[0].length;

        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                dfs(board, root, i, j, result);
            }
        }
        return new ArrayList<>(result);


    }

    private void dfs(char[][] board, Node node, int row, int col, Set<String> result)
    {
        char ch = board[row][col];
        if(!node.containsKey(ch))
        {
            return;
        }

        node = node.getKey(ch);

        if(node.word != null)
        {
            result.add(node.word);
        }
        //still continue;
        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};
        board[row][col] = '*';
        for(int i = 0; i < 4; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < board.length && nCol >= 0 && nCol < board[0].length && board[nRow][nCol] != '*')
            {
                dfs(board, node, nRow, nCol, result);
            }
        }
        board[row][col] = ch;
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
}