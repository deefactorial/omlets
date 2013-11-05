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

import org.openmoney.omlets.mobile.client.model.InitialData;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.utils.RestRequest;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Implementation for {@link AccessService}.
 * 
 * @author luis
 */
public class AccessServiceImpl implements AccessService {
    @Override
    public void login(String username, String password, AsyncCallback<InitialData> callback) {
        RestRequest<InitialData> request = new RestRequest<InitialData>(RequestBuilder.GET, "access/initialData");
        request.setUsername(username);
        request.setPassword(password);
        request.send(callback);
    }
    
    @Override
    public void register(String firstname, String lastname, String username, String email, String password, String password2, AsyncCallback<InitialData> callback) {
        RestRequest<InitialData> request = new RestRequest<InitialData>(RequestBuilder.POST, "access/register");
        Parameters params = new Parameters();
        params.add("firstname",firstname);
        params.add("lastname", lastname);
        params.add("username", username);
        params.add("email", email);
        params.add("password", password);
        params.add("password2", password2);
        request.setParameters(params);
        request.send(callback);
    }
}
