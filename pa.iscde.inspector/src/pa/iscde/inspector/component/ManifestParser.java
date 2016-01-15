package pa.iscde.inspector.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ManifestParser {

	private String Name;
	private String symbolicName;

	/**
	 * Get the Blunle-Name 
	 * @return name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * Get the Bundle-SymbolicName
	 * @return symbolic name
	 */
	public String getSymbolicName() {
		return symbolicName;
	}
	/**
	 * Read the important attribute in the  manifest file
	 * @param file- the manifest file 
	 * @return 
	 */
	public ManifestParser readFile(File file) {
		try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
			String line = br.readLine();
			while (line != null) {

				if (line.startsWith("Bundle-Name")) {
					Name = line.substring(line.indexOf(' ')+1);
				}
				if(line.startsWith("Bundle-SymbolicName")){
					symbolicName = line.substring(line.indexOf(' ')+1,line.indexOf(';'));
				}
				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
}
