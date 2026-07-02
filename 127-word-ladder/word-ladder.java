class Solution {
    private static class Node {
        String word;
        int length;

        Node(String word, int length) {
            this.word = word;
            this.length = length;
        }
    }

    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);

        if (!dict.contains(endWord)) {
            return 0;
        }

        Deque<Node> q = new ArrayDeque<>();
        q.offer(new Node(beginWord, 1));
        dict.remove(beginWord);

        while (!q.isEmpty()) {
            Node cur = q.poll();

            if (cur.word.equals(endWord)) {
                return cur.length;
            }

            char[] chars = cur.word.toCharArray();

            // Generate all words that differ by one character.
            for (int i = 0; i < chars.length; i++) {
                char original = chars[i];

                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == original) continue;

                    chars[i] = c;
                    String nextWord = new String(chars);

                    if (dict.contains(nextWord)) {
                        q.offer(new Node(nextWord, cur.length + 1));
                        dict.remove(nextWord);
                    }
                }

                chars[i] = original;
            }
        }

        return 0;
    }
}