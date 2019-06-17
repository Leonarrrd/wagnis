package server;

import com.sun.corba.se.spi.activation.ServerManager;
import model.Game;
import server.threads.GameLoopThread;
import server.threads.ServerLobbyThread;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println("Socket connected!");
        InputStream is;
        ObjectInputStream ois;
        OutputStream os;
        ObjectOutputStream oos;

        try {
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);

            SocketGameManager.getInstance().getSocketObjectOutputStreamMap().put(socket, oos);
            SocketGameManager.getInstance().getSocketObjectInputStreamMap().put(socket, ois);

            ServerLobbyThread serverLobbyThread = new ServerLobbyThread(socket, ois);
            serverLobbyThread.start();


            //new GameLoopThread(socket, ois).start();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


    }
}
