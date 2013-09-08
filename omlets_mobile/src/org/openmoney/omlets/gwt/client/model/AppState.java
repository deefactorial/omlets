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
package org.openmoney.omlets.gwt.client.model;

/**
 * Represents the possible statuses in the application workflow.
 * 
 * @author luis
 */
public enum AppState {

    /**
     * There is no server configured
     */
    SERVER_NOT_CONFIGURED,

    /**
     * The server data has not yet been loaded
     */
    SERVER_DATA_NOT_LOADED,

    /**
     * The server data has been loaded, but there is no logged user
     */
    NO_LOGGED_USER,

    /**
     * There is a successfully authenticated user
     */
    READY;

    public boolean matches(AppState state) {
        return ordinal() >= state.ordinal();
    }

}
