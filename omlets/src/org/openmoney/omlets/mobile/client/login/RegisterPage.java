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
import java.util.Arrays;
import java.util.List;

import org.openmoney.omlets.mobile.client.Configuration;
import org.openmoney.omlets.mobile.client.LoggedUser;
import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.OmletsMobile;
import org.openmoney.omlets.mobile.client.model.AppState;
import org.openmoney.omlets.mobile.client.model.CustomField;
import org.openmoney.omlets.mobile.client.model.GeneralData;
import org.openmoney.omlets.mobile.client.model.InitialData;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.services.AccessService;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.ui.panels.SquarePanel;
import org.openmoney.omlets.mobile.client.ui.widgets.EmailTextField;
import org.openmoney.omlets.mobile.client.ui.widgets.LabelField;
import org.openmoney.omlets.mobile.client.ui.widgets.PasswordField;
import org.openmoney.omlets.mobile.client.ui.widgets.TextField;
import org.openmoney.omlets.mobile.client.utils.BaseAsyncCallback;
import org.openmoney.omlets.mobile.client.utils.PageAction;
import org.openmoney.omlets.mobile.client.utils.PageActionAsync;
import org.openmoney.omlets.mobile.client.utils.ParameterKey;
import org.openmoney.omlets.mobile.client.utils.Storage;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to show the login form.
 * 
 * @author luis
 */
public class RegisterPage extends Page {

    private AccessService accessService = GWT.create(AccessService.class);
    
    
    private TextField     principal;
    private EmailTextField    email;
    
    private PasswordField password;
    private PasswordField password2;
    private LabelField    loginLabel;
    private GeneralData   data;    
    private String        principalName;
    private PageAction    registerAction;    

    @Override
    public String getHeading() {
        return "Register";
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
               
        data = OmletsMobile.get().getGeneralData();                
        
        // Get storage data
        principalName = Storage.get().getItem("principal");          
                
        SquarePanel container = new SquarePanel(); 
        container.addStyleName("register");
                        
        loginLabel = new LabelField("Register as : ");
        loginLabel.setStyleName("register-label");
        
        container.add(loginLabel);
       
        
        principal = new TextField(resolvePrincipalInputMessage());        
        
        
        if(principalName != null) {
            principal.setValue(principalName);
            //principal.setReadOnly(true); // If a principal is already set shows a label instead of text box
        }
        container.add(principal);
        
        email = new EmailTextField("Type your email address here.");
        container.add(email);
        
        password = new PasswordField(resolvePasswordInputMessage());
        container.add(password); 
        
        password2 = new PasswordField("Re-type your password here.");     
        container.add(password2); 
        
        loginLabel = new LabelField("Server : "+Configuration.get().getServerAppUrl());
        loginLabel.setStyleName("register-label");
        
        container.add(loginLabel);
        
        // If user session has expired, display a message
        if(LoggedUser.get().isSessionExpired()) {
            Notification.get().information(messages.sessionExpired());
        }     
        
        // If user presses back button on this page, exit application        
        Navigation.get().exitOnHistoryChange();
        
        return container;
    }
    
    @Override
    public List<PageAction> getPageActions() {
        List<PageAction> actions = new ArrayList<PageAction>();                
        
        // Add login action
        actions.add(getCancelAction());
        
        actions.add(getRegisterAction());
       
        return actions;
    }
    
    /**
     * Resolves principal input's message
     */
    private String resolvePrincipalInputMessage() {        
        switch(data.getPrincipalType()) {
            case USER:
                return messages.typeYourUsernameHere();
            case CARD:               
                return messages.typeYourCardHere();
            case CUSTOM_FIELD:
                CustomField customPrincipal = data.getPrincipalCustomField();
                return messages.typeYourCustomFieldHere(customPrincipal.getDisplayName());
            case EMAIL:
                return messages.typeYourEmailHere();
        }
        return "";       
    }       
    
    /**
     * Resolves password input's message
     */
    private String resolvePasswordInputMessage() {
        switch(data.getCredentialType()) {
            case LOGIN_PASSWORD:
                return messages.typeYourPasswordHere();
            case CARD_SECURITY_CODE:               
                return messages.typeYourCardSecurityCodeHere();
            case PIN:
                return messages.typeYourPinHere();
            case TRANSACTION_PASSWORD:
                return messages.typeYourTransactionPasswordHere();
        }
        return "";      
    }
        
    /**
     * Returns login page action
     */
    private PageAction getRegisterAction() {
       registerAction = new PageActionAsync<InitialData>() {        
            @Override
            public void runAsync(BaseAsyncCallback<InitialData> callback) {
                // Validate input
                if(validate()) {                
                    // Execute user login
                    accessService.register( principal.getValue(), email.getValue(), password.getValue(), password2.getValue(), callback);
                } else {
                	this.cancelCallback();
                }
                
            } 
            
            @Override
            protected void onCallbackSuccess(InitialData result) {
            	if (!result.getErrorDetails().isEmpty()) {
            		Notification.get().hide();
            		Notification.get().error(result.getErrorDetails());
            	} else {
            		
	            	if (result.getAccounts().length() == 0){
	            		//Go to login page and display a successful registration message. approval pending.
	            		
	            		Parameters params = new Parameters();
	            		params.add(ParameterKey.SUCCESS, "Successfully Registered an account!<br/>You will recieve an email notification upon approval.");
	            		Navigation.get().go(PageAnchor.LOGIN, params);
	            		
	            	} else {
	            	
		                // Initialise user's session data
		                LoggedUser.get().initialize(principal.getValue(), password.getValue(), result);                                                        
		                
		                // Load initial page
		                Navigation.get().loadInitialPage();  
	            	}
            	}
            }
           
            @Override
            public String getLabel() {               
                return "Register";
            }
        }; 
        return registerAction;
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
	        		StringHelper.isEmpty(email.getValue()) ||
	        		StringHelper.isEmpty(principal.getValue()) || 
	        		StringHelper.isEmpty(password.getValue()) ||
	        		StringHelper.isEmpty(password2.getValue())) {
	            Notification.get().error("Registration fields cannot be empty");
	            return false;
	        }
	        
	        // check password equality
	        if( !password.getValue().equals( password2.getValue() ) ){
	        	Notification.get().error("Passwords are not equal");
	            return false;
	        }
	        
	        // password size
	        if( password.getValue().length() < 2 ) {
	        	Notification.get().error("Password is too short!");
	            return false;
	        }
	
	        return true;
	 }
}
