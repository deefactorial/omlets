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

import org.openmoney.omlets.mobile.client.LoggedUser;
import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.OmletsMobile;
import org.openmoney.omlets.mobile.client.model.AppState;
import org.openmoney.omlets.mobile.client.model.CustomField;
import org.openmoney.omlets.mobile.client.model.GeneralData;
import org.openmoney.omlets.mobile.client.model.InitialData;
import org.openmoney.omlets.mobile.client.services.AccessService;
import org.openmoney.omlets.mobile.client.ui.Icon;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.ui.panels.SquarePanel;
import org.openmoney.omlets.mobile.client.ui.widgets.HyperLink;
import org.openmoney.omlets.mobile.client.ui.widgets.IconLabel;
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to show the login form.
 * 
 * @author luis
 */
public class LoginPage extends Page {

    private AccessService accessService = GWT.create(AccessService.class);
    
    private TextField     principal;
    private PasswordField password; 
    private LabelField    loginLabel;
    private GeneralData   data;    
    private String        principalName;
    private PageAction    loginAction;    

    @Override
    public String getHeading() {
        return messages.loginHeading();
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
    	
    	
        if (getParameters() != null) {
	        String success = getParameters().getString(ParameterKey.SUCCESS);
	        String error = getParameters().getString(ParameterKey.ERROR);
	        
	        if ( (success != null) && StringHelper.isNotEmpty(success) ){
	        	Notification.get().success(success);
	        } else if ( (error != null) && StringHelper.isNotEmpty(error)){
	        	Notification.get().error(error);
	        }
        }
               
        data = OmletsMobile.get().getGeneralData();                
        
        // Get storage data
        principalName = Storage.get().getItem("principal");          
                
        SquarePanel container = new SquarePanel(); 
        container.addStyleName("login");
        
        // Secure login message
        IconLabel loginMessage = new IconLabel(messages.secureLogin(), Icon.LOGIN.image());
        loginMessage.addStyleName("login-icon-label");
        loginMessage.addTextStyleName("login-icon-label-text");        
        container.add(loginMessage);
        
        // Custom welcome message
        String welcomeText = data.getWelcomeMessage();       
        if(StringHelper.isNotEmpty(welcomeText)) {
            LabelField welcomeMessage = new LabelField(welcomeText);
            welcomeMessage.addStyleName("login-welcome-text");
            container.add(welcomeMessage);
        }                      
        
        principal = new TextField(resolvePrincipalInputMessage());     
        
        
        // If a principal is already set shows a label instead of text box
        if(principalName != null) {
            principal.setValue(principalName);
            //principal.setReadOnly(true);
            
            loginLabel = new LabelField(messages.loginAs());
            loginLabel.setStyleName("login-label");
            
            container.add(loginLabel);
        }
        container.add(principal);
        
        password = new PasswordField(resolvePasswordInputMessage());     
        password.addKeyDownHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
            	if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            		//Window.alert("enter!");
            	    loginAction.run();
            	}
            }
        });
        container.add(password); 
        
        ClickHandler handle = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
		    	PageAction action =  new PageAction() {
		    		
		    		@Override
		    		public void run() {
		    			Navigation.get().go(PageAnchor.FORGOTPASSWORD);
		    		}
		    		
		    		@Override
		    		public String getLabel() {
		    			return "Forgot Password";
		    		}
		    	};
				action.run();
			}
		};
        HyperLink forgotPassword = new HyperLink("javascript:;","Forgot your password ?",handle);
        container.add(forgotPassword);
        
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
        
        // Add change user action only if an user is already set
//        if(Storage.get().getItem("principal") != null) {
//            actions.add(getChangePrincipalAction());
//        }  
        
        actions.add(getRegisterAction());
        
        
        // Add login action
        actions.add(getLoginAction());
       
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
    private PageAction getLoginAction() {
       loginAction = new PageActionAsync<InitialData>() {        
            @Override
            public void runAsync(BaseAsyncCallback<InitialData> callback) {
                // Validate input
                if(validate()) {                
                    // Execute user login
                    accessService.login(principal.getValue(), password.getValue(), callback);
                } else {
                	this.cancelCallback();
                }
                
            } 
            @Override
            protected void onCallbackSuccess(InitialData result) {
                // Initialize user's session data
                LoggedUser.get().initialize(principal.getValue(), password.getValue(), result);                                                        
                
                // Load initial page
                Navigation.get().loadInitialPage();  
            }
            
            @Override
            public String getLabel() {               
                return messages.login();
            }
        }; 
        return loginAction;
    }    
    
    private PageAction getRegisterAction() {
    	return new PageAction() {
    		
    		@Override
    		public void run() {
    			Navigation.get().go(PageAnchor.REGISTER);
    		}
    		
    		@Override
    		public String getLabel() {
    			return "Register";
    		}
    	};
    }
    
    /**
     * Returns change user page action
     */
    private PageAction getChangePrincipalAction() {
        return new PageAction() {
            @Override
            public void run() {
                // Reset login form
                principal.clear();
                password.clear();
                principal.setReadOnly(false);
                loginLabel.setVisible(false);
                // Hide change user action
                OmletsMobile.get().getMainLayout().addActions(Arrays.asList(loginAction), true);
            }

            @Override
            public String getLabel() {
                return messages.changeUser();
            }    
        };
    }
          
    /**
     * Client side input validation
     */
    private boolean validate() {
        // Hide previous notifications
        Notification.get().hide();
        
        // Validate principal and password
        if(StringHelper.isEmpty(principal.getValue()) || StringHelper.isEmpty(password.getValue())) {
            Notification.get().error(messages.loginFieldsCannotBeEmpty());
            return false;
        }

        return true;
    }
}
