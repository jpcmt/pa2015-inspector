package pa.iscde.inspector.extensibility;

import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * Represents the type of object supported in the zest graph for the action.
 * @author Jorge Teixeira
 *
 */
public enum Graph_Type {
		/**
		 * NODE - represent the zest {@link GraphNode}
		 * 
		 */
		NODE,
		/**
		 * CONNECTION - represent the zest {@link GraphConnection}
		 */
		CONNECTION
}
