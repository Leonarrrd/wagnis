package server;

import server.threads.ServerIOThread;

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

            ServerIOThread serverIOThread = new ServerIOThread(socket, ois);
            serverIOThread.start();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


    }
}
