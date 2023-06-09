package dk.dtu.compute.se.pisd.roborally.webApplication.api.controller;

//import dk.dtu.compute.se.pisd.roborally.controller.MultiPlayerController;
import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.AllPlayersTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
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

//    public WebService webService;

//    @Autowired
//    public WebController(WebService webService) {
//        this.webService = webService;
//    }
//
    @GetMapping("/map")
    public String getMapName(){
        String name = webService.getMapName();
        System.out.println("get map name method webcontroller");
        return name;
    }
//
//    /*@GetMapping("/player")
//    public ResponseEntity<List<Product>> getProduct()
//    {
//        List<Product> products = productService.findAll();
//        return ResponseEntity.ok().body(products);
//    }*/
//
//    @PostMapping("/map")
//    public ResponseEntity<String > setMapName(@RequestBody String name) {
//        boolean added = webService.setMapName(name);
//        if(added)
//            return ResponseEntity.ok().body("added");
//        else
//            return ResponseEntity.internalServerError().body("not added");
//
//    }
//
    @GetMapping("/perm")
    public int permToJoin(){
        int player = webService.permToJoin();
        return player;
    }

    @PostMapping("/addPlayer")
    public ResponseEntity<String > addPlayer(@RequestBody String player) {

//        System.out.println("recieved String addPlayers: " + player);
//        Gson gson = new Gson();
//
//        PlayerTemplate template = gson.fromJson(player, PlayerTemplate.class);
//
//        template
//
//
//        for (PlayerTemplate playerTemplate: template.players) {
//            Space space = board.getSpace(playerTemplate.x, playerTemplate.y);
//            if(space != null){
//                Player player = new Player(board, playerTemplate.color, playerTemplate.playerName, playerTemplate.program, playerTemplate.cards);
//                player.setHeading(playerTemplate.playerHeading);
//                player.setCheckpoints(playerTemplate.checkpoints);
//                board.addPlayer(player);
//                space.setPlayer(player);
//            }
//        }
        boolean added = webService.addPlayer(player);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    @GetMapping("/getAllPlayer")
    public ResponseEntity<String> getAllPlayers(){
        String players = webService.findAllPlayers();
        return ResponseEntity.ok().body(players);
    }

    @GetMapping("/currPlayer")
    public ResponseEntity<String> getCurrentPlayer(){
        String currPlayer = webService.getCurrentPlayer();
        System.out.println("map: " + currPlayer);
        return ResponseEntity.ok().body(currPlayer);
    }

    @GetMapping("/phase")
    public ResponseEntity<Phase> getPhase(){
        Phase phase = webService.getPhase();
        return ResponseEntity.ok().body(phase);
    }

    @GetMapping("/maxPlayers")
    public int getMaxPlayers(){
        int player = webService.getMaxPlayers();
        return player;
    }

    @GetMapping("/amountOfPlayers")
    public int getAmountOfPlayers(){
        int player = webService.getAmountOfPlayers();
        return player;
    }

    @GetMapping("/currentPlayerIndex")
    public int getCurrentPlayerIndex(){
        int player = webService.getCurrentPlayerIndex();
        return player;
    }

    @PostMapping("/ready")
    public ResponseEntity<String > addPlayer(@RequestBody int playerNum) {

        boolean added = webService.playersReady(playerNum);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

}
