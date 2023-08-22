/** Created on 12.06.2006 */
package szene;

import java.io.Serializable;
import java.util.Vector;

import javax.vecmath.Point3d;

import nurbs.DeBoorPunkt3d;
import nurbs.NURBSKurve;

/** Klasse, die eine NURBS Kurve enthält sowie Methoden und Angaben zur Darstellung.  
 * Triangulation ist nur für Auswahl da. 
 * Zum Anzeigen gibt es nur das Gitternetz.
 *  */
public class Kurve3D extends Objekt3D implements Serializable{

	protected Point3d[] punkte = null;
	public NURBSKurve nurbsKurve = null;
	
	/**
	 * übernimmt die angegebene NURBSKurve und setzt Standardwerte für die Darstellung
	 */
	public Kurve3D(NURBSKurve nurbsKurve) {
		this.nurbsKurve = nurbsKurve;
	}
	
	/** Copykonstruktur
	 * @param kurve
	 */
	public Kurve3D(Kurve3D kurve)
	{
		this.nurbsKurve = new NURBSKurve(kurve.nurbsKurve);
		this.position.set(kurve.position);
		this.rotationsMatrix.set(kurve.rotationsMatrix);
		this.skalierung.set(kurve.skalierung);
	}

	public synchronized Linie3D[] getDrahtgitter()
	{
		if(drahtgitter==null) erstelleDrahtgitter();
		return drahtgitter;
	}
	
	private synchronized void erstelleDrahtgitter() {
		if(punkte==null) erstellePunkte(Szene.quality);
		Vector<Linie3D> linien= new Vector<Linie3D>();
		System.out.println(punkte.length);
		for(int i=0;i<punkte.length-1;i++)
				linien.add(new Linie3D(punkte[i],punkte[i+1]));		
		drahtgitter = linien.toArray(new Linie3D[0]);
	}
	
	protected synchronized void  erstellePunkte(int unterteilung)
	{
		punkte = new Point3d[unterteilung];

		for(int i=0;i<unterteilung;i++)
		{
				DeBoorPunkt3d punkt = null;
				punkt = nurbsKurve.auswerten_skaliert((double)(i)/(punkte.length-1));
				punkte[i] = new Point3d(punkt.getX(),punkt.getY(),punkt.getZ());
			}
	}

	private void erstelleTriangulierung() {
		if(punkte==null) erstellePunkte(Szene.quality);
		
	}

	public Polygon3D[] getTriangulierung()
	{
		if(triangulierung==null) erstelleTriangulierung();
		return triangulierung;
	}


	public void update()
	{
		this.punkte = null;
		this.drahtgitter = null;
	}
}
