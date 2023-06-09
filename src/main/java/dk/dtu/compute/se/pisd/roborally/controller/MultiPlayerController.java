//package dk.dtu.compute.se.pisd.roborally.controller;
//
//import dk.dtu.compute.se.pisd.roborally.RoboRally;
//import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
//import dk.dtu.compute.se.pisd.roborally.model.Board;
//import dk.dtu.compute.se.pisd.roborally.model.Player;
//import dk.dtu.compute.se.pisd.roborally.webApplication.Client;
//import dk.dtu.compute.se.pisd.roborally.webApplication.SpringAPIApplication;
//import javafx.scene.control.ChoiceDialog;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class MultiPlayerController {
//
//    final private AppController appController;
//    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
//    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
//
//    final private RoboRally roboRally;
//
//    private GameController gameController;
//
//    private Board board;
//
//    private int maxPlayers;
//
//    public MultiPlayerController(AppController appController, @NotNull RoboRally roboRally){
//        this.appController = appController;
//        this.roboRally = roboRally;
//    }
//
//
//
//    public void startServer(){
//        SpringAPIApplication.startSpring();
//        newMultiGame();
//        int player = Client.permToJoin();
//        if(player != -1){
//            Client.addPlayer(new Player(board, PLAYER_COLORS.get(player), "Player " + player));
//        }
//
//        while(board.getPlayers().size() != maxPlayers){
//            //wait until all players are in.
////            System.out.println("waiting");
//        }
//        int temp = 0;
//        for (Player player1:board.getPlayers()) {
//            player1.setSpace(board.getSpace(temp % board.width, temp));
//            temp++;
//        }
//
//        gameController.startProgrammingPhase();
//
//        roboRally.createBoardView(gameController);
//
//
//    }
//
//    public void newMultiGame(){
//        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
//        dialog.setTitle("Player number");
//        dialog.setHeaderText("Select number of players");
//        Optional<Integer> playerAmount = dialog.showAndWait();
//
//        if (playerAmount.isPresent()) {
//            maxPlayers = playerAmount.get();
//            if (gameController != null) {
//                // The UI should not allow this, but in case this happens anyway.
//                // give the user the option to save the game or abort this operation!
//                if (!appController.stopGame()) {
//                    return;
//                }
//            }
//
//            ChoiceDialog<String> mapChoice = new ChoiceDialog<>("map1", "map1", "map2", "map3","map4");
//            mapChoice.setTitle("Select map");
//            mapChoice.setHeaderText("Select map");
//            Optional<String> mapResult = mapChoice.showAndWait();
//
//            board = LoadBoard.loadBoard(mapResult.get());
//
//            System.out.println("Map set: " + Client.setMapName(mapResult.get()) + " with name: " + mapResult.get());
//
//
//
//
//
////            // XXX the board should eventually be created programmatically or loaded from a file
////            //     here we just create an empty board with the required number of players.
////            //board = new Board(8,8);
////            gameController = new GameController(board);
////            /*int no = playerAmount.get();
////            for (int i = 0; i < no; i++) {
////                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
////                board.addPlayer(player);
////                player.setSpace(board.getSpace(i % board.width, i));
////            }*/
////
////            Player player = new Player(board, PLAYER_COLORS.get(0), "Player 1");
////            board.addPlayer(player);
////            player.setSpace(board.getSpace(0 % board.width, 0));
////
////
////            // XXX: V2
////            // board.setCurrentPlayer(board.getPlayer(0));
////            gameController.startProgrammingPhase();
////
////            roboRally.createBoardView(gameController);
//        }
//    }
//
//    public void addPlayer(){
//
//        int playerNum = Client.permToJoin();
//        if(playerNum != -1){
//            Client.addPlayer(new Player(board, PLAYER_COLORS.get(playerNum), "Player " + playerNum));
//        }else{
//            System.out.println("cant join.");
//        }
//
//
//
//    }
//
//    public void joinServer() {
//
//        String mapName = Client.getMapName();
//        System.out.println("mapName: " + mapName);
//        board = LoadBoard.loadSavedGame(mapName);
//        int player = Client.permToJoin();
//        System.out.println(player);
//        if(player != -1){
//            Client.addPlayer(new Player(board, PLAYER_COLORS.get(player), "Player " + player));
//        }
//
//        gameController.startProgrammingPhase();
//
//        roboRally.createBoardView(gameController);
//    }
//}
