import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Sender {
    private static final int PORT = 5432;
    private static final String USERNAME = "dist_user";
    private static final String PASSWORD = "dist_pass_123 ";

    public static void main(String[] args) {
        Map<String, String> serverData = readServerData(); // Method to retrieve list of server IPs and corresponding DB names

        Scanner scanner = new Scanner(System.in);
        List<SenderThread> threads = new ArrayList<>();

        // for each server : assign a thread
        for (Map.Entry<String, String> entry : serverData.entrySet()) {
            String host = entry.getKey();
            String dbName = entry.getValue();
            String url = String.format("jdbc:postgresql://%s:%d/%s", host, PORT, dbName);
            threads.add(new SenderThread(url, USERNAME, PASSWORD, ""));
        }

        while (true) {
            System.out.print("Enter a message: ");
            String message = scanner.nextLine();

            // choose a random thread to send the message
            int chosenThreadIndex = (int) (Math.random() * threads.size());
            SenderThread chosenThread = threads.get(chosenThreadIndex);
            chosenThread.setMessage(message);
            new Thread(chosenThread).start(); // start thread
        }
    }

    // asks server IP and db name from the user same as in Reader class
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
