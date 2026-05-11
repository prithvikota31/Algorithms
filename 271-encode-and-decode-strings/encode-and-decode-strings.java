public class Codec {

    // Encodes a list of strings to a single string.
    public String encode(List<String> strs) {
        //length, delimiter, str

        StringBuilder sb = new StringBuilder();
        for(String str: strs)
        {
            sb.append(str.length()).append('*').append(str);
        }
        return sb.toString();
    }

    // Decodes a single string to a list of strings.
    //eg: 3*abc5*daceb
    public List<String> decode(String s) {
        
        int i = 0;
        List<String> result = new ArrayList<>();
        while(i < s.length())
        {
            //need j pointer to capture number
            int j = i;
            while(s.charAt(j) != '*')
            {
                j++;
            }
            //after while j is at *
            int len = Integer.parseInt(s.substring(i, j));
            j++;
            result.add(s.substring(j, j + len));
            i = j + len;

        }

        return result;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.decode(codec.encode(strs));