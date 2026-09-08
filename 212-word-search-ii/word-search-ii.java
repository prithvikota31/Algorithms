class Solution {

    Node root = new Node();
    private void addWord(String word) {
        Node cur = root;
        for(int i = 0; i < word.length(); i++)
        {
            char currentCh = word.charAt(i);

            if(cur.containsKey(currentCh))
            {
                cur = cur.getKey(currentCh);
            }
            else
            {
                cur.setKey(currentCh);
                cur = cur.getKey(currentCh);
            }
        }
        cur.word = word;
    }
    

    public List<String> findWords(char[][] board, String[] words) {
        for(String word: words)
        {
            addWord(word);
        }

        //start from every every cell, and traverse along trie
        Set<String> result = new HashSet<>();
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                dfs(i, j, root, result, board);
            }
        }

        return new ArrayList<>(result);
    }

    private void dfs(int row, int col, Node node, Set<String> result, char[][] board)
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

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {-1, 0, 1, 0};

        board[row][col] = '*';

        for(int i = 0; i < delRow.length; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < board.length && nCol >= 0
                && nCol < board[0].length && board[nRow][nCol] != '*')
            {
                dfs(nRow, nCol, node, result, board);
            }
        }

        board[row][col] = ch;
    }
}


class Node{
    Node[] links = new Node[26];
    String word = null;

    public Node getKey(char ch)
    {
        return links[ch - 'a'];
    }

    public void setKey(char ch)
    {
        links[ch - 'a'] = new Node();
    }

    public boolean containsKey(char ch)
    {
        return links[ch -'a'] != null;
    }
}