package dk.dtu.compute.se.pisd.roborally.webApplication.api.controller;

import dk.dtu.compute.se.pisd.roborally.webApplication.api.model.testUser;
import dk.dtu.compute.se.pisd.roborally.webApplication.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WebController {
    public WebService webService;

    @Autowired
    public WebController(WebService webService) {
        this.webService = webService;
    }

    @GetMapping("/map")
    public String getMap(){
        String name = webService.getMapName();
        return name;
    }

    @PostMapping("/map")
    public ResponseEntity<String > setMapName(@RequestBody String name) {
        System.out.println("test");
        boolean added = webService.setMapName(name);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }
}
