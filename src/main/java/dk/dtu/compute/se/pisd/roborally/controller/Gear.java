package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Rotation;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Gears rotate the player 90 degree.
 */
public class Gear extends FieldAction{
    private Rotation rotation;

    public Gear(Rotation rotation){
        this.rotation = rotation;
    }

    /**
     * If heading is WEST the player is rotated anti-clockwise.
     * If heading is EAST the player is rotated clockwise.
     * @return The rotation direction.
     */
    public Rotation getRotation(){
        return rotation;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        if(space.getPlayer() == null){
            return false;
        }

        if(rotation == Rotation.Clockwise){
            space.getPlayer().setHeading(space.getPlayer().getHeading().next());
        }else{
            space.getPlayer().setHeading(space.getPlayer().getHeading().prev());
        }

        return true;
    }
}
