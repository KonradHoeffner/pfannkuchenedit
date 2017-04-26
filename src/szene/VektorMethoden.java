/** Created on 07.06.2006 */
package szene;

import javax.vecmath.Matrix4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

public class VektorMethoden {

	/** Prüft, ob der Winkel des Vektors a < Winkel des Vektors b
	 * a und b müssen normiert sein! */
	public static boolean VektorWinkelReihenfolge(Vector2d a,Vector2d b)
	{
		// Beide Punkte oberhalb des Ursprungs : der linkere ist größer
		if(a.y>0&&b.y>0) if(a.x>b.x) return true; else return false;
		// Beide Punkte unterhalb des Ursprungs : der rechtere ist größer
		if(a.y<0&&b.y<0) if(a.x<b.x) return true; else return false;
		// Einer oberhalb, einer unterhalb : derjenige, der unten ist, ist größer
		if(a.y>0) return true;
		return false;
	}
	
	static public Vector3d berechneNormale(Tuple3d punkt1, Tuple3d punkt2,Tuple3d punkt3)
	{
		Vector3d ab = new Vector3d();
		ab.sub(punkt2,punkt1);
		Vector3d ac = new Vector3d();
		ac.sub(punkt3,punkt1);
		Vector3d normale = new Vector3d();
		normale.cross(ab,ac);
		normale.normalize();
		return normale;
	}

	static public Vector3d MatrixToVector(Matrix4d m)
	{
		Vector3d v = new Vector3d();
		v.x=m.getElement(0,0);
		v.y=m.getElement(1,0);
		v.z=m.getElement(2,0);
		return v;
	}
	
	static public Matrix4d VectorToMatrix(Tuple3d v)
	{
		//System.out.println("v:"+v);
		Matrix4d m = new Matrix4d();
		m.setColumn(0,new Vector4d(v));
		m.setElement(3,0,1);
		
		return m;
	}
	
	/** Wandelt eine 4x4 Matrix vom Typ Matrix4d in ein double - Feld der Länge 16 um.
	 * 
	 * @param matrix
	 * @return
	 */
	static public double[] MatrixToArray(Matrix4d matrix)
	{
		 double[] matrixArray = new double[16];
		 for(int j=0;j<4;j++)
			 for(int k=0;k<4;k++)
				 matrixArray[j*4+k]=matrix.getElement(k,j);
		 return matrixArray;
	}
	
	/** Liefert die Transformationsmatrix, um einen Punkt um eine beliebige Achse und 
	 * ein beliebiges Drehzentrum zu drehen. Die Transformationsmatrix muss von links anmultipliziert werden.
	 * @param punkt
	 * @param drehZentrum
	 * @param drehAchse
	 * @param winkelBogenmass
	 * @return
	 */
	static public Matrix4d rotierePunktMatrix(Vector3d drehZentrum, Vector3d drehAchse, double winkelBogenmass)
	{
		drehAchse.set(drehAchse);
		drehAchse.normalize();
		
		Matrix4d inDieXZEbene;
		Matrix4d inDieZAchse;
		Matrix4d wegVonDerZAchse;
		Matrix4d wegVonDerXZEbene;
		
		if(drehAchse.equals(new Vector3d(0,0,1)))
		{
			(inDieXZEbene = new Matrix4d()).setIdentity();
			(inDieZAchse = new Matrix4d()).setIdentity();
			(wegVonDerZAchse = new Matrix4d()).setIdentity();
			(wegVonDerXZEbene = new Matrix4d()).setIdentity();
		} else
		{
			double d = Math.sqrt(drehAchse.x*drehAchse.x+drehAchse.y*drehAchse.y);
			inDieXZEbene = new Matrix4d(
					drehAchse.x,drehAchse.y,0,0,
					-drehAchse.y,drehAchse.x,0,0,
					0,0,d,0,
					0,0,0,d);
			inDieXZEbene.mul(1f/d);
			inDieZAchse = new Matrix4d(
					drehAchse.z,0,-d,0,
					0,1,0,0,
					d,0,drehAchse.z,0,
					0,0,0,1);
			wegVonDerZAchse = new Matrix4d(
					drehAchse.z,0,d,0,
					0,1,0,0,
					-d,0,drehAchse.z,0,
					0,0,0,1);
			wegVonDerXZEbene = new Matrix4d(
					drehAchse.x,-drehAchse.y,0,0,
					drehAchse.y,drehAchse.x,0,0,
					0,0,d,0,
					0,0,0,d);
			wegVonDerXZEbene.mul(1f/d);
		}
		
		Matrix4d translation1 = new Matrix4d();
		translation1.setIdentity();
		translation1.setTranslation(new Vector3d(-drehZentrum.x,-drehZentrum.y,-drehZentrum.z));
		//System.out.println("Translation 1: "+translation1);
		Matrix4d rotationZ = new Matrix4d();
		rotationZ.setIdentity();
		rotationZ.rotZ(winkelBogenmass);
		//System.out.println("Rotation: "+rotation);
		Matrix4d translation2 = new Matrix4d();
		translation2.setIdentity();
		translation2.setTranslation(drehZentrum);
		//System.out.println("Translation 2: "+translation2);
		Matrix4d transformation = new Matrix4d();
		transformation.mul(translation2,wegVonDerXZEbene);
		transformation.mul(wegVonDerZAchse);
		transformation.mul(rotationZ);
		transformation.mul(inDieZAchse);
		transformation.mul(inDieXZEbene);
		transformation.mul(translation1);
		return transformation;
	}
	
	/** Rotiert einen Punkt um eine bliebige Drehachse und um ein beliebiges Drehzentrum
	 * @param punkt
	 * @param drehZentrum
	 * @param drehAchse
	 * @param winkelBogenmass
	 * @return
	 */
	static public Vector3d rotierePunkt(Vector3d punkt,Vector3d drehZentrum, Vector3d drehAchse, double winkelBogenmass)
	{
		/* Rotation um beliebige Drehachse:
		 * 0. Drehachse normieren
		 * 1. Punkt so transformieren, dass Drehachse in XZ - Ebene liegt
		 * 2. Punkt so transformieren, dass Drehachse = Z - Achse
		 * 3. Rotieren
		 * 4. zurücktransformieren
		 * Falls Drehachse nicht durch Ursprung geht vorher und nachher Translation durchführen
		 * weitere Informationen : siehe Skript Computergrafik Kapitel 3, Seite 24
		 
		 */
		// Referenz vermeiden -> kopieren
		
		//System.out.println("Trans: " + transformation);
		Matrix4d alterPunktMatrix = VectorToMatrix(punkt);
		Matrix4d neuerPunktMatrix = new Matrix4d();
		//Vector3d neuerPunkt = new Vector3d();
		neuerPunktMatrix.mul(rotierePunktMatrix(drehZentrum,drehAchse,winkelBogenmass),alterPunktMatrix);
		
		//System.out.println("np1:"+kamera.position);
		return MatrixToVector(neuerPunktMatrix);
		
	}
}
