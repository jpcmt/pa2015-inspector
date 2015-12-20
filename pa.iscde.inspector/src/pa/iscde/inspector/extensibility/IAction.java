package pa.iscde.inspector.extensibility;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import pa.iscde.inspector.gui.ComponentDisign;
/**
 * 
 * @author Jorge Teixeira
 *
 *This interface has to be implemented by the extension class
 */
public interface IAction{
	
	/**
	 * @return The label name for the action composite
	 */
	String TabName();
	
	/**
	 * @param composite The action composite, in which you can add widgets
	 */
	void actionComposite(Composite composite);

	/**
	 * Activate when the selection event occurs
	 * @param graphObj The object select in the zest composite
	 */
	void selectionChange(IActionComponent graphObj);
	
	/**
	 * when a client unselect the widgets this method get call
	 */
	
	void zeroSelection();
	
	
	
}
