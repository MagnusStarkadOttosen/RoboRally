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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.Gear;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

//TODO: the current icons should be changed to images
/**
 * What is drawn on each space.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 40; // 75; was 60 reduced do to screen size.
    final public static int SPACE_WIDTH = 40; // 75; was 60 reduced do to screen size.

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;
        String path;
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);
        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);
        path = getClass().getResource("/image/img.png").toExternalForm();
        this.setStyle("-fx-background-image: url(/image/img.png),url(" + path + "); -fx-background-repeat: no-repeat; -fx-background-size: "+SpaceView.SPACE_HEIGHT+"; -fx-background-position:center center;");

//         updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * Draws the player icons.
     */
    private void updatePlayer() {
        Player player = space.getPlayer();

        if (player != null) {
            String path = Objects.requireNonNull(this.getClass().getResource("/image/")).toString();
            String image = path + player.getColor().toLowerCase() + "Robot.png";
            String repeat = "no-repeat";
            StackPane stackPane = new StackPane();

            String playerColor = player.getColor();

            switch (playerColor) {
                case "red", "green","blue", "orange", "grey","magenta":
                    stackPane.setStyle("-fx-background-image: url(" + image + "); -fx-background-size: " + SPACE_HEIGHT * 0.8 + "; -fx-background-repeat: " + repeat + ";");
                    break;
            }
            this.getChildren().add(stackPane);
// old style for players
//            Polygon arrow = new Polygon(0.0, 0.0,
//                    10.0, 20.0,
//                    20.0, 0.0 );
//            try {
//                arrow.setFill(Color.valueOf(player.getColor()));
//            } catch (Exception e) {
//                arrow.setFill(Color.MEDIUMPURPLE);
//            }
//
//            arrow.setRotate((90*player.getHeading().ordinal())%360);
//            this.getChildren().add(arrow);


        }
    }
    /**
     * Updates space with everything on it.
     * @param subject
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();
            drawActionField();
            drawWalls();
            updatePlayer();
        }
    }

    /**
     * Draws the wall icons.
     */
    private void drawWalls(){

        List<Heading> walls = space.getWalls();
        for (Heading wall : walls) {
            String path = null;
            switch(wall){
                case EAST -> path = Objects.requireNonNull(this.getClass().getResource("/image/wallEast.png")).toExternalForm();
                case NORTH -> path = Objects.requireNonNull(this.getClass().getResource("/image/wallNorth.png")).toExternalForm();
                case WEST -> path = Objects.requireNonNull(this.getClass().getResource("/image/wallWest.png")).toExternalForm();
                case SOUTH -> path = Objects.requireNonNull(this.getClass().getResource("/image/wallSouth.png")).toExternalForm();
            }

            this.setStyle("-fx-background-image:  url(/image/img.png),url("+path+"); -fx-background-repeat: no-repeat; -fx-background-size: "+SpaceView.SPACE_HEIGHT+"; -fx-background-position:center center;");

//old style walls
//            Pane pane = new Pane();
//            Line line;
//            switch(wall){
//                case EAST -> line = new Line(SPACE_WIDTH-2,2,SPACE_WIDTH-2,SPACE_HEIGHT-2);
//                case WEST -> line = new Line(2,2,2,SPACE_HEIGHT-2);
//                case NORTH -> line = new Line(2,2,SPACE_WIDTH-2,2);
//                default -> line = new Line(2,SPACE_HEIGHT-2,SPACE_WIDTH-2,SPACE_HEIGHT-2);
//            }
//            line.setStrokeWidth(5);
//            line.setStroke(Color.YELLOW);
//            pane.getChildren().add(line);
//            this.getChildren().add(pane);
        }
    }

    /**
     * Draws the action field on the space. Action field include conveyor, gear and checkpoints.
     */
    private void drawActionField(){


        List<FieldAction> actions = space.getActions();

        for (FieldAction action:
             actions) {
            if (action.getClass() == ConveyorBelt.class) {
                Polygon arrow = new Polygon(0.0, 0.0,
                        SPACE_WIDTH/2-3, SPACE_HEIGHT-9,
                        SPACE_HEIGHT-9,0.0 );
                try {
                    arrow.setFill(Color.BLUEVIOLET);
                } catch (Exception e) {
                    arrow.setFill(Color.MEDIUMPURPLE);
                }

                arrow.setRotate((90* ((ConveyorBelt) action).getHeading().ordinal())%360);
                this.getChildren().add(arrow);
            }
            if (action.getClass() == Gear.class) {
                String path;
//                Circle circle = new Circle(SPACE_WIDTH/2-5);
//                Text text = new Text();
//                circle.setStrokeWidth(5);
                if(((Gear) action).getRotation() == Rotation.AntiClockwise){
                    path = getClass().getResource("/image/counterclockwise.png").toExternalForm();
//                    circle.setStroke(Color.GRAY);
//                    text.setText("Anti-\nClockwise");
                    this.setStyle("-fx-background-image: url(/image/img.png),url(" + path + "); -fx-background-repeat: no-repeat; -fx-background-size: "+SpaceView.SPACE_HEIGHT+"; -fx-background-position:center center;");


                }else{
                    path = getClass().getResource("/image/clockwise.png").toExternalForm();
                    this.setStyle("-fx-background-image: url(/image/img.png),url(" + path + "); -fx-background-repeat: no-repeat; -fx-background-size: "+SpaceView.SPACE_HEIGHT+"; -fx-background-position:center center;");

                }

            }

// checkpoints
            if (action.getClass() == Checkpoint.class) {
                int number = Checkpoint.getNumber(); // Access static member using class reference
                String path;

                switch (number) {
                    case 1:
                        path = Objects.requireNonNull(this.getClass().getResource("/image/Check2.png")).toExternalForm();
                        break;
                    case 2:
                        path = Objects.requireNonNull(this.getClass().getResource("/image/Check3.png")).toExternalForm();
                        break;
                    case 3:
                        path = Objects.requireNonNull(this.getClass().getResource("/image/Check4.png")).toExternalForm();
                        break;
                    case 4:
                        path = Objects.requireNonNull(this.getClass().getResource("/image/Check5.png")).toExternalForm();
                        break;
                    default:
                        // Handle other cases
                        path = Objects.requireNonNull(this.getClass().getResource("/image/Check1.png")).toExternalForm();
                        break;
                }
                this.setStyle("-fx-background-image: url(/image/img.png),url(" + path+ "); -fx-background-repeat: no-repeat; -fx-background-size: "+SpaceView.SPACE_HEIGHT+"; -fx-background-position:center center;");

            }



// here the old code of the checkpoints
//            if(action.getClass() == Checkpoint.class){
//                Circle circle = new Circle((double) SPACE_WIDTH /2-5);
//                Text text = new Text();
//                circle.setStrokeWidth(5);
//                circle.setStroke(Color.YELLOW);
//                text.setText(String.valueOf(Checkpoint.getNumber()));
//
//                text.setFont(Font.font(10));
//                text.setStroke(Color.ORANGE);
//
//                this.getChildren().add(circle);
//                this.getChildren().add(text);
            }
        }
    }

