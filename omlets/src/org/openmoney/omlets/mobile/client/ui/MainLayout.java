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
package org.openmoney.omlets.mobile.client.ui;

import java.util.List;

import org.openmoney.omlets.mobile.client.Notification;
import org.openmoney.omlets.mobile.client.ui.panels.BottomPanel;
import org.openmoney.omlets.mobile.client.ui.panels.MenuPanel;
import org.openmoney.omlets.mobile.client.ui.panels.TopPanel;
import org.openmoney.omlets.mobile.client.utils.PageAction;
import org.openmoney.omlets.mobile.client.utils.ScreenHelper;
import org.openmoney.omlets.mobile.client.utils.StringHelper;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeHandler;
import com.googlecode.mgwt.ui.client.MGWT;



/**
 * Controls the main layout of the application.
 * 
 * @author luis
 */
public class MainLayout {

    private FlowPanel   topContainer;          
    private TopPanel    topPanel;
    private MenuPanel   menuPanel;
    private BottomPanel bottomPanel;

    private ScrollPanel mainContainer;
    private SimplePanel notificationContainer;
    private SimplePanel bottomContainer;    
    private SimplePanel menuContainer;
    private SimplePanel pageContainer;
    private FlowPanel   scrollContainer;
    private FlowPanel   mainWrapper;
    
    private Page page;
    private List<PageAction> actions;
    private Widget widget;
    
    private String pageContainerDependentStyle;
    private String notificationContainerDependentStyle;
            
    public MainLayout() {
        topPanel = new TopPanel();
        menuPanel = new MenuPanel();
        bottomPanel = new BottomPanel();
               
        topContainer = new FlowPanel();
        topContainer.setStyleName("top");
        topContainer.add(topPanel);
                
        menuContainer = new SimplePanel();
        menuContainer.setVisible(false);
        menuContainer.setStyleName("menu");
        menuContainer.add(menuPanel);
        
        mainWrapper = new FlowPanel();
        mainWrapper.setStyleName("main-wrapper");
        
        mainContainer = new ScrollPanel();
        mainContainer.setStyleName("main");
        mainContainer.getElement().setId("mainContainer");
        mainContainer.getElement().getStyle().setPaddingRight(15, Unit.PX);
        
        MGWT.addOrientationChangeHandler(new MyOrientationChangeHandler());
        
        notificationContainer = new SimplePanel();
        notificationContainer.setVisible(false);
        notificationContainer.setStyleName("notification-container");
        notificationContainer.getElement().setId("notificationContainer");                     
        
        pageContainer = new SimplePanel();    
        pageContainer.setStyleName("page");
        
        FlowPanel container = new FlowPanel();
        container.add(notificationContainer);
        container.add(pageContainer);
        
        mainContainer.setWidget(container);
        mainWrapper.add(mainContainer);
        
        bottomContainer = new SimplePanel();
        bottomContainer.setStyleName("bottom");
        bottomContainer.add(bottomPanel);
        
        addResizeHandler();
    }      
    
    private class MyOrientationChangeHandler implements OrientationChangeHandler {
        @Override
        public void onOrientationChanged(OrientationChangeEvent event) {
        	adjust();
        	return;
//            switch (event.getOrientation()) {
//                case LANDSCAPE:
//                    // handle landscape orientation
//                	adjust();
//                    return;
//                case PORTRAIT:
//                    // handle portrait orientation
//                	adjust();
//                    return;
//
//            }
        }
    }
    
    
    
    /**
     * Adjust application layout on resize events
     */
    private void addResizeHandler() {
        // Adjust layout on resize events
    	
    	
    	Window.addResizeHandler(new ResizeHandler() {

    		  Timer resizeTimer = new Timer() {  
    		    @Override
    		    public void run() {
    		    	adjust();
    		    }
    		  };

    		  @Override
    		  public void onResize(ResizeEvent event) {
    		    resizeTimer.cancel();
    		    resizeTimer.schedule(250);
    		  }
    	});
    	

    }   
           
    /**
     * Adjust the main container to fit exactly in available screen
     */
    public void adjust() {     
        int mainHeight = getMainHeight();
        if(mainHeight != 0) {
            String height = Math.abs(mainHeight) + "px";
            mainContainer.setHeight(height);               
        } 
    }
    

    
    /**
     * Returns the available main screen
     */
    public int getMainHeight() {
        
        int menuHeight = 0;
        int bottomHeight = 0;
        if(page != null) {
            if(page.isDisplayMenu()) {
                menuHeight = menuContainer.getOffsetHeight(); 
            }
            if(actions != null && actions.size() > 0) {
                bottomHeight = bottomContainer.getOffsetHeight();
            }            
        }       
                
        int topHeight = topContainer.getOffsetHeight();                      
        int totalHeight = Window.getClientHeight();
        
        return totalHeight - topHeight - menuHeight - bottomHeight;        
    }
    
    /**
     * Returns the current page
     */
    public Page getCurrentPage() {
        return this.page;
    }

    /**
     * Adds the layout elements to the given panel
     */
    public void apply(Panel panel) {
        panel.add(topContainer);
        panel.add(menuContainer);
        panel.add(mainWrapper);
        panel.add(bottomContainer);
    }

    /**
     * Shows the given page on the main layout or refresh the actual page if none given
     */
    public void show(Page page) {
        if(page != null) {
            this.page = page;
        }                
        setHeading();
        setMenu();        
        addOptions();
        addActions();
        setPage(); 
    }
    
    /**
     * Sets page contents in the main container
     */
    private void setPage() {
      
        // Restores notification if the layout was changed for previous page 
        Notification.get().resetLayout();
        
        // Reset dependent styles if were changed for the previous page
        resetDependentStyles();
    
        // Hide any visible notification
        Notification.get().hide();
    
        // Create the page
        widget = page.initialize();        
        if(widget != null) {            
            pageContainer.setWidget(widget);     
                        
            // Add scroll panel if native scrolling is not supported
            if(!page.hasCustomScroll() && !ScreenHelper.supportsTouch()) {
                createScrollPanel();
                scrollContainer.setVisible(true);
                mainWrapper.add(scrollContainer);
            } else if(scrollContainer != null) {
                scrollContainer.setVisible(false);
            }
            
            // Adjust the page to fit in the available screen
            widget.addAttachHandler(new AttachEvent.Handler() {  
                @Override
                public void onAttachOrDetach(AttachEvent event) {               
                    adjust();
                }
            });
        }
    }
    
    /**
     * Creates a scroll panel widget
     */
    private void createScrollPanel() {
        if(scrollContainer != null) {
            // Scroll panel already created
            return;
        }
        
        scrollContainer = new FlowPanel();
        scrollContainer.setStyleName("scroll-page");
//        
//        SimplePanel up = new SimplePanel();
//        up.setStyleName("scroll-page-image");
//        up.setWidget(Icon.UP.image());       
//        
//        SimplePanel down = new SimplePanel();
//        down.setStyleName("scroll-page-image");
//        down.setWidget(Icon.DOWN.image());                      
//        
//        scrollContainer.add(up);
//        scrollContainer.add(down);    
//        
//        // Add scrolling events
//        ComponentEventHelper.addScrollEvents(up, down, mainContainer.getElement().getId());
    }
    
    /**
     * Adds a custom dependent style for page container
     */
    public void addPageContainerStyle(String style) {
        pageContainerDependentStyle = style;
        pageContainer.setStyleDependentName(style, true);
    }
    
    /**
     * Adds a custom dependent style for notification container
     */
    public void addNotificationContainerStyle(String style) {
        notificationContainerDependentStyle = style;
        notificationContainer.setStyleDependentName(style, true);
    }
    
    /**
     * Resets added dependent styles
     */
    public void resetDependentStyles() {        
        if(StringHelper.isNotEmpty(pageContainerDependentStyle)) {
            pageContainer.setStyleDependentName(pageContainerDependentStyle, false); 
        } 
        if(StringHelper.isNotEmpty(notificationContainerDependentStyle)) {
            notificationContainer.setStyleDependentName(notificationContainerDependentStyle, false);
        }
    }
    
    /**
     * Sets page heading in the top menu bar
     */
    private void setHeading() {
        topPanel.setHeading(page == null ? "" : page.getHeading());
    }
    
    /**
     * Sets the menu bar, selects the given page and adjust the components to display properly   
     */
    private void setMenu() {
        menuContainer.setVisible(page.isDisplayMenu());  
        menuPanel.select(page.getPageAnchor());               
    }
    
    /**
     * Add top bar options
     */
    private void addOptions() {
        if(page.getPageAnchor() != null) {
            topPanel.setFromPage(page.getPageAnchor());
            topPanel.displayButtons();
            topPanel.select(page.getPageAnchor());
        }
    }
    
    /**
     * Add page actions to the bottom bar or hides the bar 
     * if no actions are available for the given page     
     */
    private void addActions() {
        addActions(page.getPageActions(), false);
    }
    
    /**
     * Add actions to the bottom bar or hides the bar
     * if no actions are available for the given page.
     * Set adjust flag to true when buttons are added asynchronously
     * and an screen adjust is needed 
     */
    public void addActions(List<PageAction> actions, boolean adjust) {
        this.actions = actions; 
        if(actions != null) {
            bottomContainer.setVisible(true);
            bottomPanel.show();
            bottomPanel.setActionButtons(this.actions);         
        } else {
            bottomContainer.setVisible(false);
            bottomPanel.hide();
            bottomPanel.clearButtons();
        }
        if(adjust) {
            adjust();
        }
    }
    
    /**
     * Rotates load image
     */
    public void startLoading() {
        topPanel.startLoading();
    }   
    
    /**
     * Stops load image rotation
     */
    public void stopLoading() {
        topPanel.stopLoading();
    }
}
