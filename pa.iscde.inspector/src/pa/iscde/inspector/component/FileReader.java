package pa.iscde.inspector.component;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

	public static final String MANIFEST_FOLDER_NAME = "META-INF";
	public static final String PLUGIN_FILE_NAME = "plugin.xml";
	private static final String PLUGIN_FILE_CONFIG_PATH = "plugin_path.config";
	private List<String> paths;

	public FileReader() {
		File config = getConfigFile();
		if (config == null)
			throw new IllegalStateException("Configuration file could'nt be found");
		paths = getAllPaths(config);

	}

	private List<String> getAllPaths(File config) {
		List<String> paths = null;
		try {
			paths = Files.readAllLines(config.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paths;
	}

	private File getConfigFile() {
		File config = null;
		DirectoryStream<Path> f = null;
		try {
			f = Files.newDirectoryStream(
					new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath())
							.toPath());
			for (Path path : f) {
				if (path.getFileName().toString().equals(PLUGIN_FILE_CONFIG_PATH)) {
					config = new File(path.toUri());
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return config;
	}

	public List<FilesToRead> getFilesPaths() {
		List<FilesToRead> list = new ArrayList<FilesToRead>();
		for (String path : paths) {
			list.addAll(filesToReadPaths(path));
		}
		return list;
	}

	private ArrayList<FilesToRead> filesToReadPaths(String path) {
		ArrayList<FilesToRead> paths = new ArrayList<FilesToRead>();

		File file = new File(path);

		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {

			if (files[i].isDirectory()) {
				File[] files2 = files[i].listFiles();
				int numerOfFilesRead = 0;
				FilesToRead filesToRead = new FilesToRead();

				for (int j = 0; j < files2.length; j++) {
					if (files2[j].getName().equals(MANIFEST_FOLDER_NAME)) {
						filesToRead.setManifestPath(files2[j].listFiles()[0].getAbsolutePath());
						numerOfFilesRead++;
					}
					if (files2[j].getName().equals(PLUGIN_FILE_NAME)) {
						filesToRead.setPluginXmlPath(files2[j].getAbsolutePath());
						numerOfFilesRead++;
					}
					if (numerOfFilesRead == 2) {
						paths.add(filesToRead);
						break;
					}
				}
			}
		}
		return paths;
	}

//	public static void main(String[] args) {
//		for (FilesToRead string : new FileReader().getFilesPaths()) {
//			System.out.println(string.getManifestPath());
//		}
//	}

}
