package nurbs;

import java.io.Serializable;
import javax.vecmath.Vector3d;//nur für die letzten 2 Methoden benötigt, damit in der GUI mit Vector3d gearbeitet werden kann

/**
 * stellt eine Klasse zur Erzeugung einer Nurbs-Kurve zur Verfügung
 * @author marci
 *
 */
public class NURBSKurve extends NURBSObjekt implements Serializable{
	public DeBoorPunkt3d[] kontrollpolygon;				//Kontrollpolygon mit Länge l+1
	protected int ordnung;								//Polynomordnung
	protected double[] knotenvektor;					//Knotenvektor mit Länge l+ordnung
	
	public NURBSKurve(){}
	/**
	 * Konstruktor
	 * @param kontrollpolygon	das zur NURBS-Kurve gehörige Knotrollpolygon von DeBoor-Punkten
	 * @param ordnung			Ordnung der Nurbs-Kurve  (Grad = Ordnung-1)
	 * @param auswahl			nur wichtig für die Standardinitialisierung des Knotenvektors
	 */
	public NURBSKurve(DeBoorPunkt3d[] kontrollpolygon, int ordnung, boolean auswahl){
		this.kontrollpolygon = kontrollpolygon;
		this.ordnung = ordnung;
		this.knotenvektor = knotenvektor(this.kontrollpolygon.length, this.ordnung, auswahl);
	}
	/** 
	 * Copykonstruktor
	 * @param kurve				Instanz von NURBSKurve, von der eine Kopie erzeugt werden soll
	 */
	public NURBSKurve(NURBSKurve kurve) {
		this.ordnung = kurve.ordnung;
		this.kontrollpolygon = new DeBoorPunkt3d[kurve.kontrollpolygon.length];
		for(int i = 0; i < kontrollpolygon.length; i++){
			this.kontrollpolygon[i] = new DeBoorPunkt3d(kurve.kontrollpolygon[i]);		
		}
		this.knotenvektor = (double[]) kurve.knotenvektor.clone();
	}
	
	/**
	 * wertet die NURBSKurve an der Parameterstelle t mittels des DeBoor-Algorithmus aus und liefert bei Bedarf auch die zugehörige Tangente
	 * @param t
	 * @return
	 */
	public DeBoorPunkt3d[] auswerten(double t){
		return this.auswerten(t, this.kontrollpolygon, this.knotenvektor, this.ordnung);
	}
	/**
	 * wertet die NURBSKurve an der Parameterstelle t mittels des DeBoor-Algorithmus aus und liefert auch die zugehörige Tangente
	 * die Auswertung ist dabei auf das Intervall (0, 1) skaliert
	 * @param t
	 * @return
	 */
	public DeBoorPunkt3d[] auswerten_skaliert_mit_tangente(double t){
		return this.auswerten(t * (this.getMaxKnoten()));
	}
	/**
	 * wertet die NURBSKurve an der Parameterstelle t mittels des DeBoor-Algorithmus aus
	 * die Auswertung ist dabei auf das Intervall (0, 1) skaliert
	 * @param t
	 * @return
	 */
	public DeBoorPunkt3d auswerten_skaliert(double t){
		return this.auswerten_skaliert_mit_tangente(t)[0];
	}
	/**
	 * liefert das größte Element des Knotenvektors
	 * @return
	 */
	public double getMaxKnoten(){
		return this.knotenvektor[this.knotenvektor.length - 1];
	}
	/**
	 * liefert den aktuellen Knotenvektor der Nurbs-Kurve
	 * @return
	 */
	public double[] getKnotenvektor(){
		return this.knotenvektor;
	}
	/**
	 * die Koordinaten des i-ten Kontrollpunktes auf die Koordinaten des übergebenen Kontrollpunktes setzen
	 * @param i
	 * @param neu
	 */
	public void setKontrollpolygon(int i, Punkt3d neu){
		if ((i >= 0) && (i < this.kontrollpolygon.length)){
			this.kontrollpolygon[i].setX(neu.getX());
			this.kontrollpolygon[i].setY(neu.getY());
			this.kontrollpolygon[i].setZ(neu.getZ());
		}
	}
	/**
	 * das Gewicht des i-ten Kontrollpunktes auf den übergebenen Wert setzen
	 * @param i
	 * @param gewicht
	 */
	public void setGewicht(int i, double gewicht){
		if ((i >= 0) && (i < this.kontrollpolygon.length)){
			this.kontrollpolygon[i].setGewicht(gewicht);
		}
	}
	/**
	 * setzt den Knotenvektor auf den übergebenen Wert
	 * @param neuerVektor
	 * @throws KnotenvektorException 
	 */
	public void setKnotenvektor(double[] neuerVektor) throws KnotenvektorException{
		if (neuerVektor.length == this.knotenvektor.length){
			try {
				this.checkKnotenvektor(neuerVektor);
				this.knotenvektor = (double[]) neuerVektor.clone();
			} catch (KnotenvektorException ke){
				ke.printStackTrace();
			}
		} else throw new KnotenvektorException(false);
	}
	/**
	 * den i-ten Knoten im Knotenvektor auf den übergebenen Wert setzen
	 * @param i
	 * @param wert
	 */
	public void setKnoten(int i, double wert){
		this.setKnoten(this.knotenvektor, i, wert);
	}
	/**
	 * analog auswerten_skaliert_mit_tangente(t)		als return diesmal bloss ein Vector3d[] für die GUI
	 * @param t
	 * @return
	 */
	public Vector3d[] auswerten_mit_tangente(double t){
		Vector3d[] ergebnis = new Vector3d[2];
		DeBoorPunkt3d tmp = new DeBoorPunkt3d(this.auswerten_skaliert_mit_tangente(t)[0]);
		ergebnis[0] = new Vector3d(tmp.getX(), tmp.getY(), tmp.getZ());
		tmp = new DeBoorPunkt3d(this.auswerten_skaliert_mit_tangente(t)[1]);
		ergebnis[1] = new Vector3d(tmp.getX(), tmp.getY(), tmp.getZ());
		return ergebnis;
	}
	/**
	 * analog auswerten_skaliert(t)		als return diesmal bloss ein Vector3d für die GUI
	 * @param t
	 * @return
	 */
	public Vector3d auswerten_ohne_tangente(double t){
		DeBoorPunkt3d tmp = new DeBoorPunkt3d(this.auswerten_skaliert(t));
		Vector3d ergebnis = new Vector3d(tmp.getX(), tmp.getY(), tmp.getZ());
		return ergebnis;
	}
}