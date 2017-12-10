import javax.ejb.Remote;

/**
 * Provides methods for calculation ov cutting cost
 * @author Michał Śliwa
 */
@Remote
public interface ICostRemote
{
    /**
     * Sets reference to remote plate
     * @param reference 
     */
    public void setPlateReference(IPlateRemote reference);
    
    /**
     * Calculates cost of cutting referenced plate
     * @return cost 
     */
    public double calculateCuttingCost();
}
