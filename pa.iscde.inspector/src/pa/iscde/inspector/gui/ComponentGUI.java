package pa.iscde.inspector.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.omg.CORBA.OMGVMCID;

import extensionPoint.IActionComponentImp;
import extensionPoint.TestExtensionPoint;
import pa.iscde.inspector.deepsearch.SearchComponent;
import pa.iscde.inspector.extensibility.IAction;
import pa.iscde.inspector.extensibility.IActionComponent;
import pa.iscde.inspector.internal.InspectorAtivator;

public class ComponentGUI {

	private Composite viewArea;
	private Collection<ComponentDisign> componentDisigns;
	private Collection<Composite> composites = new ArrayList<Composite>();
	private Composite zestPanel;
	private Composite configButtonPanel;
	private Composite actionPanel;
	private Graph graph;
	private int height;
	private Composite serviceComposite;
	private org.eclipse.swt.widgets.List listService;
	private List<File> servicesClass = new ArrayList<File>();
	private SearchComponent searchComponent;
	private Composite infoComposite;
	private ComponentInfoView componentInfoView;
	private Tree tree;

	public ComponentGUI(Composite viewArea, Collection<ComponentDisign> componentDisigns) {
		this.viewArea = viewArea;
		height = viewArea.getSize().y;
		this.componentDisigns = componentDisigns;
		searchComponent = new SearchComponent();
	}

	private void organizeLayout() {
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.heightHint = (int) (.8 * height);
		viewArea.setLayout(new GridLayout(1, false));
		configButtonPanel = new Composite(viewArea, SWT.NONE);
		configButtonPanel.setLayout(new RowLayout(SWT.VERTICAL | SWT.UP));
		zestPanel = new Composite(viewArea, SWT.NONE);
		zestPanel.setLayoutData(gridData);
		zestPanel.setLayout(new FillLayout());
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
		TabFolder infoTab = addInfoTab();
		addServiceTab(infoTab);
		addAtionTab(infoTab);

	}

	private TabFolder addInfoTab() {
		TabFolder tabFolder = new TabFolder(actionPanel, SWT.NONE);
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Component Info");
		infoComposite = new Composite(tabFolder, SWT.NONE);
		infoComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		infoComposite.setLayout(new FillLayout());
		tabItem.setControl(infoComposite);

		return tabFolder;
	}

	private void addServiceTab(TabFolder tabFolder) {

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
				InspectorAtivator.getInstance().getJavaEditorService()
						.openFile(servicesClass.get(listService.getSelectionIndex()));
			}
		});
	}

	private void addAtionTab(TabFolder tabFolder) {
		final TestExtensionPoint testExtensionPoint = new TestExtensionPoint();

		final Collection<IAction> getiActions = testExtensionPoint.getiActions();
		graph.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				List selection = ((Graph) e.widget).getSelection();
				List<IActionComponent> components = new ArrayList<IActionComponent>();
				showInfo(selection);
				showServices(selection);
				if (!selection.isEmpty()) {
					for (Object object : selection) {
						GraphItem obj = (GraphItem) object;
						if (obj instanceof GraphNode)
							for (ComponentDisign componentDisign : componentDisigns) {
								if (componentDisign.getNode().equals(obj)) {
									componentDisign.getBundle();
									components.add(new IActionComponentImp(componentDisign.getBundle(), obj));
									break;
								}
							}
						else if (obj instanceof GraphConnection)
							components.add(new IActionComponentImp(null, obj));
					}
				}
				for (final IAction actions : getiActions) {
					actions.selectionChange(components);

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		List<IAction> actions = (List<IAction>) testExtensionPoint.getiActions();
		List<String> names = testExtensionPoint.getNames();
		for (int i = 0; i <  actions.size();i++) {
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText(names.get(i));
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tabItem.setControl(composite);
			actions.get(i).actionComposite(composite);
		}

	}

	protected void showInfo(List selection) {
		if (tree != null) {
			tree.dispose();
		}
		List<ComponentDisign> componentDisigns = new ArrayList<ComponentDisign>();
		List<GraphConnection> graphConnections = new ArrayList<GraphConnection>();
		for (Object obj : selection) {
			GraphItem item = (GraphItem) obj;
			if (item instanceof GraphNode)
				componentDisigns.add((ComponentDisign) item.getData());
			else {
				graphConnections.add((GraphConnection) item);

			}
		}
		componentInfoView = new ComponentInfoView(infoComposite, componentDisigns);
		tree = componentInfoView.genTree(infoComposite);
		componentInfoView.genConnectionTree(graphConnections, infoComposite, tree);
		infoComposite.layout();
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
		searchComponent = null;
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
