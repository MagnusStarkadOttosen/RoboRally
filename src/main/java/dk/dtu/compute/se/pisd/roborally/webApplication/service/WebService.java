package dk.dtu.compute.se.pisd.roborally.webApplication.service;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.AllPlayersTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SaveTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.webApplication.api.model.testUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class WebService implements IWebService{
    private List<testUser> userList;
    private String temp = "temp1";

    private List<Player> playerList;

    public WebService(){
        userList = new ArrayList<>();

        testUser user1 = new testUser(1, "test1", 5);
        testUser user2 = new testUser(2, "test2", 4);
        testUser user3 = new testUser(3, "test3", 3);
        testUser user4 = new testUser(4, "test4", 2);
        testUser user5 = new testUser(5, "test5", 1);

        userList.addAll(Arrays.asList(user1,user2,user3,user4,user5));
    }

    public Optional<testUser> getUser(Integer id) {
        Optional optional = Optional.empty();
        for (testUser user: userList) {
            if(id == user.getId()){
                optional = Optional.of(user);
                return optional;
            }
        }
        return optional;
    }

    public boolean setMapName(String name) {
        temp = name;
        return true;
    }

    @Override
    public String getMapName(){
//        System.out.println("test from webservice");
//        System.out.println("test: " + this.board.boardName);

        return board.boardName;
    }

    public boolean addPlayers(List<Player> playerList){
        this.playerList = playerList;
        return true;
    }

    public List<Player> getPlayers(){
        return playerList;
    }

//    public boolean addPlayer(Player player){
//        playerList.add(player);
//        return true;
//    }


    private static Board board;

    private static int maxPlayers;

    public String findAllPlayers() {

        AllPlayersTemplate template = new AllPlayersTemplate();

        for (Player player: board.getPlayers()) {
            Space space = player.getSpace();
            PlayerTemplate playerTemplate = new PlayerTemplate();
            playerTemplate.x = space.x;
            playerTemplate.y = space.y;
            playerTemplate.playerName = space.getPlayer().getName();
            playerTemplate.color = space.getPlayer().getColor();
            playerTemplate.playerHeading = space.getPlayer().getHeading();
            playerTemplate.checkpoints = space.getPlayer().getCheckpoints();

            for (CommandCardField card: space.getPlayer().getCardsList()) {
                playerTemplate.cards.add(card.getCard());
            }
            for (CommandCardField card: space.getPlayer().getProgramList()) {
                playerTemplate.program.add(card.getCard());
            }
            template.players.add(playerTemplate);
        }

        Gson gson = new Gson();
        String temp = gson.toJson(template);
//        System.out.println("temp: " + temp);
        return temp;
    }

    public String getCurrentPlayer(){

        Player player = board.getCurrentPlayer();

        Space space = player.getSpace();
        PlayerTemplate playerTemplate = new PlayerTemplate();
        playerTemplate.x = space.x;
        playerTemplate.y = space.y;
        playerTemplate.playerName = space.getPlayer().getName();
        playerTemplate.color = space.getPlayer().getColor();
        playerTemplate.playerHeading = space.getPlayer().getHeading();
        playerTemplate.checkpoints = space.getPlayer().getCheckpoints();

        for (CommandCardField card: space.getPlayer().getCardsList()) {
            playerTemplate.cards.add(card.getCard());
        }
        for (CommandCardField card: space.getPlayer().getProgramList()) {
            playerTemplate.program.add(card.getCard());
        }

        Gson gson = new Gson();
        String temp = gson.toJson(playerTemplate);
//        System.out.println("temp: " + temp);
        return temp;
    }

    @Override
    public Phase getPhase() {
        return board.getPhase();
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public int getAmountOfPlayers() {
        return board.getPlayers().size();
    }

    @Override
    public int getCurrentPlayerIndex() {
//        System.out.println("getCurrentPlayerIndex");
//        System.out.println("board size: " + board.getPlayers().size());




        return board.getPlayers().indexOf(board.getCurrentPlayer());
    }

    private boolean[] readyPlayers;


    @Override
    public boolean playersReady(int playerNum) {

        readyPlayers[playerNum] = true;

        return true;
    }

    public void addBoard(Board board){
        this.board = board;
        System.out.println("after: " + this.board.boardName);
    }

    public void setMaxPlayers(int max){
        maxPlayers = max;

        readyPlayers = new boolean[maxPlayers];
        for (int i = 0; i < maxPlayers; i++) {
            readyPlayers[i] = false;
        }

        System.out.println("MaxPlayers set to: " + max);
    }

    public int permToJoin() {
        System.out.println("1 Players: " + board.getPlayers().size() + " maxPlayer: " + maxPlayers);
        if(board.getPlayers().size()==maxPlayers){
            System.out.println("2 Players: " + board.getPlayers().size() + " maxPlayer: " + maxPlayers);
            return -1;
        }
        System.out.println("3 Players: " + board.getPlayers().size() + " maxPlayer: " + maxPlayers);
        return board.getPlayers().size();
    }

    public boolean addPlayer(String playerStr){

        System.out.println("recieved String addPlayers: " + playerStr);
        Gson gson = new Gson();

        PlayerTemplate playerTemplate = gson.fromJson(playerStr, PlayerTemplate.class);

        Space space = board.getSpace(playerTemplate.x, playerTemplate.y);
        if(space != null){
            Player player = new Player(board, playerTemplate.color, playerTemplate.playerName, playerTemplate.program, playerTemplate.cards);
            player.setHeading(playerTemplate.playerHeading);
            player.setCheckpoints(playerTemplate.checkpoints);
            board.addPlayer(player);
            space.setPlayer(player);

            if(board.getCurrentPlayer() == null){
                board.setCurrentPlayer(player);
            }

        }

        if(board.getPlayers().size() == maxPlayers){
            board.setPhase(Phase.PROGRAMMING);
        }

//        board.addPlayer(player);
//        player.setSpace(board.getSpace(board.getPlayers().size() % board.width, 0));
        return true;
    }

    public boolean addPlayerDirect(Player player){
        board.addPlayer(player);
        player.setSpace(board.getSpace(board.getPlayers().size() % board.width, 0));
        return true;
    }

    private static GameController gameController;

    public void temp(@NotNull RoboRally roboRally){
        gameController = new GameController(board);
//        System.out.println(board.getPlayer(0).getName());


//        MultiThreading multiThreading = new MultiThreading();
//        Thread myThread = new Thread(multiThreading);
//        myThread.start();
//
////        while (board.getPlayers().size() != maxPlayers){
//////            try {
//////                wait(10000);
//////            } catch (InterruptedException e) {
//////                throw new RuntimeException(e);
//////            }
////            System.out.println("waiting on players: " + board.getPlayers().size() + "/" + maxPlayers);
////        }


        gameController.startProgrammingPhase();
        roboRally.createBoardView(gameController);
    }
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
//    public void joinHost(){
//
//
//
//
//        int playerNum = permToJoin();
//        System.out.println(playerNum);
//        if(playerNum != -1){
//            Player player = new Player(board, PLAYER_COLORS.get(playerNum), "Player " + (playerNum+1));
//            addPlayer(player);
//        }
//    }

    public void testBoard(){
        System.out.println("testing: " + board.boardName);
    }


}
