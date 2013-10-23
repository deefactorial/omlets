package nl.strohalm.cyclos.mobile.client.services;

import nl.strohalm.cyclos.mobile.client.model.Parameters;
import nl.strohalm.cyclos.mobile.client.model.Settings;
import nl.strohalm.cyclos.mobile.client.model.UserSpace;
import nl.strohalm.cyclos.mobile.client.utils.RestRequest;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SettingsServiceImpl implements SettingsService {

	@Override
	public void getSettings(AsyncCallback<JsArray<UserSpace>> callback) {
		 RestRequest<JsArray<UserSpace>> request = new RestRequest<JsArray<UserSpace>>(RequestBuilder.GET, "settings/");        
	     request.sendAuthenticated(callback);
	}

    @Override
    public void postSettings(Parameters params, AsyncCallback<Settings> callback) {
        RestRequest<Settings> request = new RestRequest<Settings>(RequestBuilder.POST, "settings/", params);
        request.sendAuthenticated(callback);
    }   
	
}
