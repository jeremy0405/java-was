package db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.User;

public class DataBase {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static boolean validateDuplicatedId(User user) {
        User findUser = findUserById(user.getUserId());
        return findUser == null;
    }
}
