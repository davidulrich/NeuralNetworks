/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticalgorithm;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Faiding
 */
abstract public class Chromosome<GENE_TYPE, GA_TYPE extends GeneticAlgorithm<?>>
		implements Comparable<Chromosome<GENE_TYPE, GA_TYPE>> {
    //cost for chromosome, the lower the better
    private double cost;

    //individual elements of chromosome
    private GENE_TYPE[] genes;

    //genetic algorithm that the chromosome is associated with
    private GA_TYPE geneticAlgorithm;

    //calculate the cost for this chromosome
    abstract public void calculateCost() throws NeuralNetworkError;

    /**
     * Compare two chromosomes and sort by cost
     * 1 is fit, -1 is unfit
     */
    public int compareTo(final Chromosome<GENE_TYPE, GA_TYPE> other) {
        if (getCost() > other.getCost()) {
            return 1;
            } else {
            return -1;
            }
    }

    //return the cost
    public double getCost() {
        return this.cost;
    }

    //get specified gene
    public GENE_TYPE getGene(final int gene) {
        return this.genes[gene];
    }

    //get entire gene array
    public GENE_TYPE[] getGenes() {
        return this.genes;
    }

    //return the genetic algorithm
    public GA_TYPE getGeneticAlgorithm() {
        return this.geneticAlgorithm;
    }

    //get size of the array
    private int size() {
        return this.genes.length;
    }

    //called to mutate
    abstract public void mutate();

    //set the cost of the chromosome
    public void setCost(final double cost) {
        this.cost=cost;
    }

    //set specified genes value
    public void setGene(final int gene, final GENE_TYPE value) {
        this.genes[gene] = value;
    }

    //set entire gene array
    public void setGenes(final GENE_TYPE[] genes) throws NeuralNetworkError {
        this.genes=genes;
    }

    //set genes directly
    public final void setGenesDirect(final GENE_TYPE[] genes)
            throws NeuralNetworkError {
        this.genes=genes;
    }

    //set the genetic algorithm
    public void setGeneticAlgorithm(final GA_TYPE geneticAlgorithm) {
        this.geneticAlgorithm = geneticAlgorithm;
    }

    //convert the chromosome to a string
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[Chromosome: cost=");
        builder.append(getCost());
        return builder.toString();
    }
    
    /**
     * Get list of genes not used.  Useful for avoiding gene repetition.
     */

    private GENE_TYPE getNotTaken(final Chromosome<GENE_TYPE, GA_TYPE> source, final Set<GENE_TYPE> taken) {
        final int geneLength = source.size();

        for(int i=0;i < geneLength; i++) {
            final GENE_TYPE trial = source.getGene(i);
            //add an iteration of the trial if not already there
            if(!taken.contains(trial)) {
                taken.add(trial);
                return trial;
            }
        }

        return null;
    }


    /**
     * Mate with father assuming the mother is present
     */
    public void mate(final Chromosome<GENE_TYPE, GA_TYPE> father,
            final Chromosome<GENE_TYPE, GA_TYPE> offspring1,
            final Chromosome<GENE_TYPE, GA_TYPE> offspring2)
            throws NeuralNetworkError {

        //get the length of the genes
        final int geneLength = getGenes().length;

        //determine the two positions to cut the gene by randomization
        final int cutpoint1 = (int) (Math.random() * (geneLength - getGeneticAlgorithm().getCutLength()));
        final int cutpoint2 = cutpoint1 + getGeneticAlgorithm().getCutLength();

        //keep track of points used in each of the offspring, defaults false
        final Set<GENE_TYPE> taken1 = new HashSet<GENE_TYPE>();
        final Set<GENE_TYPE> taken2 = new HashSet<GENE_TYPE>();

        //handle the cut section
        for (int i=0;i< geneLength; i++){
            //ensure middle portion
            if ((i < cutpoint1) || (i > cutpoint2)) {
            }  else {
                //set the genes to offspring 1 and 2, and apply them to the list of genes taken
                offspring1.setGene(i, father.getGene(i));
                offspring2.setGene(i, this.getGene(i));
                taken1.add(offspring1.getGene(i));
                taken2.add(offspring2.getGene(i));
            }

        }

        //handle the outer sections
        for (int i = 0; i < geneLength; i++) {
            if((i < cutpoint1) || (i > cutpoint2)) {
                //if repeat is prevented...
                if(getGeneticAlgorithm().isPreventRepeat()) {
                    offspring1.setGene(i,getNotTaken(this, taken1));
                    offspring2.setGene(i,getNotTaken(father, taken2));

                } else {
                    offspring1.setGene(i, this.getGene(i));
                    offspring2.setGene(i, father.getGene(i));
                }
            }
        }

        //mutate the genes randomly for each offspring
        if(Math.random() < this.geneticAlgorithm.getMutationPercent()) {
            offspring1.mutate();
        }
        if(Math.random() < this.geneticAlgorithm.getMutationPercent()) {
            offspring2.mutate();
        }

        //calculate the cost
        offspring1.calculateCost();
        offspring2.calculateCost();
    }
}
