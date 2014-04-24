/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ocram.arquillian.as7.security.jms;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.InvalidDestinationException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.message.callback.GroupPrincipalCallback;

import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.ocram.arquillian.as7.security.UserPassCallbackHandler;

/**
 * MessageEchoBean
 * 
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 * @version $Revision: $
 */
@MessageDriven(
        activationConfig = { @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/" + MessageEchoBean.QUEUE_NAME ),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
@DeclareRoles({"admin"})
@PermitAll
@RunAs("admin")
public class MessageEchoBean implements MessageListener {

    public final static String QUEUE_NAME = "TEST";
    
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory factory;

    public void onMessage(Message message) {
        Subject sub = Subject.getSubject(AccessController.getContext());
        System.out.println("subject: " + (sub != null));
        if( sub != null ) { 
            System.out.println( "! " + sub.getClass().getName() );
            for( Principal prin : sub.getPrincipals() ) { 
                System.out.println( "!! " + prin.getName());
            }
        }
        SecurityContext sc = SecurityContextAssociation.getSecurityContext();
        System.out.println("secCtx: " + (sc != null));
        if( sc != null ) { 
            Map<String, Object> data = sc.getData();
            System.out.println("data: " + (data != null));
            if( data != null ) { 
                System.out.println("? data: " + data.getClass().getName());
                for( Entry<String, Object> entry : data.entrySet()) { 
                    System.out.println( "?? "  + entry.getKey() + ": " + (entry.getValue() == null ? "null" : entry.getValue().getClass().getName()));
                }
                data.put("org.jbpm.task.roles", "hahah!");
            }
        }
        
        System.out.println( "? " + SecurityContextAssociation.getSecurityContext().getData().get("org.jbpm.task.roles") );

        Connection connection = null;
        try {
            connection = factory.createConnection();
            connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createProducer(message.getJMSReplyTo()).send(message);
        } catch (InvalidDestinationException e) {
            System.out.println("Dropping invalid message" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Could not reply to message", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private Subject tryLogin() throws LoginException {
        String[] creds = { "mary", "mary123@" };
        CallbackHandler handler = new UserPassCallbackHandler(creds);
        LoginContext lc = new LoginContext("ApplicationRealm", handler);
        lc.login();
        return lc.getSubject();
    }
}
