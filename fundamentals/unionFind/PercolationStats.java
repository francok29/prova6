import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] thresholdResults;
    private int trials;

    public PercolationStats(int gridLength, int trials) {
        /*
        It does multiple times of a percolation experiment.
        It does multiple times of a percolation experiment.
        */
        if (trials < 1 || gridLength < 1) {
            throw new IllegalArgumentException();
        }

        this.trials = trials;
        thresholdResults = new double[trials];

        for (int i = 0; i < trials; i++) {
            thresholdResults[i] = test(gridLength);
        } 
    }

    private double test(int gridLength) {
        /*
        Test with given lenght of a grid. Creates random row and col and
        open the site. After a system percolates, it calculate threshold
        and return the value
        */

        Percolation percolation = new Percolation(gridLength);
        int openedSites = 0;

        // generate random row and col and open the site.
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(1, gridLength+1);
            int col = StdRandom.uniform(1, gridLength+1);

            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
                openedSites++;
            }
        }
        double threshold = (double) openedSites/(double) (gridLength*gridLength);
        return threshold;
    }
    
    public double mean() {
        // Calculate mean value of the thresholds.
       return StdStats.mean(thresholdResults);
    }
    
    public double stddev() {
        // Calculate standard deviation.
        return StdStats.stddev(thresholdResults); 
    }
    
    public double confidenceLo() {
        // Calculate confidence low
        return mean() - 1.96*stddev()/Math.sqrt(trials);

    }

    public double confidenceHi() {
        // Calculate confidence high
        return mean() + 1.96*stddev()/Math.sqrt(trials);
    }

    public static void main(String[] args) {
        // Unit test

        int gridLength = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats statistic = new PercolationStats(gridLength, trials);

        StdOut.println("mean                    = " + statistic.mean());
        StdOut.println("standard deviation      = " + statistic.stddev());
        StdOut.println("95% confidence interval = " + statistic.confidenceLo() 
                    + ", " + statistic.confidenceHi());
    }
}