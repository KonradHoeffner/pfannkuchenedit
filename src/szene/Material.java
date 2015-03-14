package szene;

import java.awt.Color;
import java.io.Serializable;

/**  
 Datenhaltungsklasse für Materialien mit ihren Eigenschaften
 @author konrad
  */
public class Material implements Serializable{

	public String name;
	public Color ambient,diffus,spiegelnd;
	public byte glanz;
	
	public Material(/*String name, */Color ambient, Color diffus, Color spiegelnd, byte glanz) {
		//this.name = name;
		this.ambient = ambient;
		this.diffus = diffus;
		this.spiegelnd = spiegelnd;
		this.glanz = glanz;
	}
	public Material() {
		ambient = new Color(0.5f,0.5f,0.5f,1f);
		diffus  = new Color(0.5f,0.5f,0.5f,1f);
		spiegelnd = new Color(0.5f,0.5f,0.5f,1f);
		glanz = 5;
	}
	
}

