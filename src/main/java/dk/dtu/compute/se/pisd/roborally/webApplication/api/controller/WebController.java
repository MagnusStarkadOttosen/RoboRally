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
    public ResponseEntity<String > addReady(@RequestBody String programList) {

        boolean added = webService.playersReady(programList);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    @GetMapping("/getAllData")
    public ResponseEntity<String> getAllData(){
        String players = webService.findAllData();
        return ResponseEntity.ok().body(players);
    }


    @GetMapping("/isProgFinished")
    public boolean isProgFinished(){
        boolean player = webService.isProgFinished();
        return player;
    }

    @GetMapping("/isMovingFinished")
    public boolean isMovingFinished(){
        boolean player = webService.isMovingFinished();
        return player;
    }

    @PostMapping("/updatePlayer")
    public ResponseEntity<String > updatePlayer(@RequestBody String player) {

        boolean added = webService.updatePlayer(player);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

}
