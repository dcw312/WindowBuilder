/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.xml.xsom.impl.parser.state;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Dispatches incoming events into sub handlers appropriately
 * so that the interleaving semantics will be correctly realized.
 * 
 * @author Kohsuke Kawaguchi (kk@kohsuke.org)
 */
public abstract class NGCCInterleaveFilter implements NGCCEventSource, NGCCEventReceiver {
    protected NGCCInterleaveFilter( NGCCHandler parent, int cookie ) {
        this._parent = parent;
        this._cookie = cookie;
    }
    
    protected void setHandlers( NGCCEventReceiver[] receivers ) {
        this._receivers = receivers;
    }
    
    /** event receiverse. */
    protected NGCCEventReceiver[] _receivers;
    
    public int replace(NGCCEventReceiver oldHandler, NGCCEventReceiver newHandler) {
        for( int i=0; i<_receivers.length; i++ )
            if( _receivers[i]==oldHandler ) {
                _receivers[i]=newHandler;
                return i;
            }
        throw new InternalError(); // a bug in RelaxNGCC.
    }


    /** Parent handler. */
    private final NGCCHandler _parent;
    /** Cookie given by the parent. */
    private final int _cookie;



//
//
// event handler
//
//
    /**
     * Receiver that is being locked and therefore receives all the events.
     * <pre><xmp>
     * <interleave>
     *   <element name="foo"/>
     *   <element name="bar">
     *     <element name="foo"/>
     *   </element>
     * </interlaeve>
     * </xmp></pre>
     * When processing inside the bar element, this receiver is
     * "locked" so that it can correctly receive its child foo element.
     */
    private int lockedReceiver;
    /**
     * Nest level. Lock will be release when the lockCount becomes 0.
     */
    private int lockCount=0;
    
    public void enterElement(
        String uri, String localName, String qname,Attributes atts) throws SAXException {
        
        if(isJoining)   return; // ignore any token if we are joining. See joinByXXXX.
        
        if(lockCount++==0) {
            lockedReceiver = findReceiverOfElement(uri,localName);
            if(lockedReceiver==-1) {
                // we can't process this token. join.
                joinByEnterElement(null,uri,localName,qname,atts);
                return;
            }
        }
        
        _receivers[lockedReceiver].enterElement(uri,localName,qname,atts);
    }
    public void leaveElement(String uri, String localName, String qname) throws SAXException {
        if(isJoining)   return; // ignore any token if we are joining. See joinByXXXX.

        if( lockCount-- == 0 )
            joinByLeaveElement(null,uri,localName,qname);
        else
            _receivers[lockedReceiver].leaveElement(uri,localName,qname);
    }
    public void enterAttribute(String uri, String localName, String qname) throws SAXException {
        if(isJoining)   return; // ignore any token if we are joining. See joinByXXXX.
        
        if(lockCount++==0) {
            lockedReceiver = findReceiverOfAttribute(uri,localName);
            if(lockedReceiver==-1) {
                // we can't process this token. join.
                joinByEnterAttribute(null,uri,localName,qname);
                return;
            }
        }
                
        _receivers[lockedReceiver].enterAttribute(uri,localName,qname);
    }
    public void leaveAttribute(String uri, String localName, String qname) throws SAXException {
        if(isJoining)   return; // ignore any token if we are joining. See joinByXXXX.
        
        if( lockCount-- == 0 )
            joinByLeaveAttribute(null,uri,localName,qname);
        else
            _receivers[lockedReceiver].leaveAttribute(uri,localName,qname);
    }
    public void text(String value) throws SAXException {
        if(isJoining)   return; // ignore any token if we are joining. See joinByXXXX.
        
        if(lockCount!=0)
            _receivers[lockedReceiver].text(value);
        else {
            int receiver = findReceiverOfText();
            if(receiver!=-1)    _receivers[receiver].text(value);
            else                joinByText(null,value);
        }
    }



    /**
     * Implemented by the generated code to determine the handler
     * that can receive the given element.
     * 
     * @return
     *      Thread ID of the receiver that can handle this event,
     *      or -1 if none.
     */
    protected abstract int findReceiverOfElement( String uri, String local );
    
    /**
     * Returns the handler that can receive the given attribute, or null.
     */
    protected abstract int findReceiverOfAttribute( String uri, String local );
    
    /**
     * Returns the handler that can receive text events, or null.
     */
    protected abstract int findReceiverOfText();




//
//
// join method
//
//

    
    /**
     * Set to true when this handler is in the process of
     * joining all branches.
     */
    private boolean isJoining = false;

    /**
     * Joins all the child receivers.
     * 
     * <p>
     * This method is called by a child receiver when it sees
     * something that it cannot handle, or by this object itself
     * when it sees an event that it can't process.
     * 
     * <p>
     * This method forces children to move to its final state,
     * then revert to the parent.
     * 
     * @param source
     *      If this method is called by one of the child receivers,
     *      the receiver object. If this method is called by itself,
     *      null.
     */
    public void joinByEnterElement( NGCCEventReceiver source,
        String uri, String local, String qname, Attributes atts ) throws SAXException {
        
        if(isJoining)   return; // we are already in the process of joining. ignore.
        isJoining = true;

        // send special token to the rest of the branches.
        // these branches don't understand this token, so they will
        // try to move to a final state and send the token back to us,
        // which this object will ignore (because isJoining==true)
        // Otherwise branches will find an error.
        for( int i=0; i<_receivers.length; i++ )
            if( _receivers[i]!=source )
                _receivers[i].enterElement(uri,local,qname,atts);
        
        // revert to the parent
        _parent._source.replace(this,_parent);
        _parent.onChildCompleted(null,_cookie,true);
        // send this event to the parent
        _parent.enterElement(uri,local,qname,atts);
    }
    
    public void joinByLeaveElement( NGCCEventReceiver source,
        String uri, String local, String qname ) throws SAXException {
        
        if(isJoining)   return; // we are already in the process of joining. ignore.
        isJoining = true;

        // send special token to the rest of the branches.
        // these branches don't understand this token, so they will
        // try to move to a final state and send the token back to us,
        // which this object will ignore (because isJoining==true)
        // Otherwise branches will find an error.
        for( int i=0; i<_receivers.length; i++ )
            if( _receivers[i]!=source )
                _receivers[i].leaveElement(uri,local,qname);
        
        // revert to the parent
        _parent._source.replace(this,_parent);
        _parent.onChildCompleted(null,_cookie,true);
        // send this event to the parent
        _parent.leaveElement(uri,local,qname);
    }
    
    public void joinByEnterAttribute( NGCCEventReceiver source,
        String uri, String local, String qname ) throws SAXException {
        
        if(isJoining)   return; // we are already in the process of joining. ignore.
        isJoining = true;

        // send special token to the rest of the branches.
        // these branches don't understand this token, so they will
        // try to move to a final state and send the token back to us,
        // which this object will ignore (because isJoining==true)
        // Otherwise branches will find an error.
        for( int i=0; i<_receivers.length; i++ )
            if( _receivers[i]!=source )
                _receivers[i].enterAttribute(uri,local,qname);
        
        // revert to the parent
        _parent._source.replace(this,_parent);
        _parent.onChildCompleted(null,_cookie,true);
        // send this event to the parent
        _parent.enterAttribute(uri,local,qname);
    }
    
    public void joinByLeaveAttribute( NGCCEventReceiver source,
        String uri, String local, String qname ) throws SAXException {
        
        if(isJoining)   return; // we are already in the process of joining. ignore.
        isJoining = true;

        // send special token to the rest of the branches.
        // these branches don't understand this token, so they will
        // try to move to a final state and send the token back to us,
        // which this object will ignore (because isJoining==true)
        // Otherwise branches will find an error.
        for( int i=0; i<_receivers.length; i++ )
            if( _receivers[i]!=source )
                _receivers[i].leaveAttribute(uri,local,qname);
        
        // revert to the parent
        _parent._source.replace(this,_parent);
        _parent.onChildCompleted(null,_cookie,true);
        // send this event to the parent
        _parent.leaveAttribute(uri,local,qname);
    }
    
    public void joinByText( NGCCEventReceiver source,
        String value ) throws SAXException {

        if(isJoining)   return; // we are already in the process of joining. ignore.
        isJoining = true;
        
        // send special token to the rest of the branches.
        // these branches don't understand this token, so they will
        // try to move to a final state and send the token back to us,
        // which this object will ignore (because isJoining==true)
        // Otherwise branches will find an error.
        for( int i=0; i<_receivers.length; i++ )
            if( _receivers[i]!=source )
                _receivers[i].text(value);

        // revert to the parent
        _parent._source.replace(this,_parent);
        _parent.onChildCompleted(null,_cookie,true);
        // send this event to the parent
        _parent.text(value);
    }



//
//
// event dispatching methods
//
//
    
    public void sendEnterAttribute( int threadId,
        String uri, String local, String qname) throws SAXException {
        
        _receivers[threadId].enterAttribute(uri,local,qname);
    }

    public void sendEnterElement( int threadId,
        String uri, String local, String qname, Attributes atts) throws SAXException {
        
        _receivers[threadId].enterElement(uri,local,qname,atts);
    }

    public void sendLeaveAttribute( int threadId,
        String uri, String local, String qname) throws SAXException {
        
        _receivers[threadId].leaveAttribute(uri,local,qname);
    }

    public void sendLeaveElement( int threadId,
        String uri, String local, String qname) throws SAXException {
        
        _receivers[threadId].leaveElement(uri,local,qname);
    }

    public void sendText(int threadId, String value) throws SAXException {
        _receivers[threadId].text(value);
    }

}
