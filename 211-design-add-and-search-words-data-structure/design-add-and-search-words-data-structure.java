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

            node = node.getKey(ch);
        }

        node.setEnd();
    }
    
    public boolean search(String word) {
        return dfs(0, word, root);
    }

    private boolean dfs(int index, String word, Node node)
    {
        if(index == word.length())
        {
            return node.isEnd();
        }

        char currentChar = word.charAt(index);
        if(currentChar != '.') 
        {
            if(node.containsKey(currentChar))
            {
                return dfs(index + 1, word, node.getKey(currentChar));
            }
            else
            {
                return false;
            }
        }
        else //loop all possibilites
        {
            for(Node nextNode: node.links)
            {
                if(nextNode != null)
                {
                    if(dfs(index + 1, word, nextNode))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

class Node{
    Node[] links = new Node[26];
    boolean flag;


    public boolean containsKey(char ch)
    {
        return links[ch - 'a'] != null;
    }

    public void setKey(char ch, Node node)
    {
        links[ch - 'a'] = node;
    }

    public void setEnd()
    {
        flag = true;
    }

    public Node getKey(char ch)
    {
        return links[ch - 'a'];
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