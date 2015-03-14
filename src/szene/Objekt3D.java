package szene;

import java.io.Serializable;
import javax.vecmath.*;


public abstract class Objekt3D implements Serializable{
 
	public Vector3d position=new Vector3d(0,0,0);
	 
	public Vector3d skalierung=new Vector3d(1,1,1);
	
	//public Vector3d winkel=new Vector3d(0,0,0);

	// da zeigt das objekt hin 
	//public Vector3d face=new Vector3d(0,0,1);
	
	public Matrix4d rotationsMatrix = new EinheitsMatrix4d();

	protected Polygon3D[] triangulierung=null;
	protected Linie3D[] drahtgitter=null;
	public Material material=null;
	
	public Color3f farbe = new Color3f(1f,0f,0f); // nur zur Überbrückung bis Materialien implementiert sind   
	
	public Objekt3D()
	{
		// 
	}
	
	public Objekt3D(Vector3d position,Vector3d skalierung)
	{
		this.position.set(position);
		this.skalierung.set(skalierung);
		//this.winkel.set(winkel);
	}
	
	// NURBS
	
	//public Vector<MyPolygon> polygone = null;

	/** Erzeugt ein neues 3D - Objekt aus einer angegeben Polygonalisierung und Farbe mit angegebener Position
	 * @param polygone 
	 * @param position
	 * @param farbe
	 * 
	 */
	/*public Objekt3D(Vector<MyPolygon> polygone,Color3f farbe,Vector3d position) {
		this.position = position;
		this.farbe = farbe;
		this.polygone = polygone;
	}*/
	public void loescheTriangulierung()
	{
		triangulierung = null;
		drahtgitter = null;
	}
	
	public Polygon3D[] getTriangulierung()
	{
		return triangulierung;
	}

	public Linie3D[] getDrahtgitter()
	{
		return drahtgitter;
	}

}
