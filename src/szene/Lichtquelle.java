package szene;

import java.awt.Color;
import java.io.Serializable;

import javax.vecmath.Vector3d;

public class Lichtquelle implements Serializable{
 
	public Vector3d position;
	public Color farbe;

	/**
	 * @param position
	 * @param helligkeit
	 */
	public Lichtquelle(Vector3d position, Color farbe) {
		this.position = position;
		this.farbe = farbe;
	}
	
	public Lichtquelle()
	{
		this.position = new Vector3d(0,0,0);
		this.farbe = new Color(255,255,255);
	}
}
 
