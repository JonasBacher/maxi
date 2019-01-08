package server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FilledFile extends File {
	private static final long serialVersionUID = 1L;
	
	private byte[] content;
	
	public byte[] getContent() {
		return content;
	}

	public FilledFile(String pathname) throws IOException {
		super(pathname);
		this.content = Files.readAllBytes(this.toPath());
	}
}