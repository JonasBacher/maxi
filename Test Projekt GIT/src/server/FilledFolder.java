package server;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class FilledFolder implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private ArrayList<FilledFolder> subfolders = new ArrayList<>();
	private ArrayList<FilledFile> files = new ArrayList<>();

	public String getName() {
		return name;
	}

	public ArrayList<FilledFolder> getSubfolders() {
		return subfolders;
	}

	public ArrayList<FilledFile> getFiles() {
		return files;
	}

	public FilledFolder(File folder) throws IOException {
		this.name = folder.getName();

		File[] files = folder.listFiles();
		
		try {
			for (File file : files) {
				if (file.isDirectory()) {
					this.subfolders.add(new FilledFolder(file));
				}
				if (file.isFile()) {
					this.files.add(new FilledFile(file.getAbsolutePath()));
				}
			}
		} catch (NullPointerException e) {
			/* Permission denied */
		}
	}
}