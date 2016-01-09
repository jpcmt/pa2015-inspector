package pa.iscde.inspector.extensibility;

import java.util.Collection;

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
	 * @param composite The action composite, in which you can add widgets
	 */
	void actionComposite(Composite composite);

	/**
	 * Activate when the selection event occurs
	 * @param graphObj The object select in the zest composite
	 */
	void selectionChange(Collection<IActionComponent> graphObjs);
	
	
	
	
	
}
