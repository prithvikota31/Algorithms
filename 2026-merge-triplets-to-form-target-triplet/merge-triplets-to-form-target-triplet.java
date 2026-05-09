class Solution {
    public boolean mergeTriplets(int[][] triplets, int[] target) {
        
        boolean first = false, second = false, third = false;

        for(int i = 0; i < triplets.length; i++)
        {
            if(triplets[i][0] <= target[0] && triplets[i][1] <= target[1] && triplets[i][2] <= target[2])
            {
                if(target[0] == triplets[i][0]) first = true;
                if(target[1] == triplets[i][1]) second = true;
                if(target[2] == triplets[i][2]) third = true;
            }
        }
        return first && second && third;
    }
}