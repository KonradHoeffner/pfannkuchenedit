package nurbs;
import java.io.Serializable;
/**
 * Oberklasse für NURBSFlaeche und NURBSKurve, die den DeBoor-Algorithmus zur Verfügung stellt
 * @author marci
 *
 */
public abstract class NURBSObjekt implements Serializable{
	/**
	 * die Implementierung des DeBoor-Algorithmus
	 * @param t
	 * @param kontrollpolygon
	 * @param knotenvektor
	 * @param ordnung
	 * @return			ein Feld, das zwei DeBoorPunkte enthält, der erste Punkt des Feldes ist der gesuchte Punkt, der zweite Punkt ist die zugehörige Tangente
	 */
	protected DeBoorPunkt3d[] auswerten(double t, DeBoorPunkt3d[] kontrollpolygon, double[] knotenvektor, int ordnung){
		//hier suche ich den korrekten Index
		if (t > knotenvektor[kontrollpolygon.length]) t = knotenvektor[kontrollpolygon.length];
		int index;
		for (index = ordnung - 1; index < kontrollpolygon.length-1; index++){
			if ((knotenvektor[index] <= t) && (t < knotenvektor[index+1])) break;
		}

/*		int index = ordnung - 1;
		//hier ist irgendwo noch ein fehler
		if (t > knotenvektor[kontrollpolygon.length]) t = knotenvektor[kontrollpolygon.length];
		while ((!((t >= knotenvektor[index]) && (t < knotenvektor[index + 1]))) && ( index < kontrollpolygon.length-1)) index++;
		//bis hierhin, aber ich find ihn nicht
*/		
		DeBoorPunkt3d[][] d = new DeBoorPunkt3d[index+1][ordnung];
		double alpha;
		double eins_alpha;
		double vorfaktor1;
		double vorfaktor2;
		
		/*d_i^0 = kontrollpolygon_i*/
		for (int i = index-ordnung+1; i < index+1; i++){
			d[i][0] = kontrollpolygon[i];
		}
		
		for (int j = 1; j < ordnung; j++){
			for (int i = index-ordnung+j+1; i < index+1; i++){
				//wenn alpha so klein wird, dass DIV BY ZERO passiert, dann standardmässig auf 0 setzen
				alpha = knotenvektor[i+ordnung-j] - knotenvektor[i];
				if (Double.isNaN(alpha) || (alpha == 0.0)){
					alpha = 0.0;
					eins_alpha = 1.0;
				//sonst alpha ganz normal berechnen
				} else {
					alpha = (t - knotenvektor[i]) / alpha;
					eins_alpha = 1 - alpha;
				}
				/*Gewichte w_i^j berechnen*/
				d[i][j] = new DeBoorPunkt3d(
						0.0,
						0.0,
						0.0,
						eins_alpha * d[i-1][j-1].getGewicht() + alpha * d[i][j-1].getGewicht());
				//d[i][j].getGewicht() kann nicht 0 werden, da Gewichte > 0 sein müssen, darum keine Behandlung
				vorfaktor1 = eins_alpha * d[i-1][j-1].getGewicht() / d[i][j].getGewicht();
				vorfaktor2 = alpha * d[i][j-1].getGewicht() / d[i][j].getGewicht();
				/*d_i^j berechnen*/
				d[i][j].setX((vorfaktor1 * d[i-1][j-1].getX()) + (vorfaktor2 * d[i][j-1].getX()));
				d[i][j].setY((vorfaktor1 * d[i-1][j-1].getY()) + (vorfaktor2 * d[i][j-1].getY()));
				d[i][j].setZ((vorfaktor1 * d[i-1][j-1].getZ()) + (vorfaktor2 * d[i][j-1].getZ()));
			}
		}
		DeBoorPunkt3d[] ergebnis = new DeBoorPunkt3d[2];
		DeBoorPunkt3d tangente = new DeBoorPunkt3d(d[index][ordnung-2].getX() - d[index-1][ordnung-2].getX(),
				d[index][ordnung-2].getY() - d[index-1][ordnung-2].getY(),
				d[index][ordnung-2].getZ() - d[index-1][ordnung-2].getZ(),
				1.0);
		ergebnis[0] = new DeBoorPunkt3d(d[index][ordnung - 1]);		//dies ist der gesuchte Punkt, der beim deBoor-Algo entsteht 
		ergebnis[1] = new DeBoorPunkt3d(tangente);					//dies ist die zum gesuchten Punkt gehörige Normale
		return ergebnis;
	}
	/**
	 * initialisiert standardmässig den Knotenvektor des NurbsObjektes
	 * @param anzahlKontrollpolygon
	 * @param ordnung
	 * @param auswahl		true bedeutet zur Hälfte 0 und zur anderen Hälfte 1; false bedeutet einen Knotenvektor im Stile eines B-Splines (0,0,0,1,2,3,4,5,5,5)
	 * @return
	 */
	protected double[] knotenvektor(int anzahlKontrollpolygon, int ordnung, boolean auswahl){
		double[] help = new double[anzahlKontrollpolygon + ordnung];
		
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
			for (int i = 0; i < ordnung + 1; i++){
				help[i] = 0;
			}
			for (int i = ordnung + 2; i < help.length-ordnung; i++){
				z = i;
				help[i] = ++help[--z];
			}
			for (int i = (help.length) - ordnung - 1; i < help.length; i++){
				help[i] = anzahlKontrollpolygon - ordnung - 1;
			}
		}
		return help;
	}
	/**
	 * überprüft den Knotenvektor auf Monotonie
	 * @param knotenvektor
	 * @throws KnotenvektorException		wenn keine Monotonie erfüllt ist, dann ist der Knotenvektor ungültig
	 */
	protected void checkKnotenvektor(double[] knotenvektor) throws KnotenvektorException{
		for (int i = 0; i < knotenvektor.length-1; i++){
			if (knotenvektor[i] <= knotenvektor[i+1]){
				;//alles okay, also mache nichts
			} else {
				throw new KnotenvektorException(true);
			}
		}
	}
	/**
	 * setzt den i-ten Wert im Knotenvektor auf den übergebenen Wert
	 * @param knotenvektor
	 * @param i
	 * @param wert
	 */
	protected void setKnoten(double[] knotenvektor, int i, double wert){
		try{
			knotenvektor[i] = wert;
			this.checkKnotenvektor(knotenvektor);
		} catch (KnotenvektorException ke){
			ke.printStackTrace();
		}
	}
}