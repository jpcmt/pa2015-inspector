package extensionPoint;

import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.osgi.framework.Bundle;

import pa.iscde.inspector.extensibility.Graph_Type;
import pa.iscde.inspector.extensibility.IActionComponent;

/**
 * This class represents the object that you can make action on.
 * @author Jorge Teixeira
 *
 */

public class IActionComponentImp implements IActionComponent {
	/**
	 * This is the component {@link Bundle}.
	 */
	Bundle bundle;
	/**
	 * This represent the node object
	 */
	GraphNode node;
	/**
	 * This represent the connection
	 */
	GraphConnection connection;
	private Graph_Type type;
	
	/**
	 * To construct the {@link IActionComponentImp} you need two provide the follow parameters 
	 * @param bundle if available 
	 * @param graphObj the object select in the Zest display
	 */
	public IActionComponentImp(Bundle bundle, GraphItem graphObj) {
		this.bundle = bundle;
		if (graphObj instanceof GraphNode) {
			node = (GraphNode) graphObj;
			type = Graph_Type.NODE;
		} else if(graphObj instanceof GraphConnection){
			connection = (GraphConnection) graphObj;
			type = Graph_Type.CONNECTION;
		} else
			throw new IllegalArgumentException("Class of object not suported " + graphObj.getClass());
	}
	
	@Override
	public boolean hasBundle() {
		return bundle != null;
	}

	@Override
	public Bundle getBundle() {
		return bundle;
	}

	@Override
	public Graph_Type getType() {
		return type;
	}

	@Override
	public GraphNode getNode() {
		
		return node;
	}

	@Override
	public GraphConnection getConnection() {
		return connection;
	}

	@Override
	public GraphItem getGraphItem() {
		if(connection != null)
			return connection;
		if(node != null)
			return node;
		return null;
	}

}
