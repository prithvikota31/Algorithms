class Solution {
    public int carFleet(int target, int[] position, int[] speed) {
        int ans = 0;
        int l = position.length;
        List<Car> cars = new ArrayList<>();

        for(int i = 0; i < l; i++)
        {
            cars.add(new Car(position[i], (double)(target - position[i])/speed[i]));
        }

        Collections.sort(cars, (a,b) -> b.pos - a.pos);
        double prevTime = 0;

        for(int i = 0; i < l; i++)
        {
            if(cars.get(i).time > prevTime)
            {
                ans++;
                prevTime = cars.get(i).time;
            }
        }


        return ans;
        
    }

    class Car{
        int pos;
        double time;

        public Car(int pos, double time)
        {
            this.pos = pos;
            this.time = time;
        }
    }
}