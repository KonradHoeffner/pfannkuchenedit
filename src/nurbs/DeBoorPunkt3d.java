package nurbs;
import java.io.Serializable;//zum Speichern und Laden da
import javax.vecmath.Tuple3d;//nur für den Konstruktor mit Tuple3d da

public class DeBoorPunkt3d extends Punkt3d implements Serializable{
	public double gewicht;
	//Konstruktoren
	public DeBoorPunkt3d() {
		super();
		this.gewicht = 0d; 
	}
	public DeBoorPunkt3d(double wert){
		super(wert);
		this.gewicht = wert;
	}
	public DeBoorPunkt3d(int x, int y, int z, int gewicht){
		super(x, y, z);
		this.gewicht = (double) gewicht;
	}
	public DeBoorPunkt3d(int wert) {
		super(wert);
		this.gewicht = (double) wert;
	}
	public DeBoorPunkt3d(double x, double y, double z, double gewicht){
		super(x, y, z);
		this.gewicht = gewicht;
	}
	public DeBoorPunkt3d(DeBoorPunkt3d punkt){
		this.gewicht = punkt.gewicht;
		this.x = punkt.x;
		this.y = punkt.y;
		this.z = punkt.z;
	}
	//nur für die GUI da
	public DeBoorPunkt3d(Tuple3d punkt){
		this.x = punkt.x;
		this.y = punkt.y;
		this.z = punkt.z;
		this.gewicht = 1.0;
	}
	//Methoden
	//Getter
	public double getGewicht() {
		return this.gewicht;
	}
	//Setter
	public void setGewicht(double gewicht) {
		this.gewicht = gewicht;
	}
	
}