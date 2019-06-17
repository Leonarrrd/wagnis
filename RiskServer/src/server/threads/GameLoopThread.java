package server.threads;

import com.sun.corba.se.spi.activation.ServerManager;
import controller.GameController;
import exceptions.GameNotFoundException;
import model.Game;
import server.SocketGameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import static helper.Events.GET_GAME;

public class GameLoopThread extends Thread {

    private Socket socket;
    private ObjectInputStream ois;

    public GameLoopThread(Socket socket, ObjectInputStream ois) {
        this.socket = socket;
        this.ois = ois;
    }

    @Override
    public void run() {
        System.out.println("Game Loop Thread started...");

        String clientInput;

        while (true) {
            try {
                clientInput = ois.readUTF();
                String split[] = clientInput.split(",");
                String event = split[0];
                UUID gameId = UUID.fromString(split[1]);

                switch (event) {
                    case GET_GAME:

                        Game game = GameController.getInstance().getGameById(gameId);
                        ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(socket);
                        sOos.writeObject(game);
                        sOos.flush();
                }

            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (GameNotFoundException e) {
                e.printStackTrace();
            }



        }
    }

}
