package webserver;

import db.DataBase;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {
	private Logger log = LoggerFactory.getLogger(Response.class);
	private byte[] body;
	private DataOutputStream dos;
	private Request request;

	public Response(OutputStream out, Request request) {
		this.dos = new DataOutputStream(out);
		this.request = request;
	}

	public void writeResponse() throws IOException {
		log.debug("requestLine: {}", request.getRequestLine());
		if (request.isPOST() && request.getPath().equals("/user/create")) {

			this.body = Files.readAllBytes(new File("./webapp/index.html").toPath());

			Map<String, String> parsedBody = request.takeParsedBody();
			log.debug("POST BODY: {}", parsedBody);
			User user = new User(
				parsedBody.get("userId"),
				parsedBody.get("password"),
				parsedBody.get("name"),
				parsedBody.get("email")
			);
			saveUser(user);
			return;
		}
		this.body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());
		response200Header();
		responseBody();
	}

	private void saveUser(User user) {
		if (DataBase.validateDuplicatedId(user)){
			DataBase.addUser(user);
			response302Header("http://localhost:8080/index.html");
			responseBody();
			return;
		}
		response302Header("http://localhost:8080/user/form.html");
		responseBody();
		return;
	}

	private void response302Header(String redirectURL) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found\r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			dos.writeBytes("Location: " + redirectURL + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header() {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody() {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

}