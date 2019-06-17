package view.gui.sockets.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

import static helper.Events.*;

public class GameEventThread extends Thread {

    private ObjectInputStream reader;

    public GameEventThread(ObjectInputStream reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        System.out.println("Game loop thread running.");
        while (true) {
            try {
                String response = reader.readUTF();
                // No need to check for response != null

                String[] split = response.split(",");
                //split[0]  should always be the event name
                //split[1]  should always be the game id
                String event = split[0];
                UUID gameId = UUID.fromString(split[1]);

                switch (event) {
                    //TODO: Load Game might be better in LobbyWaitThread
                    case GET_GAME:
                        break;
                    case ADD_COUNTRY:
                        break;
                    case CHANGE_UNITS:
                        break;
                    case ASSIGN_UNITS:
                        break;
                    case ADD_CARD:
                        break;
                    case AWARD_UNITS:
                        break;
                    case CHANGE_UNITS_TO_PLACE:
                        break;
                    case USE_CARDS:
                        break;
                    case GET_COUNTRIES_ATTACK_CAN_BE_LAUNCHED_FROM:
                        break;
                    case GET_COUNTRIES_WITH_MORE_THAN_ONE_UNIT:
                        break;
                    case HAS_COUNTRY_TO_MOVE_FROM:
                        break;
                    case HAS_COUNTRY_TO_ATTACK_FROM:
                        break;
                    case GET_HOSTILE_NEIGHBORS:
                        break;
                    case FIGHT:
                        break;
                    case MOVE:
                        break;
                    case CHECK_WIN_CONDITION:
                        break;
                    case SWITCH_TURNS:
                        break;
                    case UPDATE_PLAYER_GRAPH_MAP:
                        break;
                    case POST_PHASE_CHECK:
                        break;
                    case IS_CONNECTED:
                        break;
                    case SAVE_GAME:
                        break;
                    case REMOVE_GAME:
                        break;
                    case LOAD_AVAILABLE_GAME_IDS:
                        break;


                    default:
                        //TODO: Throw exception
                        break;
                }

            } catch (IOException e) {
                //TODO: add exception?
                e.printStackTrace();
            }
        }
    }
}
