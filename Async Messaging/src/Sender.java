import java.sql.*;
import java.util.Scanner;

public class Sender
{
    public static void main(String[] args) throws SQLException
    {
        /*
        dbs = db_list;
        for each (db in db_list) {

         */

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a message: ");
        String message = scanner.nextLine();

        String senderName = "Fuad";

        Timestamp t = new Timestamp(System.currentTimeMillis());



        String url = "jdbc:postgresql://localhost:5432/asyncdb";
        String username = "postgres";
        String password = "1234";

        Connection con = DriverManager.getConnection(url, username , password);

        String sql = "INSERT INTO as_messages(sender_name, message, sent_time) VALUES(?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, senderName);
        pst.setString(2, message);
        pst.setTimestamp(3, t);
        pst.executeUpdate();

        con.close();
    }
}