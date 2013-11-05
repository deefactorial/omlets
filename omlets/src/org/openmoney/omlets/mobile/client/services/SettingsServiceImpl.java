/*
   This file is part of omlets.

   omlets is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   omlets is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with omlets; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 */
package org.openmoney.omlets.mobile.client.services;

import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.model.Settings;
import org.openmoney.omlets.mobile.client.model.UserSpace;
import org.openmoney.omlets.mobile.client.utils.RestRequest;

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
