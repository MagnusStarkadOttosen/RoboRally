package dk.dtu.compute.se.pisd.roborally.webApplication.api.model;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import java.util.List;

public class MultiplayerGameSettings {

    private Board board;

    private List<Player> playerList;

    public String getBoardName() {
        return board.boardName;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
