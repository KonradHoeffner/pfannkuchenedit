package bezier;

public class SweepGerade extends BezierFlaeche{
	public SweepGerade(BezierKurve kurve){
		this.kontrollnetz = new Punkt3d[kurve.kontrollpolygon.length][2];
		for (int i = 0; i < kurve.kontrollpolygon.length; i++){
			this.kontrollnetz[i][0] = kurve.kontrollpolygon[i];
			this.kontrollnetz[i][1] = new Punkt3d(kurve.kontrollpolygon[i].getX(), kurve.kontrollpolygon[i].getY()
					, kurve.kontrollpolygon[i].getZ() + 1.0);
		}
	}
}