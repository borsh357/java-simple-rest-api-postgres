package learnjava.restapi.controllers;

import learnjava.restapi.PostgreSQLJDBC;
import learnjava.restapi.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UsersController {
    private static List<Map<String, String>> users = new ArrayList<>() {{ }};
    private int count = getCount();

    public UsersController() throws SQLException {
    }

    private int getCount() throws SQLException {
        int count = 0;
        Connection jdbc = null;
        Statement statement = null;

        try {
            jdbc = PostgreSQLJDBC.connect();
            statement = jdbc.createStatement();
            ResultSet res = statement.executeQuery("Select count(*) as count FROM users");

            while (res.next()) {
                count = res.getInt("count");
            }
            return count;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        statement.close();
        jdbc.close();
        System.out.println(count);
        return count;
    }

    private void getDB() throws SQLException {
        users = new ArrayList<>() {{ }};
        Connection jdbc = null;
        Statement statement = null;

        try {
            jdbc = PostgreSQLJDBC.connect();
            statement = jdbc.createStatement();
            ResultSet res = statement.executeQuery("Select * FROM users");


            while (res.next()) {
                String userid = res.getString("id");
                String username = res.getString("name");
                users.add(new HashMap<>() {{
                    put("id", userid);
                    put("name", username);
                }});
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        statement.close();
        jdbc.close();
    }

    @GetMapping
    public List<Map<String, String>> listOfUsers() throws SQLException {
        getDB();
        return users;
    }

    @GetMapping("{id}")
    public Map<String, String> getUser(@PathVariable String id) throws SQLException {
        getDB();
        return findUser(id);
    }

    private Map<String, String> findUser(String id) {
        return users.stream()
                .filter(users -> users.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public Map<String, String> postUser(@RequestBody Map<String, String> user) throws SQLException {
        getDB();
        count = getCount();
        count++;
        user.put("id", String.valueOf(count));
        users.add(user);

        try {
            Connection jdbc = PostgreSQLJDBC.connect();
            PreparedStatement st = jdbc.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");

            st.setString(1, user.get("id"));
            st.setString(2, user.get("name"));
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
            return user;
    }

    @PutMapping("{id}")
    public Map<String, String> updateUser(@PathVariable String id, @RequestBody Map<String, String> user) throws SQLException {
        getDB();
        Map<String, String> userOnServer = findUser(id);

        userOnServer.putAll(user);
        userOnServer.put("id", id);

        try {
            Connection jdbc = PostgreSQLJDBC.connect();
            PreparedStatement st = jdbc.prepareStatement("UPDATE users SET name = ? WHERE id = ? ");

            st.setString(1, userOnServer.get("name"));
            st.setString(2, userOnServer.get("id"));
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userOnServer;
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable String id) throws SQLException {
        getDB();
        Map<String, String> user = getUser(id);
        users.remove(user);

        try {
            Connection jdbc = PostgreSQLJDBC.connect();
            PreparedStatement st = jdbc.prepareStatement("DELETE FROM users WHERE id = ? ");

            st.setString(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
