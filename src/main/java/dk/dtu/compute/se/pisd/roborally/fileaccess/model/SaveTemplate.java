package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import java.util.ArrayList;
import java.util.List;

public class SaveTemplate {

    public String mapName;

    public Phase phase;

    public int step;

    public int currentPlayer;

    public List<PlayerTemplate> players = new ArrayList<>();

}
