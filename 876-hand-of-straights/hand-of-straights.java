class Solution {
    public boolean isNStraightHand(int[] hand, int groupSize) {
        if(hand.length % groupSize != 0)    return false;
        //use treemap
        TreeMap<Integer, Integer>   map = new TreeMap<>();

        for(int i : hand)
        {
            //put(k, v)
            map.put(i, map.getOrDefault(i, 0) + 1);
        }
        // map is ready
        //eg: 1, 2, 3, 6, 2, 3,4, 7, 8
        // 1 1
        // 2 2
        // 3 2
        // 4 1
        // 6 1 
        // 7 1 
        // 8 1



        while(!map.isEmpty())
        {
            int start = map.firstKey();

            for(int i = start; i < start + groupSize; i++)
            {
                Integer iFreq = map.get(i);
                if(iFreq == null) return false; //not present
                else
                {
                    if(iFreq == 1) map.remove(i);
                    else
                        map.put(i, iFreq - 1);
                }
            }

        }

        return true;


        


    }
}