package bezier;

public abstract class BezierObjekt{
	
	protected Punkt3d auswerten(double t, Punkt3d[] kontrollpolygon){
		double eins_t = 1.0 - t;
		
		Punkt3d[][] matrix = new Punkt3d[kontrollpolygon.length][kontrollpolygon.length];
		for (int i = 0; i < kontrollpolygon.length; i++){
			matrix[i][0] = new Punkt3d(kontrollpolygon[i].getX(),
					kontrollpolygon[i].getY(), kontrollpolygon[i].getZ());
		}
		for (int j = 1; j < kontrollpolygon.length; j++){
			for (int i = 0; i < kontrollpolygon.length-j; i++){
				matrix[i][j] = new Punkt3d(
						(eins_t * matrix[i][j-1].getX()) + (t * matrix[i+1][j-1].getX()),
						(eins_t * matrix[i][j-1].getY()) + (t * matrix[i+1][j-1].getY()),
						(eins_t * matrix[i][j-1].getZ()) + (t * matrix[i+1][j-1].getZ()));
			}
		}
		
		return matrix[0][kontrollpolygon.length-1];
		
		/*Punkt3d[] d = new Punkt3d[kontrollpolygon.length];
		Punkt3d[] alt = new Punkt3d[kontrollpolygon.length];
		
		for (int i = 0; i < kontrollpolygon.length; i++){
			alt[i] = new Punkt3d(kontrollpolygon[i].getX(), kontrollpolygon[i].getY(), kontrollpolygon[i].getZ());
		}
		int j = 1;
		for (int i = 1; i < kontrollpolygon.length-j; i++){
			d[i] = new Punkt3d((eins_t * alt[i-1].getX()) + (t * alt[i].getX()),
					(eins_t * alt[i-1].getY()) + (t * alt[i].getY()),
					(eins_t * alt[i-1].getZ()) + (t * alt[i].getZ()));
			alt[i] = new Punkt3d(d[i].getX(), d[i].getY(), d[i].getZ());
			j++;
		}
		return d[kontrollpolygon.length/2];*/
	}
}