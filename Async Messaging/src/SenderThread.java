import java.sql.*;

public class SenderThread extends Thread
{
    String url;
    String username;
    String password;
    String sql = "INSERT INTO as_messages(sender_name, message, sent_time) VALUES(?, ?, ?)";
    String senderName = "Dolph";
    String message;

    public SenderThread(String url, String username, String password, String message) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.message = message;
    }

    @Override
    public void run() {
        Connection con = null;
        try
        {
            Timestamp t = new Timestamp(System.currentTimeMillis());

            con = DriverManager.getConnection(url, username , password);

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, senderName);
            pst.setString(2, message);
            pst.setTimestamp(3, t);
            pst.executeUpdate();
            //System.out.println("sent");
            con.close();

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }
}
