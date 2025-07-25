import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            // Thread for receiving messages
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Send messages
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;
                out.println(username + ": " + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
