/* *****************************************************************************
 *  Name:              Florian Woerz
 *  Course:            Algorithms, Part I by Princeton University
 *  Submitted:         2022/02/25
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Given two integers, n and t, this class runs t percolation simulations over (n x n)-grids and
 * for each experiment record the percentage of sites that had to be opened (choosing uniformly at
 * random) for the system to percolate. This will be used to calculate the mean (interpreted as the
 * "percolation threshold"), standard deviation and the endpoints of the 95% confidence intervals
 * for the "percolation threshold" obtained in the experiments.
 */
public class PercolationStats {
    // If the number of trails is sufficiently large (say, at least 30), the 95% confidence interval
    // can be computed as follows: [\overbar{x} - 1.96*s / sqrt{T}, \overbar{x} + 1.96*s / sqrt{T}],
    // where \overbar{x} is the mean of the experiments, s is the standard deviation, and T is the
    // number of trials.
    private static final double CONFIDENCE_95 = 1.96;

    private int trials;
    private double[] thresholds;


    /**
     * Executes `trials` many independent trials of (n x n)-grid percolation experiments.
     * Also calculates the percentage of the nodes that had to be opened for the system to
     * percolate.
     * @param n The width and length of the percolation grids
     * @param trials The number of experiments to be run
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive.");
        if (trials <= 0) throw new IllegalArgumentException("trials must be positive.");

        this.trials = trials;
        thresholds = new double[trials];

        // Execute the independent trials.
        for (int trial = 0; trial < trials; trial++) {
            Percolation perc = new Percolation(n);
            int openCount = 0; // Keeps count of the number of sites we have opened
            int row = 0;
            int col = 0;

            // We keep opening sites until the system percolates
            while (!perc.percolates()) {
                // Open a random closed site:
                boolean openNode = true;

                while (openNode) {
                    row = StdRandom.uniform(1, n + 1); // generate random row between 1 and n
                    col = StdRandom.uniform(1, n + 1); // generate random column between 1 and n
                    openNode = perc.isOpen(row, col);
                }

                // At this point we know that (row, col) is an open site. Thus, we open it up.
                perc.open(row, col);
                openCount++;
            }

            // Record the obtained experimental threshold in our array of experimental results.
            thresholds[trial] = (double) openCount / (n * n);
        }

    }


    /**
     * Returns the sample mean of the percolation threshold as obtained in our experiments.
     * @return sample mean
     */
    public double mean() {
        return StdStats.mean(thresholds);
    }


    /**
     * Returns the standard deviation of our experiments
     * @return standard deviation
     */
    public double stddev() {
        return StdStats.stddev(thresholds);
    }


    /**
     * Returns the low endpoint of the 95% confidence interval
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
    }


    /**
     * Returns the high endpoint of the 95% confidence interval
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
    }


    /**
     * Test client: Outputs the data obtained in the series of percolation experiments
     * @param args Specify n and trials
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("mean\t\t\t\t = " + stats.mean());
        System.out.println("stddev\t\t\t\t = " + stats.stddev());
        System.out.println("95% confidence interval\t = " + "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

}
