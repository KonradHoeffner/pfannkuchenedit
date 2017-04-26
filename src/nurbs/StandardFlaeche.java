package nurbs;
import java.io.Serializable;//zum Speichern und Laden da
/**
 * erstellt eine quadratische Standardfläche mit anzahl*anzahl vielen Kontrollpunkten im Intervall
 * x in [-1, 1], y in [-1, 1]
 * anzahl muss dabei >= 3 sein
 * 
 * @author marci
 */
public class StandardFlaeche extends NURBSFlaeche implements Serializable{
	
	public StandardFlaeche(int anzahl){
		this.k = anzahl - 2;
		this.l = anzahl - 2;
		this.kontrollnetz = new DeBoorPunkt3d[anzahl][anzahl];
		double abstand = 2.0 / (((double) anzahl) - 1.0);
		//System.out.println(abstand);
		for (int i = 0; i < anzahl; i++){
			for (int j = 0; j < anzahl; j++){
				this.kontrollnetz[i][j] = new DeBoorPunkt3d((i*abstand) - 1.0, (j*abstand) - 1.0, 0.0, 1.0);
			}
		}
		this.knotenvektor_K = knotenvektor(this.kontrollnetz.length, k, true);
		this.knotenvektor_L = knotenvektor(this.kontrollnetz[0].length, l, true);
	}
	public StandardFlaeche(int anzahl, int k, int l){
		this.k = k;
		this.l = l;
		this.kontrollnetz = new DeBoorPunkt3d[anzahl][anzahl];
		double abstand = 2.0 / (((double) anzahl) - 1.0);
		//System.out.println(abstand);
		for (int i = 0; i < anzahl; i++){
			for (int j = 0; j < anzahl; j++){
				this.kontrollnetz[i][j] = new DeBoorPunkt3d((i*abstand) - 1.0, (j*abstand) - 1.0, 0.0, 1.0);
			}
		}
		this.knotenvektor_K = knotenvektor(this.kontrollnetz.length, this.k, true);
		this.knotenvektor_L = knotenvektor(this.kontrollnetz[0].length, this.l, true);
	}
	//nur für Testzwecke
	public DeBoorPunkt3d[][] getNetz(){
		return this.kontrollnetz;
	}
}