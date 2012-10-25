/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

/**
 *
 * @author Faiding
 */
public class TSPSimulatedAnnealingID extends SimulatedAnnealingID<Integer> {
    
    protected City cities[];
    protected Integer path[];
    
    public TSPSimulatedAnnealingID(final City cities[], final double startTemp, final double stopTemp, final int cycles) {
        this.temperature = startTemp;
        setStartTemperature(startTemp);
        setStopTemperature(stopTemp);
        setCycles(cycles);
        
        this.cities = cities;
        this.path = new Integer[this.cities.length];
    }
    
    //In this case, the cost is the distance between two points
    //Variable function
    @Override
    public double determineError() throws NeuralNetworkError {
        //uses distance to determine cost
        double cost = 0.0;
        //cost relative to index
        double relCost = 0.0;
        for (int i = 0; i < this.cities.length - 1; i++) {
            final double dist = this.cities[this.path[i]].proximity(this.cities[this.path[i+1]]);
           
            cost += dist;
            relCost = (cost / i);
        }
        
        return relCost;
    }
    
    //Get the distance between two cities
    public double distance(final int i, final int j) {
        //city 1
        final int c1 = this.path[i % this.path.length];
        //city 2
        final int c2 = this.path[j % this.path.length];
        return this.cities[c1].proximity(this.cities[c2]);
    }
    
    @Override
    public Integer[] getArray() { return this.path; }
    
    @Override
    public void putArray(final Integer[] array) { this.path = array; }
    
    @Override
    public void randomize(){
        final int length = this.path.length;
        
        //adjust city order according to atomic annealing process
        for (int i=0; i < this.temperature; i++) {
            //floor returns largest integer of the function
            int index1 = (int) Math.floor(length * Math.random());
            int index2 = (int) Math.floor(length * Math.random());
            
            //the distance between both cities
            final double d = distance(index1, index1 + 1) + distance(index2, index2 + 1) - 
                    distance(index1, index2) - distance(index1 + 1, index2 + 1);
            if(d>0) {
                
                //sort indices
                if(index2 < index1) {
                    final int temp = index1;
                    index1 = index2;
                    index2 = temp;
                }
                for (; index2 > index1; index2--) {
                    final int temp = this.path[index1 + 1];
                    this.path[index1 + 1] = this.path[index2];
                    this.path[index2] = temp;
                    index1++;
                }
            }
        } 
    }
    
    @Override
    public Integer[] getArrayCopy() {
        Integer result[] = new Integer[this.path.length];
        System.arraycopy(path, 0, result, 0, path.length);
        return result;
    }
}
