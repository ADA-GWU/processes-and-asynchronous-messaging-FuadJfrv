import java.sql.*;

public class Reader {
    private static final String url = String.format("jdbc:postgresql://127.0.0.1:5432/asyncdb");
    private static final String username = "postgres";
    private static final String password = "123";
    private static final String senderName = "Fuad";

    public static void main(String[] args) throws SQLException, InterruptedException {
        while (true) {
            try (Connection con = DriverManager.getConnection(url, username, password)) {
                Message message = getNextMessage(con);
                if (message != null) {
                    System.out.printf("Sender %s sent %s at time %s.%n", message.senderName, message.text, message.sentTime);
                    markAsReceived(con, message.recordId);
                    // Sleep for a while if no message is found
                    Thread.sleep(5000);
                }
            }
        }
    }

    private static Message getNextMessage(Connection con) throws SQLException {
        String sql = "SELECT record_id, sender_name, message, sent_time FROM as_messages WHERE RECEIVED_TIME IS NULL AND SENDER_NAME != ? LIMIT 1 FOR UPDATE";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, senderName);
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

    private static void markAsReceived(Connection con, int recordId) throws SQLException {
        String updateSql = "UPDATE as_messages SET RECEIVED_TIME = ? WHERE record_id = ?";
        try (PreparedStatement pst = con.prepareStatement(updateSql)) {
            pst.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pst.setInt(2, recordId);
            pst.executeUpdate();
        }
    }

    static class Message {
        int recordId;
        String senderName;
        String text;
        Timestamp sentTime;
    }
}
