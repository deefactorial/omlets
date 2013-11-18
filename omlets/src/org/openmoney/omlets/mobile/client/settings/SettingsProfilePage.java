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
package org.openmoney.omlets.mobile.client.settings;

import java.util.ArrayList;
import java.util.List;

import org.openmoney.omlets.mobile.client.Configuration;
import org.openmoney.omlets.mobile.client.LoggedUser;
import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.OmletsMobile;
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
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to display accounts.
 * 
 * @author luis
 */
public class SettingsProfilePage extends Page {
	
	private SquarePanel container;
	
    private TextField     firstname;
    private TextField     lastname;
    
    private TextField     principal;
    
    private EmailTextField    email;
    
    private PasswordField password;
    private PasswordField password2;
    
    private LabelField    profileLabel;
    
    private GeneralData   data;    
    private String        principalName;
    
    private PageAction    registerAction;  
	
    
    private AccessService accessService = GWT.create(AccessService.class);
	
    //private JsArray<UserSpace> userSpaces;
    
    @Override
    public String getHeading() {
        //accounts = LoggedUser.get().getInitialData().getAccounts();
        
        return messages.settingsHeader() + " " + LoggedUser.get().getInitialData().getProfile().getName();
    }

    @Override
    public boolean hasCustomScroll() {
        return true;
    }
    
    @Override
    public Widget initialize() {
        
    	data = OmletsMobile.get().getGeneralData();    
        // List pages uses fixed notification layout
        //Notification.get().setLayout(NotificationLayout.FIXED);
        
        if (getParameters() != null) {
	        String success = getParameters().getString(ParameterKey.SUCCESS);
	        String error = getParameters().getString(ParameterKey.ERROR);
	        
	        if ( (success != null) && StringHelper.isNotEmpty(success) ){
	        	Notification.get().success(success);
	        } else if ( (error != null) && StringHelper.isNotEmpty(error)){
	        	Notification.get().error(error);
	        }
        }
        
        FlowPanel settingsPage = new FlowPanel();
        settingsPage.setStyleName("settings-page");
        
        
		FlowPanel rowContainer = new FlowPanel();
		rowContainer.setStyleName("row");
        
        container = new SquarePanel();
        
        profileLabel = new LabelField("Profile Settings : ");
        profileLabel.setStyleName("profile-label");
        
        container.add(profileLabel);
        
        firstname = new TextField("Type your first name here.");
        String firstnameString = LoggedUser.get().getInitialData().getProfile().getFirstName();
        if( firstnameString != null ) {
        	firstname.setValue(firstnameString);
        }
        container.add(firstname);
        lastname = new TextField("Type your last name here.");
        String lastnameString = LoggedUser.get().getInitialData().getProfile().getLastName();
        if( lastnameString != null ) {
        	lastname.setValue(lastnameString);
        }
        container.add(lastname);
        
        principal = new TextField(resolvePrincipalInputMessage());   
        principalName = LoggedUser.get().getInitialData().getProfile().getUsername();
        if(principalName != null) {
            principal.setValue(principalName);
            //principal.setReadOnly(true); // If a principal is already set shows a label instead of text box
        }
        container.add(principal);
        
        email = new EmailTextField("Type your email address here.");
        String emailString = LoggedUser.get().getInitialData().getProfile().getEmail();
        if( emailString != null ) {
        	email.setValue(emailString);
        }
        container.add(email);
        
        password = new PasswordField(resolvePasswordInputMessage());
        container.add(password); 
        
        password2 = new PasswordField("Re-type your password here.");     
        container.add(password2); 
        
        profileLabel = new LabelField("Server : "+Configuration.get().getServerAppUrl());
        profileLabel.setStyleName("register-label");
        container.add(profileLabel);
        
        rowContainer.add(container);
        
        settingsPage.add(rowContainer);
     
        return rowContainer;
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
    

    
    @Override
    public List<PageAction> getPageActions() {
        List<PageAction> actions = new ArrayList<PageAction>();                
        
        // Add login action
        actions.add(getCancelAction());
        
        actions.add(getRegisterAction());
       
        return actions;
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
                    accessService.updateProfile(firstname.getValue(), lastname.getValue(), principal.getValue(), email.getValue(), password.getValue(), password2.getValue(), callback);
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
            		

	            	
		                // Initialise user's session data
		                LoggedUser.get().initialize(principal.getValue(), password.getValue(), result);                                                        
		                
		                Parameters params = new Parameters();
	            		params.add(ParameterKey.SUCCESS, "Successfully Updated Your Profile!");
	            		Navigation.get().go(PageAnchor.SETTINGS, params);
	            	
            	}
            }
           
            @Override
            public String getLabel() {               
                return "Update Profile";
            }
        }; 
        return registerAction;
    }    
    
    private PageAction getCancelAction() {
    	return new PageAction() {
    		
    		@Override
    		public void run() {
    			Navigation.get().go(PageAnchor.SETTINGS);
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
        		StringHelper.isEmpty(principal.getValue())
        		) { 
            Notification.get().error("email and username fields can not be empty");
            return false;
        }
        if( password.getValue().equals(password2) && password.getValue().length() == 0 ) {
        	// passwords are null so leave them alone
        	return true;
        } else {
	        // check password equality
	        if( !password.getValue().equals( password2.getValue() ) ){
	        	Notification.get().error("Passwords are not equal");
	            return false;
	        }

        }

        return true;
    }

}
