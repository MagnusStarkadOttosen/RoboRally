package dk.dtu.compute.se.pisd.roborally.webApplication.api.model;

import dk.dtu.compute.se.pisd.roborally.model.Board;

public class MultiplayerGameSettings {

    private Board board;

    public String getBoardName() {
        return board.boardName;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
