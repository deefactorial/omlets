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
package org.openmoney.omlets.mobile.client.configuration;

import java.util.ArrayList;
import java.util.List;

import org.openmoney.omlets.mobile.client.Configuration;
import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.OmletsMobile;
import org.openmoney.omlets.mobile.client.model.AppState;
import org.openmoney.omlets.mobile.client.model.ConfigurationStatus;
import org.openmoney.omlets.mobile.client.services.ConfigurationService;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.ui.panels.SquarePanel;
import org.openmoney.omlets.mobile.client.ui.widgets.LabelField;
import org.openmoney.omlets.mobile.client.ui.widgets.LoadingPopup;
import org.openmoney.omlets.mobile.client.ui.widgets.SimpleSelectionField;
import org.openmoney.omlets.mobile.client.ui.widgets.TextField;
import org.openmoney.omlets.mobile.client.utils.BaseAsyncCallback;
import org.openmoney.omlets.mobile.client.utils.PageAction;
import org.openmoney.omlets.mobile.client.utils.PageActionAsync;
import org.openmoney.omlets.mobile.client.utils.ScreenHelper;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;

/**
 * A {@link Page} used to edit the application settings
 * 
 * @author luis
 */
public class ConfigurationPage extends Page {
    
    private ConfigurationService configurationService = GWT.create(ConfigurationService.class);
    
    private TextField omletsUrl;
    private SimpleSelectionField<String> languages;
    private LoadingPopup popup;

    @Override
    public String getHeading() {
        return messages.configurationHeading();
    }

    @Override
    public AppState getMinimumAppState() {
        return AppState.SERVER_NOT_CONFIGURED;
    }
    
    @Override
    public boolean isDisplayMenu() {
        return false;            
    }

    @Override
    public Widget initialize() {
        
        SquarePanel container = new SquarePanel();
        
        LabelField url = new LabelField(messages.omletsUrl());
        url.addStyleName("configuration-label-field");
        
        omletsUrl = new TextField(messages.typeOmletsUrlHere());
        omletsUrl.setValue(Configuration.get().getServerAppUrl());          
        
        LabelField example = new LabelField(messages.forExample());
                
        LabelField language = new LabelField(messages.language());
        language.addStyleName("configuration-label-field");      
        
        languages = new SimpleSelectionField<String>(Configuration.get().getLocales()) {
            @Override
            protected String getDisplayName(String item) {
                return StringHelper.capitalize(LocaleInfo.getLocaleNativeDisplayName(item));
            }
            @Override
            protected String getValue(String item) {
                return item;
            }           
        };                
        
        FlowPanel versionContainer = new FlowPanel();
        versionContainer.setStyleName("version-container");
        
        String appVersion = Configuration.get().getAppVersion();
        LabelField version = new LabelField(messages.version() + " " + appVersion);
        version.addStyleName("version-label-field");
        
        versionContainer.add(version);
                
        // Select the user language if set
        String userLanguage = Configuration.get().getUserLanguage();
        if(StringHelper.isNotEmpty(userLanguage)) {                    
            languages.selectItem(userLanguage);
        } else {
            languages.selectItem(Configuration.get().getDefaultLanguage());
        }
        
        if(Configuration.get().isURLConfigEnabled()) {
            language.addStyleName("top-spacing");
            container.add(url);
            container.add(omletsUrl);
            container.add(example);
        }
        container.add(language);
        container.add(languages);
        
        if(StringHelper.isNotEmpty(appVersion)) {
            container.add(versionContainer);
        }
                
        return container;
    }
    
    @Override
    public List<PageAction> getPageActions() {
        List<PageAction> actions = new ArrayList<PageAction>();       
        if(OmletsMobile.get().getAppState() == AppState.NO_LOGGED_USER) {
            actions.add(getCancelAction());
        }
        actions.add(getSaveAction());
        return actions;
    }
    
    /**
     * Returns cancel action
     */
    private PageAction getCancelAction() {
        return new PageAction() {
            @Override
            public void run() {
                Navigation.get().loadInitialPage();
            }
            @Override
            public String getLabel() {
                return messages.cancel();
            }           
        };
    }
    
    /**
     * Returns save action     
     */
    private PageAction getSaveAction() {
        return new PageActionAsync<ConfigurationStatus>() {            
            @Override
            public void runAsync(BaseAsyncCallback<ConfigurationStatus> callback) {
                // Hide any previous notification
                Notification.get().hide();
              
                String url = omletsUrl.getValue();  
                String language = languages.getSelectedItem();
                
                if(StringHelper.isEmpty(url) && Configuration.get().isURLConfigEnabled()) {                   
                    Notification.get().error(messages.omletsUrlIsRequired());
                    return;
                }
                
                // Block the page until url is processed
                popup = new LoadingPopup();
                popup.display();
                        
                configurationService.configureApplication(url, language, callback);                            
            }  
            @Override
            protected void onCallbackFailure() {
                popup.hide();
            }
            @Override
            public void onCallbackSuccess(final ConfigurationStatus result) {
                Timer timer = new Timer() {
                    @Override
                    public void run() {
                        popup.hide();
                        switch(result) {
                            case RELOAD_APP:
                                final boolean supportsReload = ScreenHelper.supportsReload();                                
                                Notification.get().alert(supportsReload ? messages.theLanguageHasChanged() : messages.theLanguageHasChangedNoReload(), messages.notice(), messages.ok(), new AlertCallback() {                                    
                                    @Override
                                    public void onOkButtonClicked() {
                                        // Reload application if supported
                                        if(supportsReload) {
                                            OmletsMobile.get().reloadApplication();
                                        } else {
                                            // Otherwise hide popup and continue
                                            Navigation.get().goNoHistory(PageAnchor.LOAD_GENERAL_DATA);
                                        }
                                    }
                                });                               
                                break;
                            case INVALID_URL:
                                Notification.get().error(messages.invalidUrlConfiguration());
                                break;
                            case CONFIGURED:
                                Navigation.get().goNoHistory(PageAnchor.LOAD_GENERAL_DATA);
                                break;
                        }                         
                    }                                       
                };
                timer.schedule(1500);
            }
            @Override
            public String getLabel() {
                return messages.save();
            }
        };
    }

}
