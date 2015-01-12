/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.scratch.ws.eap;

import static org.scratch.ws.eap.ScratchWsWarJbossEapDeploy.createTestWar;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.scratch.ws.generated.PingWebServiceException;
import org.scratch.ws.tests.AbstractBaseWebServiceIntegrationTest;

@RunAsClient
@RunWith(Arquillian.class)
public class JbossEapScratchWebserviceIntegrationTest extends AbstractBaseWebServiceIntegrationTest {

    @Deployment(testable = false, name = "jboss-eap")
    public static Archive<?> createWar() {
        return createTestWar();
    }

    @ArquillianResource
    URL deploymentUrl;

    
    @Test
    public void plainTextWebserviceTest() throws Exception {
        pingServiceTest(deploymentUrl);
    }

}