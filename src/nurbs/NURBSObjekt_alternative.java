package nurbs;

public class NURBSObjekt_alternative{
	protected DeBoorPunkt3d auswerten(double t, DeBoorPunkt3d[] kontrollpolygon, int[] knotenvektor, int grad){
		int index = -1;
		//wenn t>=maximalem Wert im Knotenvektor,
		if (t >= knotenvektor[knotenvektor.length - 1]){
			//dann suche minimalen Index mit maximalem Knotenwert und weise diesen zu
			for (int z = knotenvektor.length - 1; z > 0; z--){
				if (knotenvektor[z] > knotenvektor[z-1]){
					index = z;
				}
			}
		//wenn t<maximalem Wert im Knotenvektor,
		} else {
			//dann suche maximalen Index i mit knotenvektor[i]<=t
			index = 1;
			while (t >= knotenvektor[index + 1]){
				index++;
			}
		}
		
		DeBoorPunkt3d[][] d = new DeBoorPunkt3d[kontrollpolygon.length][grad+1];
		double alpha;
		double eins_alpha;
		double vorfaktor1;
		double vorfaktor2;
		
		for (int i = 0; i < d.length; i++){
			d[i][0] = kontrollpolygon[i];
		}
		
		for (int r = 0; r < grad + 1; r++){
			for (int i = index-grad; i < index-r; i++){
				alpha = knotenvektor[i+grad+1]-knotenvektor[i+r];
				if (Double.isNaN(alpha)){
					alpha = 0.0;
					eins_alpha = 1.0;
				} else {
					alpha = (t - knotenvektor[i+r]) / alpha;
					eins_alpha = 1 - alpha;
				}
				d[i][r] = new DeBoorPunkt3d(0.0, 0.0, 0.0,
						(eins_alpha * d[i][r-1].getGewicht()) + (alpha * d[r-1][i+1].getGewicht()));
				vorfaktor1 = eins_alpha * d[i][r-1].getGewicht() / d[r-1][i].getGewicht();
				vorfaktor2 = alpha * d[r-1][i+1].getGewicht() / d[r-1][i].getGewicht();
				d[i][r].setX((vorfaktor1 * d[i][r-1].getX()) + (vorfaktor2 * d[r-1][i+1].getX()));
				d[i][r].setY((vorfaktor1 * d[i][r-1].getY()) + (vorfaktor2 * d[r-1][i+1].getY()));
				d[i][r].setZ((vorfaktor1 * d[i][r-1].getZ()) + (vorfaktor2 * d[r-1][i+1].getZ()));
			}
		}
		return d[index-grad][grad];
	}
	protected int[] knotenvektor(int anzahlKontrollpolygon, int grad, boolean auswahl){
		int[] help = new int[anzahlKontrollpolygon + grad];
		
		if (auswahl){
			//Knotenvektor halbe halbe initialisieren
			for (int i = 0; i < help.length/2; i++){
				help[i] = 0;
			}
			for (int i = help.length/2; i < help.length; i++){
				help[i] = 1;
			}
		} else {
			//Knotenvektor äquidistant initialisieren
			int z;
			for (int i = 0; i < grad + 1; i++){
				help[i] = 0;
			}
			for (int i = grad + 2; i < help.length-grad; i++){
				z = i;
				help[i] = ++help[--z];
			}
			for (int i = (help.length) - grad - 1; i < help.length; i++){
				help[i] = anzahlKontrollpolygon - grad - 1;
			}
		}
		return help;
	}
}