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
package org.openmoney.omlets.mobile.client;

import org.openmoney.omlets.mobile.client.model.AppState;
import org.openmoney.omlets.mobile.client.model.GeneralData;
import org.openmoney.omlets.mobile.client.ui.MainLayout;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.utils.ErrorHandler;
import org.openmoney.omlets.mobile.client.utils.Storage;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutEvent;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;

/**
 * Entry point for the Cyclos mobile application.
 * 
 * @author luis
 */
public class OmletsMobile implements EntryPoint {
    
    private static OmletsMobile instance;

    public static OmletsMobile get() {
        return instance;
    }

    private static MainLayout  mainLayout;
    private GeneralData generalData;
    private PhoneGap    phoneGap;
    
    /**
     * Returns the current application state
     */
    public AppState getAppState() {
        if (!Configuration.get().isServerConfigured()) {
            return AppState.SERVER_NOT_CONFIGURED;
        } else if (generalData == null) {
            return AppState.SERVER_DATA_NOT_LOADED;
        } else if (!LoggedUser.get().isLoggedIn()) {
            return AppState.NO_LOGGED_USER;
        }
        return AppState.READY;
    }

    /**
     * Returns application general data retrieved in rest service
     */
    public GeneralData getGeneralData() {
        return generalData;
    }

    /**
     * Returns main layout which holds the application's visual elements 
     */
    public MainLayout getMainLayout() {
        return mainLayout;
    }

    /**
     * Returns PhoneGapp component 
     */
    public PhoneGap getPhoneGap() {
        return phoneGap;
    }

    @Override
    public void onModuleLoad() {
        instance = this;              
        
        initApplication();       
    }
    
    /**
     * Initializes the mobile application
     */
    private void initApplication() {           
    	

        
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {                
                ErrorHandler.handle(e);
            }
        });
        


        phoneGap = GWT.create(PhoneGap.class);
        phoneGap.addHandler(new PhoneGapAvailableHandler() {
            @Override
            public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {

                String splash = Storage.get().getItem("splash");                
                    
                // If there is no splash screen available to display, start the application
                if(StringHelper.isEmpty(splash)) {
                    startApplication();
                } else {               
                    // Wait for the splash screen
                    Timer t = new Timer() {
                        @Override
                        public void run() {
                            startApplication();
                        }                
                    };
                    t.schedule(2000);
                }
            }
        });
        phoneGap.addHandler(new PhoneGapTimeoutHandler() {
            @Override
            public void onPhoneGapTimeout(PhoneGapTimeoutEvent event) {
            	
                //throw new IllegalStateException(Messages.Accessor.get().loadingApplicationError());
            	startApplication();
            }
        });              
        
        int timeout = 250; //time out right away
        if (isPhoneGap()) {
        	timeout = 32*1000;// 32 seconds to time out
        }
        
        phoneGap.initializePhoneGap(timeout); 

        
    }
    
    public native boolean isPhoneGap() /*-{
    	return $wnd.isPhoneGap();
	}-*/;
   
    /**
     * Sets application general data
     */
    public void setGeneralData(GeneralData generalData) {
        this.generalData = generalData;                       
    }

    /**
     * Initialize main layout and starts application
     */
    private void startApplication() {        
                
        // Override opaque class and set principal class
        RootPanel.getBodyElement().setClassName("principal");
        
        // Remove loading element
        Element loading = DOM.getElementById("loading");
        if(loading != null) {
            DOM.removeChild(RootPanel.getBodyElement(), loading);
        }        

        mainLayout = new MainLayout();
        mainLayout.apply(RootPanel.get());                
       
        loadInitialPage();
    }        
    
    /**
     * Load initial page
     */
    private void loadInitialPage() {
        switch(getAppState()) {
            case SERVER_NOT_CONFIGURED:
                Navigation.get().goNoHistory(PageAnchor.CONFIGURATION);
                break;
            case SERVER_DATA_NOT_LOADED:
                Navigation.get().goNoHistory(PageAnchor.LOAD_GENERAL_DATA);
                break;
            default:
                Navigation.get().loadInitialPage();                
        }
    }
       
    /**
     * Reloads the application
     */
    public native void reloadApplication() /*-{
        $wnd.reloadApp();
    }-*/;
}
