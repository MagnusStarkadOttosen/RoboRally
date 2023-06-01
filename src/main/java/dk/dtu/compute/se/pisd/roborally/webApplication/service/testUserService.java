package dk.dtu.compute.se.pisd.roborally.webApplication.service;

import dk.dtu.compute.se.pisd.roborally.webApplication.api.model.testUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class testUserService {

    private List<testUser> userList;

    public testUserService(){
        userList = new ArrayList<>();

        testUser user1 = new testUser(1, "test1", 5);
        testUser user2 = new testUser(2, "test2", 4);
        testUser user3 = new testUser(3, "test3", 3);
        testUser user4 = new testUser(4, "test4", 2);
        testUser user5 = new testUser(5, "test5", 1);

        userList.addAll(Arrays.asList(user1,user2,user3,user4,user5));
    }

    public Optional<testUser> getUser(Integer id) {
        Optional optional = Optional.empty();
        for (testUser user: userList) {
            if(id == user.getId()){
                optional = Optional.of(user);
                return optional;
            }
        }
        return optional;
    }
}
