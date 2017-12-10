import javax.ejb.Remote;
/**
 * Represents plate structure
 * @author Michał Śliwa
 */
@Remote
public interface IPlateRemote
{
    /**
     * Add new cut to plate
     * @param direction cut direction
     * @param cost cut cost
     */
    public void addCut(Boolean direction, double cost);
    
    /**
     * Returns number of cuts in plate
     * @return number of cuts
     */
    public int getCutsNum();
    
    /**
     * Gets value of cost times multiplier for given cut
     * @param cutNo cut number
     * @return value of cut
     */
    public double getCurrentCutCost(int cutNo);
    
    /**
     * Gets direction of given cut
     * @param cutNo cut number
     * @return direction of cut
     */
    public Boolean getCutDirection(int cutNo);
    
    /**
     * Increments multiplier of given cut
     * @param cutNo cut number
     */
    public void incrementCut(int cutNo);
}
