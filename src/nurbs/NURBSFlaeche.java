package nurbs;

import java.io.Serializable;//zum Speichern und Laden da
import javax.vecmath.Vector3d;//nur für die letzten 2 Methoden benötigt, damit in der GUI mit Vector3d gearbeitet werden kann

/**
 * stellt eine Klasse zur Erzeugung einer Nurbs-Flaeche zur Verfügung
 * @author marci
 *
 */
public class NURBSFlaeche extends NURBSObjekt implements Serializable{
	public DeBoorPunkt3d[][] kontrollnetz;				//Matrix mit den Kontrollpunkten
	protected int k;									//Ordnung 1
	protected int l;									//Ordnung 2
	protected double[] knotenvektor_K;					//Knotenvektor für Ordnung 1
	protected double[] knotenvektor_L;					//Knotenvektor für Ordnung 2
	
	/**
	 * leerer Konstruktor, benötigt für die abgeleiteten Klassen
	 */
	public NURBSFlaeche(){}
	/**
	 * Konstruktor, der eine NURBSFlaeche mit einer Matrix von DeBoorPunkten, 2 Graden, 2 Knotenvektoren und einer boolean-Variable für die Bestimmung des Knotenvektors
	 * @param kontrollnetz		Matrix der DeBoorPunkte
	 * @param k					Ordnung 1  (Grad 1 = Ordnung 1 - 1)
	 * @param l					Ordnung 2  (Grad 2 = Ordnung 2 - 1)
	 * @param auswahl			true bedeutet halbe halbe, false bedeutet äquidistant
	 */
	public NURBSFlaeche(DeBoorPunkt3d[][] kontrollnetz, int k, int l, boolean auswahl){
		this.kontrollnetz = kontrollnetz;
		this.k = k;
		this.l = l;
		this.knotenvektor_K = knotenvektor(this.kontrollnetz.length, k, auswahl);
		this.knotenvektor_L = knotenvektor(this.kontrollnetz[0].length, l, auswahl);
	}
	/** 
	 * Copykonstruktor
	 * @param flaeche Instanz von NURBSFlaeche, von der eine Kopie erzeugt werden soll
	 */
	public NURBSFlaeche(NURBSFlaeche flaeche) {
		this.k = flaeche.k;
		this.l = flaeche.l;
		this.kontrollnetz = new DeBoorPunkt3d[flaeche.kontrollnetz.length][flaeche.kontrollnetz[0].length];
		for(int i = 0; i < kontrollnetz.length; i++){
			for(int j = 0; j < kontrollnetz[0].length; j++){
				this.kontrollnetz[i][j] = new DeBoorPunkt3d(flaeche.kontrollnetz[i][j]);
			}
		}
		this.knotenvektor_K = (double[]) flaeche.knotenvektor_K.clone();
		this.knotenvektor_L = (double[]) flaeche.knotenvektor_L.clone();
	}
	
	/**
	 * ruft auswerten entlang der Matrixzeilen und danach entlang der entstehenden Spalte auf
	 * @param s
	 * @param t
	 * @return					ausgewerteter Punkt an der Stelle (s, t)
	 */
	public DeBoorPunkt3d[] auswerten(double s, double t){
		DeBoorPunkt3d[] spalte = new DeBoorPunkt3d[this.kontrollnetz.length];
		for (int i = 0; i < spalte.length; i++){
			spalte[i] = this.auswerten(s, this.kontrollnetz[i], this.knotenvektor_K, this.k)[0];
		}
		return this.auswerten(t, spalte, this.knotenvektor_L, this.l);
	}
	/**
	 * auswerten wird auf 0 und 1 skaliert, bei Bedarf kann auch die Normale mit ausgelesen werden
	 * @param s
	 * @param t
	 * @return					ausgewerteter Punkt an der Stelle (s, t) mit zugehöriger Tangente
	 */
	public DeBoorPunkt3d[] auswerten_skaliert_mit_tangente(double s, double t){
		return this.auswerten(s * (this.getMaxKnoten_K()), t * (this.getMaxKnoten_L()));
	}
	/**
	 * auswerten wird auf 0 und 1 skaliert
	 * @param s
	 * @param t
	 * @return					ausgewerteter Punkt an der Stelle (s, t)
	 */
	public DeBoorPunkt3d auswerten_skaliert(double s, double t){
		return this.auswerten_skaliert_mit_tangente(s, t)[0];
	}
	/**
	 * ändert die Koordinaten des durch i und j spezifizierten Kontrollpunktes in die Koordinaten des Punktes neu
	 * @param i
	 * @param j
	 * @param neu				enthält die Koordinaten für den zu ändernden Kontrollpunkt
	 */
	public void setKontrollpunkt(int i, int j, Punkt3d neu){
		if ((i >= 0) && (i < this.kontrollnetz.length) && (j >= 0) && (j < this.kontrollnetz[0].length)){
			this.kontrollnetz[i][j].setX(neu.getX());
			this.kontrollnetz[i][j].setY(neu.getY());
			this.kontrollnetz[i][j].setZ(neu.getZ());
		}
	}
	/**
	 * ändert das Gewicht des durch i und j spezifizierten Kontrollpunktes
	 * @param i
	 * @param j
	 * @param gewicht			das neue Gewicht
	 */
	public void setGewicht(int i, int j, double gewicht){
		if ((i >= 0) && (i < this.kontrollnetz.length) && (j >= 0) && (j < this.kontrollnetz[0].length)){
			this.kontrollnetz[i][j].setGewicht(gewicht);
		}
	}
	/**
	 * gibt den durch i und j spezifizierten Kontrollpunkt zurück
	 * @param i
	 * @param j
	 * @return					
	 */
	public DeBoorPunkt3d getKontrollpunkt(int i, int j){
		return this.kontrollnetz[i][j];
	}
	/**
	 * liefert das letzte Element im k-Knotenvektor
	 * @return					höchster Wert im k-Knotenvektor
	 */
	public double getMaxKnoten_K(){
		return this.knotenvektor_K[this.knotenvektor_K.length - 1];
	}
	/**
	 * liefert das letzte Element im l-Knotenvektor
	 * @return					höchster Wert im l-Knotenvektor
	 */
	public double getMaxKnoten_L(){
		return this.knotenvektor_L[this.knotenvektor_L.length - 1];
	}
	/**
	 * liefert den k-Knotenvektor
	 * @return
	 */
	public double[] getKnotenvektor_K(){
		return this.knotenvektor_K;
	}
	/**
	 * liefert den l-Knotenvektor
	 * @return
	 */
	public double[] getKnotenvektor_L(){
		return this.knotenvektor_L;
	}
	/**
	 * setzt den k-Knotenvektor auf den übergebenen Knotenvektor
	 * @param neuerVektor
	 * @throws KnotenvektorException 
	 */
	public void setKnotenvektor_K(double[] neuerVektor) throws KnotenvektorException{
		if (neuerVektor.length == this.knotenvektor_K.length){
				this.checkKnotenvektor(neuerVektor);
				this.knotenvektor_K = (double[]) neuerVektor.clone();
			}
		 else throw new KnotenvektorException(false);
	}
	/**
	 * setzt den l-Knotenvektor auf den übergebenen Knotenvektor
	 * @param neuerVektor
	 * @throws KnotenvektorException 
	 */
	public void setKnotenvektor_L(double[] neuerVektor) throws KnotenvektorException{
		if (neuerVektor.length == this.knotenvektor_L.length){
			try {
				this.checkKnotenvektor(neuerVektor);
				this.knotenvektor_L = (double[]) neuerVektor.clone();
			} catch (KnotenvektorException ke){
				ke.printStackTrace();
			}
		} else throw new KnotenvektorException(false);
	}
	/**
	 * setzt den i-ten Wert des k-Knotenvektors auf den übergebenen Wert
	 * @param i
	 * @param wert
	 */
	public void setKnoten_K(int i, double wert){
		this.setKnoten(this.knotenvektor_K, i, wert);
	}
	/**
	 * setzt den i-ten Wert des l-Knotenvektors auf den übergebenen Wert
	 * @param i
	 * @param wert
	 */
	public void setKnoten_L(int i, double wert){
		this.setKnoten(this.knotenvektor_L, i, wert);
	}
	/**
	 * analog auswerten_skaliert_mit_tangente(s, t)		als return diesmal bloss ein Vector3d[] für die GUI
	 * @param s
	 * @param t
	 * @return
	 */
	public Vector3d[] auswerten_mit_tangente(double s, double t){
		Vector3d[] ergebnis = new Vector3d[2];
		DeBoorPunkt3d tmp = new DeBoorPunkt3d(this.auswerten_skaliert_mit_tangente(s, t)[0]);
		ergebnis[0] = new Vector3d(tmp.getX(), tmp.getY(), tmp.getZ());
		tmp = new DeBoorPunkt3d(this.auswerten_skaliert_mit_tangente(s, t)[1]);
		ergebnis[1] = new Vector3d(tmp.getX(), tmp.getY(), tmp.getZ());
		return ergebnis;
	}
	/**
	 * analog auswerten_skaliert(s, t)		als return diesmal bloss ein Vector3d für die GUI
	 * @param s
	 * @param t
	 * @return
	 */
	public Vector3d auswerten_ohne_tangente(double s, double t){
		DeBoorPunkt3d tmp = new DeBoorPunkt3d(this.auswerten_skaliert(s, t));
		Vector3d ergebnis = new Vector3d(tmp.getX(), tmp.getY(), tmp.getZ());
		return ergebnis;
	}
}