package dk.dtu.compute.se.pisd.roborally.webApplication.api.controller;

//import dk.dtu.compute.se.pisd.roborally.controller.MultiPlayerController;
import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.AllPlayersTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.webApplication.api.model.testUser;
import dk.dtu.compute.se.pisd.roborally.webApplication.service.IWebService;
import dk.dtu.compute.se.pisd.roborally.webApplication.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class WebController {

    @Autowired
    private IWebService webService;

    /**
     * Returns the name of the map.
     */
    @GetMapping("/map")
    public String getMapName(){
        String name = webService.getMapName();
        return name;
    }

    /**
     * Returns if there are space on the server. And if there are return the playernumber assigned to the new player.
     * @return
     */
    @GetMapping("/perm")
    public int permToJoin(){
        int player = webService.permToJoin();
        return player;
    }

    /**
     * Add the player to the server.
     * @param player
     * @return
     */
    @PostMapping("/addPlayer")
    public ResponseEntity<String > addPlayer(@RequestBody String player) {

        boolean added = webService.addPlayer(player);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    /**
     * Returns all players.
     * @return
     */
    @GetMapping("/getAllPlayer")
    public ResponseEntity<String> getAllPlayers(){
        String players = webService.findAllPlayers();
        return ResponseEntity.ok().body(players);
    }

    /**
     * Returns the current player.
     * @return
     */
    @GetMapping("/currPlayer")
    public ResponseEntity<String> getCurrentPlayer(){
        String currPlayer = webService.getCurrentPlayer();
        System.out.println("map: " + currPlayer);
        return ResponseEntity.ok().body(currPlayer);
    }

    /**
     * Returns the current phase.
     * @return
     */
    @GetMapping("/phase")
    public ResponseEntity<Phase> getPhase(){
        Phase phase = webService.getPhase();
        return ResponseEntity.ok().body(phase);
    }

    /**
     * Returns how many players are allowed on the server.
     * @return
     */
    @GetMapping("/maxPlayers")
    public int getMaxPlayers(){
        int player = webService.getMaxPlayers();
        return player;
    }

    /**
     * Returns how many players are on the server.
     * @return
     */
    @GetMapping("/amountOfPlayers")
    public int getAmountOfPlayers(){
        int player = webService.getAmountOfPlayers();
        return player;
    }

    /**
     * Returns the index of the current player.
     * @return
     */
    @GetMapping("/currentPlayerIndex")
    public int getCurrentPlayerIndex(){
        int player = webService.getCurrentPlayerIndex();
        return player;
    }

    /**
     * Tells the server that the player is done programming and sends the program.
     * @param programList
     * @return
     */
    @PostMapping("/ready")
    public ResponseEntity<String > addReady(@RequestBody String programList) {

        boolean added = webService.playersReady(programList);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    /**
     * Returns map name, phase, step, currentplayer index and all players.
     * @return
     */
    @GetMapping("/getAllData")
    public ResponseEntity<String> getAllData(){
        String players = webService.findAllData();
        return ResponseEntity.ok().body(players);
    }

    /**
     * Returns if all players are done programming.
     * @return
     */
    @GetMapping("/isProgFinished")
    public boolean isProgFinished(){
        boolean player = webService.isProgFinished();
        return player;
    }

    /**
     * Returns if all players are done moving.
     * @return
     */
    @GetMapping("/isMovingFinished")
    public boolean isMovingFinished(){
        boolean player = webService.isMovingFinished();
        return player;
    }

    /**
     * Tells the server to update the players with new positions.
     * @param player
     * @return
     */
    @PostMapping("/updatePlayer")
    public ResponseEntity<String > updatePlayer(@RequestBody String player) {

        boolean added = webService.updatePlayer(player);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

}
