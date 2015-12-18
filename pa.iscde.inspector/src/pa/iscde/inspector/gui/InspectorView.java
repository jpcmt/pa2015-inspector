package pa.iscde.inspector.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import pa.iscde.inspector.internal.InspectorAtivator;
import pt.iscte.pidesco.extensibility.PidescoView;

public class InspectorView implements PidescoView {

	private HashMap<String,ComponentDisign> componentDisigns;
	private Composite viewArea;

	public void init() {
		componentDisigns = InspectorAtivator.getInstance().getBundleDesignMap();
		setExtensionPointOwnerDisign();
		new ComponentInfoView(viewArea, componentDisigns.values()).fillInfoView();
	}

	private void setExtensionPointOwnerDisign() {
		for (Entry<String, ComponentDisign> componentDisign : componentDisigns.entrySet()) {
			componentDisign.getValue().setExtensionPointOwnerDesign();
		}
	}

	@Override
	public void createContents(final Composite viewArea, Map<String, Image> imageMap) {
		this.viewArea = viewArea;
		init();
	}

}
