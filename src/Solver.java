import java.util.*;



public class Solver {

    private PriorityQueue<Board> openList = new PriorityQueue<>(1, new BoardComparator());

    private ArrayList<Board> closedList = new ArrayList<>();

    private int[][] startBoard;





    public Solver(int[][] start)

    {
        startBoard = start;
    }



    private boolean inOpenList(Board board)

    {


        for (Board test : openList) {

            if (test.boardEquals(board)) {

                return true;

            }

        }



        return false;

    }



    private boolean inClosedList(Board board)

    {


        for (Board test : closedList) {

            if (test.boardEquals(board)) {

                return true;

            }

        }



        return false;

    }



    private ArrayList<Integer> reverse(ArrayList<Integer> list)

    {

        ArrayList<Integer> reverse = new ArrayList<>();



        for(int i = list.size()-1; i >= 0; i--)

        {

            reverse.add(list.get(i));

        }



        return reverse;

    }









    public ArrayList<Integer> solve()

    {

        Board root = new Board(startBoard);

        openList.add(root);

        Board solved = root;



        if (root.solvable())

        {

            int heuristic = 1;

            while (heuristic > 0)

            {

                solved = openList.poll();

                heuristic = solved.getHeuristic();

                //System.out.println(solved.getgScore());



                if (heuristic == 0)

                {

                    break;

                }

                else {

                    closedList.add(solved);



                    ArrayList<Board> children = solved.getSuccessors();



                    for (Board child : children)

                    {

                        if (inClosedList(child))

                        {

                            for (int i = 0; i < closedList.size(); i++)

                            {

                                if(closedList.get(i).boardEquals(child))

                                {

                                    if (closedList.get(i).getgScore() > child.getgScore())

                                    {

                                        closedList.remove(i);

                                        closedList.add(child);

                                        break;

                                    }

                                }

                            }

                        }

                        else if (inOpenList(child))

                        {

                            Iterator it = openList.iterator();



                            while(it.hasNext())

                            {

                                Board test = (Board) it.next();



                                if (test.boardEquals(child))

                                {

                                    if (test.getgScore() > child.getgScore())

                                    {

                                        it.remove();

                                        openList.add(child);

                                        break;

                                    }

                                }

                            }

                        }

                        else

                        {

                            openList.add(child);

                        }

                    }

                }

            }

        }

        else

        {

            return null;

        }



        ArrayList<Integer> solveSteps = new ArrayList<>();



        while (solved != null)

        {

            if (solved.getParent() != null)

            {

                for (int i = 0; i < solved.getSize(); i++)

                {

                    int step = -1;

                    for (int j = 0; j < solved.getSize(); j++)

                    {

                        if(solved.getParent().getBoard()[i][j] != solved.getBoard()[i][j])

                        {

                            if (solved.getBoard()[i][j] == 0)

                            {

                                step = solved.getParent().getBoard()[i][j];

                                solveSteps.add(solved.getParent().getBoard()[i][j]);

                                break;

                            }

                            else

                            {

                                step = solved.getBoard()[i][j];

                                solveSteps.add(solved.getBoard()[i][j]);

                                break;

                            }

                        }

                    }

                    if(step != -1)

                    {

                        break;

                    }

                }



            }

            solved = solved.getParent();

        }



        return reverse(solveSteps);

    }


}