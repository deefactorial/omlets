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
public class SettingsJoinCurrency extends Page {
	
	private FlowPanel container;
	private SettingsTradingName settingsTradingName;
	//private SettingsCurrency settingsCurrency;
    
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
        
        settingsTradingName = new SettingsTradingName();
        // TODO: add messages values for settings page.
//      settingsCurrency.setTradingName(messages.typeYourTradingNameHere());
        settingsTradingName.setTradingName("Type your requested trading name here.");
        settingsTradingName.setUserSpace("Trading Name Space:");
        settingsTradingName.setCurrency("Type the full name of the currency.");
        
        container.add(settingsTradingName);
        

     
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
            	
            	
                
            	String tradingname = settingsTradingName.getTradingName();
            	UserSpace userspace = settingsTradingName.getUserSpace();
            	String currency = settingsTradingName.getCurrency();
            	
            	if (StringHelper.isNotEmpty( tradingname ) && StringHelper.isEmpty( currency ) ) {
            		Notification.get().error("You must specify a currency to join a trading name.");
            	} 
            	
            	if (StringHelper.isEmpty( tradingname ) && StringHelper.isNotEmpty( currency ) ) {
            		Notification.get().error("You must specify a trading name to join a currency.");
            	} 
            	
            	if (StringHelper.isNotEmpty( tradingname ) && StringHelper.isNotEmpty( currency ) ) {
            		Parameters params = new Parameters();
            		params.add(ParameterKey.TRADING_NAME, tradingname);
            		params.add(ParameterKey.USER_SPACE_ID, userspace.getId().toString());
            		params.add(ParameterKey.CURRENCY, currency);
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
								params.add(ParameterKey.SUCCESS, "Successfully created a trading name.");
								Navigation.get().goNoHistory(PageAnchor.SETTINGS_JOIN_CURRENCY, params);  
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
