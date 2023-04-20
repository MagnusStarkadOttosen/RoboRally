package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;
//TODO: Conveyors could have different strength that moves the player different amounts.
/**
 * Conveyors move the player in an direction.
 */
public class Conveyor {
    private Heading heading;

    public Conveyor(Heading heading){
        this.heading = heading;
    }

    /**
     * Returns the direction the conveyor is pointing.
     * @return Heading
     */
    public Heading getHeading() {
        return heading;
    }
}
