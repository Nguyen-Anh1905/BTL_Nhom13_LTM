
package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import Server.service.Gameroom;

public class Server {
    private static final int PORT = 9999;
    private static Map<Integer, ClientHandler> userHandlers = new ConcurrentHashMap<>();
    private static Map<Integer, Gameroom> gamerooms = new ConcurrentHashMap<>(); // matchId -> Gameroom

    public static Map<Integer, ClientHandler> getUserHandlers() {
        return userHandlers;
    }

    public static Map<Integer, Gameroom> getGamerooms() {
        return gamerooms;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy trên cổng " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                
                // Tạo thread mới cho ClientHandler
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
