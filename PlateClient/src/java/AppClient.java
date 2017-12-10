import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author Michał Śliwa
 */
public class AppClient
{
    //data source query
    private static final String QUERY_STRING = "SELECT * FROM %s";
    
    //plate remote deployment descriptor
    private static final String PLATELOOKUP = "java:global/108222/"
                                                +"Plate!IPlateRemote";
    
    //search remote deployment descriptor
    private static final String COSTLOOKUP = "java:global/108222/"
                                                +"Cost!ICostRemote";
    
    //remote bean representing plate
    private static IPlateRemote plateRemote = null;
    
    //remote bean providing cut calculation
    private static ICostRemote costRemote = null;
    
    /**
     * Main function, reads data and initializes structure
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //get datasource descriptor from params
        String datasourceDescriptor = args[0];
        //get table name from params
        String tableName = args[1];
     
        DataSource dataSource = null;
        try
        {
            //initialize new context
            Context ctx = new  InitialContext();
            //lookup data source
            dataSource = (DataSource)ctx.lookup(datasourceDescriptor);
            //lookup plate remote
            plateRemote = (IPlateRemote)ctx.lookup(PLATELOOKUP);
            //lookup cost remote
            costRemote = (ICostRemote)ctx.lookup(COSTLOOKUP);
        }
        catch (NamingException ex)
        {
            //print naming exceptions
            System.out.println("Lookup failed");
            System.out.println(ex);
        }
        //if data source is found
        if (dataSource != null)
        {
            Boolean plateInitialized = true;
            //establish connection to data source
            try (Connection c = dataSource.getConnection())
            {
                //create new statement
                try (Statement s = c.createStatement())
                {
                    //complete query string
                    String query = String.format(QUERY_STRING,tableName);
                    //execute query
                    ResultSet result = s.executeQuery(query);
                    while(result.next())
                    {
                        //read data, create plate structure
                        addCut(result.getFloat("x"),result.getFloat("y"));
                    }
                }
            }
            catch (SQLException ex)
            {
                plateInitialized = false;
                //print sql exceptions
                System.out.println(ex);
            }
            //check for errors in plate initialization
            if (plateInitialized)
            {
                //inject dependency to cost remote
                costRemote.setPlateReference(plateRemote);
                //print calculated cutting cost
                System.out.println(costRemote.calculateCuttingCost());
            }
            else
            {
                System.out.println("Exception during plate initialization");
            }
        }
        else
        {
            System.out.println("Datasouce name not found");
        }
    }
    
    /**
     * Adds new cut to plate, based on input variables
     * @param x value in x plane
     * @param y value in y plane
     */
    private static void addCut(float x, float y)
    {
        //if x plane, create cut with truth side, otherwise false side
        if (x > 0)
            plateRemote.addCut(true, x);
        else if (y > 0)
            plateRemote.addCut(false, y);
    }
}
