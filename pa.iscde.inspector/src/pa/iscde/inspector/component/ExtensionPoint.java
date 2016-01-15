package pa.iscde.inspector.component;

import pa.iscde.inspector.gui.ComponentDisign;


public class ExtensionPoint {
	
	private String id;
	private String name;
	private String schema;
	private Component owner;
	private ComponentDisign ownerDesign;

	/**
	 * This parameters is getting from plugin.xml file
	 * @param id
	 * @param name
	 * @param schema
	 * @param owner
	 */
	public ExtensionPoint(String id, String name, String schema,Component owner) {
		this.id = id;
		this.name = name;
		this.schema = schema;
		this.owner = owner;
	}
	
	/**
	 * Set the {@link ComponentDisign} the owns this Extension Point
	 * @param componentDisign
	 */
	public void setOwnerDesign(ComponentDisign componentDisign) {
		this.ownerDesign = componentDisign;
	}
	/**
	 * 	 Get the {@link ComponentDesign} the owns this Extension Point
	 * @return
	 */
	public ComponentDisign getOwnerDesign() {
		return ownerDesign;
	}
	/**
	 * Get this extension point ID attribute
	 * @return
	 */
	public String getId() {
		return id;
	}
	/**
	 * Get this extension point name attribute
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get this extension point schema attribute
	 * @return
	 */
	public String getSchema() {
		return schema;
	}
	
	@Override
	public String toString() {
		
		return "ExtensionPoint: id = " + id +" - name = " + name + " - schema = " + schema + "  owner : " + owner.getName();
	}

}
