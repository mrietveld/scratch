package org.ocram.test.rest.context;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.ocram.test.domain.MyType;

@ApplicationScoped
public class DeploymentClassGatherer {

    private Set<Class<?>> classList = new HashSet<Class<?>>();
  
    public DeploymentClassGatherer() { 
        classList.add(MyType.class);
    }
    
    public Set<Class<?>> getList() { 
        return classList;
    }
}
