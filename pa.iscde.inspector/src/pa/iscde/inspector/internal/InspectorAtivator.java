package pa.iscde.inspector.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.google.common.util.concurrent.Service;

import extensionpoints.ISearchEvent;
import pa.iscde.inspector.component.Component;
import pa.iscde.inspector.component.ComponentData;
import pa.iscde.inspector.component.Extension;
import pa.iscde.inspector.component.ExtensionPoint;
import pa.iscde.inspector.deepsearch.SearchComponent;
import pa.iscde.inspector.gui.ComponentDisign;
import pt.iscte.pidesco.extensibility.PidescoServices;
import pt.iscte.pidesco.javaeditor.internal.JavaEditorActivator;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class InspectorAtivator implements BundleActivator {

	private static InspectorAtivator instance;
	private BundleContext context;
	private HashMap<String, ComponentData> bundlemap;
	private HashMap<String, ComponentDisign> bundleDesignMap;
	private JavaEditorServices javaEditorService;
	private ISearchEvent searchEventService;


	public HashMap<String, ComponentData> getBundlemap() {
		return bundlemap;
	}

	public BundleContext getContext() {
		return context;
	}

	public static InspectorAtivator getInstance() {
		return instance;
	}
	public JavaEditorServices getJavaEditorService() {
		return javaEditorService;
	}
	public ISearchEvent getSearchEventService() {
		return searchEventService;
	}
	public HashMap<String, ComponentDisign> getBundleDesignMap() {
		return bundleDesignMap;
	}
	/**
	 * Start this componet, get the required services, generate un hashMap with available {@link ComponentData} and {@link ComponentDisign}
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		instance = this;
		this.context = context;
		// Get services 
		ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
		javaEditorService = context.getService(serviceReference);
		ServiceReference<ISearchEvent>serviceReference2 = context.getServiceReference(ISearchEvent.class);
		searchEventService = context.getService(serviceReference2);
		
		addBundles();
		createBlundleDisgnMap();
	}

	private void createBlundleDisgnMap() {
		bundleDesignMap = new HashMap<String, ComponentDisign>();
		for (Entry<String, ComponentData> entry : bundlemap.entrySet()) {
			bundleDesignMap.put(entry.getKey(), new ComponentDisign(entry.getValue()));  
		}
		context.addBundleListener(new BundleChange(bundleDesignMap));
		
	}

	private void addBundles() throws ClassNotFoundException {
		Bundle[] bundles = context.getBundles();
		bundlemap = Component.getAllAvailableComponents();
		for (int i = 0; i < bundles.length; i++) {
			String key = bundles[i].getSymbolicName();
			if (bundlemap.containsKey(key)) {
				((Component) bundlemap.get(key)).setBundle(bundles[i]);
			}
		}

	}

	
	/**
	 * Stop this component
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		context = null;

	}



}
