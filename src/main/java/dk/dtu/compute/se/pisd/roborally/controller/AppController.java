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

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.webApplication.*;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Controlles the application.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    private Board board;

    /**
     * Prompts user for amount of players and what map.
     */
    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            ChoiceDialog<String> mapChoice = new ChoiceDialog<>("map1", "map1", "map2", "map3");
            mapChoice.setTitle("Select map");
            mapChoice.setHeaderText("Select map");
            Optional<String> mapResult = mapChoice.showAndWait();

//            System.out.println(mapResult.get());

            board = LoadBoard.loadBoard(mapResult.get());


            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            //board = new Board(8,8);
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }

//            board.getSpace(3,3).addWall(Heading.SOUTH);
//
//            board.getSpace(5,5).addWall(Heading.NORTH);
//            board.getSpace(5,5).addWall(Heading.WEST);
//            board.getSpace(6,6).addConveyor(Heading.WEST);
//            board.getSpace(6,7).addConveyor(Heading.EAST);
//            board.getSpace(6,7).addWall(Heading.NORTH);
//
//            board.getSpace(4,1).addConveyor(Heading.SOUTH);
//            board.getSpace(4,2).addConveyor(Heading.SOUTH);
//            board.getSpace(4,3).addConveyor(Heading.SOUTH);
//
//            board.getSpace(1,3).addGear(Rotation.AntiClockwise);
//            board.getSpace(1,4).addGear(Rotation.Clockwise);
//
//            board.getSpace(1,5).setCheckpoint(1);
//            board.getSpace(1,6).setCheckpoint(2);
//            board.setNumOfCheckpoints(2);

            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);
        }
    }

    //TODO: Should save the players hand.
    /**
     * Saves the map and player position and rotation.
     */
    public void saveGame() {
        // XXX needs to be implemented eventually

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Game");
        dialog.setHeaderText("Input name of save");
        Optional<String> result = dialog.showAndWait();


        LoadBoard.saveBoard(board, result.get());

    }

    //TODO: A better way to choose what file to load.
    /**
     * Loads the game from a file. The player have to input the file name manually.
     */
    public void loadGame() {
        // XXX needs to be implememted eventually
        // for now, we just create a new game
        if (gameController == null) {
//            newGame();


//            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
//            dialog.setTitle("Player number");
//            dialog.setHeaderText("Select number of players");
//            Optional<Integer> result = dialog.showAndWait();

            Optional<String> result;
            do{
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Load Game");
                dialog.setHeaderText("Input file to load");
                result = dialog.showAndWait();
            }while(!LoadBoard.filePresent(result.get()));

            System.out.println("File to load: " + result.get());

            board = LoadBoard.loadBoard(result.get());
            gameController = new GameController(board);
//            int no = result.get();
//            for (int i = 0; i < no; i++) {
//                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
//                board.addPlayer(player);
//                player.setSpace(board.getSpace(i % board.width, i));
//            }

            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);
        }



    }

    //TODO: Temporarily removed the saving part. It should prompt the player.
    /**
     * Stop playing the current game, <strike>giving the user the option to save
     * the game</strike> or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            //saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Prompts user if they want to exit the application. If yes close window.
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     *
     * @return true if gameController have been initialized. False if it hasn't.
     */
    public boolean isGameRunning() {
        return gameController != null;
    }

    /**
     * Does nothing right now.
     * @param subject the subject which changed
     */
    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    public void host(){
        System.out.println("hosting");
        SpringAPIApplication.startSpring();

    }

    public void join(){
        System.out.println("Joining");
    }
}
