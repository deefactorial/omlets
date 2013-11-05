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

import org.openmoney.omlets.mobile.client.services.exceptions.RestServiceException;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Well known JSON object returned from the server under handled errors.
 * 
 * @author luis
 */
public class ServerError extends JavaScriptObject {
    
    
    protected ServerError() {
    }

    public final native String getDetails() /*-{
        return $wnd.cleanString(this.errorDetails);
    }-*/;

    public final native ErrorType getError() /*-{
        try {
             return @org.openmoney.omlets.mobile.client.model.ErrorType::valueOf(Ljava/lang/String;)(this.errorCode);
        } catch(e) {
            return @org.openmoney.omlets.mobile.client.model.ErrorType::UNKNOWN_ERROR;
        }
    }-*/;

    public final Throwable resolveException() {
        return new RestServiceException(getError(), getDetails());
    }

}
