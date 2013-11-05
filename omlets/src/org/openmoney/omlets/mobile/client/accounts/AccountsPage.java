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
package org.openmoney.omlets.mobile.client.accounts;

import org.openmoney.omlets.mobile.client.LoggedUser;
import org.openmoney.omlets.mobile.client.Navigation;
import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.Notification.NotificationLayout;
import org.openmoney.omlets.mobile.client.model.Account;
import org.openmoney.omlets.mobile.client.model.AccountData;
import org.openmoney.omlets.mobile.client.model.Parameters;
import org.openmoney.omlets.mobile.client.services.AccountService;
import org.openmoney.omlets.mobile.client.ui.Page;
import org.openmoney.omlets.mobile.client.ui.PageAnchor;
import org.openmoney.omlets.mobile.client.ui.widgets.AccountRow;
import org.openmoney.omlets.mobile.client.ui.widgets.DataList;
import org.openmoney.omlets.mobile.client.utils.ParameterKey;
import org.openmoney.omlets.mobile.client.utils.ResultPage;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

class OurRangeChangeEvent extends RangeChangeEvent {

	protected OurRangeChangeEvent(Range range) {
		super(range);
		// TODO Auto-generated constructor stub
	}}

/**
 * A {@link Page} used to display accounts.
 * 
 * @author luis
 */
public class AccountsPage extends Page {
    
    private AccountService accountService = GWT.create(AccountService.class);
    private JsArray<Account> accounts;
    
    @Override
    public String getHeading() {
        accounts = LoggedUser.get().getInitialData().getAccounts();
        
        // If exists only an account display details title
        if(accounts.length() == 1) {
            return messages.accountDetailsHeading() + " " + LoggedUser.get().getInitialData().getProfile().getName();
        } 
        return messages.accountsHeading() + " " + LoggedUser.get().getInitialData().getProfile().getName();
    }

    @Override
    public boolean hasCustomScroll() {
        return true;
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
        
        if(accounts.length() == 1) {
            // If exists only an account display details page
            final Account account = accounts.get(0);
            
            Parameters params = new Parameters();
            params.add(ParameterKey.ID, account.getId());   
            
            Page page = new AccountDetailsPage();
            page.setParameters(params);

            return page.initialize();
        }
        
        // List pages uses fixed notification layout
        Notification.get().setLayout(NotificationLayout.FIXED);
        
        
        
        // If there are multiple accounts, display it
        DataList<AccountData> dataList = new DataList<AccountData>() {
        	
        	String orderBy = "currency";
        	String orderDirection = "ASC";
        	String secondaryOrderBy = "trading_name";
        	String secondaryOrderDirection = "ASC";
       
            @Override
            protected Widget onRender(Context context, AccountData data) {                
                
                // Value can be null
                if (data == null) {
                  return null;
                }
                
                boolean exists = false;
                for(int i = 0; i < accounts.length() ; i++) {
                	if(accounts.get(i).equals(data.getAccount())){
                		exists = true;
                	}
                }
                
                if (!exists) {
                	accounts.push(data.getAccount());
                	LoggedUser.get().getInitialData().setAccounts(accounts);
                }
                
                // Create row widget
                AccountRow row = new AccountRow();
                row.setHeading(data.getAccount().getType().getName());
                row.setLeftStyle("account-left");
                row.setMiddleValue(data.getStatus().getCurrency(), "");
                row.setFreeSpaceValue();
                row.setValue(data.getStatus().getFormattedTrading().toString(),"");
                //row.setFourthValue(data.getStatus().getFormattedBalance(), data.getStatus().getBalance() > 0d);
                row.setSubValue(data.getStatus().getFormattedBalance(), data.getStatus().getBalance() > 0d);
                row.setFourthVisibility(false);
                
                return row;
            }

            @Override
            protected void onSearchData(int page, int length, AsyncCallback<ResultPage<AccountData>> callback) {
                // Fetch account data
            	Parameters parameters = new Parameters();
            	parameters.add(ParameterKey.CURRENT_PAGE, page);
            	parameters.add(ParameterKey.PAGE_SIZE, length);
            	consoleLog(orderBy);
            	parameters.add(ParameterKey.ORDER_BY, orderBy);
            	parameters.add(ParameterKey.ORDER_DIRECTION, orderDirection);
            	parameters.add(ParameterKey.SECONDARY_ORDER_BY, secondaryOrderBy);
            	parameters.add(ParameterKey.SECONDARY_ORDER_DIRECTION, secondaryOrderDirection);
                accountService.getAccountsData(parameters,callback);
                
            }     
            
            
            @Override
            protected void onRowSelected(AccountData value) { 
                // Go to account details
                navigateToAccountDetails(value.getAccount());
            }
            
            @Override
            protected Widget addHeader(){

            	// TODO: add static strings to messages properties
            	
            	final AccountRow row = new AccountRow();
            	
            	//Trading name click handler
            	ClickHandler leftClickHandler = new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						
						if(orderBy.equals("trading_name")){
							//orderBy is already trading name so just change direction of order
							if (orderDirection.equals("ASC")) {
								orderDirection = "DESC";
								event.getRelativeElement().setInnerText("Trading Name ▼");
							} else {
								orderDirection = "ASC";
								event.getRelativeElement().setInnerText("Trading Name ▲");
							}
							
						} else {
							//orderBy was just currency so swap orderBy with secondary
							String tempOrderDirection = secondaryOrderDirection;
							secondaryOrderBy = orderBy; // currency
							secondaryOrderDirection = orderDirection;
							orderBy = "trading_name";
							if (tempOrderDirection.equals("ASC")) {
								orderDirection = "DESC";
								event.getRelativeElement().setInnerText("Trading Name ▼");
							} else {
								orderDirection = "ASC";
								event.getRelativeElement().setInnerText("Trading Name ▲");
							}
							row.removeMiddleStyle("underline");
							row.addHeadingStyle("underline");
						}
						
						values.clear();
						cellList.fireEvent(new OurRangeChangeEvent(cellList.getVisibleRange()));
						consoleLog("left onclick handler " + orderBy + " " + orderDirection + ", " + secondaryOrderBy + " " + secondaryOrderDirection);
					}
				};
            	row.setHeading("Trading Name ▲","", leftClickHandler);
            	row.setLeftStyle("account-left");
            	
            	//Currency click handler
            	ClickHandler middleClickHandler = new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						if(orderBy.equals("currency")){
							if (orderDirection.equals("ASC")) {
								orderDirection = "DESC";
								event.getRelativeElement().setInnerText("Currency ▼");
							} else {
								orderDirection = "ASC";
								event.getRelativeElement().setInnerText("Currency ▲");
							}
						} else {
							//orderBy was just trading name so swap orderBy with secondary
							String tempOrderDirection = secondaryOrderDirection;
							secondaryOrderBy = orderBy; //trading_name
							secondaryOrderDirection = orderDirection; // current trading name direction
							orderBy = "currency";
							
							//change the direction of  what it just was.
							if (tempOrderDirection.equals("ASC")) {
								orderDirection = "DESC";
								event.getRelativeElement().setInnerText("Currency ▼");
							} else {
								orderDirection = "ASC";
								event.getRelativeElement().setInnerText("Currency ▲");
							}
							row.removeHeadingStyle("underline");
							row.addMiddleStyle("underline");
						}
						
						orderBy = "currency";
						values.clear();
						cellList.fireEvent(new OurRangeChangeEvent(cellList.getVisibleRange()));
						consoleLog("middle onclick handler " + orderBy + " " + orderDirection + ", " + secondaryOrderBy + " " + secondaryOrderDirection);
					}
				};
            	row.setMiddleValue("Currency ▲", "row-middle-header",middleClickHandler);
            	row.addMiddleStyle("underline");
            	row.setFreeSpaceValue();
            	row.setValue("Volume", "row-right-header");
            	row.setSubValue("Balance", "row-fourth-header");
            	
            	//row.setFourthValue("Balance", "row-fourth-header");
            	row.setFourthVisibility(false);
            	
            	row.addAttachHandler(new AttachEvent.Handler() {
					
					@Override
					public void onAttachOrDetach(AttachEvent event) {
						
						resizeColumn();
					}
				});
            	
            	return row;
            }
        };
        
        

        return dataList;
    }
    
    public static native void consoleLog(String message) /*-{
		$wnd.console.log(message);
	}-*/;
    
    /**
     * Navigates to account details sending according parameters     
     */
    private void navigateToAccountDetails(Account account) {
        Parameters params = new Parameters();
        params.add(ParameterKey.ID, account.getId());   
        Navigation.get().go(PageAnchor.ACCOUNT_DETAILS, params);      
    }

}
