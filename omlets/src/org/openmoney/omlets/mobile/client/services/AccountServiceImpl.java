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

import org.openmoney.omlets.mobile.client.model.AccountData;
import org.openmoney.omlets.mobile.client.model.AccountStatus;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.model.Transfer;
import org.openmoney.omlets.mobile.client.utils.RestRequest;
import org.openmoney.omlets.mobile.client.utils.ResultPage;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Implementation for {@link AccountService}.
 */
public class AccountServiceImpl implements AccountService {

    @Override
    public void searchAccountHistory(Long accountId, Parameters parameters, AsyncCallback<ResultPage<Transfer>> callback) {
        RestRequest<ResultPage<Transfer>> request = new RestRequest<ResultPage<Transfer>>(RequestBuilder.GET, "accounts/"+accountId+"/history", parameters);                              
        request.sendAuthenticated(callback);
    }

    @Override
    public void getAccountsData(Parameters parameters, AsyncCallback<ResultPage<AccountData>> callback) {
        RestRequest<ResultPage<AccountData>> request = new RestRequest<ResultPage<AccountData>>(RequestBuilder.GET, "accounts/info", parameters);        
        request.sendAuthenticated(callback);
    }

    @Override
    public void getAccountStatus(Long accountId, AsyncCallback<AccountStatus> callback) {
        RestRequest<AccountStatus> request = new RestRequest<AccountStatus>(RequestBuilder.GET, "accounts/"+accountId+"/status");        
        request.sendAuthenticated(callback);
    }   

}
