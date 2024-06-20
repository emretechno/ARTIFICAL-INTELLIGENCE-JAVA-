
public class Hmm2 {

    private int numStates;
    private int numOutputs;
    private double[][] startProb;
    private double[][][] transProb;
    private double[][] outputProb;

    /** Constructs a second-order HMM from the given data.  The HMM
     * will have <tt>numStates</tt> possible states and
     * <tt>numOutputs</tt> possible outputs.  The HMM is then built
     * from the given set of state and output sequences.  In
     * particular, <tt>state[i][j]</tt> is the <tt>j</tt>-th element
     * of the <tt>i</tt>-th state sequence, and similarly for
     * <tt>output[i][j]</tt>.
     */
    public Hmm2(int numStates, int numOutputs, int state[][], int output[][]) {
        this.numStates = numStates;
        this.numOutputs = numOutputs;

        // Initialize counts for start, transition, and output probabilities
        int[][] startCount = new int[numStates][numStates];
        int[][][] transCount = new int[numStates][numStates][numStates];
        int[][] outputCount = new int[numStates][numOutputs];

        // Count occurrences
        for (int i = 0; i < state.length; i++) {
            if (state[i].length > 1) {
                startCount[state[i][0]][state[i][1]]++;
            }
            for (int j = 2; j < state[i].length; j++) {
                transCount[state[i][j - 2]][state[i][j - 1]][state[i][j]]++;
                outputCount[state[i][j - 1]][output[i][j - 1]]++;
            }
            // Count the output for the last state
            outputCount[state[i][state[i].length - 1]][output[i][output[i].length - 1]]++;
        }

        // Compute start probabilities with Laplace smoothing
        startProb = new double[numStates][numStates];
        int totalStart = state.length;
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                startProb[i][j] = Math.log((startCount[i][j] + 1.0) / (totalStart + numStates * numStates));
            }
        }

        // Compute transition probabilities with Laplace smoothing
        transProb = new double[numStates][numStates][numStates];
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                int totalTrans = 0;
                for (int k = 0; k < numStates; k++) {
                    totalTrans += transCount[i][j][k];
                }
                for (int k = 0; k < numStates; k++) {
                    transProb[i][j][k] = Math.log((transCount[i][j][k] + 1.0) / (totalTrans + numStates));
                }
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
     * <tt>state1</tt> and <tt>state2</tt>.
     */
    public double getLogStartProb(int state1, int state2) {
        return startProb[state1][state2];
    }

    /** Returns the log probability assigned by this HMM to a
     * transition from <tt>fromState0</tt> to <tt>fromState1</tt> to <tt>toState</tt>.
     */
    public double getLogTransProb(int fromState0, int fromState1, int toState) {
        return transProb[fromState0][fromState1][toState];
    }

    /** Returns the log probability of <tt>state</tt> emitting
     * <tt>output</tt>.
     */
    public double getLogOutputProb(int state, int output) {
        return outputProb[state][output];
    }
}
