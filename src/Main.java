/*
 Description:

   For my create project, I am creating a game that is similar to the game fifteen, except it is
   a 3x3 puzzle rather than a 4x4. The player will be able to move tiles by left-clicking them
   and each puzzle board is generated randomly (by right clicking). The time that the player takes to
   complete a puzzle will also be recorded by a timer at the bottom of the screen. If the
   player is not able to solve a puzzle, they can request a computer-generated optimal solution to the unfinished board
   by pressing the ENTER key.
  */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;

import static java.lang.System.currentTimeMillis;

public class Main extends JFrame implements  MouseListener, KeyListener{
    Board b = randomBoard();
    long startTime = currentTimeMillis();

    //Origin point
    private static final int xOrigin = 8;
    private static final int yOrigin = 31;

    public static int width = 444; //-16 pixels
    public static int height = 467;//-39 pixels
    private BoardGraphic boardGraphic = new BoardGraphic();

    private static boolean timerStarted = false; //Determines if user is timerStarted a game
    private static boolean solving = false; //Determines if computer is solving the board
    private static boolean forfeited = false;
    private static boolean playing = true;

    private int solveStep = 0;
    private ArrayList<Integer> solveMoves = new ArrayList<>();

    private final int tileLength = 3;

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    //Constructor for JFrame
    public Main() {
        setTitle("Eight Puzzle v1.0");
        setVisible(true);
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setForeground(Color.BLACK);
        setLocationRelativeTo(null);
        addMouseListener(this);
        addKeyListener(this);
    }

    public void start() {
        System.out.println("Hello player. In front of you is a 3 x 3 grid of tiles numbered from 1 to 8, and \n" +
                "the goal is to put the tiles in increasing order from left to right and top to bottom \n" +
                "as shown below. \n");

        int[][] sample = {{1,2,3},{4,5,6},{7,8,0}};
        Board sampleBoard = new Board(sample);

        System.out.println(sampleBoard.toString());

        System.out.println("After you have solved the board or you have forfeited by pressing the SPACE key, you can \n" +
                "right-click to generate a new board and press enter to start the timer again.\n\n" +
                " If you ever have any trouble, you can request the computer\n" +
                "to solve the board by forfeiting and pressing ENTER to let the computer walk you through a solution. \n\n" +
                " When you are ready, PRESS ENTER KEY TO START THE TIMER. ");


    }

    //Generates random board for the computer
    public Board randomBoard() {
        int[][] board = new int[tileLength][tileLength];

        //Random scramble algorithm

        //Create solved nxn board
        for (int i = 0; i <tileLength*tileLength ; i++) {
            board[i / tileLength][i % tileLength] = (i + 1) % (tileLength*tileLength);
        }

        Board b = new Board(board);

        //Perform 50 random moves on the board
        Random r = new Random();

        int parity = r.nextInt(2);

        for (int i = 0; i < 50 + parity; i++) {
            ArrayList<Board> children = b.getSuccessors();
            children.trimToSize();
            int count = children.size();
            b = children.get(r.nextInt(count));

        }

        b.setParent(null);
        b.setgScore(0);

        return b;
    }

    public void solve() {
        System.out.println("Solving...");
        solving = true;
        Solver s = new Solver(b.getBoard());
        ArrayList<Integer> solution = s.solve();

        System.out.println("Printing solution...");
        try{
            Thread.sleep(300);
        }catch (Exception e){}

        solution.trimToSize();

        solveMoves = solution;

        Board bRef = new Board(b.getBoard());

        System.out.println("Step 0");
        System.out.println(bRef.toString());

        for (int i = 0; i < solution.size(); i++) {
            bRef.move(solution.get(i));
            System.out.println("Step " + (i + 1));
            System.out.println(bRef.toString());
        }

        System.out.println("Solved by Computer in " + solution.size()+ " move(s).\n");
        System.out.println("Left-click screen to follow steps.");
        System.out.println("Right-click the screen to play again.");

        solveStep = 0; //Reset for user
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(0,60,90));
        g.fillRect(xOrigin, yOrigin, 428, 428);
        boardGraphic.setLoc(xOrigin + 6, yOrigin + 6);
        boardGraphic.setBoard(b.getBoard());
        boardGraphic.drawBoard(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        //Checking if the mouse is in the timerStarted field

        if (x >= xOrigin && x <= xOrigin + width && y >= yOrigin && y <= yOrigin + width && e.getButton() == MouseEvent.BUTTON1 && timerStarted && !forfeited) {
            //Finding the number in the tile clicked
            for (int i = 0; i < tileLength; i++) {
                for (int j = 0; j < tileLength; j++) {
                    if (x >= xOrigin + 6 + j * (420/tileLength) && x <= xOrigin +6 + BoardGraphic.pieceSize + j * (420/tileLength) && y >= yOrigin + 6 + i * (420/tileLength) && y <= yOrigin + 6 + BoardGraphic.pieceSize + i * (420/tileLength)) {
                        if (b.movable(b.getBoard()[i][j]))
                        {
                            solveMoves.add(b.getBoard()[i][j]);
                        }
                        b.move(b.getBoard()[i][j]);
                        break;
                    }
                }
            }

            //Check if the user solved the board
            if (b.getHeuristic() == 0) {
                timerStarted = false;
                playing = false;
                double solveTime = (double) (currentTimeMillis() - startTime) / 1000;

                boardGraphic.setMode(1);
                if (solveTime/60 >= 1) {
                    int minutes = (int) solveTime/60;
                    double seconds = solveTime - (minutes*60);
                    seconds *= 100;
                    int sec = (int) seconds;
                    seconds = (double) (sec);
                    seconds /= 100;

                    System.out.println("Solved in " + minutes + " minute(s) " +  seconds + " seconds using\n" + solveMoves.size()+ " moves.");
                }
                else
                {
                    System.out.println("Solved in " + solveTime + " seconds using " + solveMoves.size() + " moves.");
                }
                System.out.println();
                System.out.println("Right-click the screen to play again.");
            }

            repaint();

        }


        if (e.getButton() == MouseEvent.BUTTON1 && solving)
        {
                if (solveStep < solveMoves.size()) {
                    b.move(solveMoves.get(solveStep));
                    repaint();
                    solveStep++;
                }
        }

        //Right to restart the game
        if (e.getButton() == MouseEvent.BUTTON3 && (forfeited || b.getHeuristic() == 0)) {
            if (!timerStarted) {
                boardGraphic.setMode(0);
                forfeited = false;
                solving = false;
                playing = true;
                b = randomBoard();
                solveMoves = new ArrayList<>();
                System.out.println();
                System.out.println("Press ENTER to start the timer.");
                repaint();
            }
        }


        /*
        Debugging:
        System.out.println(x + "," + y);
         */

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }



    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }


    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        if (!timerStarted && !forfeited && !solving && playing && e.getKeyChar() == '\n')
        {
            startTime = currentTimeMillis();
            System.out.println("\nTimer started! Press SPACE to forfeit the game.");
            timerStarted = true;
            solving = false;
            forfeited = false;
        }

        if (e.getKeyChar() == '\n' && forfeited && b.getHeuristic() != 0) //Solves the board if the player has forfeited and the board is unsolved
        {
                boardGraphic.setMode(0);
                repaint();
                solve();
        }

        if (e.getKeyChar() == ' ' && b.getHeuristic() != 0)
        {
            if (!forfeited) {
                boardGraphic.setMode(2);
                System.out.println("Forfeited");
                forfeited = true;
                timerStarted = false;
                System.out.println();
                System.out.println("Press ENTER key to let the computer solve the board.");
                System.out.println("Right-click the screen to play again.");
                repaint();
            }
        }
    }



    public void keyPressed(KeyEvent e)
    {}

    public void keyReleased(KeyEvent e)
    {}


}
