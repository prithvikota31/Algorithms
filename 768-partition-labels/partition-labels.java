import java.util.*;

class Solution {
    public List<Integer> partitionLabels(String s) {
        List<Integer> result = new ArrayList<>();
        int n = s.length();
        int start = 0;

        while (start < n) {
            int end = start;

            // Check how far this partition needs to go
            for (int i = start; i <= end; i++) {
                char currentChar = s.charAt(i);
                // Scan the entire string to find the last occurrence of currentChar
                for (int j = n - 1; j > end; j--) {
                    if (s.charAt(j) == currentChar) {
                        end = j;
                        break;
                    }
                }
            }

            // Once we find the end of the partition, add it
            result.add(end - start + 1);
            start = end + 1;
        }

        return result;
    }
}
