package pa.iscde.inspector.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.osgi.framework.Bundle;

import pa.iscde.inspector.component.ComponentData;
import pa.iscde.inspector.component.Extension;
import pa.iscde.inspector.component.ExtensionPoint;

public class ComponentDisign {
	private Color color;
	private GraphNode node;
	private Graph graph;
	private List<GraphConnection> graphConnections;
	private ComponentData componentData;
	private List<Extension> extensions;
	private String name;
	private Bundle bundle;

	/**
	 * Construct a new {@link ComponentDisign} 
	 * @param component {@link ComponentData}
	 */
	public ComponentDisign(ComponentData component) {
		this.componentData = component;
		extensions = component.getExtensions();
		graphConnections = new ArrayList<GraphConnection>();
		name = component.getSymbolicName();
		bundle = component.getBundle();
	}
	/**
	 * Get the name
	 * @return
	 */
	public String getName() {
		return componentData.getName();
	}
	/**
	 * Get the {@link ComponentData}
	 * @return
	 */
	public ComponentData getComponentData() {
		return componentData;
	}
	/**
	 * Get the {@link Graph}
	 * @return
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Get the {@link GraphConnection}
	 * @return
	 */
	public List<GraphConnection> getGraphConnections() {
		return graphConnections;
	}
	/**
	 * Get the {@link GraphNode}
	 * @return
	 */
	public GraphNode getNode() {
		return node;
	}
	/**
	 * Get the {@link Bundle}
	 * @return
	 */
	public Bundle getBundle() {
		return bundle;
	}
	/**
	 * Get a list of this component extension
	 * @return
	 */
	public List<Extension> getExtensions() {
		return extensions;
	}
	/**
	 * Get the {@link Color}
	 * @return
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * Get the name
	 * @return
	 */
	public void setColor(Color color) {
		this.color = color;
		if (node != null)
			node.setBackgroundColor(color);
	}

	/**
	 * Get the symbolic name
	 * @return
	 */
	public String getSymbolicName() {
		return componentData.getSymbolicName();
	}
	/**
	 * Get a list of extensionPoints
	 * @return
	 */
	public List<ExtensionPoint> getExtensionPoints() {
		return componentData.getExtensionPoints();
	}

	/**
	 * Set the state of the bundle to update the background color
	 * @param type
	 */
	public void setBlundeState(int type) {
		if (node != null) {
			System.out.println(type);
			color = new Color(color.getDevice(), RGBState.GET_COLOR(type));
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					node.setBackgroundColor(color);

				}
			});
		}
	}
	/**
	 * Find the {@link ComponentDisign} the create this {@link ExtensionPoint}
	 */
	public void setExtensionPointOwnerDesign() {
		for (ExtensionPoint extensionPoint : componentData.getExtensionPoints()) {
			if (extensionPoint != null) {
				extensionPoint.setOwnerDesign(this);
			}
		}
	}
	/**
	 * Draw a {@link GraphNode} in the container
	 * 
	 * @param graphModel
	 */
	public void drawNode(IContainer graphModel) {
		graph = (Graph) graphModel;
		node = new GraphNode(graph, SWT.NONE, name, this);
		node.setData(this);
		if (getBundle() != null) {
			color = new Color(node.getDisplay(), RGBState.GET_COLOR(getBundle().getState()));
		} else
			color = new Color(node.getDisplay(), RGBState.GET_COLOR(-1));

		node.setBackgroundColor(color);
	}

	/**
	 * Draw connections from one {@link GraphNode} to another base
	 * on the {@link Extension} and the {@link ExtensionPoint}
	 */
	public void drawConnections() {
		for (Extension extension : extensions) {
			if (extension.getExtensionPoint() != null) {
				System.out.println(name + "  " + extension.getName() + " " + extension.getExtensionPoint() + " "
						+ extension.getExtensionPoint().getOwnerDesign());
				GraphConnection graphConnection = new GraphConnection(graph, SWT.NONE, node,
						extension.getExtensionPoint().getOwnerDesign().node);
				graphConnection.setData(extension.getConnectionName());
				graphConnection.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
				graphConnections.add(graphConnection);
			}
		}
	}
	/**
	 * See if this component connect to any component
	 * @return true if it does
	 */
	public boolean hasConnection() {
		if (extensions == null || extensions.size() == 0) {
			return false;
		}
		return true;
	}

}
