import java.util.*;

class Twitter {

    // Global timestamp to maintain tweet ordering
    private int timestamp = 0;

    // follower -> followees
    private Map<Integer, Set<Integer>> followMap = new HashMap<>();

    // user -> tweets
    private Map<Integer, List<Tweet>> tweetMap = new HashMap<>();

    class Tweet {
        int tweetId;
        int time;

        Tweet(int tweetId, int time) {
            this.tweetId = tweetId;
            this.time = time;
        }
    }

    public Twitter() {}

    // Append tweet to user's tweet list
    public void postTweet(int userId, int tweetId) {
        tweetMap.putIfAbsent(userId, new ArrayList<>());
        tweetMap.get(userId).add(
            new Tweet(tweetId, timestamp++)
        );
    }

    // Add follow relationship
    public void follow(int followerId, int followeeId) {
        followMap.putIfAbsent(followerId, new HashSet<>());
        followMap.get(followerId).add(followeeId);
    }

    // Remove follow relationship
    public void unfollow(int followerId, int followeeId) {
        if (followMap.containsKey(followerId)) {
            followMap.get(followerId).remove(followeeId);
        }
    }

    public List<Integer> getNewsFeed(int userId) {

        List<Integer> result = new ArrayList<>();

        // Gather all users whose tweets should appear
        Set<Integer> users = new HashSet<>();
        users.add(userId);

        if (followMap.containsKey(userId)) {
            users.addAll(followMap.get(userId));
        }

        /*
         * Max Heap Entry:
         * [time, tweetId, userId, index]
         *
         * Used to perform a K-way merge of tweet lists.
         */
        PriorityQueue<int[]> maxHeap =
            new PriorityQueue<>((a, b) -> b[0] - a[0]);

        // Push newest tweet from every relevant user
        for (int user : users) {

            List<Tweet> tweets = tweetMap.get(user);

            if (tweets == null || tweets.isEmpty()) {
                continue;
            }

            int index = tweets.size() - 1;
            Tweet tweet = tweets.get(index);

            maxHeap.offer(
                new int[]{
                    tweet.time,
                    tweet.tweetId,
                    user,
                    index
                }
            );
        }

        /*
         * Merge K sorted tweet lists:
         * Pop newest tweet globally,
         * then push the next older tweet
         * from the same user.
         */
        while (!maxHeap.isEmpty() && result.size() < 10) {

            int[] curr = maxHeap.poll();

            int tweetId = curr[1];
            int user = curr[2];
            int index = curr[3];

            result.add(tweetId);

            index--;

            if (index >= 0) {

                Tweet nextTweet =
                    tweetMap.get(user).get(index);

                maxHeap.offer(
                    new int[]{
                        nextTweet.time,
                        nextTweet.tweetId,
                        user,
                        index
                    }
                );
            }
        }

        return result;
    }
}