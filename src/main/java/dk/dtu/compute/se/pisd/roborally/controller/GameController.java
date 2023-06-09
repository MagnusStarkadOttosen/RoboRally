/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.webApplication.Client;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Controlles all functions of the game.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    public boolean isGameIsMultiplayer() {
        return gameIsMultiplayer;
    }

    public void setGameIsMultiplayer(boolean gameIsMultiplayer) {
        this.gameIsMultiplayer = gameIsMultiplayer;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    private int playerNum;

    private boolean gameIsMultiplayer = false;

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved

        if (space != null && space.board == board) {
            Player currentPlayer = board.getCurrentPlayer();
            if (currentPlayer != null && space.getPlayer() == null) {
                currentPlayer.setSpace(space);
                int playerNumber = (board.getPlayerNumber(currentPlayer) + 1) % board.getPlayersNumber();
                board.setCurrentPlayer(board.getPlayer(playerNumber));
            }
        }

    }

    // XXX: V2
    /**
     * Sets the phase to PROGRAMMING and populate the players cards.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    /**
     * This method returns random command card.
     * @return random command card.
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2
    /**
     * Hides the players cards and change the phase to ACTIVATION.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX: V2
    /**
     * Reveals a card.
     * @param register the index of the card to be revealed.
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    /**
     * Hides all the players cards.
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX: V2
    /**
     * Execute entire program.
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2
    /**
     * Execute program one step at a time.
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2
    /**
     * Continues the program at the next step.
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX: V2
    /**
     * Execute the current card and go to the next player.
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if(command.isInteractive()){
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    actionField(step);
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * Then player is on a space that have an action, example conveyor belt, gears and checkpoints, then do what the space require.
     * @param step the current step if the round.
     */
    public void actionField(int step){
        // TODO: Need complete rework. Possible moved out of gamecontroller.
//        for (int i = 0; i < board.getPlayersNumber(); i++) {
//            if (step >= Player.NO_REGISTERS) { //Runs after all players have moved.
//                if(board.getPlayer(i).getSpace().getConveyor() != null){
//                    //moveByConveyor(board.getPlayer(i));
//                    board.getPlayer(i).getSpace().getConveyor().doAction(this,board.getPlayer(i).getSpace());
//                }
//                if(board.getPlayer(i).getSpace().getGear() != null){
//                    /*if(board.getPlayer(i).getSpace().getGear().getHeading() == Heading.WEST){
//                        turnLeft(board.getPlayer(i));
//                    }else{
//                        turnRight(board.getPlayer(i));
//                    }*/
//                    board.getPlayer(i).getSpace().getGear().doAction(this,board.getPlayer(i).getSpace());
//                }
//            }
//            if(board.getPlayer(i).getSpace().getCheckpoint() != null){
////                if(board.getPlayer(i).getSpace().getCheckpoint().getNumber()>board.getPlayer(i).getCheckpoints() && board.getPlayer(i).getSpace().getCheckpoint().getNumber()<=board.getPlayer(i).getCheckpoints()+1){
////                    board.getPlayer(i).setCheckpoints(board.getPlayer(i).getSpace().getCheckpoint().getNumber());
////                    System.out.println("player: " + i + "have checkpoints: " + board.getPlayer(i).getCheckpoints());
////                    if(board.getPlayer(i).getCheckpoints()>= board.getNumOfCheckpoints()){
////                        System.out.println("Player have all checkpoints"); //TODO: Player have satisfied win condition. Need to stop game.
////                    }
////                }
//                board.getPlayer(i).getSpace().getCheckpoint().doAction(this, board.getPlayer(i).getSpace());
//            }
//        }


//        for (int i = 0; i < board.width; i++) {
//            for (int j = 0; j < board.height; j++) {
//                List<FieldAction> actions = board.getSpace(i,j).getActions();
//
//                for (FieldAction action:
//                     actions) {
//                    action.doAction(this, board.getSpace(i,j));
//                }
//
//            }
//        }

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);

            List<FieldAction> actions = player.getSpace().getActions();
            for (FieldAction action:
                     actions) {
                action.doAction(this, player.getSpace());
            }


        }




    }

    //TODO: needs change as conveyor dont function like that in the rules.
    /**
     * Moves the player in the direction of the conveyor.
     * @param player The player that is standing on the conveyor.
     */
//    public void moveByConveyor(Player player){
//        board.getNeighbour(player.getSpace(), player.getSpace().getConveyor().getHeading()).setPlayer(player);
//        if(player.getSpace().getConveyor() != null){
//            moveByConveyor(player);
//        }
//    }


    // XXX: V2
    /**
     * Calls the appropriate method for the command.
     * @param player The current player.
     * @param command The command that is executed.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case  MOVE3_FORWARD:
                    this.move3Forward(player);
                    break;
                case OPTION_LEFT_RIGHT,SANDBOX_ROUTINE,WEASEL_ROUTINE:
                    if(command.isInteractive()){ // If the card that is being activated require player input change phase.
                        board.setPhase(Phase.PLAYER_INTERACTION);
                    }
                    break;
                case SPEED_ROUTINE:
                    this.speedRoutine(player);
                    break;
                case U_TURN:
                    this.UTurn(player);
                    break;
                case SPAM_CARD:
                    this.spamCard(player);
                    break;
                case TROJAN_HORSE:
                    this.trojanHorse(player);
                    break;
                case Backwards:
                    this.turnRight(player);
                    this.turnRight(player);
                    this.moveForward(player);
                    this.turnRight(player);
                    this.turnRight(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    public void spamCard(@NotNull Player player) {
        if(player != null && player.board == board) {
            int steps = board.getStep();
            CommandCard card = generateRandomCommandCard();
            player.set(steps,card);
        }
    }
    public void trojanHorse(@NotNull Player player) {
        spamCard(player);
        spamCard(player);
    }

    public void speedRoutine(@NotNull Player player) {
        move3Forward(player);
    }

    public void move3Forward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
        moveForward(player);

    }

    /**
     *  U-TURN
     * Turn your robot 180 degrees
     * so it faces the opposite direction. The robot remains in its
     * current space.
     *
     */

    public void UTurn(@NotNull Player player) {
        if (player != null && player.board == board) {
            Heading currentHeading = player.getHeading();
            switch (currentHeading) {
                case NORTH:
                    player.setHeading(Heading.SOUTH);
                    break;
                case EAST:
                    player.setHeading(Heading.WEST);
                    break;
                case SOUTH:
                    player.setHeading(Heading.NORTH);
                    break;
                case WEST:
                    player.setHeading(Heading.EAST);
                    break;
            }

        }

    }

    // TODO: V2
    /**
     * Moves the player 1 space forward.
     * @param player The player that need to move.
     */
    public void moveForward(@NotNull Player player) {
        Space space = player.getSpace();
        if (player != null && player.board == board && space != null) {
            Heading heading = player.getHeading();
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                // XXX note that this removes an other player from the space, when there
                //     is another player on the target. Eventually, this needs to be
                //     implemented in a way so that other players are pushed away!
                if(target.getPlayer() != null){
                    pushPlayer(heading, target);
                }
                if(target.getPlayer() == null && !isBlockedByWall(heading, space)){
                    target.setPlayer(player);
                }

            }
        }
    }

    /**
     *
     * @param player Player that is moved.
     * @param space Space that is move from.
     * @param heading Direction that is moved to.
     */
    public void moveToSpace(Player player, Space space, Heading heading){
        Space target = board.getNeighbour(space, heading);

        if(target.getPlayer() != null){
            pushPlayer(heading, board.getNeighbour(space, heading));
        }
        if(target.getPlayer() == null && !isBlockedByWall(heading, space)){
            target.setPlayer(player);
        }
    }

    /**
     * Moves robots in front recursively.
     * @param heading The heading of the original player that pushes.
     * @param space The space that is moved to.
     */
    public void pushPlayer(Heading heading, Space space){
        if(board.getNeighbour(space, heading).getPlayer() != null){
            pushPlayer(heading, board.getNeighbour(space, heading));
        }
        if(board.getNeighbour(space, heading).getPlayer() == null && !isBlockedByWall(heading, space)){
            board.getNeighbour(space, heading).setPlayer(space.getPlayer());
        }
    }

    /**
     * CHecks if the player can move to space or is blocked by walls.
     * @param heading The player heading.
     * @param space The players starting space.
     * @return Whether the path is blocked or not.
     */
    public boolean isBlockedByWall(Heading heading, Space space){
        if(space.getWalls().contains((heading))){
            System.out.println("Wall it start space");
            return true;
        }
        if(board.getNeighbour(space, heading).getWalls().contains(heading.next().next())){
            System.out.println("Wall at target");
            return true;
        }
        return false;
    }

    // TODO: V2
    /**
     * Moves the player 2 space forward.
     * @param player The player that need to move.
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    // TODO: V2
    /**
     * Rotates the player clockwise.
     * @param player The player that need to move.
     */
    public void turnRight(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next());
        }
    }

    // TODO: V2
    /**
     * Rotates the player anti-clockwise.
     * @param player The player that need to move.
     */
    public void turnLeft(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * Moves card from one space to another if possible.
     * @param source Card space that is moved from.
     * @param target Card space that is moved to.
     * @return Returns true if card moved successfully.
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

    /**
     * Execute the command chosen by player and continue program.
     * @param command Command to be executed.
     */
    public void executeCommandOptionAndContinue(Command command){
        Player currentPlayer = board.getCurrentPlayer();

        if (board.getPhase() == Phase.PLAYER_INTERACTION && currentPlayer != null) {
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer, command);

            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {
                int step = board.getStep();
                step++;
                if (step < Player.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                } else {
                    startProgrammingPhase();
                }
            }
        }
        continuePrograms();
    }

    public void playerFinishedProgramming() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.WAITING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        Client.playerFinishedProgramming(playerNum);
    }
}
