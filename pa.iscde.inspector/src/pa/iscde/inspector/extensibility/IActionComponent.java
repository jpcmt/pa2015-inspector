package pa.iscde.inspector.extensibility;

import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.osgi.framework.Bundle;

public interface IActionComponent {

	boolean hasBundle();
	Bundle getBundle();
	Graph_Type getType();
	GraphNode getNode();
	GraphConnection getConnection();
	
	
}
