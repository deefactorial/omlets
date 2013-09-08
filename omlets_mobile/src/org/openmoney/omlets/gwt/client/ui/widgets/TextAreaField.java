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
package org.openmoney.omlets.gwt.client.ui.widgets;

import org.openmoney.omlets.gwt.client.utils.StringHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;

/**
 * A text area field
 */
public class TextAreaField extends Composite {

    private TextArea textArea;
    
    public TextAreaField() {
        this(null);
    }
    
    public TextAreaField(String placeholder) {
        textArea = new TextArea();
        textArea.setStyleName("form-input-field");
        if(StringHelper.isNotEmpty(placeholder)) {
            setPlaceHolder(placeholder);
        }
        initWidget(textArea);
    }
    
    /**
     * Returns component's value
     */
    public String getValue() {
        return textArea.getValue();
    }
    
    /**
     * Sets the field place holder attribute
     */
    private void setPlaceHolder(String placeholder) {
        if(placeholder != null && !placeholder.isEmpty()) {
            DOM.setElementProperty(textArea.getElement(), "placeholder", placeholder);
        }
    }
    
}
