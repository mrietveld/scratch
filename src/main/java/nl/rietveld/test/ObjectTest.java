package nl.rietveld.test;

import static java.lang.System.out;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import nl.rietveld.test.objects.Bam;

public class ObjectTest {

	public ObjectTest() {}
	
	public void testMethodReflection( ) { 
		Bam bam = new Bam();
		Method SASMethod = null;
		String methodName = "geta_";
	
		Integer arr[] = new Integer [40];
		methods:
			for( int i = 1; i < 10; ++i ) {
				try {
					SASMethod = Bam.class.getDeclaredMethod(methodName + i, (Class[]) null);
				}
				catch( NoSuchMethodException nsme ) { 
					out.println( methodName + i + " does not exist." );
					continue methods;
				}
				if( SASMethod != null ) { 
					try {
						arr[i] = (Integer) SASMethod.invoke(bam);
						out.println( methodName + i + ": " + arr[i]);
					} catch (IllegalArgumentException iae) {
						out.println( iae.getClass().getSimpleName() + ": " + iae.getMessage() );
					} catch (IllegalAccessException iae) {
						out.println( iae.getClass().getSimpleName() + ": " + iae.getMessage() );
					} catch (InvocationTargetException ite) {
						out.println( ite.getClass().getSimpleName() + ": " + ite.getMessage() );
					}
				}
			}
	
	}

	public void testNullPointerLoop() { 
		String [] arguments = new String [10];
	
		String een,twe,dri,vie,fij;
	
		een = "Er was een ";
		twe = StringIndexOutOfBoundsException.class.getSimpleName();
		dri = "+";
		vie = null;
		fij = dri + dri + dri;
		
		Integer one,two,thr;
		
		one = 123123123;
		two = 45238934;
		thr = null;
	
		Bam ich = new Bam();
		Bam ni = null;
		
		for( int i = 0; i < 9; ) { 
	    	try {
	    		switch(i) { 
	    		case 0:
	    			arguments[i++] = ich.toString();
	    		case 1:
	    			arguments[i++] = een + twe + dri + vie + fij;
	    		case 2:
	    			arguments[i++] = vie.concat(een);
	    		case 3:
	    			arguments[i++] = Integer.toString(thr);
	    		case 4:
	    			arguments[i++] = Integer.toString(ich.geta_1());
	    		case 5:
	    			arguments[i++] = Integer.toString(ni.geta_4());
	    		case 6:
	    			arguments[i++] = dri + one + two;
	    		case 7: 
	    			arguments[i++] = thr.toString();
	    		case 8: 	
	    			arguments[i++] = "asdfasdffasdf" + fij;
	    		}
	    	}
	    	catch( NullPointerException npe ) { 
	    		arguments[i-1] = "";
	    	}
	    }
		
		for( int i = 0; i < 10; ++i ) { 
			out.println( "" + i + ": " + arguments[i] );
		}
	}

	public void testDataHandler() { 
		DataSource dataSource = null;
		DataHandler datahandler = new DataHandler(dataSource);
	}

	public void testClassReflection() { 
		Class intClass = int.class;
		
		out.println(intClass.getCanonicalName());
		intClass = Integer.class;
		out.println(intClass.getCanonicalName());
	}
	
}
