package bezier;
/**
 * stellt eine Klasse zur Erzeugung einer Bezier-Flaeche zur Verfügung
 * @author marci
 *
 */
public class BezierFlaeche extends BezierObjekt{
	public Punkt3d[][] kontrollnetz;			//Matrix mit den Kontrollpunkten
	/**
	 * leerer Konstruktor, benötigt für die abgeleiteten Klassen
	 */
	public BezierFlaeche(){}
	/**
	 * Konstruktor, der eine Bezier-Flaeche mit der übergebenen Matrix von Kontrollpunkten erstellt
	 * @param kontrollnetz
	 */
	public BezierFlaeche(Punkt3d[][] kontrollnetz){
		this.kontrollnetz = kontrollnetz;
	}
	/** 
	 * Copykonstruktor
	 * @param flaeche Instanz von BezierFlaeche, von der eine Kopie erzeugt werden soll
	 */
	public BezierFlaeche(BezierFlaeche flaeche) {
		this.kontrollnetz = new Punkt3d[flaeche.kontrollnetz.length][flaeche.kontrollnetz[0].length];
		for(int i = 0; i < kontrollnetz.length; i++){
			for(int j = 0; j < kontrollnetz[0].length; j++){
				this.kontrollnetz[i][j] = new Punkt3d(flaeche.kontrollnetz[i][j]);
			}
		}
	}
	
	public Punkt3d auswerten(double s, double t){
		Punkt3d[] spalte = new Punkt3d[this.kontrollnetz.length];
		for (int i = 0; i < spalte.length; i++){
			spalte[i] = this.auswerten(s, this.kontrollnetz[i]);
		}
		return this.auswerten(t, spalte);
	}
}