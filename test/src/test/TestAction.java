package test;

import pa.iscde.inspector.extensibility.IAction;
import pa.iscde.inspector.extensibility.IActionComponent;
import pa.iscde.inspector.gui.ComponentDisign;

import java.awt.PopupMenu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleException;

public class TestAction implements IAction {

	Composite composite;
	IActionComponent actionComp;
	List<ActionButton> buttons = new ArrayList<ActionButton>();
	private ButtonListener buttonListener = new ButtonListener();

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

	private void handle() {
		if (!buttons.isEmpty())
			disposeAll();
		switch (actionComp.getType()) {
		case CONNECTION:
			handleConnection();
			break;
		case NODE:
			handleNode();
		default:
			break;
		}
	}

	private void handleNode() {
		if(actionComp.hasBundle()){
			ActionButton stop = new ActionButton(composite,SWT.BUTTON1, ButtonType.STOP,"stop");
			stop.addListener(buttonListener);
			buttons.add(stop);
		}
		ActionButton rename = new ActionButton(composite,SWT.BUTTON1, ButtonType.STOP,"rename");
		rename.addListener(buttonListener);
		buttons.add(rename);
		
		composite.layout();

	}

	private void handleConnection() {
		ActionButton rename = new ActionButton(composite,SWT.BUTTON1, ButtonType.STOP,"rename");
		rename.addListener(buttonListener);
		buttons.add(rename);
		composite.layout();
	}

	private void disposeAll() {
		for (ActionButton button : buttons) {
			button.dispose();
		}
		buttons.clear();
	}
	class ButtonListener implements Listener{

		@Override
		public void handleEvent(Event event) {
			Button b = (Button) event.widget;
			ActionButton actionButton = null;
			for (ActionButton actionButt : buttons) {
				actionButton =(actionButt.HasThisButton(b))? actionButt: null;
			}
			switch (actionButton.type) {
			case STOP:
				stopBundle();
				break;
			case RENAME:
				renameGraphObj();

			default:
				break;
			}
		}

		private void renameGraphObj() {
			Dialog dialog = new Dialog( composite.getShell()){
				
			
			};
	        GridLayout layout = (GridLayout) composite.getLayout();
	        layout.numColumns = 2;

	        Label nameLabel = new Label(composite, SWT.RIGHT);
	        nameLabel.setText("Name: ");
	       Text  name = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);

	        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
	        name.setLayoutData(data);

			
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
