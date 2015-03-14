/** Created on 24.05.2006 */
package szene;

public class Farbe {
	
	byte rot,gruen,blau;
	
	/**
	 * @param rot
	 * @param gruen
	 * @param blau
	 */
	public Farbe(byte rot, byte gruen, byte blau)
	{
		this.rot = rot;
		this.gruen = gruen;
		this.blau = blau;
	}
	
	/**
	 * 
	 */
	public Farbe()
	{
		rot=0;
		gruen=0;
		blau=0;
	}
	
	
}
