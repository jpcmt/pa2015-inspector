package pa.iscde.inspector.component;

import java.io.File;
import java.util.List;

import org.osgi.framework.Bundle;
/**
 * Represents the most relevant data about a {@link Bundle}
 * @author Jorge Teixeira
 *
 */
public interface ComponentData {
	/**
	 * Get the name of the bundle
	 * @return the name
	 */
	String getName();
	/**
	 * Get a list of extension that this bundle have 
	 * @return the list of extensions
	 */
	List<Extension> getExtensions();
	/**
	 * Get a list of extension point that this bundle have 
	 * @return the list of extention point
	 */
	List<ExtensionPoint> getExtensionPoints();
	/**
	 * Get the list of services that this component provide
	 * @return
	 */
	List<File> getServices();
	/**
	 * Get this component symbolic name
	 * @return The symbolic name
	 */
	String getSymbolicName();
	/**
	 * Get this component {@link Bundle}
	 * @return the bundle
	 */
	Bundle getBundle();

}
