class TimeMap {

    Map<String, List<Pair>> map;
    public TimeMap() {
        map = new HashMap<>();
    }
    
    public void set(String key, String value, int timestamp) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(new Pair(value, timestamp));
    }
    
    public String get(String key, int timestamp) {
        if(!map.containsKey(key))   return "";

        List<Pair> list = map.get(key);
        int low = 0;
        int high = list.size() - 1;
        int ans = Integer.MIN_VALUE;
        while(low <= high)
        {
            int mid = low + (high - low) / 2;
            if(list.get(mid).timestamp <= timestamp)
            {
                ans = Math.max(ans, mid);
                low = mid + 1;
            }
            else
            {
                high = mid - 1;
            }
        }

        if(ans == Integer.MIN_VALUE)
        {
           return ""; 
        }
        else
        {
            return list.get(ans).value;
        }
    }
}

class Pair{
    String value;
    int timestamp;
    public Pair(String value, int timestamp)
    {
        this.timestamp = timestamp;
        this.value = value;
    }
}

/**
 * Your TimeMap object will be instantiated and called as such:
 * TimeMap obj = new TimeMap();
 * obj.set(key,value,timestamp);
 * String param_2 = obj.get(key,timestamp);
 */