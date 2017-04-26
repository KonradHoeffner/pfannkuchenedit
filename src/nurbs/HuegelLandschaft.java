package nurbs;
import java.io.Serializable;//zum Speichern und Laden da

public class HuegelLandschaft extends StandardFlaeche implements Serializable{
	public HuegelLandschaft(){
		super(10);
		this.kontrollnetz[0][1].setZ(0.5);
		this.kontrollnetz[2][3].setZ(-0.5);
		this.kontrollnetz[4][6].setZ(0.6);
		this.kontrollnetz[1][5].setZ(-0.4);
		this.kontrollnetz[5][9].setZ(0.3);
	}
}