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
import java.util.Timer;

import static java.lang.System.currentTimeMillis;

public class Main extends JFrame implements  MouseListener, KeyListener{
    static Board b = randomBoard();
    long startTime = currentTimeMillis();

    //Origin point
    private static final int xOrigin = 8;
    private static final int yOrigin = 31;

    public static int width = 542;
    public static int height = 563;
    private BoardGraphic boardGraphic = new BoardGraphic();

    private static boolean playing = true; //Determines if user is playing a game
    private static boolean solving = false; //Determines if computer is solving the board
    private static boolean forfeited = false;

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    //Constructor for JFrame
    public Main() {
        setTitle("Eight Puzzle (Beta Version)");
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
        startTime = currentTimeMillis();
        System.out.println("Timer started! Press SPACE to forfeit the game.");
        b = randomBoard();
        playing = true;
        solving = false;
        forfeited = false;
        repaint();
    }

    //Generates random board for the computer
    public static Board randomBoard() {
        int[][] board = new int[3][3];

        //Random scramble algorithm

        //Create solved 3x3 board
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = (i + 1) % 9;
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
        playing = false;
        solving = true;
        Solver s = new Solver(b.getBoard());
        ArrayList<Integer> solution = s.solve();
        solution.trimToSize();

        System.out.println("Step 0");
        System.out.println(b.toString());

        for (int i = 0; i < solution.size(); i++) {
            b.move(solution.get(i));
            System.out.println("Step " + (i+1));
            System.out.println(b.toString());
            repaint();
        }

        System.out.println("Solved by Computer in " + solution.size()+ " move(s).");
        System.out.println();
        System.out.println("Right-click to play again.");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.fillRect(xOrigin, yOrigin, 526, 526);
        boardGraphic.setLoc(xOrigin + 6, yOrigin + 6);
        boardGraphic.setBoard(b.getBoard());
        boardGraphic.drawBoard(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        //Checking if the mouse is in the playing field

        if (x >= xOrigin && x <= xOrigin + width && y >= yOrigin && y <= yOrigin + width && e.getButton() == MouseEvent.BUTTON1 && playing && !forfeited) {
            //Finding the number in the tile clicked
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (x >= xOrigin + 4 + j * 172 && x <= xOrigin +4+ (j + 1) * 172 && y >= yOrigin + 4+ i * 172 && y <= yOrigin + 4 + (i + 1) * 172) {
                        b.move(b.getBoard()[i][j]);
                        break;
                    }
                }
            }

            if (b.getHeuristic() == 0) {
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

                    System.out.println("Solved in " + minutes + " minute(s) " +  seconds + " seconds.");
                }
                else
                {
                    System.out.println("Solved in " + solveTime + " seconds.");
                }
                System.out.println();
                System.out.println("Right-click to play again.");
            }

            repaint();

        }

        if (e.getButton() == MouseEvent.BUTTON3 && (forfeited || b.getHeuristic() == 0)) {
            if (!playing) {
                boardGraphic.setMode(0);
                startTime = currentTimeMillis();
                System.out.println();
                System.out.println("Timer started! Press SPACE to forfeit the game.");
                b = randomBoard();
                playing = true;
                solving = false;
                forfeited = false;
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

        if (e.getKeyChar() == '\n' && forfeited && b.getHeuristic() != 0) //Solves the board if the player has forfeited and the board is unsolved
        {
                boardGraphic.setMode(0);
                revalidate();
                solve();
        }
        else if (e.getKeyChar() == ' ' && b.getHeuristic() != 0)
        {
            if (!forfeited) {
                boardGraphic.setMode(2);
                System.out.println("Forfeited");
                forfeited = true;
                playing = false;
                System.out.println();
                System.out.println("Press ENTER key to let the computer solve the board.");
                System.out.println("Right-click to play again.");
                repaint();
            }
        }
    }



    public void keyPressed(KeyEvent e)
    {}

    public void keyReleased(KeyEvent e)
    {}


}