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
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SaveTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.*;

import java.awt.*;
import java.io.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {

    //TODO: The variable numOfCheckpoints in boards should be set be the loadBoard method.

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";
    private static final String FALLBACKSAVE = "fallbacksave";
    private static final String SAVEFOLDER = "saves";


    /**
     * This loads the game from a json file.
     * @param boardname Name of the file to load
     * @return The board that is loaded from the file.
     */
    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + boardname + "." + JSON_EXT);
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            return new Board(8,8);
        }

		// In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

		Board result;
		// FileReader fileReader = null;
        JsonReader reader = null;
		try {
			// fileReader = new FileReader(filename);
			reader = gson.newJsonReader(new InputStreamReader(inputStream));
			BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

			result = new Board(template.width, template.height, boardname);
			for (SpaceTemplate spaceTemplate: template.spaces) {
			    Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
			    if (space != null) {
                    space.getActions().addAll(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }
            /*for (PlayerTemplate playerTemplate: template.players) {
                Space space = result.getSpace(playerTemplate.x, playerTemplate.y);
                if(space != null){
                    Player player = new Player(result, playerTemplate.color, playerTemplate.playerName);
                    player.setHeading(playerTemplate.playerHeading);
                    result.addPlayer(player);
                    space.setPlayer(player);
                }
            }*/
			reader.close();
			return result;
		} catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {}
            }
            if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e2) {}
			}
		}
		return null;
    }

    /**
     * This saves the board to a json file.
     * @param board The board that is going to be saved.
     * @param name The name of the save.
     */
    public static void saveBoard(Board board, String name) {

        SaveTemplate template = new SaveTemplate();
        template.mapName = board.boardName;
        template.phase = board.getPhase();
        template.step = board.getStep();


        template.currentPlayer = board.getPlayers().indexOf(board.getCurrentPlayer());

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

            //playerTemplate.cards = space.getPlayer().getCardsList();
            //playerTemplate.program = space.getPlayer().getProgramList();
            template.players.add(playerTemplate);
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        // TODO: this is not very defensive, and will result in a NullPointerException
        //       when the folder "resources" does not exist! But, it does not need
        //       the file "simpleCards.json" to exist!
        String filename =
                classLoader.getResource(SAVEFOLDER).getPath() + "/" + name + "." + JSON_EXT;

        System.out.println("Filename: " + filename);

        // In simple cases, we can create a Gson object with new:
        //
        //   Gson gson = new Gson();
        //
        // But, if you need to configure it, it is better to create it from
        // a builder (here, we want to configure the JSON serialisation with
        // a pretty printer):
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
                setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(template, template.getClass(), writer);
            writer.close();
        } catch (IOException e1) {
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException e2) {}
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {}
            }
        }

    }

    //TODO: Probably a better way to do it.
    /**
     *
     * @param saveGameName Name of the file to test.
     * @return whether the file is present or not
     */
    public static boolean filePresent(String saveGameName){
        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(SAVEFOLDER + "/" + saveGameName + "." + JSON_EXT);
        if (inputStream == null) {
            return false;
        }else{
            return true;
        }
    }

    public static Board loadSavedGame(String saveGameName){
        if (saveGameName == null) {
            saveGameName = FALLBACKSAVE;
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(SAVEFOLDER + "/" + saveGameName + "." + JSON_EXT);
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            return new Board(8,8);
        }

        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            SaveTemplate template = gson.fromJson(reader, SaveTemplate.class);

            result = loadBoard(template.mapName);
            result.setPhase(template.phase);
            result.setStep(template.step);

            for (PlayerTemplate playerTemplate: template.players) {
                Space space = result.getSpace(playerTemplate.x, playerTemplate.y);
                if(space != null){
                    Player player = new Player(result, playerTemplate.color, playerTemplate.playerName, playerTemplate.program, playerTemplate.cards);
                    player.setHeading(playerTemplate.playerHeading);
                    player.setCheckpoints(playerTemplate.checkpoints);
                    result.addPlayer(player);
                    space.setPlayer(player);
                }
            }
            result.setCurrentPlayer(result.getPlayer(template.currentPlayer));

            reader.close();
            return result;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {}
            }
        }
        return null;
    }

//    public static void convertToJson(Board board){
//
//        SaveTemplate template = new SaveTemplate();
//        template.mapName = board.boardName;
//        template.phase = board.getPhase();
//        template.step = board.getStep();
//
//        template.currentPlayer = board.getPlayers().indexOf(board.getCurrentPlayer());
//
//        for (Player player: board.getPlayers()) {
//            Space space = player.getSpace();
//            PlayerTemplate playerTemplate = new PlayerTemplate();
//            playerTemplate.x = space.x;
//            playerTemplate.y = space.y;
//            playerTemplate.playerName = space.getPlayer().getName();
//            playerTemplate.color = space.getPlayer().getColor();
//            playerTemplate.playerHeading = space.getPlayer().getHeading();
//            playerTemplate.checkpoints = space.getPlayer().getCheckpoints();
//
//            for (CommandCardField card: space.getPlayer().getCardsList()) {
//                playerTemplate.cards.add(card.getCard());
//            }
//            for (CommandCardField card: space.getPlayer().getProgramList()) {
//                playerTemplate.program.add(card.getCard());
//            }
//
//            template.players.add(playerTemplate);
//        }
//
////        ClassLoader classLoader = LoadBoard.class.getClassLoader();
////        // TODO: this is not very defensive, and will result in a NullPointerException
////        //       when the folder "resources" does not exist! But, it does not need
////        //       the file "simpleCards.json" to exist!
////        String filename =
////                classLoader.getResource(SAVEFOLDER).getPath() + "/" + name + "." + JSON_EXT;
////
////        System.out.println("Filename: " + filename);
//
//        // In simple cases, we can create a Gson object with new:
//        //
//        //   Gson gson = new Gson();
//        //
//        // But, if you need to configure it, it is better to create it from
//        // a builder (here, we want to configure the JSON serialisation with
//        // a pretty printer):
//        GsonBuilder simpleBuilder = new GsonBuilder().
//                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
//                setPrettyPrinting();
//        Gson gson = simpleBuilder.create();
//
//        FileWriter fileWriter = null;
//        JsonWriter writer = null;
//        try {
//            fileWriter = new FileWriter(filename);
//            writer = gson.newJsonWriter(fileWriter);
//            gson.toJson(template, template.getClass(), writer);
//            writer.close();
//        } catch (IOException e1) {
//            if (writer != null) {
//                try {
//                    writer.close();
//                    fileWriter = null;
//                } catch (IOException e2) {}
//            }
//            if (fileWriter != null) {
//                try {
//                    fileWriter.close();
//                } catch (IOException e2) {}
//            }
//        }
//
//
//
//
//    }
}

//    Gson temp = new Gson();
//            System.out.println("json: " + temp.toJson(playerTemplate));
//