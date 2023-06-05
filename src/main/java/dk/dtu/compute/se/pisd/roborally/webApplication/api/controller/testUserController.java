package dk.dtu.compute.se.pisd.roborally.webApplication.api.controller;

import dk.dtu.compute.se.pisd.roborally.webApplication.api.model.testUser;
import dk.dtu.compute.se.pisd.roborally.webApplication.service.testUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * For testing
 */
@RestController
public class testUserController {

    private testUserService userService;

    @Autowired
    public testUserController(testUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public testUser getUser(@RequestParam Integer id){
        Optional user = userService.getUser(id);
        if(user.isPresent()){
            return (testUser) user.get();
        }
        return null;
    }
}
