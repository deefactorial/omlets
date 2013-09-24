/*
   This file is part of Cyclos.

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
package nl.strohalm.cyclos.mobile.client.services.exceptions;

import nl.strohalm.cyclos.mobile.client.model.ErrorType;

/**
 * Exception raised on server error on REST calls.
 * 
 * @author luis
 */
public class RestServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    private String details;
    private ErrorType type;

    public RestServiceException(ErrorType type, String details) {
        this.type = type;
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
    
    public final ErrorType getType() {
        return type;
    }    

}