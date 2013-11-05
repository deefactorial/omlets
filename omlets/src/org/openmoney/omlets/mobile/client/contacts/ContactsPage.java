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
package org.openmoney.omlets.mobile.client.contacts;

import java.util.Arrays;
import java.util.List;

import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.Notification.NotificationLayout;
import org.openmoney.omlets.mobile.client.model.Contact;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.services.ContactService;
import org.openmoney.omlets.mobile.client.ui.Icon;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.widgets.SimpleDataList;
import org.openmoney.omlets.mobile.client.ui.widgets.UserRow;
import org.openmoney.omlets.mobile.client.utils.PageAction;
import org.openmoney.omlets.mobile.client.utils.ParameterKey;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to display contacts. 
 */
public class ContactsPage extends Page {

    private ContactService contactService = GWT.create(ContactService.class);
    
    @Override
    public String getHeading() {
        return messages.contactsHeading();
    }

    @Override
    public boolean hasCustomScroll() {     
        return true;
    }
    
    @Override
    public Widget initialize() {
        
        // List pages uses fixed notification layout
        Notification.get().setLayout(NotificationLayout.FIXED);
        
        SimpleDataList<Contact> dataList = new SimpleDataList<Contact>() {
            
            @Override
            protected Widget onRender(Context context, Contact data) {                
                
                // Value can be null
                if (data == null) {
                  return null;
                }
                
                // Create row widget
                UserRow row = new UserRow();
                row.setImage(Icon.CONTACT.image());
                row.setHeading(data.getMember().getUsername());
                row.setSub(data.getMember().getName());               
                
                return row;
            }

            @Override
            protected void onSearchData(AsyncCallback<JsArray<Contact>> callback) {
                // Fetch contacts
                contactService.list(callback);
            }            
            
            @Override
            protected void onRowSelected(Contact value) {   
                // Go to contact details page
                navigateTo(value.getMember().getId());
            }          
        };

        return dataList;
    }
        
    /**
     * Navigates to contact details sending according parameters     
     */
    protected void navigateTo(Long memberId) {
        Parameters params = new Parameters();
        params.add(ParameterKey.ID, memberId);   
        //Navigation.get().go(PageAnchor.CONTACT_DETAILS, params);    
    }
    
    @Override
    public List<PageAction> getPageActions() {
        return Arrays.asList(getSearchAction());
    }

    /**
     * Returns search action
     */
    private PageAction getSearchAction() {
        return new PageAction() {
            @Override
            public void run() {
                //Navigation.get().go(PageAnchor.USER_SEARCH);
            }
            @Override
            public String getLabel() {
                return messages.addNewContact();
            }            
        };
    }
    
}
