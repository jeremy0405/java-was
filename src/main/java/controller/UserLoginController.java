package controller;

import db.UserDataBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;
import webserver.HttpSession;
import webserver.HttpStatus;
import webserver.Request;
import webserver.Response;

public class UserLoginController implements Controller {

	private static final UserLoginController instance = new UserLoginController();

	private Logger log = LoggerFactory.getLogger(UserLoginController.class);

	private UserLoginController() {
	}

	public static UserLoginController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) {
		Map<String, String> parsedBody = request.takeParsedBody();
		log.debug("POST BODY: {}", parsedBody);

		log.debug(request.getMime());

		User user = UserDataBase.findUserById(parsedBody.get("userId"));
		List<Pair> pairs = new ArrayList<>();
		pairs.add(new Pair("Content-Type", request.getMime()));

		if (user != null && user.getPassword().equals(parsedBody.get("password"))) {
			log.debug("login 성공");
			pairs.add(new Pair("Location", "http://localhost:8080/index.html"));
			pairs.add(new Pair("Set-Cookie", "sessionId=" + HttpSession.makeUUID(user.getUserId()) + "; max-age=20; Path=/; HttpOnly"));
			response.write(HttpStatus.FOUND, pairs);
			return;
		}
		log.debug("login 실패");
		pairs.add(new Pair("Location", "http://localhost:8080/user/login_failed.html"));
		response.write(HttpStatus.FOUND, pairs);
	}


}
