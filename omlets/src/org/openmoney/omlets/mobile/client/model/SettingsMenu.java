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

import org.openmoney.omlets.mobile.client.ui.PageAnchor;


public class SettingsMenu extends Entity {


	
	protected SettingsMenu(){

	}
	
	public final native String getMenuName() /*-{
		return $wnd.cleanString(this.menuName);
	}-*/;
	
	public final native PageAnchor getPage() /*-{
		return this.page;
	}-*/;
	
	public final native void setMenuName(String menu) /*-{
		this.menuName = menu;
	}-*/;
	
	public final native void setPage(PageAnchor anchor) /*-{
		this.page = anchor;
	}-*/;
}
