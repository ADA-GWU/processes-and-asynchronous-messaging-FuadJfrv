import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Reader
{
    public static void main(String[] args) throws SQLException
    {
        /*
           var ips = ReadDbIpList();
           for each (ip in ips) {
                t = new Thread();
                t.connect(ip);
           }

           while (userInput) {
               text = userInput.Read();
               db = Random(thread).db;
               db.Insert(text);
           }
         */


        String sql = "select name from product where id=8";
        String url = "jdbc:postgresql://localhost:5432/dbtest";
        String username = "postgres";
        String password = "1234";

        Connection con = DriverManager.getConnection(url, username , password);
        Statement st = con.createStatement();
        st.executeQuery(sql);
    }
}
