package nl.strohalm.cyclos.mobile.client.model;

public class UserSpace extends Entity {
	
	protected UserSpace() {
	}
	
	public final native String getSpaceName() /*-{
    	return $wnd.cleanString(this.space_name);
	}-*/;  

}
