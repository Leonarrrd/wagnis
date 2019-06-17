package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int PORT = 33478;

    public void run() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on " + PORT + ".");

        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                socket = serverSocket.accept();
                SocketGameManager.getInstance().getSockets().add(socket.getInetAddress().toString());
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

            new ServerThread(socket).start();
        }
    }
}
