package test;

import pa.iscde.inspector.extensibility.Graph_Type;
import pa.iscde.inspector.extensibility.IAction;
import pa.iscde.inspector.extensibility.IActionComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.zest.core.widgets.GraphItem;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * 
 * @author Jorge Teixeira
 * 
 *         This class is a test class for the extensionPoit InspectorAction
 *
 */
public class TestAction implements IAction {

	Composite composite;
	IActionComponent actionComp;
	List<GraphItem> invisibles = new ArrayList<GraphItem>();
	List<ActionButton> buttons = new ArrayList<ActionButton>();
	private ButtonListener buttonListener = new ButtonListener();
	GraphItem item;

	@Override
	public String TabName() {

		return "action";
	}

	@Override
	public void actionComposite(Composite composite) {
		composite.setLayout(new GridLayout(3, true));
		composite.setSize(200, 200);
		this.composite = composite;
	}

	@Override
	public void selectionChange(final IActionComponent iActionComponent) {
		actionComp = iActionComponent;
		handle();
	}

	@Override
	public void zeroSelection() {
		actionComp = null;
		for (ActionButton actionButton : buttons) {
			actionButton.dispose();
		}
		if (!invisibles.isEmpty()) {
			ActionButton makeVisible = new ActionButton(composite, SWT.BUTTON1, ButtonType.VISIBLE, "make all visible");
			makeVisible.addListener(buttonListener);
			buttons.add(makeVisible);
			composite.layout();
		}

	}

	private void handle() {
		if (!buttons.isEmpty())
			disposeAll();
		if (actionComp != null)
			switch (actionComp.getType()) {
			case CONNECTION:
				item = actionComp.getConnection();
				handleConnection();
				break;
			case NODE:
				item = actionComp.getNode();
				handleNode();
			default:
				break;
			}
	}

	private void handleNode() {
		if (actionComp.hasBundle() && actionComp.getBundle().getState() == Bundle.ACTIVE) {
			ActionButton stop = new ActionButton(composite, SWT.BUTTON1, ButtonType.STOP, "stop");
			stop.addListener(buttonListener);
			buttons.add(stop);
		}
		ActionButton rename = new ActionButton(composite, SWT.BUTTON1, ButtonType.RENAME, "rename");
		rename.addListener(buttonListener);
		buttons.add(rename);
		if (actionComp.getNode().isVisible()) {
			ActionButton invisible = new ActionButton(composite, SWT.BUTTON1, ButtonType.INVISIBLE, "invisible");
			invisible.addListener(buttonListener);
			buttons.add(invisible);
		}
		composite.layout();

	}

	private void handleConnection() {
		ActionButton rename = new ActionButton(composite, SWT.BUTTON1, ButtonType.RENAME, "rename");
		rename.addListener(buttonListener);
		buttons.add(rename);

		if (actionComp.getConnection().isVisible()) {
			ActionButton invisible = new ActionButton(composite, SWT.BUTTON1, ButtonType.INVISIBLE, "invisible");
			invisible.addListener(buttonListener);
			buttons.add(invisible);
			composite.layout();
		}
	}

	private void disposeAll() {
		for (ActionButton button : buttons) {
			button.dispose();
		}
		buttons.clear();
	}

	class ButtonListener implements Listener {

		@Override
		public void handleEvent(Event event) {
			Button b = (Button) event.widget;
			ActionButton actionButton = null;
			for (ActionButton actionButt : buttons) {
				if (actionButt.HasThisButton(b)) {
					actionButton = actionButt;
					break;
				}
			}
			switch (actionButton.type) {
			case STOP:
				stopBundle();
				break;
			case RENAME:
				renameGraphObj();
				break;
			case START:
				startBundle();
				break;
			case INVISIBLE:
				invisible();
				break;
			case VISIBLE:
				visible();
				break;
			default:
				break;
			}
			handle();
		}

		private void visible() {
			for (GraphItem item : invisibles) {
				item.setVisible(true);
			}

		}

		private void invisible() {
			GraphItem item = null;
			if (actionComp.getType() == Graph_Type.CONNECTION) {
				item = actionComp.getConnection();
			}
			if (actionComp.getType() == Graph_Type.NODE) {
				item = actionComp.getNode();
			}
			final GraphItem it = item;
			invisibles.add(it);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					it.setVisible(false);
				}
			});
		}

		private void startBundle() {
			try {
				actionComp.getBundle().start();
			} catch (BundleException e) {
				e.printStackTrace();
			}

		}

		private void renameGraphObj() {
			final AskDialog dialog = new AskDialog(composite.getShell());

			dialog.create();

			if (dialog.open() == Window.OK) {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						item.setText(dialog.getName());

					}
				});
			}

		}

		private void stopBundle() {
			try {
				actionComp.getBundle().stop();
			} catch (BundleException e) {
				e.printStackTrace();
			}

		}
	}

}
