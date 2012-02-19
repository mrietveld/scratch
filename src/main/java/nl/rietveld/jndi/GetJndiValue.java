package nl.rietveld.jndi;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/**
 * TODO <Description>.
 *
 * @author 9304312 TODO replace number with real name
 * @version $Date: $ $Revision: $ $Name: $
 */
public class GetJndiValue {

	private static final String server 
		= "http://10.211.129.26:8080/";
	private static final String valueName 
		= "nl.bedrijf.services.AccountService.endpointAddress";
    /**
     * @param args
     * @throws NoSuchProviderException 
     * @throws NoSuchAlgorithmException 
     * @throws NamingException 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, NamingException {
        Hashtable env = new Hashtable();
        
        env.put( Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.HttpNamingContextFactory" );
        env.put( Context.PROVIDER_URL, server + "invoker/JNDIFactory" );
        Context initialContext = new InitialContext(env);
        
        String result = (String) initialContext.lookup(valueName);
        System.out.println(result);
        
    }

}
