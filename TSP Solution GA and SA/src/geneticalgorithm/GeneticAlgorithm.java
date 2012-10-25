/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticalgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Faiding
 */
abstract public class GeneticAlgorithm<CHROMOSOME_TYPE extends Chromosome<?, ?>> {
    /**
     * This section stores the variables passed into the GeneticAlgorithm class for iteration
     */
    //how many chromosomes to be created
    private int populationSize;
    //percent to mutate
    private double mutationPercent;
    //what percent chosen to mate from entire population
    private double percentToMate;
    //percent of mating population that chooses partners
    private double matingPopulation;
    //if gene will be prevented from repeating
    private boolean preventRepeat;
    //how much genetic material to cut
    private int cutLength;
    //thread pool for processing
    private ExecutorService pool;
    //population
    private CHROMOSOME_TYPE[] chromosomes;
    //get a specific chromosome from the array
    public CHROMOSOME_TYPE getChromosome(final int i) {
        return this.chromosomes[i];
    }
    //get entire population of chromosomes
    public CHROMOSOME_TYPE[] getChromosomes() {
        return this.chromosomes;
    }
    //get the cut length
    public int getCutLength() {
        return this.cutLength;
    }
    //get mating population
    public double getMatingPopulation() {
        return this.matingPopulation;
    }
    //get mutation percent
    public double getMutationPercent() {
        return this.mutationPercent;
    }
    //get percent to mate
    public double getPercentToMate() {
        return this.percentToMate;
    }
    //get the threadpool
    public ExecutorService getPool() {
        return this.pool;
    }
    //get population size
    public int getPopulationSize() {
        return this.populationSize;
    }
    //whether or not genes are prevented
    public boolean isPreventRepeat() {
        return this.preventRepeat;
    }

    //Modify the weight matrix and thresholds based on the last call to calcError
    public void iteration() throws NeuralNetworkError {

        //get the total count to mate
        final int countToMate = (int) (getPopulationSize() * getPercentToMate());
        //produce 2 offspring
        final int offspringCount = countToMate * 2;
        int offspringIndex = getPopulationSize () - offspringCount;

        //build the entire population
        final int matingPopulationSize = (int) (getPopulationSize() * getMatingPopulation());

        //create a new collection for the iterations
        final Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();

        /**
         * Mate and form the next generation, producing two children
         *
         * MOTHER = for each chromosome in the array
         * FATHER = random by the population size
         * CHILD1 = first child
         * CHILD2 = second child
         */
        for(int i=0;i < countToMate; i++) {
            final CHROMOSOME_TYPE mother = this.chromosomes[i];
            final int fatherInt = (int) (Math.random() * matingPopulationSize);
            final CHROMOSOME_TYPE father = this.chromosomes[fatherInt];
            final CHROMOSOME_TYPE child1 = this.chromosomes[offspringIndex];
            final CHROMOSOME_TYPE child2 = this.chromosomes[offspringIndex+1];

            //MateWorker constructor
            final MateWorker<CHROMOSOME_TYPE> worker = new MateWorker<CHROMOSOME_TYPE>(
                    mother, father, child1, child2);

            try {
                if (this.pool !=null) {
                    tasks.add(worker);

                } else {
                    worker.call();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //add the two children to the offspring index
            offspringIndex += 2;
        }
        if(this.pool != null) {
            try {
                this.pool.invokeAll(tasks, 120, TimeUnit.SECONDS);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

        //sort the next generation
        sortChromosomes();
    }

    /**
     * Set the variables
     *
     *
     * @param i
     * @param value
     */
    //set the specified chromosome to a specific value
    public void setChromosome(final int i, final CHROMOSOME_TYPE value){
        this.chromosomes[i] = value;
    }

    //set the entire population
    public void setChromosomes(final CHROMOSOME_TYPE[] chromosomes) {
        this.chromosomes = chromosomes;
    }

    //set the cut length
    public void setCutLength(final int cutLength) {
        this.cutLength = cutLength;
    }

    //set mating population percent
    public void setMatingPopulation(final double matingPopulation) {
        this.matingPopulation = matingPopulation;
    }

    //set the mutation percent
    public void setMutationPercent(final double mutationPercent) {
        this.mutationPercent = mutationPercent;
    }

    //set percent to mate
    public void setPercentToMate(final double percentToMate) {
        this.percentToMate = percentToMate;
    }

    //set thread pool
    public void setPool(final ExecutorService pool) {
        this.pool = pool;
    }

    //set population size
    public void setPopulationSize(final int populationSize) {
        this.populationSize = populationSize;
    }

    //set gene and prevent repeat
    public void setPreventRepeat(final boolean preventRepeat) {
        this.preventRepeat = preventRepeat;
    }

    public void sortChromosomes() {
        Arrays.sort(this.chromosomes);
    }
}
