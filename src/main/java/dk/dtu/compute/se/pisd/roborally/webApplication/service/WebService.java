package dk.dtu.compute.se.pisd.roborally.webApplication.service;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.AllPlayersTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.ProgramTemplate;
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

//    public Optional<testUser> getUser(Integer id) {
//        Optional optional = Optional.empty();
//        for (testUser user: userList) {
//            if(id == user.getId()){
//                optional = Optional.of(user);
//                return optional;
//            }
//        }
//        return optional;
//    }
//
//    public boolean setMapName(String name) {
//        temp = name;
//        return true;
//    }

    @Override
    public String getMapName(){
        return board.boardName;
    }

//    public boolean addPlayers(List<Player> playerList){
//        this.playerList = playerList;
//        return true;
//    }

    public List<Player> getPlayers(){
        return playerList;
    }

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
        return board.getPlayers().indexOf(board.getCurrentPlayer());
    }

    private static boolean[] readyPlayers;


    @Override
    public boolean playersReady(String programList) {

        Gson gson = new Gson();

        ProgramTemplate template = gson.fromJson(programList, ProgramTemplate.class);

        int playerNum = template.playerNum;
        readyPlayers[playerNum] = true;

        board.getPlayer(playerNum).setProgram(template.program);


        boolean allPlayersReady = true;
        for (boolean bool: readyPlayers) {
            if(bool == false){
                allPlayersReady = false;
            }
        }

        if(allPlayersReady){
            Arrays.fill(readyPlayers, false);
            allPlayersAreReady();
        }

        return true;
    }

    private static GameController gameController;
    @Override
    public void createGameController() {
        gameController = new GameController(board);
    }

    @Override
    public String findAllData() {
        SaveTemplate template = new SaveTemplate();

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

        template.mapName = board.boardName;
        template.phase = board.getPhase();
        template.currentPlayer = board.getPlayers().indexOf(board.getCurrentPlayer());

        Gson gson = new Gson();
        String temp = gson.toJson(template);
        return temp;
    }

    private static boolean isProgFinished = false;

    @Override
    public boolean isProgFinished() {
        return isProgFinished;
    }

    private static boolean isMovingFinished = false;
    @Override
    public boolean isMovingFinished() {
        return isMovingFinished;
    }

    public void addBoard(Board board){
        this.board = board;
    }

    public void setMaxPlayers(int max){
        maxPlayers = max;

        readyPlayers = new boolean[maxPlayers];
        playerFinishedMoving = new boolean[maxPlayers];
        for (int i = 0; i < maxPlayers; i++) {
            readyPlayers[i] = false;
            playerFinishedMoving[i] = false;
        }
    }

    public int permToJoin() {
        if(board.getPlayers().size()==maxPlayers){
            return -1;
        }
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
            gameIsFull();
        }


        return true;
    }

    private void gameIsFull() {
        isProgFinished = false;
        gameController.startProgrammingPhase();
    }

    public boolean addPlayerDirect(Player player){
        board.addPlayer(player);
        player.setSpace(board.getSpace(board.getPlayers().size() % board.width, 0));
        return true;
    }

    public void temp(@NotNull RoboRally roboRally){
        gameController = new GameController(board);

        gameController.startProgrammingPhase();
        roboRally.createBoardView(gameController);
    }

    public void testBoard(){
        System.out.println("testing: " + board.boardName);
    }

    public void allPlayersAreReady(){

        isProgFinished = true;
        isMovingFinished = false;

        gameController.finishProgrammingPhase();
    }


    private static boolean[] playerFinishedMoving;
    @Override
    public boolean updatePlayer(String playerStr) {

        Gson gson = new Gson();
        PlayerTemplate playerTemplate = gson.fromJson(playerStr, PlayerTemplate.class);

        Space space = board.getSpace(playerTemplate.x, playerTemplate.y);
        if(space != null){

            for (Player player: board.getPlayers()) {
                if(player.getName().equals(playerTemplate.playerName)){
                    playerFinishedMoving[board.getPlayers().indexOf(player)] = true;

                    player.setSpace(board.getSpace(playerTemplate.x, playerTemplate.y));
                    player.setHeading(playerTemplate.playerHeading);
                    player.setCheckpoints(playerTemplate.checkpoints);
                }
            }
        }

        boolean allPlayersReady = true;
        for (boolean bool: playerFinishedMoving) {
            if(bool == false){
                allPlayersReady = false;
            }
        }
        if(allPlayersReady){
            Arrays.fill(playerFinishedMoving, false);

            isMovingFinished = true;
            isProgFinished = false;
            gameController.startProgrammingPhase();
        }
        return true;
    }
}
