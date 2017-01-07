import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // the length of one side of the grid.
    private int gridLength;

    // keep track if the cite is opened.
    private boolean[] isOpen;
    // percolation represents connectivity between sites. connected open sites, percolate to on another.

    private WeightedQuickUnionUF percolation;
    // quick union structure for tracking fullness without backwash.
    // it does not have bottom virtual site.
    private WeightedQuickUnionUF isPercolationFull;
    // when these two virtualindices are connected, the system percolates.
    // virtualTopIndex is connected to entire top row.
    private int virtualTopIndex;

    // virtualBottomIndex is connected to entire bottom row.
    // initializes to (N^2)+1;
    private int virtualBottomIndex;

    public Percolation(int N) {
        /*
        It creates N length of grid and create virtual bottom site 
        and virtual top site.
        */

        // throw error if N (a length of grid) is smaller than 1.
        if (N < 1) {
            throw new IllegalArgumentException();
        }

        gridLength = N;
        int arraySize = N*N+2;
        isOpen = new boolean[arraySize];

        virtualTopIndex = 0;
        virtualBottomIndex = N*N+1;

        isOpen[virtualTopIndex] = true;
        isOpen[virtualBottomIndex] = true;

        percolation = new WeightedQuickUnionUF(arraySize);
        //isPercolationFull does not have a virtual bottom site.
        isPercolationFull = new WeightedQuickUnionUF(arraySize-1);
        

        for (int col = 1; col <= N; col++) {
            // connect all top row sites to virtual top site
            int row = 1;
            int topSiteIndex = getSiteIndex(row,col);
            percolation.union(virtualTopIndex, topSiteIndex);
            isPercolationFull.union(virtualTopIndex, topSiteIndex);

            // connect all bottom row sites to virtual bottom site
            row = N;
            int bottomSiteIndex = getSiteIndex(row,col);
            percolation.union(virtualBottomIndex, bottomSiteIndex);
        } 
    }

    private int getSiteIndex(int row, int col) {
        // get the index of the site.
        checkBounds(row, col);
        return (row-1)*gridLength + col;
    }

    private void checkBounds(int row, int col) {
        /*
        throw error if input row and col are larger than length of grid
        throw error if input row and col are smaller than 1
        */
        if (row > gridLength || row < 1)
        {
            throw new IndexOutOfBoundsException("row index is out of bound");
        }
        if (col > gridLength || col < 1) 
        {
            throw new IndexOutOfBoundsException("column index is out of bounds");
        }
    }

    

    public void open(int row, int col) {
        // open a site if the site is closed and connect to the surrounding
        // up to four sites if the sites are opened.
        int siteIndex = getSiteIndex(row, col);
        if (!isOpen[siteIndex]) {
        
            isOpen[siteIndex] = true;

            if (col > 1 && isOpen(row, col-1)) {
                int indexToLeft = getSiteIndex(row, col-1);
                percolation.union(siteIndex, indexToLeft);
                isPercolationFull.union(siteIndex, indexToLeft);
            }

            if (col < gridLength && isOpen(row, col+1)) {
                int indexToRight = getSiteIndex(row, col+1);
                percolation.union(siteIndex, indexToRight);
                isPercolationFull.union(siteIndex, indexToRight);
            }

            if (row > 1 && isOpen(row-1, col)) {
                int indexToTop = getSiteIndex(row-1, col);
                percolation.union(siteIndex, indexToTop);
                isPercolationFull.union(siteIndex, indexToTop);
            }
            if (row < gridLength && isOpen(row+1, col)) {
                int indexToBottom = getSiteIndex(row+1, col);
                percolation.union(siteIndex, indexToBottom);
                isPercolationFull.union(siteIndex, indexToBottom);   
            }
        }
    }

    public int numberOfOpenSites() {
        // returns the number of sites that are opened.
        int openedSites = 0;
        for (int i = 0; i < this.isOpen.length; i++) {
            if (isOpen[i] == true) {
                openedSites++;
            }
        }
        // subtract the virtual top site and virtual bottom site.
        return openedSites - 2;
    }  

    public boolean isOpen(int row, int col) {
        // return true if the site is opened.
        int siteIndex = getSiteIndex(row, col);
        return isOpen[siteIndex];
    }

    public boolean isFull(int row, int col) {
        // return true if the site is connected to the virtual top site.
        int siteIndex = getSiteIndex(row, col);
        return(isPercolationFull.connected(virtualTopIndex, siteIndex) && isOpen[siteIndex]);
    }

    public boolean percolates() {
        // return true if the system percolates.
        if(gridLength > 1) {
            return percolation.connected(virtualTopIndex, virtualBottomIndex);
        }
        else {
            return isOpen[getSiteIndex(1,1)];
        }
    }

    public static void main(String[] args)
    {
        // unit test
        Percolation percolation3 = new Percolation(3);
        StdOut.println("Nothing is opened. Percolates? " + percolation3.percolates());
        percolation3.open(1,1);
        StdOut.println("One site is opened. Percolates?" + percolation3.percolates());
        percolation3.open(2,1);
        percolation3.open(3,1);
        percolation3.open(2,3);
        percolation3.open(1,3);
        StdOut.println("number of opened sites : " + percolation3.numberOfOpenSites());
        StdOut.println("Does it percolate? : " + percolation3.percolates());       
    }
}
