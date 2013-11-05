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

import org.openmoney.omlets.mobile.client.model.BasicMember;
import org.openmoney.omlets.mobile.client.model.MemberData;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.utils.RestRequest;
import org.openmoney.omlets.mobile.client.utils.ResultPage;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Implementation for {@link UserService}.
 */
public class UserServiceImpl implements UserService {

    @Override
    public void searchMembers(Parameters parameters, AsyncCallback<ResultPage<BasicMember>> callback) {
        RestRequest<ResultPage<BasicMember>> request = new RestRequest<ResultPage<BasicMember>>(RequestBuilder.GET, "members", parameters);                      
        request.sendAuthenticated(callback);
    }

    @Override
    public void getMemberData(Long memberId, AsyncCallback<MemberData> callback) {
        RestRequest<MemberData> request = new RestRequest<MemberData>(RequestBuilder.GET, "members/memberData/"+memberId);                      
        request.sendAuthenticated(callback);
    }

}
//10-15 23:46:13.577: I/Web Console(16885): RestRequest:GET,url:http://openmoney.proudleo.com/rest/payments/paymentData?destination=MEMBER&toMemberId=593 at file:///android_asset/www/js/library.js:434
