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
package org.openmoney.omlets.mobile.client.ui.widgets;

import org.openmoney.omlets.mobile.client.model.UserSpace;
import org.openmoney.omlets.mobile.client.services.SettingsService;
import org.openmoney.omlets.mobile.client.ui.panels.SquarePanel;
import org.openmoney.omlets.mobile.client.utils.BaseAsyncCallback;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * An account row widget used in data list. 
 */
public class SettingsCurrency extends Composite {        
    
    private FlowPanel container;
    private LabelField settingsLabel;
    private SquarePanel rowContent;
    private FlowPanel firstContainer;
    private HorizontalPanel secondContainer;
    

    private TextField currency;
    private LabelField userspacelabel;
    private SelectionField<UserSpace> userSpacesSelector;
    
    private SettingsService settingsService = GWT.create(SettingsService.class);
       
    public SettingsCurrency() {
    	
        container = new FlowPanel();
        initWidget(container);
        container.setStyleName("row");
        
        rowContent = new SquarePanel();
        //rowContent.setStyleName("row-content");
        
        settingsLabel = new LabelField("Create a Currency");
        settingsLabel.addStyleName("payment-label-field");
        
        rowContent.add(settingsLabel);
        
        firstContainer = new FlowPanel();
        firstContainer.addStyleName("first-row");
        
        secondContainer = new HorizontalPanel();
        secondContainer.getElement().getStyle().setDisplay(Display.BLOCK);
        secondContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        
        secondContainer.addStyleName("second-row");
        
        rowContent.add(firstContainer);
        rowContent.add(secondContainer);
        
        container.add(rowContent);      
        
    }
    

    

    /**
     * Sets Second container style
     */
    public void setSecondStyle(String style) {
        if(StringHelper.isNotEmpty(style)) {
            firstContainer.addStyleName(style);
        }
    }

    /**
     * Sets right container style
     */
    public void setRightStyle(String style) {
        if(StringHelper.isNotEmpty(style)) {
            secondContainer.addStyleName(style);
        }
    }
    
    
    public void setCurrency(String placeholder) {
    	currency = new TextField(placeholder);
    	currency.setVisible(true);

    	
    	firstContainer.add(currency);
    }
    

    
    
    public String getCurrency(){
    	return currency.getValue();
    }
    
    public void setUserSpace(String placeholder){
    	
        userSpacesSelector = new SelectionField<UserSpace>() {
        	
            @Override
            protected String getDisplayName(UserSpace item) {
                return item.getSpaceName();
            }
                     
        };    
        //userSpacesSelector.setStyleName("userspacce-selector");
        
        userSpacesSelector.setWidth("100%");
        
        
    	userspacelabel = new LabelField(placeholder);
    	userspacelabel.setStyleName("userspace-selector-label");
        
        //UIHelper.setPlaceHolder(placeholder, userSpacesSelector.getElement());
    	secondContainer.add(userspacelabel);
        
        // Fetch account status data
        settingsService.getSettings(new BaseAsyncCallback<JsArray<UserSpace>>() {
            @Override
            public void onSuccess(JsArray<UserSpace> userSpaces) {
            	userSpacesSelector.setItems(userSpaces);
            	userSpacesSelector.selectFirst();
            }                
        });  
    	
    	secondContainer.add(userSpacesSelector);
    	secondContainer.setCellWidth(userSpacesSelector, "100%");
    }
    
	public UserSpace getUserSpace(){
		return userSpacesSelector.getSelectedItem();
	}
    
}
