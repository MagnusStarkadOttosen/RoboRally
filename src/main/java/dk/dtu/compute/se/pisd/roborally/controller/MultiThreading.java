package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.webApplication.Client;
import dk.dtu.compute.se.pisd.roborally.webApplication.service.IWebService;
import dk.dtu.compute.se.pisd.roborally.webApplication.service.WebService;
import javafx.concurrent.Task;

public class MultiThreading extends Task<Integer> {

    private static Board board;

    private static GameController gameController;

    public MultiThreading(Board board, GameController gameController) {
        this.board = board;
        this.gameController = gameController;
    }

    /**
     * The player waits in this thread so the GUI doesn't freeze.
     * @return
     * @throws Exception
     */
    @Override
    protected Integer call() throws Exception {

        while(!Client.allPlayersFinishedProgramming()){
        }

        Client.updateAllPlayers(board);
        board.setPhase(Client.getPhase());
        board.setStep(0);
        board.setCurrentPlayer(board.getPlayer(Client.getCurrentPlayerIndex()));

        gameController.executePrograms();

        Client.updatePlayers(board.getPlayer(gameController.getPlayerNum()));

        while(!Client.allPlayersFinishedMoving()){
        }
        Client.updateAllPlayers(board);


        return null;
    }
}
