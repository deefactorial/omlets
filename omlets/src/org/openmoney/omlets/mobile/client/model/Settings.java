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


public class Settings extends Entity {

	protected Settings(){
	}
	
    public final native String getError() /*-{
    	return $wnd.cleanString(this.error);
	}-*/;
    
    public final native Long getUACID() /*-{
		return @java.lang.Long::valueOf(Ljava/lang/String;)($wnd.cleanString(this.uacID));
	}-*/;
    
    public final native Long getCurrencyID() /*-{
		return @java.lang.Long::valueOf(Ljava/lang/String;)($wnd.cleanString(this.currencyID));
	}-*/;
}
