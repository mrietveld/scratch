package nl.rietveld.test.objects;

import java.util.Comparator;

public class BomComparator implements Comparator<Bom>{

    public int compare(Bom o1, Bom o2) {
        if( o2.getId() < o1.getId()) { 
            return -1;
        }
        else if( o2.getId() > o1.getId()) { 
            return 1;
        }
        return 0;
    }

}
