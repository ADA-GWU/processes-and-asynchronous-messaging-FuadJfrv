import java.sql.*;

public class SenderThread extends Thread {
    private String url;
    private String username;
    private String password;
    private static final String SQL = "INSERT INTO async_messages(sender_name, message, sent_time) VALUES(?, ?, ?)";
    private static final String SENDER_NAME = "Fuad";
    private String message;

    public SenderThread(String url, String username, String password, String message) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            insertMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertMessage() throws SQLException {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement pst = con.prepareStatement(SQL)) {

            pst.setString(1, SENDER_NAME);
            pst.setString(2, message);
            pst.setTimestamp(3, currentTime);
            pst.executeUpdate();
        }
    }
}
