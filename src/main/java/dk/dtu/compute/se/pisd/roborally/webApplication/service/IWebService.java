package dk.dtu.compute.se.pisd.roborally.webApplication.service;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IWebService {

    String findAllPlayers();
    void addBoard(Board board);
    void setMaxPlayers(int max);
    int permToJoin();
    boolean addPlayer(String playerStr);
    boolean addPlayerDirect(Player player);
    void temp(@NotNull RoboRally roboRally);
//    void joinHost();
    String getMapName();

    void testBoard();

    String getCurrentPlayer();

    Phase getPhase();

    int getMaxPlayers();

    int getAmountOfPlayers();

    int getCurrentPlayerIndex();

    boolean playersReady(int playerNum);
}
