package nurbs;
import java.io.Serializable;//zum Speichern und Laden da

public class SektGlas extends NURBSKurve implements Serializable{
	private double scale;
	
	public SektGlas(){
		this.ordnung = 5;
		this.scale = 1d;
		this.kontrollpolygon = kontrollpolygon();
		this.knotenvektor = knotenvektor(this.kontrollpolygon.length, this.ordnung, true);
	}
	
	public SektGlas(double scale){
		this.ordnung = 5;
		this.scale = scale;
		this.kontrollpolygon = kontrollpolygon();
		this.knotenvektor = knotenvektor(this.kontrollpolygon.length, this.ordnung, true);
	}
	private DeBoorPunkt3d[] kontrollpolygon(){
		DeBoorPunkt3d[] kontrollpolygon = new DeBoorPunkt3d[5];
		kontrollpolygon[0] = new DeBoorPunkt3d(-1.0 * this.scale, 1.0 * this.scale, 0.0, 1.0);
		kontrollpolygon[1] = new DeBoorPunkt3d(-0.9 * this.scale, 0.1 * this.scale, 0.0, 10.0);
		kontrollpolygon[2] = new DeBoorPunkt3d(0.0 * this.scale, 0.1 * this.scale, 0.0, 20.0);
		kontrollpolygon[3] = new DeBoorPunkt3d(0.3 * this.scale, 1.6 * this.scale, 0.0, 3.0);
		kontrollpolygon[4] = new DeBoorPunkt3d(1.0 * this.scale, 1.6 * this.scale, 0.0, 1.0);
		
		return kontrollpolygon;
	}
}