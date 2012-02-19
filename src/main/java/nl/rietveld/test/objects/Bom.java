package nl.rietveld.test.objects;

import java.util.Random;

public class Bom {

	private int id = 0;
	
	private static Random random = new Random();

	public Bom() { 
		id = random.nextInt(1000000);
	}
	
	public int getId() { 
		return id;
	}
}
