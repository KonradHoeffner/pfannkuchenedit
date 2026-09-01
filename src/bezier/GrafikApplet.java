package bezier;
import module java.base;
import module java.desktop;

public class GrafikApplet extends Applet{
	public void putPixel(Graphics g,int x,int y){
		 g.drawRect(x,this.getHeight()-y,2,2);
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
		Punkt3d verschiebung = new Punkt3d(150d);
		double scale = 1d;
		int ebene = 3;						//1 bedeutet xy-Ebene
											//2 bedeutet xz-Ebene
											//3 bedeutet yz-Ebene
											//default ist xy-Ebene
		
		Kreis kreis = new Kreis();
		SektGlas glas = new SektGlas(75d);
		//SweepGerade test = new SweepGerade(glas);
		SweepRotation test = new SweepRotation(glas, kreis);
		Punkt3d[][] polygon = test.kontrollnetz;
		
		for (int i = 0; i < polygon.length; i++){
			for (int j = 0; j < polygon[0].length; j++){
				switch (ebene){
				case 0 -> this.putKontrolle(g, (int) ((polygon[i][j].getX() + verschiebung.getX()) * scale), (int) ((polygon[i][j].getY() + verschiebung.getY()) * scale), i);
				case 1 -> this.putKontrolle(g, (int) ((polygon[i][j].getX() + verschiebung.getX()) * scale), (int) ((polygon[i][j].getZ() + verschiebung.getZ()) * scale), i);
				case 2 -> this.putKontrolle(g, (int) ((polygon[i][j].getY() + verschiebung.getY()) * scale), (int) ((polygon[i][j].getZ() + verschiebung.getZ()) * scale), i);
				default -> this.putKontrolle(g, (int) ((polygon[i][j].getX() + verschiebung.getX()) * scale), (int) ((polygon[i][j].getY() + verschiebung.getY()) * scale), i);
				}
			}
		}
		for (double t = 0d; t < 1d; t += 0.01){
			for (double s = 0d; s < 1d; s += 0.001){
				Punkt3d punkt = test.auswerten(s, t);
				switch (ebene){
				case 0 -> this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getY() + verschiebung.getY()) * scale));
				case 1 -> this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getZ() + verschiebung.getZ()) * scale));
				case 2 -> this.putPixel(g, (int) ((punkt.getY() + verschiebung.getY()) * scale), (int) ((punkt.getZ() + verschiebung.getZ()) * scale));
				default -> this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getY() + verschiebung.getY()) * scale));
				}
			}
		}
		System.out.println("fertig");
	}
}
