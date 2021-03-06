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
package org.openmoney.omlets.mobile.client.payments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openmoney.omlets.mobile.client.LoggedUser;
import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.OmletsMobile;
import org.openmoney.omlets.mobile.client.model.BasicMember;
import org.openmoney.omlets.mobile.client.model.CustomFieldValue;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.model.Transfer;
import org.openmoney.omlets.mobile.client.model.TransferData;
import org.openmoney.omlets.mobile.client.services.PaymentService;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.ui.panels.SquarePanel;
import org.openmoney.omlets.mobile.client.ui.widgets.FormField;
import org.openmoney.omlets.mobile.client.utils.BaseAsyncCallback;
import org.openmoney.omlets.mobile.client.utils.PageAction;
import org.openmoney.omlets.mobile.client.utils.ParameterKey;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to display payment details.
 */
public class PaymentDetailsPage extends Page {
    
    private PaymentService paymentService = GWT.create(PaymentService.class);
    //private ContactService contactService = GWT.create(ContactService.class);
    
    private FormField form;
    private SquarePanel container;    
    private TransferData data;

    @Override
    public String getHeading() {
        return messages.paymentDetailsHeading();
    }
      
    @Override
    public Widget initialize() {
        
        container = new SquarePanel(true);
        container.setVisible(false);
        
        // Create form and sets labels
        form = new FormField();            
        
        Long transferId = getParameters().getRequiredLong(ParameterKey.ID); 
        Boolean pendingPayment = getParameters().getBoolean(ParameterKey.PAYMENT_PENDING);
        Boolean paymentDone = getParameters().getBoolean(ParameterKey.PAYMENT_DONE);

        if(paymentDone != null && paymentDone) {
            Notification.get().success(messages.paymentPerformedSuccessfully());
        } else if(pendingPayment != null && pendingPayment) {
            Notification.get().information(messages.paymentRequireAuthorization());
        }
        
        // Fetch transfer data
        paymentService.getTransferData(transferId, new BaseAsyncCallback<TransferData>() {
            @Override
            public void onSuccess(TransferData result) {
                if(result != null) {
                    data = result;
                    
                    // Load data into form
                    renderTransferData();   
                }                                                    
            }            
        });        
        
        container.add(form);                
        
        return container;
    }
    
    @Override
    public List<PageAction> getPageActions() {
        return null;
    }
    
    /**
     * Returns add to contacts page action
     */
//    private PageAction getAddToContactsAction() {
//        return new PageActionAsync<Contact>() {
//           @Override
//           public void runAsync(BaseAsyncCallback<Contact> callback) {
//               Transfer transfer = data.getTransfer();
//               if(transfer != null && transfer.getBasicMember() != null) {    
//                   contactService.saveContact(transfer.getBasicMember().getId(), callback);                                                   
//               }          
//           }
//           @Override
//            public void onCallbackSuccess(Contact result) {
//               Parameters params = new Parameters();
//               params.add(ParameterKey.ID, result.getMember().getId());
//               params.add(ParameterKey.SAVE, true);
//               Navigation.get().go(PageAnchor.CONTACT_DETAILS, params);
//            }
//            @Override
//            public String getLabel() {                
//                return messages.addToContacts();
//            }         
//        };
//    }
    
    /**
     * Returns new payment action
     */
    private PageAction getNewPaymentAction() {
        return new PageAction() {
            @Override
            public void run() {  
                Parameters params = new Parameters();
                BasicMember member = data.getTransfer().getBasicMember();
                // Set if there is a member payment or system payment
                if(member != null) {                                      
                    params.set(ParameterKey.ID, member.getId());                   
                } else {
                    params.set(ParameterKey.IS_SYSTEM_ACCOUNT, true);
                }
                Navigation.get().go(PageAnchor.MAKE_PAYMENT, params);
            }
            @Override
            public String getLabel() {             
                return messages.newPayment();
            }            
        };
    }
    
    /**
     * Renders the form with the given data
     */
    private void renderTransferData() {
        
        Transfer transfer = data.getTransfer();;        
        JsArray<CustomFieldValue> customFieldValues = transfer.getCustomValues();
        
        String loggedName =  LoggedUser.get().getInitialData().getProfile().getName();
        String mytransferNameFrom = transfer.getTransferType().getFrom().getName();
        String transferName = transfer.getBasicMember() == null ? transfer.getSystemAccountName() : transfer.getBasicMember().getName();

        // Set from and to
        String from = transfer.getAmount() > 0 ? transferName : mytransferNameFrom;
        String to = transfer.getAmount() > 0 ? mytransferNameFrom : transferName;
        
        Map<String, String> formData = new LinkedHashMap<String, String>();
        
        // Add transfer properties
        formData.put(messages.date(), transfer.getFormattedProcessDate());
        formData.put(messages.paymentNumber(), transfer.getTransactionNumber());
        formData.put(messages.from(), from);
        formData.put(messages.to(), to);
        //formData.put(messages.type(), transfer.getTransferType().getName());
        NumberFormat numberFormat = NumberFormat.getFormat("###,###,###.00;-###,###,###.00");
        formData.put(messages.amount(),  numberFormat.format(Double.parseDouble(transfer.getAmount().toString())));
        formData.put("Currency", transfer.getTransferType().getFrom().getCurrency().getName());
        formData.put(messages.description(), transfer.getDescription()); 
        
        // Add custom fields
        if(customFieldValues != null && customFieldValues.length() > 0) {
            for(int i = 0; i < customFieldValues.length(); i++) {
                CustomFieldValue value = customFieldValues.get(i);
                formData.put(value.getDisplayName(), value.getValue());  
            }         
        }   
        form.setData(formData);
        
        List<PageAction> actions = new ArrayList<PageAction>();        
               
        // Add contact action if related member is not a contact yet
        if(data.isCanAddContact()) {
           // actions.add(getAddToContactsAction());
        }
        
        // Add payment action if contact can make member/system payments
        if(transfer.getBasicMember() == null && LoggedUser.get().getInitialData().canMakeSystemPayments()) {
            actions.add(getNewPaymentAction());
        } else if(transfer.getBasicMember() != null && LoggedUser.get().getInitialData().canMakeMemberPayments()) {
            actions.add(getNewPaymentAction());
        }
        
        // Add actions asynchronously
        OmletsMobile.get().getMainLayout().addActions(actions, true);
        
        // Display loaded data
        container.setVisible(true);
    }

}
