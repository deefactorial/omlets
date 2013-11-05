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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * Overlay type for general data.
 * 
 * @author luis
 */
public class GeneralData extends JavaScriptObject {

    protected GeneralData() {
    }

    public final native String getWelcomeMessage() /*-{
        return $wnd.cleanString(this.welcomeMessage);
    }-*/;
    
    public final native String getApplicationName() /*-{
        return $wnd.cleanString(this.applicationName);
    }-*/;

    public final native CredentialType getCredentialType() /*-{
        return @org.openmoney.omlets.mobile.client.model.CredentialType::valueOf(Ljava/lang/String;)(this.credentialType);
    }-*/;

    public final native String getVersion() /*-{
        return $wnd.cleanString(this.version);
    }-*/;

    public final native CustomField getPrincipalCustomField() /*-{
        return $wnd.cleanObject(this.principalCustomField);
    }-*/;

    public final native PrincipalType getPrincipalType() /*-{
        return @org.openmoney.omlets.mobile.client.model.PrincipalType::valueOf(Ljava/lang/String;)(this.principalType);
    }-*/;
    
    public final native JsArray<Image> getImages() /*-{
        return $wnd.cleanObject(this.images);
    }-*/;
    
}
