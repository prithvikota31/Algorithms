class Twitter {
    private int timeStamp = 0;
    //follower -> followee (usersSet)
    Map<Integer, Set<Integer>> followerToFollowee = new HashMap<>();
    Map<Integer, List<Tweet>> userToTweets = new HashMap<>();

    public Twitter() {
        
    }
    
    public void postTweet(int userId, int tweetId) {
        userToTweets.putIfAbsent(userId, new ArrayList<>());
        userToTweets.get(userId).add(new Tweet(tweetId, timeStamp++));
    }
    
    public List<Integer> getNewsFeed(int userId) {
        List<Integer> result = new ArrayList<>();

        //use heap sor kind of k merge listtype
        //int[] -> {timeStamp, userId, currentIndex, tweetId}
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        Set<Integer> followees =
                    new HashSet<>(followerToFollowee.getOrDefault(userId, new HashSet<>()));
        followees.add(userId);
        for(int followee: followees)
        {
            if(!userToTweets.containsKey(followee) || userToTweets.get(followee).isEmpty())
            {
                continue;
            }
            int followeId = followee;
            int curIndex = userToTweets.get(followeId).size() - 1;
            Tweet recentTweet = userToTweets.get(followeId).get(curIndex);
            int tweetId = recentTweet.tweetId;
            int timeStamp = recentTweet.timeStamp;

            maxHeap.offer(new int[]{timeStamp, followeId, curIndex, tweetId});
        }



        //mow fill the list one by one
        while(!maxHeap.isEmpty())
        {
            int[] cur = maxHeap.poll();
            int cTimeStamp = cur[0];
            int cUserId = cur[1];
            int cIndex = cur[2];
            int cTweetId = cur[3];

            result.add(cTweetId);
            if(result.size() == 10)
            {
                break;
            }

            //insert next item into heap
            int nIndex = cIndex - 1;
            if(nIndex < 0)
            {
                continue; // no more tweets with this current user
            }
            Tweet nextTweet = userToTweets.get(cUserId).get(nIndex);
            int nTweetId = nextTweet.tweetId;
            int nTimeStamp = nextTweet.timeStamp;
            
            maxHeap.offer(new int[]{nTimeStamp, cUserId, nIndex, nTweetId});

        }

        return result;

        
    }
    
    public void follow(int followerId, int followeeId) {
        followerToFollowee.putIfAbsent(followerId, new HashSet<>());
        followerToFollowee.get(followerId).add(followeeId);
    }
    
    public void unfollow(int followerId, int followeeId) {
        if(followerToFollowee.containsKey(followerId))
        {
            followerToFollowee.get(followerId).remove(followeeId);
        }
    }
}

class Tweet
{
    int tweetId;
    int timeStamp;
    public Tweet(int tweetId, int timeStamp)
    {
        this.tweetId = tweetId;
        this.timeStamp = timeStamp;
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