import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.ejb.Stateful;
/**
 * Represents plate structure 
 * @author Michał Śliwa
 */
@Stateful
public class Plate implements IPlateRemote
{
    //private list of cuts
    private final ArrayList<Cut> cutsList = new ArrayList<>();

    /**
     * Add new cut to plate
     * @param direction cut direction
     * @param cost cut cost
     */
    @Override
    public void addCut(Boolean direction, double cost)
    {
        this.cutsList.add(new Cut(direction, cost));
    }
    
    /**
     * Returns number of cuts in plate
     * @return number of cuts
     */
    @Override
    public int getCutsNum()
    {
        //this method is called only once, sort here for better performance
        //sorting descending by cut value
        Collections.sort(cutsList, new CutComparator());
        return cutsList.size();
    }

    /**
     * Gets value of cost times multiplier for given cut
     * @param cutNo cut number
     * @return value of cut
     */    
    @Override
    public double getCurrentCutCost(int cutNo)
    {
        Cut c = cutsList.get(cutNo);
        return c.cost*c.multiplier;
    }
    
    /**
     * Gets direction of given cut
     * @param cutNo cut number
     * @return direction of cut
     */
    @Override
    public Boolean getCutDirection(int cutNo)
    {
        return cutsList.get(cutNo).direction;
    }
    
    /**
     * Increments multiplier of given cut
     * @param cutNo cut number
     */
    @Override
    public void incrementCut(int cutNo)
    {
        cutsList.get(cutNo).multiplier++;
    }
    
    /**
     * Comparator for Cut class
     */
    private class CutComparator implements Comparator<Cut>
    {
        //compares based on cost value
        @Override
        public int compare(Cut o1, Cut o2)
        {
            return (int)Math.signum(o2.cost-o1.cost);
        }
    } 
                
    /**
     * Class representing a single cut
     */
    private class Cut
    {
        //direction of cut
        public Boolean direction;
        //cost of cut
        public double cost;
        //cut cost multiplier
        public int multiplier;
        
        public Cut(Boolean direction, double cost)
        {
            this.direction = direction;
            this.cost = cost;
            this.multiplier = 1;
        }
    }
}
