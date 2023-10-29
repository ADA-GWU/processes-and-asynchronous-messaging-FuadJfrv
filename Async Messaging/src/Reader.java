import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Reader {
    private String url;
    private static final String USERNAME = "dist_user";
    private static final String PASSWORD = "dist_pass_123";
    private static final String SENDER_NAME = "Fuad";

    public Reader(String host, String dbName) {
        this.url = String.format("jdbc:postgresql://%s:5432/%s", host, dbName);
    }

    //constantly reads new messages from the database and prints them
    public void startReading() throws SQLException, InterruptedException {
        while (true) {
            try (Connection con = DriverManager.getConnection(url, USERNAME, PASSWORD)) {
                Message message = getNextMessage(con);
                if (message != null) {
                    System.out.printf("Sender %s sent %s at time %s.%n", message.senderName, message.text, message.sentTime);
                    markAsReceived(con, message.recordId);
                    Thread.sleep(3000); //wait 3 seconds before reading again
                }
            }
        }
    }

    // get the next available message from the database
    private Message getNextMessage(Connection con) throws SQLException {
        String sql = "SELECT record_id, sender_name, message, sent_time FROM async_messages WHERE RECEIVED_TIME IS NULL AND SENDER_NAME != ? LIMIT 1 FOR UPDATE";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, SENDER_NAME);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Message msg = new Message();
                    msg.recordId = rs.getInt("record_id");
                    msg.senderName = rs.getString("sender_name");
                    msg.text = rs.getString("message");
                    msg.sentTime = rs.getTimestamp("sent_time");
                    return msg;
                }
            }
        }
        return null;
    }

    // marks message as received by updating the RECEIVED_TIME in the database
    private void markAsReceived(Connection con, int recordId) throws SQLException {
        String updateSql = "UPDATE async_messages SET RECEIVED_TIME = ? WHERE record_id = ?";
        try (PreparedStatement pst = con.prepareStatement(updateSql)) {
            pst.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pst.setInt(2, recordId);
            pst.executeUpdate();
        }
    }

    // represents message from the db
    static class Message {
        int recordId;
        String senderName;
        String text;
        Timestamp sentTime;
    }

    public static void main(String[] args) {
        //get server data
        Map<String, String> serverData = readServerData();

        //for each server create a thread
        for (Map.Entry<String, String> entry : serverData.entrySet()) {
            String host = entry.getKey();
            String dbName = entry.getValue();

            Reader reader = new Reader(host, dbName);
            new Thread(() -> {
                try {
                    reader.startReading();
                } catch (SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // asks server IP and db name from the user same as in Sender class
    private static Map<String, String> readServerData() {
        Map<String, String> data = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter server IP (or type 'exit' to finish): ");
            String ip = scanner.nextLine();

            if ("exit".equalsIgnoreCase(ip)) {
                break;
            }

            System.out.print("Enter corresponding DB name for IP " + ip + ": ");
            String dbName = scanner.nextLine();

            data.put(ip, dbName);
        }

        return data;
    }
}
