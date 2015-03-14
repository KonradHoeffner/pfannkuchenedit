package nurbs;

import java.io.Serializable;//zum Speichern und Laden da

/**
 * liefert die NURBS-Darstellung für einen Kreis mit Radius radius um den Ursprung (0,0,0) in der YZ-Ebene
 * @author marci
 *
 */

public class Kreis extends NURBSKurve implements Serializable{
	private double radius;
	private Punkt3d ursprung;
	/**
	 * erstellt einen Einheits-Kreis um den Koordinatenursprung
	 */
	public Kreis(){
		this.radius = 1;
		this.ordnung = 3;
		this.ursprung = new Punkt3d(0.0);
		this.knotenvektor = this.knotenvektor();
		this.kontrollpolygon = this.kontrollpolygon();
	}
	/**
	 * erstellt einen Kreis um den Koordinatenursprung mit gegebenem Radius
	 * @param radius
	 */
	public Kreis(double radius){
		this.ordnung = 3;
		this.radius = radius;
		this.ursprung = new Punkt3d(0.0);
		this.knotenvektor = this.knotenvektor();
		this.kontrollpolygon = this.kontrollpolygon();
	}
	/**
	 * erstellt einen Kreis um den gegebenen Ursprung mit gegebenem Radius
	 * @param radius
	 * @param ursprung
	 */
	public Kreis(double radius, Punkt3d ursprung){
		this.ordnung = 3;
		this.radius = radius;
		this.ursprung = ursprung;
		this.knotenvektor = this.knotenvektor();
		this.kontrollpolygon = this.kontrollpolygon();		
	}
	
	private DeBoorPunkt3d[] kontrollpolygon(){
		DeBoorPunkt3d[] kontrollpolygon = new DeBoorPunkt3d[9];
		
		kontrollpolygon[0] = new DeBoorPunkt3d(0.0, this.radius + this.ursprung.getY(), this.ursprung.getZ(), 1.0);
		kontrollpolygon[1] = new DeBoorPunkt3d(0.0, this.radius + this.ursprung.getY(), this.radius + this.ursprung.getZ(), Math.sqrt(0.5));
		kontrollpolygon[2] = new DeBoorPunkt3d(0.0, this.ursprung.getY(), this.radius + this.ursprung.getZ(), 1.0);
		kontrollpolygon[3] = new DeBoorPunkt3d(0.0, -this.radius + this.ursprung.getY(), this.radius + this.ursprung.getZ(), Math.sqrt(0.5));
		kontrollpolygon[4] = new DeBoorPunkt3d(0.0, -this.radius + this.ursprung.getY(), this.ursprung.getZ(), 1.0);
		kontrollpolygon[5] = new DeBoorPunkt3d(0.0, -this.radius + this.ursprung.getY(), -this.radius + this.ursprung.getZ(), Math.sqrt(0.5));
		kontrollpolygon[6] = new DeBoorPunkt3d(0.0, this.ursprung.getY(), -this.radius + this.ursprung.getZ(), 1.0);
		kontrollpolygon[7] = new DeBoorPunkt3d(0.0, this.radius + this.ursprung.getY(), -this.radius + this.ursprung.getZ(), Math.sqrt(0.5));
		kontrollpolygon[8] = kontrollpolygon[0];
		
		/*kontrollpolygon[0] = new DeBoorPunkt3d(this.radius + this.ursprung.getX(), this.ursprung.getY(), 0.0, 1.0);
		kontrollpolygon[1] = new DeBoorPunkt3d(this.radius + this.ursprung.getX(), this.radius + this.ursprung.getY(), 0.0, Math.sqrt(0.5));
		kontrollpolygon[2] = new DeBoorPunkt3d(this.ursprung.getX(), this.radius + this.ursprung.getY(), 0.0, 1.0);
		kontrollpolygon[3] = new DeBoorPunkt3d(-this.radius + this.ursprung.getX(), this.radius + this.ursprung.getY(), 0.0, Math.sqrt(0.5));
		kontrollpolygon[4] = new DeBoorPunkt3d(-this.radius + this.ursprung.getX(), this.ursprung.getY(), 0.0, 1.0);
		kontrollpolygon[5] = new DeBoorPunkt3d(-this.radius + this.ursprung.getX(), -this.radius + this.ursprung.getY(), 0.0, Math.sqrt(0.5));
		kontrollpolygon[6] = new DeBoorPunkt3d(this.ursprung.getX(), -this.radius + this.ursprung.getY(), 0.0, 1.0);
		kontrollpolygon[7] = new DeBoorPunkt3d(this.radius + this.ursprung.getX(), -this.radius + this.ursprung.getY(), 0.0, Math.sqrt(0.5));
		kontrollpolygon[8] = kontrollpolygon[0];*/
		
		return kontrollpolygon;
	}
	private double[] knotenvektor(){
		double[] knotenvektor = {0d,0d,0d,1d,1d,2d,2d,3d,3d,4d,4d,4d};
		return knotenvektor;
	}
	public DeBoorPunkt3d[] getKontrollpolygon(){
		return this.kontrollpolygon;
	}
}