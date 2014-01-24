package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

import java.util.Comparator;

public class CostComparator implements Comparator<Pixel> {

	@Override
	public int compare(Pixel arg0, Pixel arg1) {
		// TODO Auto-generated method stub
		if(arg0.getF()>arg1.getF())
			return 1;
		else if(arg0.getF()<arg1.getF())
			return -1;
		return 0;
	}

}