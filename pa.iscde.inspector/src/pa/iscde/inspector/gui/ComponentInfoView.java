package pa.iscde.inspector.gui;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.ComponentView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.zest.core.widgets.GraphConnection;

import pa.iscde.inspector.component.Extension;
import pa.iscde.inspector.component.ExtensionPoint;

public class ComponentInfoView {

	private Composite viewArea;
	private Collection<ComponentDisign> componentDisigns;

	/**
	 * Construct a new {@link ComponentInfoView}
	 * 
	 * @param viewArea
	 *            the composite to write the informations
	 * @param componentDisigns
	 *            collections that have the information to right
	 */
	public ComponentInfoView(Composite viewArea, Collection<ComponentDisign> componentDisigns) {
		this.viewArea = viewArea;
		this.componentDisigns = componentDisigns;
	}

	/**
	 * Write the informations for every component on the componentDisigns
	 */
	public void fillInfoView() {
		viewArea.setLayout(new GridLayout(1, false));
		final Composite topComposite = new Composite(viewArea, SWT.NONE);
		topComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		final Composite bodyComposite = new Composite(viewArea, SWT.NONE);
		bodyComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		final Label label = new Label(topComposite, SWT.LEFT);
		label.setText("Component Info");
		final Button button = new Button(topComposite, SWT.BUTTON4);
		button.setText("view");

		final Tree tree = genTree(bodyComposite);

		bodyComposite.setLayout(new FillLayout());
		topComposite.setLayout(new FillLayout());

		tree.getItems()[0].setExpanded(true);
		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				switch (event.type) {
				case SWT.Selection:
					topComposite.dispose();
					bodyComposite.dispose();
					viewArea.setLayout(new FillLayout(SWT.VERTICAL));
					zestInit();
					viewArea.layout();
					break;

				default:
					break;
				}

			}
		});
		viewArea.layout();

	}

	/**
	 * Create a {@link Tree} that expand to show more information
	 * @param bodyComposite
	 * @return
	 */
	public Tree genTree(Composite bodyComposite) {
		final Tree tree = new Tree(bodyComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.LINE_CUSTOM);
		for (ComponentDisign componentDisign : componentDisigns) {
			TreeItem name = new TreeItem(tree, 0);
			name.setText(componentDisign.getName());
			nodeTreeAtributtes(componentDisign, name);
		}

		tree.addListener(SWT.Expand, new Listener() {
			@Override
			public void handleEvent(Event e) {
			}

		});
		return tree;
	}

	private void nodeTreeAtributtes(ComponentDisign componentDisign, TreeItem name) {
		TreeItem symbolicName = new TreeItem(name, 1);
		symbolicName.setText("symbolic name : " + componentDisign.getSymbolicName());
		TreeItem extensão = new TreeItem(name, 2);
		extensão.setText("Extensions");
		TreeItem extensionPoint = new TreeItem(name, 3);
		extensionPoint.setText("Extension Points");
		int i = 0;
		for (Extension extension : componentDisign.getExtensions()) {
			TreeItem ext_number = new TreeItem(extensão, 0);
			ext_number.setText("[" + i++ + "] : Extension");
			TreeItem id = new TreeItem(ext_number, 0);
			id.setText("id :" + extension.getId());
			TreeItem ex_name = new TreeItem(ext_number, 1);
			ex_name.setText("name :" + extension.getName());
			TreeItem point = new TreeItem(ext_number, 1);
			point.setText("point :" + extension.getPoint());
			TreeItem extensionPointID = new TreeItem(ext_number, 1);
			extensionPointID.setText("Extension Point Id :" + extension.getPoint());

		}
		for (ExtensionPoint extPoint : componentDisign.getExtensionPoints()) {
			TreeItem id = new TreeItem(extensionPoint, 0);
			id.setText("id :" + extPoint.getId());
			TreeItem ex_name = new TreeItem(extensionPoint, 1);
			ex_name.setText("name :" + extPoint.getName());
			TreeItem schema = new TreeItem(extensionPoint, 1);
			schema.setText("schema :" + extPoint.getSchema());

		}
	}
	/**
	 * Constructor a new {@link ComponentGUI}
	 */
	protected void zestInit() {
		new ComponentGUI(viewArea, componentDisigns).fillArea();
	}

	/**
	 * Write information for the connection
	 * @param graphConnections list of connection to right information about
	 * @param infoComposite the composite to right on
	 * @param tree the tree to fill on the @param infoComposite
	 */
	public void genConnectionTree(List<GraphConnection> graphConnections, Composite infoComposite, Tree tree) {
		for (GraphConnection graphConnection : graphConnections) {
			TreeItem connection = new TreeItem(tree, 0);
			connection.setText("Connection : " + graphConnection.getData());
			TreeItem sourceName = new TreeItem(connection, 1);
			ComponentDisign source = (ComponentDisign) graphConnection.getSource().getData();
			sourceName.setText("Source : " + source.getName());
			nodeTreeAtributtes(source, sourceName);
			TreeItem destName = new TreeItem(connection, 2);
			ComponentDisign destination = (ComponentDisign) graphConnection.getDestination().getData();
			destName.setText("Destination  : " + destination.getName());
			nodeTreeAtributtes(destination, destName);
		}
	}

}
