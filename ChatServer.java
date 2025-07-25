import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 12345;
    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                clients.add(out);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    for (PrintWriter client : clients) {
                        client.println(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) clients.remove(out);
                try { socket.close(); } catch (IOException e) {}
            }
        }
    }
}
