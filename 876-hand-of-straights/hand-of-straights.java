class Solution {
    public boolean isNStraightHand(int[] hand, int groupSize) {
        if(hand.length % groupSize != 0)    return false;
        TreeMap<Integer, Integer> map = new TreeMap<>();

        for(int num: hand)
        {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }


        while(!map.isEmpty())
        {
            
            int card = map.firstKey();
            for(int i = 0; i < groupSize; i++)
            {
                if(!map.containsKey(card))
                {
                    return false;
                }
                map.put(card, map.get(card) - 1);
                if(map.get(card) == 0)
                {
                    map.remove(card);
                }
                card++;
            }
        }

        return true;

    }
}