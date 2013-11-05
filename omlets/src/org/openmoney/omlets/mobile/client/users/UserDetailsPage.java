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
package org.openmoney.omlets.mobile.client.users;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openmoney.omlets.mobile.client.LoggedUser;
import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.OmletsMobile;
import org.openmoney.omlets.mobile.client.model.CustomFieldValue;
import org.openmoney.omlets.mobile.client.model.Member;
import org.openmoney.omlets.mobile.client.model.MemberData;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.services.ContactService;
import org.openmoney.omlets.mobile.client.services.UserService;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.ui.panels.SquarePanel;
import org.openmoney.omlets.mobile.client.ui.widgets.EmailField;
import org.openmoney.omlets.mobile.client.ui.widgets.FormField;
import org.openmoney.omlets.mobile.client.utils.BaseAsyncCallback;
import org.openmoney.omlets.mobile.client.utils.PageAction;
import org.openmoney.omlets.mobile.client.utils.ParameterKey;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to display user details.
 */
public class UserDetailsPage extends Page {

    private UserService userService = GWT.create(UserService.class);
    private ContactService contactService = GWT.create(ContactService.class);
    
    private FormField form;    
    private MemberData userData;    
    private Long userId;
    private SquarePanel container;
    
    @Override
    public String getHeading() {       
        return messages.userDetailsHeading();
    }

    @Override
    public Widget initialize() {        
        
        container = new SquarePanel(true);   
        container.setVisible(false);
        
        // Create form and sets labels
        form = new FormField(); 
        
        userId = getParameters().getRequiredLong(ParameterKey.ID);
  
        // Display contact information
        getUserData();
        
        container.add(form);
        
        return container;
    }
       
    /**
     * Retrieves user and renders the form
     */
    private void getUserData() {
        userService.getMemberData(userId, new BaseAsyncCallback<MemberData>() {
            @Override
            public void onSuccess(MemberData result) {
                userData = result;
                renderUserData();
            }            
        });
    }
    
    /**
     * Renders form data
     */
    private void renderUserData() {
                
        Member member = userData.getMember();
        JsArray<CustomFieldValue> customFieldValues = null;
        
        Map<String, String> formData = new LinkedHashMap<String, String>();
        
        // Add member properties
        if(member != null) {
            formData.put(messages.username(), member.getUsername());
            formData.put(messages.name(), member.getName());  
            formData.put(messages.email(), new EmailField(member.getEmail()).getHTML());
            customFieldValues = member.getCustomValues();
        }          

        // Add custom fields
        if(customFieldValues != null && customFieldValues.length() > 0) {
            for(int i = 0; i < customFieldValues.length(); i++) {
                CustomFieldValue value = customFieldValues.get(i);
                formData.put(value.getDisplayName(), value.getValue());  
            }         
        }   
        
        form.setData(formData);
        
        List<PageAction> actions = null;
       
        // Check if is not the logged user because himself cannot be added as contact and cannot make a payment to himself
        if(!LoggedUser.get().getUserId().equals(userId)) {
            actions = new ArrayList<PageAction>();            
            // Add contact action if related member is not a contact yet        
            if(userData.isCanAddMemberAsContact()) {
                //actions.add(getAddToContactsAction()); 
            }
            if(LoggedUser.get().getInitialData().canMakeMemberPayments()) {
                actions.add(getMakePaymentAction());
            }
        }
            
        // Add actions asynchronously
        OmletsMobile.get().getMainLayout().addActions(actions, true);
        
        // Display loaded data
        container.setVisible(true);
    }
        
    /**
     * Returns make payment page action
     */
    private PageAction getMakePaymentAction() {
        return new PageAction() {
            @Override
            public void run() {
                Parameters params = new Parameters();
                params.set(ParameterKey.ID, userId);
                Navigation.get().go(PageAnchor.MAKE_PAYMENT, params);
            }
            @Override
            public String getLabel() {                
                return messages.makePayment();
            }           
        };
    }
    
//    /**
//     * Returns add contact page action    
//     */
//    private PageAction getAddToContactsAction() {
//        return new PageActionAsync<Contact>() {
//            @Override
//            public void runAsync(BaseAsyncCallback<Contact> callback) {
//                contactService.saveContact(userId, callback);
//            }
//            @Override
//            public void onCallbackSuccess(Contact result) {
//                Parameters params = new Parameters();
//                params.add(ParameterKey.ID, result.getMember().getId());
//                params.add(ParameterKey.SAVE, true);
//                //Navigation.get().go(PageAnchor.CONTACT_DETAILS, params);
//            }
//            @Override
//            public String getLabel() {
//                return messages.addToContacts();
//            }
//        };        
//    }

}
