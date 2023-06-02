package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.Heading;

import java.util.ArrayList;
import java.util.List;

public class PlayerTemplate {

    public String playerName;
    public String color;
    public Heading playerHeading;
    public int checkpoints;
    public List<CommandCard> program = new ArrayList<>();
    public List<CommandCard> cards = new ArrayList<>();

    public int x;
    public int y;
}
