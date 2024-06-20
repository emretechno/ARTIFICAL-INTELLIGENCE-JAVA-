public class Hmm {

    private int numStates;
    private int numOutputs;
    private double[] startProb;
    private double[][] transProb;
    private double[][] outputProb;
    

    /** Constructs an HMM from the given data.  The HMM will have
     * <tt>numStates</tt> possible states and <tt>numOutputs</tt>
     * possible outputs.  The HMM is then built from the given set of
     * state and output sequences.  In particular,
     * <tt>state[i][j]</tt> is the <tt>j</tt>-th element of the
     * <tt>i</tt>-th state sequence, and similarly for
     * <tt>output[i][j]</tt>.
     */
    public Hmm(int numStates, int numOutputs,
               int state[][], int output[][]) {
        this.numStates = numStates;
        this.numOutputs = numOutputs;

        // Initialize counts for start, transition, and output probabilities
        int[] startCount = new int[numStates];
        int[][] transCount = new int[numStates][numStates];
        int[][] outputCount = new int[numStates][numOutputs];

        // Count occurrences
        for (int i = 0; i < state.length; i++) {
            // Count start state occurrences
            startCount[state[i][0]]++;
            for (int j = 0; j < state[i].length - 1; j++) {
                transCount[state[i][j]][state[i][j + 1]]++;
                outputCount[state[i][j]][output[i][j]]++;
            }
            // Count the output for the last state
            outputCount[state[i][state[i].length - 1]][output[i][output[i].length - 1]]++;
        }

        // Compute start probabilities with Laplace smoothing
        startProb = new double[numStates];
        int totalStart = state.length;
        for (int i = 0; i < numStates; i++) {
            startProb[i] = Math.log((startCount[i] + 1.0) / (totalStart + numStates));
        }

        // Compute transition probabilities with Laplace smoothing
        transProb = new double[numStates][numStates];
        for (int i = 0; i < numStates; i++) {
            int totalTrans = 0;
            for (int j = 0; j < numStates; j++) {
                totalTrans += transCount[i][j];
            }
            for (int j = 0; j < numStates; j++) {
                transProb[i][j] = Math.log((transCount[i][j] + 1.0) / (totalTrans + numStates));
            }
        }

        // Compute output probabilities with Laplace smoothing
        outputProb = new double[numStates][numOutputs];
        for (int i = 0; i < numStates; i++) {
            int totalOutput = 0;
            for (int j = 0; j < numOutputs; j++) {
                totalOutput += outputCount[i][j];
            }
            for (int j = 0; j < numOutputs; j++) {
                outputProb[i][j] = Math.log((outputCount[i][j] + 1.0) / (totalOutput + numOutputs));
            }
        }
    }

    /** Returns the number of states in this HMM. */
    public int getNumStates() {
        return numStates;
    }

    /** Returns the number of output symbols for this HMM. */
    public int getNumOutputs() {
        return numOutputs;
    }

    /** Returns the log probability assigned by this HMM to a
     * transition from the dummy start state to the given
     * <tt>state</tt>.
     */
    public double getLogStartProb(int state) {
        return startProb[state];
    }

    /** Returns the log probability assigned by this HMM to a
     * transition from <tt>fromState</tt> to <tt>toState</tt>.
     */
    public double getLogTransProb(int fromState, int toState) {
        return transProb[fromState][toState];
    }

    /** Returns the log probability of <tt>state</tt> emitting
     * <tt>output</tt>.
     */
    public double getLogOutputProb(int state, int output) {
        return outputProb[state][output];
    }
}
