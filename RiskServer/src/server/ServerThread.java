package server;

import controller.GameController;
import exceptions.GameNotFoundException;
import model.Game;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println("Socket connected!");
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
        while (true) {
            try {
                line = br.readLine();

                //TODO: SPLIT line to format;
                //MARK: Exception werfen?
                if ((line == null) || line.equalsIgnoreCase("quit")) {
                    socket.close();
                } else {
                    //MARK: DO ALL THE GAME LOGIC HERE
                    UUID gameId = UUID.fromString("blabal");
                    Game game = GameController.getInstance().getGameById(gameId);
                    switch (game.getTurn().getPhase()) {
                        case USE_CARDS:

                            break;
                        case PLACE_UNITS:
                            break;
                        case ATTACK:
                            break;
                        case TRAIL_UNITS:
                            break;
                        case PERFORM_ANOTHER_ATTACK:
                            break;
                        case MOVE:
                            break;
                        case PERFORM_ANOTHER_MOVE:
                            break;
                        default:
                            break;
                    }

                }

            } catch (GameNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
