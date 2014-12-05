package org.ocram.ws.config;

import javax.servlet.ServletConfig;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.ocram.ws.TestWebServiceImpl;

public class ScratchWsCxfServlet extends CXFNonSpringServlet {


    @Override
    public void loadBus(ServletConfig servletConfig) {
      super.loadBus(servletConfig);
      Bus bus = getBus();
      BusFactory.setDefaultBus(bus);
      Endpoint.publish("/ChangeStudent", new TestWebServiceImpl());
    }
}
