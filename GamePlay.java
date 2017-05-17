/**
 * Created by Sanket N on 09-12-2016.
 */

public class GamePlay {

    int time=10;
    private Board board;
    public static Boolean distinct_left=false, compulsory_left=false, compulsory_up=false;
    static Direction new_move;

    public GamePlay(Board board)
    {
     this.board= board;
    }


    public Boolean run(Board board)
    {
        distinct_left=false;
        compulsory_left=false;
        compulsory_up=false;

        Boolean is_runnable = true;
        Direction combine_direction;
        Boolean empty;


        //Is any tile in the bottom row empty and can be filled?
        empty = isBottomEmpty(board);

        if(empty)
            new_move= Direction.DOWN;
        else if (isDistinct(board))
                    new_move= Direction.LEFT;
            else
        {
            combine_direction = combine(board);
            if(combine_direction== Direction.RIGHT)
                new_move= Direction.RIGHT;
            else if(combine_direction==Direction.DOWN)
                     new_move= Direction.DOWN;
                 else if(board.moveRight())
                        new_move= Direction.RIGHT;
                     else if(board.moveDown())
                            new_move= Direction.DOWN;
                            else if(board.moveLeft())
                            {
                                compulsory_left=true;
                                new_move= Direction.LEFT;
                            }
                                else if(board.moveUp())
                                {
                                    compulsory_up= true;
                                    new_move= Direction.UP;
                                }

                                else is_runnable=false;
        }

        return is_runnable;
    }

    public void newMove(Board board)
    {
        board.move(new_move); //take the new move
        try {
            Thread.sleep(time);
        } catch (InterruptedException ie) {
            System.out.println("Running...");
        }

        board.placeRandomTile(); // generate a random tile

        try {
            Thread.sleep(time);
        } catch (InterruptedException ie) {
            System.out.println("Running...");
        }
    }

    public void checkSpecial(Board board)
    {
        if(distinct_left)
        {
            board.move(Direction.DOWN);
            try {
                Thread.sleep(time);
            } catch (InterruptedException ie)
            {
                System.out.println("Running...");
            }
            board.placeRandomTile();
            try {
                Thread.sleep(time);
            } catch (InterruptedException ie)
            {
                System.out.println("Running...");
            }
            return;
        }

        if(compulsory_left)
        {

            if(board.moveRight())
            {
                board.move(Direction.RIGHT);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ie)
                {
                    System.out.println("Running...");
                }
                board.placeRandomTile();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ie)
                {
                    System.out.println("Running...");
                }
                return;
            }
        }

        if(compulsory_up) {

            if (board.moveDown())
            {
                board.moveDown();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ie)
                {
                    System.out.println("Running...");
                }

                board.placeRandomTile();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ie)
                {
                    System.out.println("Running...");
                }
            }

        }

    }




    public Boolean isBottomEmpty(Board board)
    {

       int[][] grid= board.getGrid();

        for(int j=0;j<grid[3].length;j++) // is bottom row empty?
        {
            if (grid[3][j] == 0) // is tile j in the bottom row empty?
            {

                    for (int k = 2; k>=0; k--)
                    {
                        if (grid[k][j] != 0)
                            return true; // there is a tile at row k which can fill the jth position in the bottom row
                    }
            }

        }

        return false; // there is no tile in the above rows above jth position which can fill it
    }


    public Boolean isDistinct(Board board)
    {
        int grid[][] = board.getGrid();


        for(int i=grid.length-1;i>0;i--)
        {
            Boolean result;

            result = isEmpty(grid[i]);

            if(!result) //is the row empty?
            {
                result= isRightCombine(grid[i]);

                if(!result)  //can any tile in the current row merge by taking the right move?
                {
                    result = leftDownCheck(board,i);

                    if(result) //can any tile in the current row merge with a tile in the above row by taking left and down moves consecutively?
                    {
                        distinct_left = true;
                        return true;
                    }

                }
                else break;
            }
            else break;
        }

        return false;

    }

    public Boolean isEmpty(int[] row)
    {
        Boolean result= false;

        for(int i=0;i<row.length;i++)
            if(row[i]==0) {
                result = true;
                break;
            }

        return result;

    }

    public Direction combine(Board board) //can the tiles merge by taking the right or the down move?
    {
        Board copy = board.copy();
        int copy_grid[][] = copy.getGrid();

        for (int i = copy_grid.length - 1; i >= 0; i--)
        {
            if (isRightCombine(copy_grid[i]))
                return Direction.RIGHT;

            if(i==0)
                break;

            if (isDownCombine(copy_grid, i))
                return Direction.DOWN;
         }

        return Direction.LEFT;
    }


    public Boolean isRightCombine(int[] row) // can the tiles in the current row combine by taking the right move?
    {
        for(int i=0;i<row.length-1;i++)
        {
            for(int j=i+1;j<row.length;j++)
            {
                if (row[j] != 0)
                {
                    if (row[i] == row[j])
                         return true;
                    else break;
                }

            }

        }
        return false;
    }

    public Boolean isDownCombine(int[][] grid,int index) //// can the tiles in the current row combine by taking the down move?
    {

        for(int n =0;n<grid[0].length;n++)
        {
            for(int m =index-1;m>=0;m--)
            {
                if (grid[m][n] != 0)
                {
                    if (grid[index][n] == grid[m][n])
                        return true;
                    else break;

                }

            }

        }

        return false;
    }


    public Boolean leftDownCheck(Board board, int index)
    {
        Board copy = board.copy();
        int copy_grid[][];

        if(copy.moveLeft()) // is left possible?
        {
            copy.move(Direction.LEFT); // move the copy to left
            copy_grid= copy.getGrid();

            for(int j=0; j<copy_grid[0].length;j++) // is there an element which can get combined using down move
            {
                if(copy_grid[index][j]== copy_grid[index-1][j])
                    return true;
            }
        }

        return false;

    }

}
