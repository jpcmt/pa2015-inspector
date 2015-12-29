package pa.iscde.inspector.gui;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import extensionPoint.IActionComponentImp;
import extensionPoint.TestExtensionPoint;
import pa.iscde.inspector.extensibility.IAction;
import pa.iscde.inspector.internal.InspectorAtivator;

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
	private Composite serviceComposite;
	private org.eclipse.swt.widgets.List listService;
	private List<File> servicesClass = new ArrayList<File>();

	public ComponentGUI(Composite viewArea, Collection<ComponentDisign> componentDisigns) {
		this.viewArea = viewArea;
		height = viewArea.getSize().y;
		width = viewArea.getSize().x;
		this.componentDisigns = componentDisigns;
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
		TabFolder tabFolder = addServiceTab();
		addAtionTab(tabFolder);
		zestPanel.setLayout(new FillLayout());

	}

	private TabFolder addServiceTab() {

		TabFolder tabFolder = new TabFolder(actionPanel, SWT.NONE);
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Services");
		serviceComposite = new Composite(tabFolder, SWT.NONE);
		serviceComposite.setLayout(new FillLayout());
		tabItem.setControl(serviceComposite);
		listService = new org.eclipse.swt.widgets.List(serviceComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		listService.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				InspectorAtivator.getInstance().getJavaEditorService().openFile(servicesClass.get(listService.getSelectionIndex()));
			}
		});
		return tabFolder;
	}

	private void addAtionTab(TabFolder tabFolder) {
		final TestExtensionPoint testExtensionPoint = new TestExtensionPoint();

		graph.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				List selection = ((Graph) e.widget).getSelection();
				IActionComponentImp iActionComponentImp = null;
				showServices(selection);
				if (!selection.isEmpty()) {
					GraphItem obj = (GraphItem) selection.get(0);
					if (obj instanceof GraphNode)
						for (ComponentDisign componentDisign : componentDisigns) {
							if (componentDisign.getNode().equals(obj)) {
								componentDisign.getBundle();
								iActionComponentImp = new IActionComponentImp(componentDisign.getBundle(), obj);
								break;
							}
						}
					else if (obj instanceof GraphConnection)
						iActionComponentImp = new IActionComponentImp(null, obj);
				}
				for (final IAction actions : testExtensionPoint.getiActions()) {
					if (iActionComponentImp != null)
						actions.selectionChange(iActionComponentImp);
					else
						actions.zeroSelection();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		for (final IAction actions : testExtensionPoint.getiActions()) {
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText(actions.TabName());
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tabItem.setControl(composite);
			actions.actionComposite(composite);
		}

	}

	protected void showServices(List selection) {

		listService.removeAll();
		servicesClass.clear();
		for (Object object : selection) {
			if (object instanceof GraphNode) {
				GraphNode item = (GraphNode) object;
				ComponentDisign disign = (ComponentDisign) item.getData();
				if (!disign.getComponentData().getServices().isEmpty()) {
					listServices(disign.getComponentData().getServices());
				}
			}

		}
	}

	private void listServices(List<File> list) {
		for (File f : list) {
			servicesClass.add(f);
			listService.add(f.getName());
		}
		serviceComposite.layout();
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
