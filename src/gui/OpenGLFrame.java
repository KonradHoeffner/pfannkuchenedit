package gui;

import java.awt.BorderLayout;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import javax.swing.JInternalFrame;
import javax.vecmath.*;

import szene.Kamera;
import szene.Szene;

import com.jogamp.opengl.awt.GLCanvas;

public class OpenGLFrame extends JInternalFrame{
	
	public GLCanvas canvas;
	
	public static final int XY_EBENE = 0;
	public static final int XZ_EBENE = 1;
	public static final int YZ_EBENE = 2;    
	public static final int PERSPEKTIVE = 3;
	static final String[] titel = {"XY - Ebene","XZ - Ebene","YZ - Ebene","Perspektive"};
	private Kamera kamera;
	private int typ;
	Renderer renderer;
	
	public double getBreite()
	{
		switch(typ)
		{
		case XZ_EBENE: return kamera.breiteXZ;
		case YZ_EBENE: return kamera.breiteYZ;
		default: return kamera.breiteXY;
		}
	}

	public void setBreite(double breite)
	{
		switch(typ)
		{
		case XY_EBENE: {kamera.breiteXY=breite;break;}
		case XZ_EBENE: {kamera.breiteXZ=breite;break;}
		case YZ_EBENE: {kamera.breiteYZ=breite;break;}
		}
	}

	public double getWinkel()
	{
		switch(typ)
		{
		case XY_EBENE: return kamera.winkelXY;
		case XZ_EBENE: return kamera.winkelXZ;
		case YZ_EBENE: return kamera.winkelYZ;
		default: return 0;
		}
	}

	public void setWinkel(double winkel)
	{
		switch(typ)
		{
		case XY_EBENE: {kamera.winkelXY=winkel;break;}
		case XZ_EBENE: {kamera.winkelXZ=winkel;break;}
		case YZ_EBENE: {kamera.winkelYZ=winkel;break;}
		}
	}

	
	public Point3d getPos()
	{
		switch(typ)
		{
		case XY_EBENE: return kamera.posXY; 
		case XZ_EBENE: return kamera.posXZ;
		case YZ_EBENE: return kamera.posYZ;
		default: return null;
		}
	}

	/**
	 * Ist für das Verschieben mit der Maus notwendig, da Bildschirm - und Szenenkoordinatensystem unterschiedlich sind.
	 *  Gibt an, mit welchem Wert die Verschiebung vor ihrer Ausführung multipliziert wird.
	 *  Die Koordinaten bleiben allerdings an ihrer Stelle. Zur Achsenumrechnung gibt es getVerschiebungsMatrix(). 
	 */
	public Vector2d getVerschiebungsModifikator() {
		switch(typ)
		{
		case XY_EBENE: return new Vector2d(1,-1);
		case XZ_EBENE: return new Vector2d(1,-1);
		case YZ_EBENE: return new Vector2d(1,1);
		default: return null;
		}
	}	
	
////	/** NOCH NICHT GETESTET 
////	 * Gibt eine Matrix zurück, mit der die Bildschirmachsen der orthogonalen Ebenenfenster 
////	 * in Szenenachsen umgewandelt werden können. Geht allerdings auch direkt mit bildschirmToSzenenAchsen(Vector3d).
////	 * Dabei muss die Matrix von LINKS auf den Punkt multipliziert werden. Neuer Punkt = Matrix * Punkt.
////	 * Beispiel: Verschiebung mit der Maus in der YZ - Ebene. Mausvektor (deltaX,deltaY,0).
////	 * Multiplikation mit der Verschiebungsmatrix ergibt als neuen Vektor (0,deltaY,-deltaX).
////	 * Eine Translation eines Objektes in Richtung der positiven x und y Achse auf dem Bildschirm würde also im YZ - Fenster
////	 * eine Translation des Objektes in Richtung der positiven y und der negativen Z - Achse bewirken.
////	 * Hier findet allerdings noch keine Umrechnung von Pixeln in Szeneneinheiten statt (Länge des Vektors bleibt gleich).
////	 * Im Perspektivfenster und in der XY Ebene wird die Einheitsmatrix zurückgegeben.
////	 * Im Perspektivfenster müssen Achsen separat mit kamera.getSichtvektor und kamera.oben erfolgen.
////	 * @return  Die Umwandlungsmatrix. Von LINKS auf den Punkt aufmultiplizieren (MATRIX * PUNKT).
////	 */
////
////	public Matrix3d getVerschiebungsMatrix()
////	{
////		Matrix3d verschiebungsMatrix = new Matrix3d();
////		switch(typ)
////		{
////		case XY_EBENE: verschiebungsMatrix.setIdentity();
////		case XZ_EBENE: verschiebungsMatrix.setIdentity();
////		case YZ_EBENE: verschiebungsMatrix.setIdentity();
////		case PERSPEKTIVE: verschiebungsMatrix.setIdentity(); 
////		}
////		return verschiebungsMatrix;
////	}
	
	/** Wandelt Bildschirmachsen der orthogonalen Ebenenfenster in Szenenachsen um.
	 * Beispiel: Verschiebung mit der Maus in der YZ - Ebene. Mausvektor (deltaX,deltaY,0).
	 * bildschirmToSzenenAchsen(Mausvektor) ergibt als neuen Vektor (0,y,-x).
	 * Eine Translation eines Objektes in Richtung der positiven x und y Achse auf dem Bildschirm würde also im YZ - Fenster
	 * eine Translation des Objektes in Richtung der positiven y und der negativen Z - Achse bewirken.
	 * Hier findet allerdings noch keine Umrechnung von Pixeln in Szeneneinheiten statt (Länge des Vektors bleibt gleich).
	 * Diese Funktion Lässt sich nicht im Perspektivfenster anwenden.
	 * Im Perspektivfenster muss dies mit kamera.getSichtvektor und kamera.oben erfolgen.
	 * @return Die im Fenster veränderten Achsen inkl. Richtung.
	 */
	public Vector3d bildschirmToSzenenAchsen(Vector2d bildschirmAchsen)
	{
		switch(typ)
		{
		case PERSPEKTIVE:
		case XY_EBENE: return new Vector3d(bildschirmAchsen.x,-bildschirmAchsen.y,0);
		case XZ_EBENE: return new Vector3d(bildschirmAchsen.x,0,bildschirmAchsen.y);
		case YZ_EBENE: return new Vector3d(0,-bildschirmAchsen.y,-bildschirmAchsen.x);
		}
		return null;
	}
	
	/**
	 * Wandelt Mausbewegung um in Skalierung der jeweiligen Fenster.  
	 * Mausbewegung nach rechts/oben -> positive Skalierung
	 * Mausbewegung nach links/unten -> negative Skalierung
	 */
	public Vector3d bildschirmToSkalierung(Vector2d bildschirmAchsen)
	{
		switch(typ)
		{
		case PERSPEKTIVE:
		case XY_EBENE: return new Vector3d(bildschirmAchsen.x,-bildschirmAchsen.y,0);
		case XZ_EBENE: return new Vector3d(bildschirmAchsen.x,0,-bildschirmAchsen.y);
		case YZ_EBENE: return new Vector3d(0,-bildschirmAchsen.y,bildschirmAchsen.x);
		}
		return null;
	}

	public OpenGLFrame(int typ) {
		super(titel[typ], 
				true, //resizable
				false, //closable
				true, //maximizable
				true);//iconifiable
		this.typ = typ;
		this.kamera = Szene.kamera;
		// Hinzufügen: Animator muss irgendwann stoppen! (mit windowlistener der closeevent bekommt oder so )
		// Hinzufügen: Animator niedigere Rate
		
		//canvas=GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
		canvas= new GLCanvas();
		renderer = new Renderer(typ);
		canvas.addGLEventListener(renderer);
		
		this.getContentPane().add(canvas,BorderLayout.CENTER);
		OpenGLListener listener = new OpenGLListener(this,typ,renderer,canvas);
		this.addMouseWheelListener(listener);
		canvas.addMouseMotionListener(listener);
		canvas.addMouseListener(listener);
		
		final MyAnimator animator = new MyAnimator(this);
		//final Animator animator = new Animator(canvas);
		
		addContainerListener(new ContainerAdapter() {
			public void ContainerClosing(ContainerEvent e) {
				animator.stop();
				System.exit(0);
			}
		});
		animator.setPriority(Thread.MAX_PRIORITY);
		animator.start();
		

		//canvas.display();
		//canvas.requestFocus();
		// Vor setVisible muss unbedingt setSize gesetzt werden sonst kommt ein OpenGL - Fehler . Resizen kann man hinterher immernoch
		setSize(300,300);
		setVisible(true);	
	}

}
