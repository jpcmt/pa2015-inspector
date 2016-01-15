package pa.iscde.inspector.extensibility;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.osgi.framework.Bundle;
/**
 * 
 * @author Jorge Teixeira
 *
 *This interface represent the object selected by the user which can be two types. {@link GraphConnection} or {@link GraphNode}.
 *
 */
public interface IActionComponent {
	/**
	 * Verifie if the {@link IActionComponent} contains a {@link Bundle}
	 * @return true if the {@link Bundle} is present
	 */
	boolean hasBundle();
	
	/**
	 * After calling hasBundle() 
	 * This method gets the bundle associate with the {@link IActionComponent}
	 * @return the bundle
	 */
	Bundle getBundle();
	
	/**
	 * Get the type of the select {@link Widget}
	 * 
	 * @return the {@link Widget} type
	 */
	Graph_Type getType();
	
	/**
	 * When you know the type
	 * This methods gets the Node select in the {@link SelectionEvent}
	 * @return the node selected
	 */
	GraphNode getNode();
	
	/**
	 * When you know the type
	 * This methods gets the connection select in the {@link SelectionEvent}
	 * @return the connection selected
	 */
	GraphConnection getConnection();

	/**
	 * When the type can be either a {@link GraphConnection} or {@link GraphNode}
	 * 
	 * @return the item selected
	 */
	GraphItem getGraphItem();
	
	
}
