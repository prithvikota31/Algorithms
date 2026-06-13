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
                    int[] delRow,  int[] delCol)
    {
        char ch = board[row][col];



        if(!node.containsKey(ch)) //node doesn't contain ch return
        {
            return;
        }

        
        char origChar = board[row][col];
        board[row][col] = 'X';

        Node nNode = node.getKey(ch);
        if(nNode.word != null)
        {
            ans.add(nNode.word);
        }
        for(int i = 0; i < delRow.length; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < board.length && nCol >= 0 && nCol < board[0].length && board[nRow][nCol] != 'X')
            {
                dfs(board, ans, nRow, nCol, nNode, delRow, delCol);
            }       
        }
        board[row][col] = origChar;

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