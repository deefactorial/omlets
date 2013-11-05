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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface which encapsulates operations in the /access rest services.
 * 
 * @author luis
 */
public interface AccessService {

    /**
     * Actually invokes /getInitialData, but using the given username and password to attempt authentication
     */
    void login(String username, String password, AsyncCallback<InitialData> callback);
    
    /**
     * user registration
     */
    void register(String firstname, String lastname, String username, String email, String password, String password2, AsyncCallback<InitialData> callback);


}
