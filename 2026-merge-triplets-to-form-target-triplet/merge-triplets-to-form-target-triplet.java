class Solution {
    public boolean mergeTriplets(int[][] triplets, int[] target) {
        boolean first = false, second = false, third = false;
        //any triplet can be used only if all values are less than or equal to values in target

        for(int i = 0; i < triplets.length; i++)
        {
            if(triplets[i][0] <= target[0] && triplets[i][1] <= target[1]
            && triplets[i][2] <= target[2])
            {
                if(triplets[i][0] == target[0]) first = true;
                if(triplets[i][1] == target[1]) second = true;
                if(triplets[i][2] == target[2]) third = true;

            }
        }

        return first && second && third;

    }
}