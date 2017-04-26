package nurbs;
import java.applet.*;
import java.awt.Graphics;

public class GrafikApplet3d extends Applet{
	final double konstante = 10.0;
	final int faktor = 750;
	
	public void put3DPixel(Graphics g, double x, double y, double z){
		putPixel(g, (int) (faktor * (x / (z + konstante))),(int) (faktor * (y / (z + konstante))));
	}	
	public void putPixel(Graphics g,int x,int y){
		g.drawRect(x, this.getHeight() - y, 2, 2);
	}
	public void put3DKontrolle(Graphics g, double x, double y, double z, int zahl){
		putKontrolle(g, (int) (faktor * (x / (z + konstante))), (int) (faktor * (y / (z + konstante))), zahl);
	}
	public void putKontrolle(Graphics g, int x, int y, int zahl){
		g.drawRect(x, this.getHeight()-y, 7, 7);
		g.drawString(String.valueOf(zahl), x + 8, this.getHeight()- y + 8);
	}
	public void init(){
		// Applet - Initialisierung
		this.setSize(1200,500);
	}
	public void paint(Graphics g){
		/*Punkt3d[][] polygon = new Punkt3d[3][3];
		polygon[0][0] = new Punkt3d(1.0, 1.0, 2.0);polygon[0][1] = new Punkt3d(1.0, 2.0, 1.0); polygon[0][2] = new Punkt3d(1.0, 3.0, 0.0);
		polygon[1][0] = new Punkt3d(2.0, 1.0, 1.0);polygon[1][1] = new Punkt3d(2.0, 2.0, 1.0); polygon[1][2] = new Punkt3d(2.0, 3.0, 1.0);
		polygon[2][0] = new Punkt3d(3.0, 1.0, 0.0);polygon[2][1] = new Punkt3d(3.0, 2.0, 1.0); polygon[2][2] = new Punkt3d(3.0, 3.0, 2.0);
		
		for (int i = 0; i < polygon.length; i++){
			for (int j = 0; j < polygon[0].length; j++){
				this.put3DKontrolle(g, (int) polygon[i][j].getX(), (int) polygon[i][j].getY(), polygon[i][j].getZ(), i+j);
			}
		}
		BezierFlaeche test = new BezierFlaeche(polygon);*/
		
		StandardFlaeche test = new StandardFlaeche(25);
		Punkt3d punkt;
		for (double t = 0.0; t < 1.0; t += 0.01){
			for (double s = 0.0; s < 1.0; s += 0.01){
				punkt = test.auswerten_skaliert(t, s);
				this.put3DPixel(g, (int) punkt.getX(), (int) punkt.getY(), punkt.getZ());
			}
		}
		System.out.println("fertig");
	}
}