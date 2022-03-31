package controller;

import db.DataBase;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;
import webserver.HttpSession;
import webserver.HttpStatus;
import webserver.Request;
import webserver.Response;

public class UserListController implements Controller {

	private static final UserListController instance = new UserListController();

	private Logger log = LoggerFactory.getLogger(UserListController.class);

	private UserListController() {
	}

	public static UserListController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		String uuid = request.getUUID();
		String userId = HttpSession.checkUser(uuid);
		User findUser = DataBase.findUserById(userId);
		if (findUser == null) {
			//todo
			//redirect : login.html
			List<Pair> pairs = new ArrayList<>();
			pairs.add(new Pair("Location", "http://localhost:8080/user/login.html"));
			response.write(HttpStatus.FOUND, pairs);
			return;
		}
		//todo
		//userlist 출력
		List<String> htmlLines = Files.readAllLines(new File("./webapp/user/list.html").toPath());
		String target = "<!--{{users}}-->";
		int lock = 0;
		int count = 0;
		StringBuilder sb = new StringBuilder();
		for (String htmlLine : htmlLines) {
			if (lock == 1) {
				continue;
			}
			if (target.equals(htmlLine)) {
				lock++;
				Collection<User> users = DataBase.findAll();
				for (User user : users) {
					count++;
					sb.append("<tr>\n")
						.append("<th scope=\"row\">" + count + "</th>\n")
						.append("<td>" + user.getUserId() + "</td>\n")
						.append("<td>" + user.getName() + "</td>\n")
						.append("<td>" + user.getEmail() + "</td>\n")
						.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
						.append("</tr>\n");
				}
				continue;
			}
			sb.append(htmlLine);
		}
		byte[] body = sb.toString().getBytes(StandardCharsets.UTF_8);
		log.debug("path: {}", request.getPath());
		response.write(body, HttpStatus.OK);
	}

}
