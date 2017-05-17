import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.Scanner;

public class GUI extends JPanel {

    private static final int TILE_SIZE = 128;
    private static final int TILES_MARGIN = 16;
    private static final String FONT_NAME = "Arial";
    private static final Color[] TILE_COLORS = new Color[]{new Color(0x9e948a), new Color(0xeee4da), new Color(0xede0c8), new Color(0xf2b179), new Color(0xf59563), new Color(0xf67c5f),
            new Color(0xf65e3b), new Color(0xedcf72), new Color(0xedcc61), new Color(0xedc850), new Color(0xedc53f), new Color(0xedc22e), new Color(0x3c3a32)};
    private static final Color[] TEXT_COLORS = new Color[]{new Color(0x776E65), new Color(0xf9f6f2)};
    private int[] board = new int[16];


    public static GUI getWindow() {
        GUI gui = new GUI();
        gui.setBackground(new Color(0x92877d));
        JFrame game = new JFrame();
        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setResizable(false);
        game.pack();
        game.setSize(592 + game.getInsets().left + game.getInsets().right, 592 + game.getInsets().top + game.getInsets().bottom);

        game.add(gui);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        return gui;
    }


    public void drawBoard(int[] board) {
        this.board = board;
        repaint();
    }


    public void paint(Graphics g) {
        super.paint(g);
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
                drawTile(g, board[y*4 + x], x, y);
    }


    private void drawTile(Graphics g2, int value, int x, int y) {
        Graphics2D g = ((Graphics2D) g2);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        g.setColor(TILE_COLORS[Math.min(12, value)]);
        g.fillRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE);
        g.setColor(TEXT_COLORS[(value < 3) ? 0 : 1]);
        final int size = value < 7 ? 36 : value < 10 ? 32 : 24;
        final Font font = new Font(FONT_NAME, Font.BOLD, size);
        g.setFont(font);

        String s = String.valueOf(1<<value);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        if (value != 0)
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
    }


    private static int offsetCoors(int arg) {
        return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
    }

    public static void main(String[] args) {
        GUI gui = GUI.getWindow();

        int time = 0, iterations = 100, success_2048 =0, success_1024=0, success_512=0, success_256=0, success_128=0;  //Set the delay and the number of iterations you want to run the game for
        double avg_score=0, max_score=0, min_score= Double.POSITIVE_INFINITY;

        System.out.println("Choose an algorithm: \n Press 1: DFS  2: Stochastic Hill Climber  3: New algorithm");
        Scanner src= new Scanner(System.in);
        int choice = src.nextInt();

        double start_time = System.currentTimeMillis();

        for (int m = 0; m < iterations; m++)
        {
            boolean wooop = true;
            Board board = new Board();
            board.placeRandomTile();
            try {
                Thread.sleep(time);
            } catch (InterruptedException ie) {
                System.out.println("Running...");
            }

            GamePlay ai = new GamePlay(board);        //***************** Normal**************************

            DFS d = new DFS(board);                   //*****************DFS*****************

            StochasticHillClimber s = new StochasticHillClimber(board);   ///*********** Stochastic Hill Climber********************


            int[] grid;
            gui.drawBoard(new int[16]);


            while (wooop) {
                grid = convert(board.getGrid());
                gui.drawBoard(grid);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ie) {
                    System.out.println("Running...");
                }


                if(choice==1)
                {
                    wooop = d.run(board);                 //***************DFS****************8
                    grid = convert(board.getGrid());
                    gui.drawBoard(grid);
                    grid = convert(board.getGrid());
                    gui.drawBoard(grid);
                    board.placeRandomTile();
                }


                if(choice==2)
                {
                    wooop = s.run(board);                 //***************Stochastic Hill Climber****************8
                    grid = convert(board.getGrid());
                    gui.drawBoard(grid);
                    grid = convert(board.getGrid());
                    gui.drawBoard(grid);
                    board.placeRandomTile();
                }

                if(choice==3)
                {
                    wooop = ai.run(board);                        //********** Normal********************
                    ai.newMove(board);
                    grid = convert(board.getGrid());
                    gui.drawBoard(grid);
                    ai.checkSpecial(board);
                    grid = convert(board.getGrid());
                    gui.drawBoard(grid);
                }


            }

            int score = board.getScore();
            if(board.getBiggest()>=2048)
                success_2048++;
            if(board.getBiggest()>=1024)
                success_1024++;
            if(board.getBiggest()>=512)
                success_512++;
            if(board.getBiggest()>=256)
                success_256++;
            if(board.getBiggest()>=128)
                success_128++;
            if(score> max_score)
                max_score= score;
            if(score<min_score)
                min_score = score;

            avg_score = avg_score+ score;
            System.out.println("Iteration no: "+(m+1)+" finished with score: " + score+" and reached: "+board.getBiggest());
        }

        double end_time= System.currentTimeMillis();

        avg_score= avg_score/iterations;
        System.out.println("Reached 128: "+success_128+" / "+iterations);
        System.out.println("Reached 256: "+success_256+" / "+iterations);
        System.out.println("Reached 512: "+success_512+" / "+iterations);
        System.out.println("Reached 1024: "+success_1024+" / "+iterations);
        System.out.println("Reached 2048: "+success_2048+" / "+iterations);
        System.out.println("Average score: " + avg_score);
        System.out.println("Maximum score: " + max_score);
        System.out.println("Minimum score: " + min_score);
        System.out.println("Average time required: "+(end_time-start_time)/iterations);
    }

    private static int[] convert(int[][] grid) {
        int[] converted = new int[16];
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                converted[count] = grid[i][j] == 0 ? 0 : Math.getExponent((double) grid[i][j]);
                count++;
            }
        }
        return converted;
    }
}