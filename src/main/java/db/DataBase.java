package db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.User;

public class DataBase {
    private static Map<String, User> users = new HashMap<>();

    static {
        users.put("user1", new User("user1", "1234", "나단", "a@a"));
        users.put("user2", new User("user2", "1234", "제리", "b@b"));
    }

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
