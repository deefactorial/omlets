package nl.strohalm.cyclos.mobile.client.model;


public class Settings extends Entity {

	protected Settings(){
	}
	
    public final native String getError() /*-{
    	return $wnd.cleanString(this.error);
	}-*/;
    
    public final native Long getUACID() /*-{
		return @java.lang.Long::valueOf(Ljava/lang/String;)($wnd.cleanString(this.uacID));
	}-*/;
    
    public final native Long getCurrencyID() /*-{
		return @java.lang.Long::valueOf(Ljava/lang/String;)($wnd.cleanString(this.currencyID));
	}-*/;
}
