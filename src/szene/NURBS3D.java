/** Created on 09.06.2006 */
package szene;

import java.io.Serializable;
import java.util.Vector;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import nurbs.DeBoorPunkt3d;
import nurbs.NURBSFlaeche;

/** Enthält eine NURBS - Fläche sowie Angaben zur Darstellung in der Szene und Methoden zum Erzeugen der Triangulierung */
public class NURBS3D extends Objekt3D implements Serializable{

	public NURBSFlaeche nurbsFlaeche = null;
	protected Point3d[][] punkte = null;
	protected Vector3d[][] punktNormalen;
	
	//protected int unterteilung;
	
	/**
	 * @param nurbsFlaeche
	 */
	public NURBS3D(NURBSFlaeche nurbsFlaeche) {
		this.nurbsFlaeche = nurbsFlaeche;
	}

	
	/**
	 * @param position Startposition der NURBS-Fläche
	 * @param skalierung Skalierung in x,y und z - Richtung
	 * @param winkel Winkel in Bogenmaß für die Drehung um die X, Y und Z Achse (in dieser Reihenfolge)
	 */
	public NURBS3D(NURBSFlaeche nurbsFlaeche,Vector3d position, Vector3d skalierung) {
		super(position, skalierung);
		this.nurbsFlaeche = nurbsFlaeche;
	}

	/** Copykonstruktor
	 * @param nurbs Instanz von NURBS3D, von der die Kopie angelegt werden soll 
	 */
	public NURBS3D(NURBS3D nurbs)
	{
		this.nurbsFlaeche = new NURBSFlaeche(nurbs.nurbsFlaeche);
		this.position.set(nurbs.position);
		this.rotationsMatrix.set(nurbs.rotationsMatrix);
		this.skalierung.set(nurbs.skalierung);
		this.material=nurbs.material;
		
	}
	
	
	protected synchronized void  erstellePunkte(int unterteilung)
	{
		punkte = new Point3d[unterteilung][unterteilung];
		punktNormalen = new Vector3d[unterteilung][unterteilung];
		for(int i=0;i<unterteilung;i++)
			for(int j=0;j<unterteilung;j++)

		{
				DeBoorPunkt3d punkt = null;
//				try
//				{
				punkt = nurbsFlaeche.auswerten_skaliert((double)(i)/(punkte.length-1),(double)(j)/(punkte.length-1));
//				}
//				catch (Exception e)
//				{
//					
//				}
//				if(punkt==null) punkt = new DeBoorPunkt3d(0,0,0,1);
				punkte[i][j] = new Point3d(punkt.getX(),punkt.getY(),punkt.getZ());
			}
		// Normalen berechnen
		// Rand : gleich den Polygonnormalen 
		// unterer Rand
		for(int i=0;i<unterteilung-1;i++)
			punktNormalen[i][0] = 
				VektorMethoden.berechneNormale(punkte[i][0],punkte[i][1],punkte[i+1][0]);
		// oberer Rand
		for(int i=1;i<unterteilung;i++)
			punktNormalen[i][unterteilung-1] = 
				VektorMethoden.berechneNormale(punkte[i][unterteilung-1],punkte[i][unterteilung-2],punkte[i-1][unterteilung-1]);
		
		// linke Seite
		for(int j=1;j<unterteilung;j++)
			punktNormalen[0][j] = 
				VektorMethoden.berechneNormale(punkte[0][j],punkte[1][j],punkte[0][j-1]);	
		// rechte Seite			
		for(int j=0;j<unterteilung-1;j++)
			punktNormalen[unterteilung-1][j] = 
				VektorMethoden.berechneNormale(punkte[unterteilung-1][j],punkte[unterteilung-2][j],punkte[unterteilung-1][j+1]);	

		// Mitte : Mittelwert der angrenzenden Polygonnormalen
		// Todo irgenwann : Laufzeit Algorithmus optimieren unter Verwendung von Zwischenergebnissen
		for(int i=1;i<unterteilung-1;i++)
			for(int j=1;j<unterteilung-1;j++)
			{
				// oben links
				Vector3d n1 = VektorMethoden.berechneNormale(punkte[i][j],punkte[i-1][j],punkte[i][j+1]);
				// oben rechts
				Vector3d n2 = VektorMethoden.berechneNormale(punkte[i][j],punkte[i][j+1],punkte[i+1][j]);
				// unten links
				Vector3d n3 = VektorMethoden.berechneNormale(punkte[i][j],punkte[i][j-1],punkte[i-1][j]);
				// unten rechts
				Vector3d n4 = VektorMethoden.berechneNormale(punkte[i][j],punkte[i+1][j],punkte[i][j-1]);
				Vector3d punktNormale = new Vector3d();
				punktNormale.add(n1,n2);
				punktNormale.add(n3);
				punktNormale.add(n4);
				//punktNormale.scale(0.25); // wird ja eh wieder normalisiert später
				punktNormalen[i][j] = punktNormale;
			}
		// Orientierung scheint entgegengesetzt zur Flächennormale zu sein -> umändern
		for(int i=0;i<unterteilung;i++)
			for(int j=0;j<unterteilung;j++)
			{
				punktNormalen[i][j].normalize();
				punktNormalen[i][j].scale(-1);
			}
				

	}

	protected synchronized void erstelleTriangulierung(int unterteilung)
	{
		Vector<Polygon3D> polygone= new Vector<Polygon3D>();
		if(punkte==null) erstellePunkte(unterteilung);
		for(int i=0;i<unterteilung-1;i++)
			for(int j=0;j<unterteilung-1;j++)
			{
				// oberes Dreieck
				polygone.add(new  Polygon3D(new Point3d[] {punkte[i][j],punkte[i+1][j],punkte[i][j+1]},
						new Vector3d[] {punktNormalen[i][j],punktNormalen[i+1][j],punktNormalen[i][j+1]})); 
				// unteres Dreieck
				polygone.add(new Polygon3D(new Point3d[]{punkte[i+1][j],punkte[i+1][j+1],punkte[i][j+1]},
						new Vector3d[]{punktNormalen[i+1][j],punktNormalen[i+1][j+1],punktNormalen[i][j+1]}));
			}
		triangulierung = polygone.toArray(new Polygon3D[0]);
	}

	protected synchronized void erstelleTriangulierung()
	{
		erstelleTriangulierung(Szene.quality);
	}
	
	public synchronized Polygon3D[] getTriangulierung()
	{
		if(triangulierung==null) erstelleTriangulierung(); 
		return triangulierung;
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
		System.out.println(punkte[0].length);
		for(int i=0;i<punkte.length-1;i++)
			for(int j=0;j<punkte[0].length;j++)
				linien.add(new Linie3D(punkte[i][j],punkte[i+1][j]));
		for(int i=0;i<punkte.length;i++)
			for(int j=0;j<punkte[0].length-1;j++)
				linien.add(new Linie3D(punkte[i][j],punkte[i][j+1]));
			
				
		drahtgitter = linien.toArray(new Linie3D[0]);
	}


	/**
	 * Nach Änderungen an den Kontrollpunkten aufrufen um die Triangulierung zu erneuern
	 */
	public synchronized void update()
	{
		punkte = null;
		triangulierung = null;
		drahtgitter = null;
	}

}
