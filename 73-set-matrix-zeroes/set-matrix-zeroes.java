class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int firstRowZero = 1;
        int firstColZero = 1;

        //check first column
        for(int i = 0; i < m; i++)
        {
            if(matrix[i][0] == 0)
            {
                firstColZero = 0;
                break;
            }
        }

        for(int i = 0; i < n; i++)
        {
            if(matrix[0][i] == 0)
            {
                firstRowZero = 0;
                break;
            }
        }

        //for all other rows and columns use first row and column
        for(int i = 1; i < m; i++)
        {
            for(int j = 1; j < n; j++)
            {
                if(matrix[i][j] == 0)
                {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        for(int i = 1; i < m; i++)
        {
            for(int j = 1; j < n; j++)
            {
                if(matrix[i][0] == 0 || matrix[0][j] == 0)
                {
                    matrix[i][j] = 0;
                }
            }
        }

        //now first row or col
        if(firstRowZero == 0)
        {
            for(int i = 0; i < n; i++)
            {
                matrix[0][i] = 0;
            }

        }

        //now first row or col
        if(firstColZero == 0)
        {
            for(int i = 0; i < m; i++)
            {
                matrix[i][0] = 0;
            }
        }

    }
}