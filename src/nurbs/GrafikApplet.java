package nurbs;
import java.applet.*;
import java.awt.Graphics;
import java.util.Date;
/** Created on 15.10.2005 */

public class GrafikApplet extends Applet{	
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
		Punkt3d verschiebung = new Punkt3d(100d);
		double scale = 2d;
		int ebene = 1;						//1 bedeutet xy-Ebene
											//2 bedeutet xz-Ebene
											//3 bedeutet yz-Ebene
											//default ist xy-Ebene
		
/*		int k = 22;
		DeBoorPunkt3d[] polygon = new DeBoorPunkt3d[24];
		polygon[0] = new DeBoorPunkt3d(-1.0, 1.0, 0.0, 1.0);
		polygon[1] = new DeBoorPunkt3d(-0.5, 0.5, 0.0, 1.0);
		polygon[2] = new DeBoorPunkt3d(0.0, 0.5, 0.0, 1.0);
		polygon[3] = new DeBoorPunkt3d(0.5, 1.0, 0.0, 1.0);
		polygon[4] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[5] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[6] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[7] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[8] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[9] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[10] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[11] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[12] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[13] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[14] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[15] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[16] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[17] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[18] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[19] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[20] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[21] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[22] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		polygon[23] = new DeBoorPunkt3d(1.0, 0.5, 0.0, 1.0);
		
		//false besagt äquidistant, true besagt halbe halbe
		NURBSKurve test = new NURBSKurve(polygon, k, false);
		
		for (int i = 0; i < polygon.length; i++){
			this.putKontrolle(g, (int) ((polygon[i].getX() + verschiebung.getX()) * scale), (int) ((polygon[i].getY() + verschiebung.getY()) * scale), i);
		}
		for (double t = 0d; t < 1d; t++){
			DeBoorPunkt3d punkt = test.auswerten_skaliert(t);
			this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getY() + verschiebung.getY()) * scale));
		}*/
		
		Kreis kreis = new Kreis();
		SektGlas glas = new SektGlas(75d);
		SweepRotation test = new SweepRotation(glas, kreis);
		//SweepGerade test = new SweepGerade(glas);
		
		//HuegelLandschaft test = new HuegelLandschaft();
		
		DeBoorPunkt3d[][] polygon = test.kontrollnetz;
		
		for (int i = 0; i < polygon.length; i++){
			for (int j = 0; j < polygon[0].length; j++){
				switch (ebene){
				case 0: {
					this.putKontrolle(g, (int) ((polygon[i][j].getX() + verschiebung.getX()) * scale), (int) ((polygon[i][j].getY() + verschiebung.getY()) * scale), i);
					break;
				}
				case 1: {
					this.putKontrolle(g, (int) ((polygon[i][j].getX() + verschiebung.getX()) * scale), (int) ((polygon[i][j].getZ() + verschiebung.getZ()) * scale), i);
					break;
				}
				case 2: {
					this.putKontrolle(g, (int) ((polygon[i][j].getY() + verschiebung.getY()) * scale), (int) ((polygon[i][j].getZ() + verschiebung.getZ()) * scale), i);
					break;
				}
				default: {
					this.putKontrolle(g, (int) ((polygon[i][j].getX() + verschiebung.getX()) * scale), (int) ((polygon[i][j].getY() + verschiebung.getY()) * scale), i);
					break;
				}
				}
			}
		}
		
		DeBoorPunkt3d punkt;
		
		for (double t = 0.0; t < 1.0; t += 0.001){
			for (double s = 0.0; s < 1.0; s += 0.01){
				switch (ebene){
				case 0: {
					punkt = test.auswerten_skaliert(s, t);
					this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getY() + verschiebung.getY()) * scale));
					break;
				}
				case 1: {
					punkt = test.auswerten_skaliert(s, t);
					this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getZ() + verschiebung.getZ()) * scale));
					break;
				}
				case 2: {
					punkt = test.auswerten_skaliert(s, t);
					this.putPixel(g, (int) ((punkt.getY() + verschiebung.getY()) * scale), (int) ((punkt.getZ() + verschiebung.getZ()) * scale));
					break;
				}
				default: {
					punkt = test.auswerten_skaliert(s, t);
					this.putPixel(g, (int) ((punkt.getX() + verschiebung.getX()) * scale), (int) ((punkt.getY() + verschiebung.getY()) * scale));
					break;
				}
				}
			}
		}
		System.out.println("fertig");
		long danach = (new Date()).getTime();		
		long zwischendrin = (danach-davor); // Zwischendrin enthält vergangene Zeit in Millisekunden
		System.out.println(zwischendrin + " ns");
	}
}