package bezier;
public class Main{
	public static void main(String[] args){
		double t = 0.1;
		double s = 0.9;
		
		Punkt3d[] spalte = new Punkt3d[3];
		Punkt3d[] polygon1 = new Punkt3d[3];
		polygon1[0] = new Punkt3d(0.0, 0.0, 1.0);
		polygon1[1] = new Punkt3d(0.0, 1.0, 0.0);
		polygon1[2] = new Punkt3d(0.0, 2.0, -1.0);
		BezierKurve test1 = new BezierKurve(polygon1);
		spalte[0] = new Punkt3d(test1.auswerten(t).getX(), test1.auswerten(t).getY(), test1.auswerten(t).getZ());
		//System.out.println("t=" + t + ":   x: " + test1.auswerten(t).getX() + "    y: " + 
		//		test1.auswerten(t).getY() + " 	z: " + test1.auswerten(t).getZ());
		
		Punkt3d[] polygon2 = new Punkt3d[3];
		polygon2[0] = new Punkt3d(1.0, 0.0, 0.0);
		polygon2[1] = new Punkt3d(1.0, 1.0, 0.0);
		polygon2[2] = new Punkt3d(1.0, 2.0, 0.0);
		BezierKurve test2 = new BezierKurve(polygon2);
		spalte[1] = new Punkt3d(test2.auswerten(t).getX(), test2.auswerten(t).getY(), test2.auswerten(t).getZ());
		//System.out.println("t=" + t + ":   x: " + test2.auswerten(t).getX() + "    y: " + 
		//		test2.auswerten(t).getY() + " 	z: " + test2.auswerten(t).getZ());
		
		Punkt3d[] polygon3 = new Punkt3d[3];
		polygon3[0] = new Punkt3d(2.0, 0.0, -1.0);
		polygon3[1] = new Punkt3d(2.0, 1.0, 0.0);
		polygon3[2] = new Punkt3d(2.0, 2.0, 1.0);
		BezierKurve test3 = new BezierKurve(polygon3);
		spalte[2] = new Punkt3d(test3.auswerten(t).getX(), test3.auswerten(t).getY(), test3.auswerten(t).getZ());
		//System.out.println("t=" + t + ":   x: " + test3.auswerten(t).getX() + "    y: " + 
		//		test3.auswerten(t).getY() + " 	z: " + test3.auswerten(t).getZ());
		BezierKurve test4 = new BezierKurve(spalte);
		
		
		System.out.println("x: " + test4.auswerten(s).getX() + "  y: " + test4.auswerten(s).getY() + " 	z: " + test4.auswerten(s).getZ());
		
		
		/*
		Punkt3d[][] polygon = new Punkt3d[3][3];
		polygon[0][0] = new Punkt3d(0.0, 0.0, 1.0);polygon[0][1] = new Punkt3d(0.0, 1.0, 0.0); polygon[0][2] = new Punkt3d(0.0, 2.0, -1.0);
		polygon[1][0] = new Punkt3d(1.0, 0.0, 0.0);polygon[1][1] = new Punkt3d(1.0, 1.0, 0.0); polygon[1][2] = new Punkt3d(1.0, 2.0, 0.0);
		polygon[2][0] = new Punkt3d(2.0, 0.0, -1.0);polygon[2][1] = new Punkt3d(2.0, 1.0, 0.0); polygon[2][2] = new Punkt3d(2.0, 2.0, 1.0);
		
		double s = 0.1; double t = 0.9;
		BezierFlaeche test = new BezierFlaeche(polygon);
		System.out.println("x: " + test.auswerten(s, t).getX() + " 	y: " + test.auswerten(s, t).getY() + " 	z: " + test.auswerten(s, t).getZ());
		*/
	}
}