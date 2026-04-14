class Solution {
    public boolean isNStraightHand(int[] hand, int groupSize) {
        TreeMap<Integer, Integer> sortedMap = new TreeMap<>();

        int n = hand.length;
        if(n % groupSize != 0)  return false;

        for(int i = 0; i < n; i++)
        {
            sortedMap.put(hand[i], sortedMap.getOrDefault(hand[i], 0) + 1);
        }


        // while(!sortedMap.isEmpty())
        // {
        //     int cur = sortedMap.firstKey();
        //     for(int i = 0; i < groupSize - 1; i++)
        //     {
        //         int curValue = sortedMap.get(cur);
        //         if(curValue == 1)
        //         {
        //            sortedMap.remove(cur); 
        //         }
        //         else
        //         {
        //             sortedMap.put(cur, curValue - 1);
        //         }

        //         if(sortedMap.containsKey(cur + 1))
        //         {
        //             cur += 1;
        //         }
        //         else
        //         {
        //             return false;
        //         }
        //     }
        //     if(sortedMap.get(cur) == 1)
        //     {
        //         sortedMap.remove(cur); 
        //     }
        //     else
        //     {
        //         sortedMap.put(cur, sortedMap.get(cur) - 1);
        //     }
        while (!sortedMap.isEmpty()) {
            int start = sortedMap.firstKey();

            for (int i = 0; i < groupSize; i++) {
                int curr = start + i;

                if (!sortedMap.containsKey(curr)) return false;

                int count = sortedMap.get(curr);
                if (count == 1) sortedMap.remove(curr);
                else sortedMap.put(curr, count - 1);
            }
        }


        return true;
    }
}