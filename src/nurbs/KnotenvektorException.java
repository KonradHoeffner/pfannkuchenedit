package nurbs;

public class KnotenvektorException extends Exception{
	public KnotenvektorException(boolean fehler){
		if (fehler){
			System.err.println("Der Knotenvektor erfüllt nicht die Eigenschaft der Monotonie!!");
		} else {
			System.err.println("Der Knotenvektor hat nicht die geforderte Länge!!");
		}
	}
}