package org.ocram;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@TransactionManagement(TransactionManagementType.BEAN)
public class Scratch {

    public void syntax() { 
       Integer.parseInt("10"); 
    }
}
