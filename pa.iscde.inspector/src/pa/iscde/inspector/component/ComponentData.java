package pa.iscde.inspector.component;

import java.io.File;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public interface ComponentData {
	
	String getName();
	List<Extension> getExtensions();
	List<ExtensionPoint> getExtensionPoints();
	List<File> getServices();
	String getSymbolicName();
	Bundle getBundle();

}
