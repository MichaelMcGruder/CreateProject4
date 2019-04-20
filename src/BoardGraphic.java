import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BoardGraphic extends JPanel{
    /*
    For drawing a 3x3 board to the screen at a specific location (relative to top-left corner)
     */

    private int xPos, yPos;

    //Board and Board color grid
    private Board board;


    private final int spacing = 4;
    private final int pieceSize = 168;

    private final int boardSize = 3*spacing + 3*pieceSize;

    private Color[] numColors = new Color[8];

    private int boardMode = 0;

    public BoardGraphic(){
        //Init numColors
        for (int i = 0; i < 8; i++)
        {
            if(i%2 == 0)
            {
                numColors[i] = new Color(128, 0, 0);
            }
            else
            {
                numColors[i] = new Color(0,70,0);
            }

        }
    }


    public BoardGraphic(int x_init, int y_init , int[][] newBoard)
    {
        xPos = x_init;
        yPos = y_init;
        board = new Board(newBoard);

        setBounds(x_init-2, y_init-2, Main.width, Main.height);

        //Init numColors
        for (int i = 0; i < 8; i++)
        {
            if(i%2 == 0)
            {
                numColors[i] = new Color(128,0,0);
            }
            else
            {
                numColors[i] = new Color(0,70,0);
            }

        }
    }

    public void setLoc(int x, int y)
    {
        xPos = x;
        yPos = y;
    }

    public void setMode(int mode)
    {
        switch (mode)
        {
            default:
                boardMode = 0;
                for (int i = 0; i < 8; i++)
                {
                    if(i%2 == 0)
                    {
                        numColors[i] = new Color(128,0,0);
                    }
                    else
                    {
                        numColors[i] = new Color(0,70,0);
                    }

                }
                break;

            case 1: //Winning state
                for (int i = 0; i < 8; i++)
                {
                    numColors[i] = new Color(255, 255, 0);

                }
                break;

            case 2: //Forfeit state
                for (int i = 0; i < 8; i++)
                {
                    numColors[i] = new Color(255, 0, 0);

                }
                break;
        }
    }


    public void setBoard(int[][] b)
    {
        board = new Board(b);
    }



    public void drawBoard(Graphics graphics)
    {
        //Initializing the first piece position
        int x = xPos;
        int y = yPos;


        for (int i = 0; i < board.getSize(); i++)
        {
            for (int j = 0; j < board.getSize(); j++)
            {
                //Draw game piece with spacing if not at the final iteration (except 0)

                if(board.getBoard()[i][j] != 0) {
                    graphics.setColor(numColors[board.getBoard()[i][j]-1]);
                    graphics.fillRoundRect(x, y, pieceSize, pieceSize, 7, 7);
                    graphics.setColor(Color.white);
                    Font f = new Font("Comic Sans",Font.BOLD, 70);
                    FontMetrics fm = getFontMetrics(f);
                    graphics.setFont(f);
                    Rectangle2D r = fm.getStringBounds("1",graphics);graphics.drawString(Integer.toString(board.getBoard()[i][j]), x + (pieceSize / 2) - ((int) (r.getWidth()/2)), y + (pieceSize / 2) + ((int) (r.getHeight()/2))-10);
                }
                if (j != board.getSize()-1)
                {
                    x += pieceSize + spacing;
                }
            }
            if (i != board.getSize()-1)
            {
                y += pieceSize + spacing;
                x = xPos;
            }
        }

    }

}
