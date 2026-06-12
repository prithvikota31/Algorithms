class Solution {
    public int countPrimes(int n) {
        // No primes less than 2.
        if (n <= 2) {
            return 0;
        }

        // isComposite[x] means x is NOT prime.
        // false initially means "not marked composite yet".
        boolean[] isComposite = new boolean[n];

        // Only need to start crossing multiples from numbers up to sqrt(n).
        for (int num = 2; num * num < n; num++) {
            if (!isComposite[num]) {

                // Start from num * num because smaller multiples were already handled
                // by smaller prime factors.
                for (int multiple = num * num; multiple < n; multiple += num) {
                    isComposite[multiple] = true;
                }
            }
        }

        int count = 0;

        // Count numbers that were never marked composite.
        for (int num = 2; num < n; num++) {
            if (!isComposite[num]) {
                count++;
            }
        }

        return count;
    }
}