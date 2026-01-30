public class Codec {

    // Encodes a list of strings to a single string.
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();

        for(String str: strs)
        {
            sb.append(str.length()).append('#').append(str);
        }

        return sb.toString();
    }

    // Decodes a single string to a list of strings.
    public List<String> decode(String s) {
        List<String> result = new ArrayList<>();
        //example 4#node4#soap10#xyz....
        //2 pointers, i start, j move till #
        int i = 0;
        while(i < s.length())
        {
            int j = i;
            while(s.charAt(j) != '#')   j++;
            //after above while now j is at #
            //so length [i, j) exclude j
            int len = Integer.parseInt(s.substring(i, j));
            j++;
            result.add(s.substring(j, j + len)); //eg, j = 0 and len 1, [0, 1] only 0th
            i = j + len;
        }
        return result;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.decode(codec.encode(strs));