/*
   This file is part of Cyclos.

   Cyclos is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   Cyclos is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Cyclos; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 */
package nl.strohalm.cyclos.mobile.client.settings;

import java.util.Arrays;
import java.util.List;

import nl.strohalm.cyclos.mobile.client.LoggedUser;
import nl.strohalm.cyclos.mobile.client.Navigation;
import nl.strohalm.cyclos.mobile.client.Notification;
import nl.strohalm.cyclos.mobile.client.Notification.NotificationLayout;
import nl.strohalm.cyclos.mobile.client.model.Parameters;
import nl.strohalm.cyclos.mobile.client.model.Settings;
import nl.strohalm.cyclos.mobile.client.model.UserSpace;
import nl.strohalm.cyclos.mobile.client.services.SettingsService;
import nl.strohalm.cyclos.mobile.client.ui.Page;
import nl.strohalm.cyclos.mobile.client.ui.PageAnchor;
import nl.strohalm.cyclos.mobile.client.ui.widgets.SettingsCurrency;
import nl.strohalm.cyclos.mobile.client.ui.widgets.SettingsTradingName;
import nl.strohalm.cyclos.mobile.client.utils.PageAction;
import nl.strohalm.cyclos.mobile.client.utils.ParameterKey;
import nl.strohalm.cyclos.mobile.client.utils.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to display accounts.
 * 
 * @author luis
 */
public class SettingsCreateCurrency extends Page {
	
	private FlowPanel container;
	//private SettingsTradingName settingsTradingName;
	private SettingsCurrency settingsCurrency;
    
	private SettingsService settingsService = GWT.create(SettingsService.class);
	
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
        
        container = new FlowPanel();
        //initWidget(container);
        container.setStyleName("settings-page");
        

        
        settingsCurrency = new SettingsCurrency();
//        settingsCurrency.setCurrency(messages.typeCurrencyHere());
//        settingsCurrency.setUserSpace(messages.selectCurrencyDomain());
        settingsCurrency.setCurrency("Type your requested currency name here.");
        settingsCurrency.setUserSpace("Currency Space:");
        
        container.add(settingsCurrency);
     
        return container;
    }


    @Override
    public List<PageAction> getPageActions() {
          return Arrays.asList(getNextAction());
    }
   
    /**
     * Returns next's page action
     */
    private PageAction getNextAction() {
        return new PageAction() {
            @Override
            public void run() {
            	
            	String createcurrency = settingsCurrency.getCurrency();
            	UserSpace currencyUserSpace = settingsCurrency.getUserSpace();
            	
            	if (StringHelper.isNotEmpty( createcurrency ) ) {
            		Parameters params = new Parameters();
            		params.add(ParameterKey.USER_SPACE_ID, currencyUserSpace.getId().toString());
            		params.add(ParameterKey.CURRENCY, createcurrency);
            		AsyncCallback<Settings> callback = new AsyncCallback<Settings> (){

						@Override
						public void onFailure(Throwable caught) {
							
							Notification.get().error(caught.getMessage());
						}

						@Override
						public void onSuccess(Settings result) {
							
							
							if (StringHelper.isEmpty( result.getError() ) ) {
								//Notification.get().success("Successfully created a trading name.");
								Parameters params = new Parameters();
								params.add(ParameterKey.SUCCESS, "Successfully created a currency.");
								Navigation.get().goNoHistory(PageAnchor.SETTINGS_CREATE_CURRENCY, params);  
							} else {
								//Notification.get().alert(result.getError());
								//params.add(ParameterKey.ERROR, result.getError());
								Notification.get().error(result.getError());
							}
			                
						}
            			
            		};
					settingsService.postSettings(params, callback);
            	}
                
            	
            	

            }
            @Override
            public String getLabel() {
                return messages.next();
            }            
        };
    }

}
