/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

/**
 *
 * @author Faiding
 * Holds algorithm for determining atomic fitness
 * 
 * Four parameters are passed into TSPSimulatedAnnealingID, which utilizes uniform functions
 * within these two classes to determine error and cost.  The determinants of error and cost
 * vary by each application of the algorithm.
 */
abstract public class SimulatedAnnealingID<UNIT_TYPE> {
    private double startTemperature;
    private double stopTemperature;
    private int cycles;
    protected double temperature;
    public double error;
    
    //Subclasses must provide a method that evaluates the error for each solution.
    // The lower the error, the better.
    public abstract double determineError() throws NeuralNetworkError;
    
    public abstract UNIT_TYPE[] getArray();
    
    //returns
    public int getCycles() { return this.cycles;    }
    
    public double getStartTemperature() { return this.startTemperature; }
    
    public double getStopTemperature() {  return this.stopTemperature;  }
    
    public double getTemperature() { return this.temperature;  }
    
    public double getError() {  return this.error;  }
    
    //sets
    public abstract UNIT_TYPE[] getArrayCopy();
    
    public abstract void putArray(UNIT_TYPE[] array);
    
    public abstract void randomize();
    
    public void setCycles(final int cycles) {  this.cycles = cycles;  }
    
    public void setError(final double error) {   this.error = error;  }
    
    public void setStartTemperature(final double startTemperature) { this.startTemperature = startTemperature; }
    
    public void setStopTemperature(final double stopTemperature) { this.stopTemperature = stopTemperature; }
    
    public void setTemperature(final double temperature) { this.temperature = temperature; }
    
    
    //perform an iteration of simulated annealing
    public void iteration() throws NeuralNetworkError {
        //array with the best fit
        UNIT_TYPE bestArray[];
        
        setError(determineError());
        bestArray = this.getArrayCopy();
        
        this.temperature = this.getStartTemperature();
        
        //for each cycle...
        for(int i=0; i < this.cycles; i++) {
            //current error
            double curError;
            //randomize each point
            randomize();
            curError = determineError();
            
            //assign final array for best fit
            if (curError < getError()) {
                bestArray = this.getArrayCopy();
                setError(curError);
            }
            
            this.putArray(bestArray);
            
            //ratio to scale the temperature ATOMIC FITNESS ALGORITHM
            final double ratio = Math.exp(Math.log(getStopTemperature() / getStartTemperature())) / (getCycles() - 1);
            this.temperature *= ratio;
        }
    }
    
}
