class Trie {
    Node root;
    public Trie() {
        root = new Node();
    }
    
    public void insert(String word) {
        Node cur = root;

        for(int i = 0; i < word.length(); i++)
        {
            char ch = word.charAt(i);
            if(!cur.containsKey(ch))
            {
                cur.setKey(ch);
            }
            cur = cur.getNode(ch);
        }
        cur.setEnd();
    }
    
    public boolean search(String word) {
        Node cur = root;

        for(int i = 0; i < word.length(); i++)
        {
            char ch = word.charAt(i);
            if(!cur.containsKey(ch))
            {
                return false;
            }
            cur = cur.getNode(ch);
        }
        return cur.isEndWord();
    }
    
    public boolean startsWith(String prefix) {
        Node cur = root;

        for(int i = 0; i < prefix.length(); i++)
        {
            char ch = prefix.charAt(i);
            if(!cur.containsKey(ch))
            {
                return false;
            }
            cur = cur.getNode(ch);
        }
        return true;
    }
}

class Node{
    Node[] links = new Node[26];
    boolean isEnd = false;

    public Node getNode(char ch)
    {
        return links[ch - 'a'];
    }

    public void setKey(char ch)
    {
        links[ch - 'a'] = new Node();
    }

    public boolean containsKey(char ch)
    {
        return links[ch - 'a'] != null;
    }

    public void setEnd()
    {
        isEnd = true;
    }

    public boolean isEndWord()
    {
        return isEnd;
    }

}

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */