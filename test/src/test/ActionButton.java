package test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import test.TestAction.ButtonListener;

public class ActionButton {
	ButtonType type;
	Button button;
	
	public ActionButton(Composite parent, int style,ButtonType type, String string) {
		button = new Button(parent, style);
		this.type  = type;
		button.setText(string);
	}
	
	public ButtonType getType() {
		return type;
	}
	

	public void addListener(ButtonListener listener) {
		button.addListener(SWT.Selection, listener);
	}

	public void dispose() {
		button.dispose();
	}

	public boolean HasThisButton(Button b) {
		
		return b.equals(button);
	}


}
