/** Created on 10.06.2006 */
package szene;
import java.io.Serializable;

import javax.vecmath.Tuple3d;

public class Linie3D implements Serializable{
	
	public Tuple3d punkt1;
	public Tuple3d punkt2;
	
	public Linie3D(Tuple3d p1,Tuple3d p2) {
		punkt1=p1;
		punkt2=p2;
	}
	
}
