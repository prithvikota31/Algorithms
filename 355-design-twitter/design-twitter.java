class Twitter {
    private int timestamp = 0;
    Map<Integer, Set<Integer>> followerToFollowees = new HashMap<>();
    Map<Integer, List<Tweet>> usersToTweets = new HashMap<>();
    public Twitter() {

    }

    class Tweet{
        int tweetId;
        int timestamp;
        public Tweet(int tweetId, int timestamp)
        {
            this.tweetId = tweetId;
            this.timestamp = timestamp;
        }
    }
    
    public void postTweet(int userId, int tweetId) {
        usersToTweets.putIfAbsent(userId, new ArrayList<>());
        usersToTweets.get(userId).add(new Tweet(tweetId, timestamp++));
    }
    
    public List<Integer> getNewsFeed(int userId) {
        List<Integer> newsFeed = new ArrayList<>();
        Set<Integer> feedUserIds = new HashSet<>();
        feedUserIds.add(userId); // need personal feed too 
        if(followerToFollowees.containsKey(userId))
        {
            feedUserIds.addAll(followerToFollowees.get(userId));
        }

        //create a maxheap
        PriorityQueue<int[]> pqMaxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[0],a[0]));

        //add max timestamp tweet from each user
        // its present at end of list
        for(int user: feedUserIds)
        {
           List<Tweet> userTweets =  usersToTweets.get(user);

           if(userTweets == null || userTweets.isEmpty())
           {
            continue;
           }
           int index = userTweets.size() - 1;
           Tweet recentTweetOfUser = userTweets.get(index);
           pqMaxHeap.offer(new int[]{recentTweetOfUser.timestamp, recentTweetOfUser.tweetId, index, user});
        }

        //now user k map sorting type;

        while(!pqMaxHeap.isEmpty() && newsFeed.size() < 10)
        {
            int tweetId = pqMaxHeap.peek()[1];
            newsFeed.add(tweetId);
            int user = pqMaxHeap.peek()[3];
            int nextIndex = pqMaxHeap.peek()[2] - 1;
            
            pqMaxHeap.poll();
            if(nextIndex >= 0)
            {
                Tweet nextTweet = usersToTweets.get(user).get(nextIndex);
                pqMaxHeap.offer(new int[]{nextTweet.timestamp, nextTweet.tweetId, nextIndex, user});
            }
        }

        return newsFeed;

    }
    
    public void follow(int followerId, int followeeId) {
        followerToFollowees.putIfAbsent(followerId, new HashSet<>());
        followerToFollowees.get(followerId).add(followeeId);
    }
    
    public void unfollow(int followerId, int followeeId) {
        if(followerToFollowees.containsKey(followerId))
        {
            followerToFollowees.get(followerId).remove(followeeId);
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