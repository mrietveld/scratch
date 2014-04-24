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
package org.ocram.arquillian.as7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocram.arquillian.as7.jms.JmsQueueServerSetupTask;
import org.ocram.arquillian.as7.security.UserPassCallbackHandler;
import org.ocram.arquillian.as7.security.jms.MessageEchoBean;

@RunWith(Arquillian.class)
public class MessageEchoBeanArquillianTest {
    
    @Deployment(name="mdb-echo-security-test")
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(MessageEchoBean.class, UserPassCallbackHandler.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private static final long QUALITY_OF_SERVICE_THRESHOLD_MS = 10 * 1000;

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory factory;

    @Resource(mappedName = "java:jboss/exported/jms/queue/" + MessageEchoBean.QUEUE_NAME)
    private Queue testQueue;

    @Test
    public void shouldBeAbleToSendMessage() throws Exception {
        String messageBody = "ping";

        Connection connection = null;
        Session session = null;
        try {
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageProducer producer = session.createProducer(testQueue);
            MessageConsumer consumer = session.createConsumer(tempQueue);

            connection.start();

            Message request = session.createTextMessage(messageBody);
            request.setJMSReplyTo(tempQueue);

            producer.send(request);
            Message response = consumer.receive(QUALITY_OF_SERVICE_THRESHOLD_MS);
            assertNotNull(response);
            String responseBody = ((TextMessage) response).getText();

            assertEquals("Should have responded with same message", messageBody, responseBody);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
