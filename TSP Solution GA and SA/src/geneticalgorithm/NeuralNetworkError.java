/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticalgorithm;

/**
 *
 * @author Faiding
 *
 * Used to indicate an error
 */
public class NeuralNetworkError extends RuntimeException {
public NeuralNetworkError(final String msg) {
    super(msg);
}

public NeuralNetworkError(final Throwable t) {
    super(t);
}
}
