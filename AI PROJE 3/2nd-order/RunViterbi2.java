import java.text.*;
import java.io.*;

//import DataSet class , but how ? 
//solved by importing the class from the same package
/**
 * This is a simple class for running your code if you decide to do
 * the optional part of the assignment using second-order Markov
 * models.  You might want to extend or modify this class, or write
 * your own.  (But your code should still work properly when run with
 * this code.)
 **/
public class RunViterbi2 {
    /**
     * A simple main that loads the dataset contained in the file
     * named in the first command-line argument, builds a second-order
     * HMM, prints it out and finds and prints the most likely state
     * sequence for each of the output sequences.
     **/
    public static void main(String[] argv)
            throws FileNotFoundException, IOException {

        if (argv.length != 1) {
            System.err.println("Usage: java RunViterbi2 <datafile>");
            System.exit(1);
        }

        DataSet data = new DataSet(argv[0]);

      Hmm2 hmm = new Hmm2(data.numStates, data.numOutputs, data.trainState, data.trainOutput);
        System.out.println("Start probabilities:");
        for (int i = 0; i < data.numStates; i++) {
            for (int j = 0; j < data.numStates; j++) {
                System.out.print(name(data.stateName[i] + " " + data.stateName[j]) + num(Math.exp(hmm.getLogStartProb(i, j))) + "  ");
            }
            System.out.println();
        }

        System.out.println("\nTransition probabilities:");
        for (int i = 0; i < data.numStates; i++) {
            for (int j = 0; j < data.numStates; j++) {
                for (int k = 0; k < data.numStates; k++) {
                    System.out.print(name(data.stateName[i] + " " + data.stateName[j] + " " + data.stateName[k]) + num(Math.exp(hmm.getLogTransProb(i, j, k))) + "  ");
                }
                System.out.println();
            }
        }

        System.out.println("\nOutput probabilities:");
        for (int i = 0; i < data.numStates; i++) {
            for (int j = 0; j < data.numOutputs; j++) {
                System.out.print(name(data.stateName[i] + " " + data.outputName[j]) + num(Math.exp(hmm.getLogOutputProb(i, j))) + "  ");
            }
            System.out.println();
        }

        System.out.println("\nMost likely state sequences:");
        Viterbi2 viterbi = new Viterbi2(hmm);
        for (int i = 0; i < data.testOutput.length; i++) {
            int[] state = viterbi.mostLikelySequence(data.testOutput[i]);
            for (int j = 0; j < state.length; j++) {
                System.out.print(data.stateName[state[j]] + " ");
            }
            System.out.println();
        }
    }

    // private print formatting stuff
    private static NumberFormat nf = new DecimalFormat("#.000");

    private static String name(String s) {
        return (s + "        ").substring(0, 8);  // Adjusted to ensure proper formatting
    }

    private static String num(double d) {
        return nf.format(d);
    }
}
