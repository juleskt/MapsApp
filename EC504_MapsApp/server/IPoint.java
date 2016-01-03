import java.util.Comparator;

public interface IPoint {

	public static Comparator<IPoint> xy_sorter =
		new Comparator<IPoint>() {

			public int compare(IPoint one, IPoint two) {
				double x = one.getX() - two.getX();
				if (x < 0) {
					return -1;
				}
				if (x > 0) {
					return 1;
				}
				double y = one.getY() - two.getY();
				if (y < 0 ) {
					return -1;
				}
				if (y > 0) {
					return 1;
				}
				
				return 0;
				
			}

	};	
	
	double getX();
	
	double getY();
}
