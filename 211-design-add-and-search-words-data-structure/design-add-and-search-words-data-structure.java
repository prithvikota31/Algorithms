class WordDictionary {
    Node root;
    public WordDictionary() {
        root = new Node();
    }
    
    public void addWord(String word) {
        Node node = root;
        for(int i = 0; i < word.length(); i++)
        {
            char ch = word.charAt(i);
            if(!node.containsKey(ch))
            {
                node.setKey(ch, new Node());
            }
            node = node.get(ch);
        }

        node.setEnd();
    }
    
    public boolean search(String word) {
        return dfsSearch(root, word, 0);
    }

    public boolean dfsSearch(Node node, String word, int ind)
    {
        if(ind == word.length())
        {
            return node.isEnd();
        }

        char ch = word.charAt(ind);
        if(ch != '.')
        {
            if(!node.containsKey(ch))
            {
                return false;
            }
            else
            {
                return dfsSearch(node.get(ch), word, ind + 1);
            }
        }
        else
        {
            for(int i = 0; i < node.links.length; i++)
            {
                char c = (char)(i + 'a');
                if(node.containsKey(c))
                {
                    if(dfsSearch(node.get(c), word, ind + 1))
                        return true;
                }
            }
        }

        return false;
    }
}

class Node{
    Node[] links = new Node[26];
    boolean flag = false;

    public Node()
    {

    }

    public boolean containsKey(char ch)
    {
        return (links[ch - 'a'] != null);
    }

    public void setKey(char ch, Node node)
    {
        links[ch - 'a'] = node;
    }

    public Node get(char ch)
    {
        return links[ch - 'a'];
    }
    public void setEnd()
    {
        flag = true;
    }

    public boolean isEnd()
    {
        return flag;
    }

}

/**
 * Your WordDictionary object will be instantiated and called as such:
 * WordDictionary obj = new WordDictionary();
 * obj.addWord(word);
 * boolean param_2 = obj.search(word);
 */