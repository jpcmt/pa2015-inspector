package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.GraphItem;

import pa.iscde.inspector.extensibility.IAction;
import pa.iscde.inspector.extensibility.IActionComponent;

public class Test  implements IAction{

	private Composite composite;
	private List<GraphItem> items = new ArrayList<GraphItem>();;
	private Button invisible;

	@Override
	public void actionComposite(Composite composite) {
		// get the composite
		composite.setLayout(new GridLayout(3, false));
		this.composite = composite;
		
	}

	@Override
	public void selectionChange(Collection<IActionComponent> items) {
		// clear the items selected before
		if(!this.items.isEmpty())
			this.items.clear();
		//dispose the buttons in last selection
		clearButtons();
		// When selection change
		
		if(!items.isEmpty()){
			// for every item selected
			for (IActionComponent iActionComponent : items) {
				this.items.add(iActionComponent.getGraphItem());

			}
			addTheInvisibleButton();
		}
		
	}

	private void clearButtons() {
		if(invisible != null){
			invisible.dispose();
		}
		
	}

	private void addTheInvisibleButton() {
		invisible = new Button(composite, SWT.BUTTON1);
		invisible.setText("invisible");
		// add the listener
		invisible.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// for every item 
				for (final GraphItem graphItem : items) {
					//make them invisible
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							graphItem.setVisible(false);
						}
					});
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		composite.layout();
	}

}
