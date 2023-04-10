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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.
    /**
     * Moves the player 1 space in the heading of the player.
     */
    FORWARD("Fwd"),
    /**
     * Rotates the player clockwise.
     */
    RIGHT("Turn Right"),
    /**
     * Rotates the player anti-clockwise.
     */
    LEFT("Turn Left"),
    /**
     * Moves the player 2 spaces in the heading of the player.
     */
    FAST_FORWARD("Fast Fwd"),

    // XXX Assignment P3
    /**
     * This command ask the player if they will turn left or right.
     */
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT);

    final public String displayName;

    // XXX Assignment P3
    // Command(String displayName) {
    //     this.displayName = displayName;
    // }
    //
    // replaced by the code below:

    final private List<Command> options;

    /**
     * Creates commands.
     * @param displayName What's written on the card.
     * @param options can be left blank, or a list of the choices the player can choose.
     */
    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    /**
     * Returns if the command needs player input or not
     * @return Returns true if command need player input.
     * Returns false if command doesn't need input.
     */
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     *
     * @return Returns the options for the commands.
     */
    public List<Command> getOptions() {
        return options;
    }

}
