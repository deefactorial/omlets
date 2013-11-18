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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

/**
 * An email widget which displays it as link and allows 
 * to open an email client application when clicked
 */
public class HyperLink extends Composite {
    
    private HTML html;
    
    public HyperLink(String link, String label, ClickHandler handle) {
        html = new HTML("<p><a href=\"" + link + "\" class=\"link-field\" >" + label + "</a></p>");
        html.addClickHandler(handle);
        initWidget(html);
    }
    
    
    
    /**
     * Returns the component HTML
     */
    public String getHTML() {
        return html.getHTML();
    }

}
