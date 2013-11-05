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
package org.openmoney.omlets.mobile.client.utils;

/**
 * A {@link Runnable} which holds an identification label and
 * handles asynchronous actions notifying when callback returns. 
 */
public abstract class PageActionAsync<T> extends Observable implements PageAction {

    public enum ActionStatus { DEFAULT, FAILURE, SUCCESS }
    
    private BaseAsyncCallback<T> callback;
    private ActionStatus status = ActionStatus.DEFAULT;
    
    @Override
    public final void run() {
        doNotify(ActionStatus.DEFAULT);
        callback = new BaseAsyncCallback<T>() {
            @Override
            public void onSuccess(T result) {
                doNotify(ActionStatus.SUCCESS);
                onCallbackSuccess(result);
            }              
            @Override
            public void onFailure(Throwable caught) {
                doNotify(ActionStatus.FAILURE);
                onCallbackFailure();
                super.onFailure(caught);
            }
        };
        runAsync(callback);
    }  
    
    private void doNotify(ActionStatus status) {
        this.status = status;
        notifyObservers();
    }
    
    public ActionStatus getStatus() {
        return status;
    }
    
    public abstract void runAsync(BaseAsyncCallback<T> callback);
    
    protected void onCallbackSuccess(T result) {       
    
    }
    
    protected void onCallbackFailure() {
        
    }
    
    public void cancelCallback() {
        doNotify(ActionStatus.FAILURE);
        onCallbackFailure();
    }
}
