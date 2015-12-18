package pa.iscde.inspector.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import extensionPoint.IActionComponentImp;
import extensionPoint.TestExtensionPoint;
import pa.iscde.inspector.extensibility.IAction;

public class ComponentGUI {

	private Composite viewArea;
	private Collection<ComponentDisign> componentDisigns;
	private Collection<Composite> composites = new ArrayList<Composite>();
	Composite zestPanel;
	Composite configButtonPanel;
	Composite actionPanel;
	private Graph graph;
	private int height;
	private int width;

	public ComponentGUI(Composite viewArea, Collection<ComponentDisign> componentDisigns2) {
		this.viewArea = viewArea;
		height = viewArea.getSize().x;
		width = viewArea.getSize().y;
		System.out.println(height + " " + width);
		this.componentDisigns = componentDisigns2;
	}

	private void organizeLayout() {
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.heightHint = (int) (.8 * height);
		viewArea.setLayout(new GridLayout(1, false));
		configButtonPanel = new Composite(viewArea, SWT.NONE);
		configButtonPanel.setLayout(new RowLayout(SWT.VERTICAL | SWT.UP));
		zestPanel = new Composite(viewArea, SWT.NONE);
		zestPanel.setLayoutData(gridData);
		actionPanel = new Composite(viewArea, SWT.V_SCROLL);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		actionPanel.setLayoutData(gridData);
		actionPanel.setLayout(new FillLayout());
		composites.add(actionPanel);
		composites.add(zestPanel);
		composites.add(configButtonPanel);

	}

	public void fillArea() {
		organizeLayout();
		graph = new Graph(zestPanel, SWT.NONE);
		graph.addSelectionListener(new GUISelectionAdapter());
		graph.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
		drawNode(graph);
		drawConnections();
		graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		final Button b = new Button(configButtonPanel, SWT.NONE);
		b.setText("Back");

		b.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Composite composite : composites) {
					composite.dispose();
				}
				InfoInit();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		addAtionTab();
		zestPanel.setLayout(new FillLayout());

	}

	private void addAtionTab() {
		TestExtensionPoint testExtensionPoint = new TestExtensionPoint();
		TabFolder tabFolder = new TabFolder(actionPanel, SWT.NONE);

		for (final IAction actions : testExtensionPoint.getiActions()) {
			graph.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					List selection = ((Graph) e.widget).getSelection();
					if (!selection.isEmpty()) {
						Object obj = selection.get(0);
						for (ComponentDisign componentDisign : componentDisigns) {
							if (componentDisign.getNode().equals(obj)
									|| componentDisign.getGraphConnections().equals(obj)){
								actions.selectionChange(new IActionComponentImp(componentDisign.getBundle(), obj));
								break;
							}
						}
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});

			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText(actions.TabName());
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tabItem.setControl(composite);
			actions.actionComposite(composite);
		}
	}

	protected void InfoInit() {
		new ComponentInfoView(viewArea, componentDisigns).fillInfoView();

	}

	private void drawConnections() {
		for (ComponentDisign componentDisign : componentDisigns) {
			if (componentDisign.hasConnection()) {
				componentDisign.drawConnections();
			}
		}
	}

	private void drawNode(Graph graph) {
		for (ComponentDisign componentDisign : componentDisigns) {
			componentDisign.drawNode(graph);
		}
	}
}
