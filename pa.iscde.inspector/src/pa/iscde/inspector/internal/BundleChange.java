package pa.iscde.inspector.internal;

import java.util.HashMap;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import pa.iscde.inspector.gui.ComponentDisign;

public class BundleChange implements BundleListener {

	private HashMap<String, ComponentDisign> bundleDesignMap;
	/**
	 * Create a new {@link BundleChange}
	 * @param bundleDesignMap all the available {@link ComponentDisign}
	 */
	public BundleChange(HashMap<String, ComponentDisign> bundleDesignMap) {
		this.bundleDesignMap = bundleDesignMap;
	}
	/**
	 * catch the bundle state change and update the state on the @param bundleDesignMap
	 */
	@Override
	public void bundleChanged(BundleEvent event) {
		String key = event.getBundle().getSymbolicName();
		if (bundleDesignMap.containsKey(key)) {
			ComponentDisign componentDisign = bundleDesignMap.get(key);
			componentDisign.setBlundeState(event.getType());
		}
	}

}
