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

import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.Notification.NotificationLayout;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.model.SettingsMenu;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.ui.widgets.SimpleDataList;
import org.openmoney.omlets.mobile.client.ui.widgets.UserRow;
import org.openmoney.omlets.mobile.client.utils.ParameterKey;
import org.openmoney.omlets.mobile.client.utils.ResultPage;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to display user list.
 */
public class SettingsPage extends Page {
	

    private SimpleDataList<SettingsMenu> dataList;
    
    @Override
    public String getHeading() {       
        return messages.usersHeading();
    }
    
    @Override
    public boolean hasCustomScroll() {
        return true;
    }

    @Override
    public Widget initialize() {
        
        // List pages uses fixed notification layout
        Notification.get().setLayout(NotificationLayout.FIXED);
        
        if (getParameters() != null) {
	        String success = getParameters().getString(ParameterKey.SUCCESS);
	        String error = getParameters().getString(ParameterKey.ERROR);
	        
	        if ( (success != null) && StringHelper.isNotEmpty(success) ){

               
	        	Notification.get().success(success);
	        } else if ( (error != null) && StringHelper.isNotEmpty(error)){
	        	Notification.get().error(error);
	        }
            Timer t = new Timer() {
                @Override
                public void run() {
                    Notification.get().hide();
                }                
            };
            t.schedule(5000);
        }
        
        dataList = new SimpleDataList<SettingsMenu>() {
            @Override
            protected Widget onRender(Context context, SettingsMenu menu) {

                // Value can be null, so do a null check..
                if (menu == null) {
                  return null;
                }
                              
                // Create row widget
                UserRow row = new UserRow();
                //row.setImage(Icon.USER.image());
                row.setHeading(menu.getMenuName(),"settings-menu");
                //row.setSub(menu.getName());               
                
                return row;
            }
            
            @Override
            protected void onSearchData(AsyncCallback<JsArray<SettingsMenu>> callback) {
            
            	JsArray<SettingsMenu> result = JavaScriptObject.createArray().cast();
            	
            	SettingsMenu menu = JavaScriptObject.createObject().cast();
            	menu.setMenuName("Join A Currency > ");
            	menu.setPage(PageAnchor.SETTINGS_JOIN_CURRENCY);
             	result.push(menu);
             	
            	SettingsMenu create = JavaScriptObject.createObject().cast();
            	create.setMenuName("Create A Currency > ");
            	create.setPage(PageAnchor.SETTINGS_CREATE_CURRENCY);
             	result.push(create);
             	
            	SettingsMenu profile = JavaScriptObject.createObject().cast();
            	profile.setMenuName("Profile Settings > ");
            	profile.setPage(PageAnchor.SETTINGS_PROFILE_PAGE);
             	result.push(profile);
              	
            
            	callback.onSuccess(result);
 
            }         
         
            @Override
            protected void onRowSelected(SettingsMenu value) {                 
                // Go to user details
                navigateTo(value.getPage());
            }
            
            @Override
            protected void onDataLoaded(ResultPage<SettingsMenu> result) {
                SettingsPage.this.onDataLoaded(result);
            }
        };
        
        return dataList;
    }
    
    /**
     * May be override in order to execute actions on data loaded
     */
    protected void onDataLoaded(ResultPage<SettingsMenu> result) {        
    }
    
    /**
     * Navigates to user details sending according parameters     
     */
    protected void navigateTo(PageAnchor page) {
        Parameters params = new Parameters();
        //params.add(ParameterKey.ID, memberId);
        
        Navigation.get().go(page, params);
    }
    

    


}
