package bezier;
public class Punkt3d {
	//Attribute
	public double x;
	public double y;
	public double z;
	//Konstruktoren
	public Punkt3d(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Punkt3d(int x, int y, int z){
		this.x = (double) x;
		this.y = (double) y;
		this.z = (double) z;
	}
	public Punkt3d(double wert){
		this.x = wert;
		this.y = wert;
		this.z = wert;
	}
	public Punkt3d(int wert){
		this.x = (double) wert;
		this.y = (double) wert;
		this.z = (double) wert;
	}
	public Punkt3d(){
		this.x = 0d;
		this.y = 0d;
		this.z = 0d;
	}
	public Punkt3d(Punkt3d punkt){
		this.x = punkt.x;
		this.y = punkt.y;
		this.z = punkt.z;
	}
	//Methoden
	//Getter
	public double getX(){
		return this.x;
	}
	public double getY(){
		return this.y;
	}
	public double getZ(){
		return this.z;
	}
	//Setter
	public void setX(double wert){
		this.x = wert;
	}
	public void setY(double wert){
		this.y = wert;
	}
	public void setZ(double wert){
		this.z = wert;
	}
	public void set(double x, double y, double z){
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	public void set(Punkt3d punkt){
		this.setX(punkt.getX());
		this.setY(punkt.getY());
		this.setZ(punkt.getZ());
	}
}