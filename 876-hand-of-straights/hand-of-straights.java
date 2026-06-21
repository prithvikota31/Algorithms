class Solution {
    public boolean isNStraightHand(int[] hand, int groupSize) {
        int n = hand.length;
        if(n % groupSize != 0)  return false;
        TreeMap<Integer, Integer> sortedMap = new TreeMap<>(); //val -> freq


        for(int i = 0; i < hand.length; i++)
        {
            sortedMap.put(hand[i], sortedMap.getOrDefault(hand[i], 0) + 1);
        }

        
        while(!sortedMap.isEmpty())
        {
            int cur = sortedMap.firstKey();
            for(int i = 0; i < groupSize; i++)
            {
                if(!sortedMap.containsKey(cur))
                {
                    return false;
                }
                int curCount = sortedMap.get(cur);
                if(curCount == 1)
                {
                    sortedMap.remove(cur);
                }
                else
                {
                    sortedMap.put(cur, curCount - 1);
                }

                cur = cur + 1;

            }

        }

        return true;

    }
}