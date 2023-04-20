package dk.dtu.compute.se.pisd.roborally.model;

/**
 * The checkpoint is the goal of the game. The player that collects them all first wins.
 */
public class Checkpoint {
    private int number;

    public Checkpoint(int number){
        this.number = number;
    }

    /**
     *
     * @return The number that is assigned the checkpoint.
     */
    public int getNumber(){
        return number;
    }
}
