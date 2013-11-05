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
package org.openmoney.omlets.mobile.client.model;

import org.openmoney.omlets.mobile.client.OmletsMobile;

/**
 * Overlay type for custom possible values
 */
public class PossibleValue extends Entity {

    protected PossibleValue() {        
    }
    
    public final native String getValue() /*-{
        return $wnd.cleanString(this.value);
    }-*/;
    
    public final native Long getParentId() /*-{
        var val = $wnd.cleanString(this.parentId);        
        return $wnd.isNumeric(val) ? @java.lang.Long::valueOf(Ljava/lang/String;)(val) : null;
    }-*/;
    
    public final boolean isDefault() {
        if(OmletsMobile.get().getGeneralData().getVersion().equals("3.7")) {
            ensureDefault();
        }
        return isDefaultNative();
    }

    private final native boolean isDefaultNative() /*-{
        return this.defaultValue;
    }-*/;
    
    private final boolean ensureDefault() {
        return false;
    }
        
}
