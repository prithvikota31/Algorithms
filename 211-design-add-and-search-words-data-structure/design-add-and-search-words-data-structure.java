class WordDictionary {
    //. makes the problem search all nodes below it, so like a graph traversal 
    Node root;
    public WordDictionary() {
        root = new Node();
    }
    
    public void addWord(String word) {
        Node cur = root;
        for(int i = 0; i < word.length(); i++)
        {
            char ch = word.charAt(i);
            if(!cur.containsKey(ch))
            {
                cur.setKey(ch);
            }
            cur = cur.getKey(ch);
        }
        cur.setEnd();
    }
    
    public boolean search(String word) {
        //it may contain .
        //if it contains . we have iterate over all the nodes
        Node cur = root;
        return searchRecursion(word, 0, cur);
    }

    private boolean searchRecursion(String word, int index, Node cur)
    {
        if(index == word.length())
        {
            return cur.isEnd();
        }
        char ch = word.charAt(index);
        if(ch != '.')
        {
            if(cur.containsKey(ch))
            {
                return searchRecursion(word, index + 1, cur.getKey(ch));
            }
            else
            {
                return false;
            }       
        }
        else
        {
            boolean found = false;
            for(char c = 'a'; c < 'a' + cur.links.length; c++)
            {
                if(cur.containsKey(c))
                {
                    found = found | searchRecursion(word, index + 1, cur.getKey(c));
                    if(found == true)
                    {
                        return true;
                    }
                }
            }
            return found;
        }
    }
}

class Node{
    Node[] links = new Node[26];
    boolean isEnd;

    public boolean containsKey(char ch)
    {
        return links[ch - 'a'] != null;
    }

    public void setKey(char ch)
    {
        links[ch - 'a'] = new Node();
    }

    public Node getKey(char ch)
    {
        return links[ch - 'a'];
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
 * Your WordDictionary object will be instantiated and called as such:
 * WordDictionary obj = new WordDictionary();
 * obj.addWord(word);
 * boolean param_2 = obj.search(word);
 */