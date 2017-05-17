import java.util.Random;

/**
 * Created by Sanket N on 11-12-2016.
 */

public class StochasticHillClimber {


    private Board board;
    public static Boolean is_runnable;
    public static int depth_limit= 10;
    public static int best_score=0;

    public StochasticHillClimber(Board board)
    {
        this.board= board;
    }


    public Boolean run(Board board)
    {

        is_runnable = true;

        int best_score=0, best_index=0;
        int[] available_directions;

        available_directions = setAvailableDirections(board);

        for(int i=0;i<available_directions.length;i++)
        {
            int score=0;
            if(available_directions[i]==1)
                score= stochasticRecursive(board,0);

            if(score>best_score)
            {
                best_score = score;
                best_index=i;
            }

        }

        Direction new_move;

        new_move = nextMove(best_index);
        board.move(new_move);

        if(board.isAvailableMoves())
             return true;

        else return false;
    }



    public int[] setAvailableDirections(Board board)
    {
        Board copy = board.copy();
        int[] available_directions= new int[4];

        if(copy.moveLeft())
            available_directions[0]=1;

        if(copy.moveRight())
            available_directions[1]=1;

        if(copy.moveUp())
            available_directions[2]=1;

        if(copy.moveDown())
            available_directions[3]=1;


        return available_directions;

    }

    public Direction nextMove(int index)
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



    public int stochasticRecursive(Board board, int depth)
    {
        depth++;

        Board copy = board.copy();
        int[] available_directions = setAvailableDirections(copy);

        Random r = new Random();
        int new_index;

        do
        {
             new_index = r.nextInt(4);
             if(available_directions[new_index]==1) // is the random direction available?
                break;

        }while(true);


        Direction next_move;
        next_move = nextMove(new_index);
        copy.move(next_move); // take the random direction

        if(!copy.isAvailableMoves() || depth>depth_limit)
        {
            int value = copy.getScore();
            if (value > best_score)
            {
                best_score = value;
            }

        }

        else
        {
            return stochasticRecursive(copy, depth);
        }

        return best_score;
    }

}
