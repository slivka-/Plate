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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String tableName = "";
        String datasourceDescriptor = "";
        
        for(String s : args)
        {
            if(s.startsWith("jdbc"))
                datasourceDescriptor = s;
            else
                tableName = s;
        }
        
        if(!datasourceDescriptor.equals(""))
        {
            DataSource dataSource = null;
            try
            {
                Context ctx = new  InitialContext();
                dataSource = (DataSource)ctx.lookup(datasourceDescriptor);
            }
            catch(NamingException ex)
            {
                System.out.println("Lookup failed");
                System.out.println(ex);
            }
            if(dataSource != null && !tableName.equals(""))
            {
                try(Connection c = dataSource.getConnection())
                {
                    try(Statement s = c.createStatement())
                    {
                        String query = String.format(QUERY_STRING,tableName);
                        ResultSet result = s.executeQuery(query);
                        while(result.next())
                        {
                            System.out.println(String.format("%f %f", result.getFloat("x"),result.getFloat("y")));
                        }
                    }
                }
                catch(SQLException ex)
                {
                    System.out.println(ex);
                }
            }
            else
            {
                System.out.println("Datasouce or table name not found");
            }
        }
        else
        {
            System.out.println("Datasource descriptor not found");
        }
    } 
}
