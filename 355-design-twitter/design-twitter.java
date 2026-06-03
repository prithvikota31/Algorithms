class Twitter {
    Map<Integer, Set<Integer>> followMap;
    Map<Integer, List<int[]>> tweetMap;
    int time = 0;

    public Twitter() {
        followMap = new HashMap<>();
        tweetMap = new HashMap<>();
    }
    
    public void postTweet(int userId, int tweetId) {
        time++;
        tweetMap.computeIfAbsent(userId, t -> new ArrayList<>()).add(new int[]{time, tweetId}); 
    }
    
    public List<Integer> getNewsFeed(int userId) {
        List<Integer> result = new ArrayList<>();

        Set<Integer> users = new HashSet<>();
        users.add(userId);
        if(followMap.containsKey(userId))     
        {
            users.addAll(followMap.get(userId));
        } 
        //int[] should contain timestamp
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> (b[0] - a[0]));

        for(int user: users)
        {
            //for each user get the latest time stamp and store in heap
            //we will do k sorted kind to retrieve top 10
            //int[] should have timestamp, tweetId, nextIndex to be retrieved, userId
             List<int[]> tweets = tweetMap.get(user);
            if (tweets == null || tweets.isEmpty()) {
                continue;
            }
            int curIndex = tweets.size() - 1;
            int curTweetId = tweets.get(curIndex)[1];
            int curTimeStamp = tweetMap.get(user).get(curIndex)[0];

            maxHeap.offer(new int[]{
                curTimeStamp, curTweetId, curIndex, user});
        }

        while(!maxHeap.isEmpty() && result.size() < 10)
        {
            int[] cur = maxHeap.poll();
            int curTweetId = cur[1];
            int curIndex = cur[2];
            int user = cur[3];
            result.add(curTweetId);

            List<int[]> tweets = tweetMap.get(user);

            int nextIndex = curIndex - 1;

            if(nextIndex >= 0)
            {
                //same user
                int nextTweetId = tweets.get(nextIndex)[1];
                int nextTimeStamp = tweets.get(nextIndex)[0];
                maxHeap.offer(new int[]{
                    nextTimeStamp, nextTweetId, nextIndex, user});
            }
        }

        return result;
        

    }
    
    public void follow(int followerId, int followeeId) {
        followMap.computeIfAbsent(followerId, t -> new HashSet<>()).add(followeeId);
    }
    
    public void unfollow(int followerId, int followeeId) {
        if(followMap.containsKey(followerId))
        {
            followMap.get(followerId).remove(followeeId);
        }
        
    }
}

/**
 * Your Twitter object will be instantiated and called as such:
 * Twitter obj = new Twitter();
 * obj.postTweet(userId,tweetId);
 * List<Integer> param_2 = obj.getNewsFeed(userId);
 * obj.follow(followerId,followeeId);
 * obj.unfollow(followerId,followeeId);
 */