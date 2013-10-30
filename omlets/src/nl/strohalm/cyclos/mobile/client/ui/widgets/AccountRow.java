/*
    This file is part of Cyclos (www.cyclos.org).
    A project of the Social Trade Organisation (www.socialtrade.org).

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
package nl.strohalm.cyclos.mobile.client.ui.widgets;

import nl.strohalm.cyclos.mobile.client.utils.StringHelper;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * An account row widget used in data list. 
 */
public class AccountRow extends Composite {        
    
    private FlowPanel container;
    private FlowPanel rowContent;
    private FlowPanel leftContainer;

    private FlowPanel middleContainer;
    
    private FlowPanel freeSpaceContainer;
    
    private FlowPanel rightContainer;
    private FlowPanel fourthContainer;
    
    private Label leftLabel;
    private Label middleLabel;
    //private Label rightLabel;
    //private Label fourthLabel;
    
    public AccountRow() {
    	
        container = new FlowPanel();
        initWidget(container);
        container.setStyleName("row");
        
        rowContent = new FlowPanel();
        rowContent.setStyleName("row-content");
        
        leftContainer = new FlowPanel();
        leftContainer.addStyleName("row-left-column");
        
        
        middleContainer = new FlowPanel();
        middleContainer.addStyleName("row-middle-column");
        
        freeSpaceContainer = new FlowPanel();
        freeSpaceContainer.addStyleName("row-freespace-column");
        
        rightContainer = new FlowPanel();
        rightContainer.addStyleName("row-right-column");
        
        fourthContainer = new FlowPanel();
        fourthContainer.addStyleName("row-fourth-column");
        

        
        rowContent.add(leftContainer);
        rowContent.add(middleContainer);
        rowContent.add(freeSpaceContainer);
        rowContent.add(rightContainer);
        rowContent.add(fourthContainer);
        
        container.add(rowContent);      
        
    }
    
    /**
     * Sets left container style
     */
    public void setLeftStyle(String style) {
        if(StringHelper.isNotEmpty(style)) {
            leftContainer.addStyleName(style);
        }
    }    
    
    
    /**
     * Sets left container visibility
     */
    public void setLeftVisibility(boolean isDisplayed) {       
         leftContainer.setVisible(isDisplayed);
    }    
    
    /**
     * Sets middle container style
     */
    public void setMiddleStyle(String style) {
        if(StringHelper.isNotEmpty(style)) {
            middleContainer.addStyleName(style);
        }
    }  
    
    /**
     * Sets middle container visibility
     */
    public void setMiddleVisibility(boolean isDisplayed) {       
         middleContainer.setVisible(isDisplayed);
    }    

    /**
     * Sets right container style
     */
    public void setRightStyle(String style) {
        if(StringHelper.isNotEmpty(style)) {
            rightContainer.addStyleName(style);
        }
    }
    
    /**
     * Sets right container visibility
     */
    public void setRightVisibility(boolean isDisplayed) {       
         rightContainer.setVisible(isDisplayed);
    }   
    
    /**
     * Sets Fourth container style
     */
    public void setFourthStyle(String style) {
        if(StringHelper.isNotEmpty(style)) {
            fourthContainer.addStyleName(style);
        }
    }
    
    /**
     * Sets fourth container visibility
     */
    public void setFourthVisibility(boolean isDisplayed) {       
         fourthContainer.setVisible(isDisplayed);
    }   
    
    /**
     * Sets the row heading
     */
    public void setHeading(String heading) {
        setHeading(heading, null);
    }
    
    /**
     * Sets the row heading adding a custom style
     */
    public void setHeading(String heading, String style) {
        leftLabel = new Label(heading);
        leftLabel.addStyleName("row-heading");
        if(StringHelper.isNotEmpty(style)) {
        	leftLabel.addStyleName(style);
        }
        leftContainer.add(leftLabel);
    }
    
    /**
     * Sets the row heading adding a custom style
     */
    public void setHeading(String heading, String style, ClickHandler clickhandler) {
        leftLabel = new Label(heading);
        leftLabel.addClickHandler(clickhandler);
        leftLabel.addStyleName("row-heading");
        if(StringHelper.isNotEmpty(style)) {
        	leftLabel.addStyleName(style);
        }
        leftContainer.add(leftLabel);
    }
    
    public void removeHeadingStyle(String style){
    	leftLabel.removeStyleName(style);
    }
    
    public void addHeadingStyle(String style){
    	leftLabel.addStyleName(style);
    }
    
    /**
     * Sets the row sub-heading    
     */
    public void setSub(String description) {
        setSub(description, null);
    }
    
    /**
     * Sets the row sub-heading adding a custom style    
     */
    public void setSub(String description, String style) {
        Label descriptionLabel = new Label(description);
        descriptionLabel.addStyleName("row-sub");
        if(StringHelper.isNotEmpty(style)) {
            descriptionLabel.addStyleName(style);
        }
        leftContainer.add(descriptionLabel);
    }
    
    /**
     * Sets the row value adding a custom style
     */
    public void setValue(String value, String style) {
        setValue(value, null, style);
    }
    
    /**
     * Sets the row value
     */
    public void setValue(String value, Boolean positive) {
        setValue(value, positive, null);
    }
    
    /**
     * Sets the row value adding a custom style
     */
    public void setValue(String value, Boolean positive, String style) {
        Label valueLabel = new Label(value);       
        if(positive != null) {
            valueLabel.addStyleName(positive ? "amount-positive" : "amount-negative");
        }
        if(StringHelper.isNotEmpty(style)) {
            valueLabel.addStyleName(style);
        }
        rightContainer.add(valueLabel);
    }
    
    /**
     * Sets the row value adding a custom style
     */
    public void setValue(String value, String style, ClickHandler clickhandler) {
        Label valueLabel = new Label(value);       
        valueLabel.addClickHandler(clickhandler);
        if(StringHelper.isNotEmpty(style)) {
            valueLabel.addStyleName(style);
        }
        rightContainer.add(valueLabel);
    }
    
    /**
     * Sets the row sub-value adding a custom style    
     */
    public void setSubValue(String description, String style) {
        Label descriptionLabel = new Label(description);
        //descriptionLabel.addStyleName("row-sub");
        if(StringHelper.isNotEmpty(style)) {
            descriptionLabel.addStyleName(style);
        }
        rightContainer.add(descriptionLabel);
    }
    
    /**
     * Sets the row value
     */
    public void setSubValue(String value, Boolean positive) {
        setValue(value, positive, null);
    }
    
    /**
     * Sets the row value adding a custom style
     */
    public void setSubValue(String value, Boolean positive, String style) {
        Label valueLabel = new Label(value); 
        
        if(positive != null) {
            valueLabel.addStyleName(positive ? "amount-positive" : "amount-negative");
        }
        if(StringHelper.isNotEmpty(style)) {
            valueLabel.addStyleName(style);
        }
        rightContainer.add(valueLabel);
    }
    

    
    /**
     * Sets the row middle value adding a custom style
     */
    
    public void setMiddleValue(String value, String style) {
    	middleLabel = new Label(value); 
    	if(StringHelper.isNotEmpty(style)) {
    		middleLabel.addStyleName(style);
        }
        middleContainer.add(middleLabel);
    }
    
    /**
     * Sets the row middle value adding a custom style and clickhandler
     */
   
    public void setMiddleValue(String value, String style, ClickHandler clickhandler) {
    	middleLabel = new Label(value); 
    	
    	middleLabel.addClickHandler(clickhandler);
    	if(StringHelper.isNotEmpty(style)) {
    		middleLabel.addStyleName(style);
        }
        middleContainer.add(middleLabel);
    }
    
    
    public void removeMiddleStyle(String style){
    	middleLabel.removeStyleName(style);
    }
    
    public void addMiddleStyle(String style){
    	middleLabel.addStyleName(style);
    }
    
    public void setFreeSpaceValue(){
    	freeSpaceContainer.add(new Label(""));
    }
    
    /**
     * Sets free space container visibility
     */
    public void setFreeSpaceVisibility(boolean isDisplayed) {       
         freeSpaceContainer.setVisible(isDisplayed);
    }    
    
    
    /**
     * Sets the row fourth value adding a custom style
     */
    
    public void setFourthValue(String value, String style) {
    	Label valueLabel = new Label(value); 
    	if(StringHelper.isNotEmpty(style)) {
            valueLabel.addStyleName(style);
        }
        fourthContainer.add(valueLabel);
    }
    
    /**
     * Sets the row fourth value adding a custom style and clickhandler
     */
   
    public void setFourthValue(String value, String style, ClickHandler clickhandler) {
    	Label valueLabel = new Label(value); 
    	
    	valueLabel.addClickHandler(clickhandler);
    	if(StringHelper.isNotEmpty(style)) {
            valueLabel.addStyleName(style);
        }
        fourthContainer.add(valueLabel);
    }
    
    /**
     * Sets the row value
     */
    public void setFourthValue(String value, Boolean positive) {
        setFourthValue(value, positive, null);
    }
    
    /**
     * Sets the row value adding a custom style
     */
    public void setFourthValue(String value, Boolean positive, String style) {
        Label valueLabel = new Label(value);       
        if(positive != null) {
            valueLabel.addStyleName(positive ? "amount-positive" : "amount-negative");
        }
        if(StringHelper.isNotEmpty(style)) {
            valueLabel.addStyleName(style);
        }
        fourthContainer.add(valueLabel);
    }
    
}
