class Solution {
    Node root = new Node();
    
    public List<String> findWords(char[][] board, String[] words) {
        Set<String> set = new HashSet<>();
        int m = board.length;
        int n = board[0].length;

        for(String word : words)
        {
            addWord(word);
        }

        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                dfs(board, set, root, i, j);
            }
        }

        return new ArrayList<>(set);
    }

    public void addWord(String word)
    {
        int l = word.length();
        Node curr = root;
        for(int i = 0; i < l; i++)
        {
            char c = word.charAt(i);
            if(!curr.containsKey(c))
            {
                curr.put(c, new Node());
            }
            curr = curr.get(c);
        }

        curr.word = word;
    }

    public void dfs(char[][] board, Set<String> ans,  Node root, int row, int col)
    {
        if(row < 0 || row >= board.length || col < 0 || col >= board[0].length || board[row][col] == '*')
        {
            return;
        }

        char c = board[row][col];

        if(!root.containsKey(c))
        {
            return;
        }

        Node curr = root.get(c);

        if(curr.word != null)
        {
            ans.add(curr.word);
        }

        board[row][col] = '*';

        dfs(board, ans, curr, row, col + 1);
        dfs(board, ans, curr, row, col - 1);
        dfs(board, ans, curr, row + 1, col);
        dfs(board, ans, curr, row - 1, col);

        board[row][col] = c;
    }

    class Node{
        Node[] links = new Node[26];
        String word = null;

        public boolean containsKey(char key)
        {
            return links[key - 'a'] != null;
        }

        public Node get(char key)
        {
            return links[key - 'a'];
        }

        public void put(char key, Node value)
        {
            links[key - 'a'] = value;
        }
    }


}