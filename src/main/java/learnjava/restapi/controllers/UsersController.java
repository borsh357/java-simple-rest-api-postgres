package learnjava.restapi.controllers;

import learnjava.restapi.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UsersController {
    private int count = 4;

    public List<Map<String, String>> users = new ArrayList<>() {{
        add(new HashMap<>() {{ put( "id", "1"); put("name", "John"); }});
        add(new HashMap<>() {{ put( "id", "2"); put("name", "Dean"); }});
        add(new HashMap<>() {{ put( "id", "3"); put("name", "Paul"); }});
    }};


    @GetMapping
    public List<Map<String, String>> listOfUsers() {
        return users;
    }

    @GetMapping("{id}")
    public Map<String, String> getUser(@PathVariable String id) {
        return findUser(id);
    }

    private Map<String, String> findUser(String id) {
        return users.stream()
                .filter(users -> users.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public Map<String, String> postUser(@RequestBody Map<String, String> user) {
        user.put("id", String.valueOf(count++));
        users.add(user);
        return user;
    }

    @PutMapping("{id}")
    public Map<String, String> updateUser(@PathVariable String id, @RequestBody Map<String, String> user) {
        Map<String, String> userOnServer = findUser(id);

        userOnServer.putAll(user);
        userOnServer.put("id", id);

        return userOnServer;
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable String id) {
        Map<String, String> user = getUser(id);
        users.remove(user);
    }

}
