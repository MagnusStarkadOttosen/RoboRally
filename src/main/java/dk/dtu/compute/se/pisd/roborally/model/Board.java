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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.webApplication.Client;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Board extends Subject {

    public final int width;

    public final int height;

    public final String boardName;

    private Integer gameId;

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    /**
     *
     * @return The number of checkpoints on the board
     */
    public int getNumOfCheckpoints() {
        return numOfCheckpoints;
    }

    /**
     * Sets the number of checkpoints on the board.
     * @param numOfCheckpoints
     */
    public void setNumOfCheckpoints(int numOfCheckpoints) {
        this.numOfCheckpoints = numOfCheckpoints;
    }

    private int numOfCheckpoints;

    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    /**
     * Returns Game ID.
     * @return gameId
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * Set a id to the game. Not used right now.
     * @param gameId
     */
    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * Returns the space located at (x,y).
     * @param x
     * @param y
     * @return space
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * Returns number of players.
     * @return number of players.
     */
    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * Adds a player to the game.
     * @param player
     */
    public void addPlayer(@NotNull Player player) {
        boolean dupName = false;
        for (Player p:players) {
            if(p.getName().equals(player.getName())){
                dupName = true;
            }
        }
        if (player.board == this && !players.contains(player) && !dupName) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * Returns the player numbered i.
     * @param i index of the player
     * @return player
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * Returns the current player.
     * @return player
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * Sets the current player.
     * @param player The player whose turn it is.
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * Returns phase.
     * @return phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Sets phase.
     * @param phase
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * Returns the current step.
     * @return int
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets the step.
     * @param step
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * Returns if the programs is stepping through or not.
     * @return stepmode
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * Set if the programs is stepping through or not.
     * @param stepMode
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * Return the index of the player.
     * @param player
     * @return index
     */
    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    /**
     * Returns the current status of the game.
     * @return Phase + player + step
     */
    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
    }

    /**
     *
     * @return list of all players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    public void addAllPlayers(List<Player> players){
        this.players.addAll(players);
        notifyChange();
    }

    public void removeAllPlayers() {
        players.removeAll(players);
    }

}
