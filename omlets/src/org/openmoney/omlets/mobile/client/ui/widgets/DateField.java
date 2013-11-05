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

import java.util.Date;

import org.openmoney.omlets.mobile.client.ui.Icon;
import org.openmoney.omlets.mobile.client.utils.ComponentEventHelper;
import org.openmoney.omlets.mobile.client.utils.UIHelper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Widget for editing a date value with a date picker. 
 */
public class DateField extends Composite {       

    private CustomDateBox dateBox;
    private FlowPanel container;
    private DateTimeFormat dateTimeFormat;
    private SimplePanel select;
    
    public DateField() {

        dateTimeFormat = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);
        
        container = new FlowPanel();
        initWidget(container);
        container.setStyleName("dateBox-container");
        
        select = new SimplePanel();
        
        // Enable picker for devices with good UI handling 
        if(UIHelper.supportsComplexUI()) {
            select.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    dateBox.display();
                    event.preventDefault();
                    event.stopPropagation();
                }           
            }, ClickEvent.getType());
            ComponentEventHelper.addPressEvents(select, "dateBoxSelect-down");            
        }
        select.setStyleName("dateBoxSelect");
        select.setWidget(Icon.DATE.image());              
        container.add(select);
        
        dateBox = new CustomDateBox();              
        container.add(dateBox);        
             
        
    }
    
    /**
     * Returns the date string value using ISO format
     */
    public String getValue() {
        Date date = dateBox.getValue();
        if(date != null) {
            return dateTimeFormat.format(date);
        }
        return "";
    }
}
