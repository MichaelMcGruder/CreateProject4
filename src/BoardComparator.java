import java.util.Comparator;



public class BoardComparator implements Comparator<Board>

{

    @Override
    //Prioritizes lower boards costs
    public int compare(Board b1, Board b2)
    {
        if (b1.getCost() > b2.getCost())
        {
            return 1;
        }

        else if (b1.getCost() < b2.getCost())
        {
            return -1;
        }

        return 0;
    }

}