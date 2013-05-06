package org.ocram.reflection.proxy;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.reflection.proxy.objects.ClassInterface;
import org.ocram.reflection.proxy.objects.MessageInvocationHandler;
import org.ocram.reflection.proxy.objects.MethodRequest;
import org.ocram.reflection.proxy.objects.MethodRequestFactory;

public class ProxyMessageTest extends ScratchBaseTest {

    @Test
    public void createBasicRequest() { 
       ClassInterface operClass = MessageInvocationHandler.createRequest(); 
       int ohArg = 3;
       operClass.oh(ohArg);
       MethodRequest request = ((MethodRequestFactory) operClass).getRequest();
       
       assertTrue(request != null); 
       
       Object [] args = request.getArgs();
       assertTrue(args != null && args.length == 1 );
       assertTrue((Integer) args[0] == ohArg);
       assertTrue("oh".equals(request.getMethod().getName()));
    }
}
