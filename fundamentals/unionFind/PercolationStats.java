import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double[] thresholdResults;
    private int trials;

    public PercolationStats(int gridLength, int trials) {
        
        this.trials = trials;

        // It does multiple times of a percolation experiment.
        // The perpose is to getting the percolation threshold. 
        // Create the grid.

        Percolation percolation = new Percolation(gridLength);
        thresholdResults = new double[trials];


        for(int i = 0; i < trials; i++) {
            thresholdResults[i] = test(gridLength);
        } 

    }

    public double test(int gridLength) {
        Percolation percolation = new Percolation(gridLength);
        int openedSites = 0;

        while(!percolation.percolate()) {
            int row = StdRandom.uniform(1, gridLength+1);
            int col = StdRandom.uniform(1, gridLength+1);

            if (!percolation.isOpen(row,col)) {
                percolation.open(row, col);
                openedSites++;
                System.out.println("Opened site! " + openedSites);
            }
        }
        double threshold = (double)openedSites/(double)(gridLength*gridLength);
        System.out.println("threshold " + threshold);
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
    
    public double confidenceLO() {

        return mean() - 1.96*stddev()/Math.sqrt(trials);

    }

    public double confidenceHi() {

        return mean() + 1.96*stddev()/Math.sqrt(trials);

    }

    public void printThresholds(int trials) {
        for (int i = 0; i < trials; i++  ) {
            System.out.println(thresholdResults[i]);    
        }
        
    }

    public static void main (String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats statistic = new PercolationStats(n, trials);

        statistic.printThresholds(trials);        

        StdOut.println("mean                    = " + statistic.mean());
        StdOut.println("standard deviation      = " + statistic.stddev());
        StdOut.println("95% confidence interval = " + statistic.confidenceLO() 
                    + ", " + statistic.confidenceHi());

    }

}