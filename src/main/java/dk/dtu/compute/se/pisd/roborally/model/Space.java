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
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.Gear;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {

    public final Board board;

    public final int x;
    public final int y;

    private Player player;

    private List<Heading> walls;

//    private Conveyor conveyor;

//    private Gear gear;
//    private Checkpoint checkpoint;

    private List<FieldAction> actions;

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
        walls = new ArrayList<>();
//        conveyor = null;
//        gear = null;
//        checkpoint = null;
        actions = new ArrayList<>();
    }

    /**
     * Returns player if any.
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the position of the player to the space.
     * @param player The player that is placed.
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     * The player can call this if they change something that changes how it is shown on the board.
     */
    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

    /**
     * Adds a wall to the space.
     * @param heading The direction the wall is blocking.
     */
    public void addWall(Heading heading){
        walls.add(heading);
    }

    /**
     * Returns a list of the space's walls.
     * @return walls
     */
    public List<Heading> getWalls(){
        return walls;
    }

    /**
     * Adds a conveyor to the space.
     * @param heading The direction the wall is blocking.
     */
    public void addConveyor(Heading heading){
//        conveyor = new Conveyor(heading);
        actions.add(new ConveyorBelt(heading));
    }

    /**
     * Returns the space's conveyors. If any.
     * @return conveyor
     */
//    public Conveyor getConveyor(){
//        return conveyor;
//    }

    /**
     * Adds a gear to the space.
     * @param heading If WEST rotate player anti-clockwise, if EAST rotate clockwise.
     */
    public void addGear(Rotation rotation){
//        gear = new Gear(rotation);
        actions.add(new Gear(rotation));
    }

    /**
     * Returns the space's gear. If any.
     * @return conveyor
     */
//    public Gear getGear(){
//        return gear;
//    }

    /**
     * Adds a checkpoint with a number to the space.
     * @param number
     */
    public void setCheckpoint(int number){
//        checkpoint = new Checkpoint(number);
        actions.add(new Checkpoint(number));
    }

    /**
     *
     * @return The checkpoint if any.
     */
//    public Checkpoint getCheckpoint(){
//        return checkpoint;
//    }

    /**
     *
     * @return List of all actions the space can do.
     */
    public List<FieldAction> getActions() {
        return actions;
    }
}
