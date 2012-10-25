/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticalgorithm;

/**
 *Main function for the TSP problem
 * @author Faiding
 */
public class TSPChromosome extends Chromosome<Integer, TSPGeneticAlgorithm> {

	protected City cities[];

        //Uniform
	TSPChromosome(final TSPGeneticAlgorithm owner, final City cities[]) {
		this.setGeneticAlgorithm(owner);
		this.cities = cities;

                //assign the main variable to the genes
		final Integer genes[] = new Integer[this.cities.length];
		final boolean taken[] = new boolean[cities.length];

                //set all genes untaken
		for (int i = 0; i < genes.length; i++) {
			taken[i] = false;
		}
		for (int i = 0; i < genes.length - 1; i++) {
			int icandidate;
			do {
                            //take a random point in the genes
				icandidate = (int) (Math.random() * genes.length);
			} while (taken[icandidate]);
                        //apply untaken genes
			genes[i] = icandidate;
                        //denote taken for randomly selected candidates
			taken[icandidate] = true;
			if (i == genes.length - 2) {
				icandidate = 0;
				while (taken[icandidate]) {
					icandidate++;
				}
				genes[i + 1] = icandidate;
			}
		}
		setGenes(genes);
		calculateCost();

	}
        //Cost Function (or fitness) determined by distance between two points observed
        /**
         * The fitness/cost is the total distance between each point.  As the object is to minimize distance/cost,
         * we want the smallest number possible.
         * @throws NeuralNetworkError
         * 
         * Cost Function is variable
         */
	@Override
	public void calculateCost() throws NeuralNetworkError {
		double cost = 500.0;
                //cost relative to index
                double relCost = 0.0;
		for (int i = 0; i < this.cities.length - 1; i++) {
                    //get distance of other city
			final double dist = this.cities[getGene(i)]
					.proximity(this.cities[getGene(i + 1)]);
			cost += dist;
                        //cost factor
                        relCost = (cost / i);
		}
                //set the cost of each leg
		setCost(relCost);

	}

        //transform a gene by swapping with 2 random genes
        //Uniform function
	@Override
	public void mutate() {
		final int length = this.getGenes().length;
		final int iswap1 = (int) (Math.random() * length);
		final int iswap2 = (int) (Math.random() * length);
		final Integer temp = getGene(iswap1);
                //assign each split to a new gene
		setGene(iswap1, getGene(iswap2));
		setGene(iswap2, temp);
	}

}


