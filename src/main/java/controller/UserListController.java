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
		String userId = HttpSession.checkUser(request.getUUID());
		User findUser = DataBase.findUserById(userId);

		log.debug(request.getMime());

		List<Pair> pairs = new ArrayList<>();
		pairs.add(new Pair("Content-Type", request.getMime()));

		if (findUser == null) {
			pairs.add(new Pair("Location", "http://localhost:8080/user/login.html"));
			response.write(HttpStatus.FOUND, pairs);
			return;
		}

		byte[] body = makeDynamicHtmlBody();
		pairs.add(new Pair("Content-Length", String.valueOf(body.length)));
		response.write(body, HttpStatus.OK, pairs);

		log.debug("path: {}", request.getPath());
	}

	private byte[] makeDynamicHtmlBody() throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> htmlLines = Files.readAllLines(new File("./webapp/user/list.html").toPath());
		String target = "{{users}}";
		for (String htmlLine : htmlLines) {

			if (htmlLine.contains(target)) {
				writeDynamicUserList(sb);
				continue;
			}

			sb.append(htmlLine);
		}

		return sb.toString().getBytes(StandardCharsets.UTF_8);
	}

	private void writeDynamicUserList(StringBuilder sb) {
		Collection<User> users = DataBase.findAll();
		int count = 0;
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
	}

}
