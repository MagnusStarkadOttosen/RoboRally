package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * The checkpoint is the goal of the game. The player that collects them all first wins.
 */
public class Checkpoint extends FieldAction {
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

    @Override
    public boolean doAction(GameController gameController, Space space) {
        if(space.getPlayer() == null){
            return false;
        }
        System.out.println(space.getPlayer().getCheckpoints() + " " + number);
        if(space.getPlayer().getCheckpoints() < number && (space.getPlayer().getCheckpoints()+1) >= number){
            space.getPlayer().setCheckpoints(number);
            System.out.println("player: " + space.getPlayer().getName() + "have checkpoints: " + space.getPlayer().getCheckpoints());
        }
        if(space.getPlayer().getCheckpoints() >= gameController.board.getNumOfCheckpoints()){
            System.out.println("Player have all checkpoints"); //TODO: Player have satisfied win condition. Need to stop game.
        }

        return true;
    }
}
