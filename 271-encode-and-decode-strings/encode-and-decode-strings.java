public class Codec {

    // Encodes a list of strings to a single string.
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();

        //format 4#code
        for(String str: strs)
        {
            sb.append(str.length());
            sb.append("#");
            sb.append(str);
        }

        return sb.toString();
    }

    // Decodes a single string to a list of strings.
    public List<String> decode(String s) {
        //4#code5#apple
        List<String> result = new ArrayList<>();
        int i = 0;
        while(i < s.length())
        {
            //find first '#'
            int hashIndex = s.indexOf("#", i);

            int strLen = Integer.parseInt(s.substring(i, hashIndex)); // 4

            //string start and end indexs
            //eg: 0 + 1 -> 0 + 4 => 1 => 5
            int start = hashIndex + 1;
            int end = start + strLen; //end not inclusive

            result.add(s.substring(start, end));
            i = end;
        }

        return result;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.decode(codec.encode(strs));