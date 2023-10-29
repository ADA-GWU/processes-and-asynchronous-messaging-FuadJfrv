import java.sql.*;
import java.util.Scanner;

public class Sender
{
    public static void main(String[] args) throws SQLException
    {
        String host = "127.0.0.1";
        int port = 5432;
        String dbName = "asyncdb";

        String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, dbName);

        String username = "postgres";
        String password = "123";

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a message: ");
            String message = scanner.nextLine();

            SenderThread thread1 = new SenderThread(url, username, password, message);
            thread1.start();
        }
    }
}