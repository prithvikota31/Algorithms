class Solution {
    public int carFleet(int target, int[] position, int[] speed) {

        List<Car> cars = new ArrayList<>();
        for(int i = 0; i < position.length; i++)
        {
            cars.add(new Car());
        }

        for(int i = 0; i < position.length; i++)
        {
            
            cars.get(i).position = position[i];
            cars.get(i).time = (double)(target - position[i]) / speed[i];      
        }

        Collections.sort(cars, (a, b)->(b.position - a.position));

        int count = 0;
        double prevtime = 0;

        for(int i = 0; i < cars.size(); i++)
        {
            if(cars.get(i).time >  prevtime)
            {
                count++;
                prevtime = cars.get(i).time;
            }
        } 

        return count;      
    }
    class Car {
    int position;
    double time;
}
    
}

