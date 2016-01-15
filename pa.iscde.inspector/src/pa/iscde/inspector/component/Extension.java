package pa.iscde.inspector.component;

import java.util.List;

import com.google.common.base.Splitter;
/**
 * Represent the extension of the bundle which information is get from the plugin.xml file
 * @author Jorge
 *
 */
public class Extension {

	private String id;
	private String name;
	private String point;
	private String connectionName;
	private ExtensionPoint extensionPoint;

	/**
	 * Get the extension id
	 *  @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Get the extension name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 *  Get the extension connection name
	 * @return name
	 */
	public String getConnectionName() {
		if (connectionName == null) {
			connectionName = name + " -> " + extensionPoint.getName();
		}
		return connectionName;
	}
	/**
	 *  Get the extension point that this extension extends {@link ExtensionPoint}
	 * @return extension point
	 */
	public ExtensionPoint getExtensionPoint() {
		return extensionPoint;
	}

	/**
	 * /**
	 *  Get the extension point attribute
	 * @return
	 */
	public String getPoint() {
		return point;
	}

	/**
	 * Create a new extension
	 * @param id attribute
	 * @param name attribute
	 * @param point attribute
	 */
	public Extension(String id, String name, String point) {
		this.id = id;
		this.point = point;
		this.name = verifieName(name);

	}

	private String verifieName(String name2) {
		String name = name2 != null ? name2 : id;
		if (name == null) {
			Iterable<String> split = Splitter.on(".").split(point);
			for (String string : split) {
				name = string;
			}
		}

		return name;
	}

	@Override
	public String toString() {

		return "Extension: id = " + id + " - name = " + name + " - point = " + point + " .";
	}
	
	/**
	 * 	Find this object extension point
	 * @param eXTPOINTS
	 */
	public void findConnections(List<ExtensionPoint> eXTPOINTS) {
		String[] split = point.split("\\.");
		String stringToCompare = "";
		if (split.length > 0) {
			stringToCompare = split[split.length - 1];
		}
		for (ExtensionPoint extensionPoint : eXTPOINTS) {
			if (extensionPoint.getId().equals(stringToCompare)) {
				this.extensionPoint = extensionPoint;
			}
		}
	}

}
