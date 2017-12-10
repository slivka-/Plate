import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateful;

/**
 * Provides methods for calculation ov cutting cost
 * @author Michał Śliwa
 */
@Stateful
public class Cost implements ICostRemote
{
    //reference to plate remote
    @EJB
    private IPlateRemote plateRemote = null;
    
    /**
     * Sets reference to remote plate
     * @param reference 
     */
    @Override
    public void setPlateReference(IPlateRemote reference)
    {
        this.plateRemote = reference;
    }
    
    /**
     * Calculates cost of cutting referenced plate
     * @return cost 
     */
    @Override
    public double calculateCuttingCost()
    {
        //get number of cuts in plate
        int l = plateRemote.getCutsNum();
        
        for (int i=0;i < l;i++)//for each cut
            for (int j=i;i < l;i++)//get all following cuts
                if (!Objects.equals(plateRemote.getCutDirection(j), 
                        plateRemote.getCutDirection(i)))
                    //if dirrection is different increment multiplier
                    plateRemote.incrementCut(j);
        
        double currentSum = 0.0;
        //sum cuts values
        for (int i=0;i<l;i++)
            currentSum += plateRemote.getCurrentCutCost(i);
        //return cutting cost
        return currentSum;
    }
    
}
