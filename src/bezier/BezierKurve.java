package bezier;
/**
 * stellt eine Klasse zur Erzeugung einer Bezier-Kurve zur Verfügung
 * @author marci
 *
 */
public class BezierKurve extends BezierObjekt{
	public Punkt3d[] kontrollpolygon;				//Kontrollpolygon
	/**
	 * leerer Konstruktor, benötigt für die abgeleiteten Klassen
	 */
	public BezierKurve(){}
	
	public BezierKurve(Punkt3d[] kontrollpolygon){
		this.kontrollpolygon = kontrollpolygon;
	}
	/** 
	 * Copykonstruktor
	 * @param kurve Instanz von BezierKurve, von der eine Kopie erzeugt werden soll
	 */
	public BezierKurve(BezierKurve kurve) {
		this.kontrollpolygon = new Punkt3d[kurve.kontrollpolygon.length];
		for(int i = 0; i < kontrollpolygon.length; i++){
			this.kontrollpolygon[i] = new Punkt3d(kurve.kontrollpolygon[i]);
		}
	}
	
	public Punkt3d auswerten(double t){
		return this.auswerten(t, this.kontrollpolygon);
	}
}