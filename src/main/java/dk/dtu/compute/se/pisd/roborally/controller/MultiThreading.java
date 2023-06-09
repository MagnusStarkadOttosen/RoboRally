package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.webApplication.Client;
import dk.dtu.compute.se.pisd.roborally.webApplication.service.IWebService;
import dk.dtu.compute.se.pisd.roborally.webApplication.service.WebService;
import javafx.concurrent.Task;

public class MultiThreading extends Task<Integer> {

    Board board;

    public MultiThreading(Board board){
        this.board = board;
    }
    @Override
    protected Integer call() throws Exception {

        while(true){
            Client.getAllPlayers(board);
            board.setCurrentPlayer(board.getPlayer(Client.getCurrentPlayerIndex()));
            board.setPhase(Client.getPhase());
        }
    }



//    IWebService webService;
//    RoboRally roboRally;
//
//    Board board;
//
////    public MultiThreading(IWebService webService, RoboRally roboRally) {
////        this.webService = webService;
////        this.roboRally = roboRally;
////    }
//
//    public MultiThreading(Board board){
//        this.board = board;
//    }
//
//    @Override
//    public void run() {
//
////        int maxplayers = Client.getMaxPlayers();
////        int amount = 0;
////
////        do {
////            amount = Client.getCurrentAmountOfPlayers();
//////            System.out.println("waiting: " + amount + "/" + maxplayers);
//////            try {
//////                wait(1000);
//////            } catch (InterruptedException e) {
//////                throw new RuntimeException(e);
//////            }
////        }while(amount != maxplayers);
////        System.out.println("dfsdfjdlf");
////        webService.temp(roboRally);
//
//        while(true){
//            Client.getAllPlayers(board);
//            board.setCurrentPlayer(board.getPlayer(Client.getCurrentPlayerIndex()));
//            board.setPhase(Client.getPhase());
//            try {
//                wait(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//
//    }
}
