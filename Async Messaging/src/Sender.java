import java.sql.*;
import java.util.Scanner;

public class Sender
{
    public static void main(String[] args) throws SQLException
    {

        String url = "jdbc:postgresql://localhost:5432/asyncdb";
        String username = "postgres";
        String password = "fufu9999";

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a message: ");
            String message = scanner.nextLine();

            SenderThread thread1 = new SenderThread(url, username, password, message);
            thread1.start();
        }
    }
}