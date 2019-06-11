package server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        InputStream is = null;
        BufferedReader br = null;
        DataOutputStream out = null;

        try {
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String line;
        while(true) {
            try {
                line = br.readLine();
                if((line == null) ||line.equalsIgnoreCase("quit")) {
                    socket.close();
                } else {
                    //MARK: DO ALL THE GAME LOGIC HERE
                }
            } catch(IOException e) {
                e.printStackTrace();
                return;
            }
        }

    }
}
