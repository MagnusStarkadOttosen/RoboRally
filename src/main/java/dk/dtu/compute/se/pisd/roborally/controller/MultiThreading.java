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

//    private static int playerNum;
//
    private static GameController gameController;

//    private static RoboRally roboRally;

//    public MultiThreading(Board board, int playerNum, GameController gameController, RoboRally roboRally) {
//        this.board = board;
//        this.playerNum = playerNum;
//        this.gameController = gameController;
//        this.roboRally = roboRally;
//    }

    public MultiThreading(Board board, GameController gameController) {
        this.board = board;
        this.gameController = gameController;
    }

    @Override
    protected Integer call() throws Exception {

        while(!Client.allPlayersFinishedProgramming()){
        }


        System.out.println("after waiting");
//        board.removeAllPlayers();
//        System.out.println("after removing all players");
//        Client.getAllData(board);
//        System.out.println("after getAllData");

        Client.updateAllPlayers(board);
        board.setPhase(Client.getPhase());
        board.setStep(0);
        board.setCurrentPlayer(board.getPlayer(Client.getCurrentPlayerIndex()));

        System.out.println("player 1: ");
        for (CommandCardField c:board.getPlayer(0).getProgramList()) {
            if(c.getCard() != null){
                System.out.print(c.getCard().getName() + " ");
            }else{
                System.out.print("null ");
            }
        }
        System.out.println();
        System.out.println("player 2: ");
        for (CommandCardField c:board.getPlayer(1).getProgramList()) {
            if(c.getCard() != null){
                System.out.print(c.getCard().getName() + " ");
            }else{
                System.out.print("null ");
            }
        }
        System.out.println();

        System.out.println("player 1 x: " + board.getPlayer(0).getSpace().x + " y: " + board.getPlayer(0).getSpace().y + " rotation: " + board.getPlayer(0).getHeading());
        System.out.println("player 2 x: " + board.getPlayer(1).getSpace().x + " y: " + board.getPlayer(1).getSpace().y + " rotation: " + board.getPlayer(1).getHeading());

        gameController.executePrograms();

        System.out.println("player 1 x: " + board.getPlayer(0).getSpace().x + " y: " + board.getPlayer(0).getSpace().y + " rotation: " + board.getPlayer(0).getHeading());
        System.out.println("player 2 x: " + board.getPlayer(1).getSpace().x + " y: " + board.getPlayer(1).getSpace().y + " rotation: " + board.getPlayer(1).getHeading());
        System.out.println("after executePrograms");
//        Client.getAllData(board);

        Client.updatePlayers(board.getPlayer(gameController.getPlayerNum()));
        System.out.println("after update player");
        while(!Client.allPlayersFinishedMoving()){
        }
        System.out.println("after allPlayersFinishedProgramming");
//        Client.getAllData(board);
        Client.updateAllPlayers(board);
        System.out.println("after getAllData");

        return null;
    }


    public void temp(Player player){
//        for (int i = 0; i < board.getPlayersNumber(); i++) {
//            Player player = board.getPlayer(i);
//            if (player != null) {
//                for (int j = 0; j < Player.NO_REGISTERS; j++) {
//                    CommandCardField field = player.getProgramField(j);
//                    field.setCard(null);
//                    field.setVisible(true);
//                }
//                for (int j = 0; j < Player.NO_CARDS; j++) {
//                    CommandCardField field = player.getCardField(j);
//                    field.setCard(generateRandomCommandCard());
//                    field.setVisible(true);
//                }
//            }
//        }

        for (int i = 0; i < Player.NO_CARDS; i++) {
            CommandCardField field = player.getCardField(i);
            field.setVisible(true);
        }

        System.out.println(player.getCardField(0).getCard().getName());



    }

}
