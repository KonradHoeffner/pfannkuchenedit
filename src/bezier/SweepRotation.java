package bezier;

public class SweepRotation extends BezierFlaeche{
	public SweepRotation(BezierKurve kurve, Kreis kreis){
		this.kontrollnetz = new Punkt3d[kurve.kontrollpolygon.length][kreis.kontrollpolygon.length];
		for (int i = 0; i < kurve.kontrollpolygon.length; i++){
			for (int j = 0; j < kreis.kontrollpolygon.length; j++){
				this.kontrollnetz[i][j] = new Punkt3d(kurve.kontrollpolygon[i].getX()
						, kurve.kontrollpolygon[i].getY() * kreis.kontrollpolygon[j].getY()
						, kurve.kontrollpolygon[i].getY() * kreis.kontrollpolygon[j].getZ());
			}
		}
	}
}