package extensionPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import pa.iscde.inspector.extensibility.IAction;
/**
 * This class find and construct all the extension that exist for the action extension point
 * @author Jorge Teixeira
 *
 */
public class ExtensionPoint {

	private IExtensionRegistry extRegistry = Platform.getExtensionRegistry();

	private IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pa.iscde.inspector.inspectorAction");

	private List<IAction> iActions = new ArrayList<IAction>();
	private List<String> names = new ArrayList<String>();

	/**
	 * This methos is to find and save all the {@link IAction} extension that exists 
	 * and there names attribute.
	 */
	public void init() {
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				String s = c.getAttribute("name");
				try {
					iActions.add((IAction) c.createExecutableExtension("class"));
					names.add(s);
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	/**
	 * Get all the IActions
	 * @return a collection of {@link IAction}
	 */
	public Collection<IAction> getiActions() {
		return iActions;
	}
	/**
	 * Get all the IAction name atributte
	 * @return a list of names which as the same index as the name owner( {@link IAction}).
	 */
	public List<String> getNames() {
		return names;
	}

}
