class TimeMap {
    Map<String, List<Pair>> map;


    public TimeMap() {
        map = new HashMap<>();
        
    }
    
    public void set(String key, String value, int timestamp) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(new Pair(value, timestamp));
        
    }
    
    public String get(String key, int timestamp) {
        if(!map.containsKey(key))
        {
            return "";
        }
        List<Pair> list = map.get(key);

        int l = 0;
        int h = list.size() - 1;

        String ans = "";

        while(l <= h)
        {
            int mid = l + (h-l)/2;

            if(list.get(mid).time <= timestamp)
            {
                ans = list.get(mid).val;
                l = mid + 1;
            }
            else
            {
                h = mid - 1;
            }
        }

        return ans;
        
    }

    class Pair{
        String val;
        int time;
        public Pair(String val, int time)
        {
            this.val = val;
            this.time = time;
        }
    }
}

/**
 * Your TimeMap object will be instantiated and called as such:
 * TimeMap obj = new TimeMap();
 * obj.set(key,value,timestamp);
 * String param_2 = obj.get(key,timestamp);
 */