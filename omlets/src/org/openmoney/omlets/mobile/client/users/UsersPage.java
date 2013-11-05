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
package org.openmoney.omlets.mobile.client.users;

import java.util.Arrays;
import java.util.List;

import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.Notification.NotificationLayout;
import org.openmoney.omlets.mobile.client.model.BasicMember;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.services.UserService;
import org.openmoney.omlets.mobile.client.ui.Icon;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.widgets.DataList;
import org.openmoney.omlets.mobile.client.ui.widgets.UserRow;
import org.openmoney.omlets.mobile.client.utils.PageAction;
import org.openmoney.omlets.mobile.client.utils.ParameterKey;
import org.openmoney.omlets.mobile.client.utils.ResultPage;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Page} used to display user list.
 */
public class UsersPage extends Page {

    private DataList<BasicMember> dataList;
    
    private UserService userService = GWT.create(UserService.class);
    
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
        
        final String keywords = getParameters() != null ? getParameters().getString(ParameterKey.KEYWORDS) : null; 
        
        dataList = new DataList<BasicMember>() {
            @Override
            protected Widget onRender(Context context, BasicMember member) {

                // Value can be null, so do a null check..
                if (member == null) {
                  return null;
                }
                              
                // Create row widget
                UserRow row = new UserRow();
                row.setImage(Icon.USER.image());
                row.setHeading(member.getUsername());
                row.setSub(member.getName());               
                
                return row;
            }

            @Override
            protected void onSearchData(int page, int length, AsyncCallback<ResultPage<BasicMember>> callback) {
                // Fetch users
                Parameters parameters = new Parameters();
                parameters.set(ParameterKey.CURRENT_PAGE, String.valueOf(page));
                parameters.set(ParameterKey.PAGE_SIZE, String.valueOf(length));
                parameters.set(ParameterKey.EXCLUDE_LOGGED_IN, true);
                if(StringHelper.isNotEmpty(keywords)) {
                    parameters.set(ParameterKey.KEYWORDS, keywords);
                }
                userService.searchMembers(parameters, callback);   
            }         
         
            @Override
            protected void onRowSelected(BasicMember value) {                 
                // Go to user details
                navigateTo(value.getId());
            }
            
            @Override
            protected void onDataLoaded(ResultPage<BasicMember> result) {
                UsersPage.this.onDataLoaded(result);
            }
        };
        
        return dataList;
    }
    
    /**
     * May be override in order to execute actions on data loaded
     */
    protected void onDataLoaded(ResultPage<BasicMember> result) {        
    }
    
    /**
     * Navigates to user details sending according parameters     
     */
    protected void navigateTo(Long memberId) {
        Parameters params = new Parameters();
        params.add(ParameterKey.ID, memberId);
        //Navigation.get().go(PageAnchor.USER_DETAILS, params);
    }
    
    @Override
    public List<PageAction> getPageActions() {
        return Arrays.asList(getSearchUserAction());
    }
    
    /**
     * Returns search user action
     */
    public PageAction getSearchUserAction() {
        return new PageAction() {
            @Override
            public void run() {
                //Navigation.get().go(PageAnchor.USER_SEARCH);
            }
            @Override
            public String getLabel() {              
                return messages.newSearch();
            }            
        };
    }

}
