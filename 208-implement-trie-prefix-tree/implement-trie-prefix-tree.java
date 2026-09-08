class Trie {
    Node root;
    public Trie() {
        root = new Node();
    }
    
    public void insert(String word) {
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
        cur.setEnd();
    }
    
    public boolean search(String word) {
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
                return false;
            }
        }
        
        return cur.isEnd();

    }
    
    public boolean startsWith(String word) {
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
                return false;
            }
        }
        
        return true;
    }
}

class Node{
    Node[] links = new Node[26];
    boolean isEnd = false;

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

    public void setEnd()
    {
        isEnd = true;
    }

    public boolean isEnd()
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