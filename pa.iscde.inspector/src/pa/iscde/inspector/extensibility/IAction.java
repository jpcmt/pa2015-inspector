package pa.iscde.inspector.extensibility;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

import pa.iscde.inspector.gui.ComponentDisign;
/**
 * 
 * @author Jorge Teixeira
 *
 *This interface represents the supported extension type of object.
 *The object receive a composite where you can introduce {@link Widget} and react to the user
 *selection to make an action in the {@link IActionComponent} select 
 */
public interface IAction{
	
	
	
	/**Call to every extension immediate after the creation
	 * @param composite The action composite the control a {@link TabItem}, in which you can add widgets
	 */
	void actionComposite(Composite composite);

	/**
	 * Activate when the selection event occurs
	 * @param items The 0 or more items select in the Zest Graph
	 */
	void selectionChange(Collection<IActionComponent> items);
	
	
	
	
	
}
