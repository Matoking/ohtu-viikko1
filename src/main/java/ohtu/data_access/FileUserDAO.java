/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.data_access;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import ohtu.domain.User;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author matoking
 */
public class FileUserDAO implements UserDao {

    private List<User> users;

    private String fileName;

    public FileUserDAO(String fileName) {
        users = new ArrayList<User>();
        this.fileName = fileName;
        loadUsers();
    }

    private void loadUsers() {
        try {
            JSONObject jsonObj = new JSONObject(readFile(fileName, Charset.defaultCharset()));
            JSONArray jsonUsers = jsonObj.getJSONArray("users");
            
            for (int i=0; i < jsonUsers.length(); i++) {
                String username = jsonUsers.getJSONObject(i).getString("username");
                String password = jsonUsers.getJSONObject(i).getString("password");
                
                users.add(new User(username, password));
                System.out.println("Loaded user " + username);
            }
        } catch (IOException e) {
            System.out.println("Couldn't read.");
        }
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private void saveUsers() {
        JSONObject jsonObj = new JSONObject();

        ArrayList<JSONObject> jsonUsers = new ArrayList<JSONObject>();

        for (User user : users) {
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("username", user.getUsername());
            jsonUser.put("password", user.getPassword());

            jsonUsers.add(jsonUser);
        }

        jsonObj.put("users", jsonUsers);

        writeToFile(jsonObj);
    }
    
    private void writeToFile(JSONObject jsonObj) {
        File file = new File(fileName);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObj.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Couldn't write.");
        }
    }

    @Override
    public List<User> listAll() {
        return users;
    }

    @Override
    public User findByName(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void add(User user) {
        users.add(user);
        saveUsers();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
