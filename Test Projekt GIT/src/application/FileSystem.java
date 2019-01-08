package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileSystem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private FilledFolder root;
	private FilledFile file;

	private void rebuild(FilledFolder ff, String currentRoot) throws IOException {
		ArrayList<FilledFile> subFiles = ff.getFiles();
		ArrayList<FilledFolder> subFolders = ff.getSubfolders();
		
		try {
			Files.createDirectory(new File(currentRoot).toPath());
		}catch (FileAlreadyExistsException e){
			/* Wenn der Ordner bereits existiert, wird der Inhalt in diesen gespeichert */
		}

		for (FilledFile file : subFiles) {
			System.out.println(currentRoot + "\\" + file.getName());
			File newFile = new File(currentRoot + "\\" + file.getName());
			newFile.createNewFile();
			Files.write(newFile.toPath(), file.getContent());
		}
		for (FilledFolder folder : subFolders) {
			rebuild(folder, currentRoot + "\\" + folder.getName());
		}
	}
	
	public void rebuild(String outputPath) throws IOException {
		if(file == null) {
			rebuild(root, outputPath);	
		}else {
			File newFile = new File(outputPath + "\\" + file.getName());
			newFile.createNewFile();
			Files.write(newFile.toPath(), file.getContent());
		}
	}

	public FileSystem(String path) throws IOException {
		File file = new File(path);
		if(file.isFile()) {
			this.file = new FilledFile(path);
		}else {
			this.root = new FilledFolder(new File(path));	
		}
	}
}