package org.ocram.jaxb.objects.inheritance;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name=CoreObject.xmlName)
public class ImplObject extends CoreObject {

    public void execute() {
        one = one + "one";
        System.out.println(": one");
    }
}
