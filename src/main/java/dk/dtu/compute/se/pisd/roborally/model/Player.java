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
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;

    private String name;
    private String color;

    private Space space;
    private Heading heading = SOUTH;

    private CommandCardField[] program;
    private CommandCardField[] cards;

    private int checkpoints;

    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;
        checkpoints = 0;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    public Player(@NotNull Board board, String color, @NotNull String name, List<CommandCard> programList, List<CommandCard> cardList) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;
        checkpoints = 0;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
            program[i].setCard(programList.get(i));
        }
        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
            cards[i].setCard(cardList.get(i));
        }
    }

    /**
     * Returns players name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the players name.
     * @param name
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Returns the players color.
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the players color.
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * Returns the space the player is located.
     * @return space
     */
    public Space getSpace() {
        return space;
    }

    /**
     * Sets the space the player is located.
     * @param space
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * Returns the direction the player is pointing.
     * @return Heading
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sets the direction the player is pointing.
     * @param heading
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Returns the field with index i that the player programs.
     * @param i
     * @return CommandCardField
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * Returns the field with index i that holds the players cards.
     * @param i
     * @return CommandCardField
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public CommandCardField[] getCardsList(){
        return cards;
    }

    public CommandCardField[] getProgramList(){
        return program;
    }

    /**
     * Sets the variable checkpoint to the highest checkpoint the player have reached.
     * @param checkpoints The number on the checkpoint.
     */
    public void setCheckpoints(int checkpoints){
        this.checkpoints = checkpoints;
    }

    /**
     *
     * @return The highest checkpoint the player have reached.
     */
    public int getCheckpoints(){
        return checkpoints;
    }
    public void set(int s,CommandCard card){
        program[s].setCard(card);
    }

    public void setCards(List<CommandCard> cards){

        for (int i = 0; i < NO_CARDS; i++) {
            this.cards[i].setCard(cards.get(i));
        }


//        System.out.println("cards: " + this.cards[0].getCard().getName());

//        this.cards = (CommandCardField[]) cards.toArray();
    }

    public void setProgram(List<CommandCard> program){
        for (int i = 0; i < NO_REGISTERS; i++) {
            this.program[i].setCard(program.get(i));
        }
//        this.program = (CommandCardField[]) program.toArray();
    }
}
