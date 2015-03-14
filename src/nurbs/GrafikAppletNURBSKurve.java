package nurbs;
import java.applet.*;
import java.awt.Graphics;
import java.util.Date;
/** Created on 15.10.2005 */

public class GrafikAppletNURBSKurve extends Applet{	
	public void erzeugen(){
		
	}
	public void putPixel(Graphics g,int x,int y){
		 g.drawRect(x, this.getHeight()-y, 2, 2);
	}
	public void putKontrolle(Graphics g, int x, int y, int zahl){
		g.drawRect(x, this.getHeight()-y, 7, 7);
		g.drawString(String.valueOf(zahl), x+8, this.getHeight()- y + 8);
	}
	public void init(){
		// Applet - Initialisierung
		this.setSize(1200,500);	
	}
	public void paint(Graphics g){
		long davor = (new Date()).getTime();
		Punkt3d verschiebung = new Punkt3d(10d);
		double scale = 15d;
		int ebene = 0;						//0 bedeutet xy-Ebene
											//1 bedeutet xz-Ebene
											//2 bedeutet yz-Ebene
											//default ist xy-Ebene
//		Kreis test = new Kreis();
//		SektGlas test = new SektGlas(75d);
		DeBoorPunkt3d[] kontrolle = new DeBoorPunkt3d[6];
		kontrolle[0] = new DeBoorPunkt3d(2d);
		kontrolle[1] = new DeBoorPunkt3d(4d, 8d, 0d, 1d);
		kontrolle[2] = new DeBoorPunkt3d(10d, 10d, 0d, 1d);
		kontrolle[3] = new DeBoorPunkt3d(20d, 8d, 0d, 1d);
		kontrolle[4] = new DeBoorPunkt3d(25d, 6d, 0d, 1d);
		kontrolle[5] = new DeBoorPunkt3d(30d, 10d, 0d, 1d);
		NURBSKurve test = new NURBSKurve(kontrolle, 6, true);
		
		DeBoorPunkt3d[] polygon = test.kontrollpolygon;
		
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
		DeBoorPunkt3d punkt;
		
		for (double t = 0.0; t < 1.0; t += 0.001){
			switch (ebene){
			case 0: {
				punkt = test.auswerten_skaliert(t);
				this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getY() + verschiebung.getY()) * scale));
				break;
			}
			case 1: {
				punkt = test.auswerten_skaliert(t);
				this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getZ() + verschiebung.getZ()) * scale));
				break;
			}
			case 2: {
				punkt = test.auswerten_skaliert(t);
				this.putPixel(g, (int) ((punkt.getY() + verschiebung.getY()) * scale), (int) ((punkt.getZ() + verschiebung.getZ()) * scale));
				break;
			}
			default: {
				punkt = test.auswerten_skaliert(t);
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