


public class Viterbi2 {

    private Hmm2 hmm;

    public Viterbi2(Hmm2 hmm) {
        this.hmm = hmm;
    }


    public int[] mostLikelySequence(int[] output) {
        // The Viterbi algorithm for a second-order HMM
        int T = output.length;
        int N = hmm.getNumStates();

        double[][][] viterbi = new double[N][N][T];
        int[][][] backpointer = new int[N][N][T];

        // Initialization step
        for (int s1 = 0; s1 < N; s1++) {
            for (int s2 = 0; s2 < N; s2++) {
                if (T > 1) {
                    viterbi[s1][s2][1] = hmm.getLogStartProb(s1, s2) + hmm.getLogOutputProb(s1, output[0]) + hmm.getLogOutputProb(s2, output[1]);
                    backpointer[s1][s2][1] = -1;
                }
            }
            // Special case for T = 1
        }

        // Recursion step
        for (int t = 2; t < T; t++) {
            for (int s1 = 0; s1 < N; s1++) {
                for (int s2 = 0; s2 < N; s2++) {
                    double maxProb = Double.NEGATIVE_INFINITY;
                    int bestState = 0;
                    for (int s0 = 0; s0 < N; s0++) {
                        double prob = viterbi[s0][s1][t - 1] + hmm.getLogTransProb(s0, s1, s2) + hmm.getLogOutputProb(s2, output[t]);
                        if (prob > maxProb) {
                            maxProb = prob;
                            bestState = s0;
                        }
                    }
                    viterbi[s1][s2][t] = maxProb;
                    backpointer[s1][s2][t] = bestState;
                }
            }
        }

        // Termination step
        double maxProb = Double.NEGATIVE_INFINITY;
        int bestLastState1 = 0;
        int bestLastState2 = 0;
        for (int s1 = 0; s1 < N; s1++) {
            for (int s2 = 0; s2 < N; s2++) {
                if (viterbi[s1][s2][T - 1] > maxProb) {
                    maxProb = viterbi[s1][s2][T - 1];
                    bestLastState1 = s1;
                    bestLastState2 = s2;
                }
            }
        }

        // Path backtracking
        int[] bestPath = new int[T];
        bestPath[T - 1] = bestLastState2;
        bestPath[T - 2] = bestLastState1;
        for (int t = T - 3; t >= 0; t--) {
            bestPath[t] = backpointer[bestPath[t + 1]][bestPath[t + 2]][t + 2];
        }

        return bestPath;
    }
}
