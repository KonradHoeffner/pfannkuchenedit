package nurbs;
import java.io.Serializable;//zum Speichern und Laden da
/**
 * stellt das Sweepen als Rotation um die x-Achse zur Verfügung
 * @author marci
 *
 */
public class SweepRotation extends NURBSFlaeche implements Serializable{
	public SweepRotation(NURBSKurve kurve, Kreis kreis){
		this.kontrollnetz = new DeBoorPunkt3d[kurve.kontrollpolygon.length][kreis.kontrollpolygon.length];
		for (int i = 0; i < kurve.kontrollpolygon.length; i++){
			for (int j = 0; j < kreis.kontrollpolygon.length; j++){
				this.kontrollnetz[i][j] = new DeBoorPunkt3d(kurve.kontrollpolygon[i].getX()
						, kurve.kontrollpolygon[i].getY() * kreis.kontrollpolygon[j].getY()
						, kurve.kontrollpolygon[i].getY() * kreis.kontrollpolygon[j].getZ()
						, kurve.kontrollpolygon[i].getGewicht() * kreis.kontrollpolygon[j].getGewicht());
			}
		}
		this.l = kurve.ordnung;
		this.k = kreis.ordnung;
		this.knotenvektor_L = kurve.knotenvektor;
		this.knotenvektor_K = kreis.knotenvektor;
	}
	public SweepRotation(NURBSKurve kurve){
		Kreis kreis = new Kreis();
		this.kontrollnetz = new DeBoorPunkt3d[kurve.kontrollpolygon.length][kreis.kontrollpolygon.length];
		for (int i = 0; i < kurve.kontrollpolygon.length; i++){
			for (int j = 0; j < kreis.kontrollpolygon.length; j++){
				this.kontrollnetz[i][j] = new DeBoorPunkt3d(kurve.kontrollpolygon[i].getX()
						, kurve.kontrollpolygon[i].getY() * kreis.kontrollpolygon[j].getY()
						, kurve.kontrollpolygon[i].getY() * kreis.kontrollpolygon[j].getZ()
						, kurve.kontrollpolygon[i].getGewicht() * kreis.kontrollpolygon[j].getGewicht());
			}
		}
		this.l = kurve.ordnung;
		this.k = kreis.ordnung;
		this.knotenvektor_L = kurve.knotenvektor;
		this.knotenvektor_K = kreis.knotenvektor;
	}
}