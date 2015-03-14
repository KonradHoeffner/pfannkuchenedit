package bezier;

public class SektGlas extends BezierKurve{
	private Punkt3d verschiebung;
	private double scale;
	
	public SektGlas(){
		this.verschiebung = new Punkt3d(0d);
		this.scale = 1d;
		this.kontrollpolygon = this.kontrollpolygon();
	}
	public SektGlas(double scale){
		this.verschiebung = new Punkt3d(0d);
		this.scale = scale;
		this.kontrollpolygon = this.kontrollpolygon();
	}
	public SektGlas(double scale, Punkt3d verschiebung){
		this.verschiebung = verschiebung;
		this.scale = scale;
		this.kontrollpolygon = this.kontrollpolygon();
	}
	
	private Punkt3d[] kontrollpolygon(){
		Punkt3d[] kontrollpolygon = new Punkt3d[5];
		
		kontrollpolygon[0] = new Punkt3d((this.verschiebung.getX() - 1.0) * this.scale, (this.verschiebung.getY() + 1.0) * this.scale, this.verschiebung.getZ() * this.scale);
		kontrollpolygon[1] = new Punkt3d((this.verschiebung.getX() - 0.8) * this.scale, (this.verschiebung.getY() + 0.1) * this.scale, this.verschiebung.getZ() * this.scale);
		kontrollpolygon[2] = new Punkt3d((this.verschiebung.getX() + 0.8) * this.scale, (this.verschiebung.getY() + 0.1) * this.scale, this.verschiebung.getZ() * this.scale);
		kontrollpolygon[3] = new Punkt3d((this.verschiebung.getX() + 1.0) * this.scale, (this.verschiebung.getY() + 1.5) * this.scale, this.verschiebung.getZ() * this.scale);
		kontrollpolygon[4] = new Punkt3d((this.verschiebung.getX() + 2.2) * this.scale, (this.verschiebung.getY() + 1.5) * this.scale, this.verschiebung.getZ() * this.scale);
		
		return kontrollpolygon;
	}
}