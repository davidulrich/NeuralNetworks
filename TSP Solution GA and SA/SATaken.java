/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

/**
 *
 * @author Faiding
 * 
 * Randomly determines fit candidates and builds array for the Simulated Annealing algorithmic process.
 */
public class SATaken {
    public Integer[] CalcTaken(int cityLength) {
        final boolean taken[] = new boolean [cityLength];
        final Integer path[] = new Integer[cityLength];
        for(int i=0; i < path.length; i++) { taken[i]=false; }
        
        for(int i=0; i < path.length - 1; i++) {
            int icandidate;
            do {
                icandidate = (int) (Math.random() * path.length);
                
            } while (taken[icandidate]);
            path[i] = icandidate;
            taken[icandidate] = true;
            if( i==path.length - 2) {
                icandidate = 0;
                while (taken[icandidate]) {
                    icandidate++;
                }
                path[i+1] = icandidate;
            }
        }
        return path;
    }
}
