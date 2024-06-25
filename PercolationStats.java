import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        double[] results = new double[trials];
        for (int i = 0; i < trials; i += 1) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                perc.open(row, col);
            }

            results[i] = (double) perc.numberOfOpenSites() / (n * n);
        }

        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceLo = mean - CONFIDENCE_95 * stddev / Math.sqrt(trials);
        confidenceHi = mean + CONFIDENCE_95 * stddev / Math.sqrt(trials);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats perc = new PercolationStats(n, trials);

        StdOut.printf("mean = %f", perc.mean());
        StdOut.println();

        StdOut.printf("stddev = %f", perc.stddev());
        StdOut.println();

        StdOut.printf("95%% confidence interval = [%f, %f]", perc.confidenceLo(),
                      perc.confidenceHi());
        StdOut.println();
    }
}
