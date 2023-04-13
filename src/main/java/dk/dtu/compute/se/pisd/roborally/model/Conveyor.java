package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

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
