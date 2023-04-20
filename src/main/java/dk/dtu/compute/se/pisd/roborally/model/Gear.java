package dk.dtu.compute.se.pisd.roborally.model;

/**
 * Gears rotate the player 90 degree.
 */
public class Gear {
    private Heading heading;//TODO: Need an enum with rotation instead of heading.

    public Gear(Heading heading){
        this.heading = heading;
    }

    /**
     * If heading is WEST the player is rotated anti-clockwise.
     * If heading is EAST the player is rotated clockwise.
     * @return The rotation direction.
     */
    public Heading getHeading(){
        return heading;
    }
}
