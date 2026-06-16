public class Codec {

    // Encodes a list of strings to a single string.
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();

        for(String str: strs)
        {
            sb.append(str.length());
            sb.append('#');
            sb.append(str);
        }

        return sb.toString();
    }

    // Decodes a single string to a list of strings.
    //5#abcde3#xyz
    public List<String> decode(String s) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while(i < s.length())
        {
            int j = i;

            while(s.charAt(j) != '#')
            {
                j++;
            }
            int strLength = Integer.parseInt(s.substring(i, j)); // this gives 5;
            int strStart = j + 1;
            result.add(s.substring(strStart, strStart + strLength));

            i = strStart + strLength;
        }

        return result;
        




    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.decode(codec.encode(strs));