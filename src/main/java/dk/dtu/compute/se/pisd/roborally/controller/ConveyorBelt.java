package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
//TODO: Conveyors could have different strength that moves the player different amounts.
/**
 * Conveyors move the player in an direction.
 */
public class ConveyorBelt extends FieldAction {
    private Heading heading;

    public ConveyorBelt(Heading heading){
        this.heading = heading;
    }

    /**
     * Returns the direction the conveyor is pointing.
     * @return Heading
     */
    public Heading getHeading() {
        return heading;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        if(space.getPlayer() == null){
            return false;
        }

        gameController.moveToSpace(space.getPlayer(), space, heading);

        return true;
    }
}
