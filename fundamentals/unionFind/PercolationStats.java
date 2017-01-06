import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double[] thresholdResults;
    private int trials;

    public PercolationStats(int n, int trials) {
        
        this.trials = trials;

        // It does multiple times of a percolation experiment.
        // The perpose is to getting the percolation threshold. 
        // Create the grid.

        Percolation percolation = new Percolation(n);
        thresholdResults = new double[trials];

        int opnedSites = 0;
        for (int i = 0; i < trials + 1; i++) {
            while(!percolation.percolate()) {

                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);

                percolation.open(row, col);
                opnedSites++;
            }   

            double threshold = (double)opnedSites/(double)(n*n);
            thresholdResults[i] = threshold;

        }
    }
    
    public double mean() {

        // Calculate mean value of the thresholds.
       return StdStats.mean(thresholdResults);

    }
    
    public double stddev() {

        // Calculate standard deviation.
        return StdStats.stddev(thresholdResults); 

    }
    
    public double confidenceLO() {

        return mean() - 1.96*stddev()/Math.sqrt(trials);

    }

    public double confidenceHi() {

        return mean() + 1.96*stddev()/Math.sqrt(trials);

    }

    public static void main (String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats statistic = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + statistic.mean());
        StdOut.println("standard deviation      = " + statistic.stddev());
        StdOut.println("95% confidence interval = " + statistic.confidenceLO() 
                    + ", " + statistic.confidenceHi());

    }

}