package nurbs;
import java.io.Serializable;//zum Speichern und Laden da
/**
 * stellt das Sweepen als Verschiebung entlang der z-Achse um eine Einheit zur Verfügung
 * @author marci
 *
 */
public class SweepVerschiebung extends NURBSFlaeche implements Serializable{
	public SweepVerschiebung(NURBSKurve kurve){
		this.l = kurve.ordnung;
		this.knotenvektor_L = kurve.knotenvektor;
		this.k = 2;
		this.knotenvektor_K = new double[4];
		this.knotenvektor_K[0] = 0d;
		this.knotenvektor_K[1] = 0d;
		this.knotenvektor_K[2] = 1d;
		this.knotenvektor_K[3] = 1d;
		
		this.kontrollnetz = new DeBoorPunkt3d[kurve.kontrollpolygon.length][2];
		for (int i = 0; i < kurve.kontrollpolygon.length; i++){
			this.kontrollnetz[i][0] = kurve.kontrollpolygon[i];
			this.kontrollnetz[i][1] = new DeBoorPunkt3d(kurve.kontrollpolygon[i].getX(), kurve.kontrollpolygon[i].getY()
					, kurve.kontrollpolygon[i].getZ() + 1.0, kurve.kontrollpolygon[i].getGewicht());
		}
	}
}