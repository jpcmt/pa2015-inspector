package pa.iscde.inspector.component;

public class FilesToRead {
	
	private String manifestPath;
	private String pluginXmlPath;
	/**
	 * Get the manifest file path
	 * @return
	 */
	public String getManifestPath() {
		return manifestPath;
	}
	/**
	 * Set the manifest file path
	 * @param manifestPath
	 */
	public void setManifestPath(String manifestPath) {
		this.manifestPath = manifestPath;
	}
	/**
	 * get the plugin.xml path
	 * @return
	 */
	public String getPluginXmlPath() {
		return pluginXmlPath;
	}
	/**
	 * set the plugin.xml path
	 * @param pluginXmlPath
	 */
	public void setPluginXmlPath(String pluginXmlPath) {
		this.pluginXmlPath = pluginXmlPath;
	}
	

}
