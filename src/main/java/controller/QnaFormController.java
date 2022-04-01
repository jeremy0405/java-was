package controller;

import db.UserDataBase;
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

public class QnaFormController implements Controller {

	private static final QnaFormController instance = new QnaFormController();

	private Logger log = LoggerFactory.getLogger(QnaFormController.class);

	private QnaFormController() {
	}

	public static QnaFormController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		String userId = HttpSession.checkUser(request.getUUID());
		User findUser = UserDataBase.findUserById(userId);

		log.debug(request.getMime());

		List<Pair> pairs = new ArrayList<>();
		pairs.add(new Pair("Content-Type", request.getMime()));

		if (findUser == null) {
			pairs.add(new Pair("Location", "http://localhost:8080/user/login.html"));
			response.write(HttpStatus.FOUND, pairs);
			return;
		}

		byte[] body = Files.readAllBytes(new File("./webapp" + request.getPath() + ".html").toPath());
		log.debug(request.getPath() + ".html");
		pairs.add(new Pair("Content-Length", String.valueOf(body.length)));
		response.write(body, HttpStatus.OK, pairs);

		log.debug("path: {}", request.getPath());
	}


}
