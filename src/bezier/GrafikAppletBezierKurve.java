package bezier;
import java.applet.*;
import java.awt.Graphics;
import java.util.Date;

public class GrafikAppletBezierKurve extends Applet{
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
		long davor = (new Date()).getTime();
		Punkt3d verschiebung = new Punkt3d(150d);
		double scale = 1d;
		int ebene = 1;						//0 bedeutet xy-Ebene
											//1 bedeutet xz-Ebene
											//2 bedeutet yz-Ebene
											//default ist xy-Ebene
//		Kreis test = new Kreis();
		SektGlas test = new SektGlas(75d);
		
		Punkt3d[] polygon = test.kontrollpolygon;
		
		for (int i = 0; i < polygon.length; i++){
			switch (ebene){
			case 0: {
				this.putKontrolle(g, (int) ((polygon[i].getX() + verschiebung.getX()) * scale), (int) ((polygon[i].getY() + verschiebung.getY()) * scale), i);
				break;
			}
			case 1: {
				this.putKontrolle(g, (int) ((polygon[i].getX() + verschiebung.getX()) * scale), (int) ((polygon[i].getZ() + verschiebung.getZ()) * scale), i);
				break;
			}
			case 2: {
				this.putKontrolle(g, (int) ((polygon[i].getY() + verschiebung.getY()) * scale), (int) ((polygon[i].getZ() + verschiebung.getZ()) * scale), i);
				break;
			}
			default: {
				this.putKontrolle(g, (int) ((polygon[i].getX() + verschiebung.getX()) * scale), (int) ((polygon[i].getY() + verschiebung.getY()) * scale), i);
				break;
			}
			}
		}
		Punkt3d punkt;
		for (double t = 0d; t < 1d; t += 0.01){
			switch (ebene){
			case 0: {
				punkt = test.auswerten(t);
				this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getY() + verschiebung.getY()) * scale));
				break;
			}
			case 1: {
				punkt = test.auswerten(t);
				this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getZ() + verschiebung.getZ()) * scale));
				break;
			}
			case 2: {
				punkt = test.auswerten(t);
				this.putPixel(g, (int) ((punkt.getY() + verschiebung.getY()) * scale), (int) ((punkt.getZ() + verschiebung.getZ()) * scale));
				break;
			}
			default: {
				punkt = test.auswerten(t);
				this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getY() + verschiebung.getY()) * scale));
				break;
			}
			}
		}
		System.out.println("fertig");
		long danach = (new Date()).getTime();		
		long zwischendrin = (danach-davor); // Zwischendrin enthält vergangene Zeit in Millisekunden
		System.out.println(zwischendrin + " ns");
	}
}