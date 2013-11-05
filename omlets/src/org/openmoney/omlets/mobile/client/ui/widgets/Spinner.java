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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Displays an image which can be rotated.
 */
public class Spinner extends Composite {
   
    private Image image;
    
    private int degreeSum;
    private int degreeMultiplier;
    private int delay;
    
    public Spinner(Image image) {
        this(image, "spinner");
    }

    public Spinner(Image image, String style) {
        this.image = image;
        this.degreeSum = 36;
        this.degreeMultiplier = 10;
        this.delay = 50;
        
        FlowPanel container = new FlowPanel(); 
        initWidget(container);
        container.setStyleName(style);
        container.add(this.image);
        
    }
    
    /**
     * Sets the spinner rotation variables
     */
    public void setCustomRotation(int degreeSum, int degreeMultiplier, int delay) {
        this.degreeSum = degreeSum;
        this.degreeMultiplier = degreeMultiplier;
        this.delay = delay;
    }
        
    /**
     * Starts image rotation
     */
    public void startSpinner() {
        // Stop spinner instantly before start a new rotation
        stopSpinner(image.getElement());
        startSpinner(image.getElement(), degreeSum, degreeMultiplier, delay);
    }
    
    /**
     * Starts element rotation using CSS3 properties and native JavaScript     
     */
    private final native void startSpinner(Element instance, int degSum, int degMulti, int delay) /*-{
        $wnd.startSpinner(instance, degSum, degMulti, delay);
    }-*/;   
    
    /**
     * Stops image rotation with a bit of delay 
     * to avoid an ugly effect
     */    
    public void stopSpinner() {
        Timer t = new Timer() {
            @Override
            public void run() {
                stopSpinner(image.getElement());
            }            
        };
        t.schedule(750);
    }
    
    /**
     * Stops element rotation using native JavaScript
     */
    private final native void stopSpinner(Element instance) /*-{
        $wnd.stopSpinner(instance);
    }-*/;   
}
