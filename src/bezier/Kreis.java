package bezier;

public class Kreis extends BezierKurve{
	private double radius;
	private Punkt3d ursprung;
	
	public Kreis(){
		this.radius = 1d;
		this.ursprung = new Punkt3d(0d);
		this.kontrollpolygon = this.kontrollpolygon();
	}
	
	public Kreis(double radius){
		this.radius = radius;
		this.ursprung = new Punkt3d(0d);
		this.kontrollpolygon = this.kontrollpolygon();
	}
	
	public Kreis(double radius, Punkt3d ursprung){
		this.ursprung = ursprung;
		this.radius = radius;
		this.kontrollpolygon = this.kontrollpolygon();
	}
	
	private Punkt3d[] kontrollpolygon(){
		Punkt3d[] kontrollpolygon = new Punkt3d[9];
		
		kontrollpolygon[0] = new Punkt3d(this.ursprung.getX(), this.ursprung.getY() + this.radius, this.ursprung.getZ());
		kontrollpolygon[1] = new Punkt3d(this.ursprung.getX(), this.ursprung.getY() + this.radius, this.ursprung.getZ() + this.radius);
		kontrollpolygon[2] = new Punkt3d(this.ursprung.getX(), this.ursprung.getY(), this.ursprung.getZ() + this.radius);
		kontrollpolygon[3] = new Punkt3d(this.ursprung.getX(), this.ursprung.getY() - this.radius, this.ursprung.getZ() + this.radius);
		kontrollpolygon[4] = new Punkt3d(this.ursprung.getX(), this.ursprung.getY() - this.radius, this.ursprung.getZ());
		kontrollpolygon[5] = new Punkt3d(this.ursprung.getX(), this.ursprung.getY() - this.radius, this.ursprung.getZ() - this.radius);
		kontrollpolygon[6] = new Punkt3d(this.ursprung.getX(), this.ursprung.getY(), this.ursprung.getZ() - this.radius);
		kontrollpolygon[7] = new Punkt3d(this.ursprung.getX(), this.ursprung.getY() + this.radius, this.ursprung.getZ() - this.radius);
		kontrollpolygon[8] = kontrollpolygon[0];
		
		return kontrollpolygon;
	}
}