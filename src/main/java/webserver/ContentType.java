package webserver;

import java.util.Arrays;

public enum ContentType {

	HTML("text/html", "html"),
	CSS("text/css", "css"),
	JS("text/javascript", "js"),
	DEFAULT("text/html", "null");

	private final String mime;
	private final String extension;

	ContentType(String mime, String extension) {
		this.mime = mime;
		this.extension = extension;
	}

	public static ContentType create(String extension) {
		return Arrays.stream(ContentType.values())
			.filter(c -> c.isSameExtension(extension))
			.findAny()
			.orElse(DEFAULT);
	}

	public String getMime() {
		return mime;
	}

	private boolean isSameExtension(String extension) {
		return this.extension.equals(extension);
	}

}
