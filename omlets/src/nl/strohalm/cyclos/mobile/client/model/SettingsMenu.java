package nl.strohalm.cyclos.mobile.client.model;

import nl.strohalm.cyclos.mobile.client.ui.PageAnchor;


public class SettingsMenu extends Entity {


	
	protected SettingsMenu(){

	}
	
	public final native String getMenuName() /*-{
		return $wnd.cleanString(this.menuName);
	}-*/;
	
	public final native PageAnchor getPage() /*-{
		return this.page;
	}-*/;
	
	public final native void setMenuName(String menu) /*-{
		this.menuName = menu;
	}-*/;
	
	public final native void setPage(PageAnchor anchor) /*-{
		this.page = anchor;
	}-*/;
}
