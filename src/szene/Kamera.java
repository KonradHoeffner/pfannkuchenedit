package szene;

import java.io.Serializable;

import javax.vecmath.*;

/** Datenhaltungsklasse für die Kameravariablen.
 * Diese sind public (keine Getter und Setter), da sie uneingeschränkt manipuliert werden können
 * @author konrad */
public class Kamera implements Serializable{
	// Alles was näher an der Kamera ist wird geclippt 
	public double clippingNear = 0.1;
	// Alles was weiter weg von der Kamera ist wird geclippt
	public double clippingFar = 1000;
	// Winkel in Grad, der den Sichtwinkel in Y - Richtung angibt. Muss zwischen 0 und 180 liegen
	public double fovy = 45;
	// Dort steht die Kamera
	public Vector3d position;
	// Dort zeigt die Kamera hin
	public Vector3d ziel;
	// Zeigt nach oben
	public Vector3d oben;
	// Breite der Ebenenfenster (nicht in Pixeln sondern in Einheiten der Szenerie)
	public double breiteXY=10;
	public double breiteXZ=10;
	public double breiteYZ=10;
	// Positionen des Betrachters über den Ebenenfenstern
	public Point3d posXY=new Point3d(0,0,0);
	public Point3d posXZ=new Point3d(0,0,0);
	public Point3d posYZ=new Point3d(0,0,0);
	// Winkel der Ebenenfenster in Bogenmaß
	public double winkelXY=0;
	public double winkelXZ=0;
	public double winkelYZ=0;

	
	public void kameraVor(double anzahlEinheiten)
	{
		Vector3d nachVorne = getSichtVektor();
		nachVorne.scale(anzahlEinheiten);
		position.add(nachVorne);
		ziel.add(nachVorne);
	}

	public Vector3d getSichtVektor()
	{
		Vector3d sichtVektor = new Vector3d();
		sichtVektor.sub(position,ziel);
		sichtVektor.normalize();
		return sichtVektor;
	}

	public Vector3d getRechts()
	{
		Vector3d rechts = new Vector3d();
		// Weil bei OpenGL die z - Achse falschrum ist :-( , hier oben X sichtvektor und nicht andersrum
		rechts.cross(oben,getSichtVektor());
		rechts.normalize();
		return rechts;
	}

	/**
	 * Setzt Standardwerte. Zentriert die Sicht auf den Ursprung
	 */

	public Kamera() {
		clippingNear = 0.1;
		clippingFar  = 1000;
		fovy = 45;
		position= new Vector3d(0,0,15);
		ziel = new Vector3d(0,0,0);
		oben = new Vector3d(0,1,0);
		
	}

	/**
	 * @param clippingNear Alles was näher an der Kamera ist wird geclippt
	 * @param clippingFar Alles was weiter weg von der Kamera ist wird geclippt
	 * @param fovy Winkel in Grad, der den Sichtwinkel in Y - Richtung angibt. Muss zwischen 0 und 180 liegen
	 * @param position Dort steht die Kamera
	 * @param ziel Dort zeigt die Kamera hin
	 */
	public Kamera(double clippingNear, double clippingFar, double fovy, Vector3d position, Vector3d ziel) {
		this.clippingNear = clippingNear;
		this.clippingFar = clippingFar;
		this.fovy = fovy;
		this.position = position;
		this.ziel = ziel;
		oben = new Vector3d(0,1,0);
	}
}