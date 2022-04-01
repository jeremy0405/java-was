package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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
		byte[] body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());
		List<Pair> pairs = new ArrayList<>();

		log.debug(request.getMime());

		pairs.add(new Pair("Content-Type", request.getMime()));
		pairs.add(new Pair("Content-Length", String.valueOf(body.length)));
//		dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//		dos.writeBytes("Content-Length: " + body.length + "\r\n");


		log.debug("path: {}", request.getPath());
		response.write(body, HttpStatus.OK, pairs);
	}
}
