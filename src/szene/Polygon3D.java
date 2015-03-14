/** Created on 06.06.2006 */
package szene;

import java.io.Serializable;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class Polygon3D implements Serializable{

	public Tuple3d punkte[] = new Tuple3d[3];
	// Benutzung optional : Punktnormalen
	public Vector3d punktNormalen[] = new Vector3d[3];
	// Flächennormale
	public Vector3d normale;

//	private void berechneNormale()
//	{
//		Vector3d ab = new Vector3d();
//		ab.sub(punkte[1],punkte[0]);
//		Vector3d ac = new Vector3d();
//		ac.sub(punkte[2],punkte[0]);
//		normale = new Vector3d();
//		normale.cross(ab,ac);
//		normale.normalize();
//	}

	private void berechneNormale()
	{
		normale = VektorMethoden.berechneNormale(punkte[0],punkte[1],punkte[2]);
	}
	
	public Polygon3D(Tuple3d[] punkte) {
		this.punkte = punkte;
		berechneNormale();
	}

	public Polygon3D(Polygon3D polygon) {
		for(int i=0;i<3;i++)
		{
			punkte[i] = new Vector3d(polygon.punkte[i]);
			punktNormalen[i] = new Vector3d(polygon.punktNormalen[i]);
		}				
	}

	public Polygon3D(Tuple3d p1,Tuple3d p2,Tuple3d p3) {
		punkte[0]=p1;
		punkte[1]=p2;
		punkte[2]=p3;
		berechneNormale();
	}

	public Polygon3D(Tuple3d[] punkte,Vector3d[] punktNormalen) {
		this.punkte = punkte;
		this.punktNormalen = punktNormalen;
		berechneNormale();
	}

}
