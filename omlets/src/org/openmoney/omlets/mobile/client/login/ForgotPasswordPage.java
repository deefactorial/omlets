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
package org.openmoney.omlets.mobile.client.login;

import java.util.ArrayList;
import java.util.List;

import org.openmoney.omlets.mobile.client.Configuration;
import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.model.AppState;
import org.openmoney.omlets.mobile.client.model.ForgotPasswordConfirm;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.services.AccessService;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.ui.panels.SquarePanel;
import org.openmoney.omlets.mobile.client.ui.widgets.EmailTextField;
import org.openmoney.omlets.mobile.client.ui.widgets.LabelField;
import org.openmoney.omlets.mobile.client.utils.BaseAsyncCallback;
import org.openmoney.omlets.mobile.client.utils.PageAction;
import org.openmoney.omlets.mobile.client.utils.PageActionAsync;
import org.openmoney.omlets.mobile.client.utils.ParameterKey;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to show the login form.
 * 
 * @author luis
 */
public class ForgotPasswordPage extends Page {

    private AccessService accessService = GWT.create(AccessService.class);

    private EmailTextField    email;
    
    private LabelField    forgotPasswordLabel;
    //private GeneralData   data;
    private PageAction    resetPassword;    

    @Override
    public String getHeading() {
        return "Forgotten Password";
    }

    @Override
    public AppState getMinimumAppState() {
        return AppState.NO_LOGGED_USER;
    }
    
    @Override
    public boolean isDisplayMenu() {
        return false;
    }      

    @Override
    public Widget initialize() {
               
        //data = OmletsMobile.get().getGeneralData();
                
        SquarePanel container = new SquarePanel(); 
        container.addStyleName("forgot-password");
                        
        forgotPasswordLabel = new LabelField("Forgotten Password : ");
        forgotPasswordLabel.setStyleName("forgot-password-label");
        
        container.add(forgotPasswordLabel);
        
        email = new EmailTextField("Type your email address or username here.");
        container.add(email);
        
        forgotPasswordLabel = new LabelField("Server : " + Configuration.get().getServerAppUrl());
        forgotPasswordLabel.setStyleName("register-label");
        
        container.add(forgotPasswordLabel);
        
        return container;
    }
    
    @Override
    public List<PageAction> getPageActions() {
        List<PageAction> actions = new ArrayList<PageAction>();                
        
        // Add login action
        actions.add(getCancelAction());
        
        actions.add(getForgotPasswordAction());
       
        return actions;
    }
    
    /**
     * Resolves principal input's message
     */
//    private String resolvePrincipalInputMessage() {        
//        switch(data.getPrincipalType()) {
//            case USER:
//                return messages.typeYourUsernameHere();
//            case CARD:               
//                return messages.typeYourCardHere();
//            case CUSTOM_FIELD:
//                CustomField customPrincipal = data.getPrincipalCustomField();
//                return messages.typeYourCustomFieldHere(customPrincipal.getDisplayName());
//            case EMAIL:
//                return messages.typeYourEmailHere();
//        }
//        return "";       
//    }       
    

        
    /**
     * Returns login page action
     */
    private PageAction getForgotPasswordAction() {
       resetPassword = new PageActionAsync<ForgotPasswordConfirm>() {        
            @Override
            public void runAsync(BaseAsyncCallback<ForgotPasswordConfirm> callback) {
                // Validate input
                if(validate()) {                
                    // Execute user login
                    accessService.forgotPassword(email.getValue(), callback);
                } else {
                	this.cancelCallback();
                }
                
            } 
            
            @Override
            protected void onCallbackSuccess(ForgotPasswordConfirm result) {
            	if (!result.getErrorDetails().isEmpty()) {
            		Notification.get().hide();
            		Notification.get().error(result.getErrorDetails());
            	} else {
            		
	            	if (result.isEmailSent()){
	            		//Go to login page and display a successful registration message. approval pending.
	            		
	            		Parameters params = new Parameters();
	            		params.add(ParameterKey.SUCCESS, "Successfully sent password reset link to your email!");
	            		Navigation.get().go(PageAnchor.LOGIN, params);
	            		
	            	} else {
	            		// should not get here, either there will be an error or email will be sent.
	            		Notification.get().hide();
	            		Notification.get().error(result.getErrorDetails());
	            	}
            	}
            }
           
            @Override
            public String getLabel() {               
                return "Reset Password";
            }
        }; 
        return resetPassword;
    }    
    
    private PageAction getCancelAction() {
    	return new PageAction() {
    		
    		@Override
    		public void run() {
    			Navigation.get().go(PageAnchor.LOGIN);
    		}
    		
    		@Override
    		public String getLabel() {
    			return "Cancel";
    		}
    	};
    }
          
    /**
     * Client side input validation
     */
    private boolean validate() {
        // Hide previous notifications
        Notification.get().hide();
        
        // Validate all feilds
        if(
        		StringHelper.isEmpty(email.getValue())) {
            Notification.get().error("Forgotten password field cannot be empty");
            return false;
        }

        return true;
    }
}
