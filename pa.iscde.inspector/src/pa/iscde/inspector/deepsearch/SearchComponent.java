package pa.iscde.inspector.deepsearch;

import java.util.Collection;
import java.util.HashMap;

import extensionpoints.ISearchEvent;
import extensionpoints.ISearchEventListener;
import pa.iscde.inspector.component.Extension;
import pa.iscde.inspector.component.ExtensionPoint;
import pa.iscde.inspector.gui.ComponentDisign;
import pa.iscde.inspector.internal.InspectorAtivator;

public class SearchComponent {

	private ISearchEvent searchEvent;
	private HashMap<String, ComponentDisign> bundleDesignMap;
	private ComponentDisign componentReacted;

	public SearchComponent() {
		searchEvent = InspectorAtivator.getInstance().getSearchEventService();
		bundleDesignMap = InspectorAtivator.getInstance().getBundleDesignMap();
		monitorizeSearch();
	}

	private void monitorizeSearch() {
		searchEvent.addListener(new ISearchEventListener() {

			@Override
			public void searchEvent(String text_Search, String text_SearchInCombo, String specificText_SearchInCombo,
					String text_SearchForCombo, Collection<String> buttonsSelected_SearchForCombo) {

				searchMatch(text_Search);
			}
		});
	}

	protected void searchMatch(String text_Search) {
		if (componentReacted != null)
			componentReacted.getNode().unhighlight();
		if (!matchWithSymbolicName(text_Search))
			if (!matchWithBundleName(text_Search))
				if(!matchWithExtensionPoint(text_Search))
					if(!matchWithExtension(text_Search))
						componentReacted = null;
		

	}

	private boolean matchWithExtension(String text_Search) {
		for (ComponentDisign componentDisign : bundleDesignMap.values()) {
			for (Extension extension : componentDisign.getComponentData().getExtensions()) {
				if (matchWithExtension(extension, text_Search)) {
					reactToTheSearch(componentDisign);
					return true;
				}
			}
		}
		return false;
	}

	private boolean matchWithExtension(Extension extension, String text_Search) {
		if (compareString(extension.getName(), text_Search))
			return true;
		if (compareString(extension.getId(), text_Search))
			return true;

		return compareString(extension.getPoint(), text_Search);
	}

	private boolean matchWithExtensionPoint(String text_Search) {
		for (ComponentDisign componentDisign : bundleDesignMap.values()) {
			for (ExtensionPoint extensionPoint : componentDisign.getComponentData().getExtensionPoints()) {
				if (mathWithExtensionPoint(extensionPoint, text_Search)) {
					reactToTheSearch(componentDisign);
					return true;
				}
			}
		}
		return false;
	}

	private boolean mathWithExtensionPoint(ExtensionPoint extensionPoint, String text_Search) {
		if (compareString(extensionPoint.getName(), text_Search))
			return true;
		if (compareString(extensionPoint.getId(), text_Search))
			return true;

		return compareString(extensionPoint.getSchema(), text_Search);
	}

	private boolean compareString(String name, String text_Search) {
		return name.toLowerCase().equals(text_Search.toLowerCase());
	}

	private boolean matchWithBundleName(String text_Search) {
		for (ComponentDisign componentDisign : bundleDesignMap.values()) {
			if (compareString(componentDisign.getComponentData().getName(), text_Search)) {
				reactToTheSearch(componentDisign);
				return true;
			}
		}
		return false;
	}

	private boolean matchWithSymbolicName(String text_Search) {
		for (String symbolicName : bundleDesignMap.keySet()) {
			if (compareString(symbolicName, text_Search)) {
				System.out.println("Match: " + symbolicName);
				reactToTheSearch(bundleDesignMap.get(symbolicName));
				return true;
			}
		}
		return false;
	}

	private void reactToTheSearch(ComponentDisign componentDisign) {
		componentReacted = componentDisign;
		componentReacted.getNode().highlight();
	}

}
