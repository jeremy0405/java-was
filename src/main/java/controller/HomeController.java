package controller;

import db.ArticleDataBase;
import db.UserDataBase;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;
import webserver.HttpStatus;
import webserver.Request;
import webserver.Response;

public class HomeController implements Controller {

	private static final HomeController instance = new HomeController();

	private Logger log = LoggerFactory.getLogger(HomeController.class);
	private HomeController() {
	}

	public static HomeController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		byte[] body;
		if (request.takeExtension().equals("html")) {
			body = makeDynamicHtmlBody(request);
		} else {
			body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());

		}
		log.debug(body.length+"");
		List<Pair> pairs = new ArrayList<>();

		log.debug(request.getMime());

		pairs.add(new Pair("Content-Type", request.getMime()));
		pairs.add(new Pair("Content-Length", String.valueOf(body.length)));

		log.debug("path: {}", request.getPath());
		response.write(body, HttpStatus.OK, pairs);
	}

	private byte[] makeDynamicHtmlBody(Request request) throws IOException {
		StringBuilder sb = new StringBuilder();

		List<String> htmlLines = Files.readAllLines(new File("./webapp" + request.getPath()).toPath());
		String target = "{{articles}}";
		for (String htmlLine : htmlLines) {
			if (htmlLine.contains(target)) {
				writeDynamicArticleList(sb);
				continue;
			}

			sb.append(htmlLine);
		}

		return sb.toString().getBytes(StandardCharsets.UTF_8);
	}

	private void writeDynamicArticleList(StringBuilder sb) {
		Collection<Article> articles = ArticleDataBase.findAll();
		for (Article article : articles) {
			sb.append(
				"            <li>\n"
				+ "                  <div class=\"wrap\">\n"
				+ "                      <div class=\"main\">\n"
				+ "                          <strong class=\"subject\">\n"
				+ "                              <a href=\"./qna/show.html\">"+article.getContent()+"</a>\n"
				+ "                          </strong>\n"
				+ "                          <div class=\"auth-info\">\n"
				+ "                              <i class=\"icon-add-comment\"></i>\n"
				+ "                              <span class=\"time\">"+article.getDate()+"</span>\n"
				+ "                              <a href=\"./user/profile.html\" class=\"author\">"+article.getUserId()+"</a>\n"
				+ "                          </div>\n"
				+ "                          <div class=\"reply\" title=\"댓글\">\n"
				+ "                              <i class=\"icon-reply\"></i>\n"
				+ "                              <span class=\"point\">8</span>\n"
				+ "                          </div>\n"
				+ "                      </div>\n"
				+ "                  </div>\n"
				+ "              </li>\n");
		}
	}

}
