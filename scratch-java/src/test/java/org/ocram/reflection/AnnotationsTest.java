package org.ocram.reflection;

import java.lang.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

@XmlAccessorType(XmlAccessType.FIELD)
public class AnnotationsTest extends ScratchBaseTest {

    @Test
    public void classLevelAnnotations() {
        Annotation anno = this.getClass().getAnnotation(XmlAccessorType.class);
        XmlAccessorType xmlAccessorType = (XmlAccessorType) anno;
        assertEquals( xmlAccessorType.value(), XmlAccessType.FIELD );

        anno = this.getClass().getAnnotation(XmlRootElement.class);
        assertNull( anno );
    }
}
