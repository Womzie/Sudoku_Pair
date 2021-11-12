/** Sudoku solver. */
public class Sudoku {

    /** Prints the solution to the puzzle in the specified directory. */
    public static void main(String[] args) {
        String puzzle = new In("sudoku1.txt").readAll();
        Square[][] grid = createSquares(puzzle);
        solve(grid);
        StdOut.println(toString(grid));


    }

    /** Returns a new Location object with the specified row and column. */
    static Location createLocation(int r, int c) {
        Location here = new Location();
        here.row = r;
        here.column = c;

        return here;
    }

    /** Returns an array of the eight Locations in the same row as here.
     * @return*/
    static Location[] findRow(Location here) {

       // int rowTF = here.row;

        int a = 0;
        Location [] computed = new Location[8];
        for (int i = 0; i < 9; i++) {
            if( i != here.column) {
                computed[a] = createLocation(here.row, i);
                a++;
            }
        }
        return computed;
    }

    /** Returns an array of the eight Locations in the same column as here. */
    static Location[] findColumn(Location here) {
        //int colTF = here.column;
        int a = 0;
        Location [] computed = new Location[9];
        for (int i = 0; i < 9; i++) {
            if(i!= here.row) {
                computed[a] = createLocation(i, here.column);
                a++;
            }
        }
        return computed;

    }

    /** Returns an array of the eight Locations in the same 3x3 block as here. */
    static Location[] findBlock(Location here) {
        int sRow = here.row;
        int sCol = here.column;

        int blockR = (sRow / 3) ;
        int blockC = sCol / 3;

        int loc = 0;
        Location [] computed = new Location[8];
        for (int r = 3 * blockR; r < 3 * blockR + 3; r++) {
            for (int c = 3 * blockC; c < 3 * blockC + 3; c++) {
                if(r != sRow || c != sCol){
                    computed[loc] = createLocation(r,c);
                    loc ++;
                }

            }


        }
        return computed;
    }

    /**
     * Returns a 9x9 array of Squares, each of which has value 0 and knows about the other squares in its row,
     * column, and block.
     */
    static Square[][] createSquares() {
        Square[][] grid = new Square[9][9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                grid[r][c] = new Square();
                grid[r][c].value = 0;
            }
        }

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                //row
                int  a= 0;
                grid[r][c].row = new Square[8];
                for (int iC = 0; iC < 9; iC++) {
                    if( iC != c) {
                        grid[r][c].row[a] = grid[r][iC];
                        a++;
                    }
                }

                //column
                int  b= 0;
                grid[r][c].column = new Square[8];
                for (int iR = 0; iR < 9; iR++) {
                    if( iR != r) {
                        grid[r][c].column[b] = grid[iR][c];
                        b++;
                    }
                }

                //block
                int blockR = r / 3;
                int blockC = c / 3;

                int loc = 0;
                grid[r][c].block = new Square[8];
                for (int iR = 3 * blockR; iR < 3 * blockR + 3; iR++) {
                    for (int iC = 3 * blockC; iC < 3 * blockC + 3; iC++) {
                        if(iR != r || iC != c){
                            grid[r][c].block[loc] = grid[iR][iC];

                            loc ++;
                        }

                    }
                }

            }
        }

        return grid;
    }




    /** Returns a String representing grid, showing the numbers (or . for squares with value 0). */
    static String toString(Square[][] grid) {
        String diagram = "";
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if(grid[r][c].value == 0){
                    diagram += ".";
                }
                else{
                    diagram += grid[r][c].value;
                }
            }
            diagram += "\n";
        }

        return diagram;
    }

    /**
     * Returns a 9x9 array of Squares, each of which has value 0 and knows about the other squares in its row,
     * column, and block. Any numbers in diagram are filled in to the grid; empty squares should be given as
     * periods.
     */
    static Square[][] createSquares(String diagram) {
        Square[][] grid = new Square[9][9];

        //regex
        String[] lines = diagram.split("\\r?\\n|\\r");

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                grid[r][c] = new Square();
                if(lines[r].charAt(c) == '.'){
                    grid[r][c].value = 0;
                }
                else{
                    // minus 48 because of ascii or unicode or whatever
                    grid[r][c].value = (int) lines[r].charAt(c) - 48;
                }

            }
        }

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                //row
                int  a= 0;
                grid[r][c].row = new Square[8];
                for (int iC = 0; iC < 9; iC++) {
                    if( iC != c) {
                        grid[r][c].row[a] = grid[r][iC];
                        a++;
                    }
                }

                //column
                int  b= 0;
                grid[r][c].column = new Square[8];
                for (int iR = 0; iR < 9; iR++) {
                    if( iR != r) {
                        grid[r][c].column[b] = grid[iR][c];
                        b++;
                    }
                }

                //block
                int blockR = r / 3;
                int blockC = c / 3;

                int loc = 0;
                grid[r][c].block = new Square[8];
                for (int iR = 3 * blockR; iR < 3 * blockR + 3; iR++) {
                    for (int iC = 3 * blockC; iC < 3 * blockC + 3; iC++) {
                        if(iR != r || iC != c){
                            grid[r][c].block[loc] = grid[iR][iC];

                            loc ++;
                        }

                    }
                }

            }
        }

        return  grid;
    }

    /**
     * Returns a boolean array of length 10. For each digit, the corresponding entry in the array is true
     * if that number does not appear elsewhere in s's row, column, or block.
     */
    static boolean[] findValidNumbers(Square s) {
        boolean[] vNums =  {false,true,true,true,true,true,true,true,true,true};


        for (int i = 0; i < 8; i++) {
            vNums[s.row[i].value] = false;
            vNums[s.column[i].value] = false;
            vNums[s.block[i].value] = false;

        }
        return vNums;
    }

    /**
     * Returns true if grid can be solved. If so, grid is modified to fill in that solution.
     */
    static boolean solve(Square[][] grid) {

        // Here's an outline of the algorithm:
        // for each square
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                //     if its value is 0
                if(grid[r][c].value == 0) {
                    //         for each valid number that could be filled in
                    boolean[] vNums = findValidNumbers(grid[r][c]);
                    for (int i = 0; i < vNums.length; i++) {
                        if(vNums[i]){
                            grid[r][c].value = i;

                            //StdOut.println(toString(grid));
                            if(solve(grid)){
                                return true;
                            }
                        }
                    }
                    grid[r][c].value = 0;
                    return false;
                    //             if you can solve the rest of the grid
                    //                 return true
                    //         nothing worked: set value back to 0 and return false
                    // no squares left to fill in: return true
                }
            }
        }

        return true;
    }

}