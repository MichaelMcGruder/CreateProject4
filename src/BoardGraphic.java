import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class BoardGraphic extends JPanel{
    /*
    For drawing a 3x3 board to the screen at a specific location (relative to top-left corner)
     */

    private int xPos, yPos;

    //Board and Board color grid
    private Board board;


    private final int spacing = 4;
    private int pieceSize = 126;
    private int tileLength = 3;

    private final int boardSize = tileLength*spacing + tileLength*pieceSize;

    private final Color[] numColors = new Color[99];

    private int boardMode = 0;

    public BoardGraphic(){
        //Init numColors
        for (int i = 0; i < (tileLength * tileLength) - 1; i++)
        {
            if(i%2 == 0)
            {
                numColors[i] = new Color(128, 0, 0);
            }
            else
            {
                numColors[i]  = new Color(0,70,0);
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
        for (int i = 0; i < (tileLength*tileLength) - 1; i++)
        {
            if(i%2 == 0)
            {
                numColors[i] = new Color(128, 0, 0);
            }
            else
            {
                numColors[i]  = new Color(0,70,0);
            }

        }
    }

    public void setTileLength(int x)
    {
        tileLength = x;
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
                for (int i = 0; i < (tileLength*tileLength) - 1; i++)
                {
                    if(i%2 == 0)
                    {
                        numColors[i] = new Color(128, 0, 0);
                    }
                    else
                    {
                        numColors[i]  = new Color(0,70,0);
                    }

                }
                break;

            case 1: //Winning state
                for (int i = 0; i < (tileLength*tileLength) - 1; i++)
                {
                    numColors[i] = new Color(255, 255, 0);

                }
                break;

            case 2: //Forfeit state
                for (int i = 0; i < (tileLength*tileLength) - 1; i++)
                {
                    numColors[i] = new Color(255, 0, 0);

                }
                break;
        }
    }


    public void setBoard(int[][] b)
    {
        board = new Board(b);

        //Update size
        setTileLength(board.getBoard().length);

        pieceSize = (528/tileLength)-spacing;
    }



    public void drawBoard(Graphics graphics)
    {
        //Initializing the first piece position
        int x = xPos;
        int y = yPos;

        int fontSize = 0;
        if (tileLength >= 2 && tileLength <= 6)
        {
            fontSize = 70;
        }
        else if (tileLength > 6 && tileLength <=8)
        {
            fontSize = 50;
        }
        else{
            fontSize = 40;
        }

        for (int i = 0; i < tileLength; i++)
        {
            for (int j = 0; j < tileLength; j++)
            {
                //Draw game piece with spacing if not at the final iteration (except 0)

                if(board.getBoard()[i][j] != 0) {
                    graphics.setColor(numColors[board.getBoard()[i][j]-1]);
                    graphics.fillRoundRect(x, y, pieceSize, pieceSize, 10, 10);
                    graphics.setColor(Color.white);
                    Font f = new Font("Comic Sans",Font.BOLD, fontSize);
                    FontMetrics fm = getFontMetrics(f);
                    graphics.setFont(f);
                    Rectangle2D r = fm.getStringBounds(Integer.toString(board.getBoard()[i][j]),graphics);
                    graphics.drawString(Integer.toString(board.getBoard()[i][j]), x + (pieceSize / 2) - ((int) (r.getWidth()/2)), y + (pieceSize / 2) + ((int) (r.getHeight()/2))-10);
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
