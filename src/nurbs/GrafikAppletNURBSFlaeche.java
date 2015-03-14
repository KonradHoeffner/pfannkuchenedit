package nurbs;

import java.applet.*;
import java.awt.Graphics;
import java.util.Date;
/** Created on 15.10.2005 */

public class GrafikAppletNURBSFlaeche extends Applet{	
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
		Punkt3d verschiebung = new Punkt3d(150d);
		double scale = 1d;
		int ebene = 1;						//0 bedeutet xy-Ebene
											//1 bedeutet xz-Ebene
											//2 bedeutet yz-Ebene
											//default ist xy-Ebene
		Kreis kreis = new Kreis();
		SektGlas glas = new SektGlas(75d);
		SweepRotation test = new SweepRotation(glas, kreis);
//		SweepVerschiebung test = new SweepVerschiebung(glas);
		
//		HuegelLandschaft test = new HuegelLandschaft();
		
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
		
		for (double t = 0.0; t < 1.0; t += 0.1){
			for (double s = 0.0; s < 1.0; s += 0.1){
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