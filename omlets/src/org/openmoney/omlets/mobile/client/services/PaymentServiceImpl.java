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
import org.openmoney.omlets.mobile.client.model.PaymentConfirm;
import org.openmoney.omlets.mobile.client.model.PaymentData;
import org.openmoney.omlets.mobile.client.model.PaymentPreview;
import org.openmoney.omlets.mobile.client.model.TransferData;
import org.openmoney.omlets.mobile.client.utils.RestRequest;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Implementation for {@link PaymentService}.
 */
public class PaymentServiceImpl implements PaymentService {
    
    @Override
    public void getTransferData(Long transferId, AsyncCallback<TransferData> callback) {
       RestRequest<TransferData> request = new RestRequest<TransferData>(RequestBuilder.GET, "accounts/transferData/"+transferId);
       request.sendAuthenticated(callback);
    }

    @Override
    public void getPaymentData(Parameters params, AsyncCallback<PaymentData> callback) {
        RestRequest<PaymentData> request = new RestRequest<PaymentData>(RequestBuilder.GET, "payments/paymentData", params);
        request.sendAuthenticated(callback);
    }

    @Override
    public void getPaymentPreview(boolean isSystem, Parameters params, AsyncCallback<PaymentPreview> callback) {
       RestRequest<PaymentPreview> request = new RestRequest<PaymentPreview>(RequestBuilder.POST, "payments/" + (isSystem ? "systemPayment" : "memberPayment"), params);
       request.sendAuthenticated(callback);
    }

    @Override
    public void doPayment(boolean isSystem, Parameters params, AsyncCallback<PaymentConfirm> callback) {
        RestRequest<PaymentConfirm> request = new RestRequest<PaymentConfirm>(RequestBuilder.POST, "payments/" + (isSystem ? "confirmSystemPayment" : "confirmMemberPayment"), params);
        request.sendAuthenticated(callback);
    }   

}
