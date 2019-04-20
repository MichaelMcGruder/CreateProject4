import java.util.ArrayList;



public class Board {

    private int[][] board;

    private int gScore;

    private int size;

    private int blankRow, blankCol;

    private Board parent;

    private int selRow, selCol;

    public Board(int[][] newBoard)

    {
            board = newBoard;
            size = board.length;

            gScore = 0;

            for(int i = 0; i < board.length; i++)
            {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 0) {
                        blankRow = i;
                        blankCol = j;
                        break;
                    }
                }

            }

            parent = null;


    }



    //Accessor methods

    public int[][] getBoard()

    {
        int[][] boardCopy = new int[board.length][board.length];

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board.length; j++)
            {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;

    }



    public int getHeuristic()

    {

        int count = 1;

        int heuristic = 0;

        for (int i = 0; i < size; i++)

        {

            for (int j = 0; j < size; j++)

            {

                if (board[i][j] != count%(size*size))

                {

                    heuristic++;

                }

                count++;

            }

        }



        return heuristic;

    }



    public int getgScore()

    {

        return gScore;

    }





    public int getCost()

    {

        return getHeuristic() + gScore;

    }



    public int getSize()

    {

        return size;

    }



    public Board getParent()

    {

        return parent;

    }







    //Mutator methods
    public void setParent(Board board)

    {

        parent = board;

    }



    public void setgScore(int gScore) {

        this.gScore = gScore;

    }



    public void incGScore()

    {

        gScore++;

    }









    //Changing the board (for both the computer and the player)

    //Player moves
    public boolean movable(int number)

    {
        int row = -1, column = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
            {
                if (board[i][j] == number)
                {
                    row =  i;
                    column = j;
                    break;
                }
            }
            if (column > -1 && row > -1)
            {
                break;
            }
        }

        selRow = row;
        selCol = column;


        if ((row == blankRow && column != blankCol) || (column == blankCol && row != blankRow))
        {
            return true;
        }
        else
        {
            return false;
        }
    }



    public void move(int number)

    {
        if (movable(number))
        {
            if (selRow == blankRow  && selCol != blankCol) //Horizontal moves
            {
                if (selCol > blankCol)
                {
                    do {
                        board[blankRow][blankCol] = board[blankRow][blankCol + 1];
                        board[blankRow][blankCol + 1] = 0;

                        blankCol++;
                    }while(blankCol != selCol);
                }
                else
                {
                    do {
                        board[blankRow][blankCol] = board[blankRow][blankCol - 1];
                        board[blankRow][blankCol - 1] = 0;

                        blankCol--;
                    }while(blankCol != selCol);
                }
            }
            else if (selCol == blankCol && selRow != blankRow) //Vertical
            {
                if (selRow > blankRow)
                {
                    do {
                        board[blankRow][blankCol] = board[blankRow + 1][blankCol];
                        board[blankRow + 1][blankCol] = 0;

                        blankRow++;
                    }while (blankRow != selRow);
                }
                else
                {
                    do {
                        board[blankRow][blankCol] = board[blankRow - 1][blankCol];
                        board[blankRow - 1][blankCol] = 0;

                        blankRow--;
                    }while(blankRow != selRow);
                }
            }
        }

    }

    //Computer moves
    private boolean compMovable(int number)
    {
        //Down
        try
        {
            if(board[blankRow - 1][blankCol] == number)
            {
                return true;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){}

        //Up
        try
        {
            if(board[blankRow + 1][blankCol] == number)
            {
                return true;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){}

        //Left
        try
        {
            if(board[blankRow][blankCol + 1] == number)
            {
                return true;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){}

        //Right
        try
        {
            if(board[blankRow][blankCol - 1] == number)
            {
                return true;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){}

        return false;
    }


    private void compMove(int number)
    {
        //Down
        try
        {
            if(board[blankRow - 1][blankCol] == number)
            {
                board[blankRow][blankCol] = board[blankRow - 1][blankCol];
                board[blankRow - 1][blankCol] = 0;
                blankRow--;
                return;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){}

        //Up
        try
        {
            if(board[blankRow + 1][blankCol] == number)
            {
                board[blankRow][blankCol] = board[blankRow + 1][blankCol];
                board[blankRow + 1][blankCol] = 0;
                blankRow++;
                return;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){}

        //Left
        try
        {
            if(board[blankRow][blankCol + 1] == number)
            {
                board[blankRow][blankCol] = board[blankRow][blankCol + 1];
                board[blankRow][blankCol + 1] = 0;
                blankCol++;
                return;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){}

        //Right
        try
        {
            if(board[blankRow][blankCol - 1] == number)
            {
                board[blankRow][blankCol] = board[blankRow][blankCol - 1];
                board[blankRow][blankCol - 1] = 0;
                blankCol--;
                return;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){}
    }



    public Board getCopy()

    {

        Board copy = new Board(getBoard());

        copy.setParent(parent);

        copy.setgScore(gScore);

        return copy;

    }

    //Return an array list of next possible board positions
    public ArrayList<Board> getSuccessors()

    {
        //Reference board for the board that is running getSuccessors
        Board bRef = new Board(board);
        bRef.setParent(parent);
        bRef.setgScore(gScore);
        ArrayList<Board> successors = new ArrayList<>();


        for (int i = 1; i < board.length*board.length; i++)

        {
            if (bRef.compMovable(i))

            {

                Board copy = bRef.getCopy();

                copy.setParent(bRef);

                copy.compMove(i);

                copy.incGScore();

                successors.add(copy);

            }

        }



        return successors;

    }



    //Solvable boolean

    public boolean solvable() {

        int parity = (blankCol + blankRow) % 2;



        int inversions = 0;



        //Copy board

        int[][] tempBoard = new int[size][size];



        for (int i = 0; i < size; i++)

        {

            for (int j = 0; j < size; j++)

            {

                tempBoard[i][j] = board[i][j];

            }

        }



        //Solve the board by switching tiles in tempBoard, making sure to count the number of swaps necessary



        for (int i = 0; i < size*size; i++)

        {

            if (tempBoard[i/size][i%size] != (i+1)%(size*size))

            {

                for (int j = 0; j < size*size; j++)

                {

                    if (tempBoard[j/size][j%size] == (i+1)%(size*size))

                    {

                        int temp = tempBoard[i/size][i%size];

                        tempBoard[i/size][i%size] = board[j/size][j%size];

                        tempBoard[j/size][j%size] = temp;

                        break;

                    }

                }



                inversions++;

            }

        }





        return inversions%2 == parity;



    }

    public String toString()
    {
        String out = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                    out = out + Integer.toString(board[i][j]) + " ";

            }
             out = out + "\n";
        }

        return out;
    }



    public boolean boardEquals(Board b)

    {

        for (int i = 0; i < b.getSize(); i++)

        {

            for (int j = 0; j < b.getSize(); j++)

            {

                if (board[i][j] != b.getBoard()[i][j])

                {

                    return false;

                }

            }

        }
        return true;

    }

}