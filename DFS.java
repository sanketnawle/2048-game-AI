/**
 * Created by Sanket N on 11-12-2016.
 */

public class DFS {


    private Board board;
    public static Boolean is_runnable;
    public static int depth_limit= 5;

    public DFS(Board board)
    {
        this.board= board;
    }


    public Boolean run(Board board)
    {

        is_runnable = true;

        int best_score=0, best_index=0;
        int[] available_directions;

        available_directions = setAvailableDirections(board); // what are the available directions now?

        for(int i=0;i<available_directions.length;i++)
        {
            int score=0;
            if(available_directions[i]==1)  //if the direction is available
                score= dfsRecursive(board,i,0); // make a dfs tree for this direction can obtain the best score it can get to

            if(score>best_score)
            {
                best_score = score;
                best_index=i; // which direction was best?
            }

        }

        Direction new_move;

        new_move = nextMove(best_index);

        board.move(new_move); //take the best move

        if(board.isAvailableMoves())
            return true;    // game can progress
        else return false;  // no moves possible here onwards: Game over
    }




    public int[] setAvailableDirections(Board board) //if the direction is available, set the array to 1
    {
        int[] available_directions= new int[4];

        if(board.moveLeft())
            available_directions[0]=1;

        if(board.moveRight())
            available_directions[1]=1;

        if(board.moveUp())
            available_directions[2]=1;

        if(board.moveDown())
            available_directions[3]=1;


        return available_directions;

    }

    public Direction nextMove(int index) //return the direction based on the index
    {
        if(index==0)
            return Direction.LEFT;

        if(index==1)
            return Direction.RIGHT;

        if(index==2)
            return Direction.UP;

        if(index==3)
            return Direction.DOWN;

        return  null;

    }


    public int dfsRecursive(Board board, int index, int depth) // generate a search tree for each direction given by the run function
    {
        depth++;

        int[] available_directions;
        Board copy = board.copy();
        Direction next_move;

        next_move = nextMove(index);
        copy.move(next_move);

        available_directions = setAvailableDirections(copy);
        int best_score=0;


        for(int i=0;i<available_directions.length;i++)
        {
            if(!board.isAvailableMoves() || depth>depth_limit)
            {
                int value = copy.getScore();

                if(value>best_score) {
                    best_score = value;
                }
            }
            else return dfsRecursive(copy,i,depth);

        }

        return best_score;
    }

}
