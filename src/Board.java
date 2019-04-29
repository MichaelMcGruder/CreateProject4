import java.util.ArrayList;



public class Board {

    private int[][] board;

    private int gScore;

    private int size;

    private int blankRow, blankCol;

    private Board parent;

    private int selRow, selCol;

    //Board constructor
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


    /*
    Accessor methods
    */

    //Returns the currents
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


    //Calculate current heuristic for the board
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


    //Returns the gScore (# of moves performed)
    public int getgScore()

    {

        return gScore;

    }


    //Returns heuristic + gScore for A*
    public int getCost()

    {

        return getHeuristic() + gScore;

    }


    //Returns current size n of the n by n board
    public int getSize()

    {

        return size;

    }


    //Returns parent board
    public Board getParent()

    {

        return parent;

    }







    /*
    Mutator methods
     */

    //Sets parent board
    public void setParent(Board board)

    {

        parent = board;

    }


    //Sets gScore
    public void setGScore(int gScore) {

        this.gScore = gScore;

    }


    //Increments gScore by 1
    public void incGScore()

    {
        gScore++;
    }









    //Changing the board (for both the computer and the player)

    //Determines if a tile can be moved
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


    //Moves a tile if it is movable
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

    //Returns a copy of the current board
    public Board getCopy()

    {

        Board copy = new Board(getBoard());

        copy.setParent(parent);

        copy.setGScore(gScore);

        return copy;

    }

    //Returns an array list of next possible board positions
    public ArrayList<Board> getSuccessors()

    {
        //Reference board for the board that is running getSuccessors
        Board bRef = new Board(board);
        bRef.setParent(parent);
        bRef.setGScore(gScore);
        ArrayList<Board> successors = new ArrayList<>();


        for (int i = 1; i < board.length*board.length; i++)

        {
                Board copy = bRef.getCopy(); //Creates a second copy (different object) of bRef
                copy.move(i);

                //Only adds the copy to the successor list if it has a different configuration than bRef
                if (!copy.boardEquals(bRef))
                {
                    copy.setParent(bRef);
                    copy.incGScore();
                    successors.add(copy);
                }

        }



        return successors;

    }



    //Boolean that determines if the current board is solvable

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

    //Converts board to a string
    public String toString()
    {
        String out = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++){
                    if(board[i][j] != 0)
                    {
                        out = out + Integer.toString(board[i][j]) + " ";
                    }
                    else
                    {
                        out = out + "* ";
                    }
            }
             out = out + "\n";
        }

        return out;
    }

    //Determines if two boards have the same configuration
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