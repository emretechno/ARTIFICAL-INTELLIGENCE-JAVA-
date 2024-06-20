
public class Viterbi {

    private Hmm hmm;

    public Viterbi(Hmm hmm) {
        this.hmm = hmm;
    }

    /**
     * Computes the most likely sequence of states for the given output sequence.
     * @param output The sequence of observed outputs.
     * @return The most likely sequence of states.
     */
    
    public int[] mostLikelySequence(int[] output) {
        int T = output.length; // length of the observed sequence
        int N = hmm.getNumStates(); // number of states in the HMM

        double[][] viterbi = new double[N][T]; // table to store the probabilities of the most likely paths
        int[][] backpointer = new int[N][T]; // table to store the back pointers

        // Initialization step
        for (int s = 0; s < N; s++) {
            viterbi[s][0] = hmm.getLogStartProb(s) + hmm.getLogOutputProb(s, output[0]);
            backpointer[s][0] = 0;
        }

        // Recursion step
        for (int t = 1; t < T; t++) {
            for (int s = 0; s < N; s++) {
                double maxProb = Double.NEGATIVE_INFINITY;
                int bestState = 0;
                for (int sp = 0; sp < N; sp++) {
                    double prob = viterbi[sp][t - 1] + hmm.getLogTransProb(sp, s) + hmm.getLogOutputProb(s, output[t]);
                    if (prob > maxProb) {
                        maxProb = prob;
                        bestState = sp;
                    }
                }
                viterbi[s][t] = maxProb;
                backpointer[s][t] = bestState;
            }
        }

        // Termination step
        double maxProb = Double.NEGATIVE_INFINITY;
        int bestLastState = 0;
        for (int s = 0; s < N; s++) {
            if (viterbi[s][T - 1] > maxProb) {
                maxProb = viterbi[s][T - 1];
                bestLastState = s;
            }
        }

        // Path backtracking
        int[] bestPath = new int[T];
        bestPath[T - 1] = bestLastState;
        for (int t = T - 2; t >= 0; t--) {
            bestPath[t] = backpointer[bestPath[t + 1]][t + 1];
        }

        return bestPath;
    }
}
