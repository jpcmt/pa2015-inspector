package pa.iscde.inspector.component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.equinox.internal.app.MainApplicationLauncher;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class Component implements ComponentData {

	private String Name;
	private List<Extension> extensions;
	private List<ExtensionPoint> extensionPoints;
	private List<String> requiredBundle;
	private List<File> services = new ArrayList<File>();
	private String symbolicName;
	private Bundle bundle;
	private static List<ExtensionPoint> EXTPOINTS = new ArrayList<ExtensionPoint>();

	public static HashMap<String, ComponentData> getAllAvailableComponents() {
		List<FilesToRead> filesToReadPaths = new FileReader().getFilesPaths();
		HashMap<String, ComponentData> components = new HashMap<String, ComponentData>();
		for (FilesToRead path : filesToReadPaths) {
			Component comp = new Component();
			ManifestParser manifestParser = new ManifestParser().readFile(new File(path.getManifestPath()));

			comp.Name = manifestParser.getName();
			comp.requiredBundle = manifestParser.getRequiredBundle();
			comp.symbolicName = manifestParser.getSymbolicName();
			PluginXmlParser pluginXmlParser = new PluginXmlParser().ReadFile(new File(path.getPluginXmlPath()), comp);

			comp.extensions = pluginXmlParser.getExtension();
			comp.extensionPoints = pluginXmlParser.getExtensionPoint();

			EXTPOINTS.addAll(comp.extensionPoints);

			components.put(manifestParser.getSymbolicName(), comp);
		}
		for (Entry<String, ComponentData> component : components.entrySet()) {
			for (Extension extension : component.getValue().getExtensions()) {
				extension.findConnections(EXTPOINTS);
			}
		}
		return components;
	}

	public Component(String manifestPath, String pluginXmlPath) {
		ManifestParser parser = new ManifestParser().readFile(new File(manifestPath));
		PluginXmlParser pluginXmlParser = new PluginXmlParser().ReadFile(new File(manifestPath), this);
		extensions = pluginXmlParser.getExtension();
		extensionPoints = pluginXmlParser.getExtensionPoint();
		Name = parser.getName();
		requiredBundle = parser.getRequiredBundle();
	}

	public Component() {
	}

	@Override
	public List<Extension> getExtensions() {
		return extensions;
	}

	@Override
	public String getName() {
		return Name;
	}

	@Override
	public List<ExtensionPoint> getExtensionPoints() {
		return Collections.unmodifiableList(extensionPoints);
	}

	public List<String> getRequiredBundle() {
		return requiredBundle;
	}

	@Override
	public List<File> getServices() {
		return services;
	}

	@Override
	public String getSymbolicName() {

		return symbolicName;
	}

	@Override
	public Bundle getBundle() {

		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
		ServiceReference<?>[] registeredServices = bundle.getRegisteredServices();
		if (registeredServices != null)
			verifiedRegistedService(registeredServices);
	}

	private void verifiedRegistedService(ServiceReference<?>[] registeredServices) {

		for (int i = 0; i < registeredServices.length; i++) {
			String text = registeredServices[i].toString();
			text = text.substring(text.indexOf('{') + 1, text.indexOf('}'));
			Class<?> clazz = null;
			try {
				ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
				clazz = contextClassLoader.loadClass(text);
				File f = findClassFile(
						new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath()).listFiles(),
						clazz);
				services.add(f);
			} catch (ClassNotFoundException e) {
				break;
			}
		}

	}

	private File findClassFile(File[] files, Class<?> clazz) {
		for (int i = 0; i < files.length; i++) {

			if (files[i].isFile() && files[i].getName().equals(clazz.getSimpleName() + ".java")) {
				return files[i];
			}
			if (files[i].isDirectory()) {
				File f = findClassFile(files[i].listFiles(), clazz);
				if (f != null)
					return f;
			}
		}
		return null;
	}

}
