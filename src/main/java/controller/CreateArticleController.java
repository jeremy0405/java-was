package controller;

import db.ArticleDataBase;
import db.UserDataBase;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;
import webserver.HttpSession;
import webserver.HttpStatus;
import webserver.Request;
import webserver.Response;

public class CreateArticleController implements Controller {

	private static final CreateArticleController instance = new CreateArticleController();

	private Logger log = LoggerFactory.getLogger(CreateArticleController.class);

	private CreateArticleController() {
	}

	public static CreateArticleController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		String userId = HttpSession.checkUser(request.getUUID());
		User findUser = UserDataBase.findUserById(userId);

		log.debug(request.getMime());

		List<Pair> pairs = new ArrayList<>();
		pairs.add(new Pair("Content-Type", request.getMime()));

		if (findUser != null) {
			Map<String, String> articleData = request.takeParsedBody();
			Article article = new Article(LocalDate.now(), findUser.getUserId(), articleData.get("contents"));
			ArticleDataBase.postArticle(article);
		}

		pairs.add(new Pair("Location", "http://localhost:8080/index.html"));
		response.write(HttpStatus.FOUND, pairs);

		log.debug("path: {}", request.getPath());
	}

}
