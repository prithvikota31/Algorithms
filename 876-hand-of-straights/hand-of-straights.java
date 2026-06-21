class Solution {
    public boolean isNStraightHand(int[] hand, int groupSize) {
        int l = hand.length;

        if(l % groupSize != 0)
        {
            return false;
        }

        TreeMap<Integer, Integer> map = new TreeMap<>();

        for(int i = 0; i < l; i++)
        {
            map.put(hand[i], map.getOrDefault(hand[i], 0)+1);
        }

        while(!map.isEmpty())
        {
            int curr = map.firstKey();
           
            for(int i = 0; i < groupSize; i++)
            {
                int num = curr + i;
                if(!map.containsKey(num))
                {
                   return false;
                }
                map.put(num, map.get(num) - 1);

                if(map.get(num) == 0)
                {
                    map.remove(num);
                }
            } 
        }

        return true;

        
    }
}







