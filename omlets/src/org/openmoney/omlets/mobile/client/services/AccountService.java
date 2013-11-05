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
import org.openmoney.omlets.mobile.client.utils.ResultPage;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface which encapsulates operations in the /accounts rest services.
 */
public interface AccountService {

    /**
     * Searches for account history of an specific account
     */
    void searchAccountHistory(Long accountId, Parameters parameters, AsyncCallback<ResultPage<Transfer>> callback);
    
    /**
     * Get all accounts with their status
     */
    void getAccountsData(Parameters parameters, AsyncCallback<ResultPage<AccountData>> callback);
    
    /**
     * Get an account status by id
     */
    void getAccountStatus(Long accountId, AsyncCallback<AccountStatus> callback);
       
}
