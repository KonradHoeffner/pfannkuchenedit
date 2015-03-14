/** Created on 05.06.2006 */
package gui;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import nurbs.DeBoorPunkt3d;
import nurbs.NURBSKurve;
import szene.Kurve3D;
import szene.NURBS3D;
import szene.Szene;
import szene.VektorMethoden;

import com.jogamp.opengl.awt.GLCanvas;

public class OpenGLListener extends MouseAdapter implements MouseMotionListener, MouseWheelListener{
	
	// Aktuelle Mausposition im Fenster
	private Vector2d aktPos=new Vector2d(0,0);
	
	// Vorige Mausposition im Fenster
	private Vector2d lastPos=new Vector2d(0,0);
	
	//	Veränderung der Mauskoordinaten seit dem letzten Abfragen in Pixeln
	Vector2d verschiebungPixel = new Vector2d(0,0);
	//	In Szenenkoordinaten (geht natürlich nur bei den Ebenenfenstern exakt)
	Vector2d verschiebungEinheiten = new Vector2d(0,0);
	//	In Szenenkoordinaten und mit konvertierten Achsen
	Vector3d verschiebungSzene = new Vector3d(0,0,0);
	// Letzte gedrückte Maustaste war die linke (LMB - Left Mouse Button):
	private boolean lmb=true;
	private int typ;
	//private Kamera kamera;
	private Renderer renderer;
	private GLCanvas canvas;
	private OpenGLFrame frame; 	
	
	/**
	 * Dient dem Einrasten des Drehens der Ebenenfenster
	 */
//	private int einrasten = 0;
//	private final int einrasten_count = 30;
//	private final double einrasten_winkel = 5*Math.PI/180;
//	private final double epsilon = 0.001;
//	private boolean eingerastet = false;
	
	public OpenGLListener(OpenGLFrame frame,int typ, Renderer renderer, GLCanvas canvas) {
		this.typ = typ;
		this.renderer = renderer;
		this.canvas = canvas;
		this.frame = frame;
		//kamera = Szene.kamera;
	}
	
	/** Rechnet die Mausverschiebung von Pixeln in Szenenkoordinaten um */
	private void berechneVerschiebung()
	{
		verschiebungPixel.sub(aktPos,lastPos);
		verschiebungEinheiten.set(verschiebungPixel);
		switch(typ)
		{
		case OpenGLFrame.XY_EBENE:		
		case OpenGLFrame.XZ_EBENE:
		case OpenGLFrame.YZ_EBENE: 	  {verschiebungEinheiten.scale(frame.getBreite()/canvas.getWidth());break;}
		case OpenGLFrame.PERSPEKTIVE: {verschiebungEinheiten.scale(15d/canvas.getWidth());break;}
		}
		verschiebungSzene = frame.bildschirmToSzenenAchsen(verschiebungEinheiten);
	}
	
	private void update(MouseEvent e)
	{
		lastPos.set(aktPos);
		aktPos.x = e.getX();
		aktPos.y = e.getY();
		berechneVerschiebung();		
		lmb=(e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK;
	}
	
	
	/**
	 * Berechnet die Winkeländerung um den Ursprung der letzten Mausaktion im Bogenmaß.
	 * Beispiel:
	 * Alter Mauspunkt = (1,1) -> alpha = 45 °
	 * Neuer Mauspunkt = (0,5) -> beta  = 90°
	 * Winkeländerung = beta - alpha = 45°
	 *   
	 */
	private double getWinkelAenderung()
	{
		Vector2d mitte = new Vector2d(canvas.getWidth()/2,canvas.getHeight()/2);
		Vector2d mittigAkt = new Vector2d();
		mittigAkt.sub(aktPos,mitte);
		mittigAkt.normalize();
		Vector2d mittigLast = new Vector2d();
		mittigLast.sub(lastPos,mitte);
		mittigLast.normalize();
		
		return VektorMethoden.VektorWinkelReihenfolge(mittigLast,mittigAkt)?Math.acos(mittigAkt.dot(mittigLast)):-Math.acos(mittigAkt.dot(mittigLast));
	}
	
	private Vector2d getXYEbenePos(Vector2d pos)
	{

		Vector2d xyPos = new Vector2d(pos);
		xyPos.y=canvas.getHeight()-xyPos.y;
		Vector2d mitte = new Vector2d(canvas.getWidth(),canvas.getHeight());
		mitte.scale(0.5);
		xyPos.sub(mitte);
		//System.out.println(xyPos.x+" "+xyPos.y);
		
		xyPos.scale(frame.getBreite()/canvas.getWidth());
		xyPos.add(new Vector2d(Szene.kamera.posXY.x,Szene.kamera.posXY.y));
		
		return xyPos;
	}
	
	public void mouseClicked(MouseEvent e) {
		update(e);
		
		// Linke Maustaste gedrückt: Objekt / Kontrollpunkt auswählen
		// Variable lmb kann hier nicht verwendet werden, da ein Klick erst festgestellt werden kann,
		// wenn die Taste wieder losgelassen wird. getButton() gibt aber zurück welcher Button den Status gewechselt hat.
		if (e.getButton()==MouseEvent.BUTTON1)
		{
			
			if(Szene.bearbeitungsModus==Szene.NURBSKURVE_ERZEUGEN_MODUS)
			{
				if(typ==OpenGLFrame.XY_EBENE)
				{
					if(e.getClickCount()==1)
					{
						Vector2d xyEbenePos = getXYEbenePos(aktPos);
						DeBoorPunkt3d p = new DeBoorPunkt3d(xyEbenePos.x,xyEbenePos.y,0,1d);
						//p.x=aktPos.x
						Szene.neueNURBSKurve.add(p);
					}
					else
					{
						int anzahlKontrollpunkte = Szene.neueNURBSKurve.size();
						if(anzahlKontrollpunkte>2)
						{
							boolean ordnung2=true,ordnung3=true,ordnung4=true;
							if(anzahlKontrollpunkte==3){ordnung3=false;ordnung4=false;}  
							if(anzahlKontrollpunkte==4){ordnung4=false;}
							int ordnung = Szene.mainWindow.getOrdnung("\n ","NURBS - Kurve erzeugen",Szene.mainWindow.iconButtonNURBSKurve,ordnung2,ordnung3,ordnung4);
							DeBoorPunkt3d[] kontrollpunkte = Szene.neueNURBSKurve.toArray(new DeBoorPunkt3d[0]);
							//System.out.println(kontrollpunkte.length);
							NURBSKurve nurbsKurve = new NURBSKurve(kontrollpunkte,ordnung,true);
							Kurve3D kurve3d =  new Kurve3D(nurbsKurve);
							Szene.nurbsKurven.add(kurve3d);
							Szene.setAusgewaehltesObjekt(kurve3d);
							Szene.neueNURBSKurve.removeAllElements();
						}
					}
				}
			}
			else frame.renderer.pick(aktPos);
			
		}else
			// Rechte Maustaste gedrückt (oder andere nicht - linke): Objekt  / Kontrollpunkt abwählen
		{
			//System.out.println("Rechte Maustaste geklickt");
			Szene.abwaehlen();
			/*if(Szene.ausgewaehlterKontrollpunkt==null) Szene.ausgewaehlterKontrollpunkt = null;
			 else Szene.ausgewaehltesObjekt = null;*/
		}
		
	}
	
	private void perspektive_Kamera_Verschieben()
	{
		Vector3d rechts = Szene.kamera.getRechts();
		rechts.scale(verschiebungSzene.x);
		
		// Linke Maustaste gedrückt: links - rechts verschiebt szene nach links - rechts, oben - unten verschiebt szene nach oben - unten 
		if (lmb)
		{
			Vector3d hoch = new Vector3d();
			hoch.set(Szene.kamera.oben);
			hoch.scale(verschiebungSzene.y);
			Szene.kamera.position.sub(hoch);
			Szene.kamera.ziel.sub(hoch);
			
		}	else
			// Rechte (oder andere nicht - linke) Maustaste gedrückt: 
			// links - rechts verschiebt szene nach links - rechts, oben - unten verschiebt szene nach hinten - vorne			
		{
			Vector3d vorne = Szene.kamera.getSichtVektor();
			vorne.scale(verschiebungSzene.y);
			Szene.kamera.position.sub(vorne);
			Szene.kamera.ziel.sub(vorne);
			
		}
		Szene.kamera.position.sub(rechts);
		Szene.kamera.ziel.sub(rechts);
		
	}
	
	private void perspektive_Kamera_Drehen_LMB() {
		Vector3d neuesZiel = VektorMethoden.rotierePunkt(Szene.kamera.ziel,Szene.kamera.position,Szene.kamera.oben,-verschiebungEinheiten.x/10);
		neuesZiel = VektorMethoden.rotierePunkt(neuesZiel,Szene.kamera.position,Szene.kamera.getRechts(),-verschiebungEinheiten.y/10);
		Vector3d neuesOben = VektorMethoden.rotierePunkt(Szene.kamera.oben,new Vector3d(0,0,0),Szene.kamera.getRechts(),-verschiebungEinheiten.y/10);
		Szene.kamera.ziel=neuesZiel;
		Szene.kamera.oben=neuesOben;
	}
	
	private void perspektive_Kamera_Drehen_RMB() {
		//kamera.oben = VektorMethoden.rotierePunkt(kamera.oben,new Vector3d(0,0,0),kamera.getSichtVektor(),(verschiebung.y-verschiebung.x)/60);
		Szene.kamera.oben = VektorMethoden.rotierePunkt(Szene.kamera.oben,new Vector3d(0,0,0),Szene.kamera.getSichtVektor(),getWinkelAenderung());
	}
	
	/**
	 * Bewegt Kamera nach vorne / hinten
	 */
	private void perspektive_Kamera_Skalieren_LMB() {
		Vector3d vorne = Szene.kamera.getSichtVektor();
		vorne.scale(frame.bildschirmToSkalierung(verschiebungEinheiten).dot(new Vector3d(1,1,1)));
		Szene.kamera.position.sub(vorne);
		Szene.kamera.ziel.sub(vorne);
	}
	
	/**
	 * Ändert Brennweite der Kamera
	 */
	private void perspektive_Kamera_Skalieren_RMB() {
		Szene.kamera.fovy-=frame.bildschirmToSkalierung(verschiebungEinheiten).dot(new Vector3d(1,1,1));
		Szene.kamera.fovy=Math.min(Szene.kamera.fovy,160);
		Szene.kamera.fovy=Math.max(Szene.kamera.fovy,1);
		//System.out.println(kamera.fovy);
	}
	
	private void perspektive_mouseDragged()
	{
		switch(Szene.bearbeitungsModus)
		{
		case Szene.KAMERA_MODUS:
		{	
			switch(Szene.bearbeitungsFunktion)
			{
			case Szene.VERSCHIEBEN_FUNKTION: {perspektive_Kamera_Verschieben();break;}
			case Szene.SKALIEREN_FUNKTION:
			{
				if(lmb)	perspektive_Kamera_Skalieren_LMB();
				else 	perspektive_Kamera_Skalieren_RMB();
				break;
			}
			case Szene.DREHEN_FUNKTION:
			{
				if(lmb)	perspektive_Kamera_Drehen_LMB();
				else	perspektive_Kamera_Drehen_RMB();
				break;
			}
			}			
			break;
		}
		case Szene.OBJEKT_MODUS:
		{
			if(Szene.ausgewaehlterTyp==Szene.NURBSFLAECHE_AUSGEWAEHLT||Szene.ausgewaehlterTyp==Szene.LICHTQUELLE_AUSGEWAEHLT)
				switch(Szene.bearbeitungsFunktion)
				{
				case Szene.VERSCHIEBEN_FUNKTION:	{perspektive_ObjektoderNURBS_Verschieben();break;}
				case Szene.SKALIEREN_FUNKTION:		{perspektive_Objekt_Skalieren();break;}
				case Szene.DREHEN_FUNKTION:			{perspektive_Objekt_Drehen();break;}
				}
			break;	
		}
		case Szene.NURBS_MODUS:
		{
			switch(Szene.bearbeitungsFunktion)
			{
			case Szene.VERSCHIEBEN_FUNKTION:{perspektive_ObjektoderNURBS_Verschieben();break;}
			}
		}
		break;
		}
	}
	
	
	
	private void perspektive_ObjektoderNURBS_Verschieben() {
		
		Vector3d rechts = Szene.kamera.getRechts();
		rechts.scale(verschiebungEinheiten.x);
		Vector3d hoch = new Vector3d(0,0,0);
		Vector3d vorne = new Vector3d(0,0,0);
		// Linke Maustaste gedrückt: links - rechts verschiebt objekt nach links - rechts, oben - unten verschiebt objekt nach oben - unten 
		if (lmb)
		{
			hoch.set(Szene.kamera.oben);
			hoch.scale(-verschiebungEinheiten.y);
			
		}	else
			// Rechte (oder andere nicht - linke) Maustaste gedrückt: 
			// links - rechts verschiebt szene nach links - rechts, oben - unten verschiebt objekt nach hinten - vorne			
		{
			vorne = Szene.kamera.getSichtVektor();
			vorne.scale(-verschiebungEinheiten.y);
		}
		Vector3d translation = new Vector3d();
		translation.sub(hoch,vorne);
		translation.add(rechts);
		
		switch(Szene.ausgewaehlterTyp)
		{
		case Szene.NURBSFLAECHE_AUSGEWAEHLT:
		{
			Szene.ausgewaehlteNURBSFlaeche.position.add(translation);
			break;
		}
		case Szene.LICHTQUELLE_AUSGEWAEHLT:
		{
			Szene.ausgewaehlteLichtquelle.position.add(translation);
			break;
		}
		case Szene.KONTROLLPUNKT_AUSGEWAEHLT:
		{
			// So ein Wust kommt dabei raus wenn nix public ist oder von vector3d erbt:
			// sonst in einer zeile: Szene.ausgewaehlterKontrollpunkt.add(translation);
			translation = translationToKontrollPunktTranslation(Szene.ausgewaehlteNURBSFlaeche,translation);
			
			Szene.ausgewaehlterKontrollpunkt.setX(Szene.ausgewaehlterKontrollpunkt.getX()+translation.x);
			Szene.ausgewaehlterKontrollpunkt.setY(Szene.ausgewaehlterKontrollpunkt.getY()+translation.y);
			Szene.ausgewaehlterKontrollpunkt.setZ(Szene.ausgewaehlterKontrollpunkt.getZ()+translation.z);
//			System.out.println("Kontrollpunkt wird verschoben auf "+
//			Szene.ausgewaehlterKontrollpunkt.getX()+" "+
//			Szene.ausgewaehlterKontrollpunkt.getY()+" "+ 
//			Szene.ausgewaehlterKontrollpunkt.getZ());
			Szene.ausgewaehlteNURBSFlaeche.update();
			break;
		}
		
		}
	}
	
	/**
	 * Der Translationsvektor eines Kontrollpunktes braucht diese Transformation,
	 * da das NURBSObjekt ja noch Skalierungswerte und eine Rotationsmatrix hat. 
	 */
	private Vector3d translationToKontrollPunktTranslation(NURBS3D nurbs,Vector3d translation) {
		Matrix4d skalierungsVektorMatrix = VektorMethoden.VectorToMatrix(nurbs.skalierung);
		Matrix4d translationsVektorMatrix = VektorMethoden.VectorToMatrix(translation);
		Matrix4d inverseRotationsMatrix = new Matrix4d(nurbs.rotationsMatrix);
		inverseRotationsMatrix.invert();
		Matrix4d neuerSkalierungsVektorMatrix = new Matrix4d(inverseRotationsMatrix);
		Matrix4d neuerTranslationsVektorMatrix = new Matrix4d(inverseRotationsMatrix);
		neuerSkalierungsVektorMatrix.mul(skalierungsVektorMatrix);
		neuerSkalierungsVektorMatrix.mul(nurbs.rotationsMatrix);
		neuerTranslationsVektorMatrix.mul(translationsVektorMatrix);
		neuerTranslationsVektorMatrix.mul(nurbs.rotationsMatrix);
		
		//Vector3d skalierung = VektorMethoden.MatrixToVector(neuerSkalierungsVektorMatrix);
		
		translation = VektorMethoden.MatrixToVector(neuerTranslationsVektorMatrix);
		
		
//		translation.x/=skalierung.x;
//		translation.y/=skalierung.y;
//		translation.z/=skalierung.y;
		
		return translation;
		//System.out.println("Skalierung: "+nurbs.skalierung);
		//translation.scale(-1);
		
	}
	
	private void perspektive_Objekt_Skalieren() {
		// Skalierung nicht unter Mindestwert gehen lassen
		switch(Szene.ausgewaehlterTyp)
		{
		case Szene.NURBSFLAECHE_AUSGEWAEHLT:
		{
			final double MIN_SKALIERUNG = 0.01;
			NURBS3D nurbs = Szene.ausgewaehlteNURBSFlaeche;
			double skalierung = (verschiebungEinheiten.x-verschiebungEinheiten.y)/20;
			nurbs.skalierung.scale(1+skalierung);
			nurbs.skalierung.x=Math.max(nurbs.skalierung.x,MIN_SKALIERUNG);
			nurbs.skalierung.y=Math.max(nurbs.skalierung.y,MIN_SKALIERUNG);
			nurbs.skalierung.z=Math.max(nurbs.skalierung.z,MIN_SKALIERUNG);
			//System.out.println(skalierung);
			//System.out.println(nurbs.skalierung);
			break;
		}
		}
	}
	private void perspektive_Objekt_Drehen() {
		switch(Szene.ausgewaehlterTyp)
		{
		case Szene.NURBSFLAECHE_AUSGEWAEHLT:
		{
			Vector3d vorne = Szene.kamera.getSichtVektor();
			
			Matrix4d rotationsMatrix =
				VektorMethoden.rotierePunktMatrix(new Vector3d(0,0,0),vorne,-getWinkelAenderung());
			//System.out.println(rotationsMatrix);
			rotationsMatrix.mul(Szene.ausgewaehlteNURBSFlaeche.rotationsMatrix);
			Szene.ausgewaehlteNURBSFlaeche.rotationsMatrix=rotationsMatrix;
			
			//vorne.scale(getWinkelAenderung());
			
			//Szene.ausgewaehlteNURBSFlaeche.winkel.add(vorne);
			//VektorMethoden.rotierePunkt(Szene.ausgewaehlteNURBSFlaeche.winkel,new Vector3d(0,0,0),vorne,0.1);
			//System.out.println(Szene.ausgewaehlteNURBSFlaeche.winkel);
			//getWinkelAenderung()
			break;
		}
		}
	}
	
	
	private void ebenenfenster_Kamera_Verschieben_LMB()
	{
		
		Point3d pos = frame.getPos();
		pos.sub(verschiebungSzene);
		//System.out.println(verschiebungSzene);
		/*Vector2d modifikator = frame.getVerschiebungsModifikator();
		 pos.x-=modifikator.x*(verschiebungSzene.x);	
		 pos.y-=modifikator.y*(verschiebungSzene.y);*/
	}
	
	private void ebenenfenster_Kamera_Verschieben_RMB() {
		
//		Point2d pos = frame.getPos();
//		Vector2d modifikator = frame.getVerschiebungsModifikator();
//		pos.x-=modifikator.x*(verschiebungSzene.x);	
//		frame.setBreite(frame.getBreite()-(modifikator.y*(verschiebungSzene.y)));
	}
	
	private void ebenenfenster_Kamera_Skalieren() {
		frame.setBreite(frame.getBreite()-(frame.bildschirmToSkalierung(verschiebungEinheiten).dot(new Vector3d(1,1,1))));
	}
	
	/*private void einrastBehandlung()
	 {
	 if(Math.abs(frame.getWinkel())<epsilon) einrasten=Math.max(einrasten-1,0);
	 else
	 if(Math.abs(frame.getWinkel())<einrasten_winkel) einrasten++;
	 else einrasten=0;
	 
	 if(einrasten>=einrasten_count) {eingerastet=true;frame.setWinkel(0);}
	 if(einrasten==0) eingerastet = false;
	 /*System.out.println(frame.getWinkel());
	  System.out.println("Einrasten: "+einrasten);
	  
	  }*/
	
//	private void ebenenfenster_Kamera_Drehen() {
//	//einrastBehandlung();
//	//if(!eingerastet)
//	frame.setWinkel(frame.getWinkel()-getWinkelAenderung());	
//	}
	
	private void ebenenfenster_ObjektoderNURBS_Verschieben() {
		
		/*Vector2d translation2d = new Vector2d(verschiebungSzene);
		 Vector2d modifikator = frame.getVerschiebungsModifikator();
		 translation2d.x*=modifikator.x;
		 translation2d.y*=modifikator.y;
		 
		 Vector3d translation=null;*/
		/*switch(typ)
		 {
		 case OpenGLFrame.XY_EBENE: {translation = new Vector3d(translation2d.x,translation2d.y,0);break;}
		 case OpenGLFrame.XZ_EBENE: {translation = new Vector3d(translation2d.x,0,translation2d.y);break;}
		 case OpenGLFrame.YZ_EBENE: {translation = new Vector3d(0,translation2d.y,translation2d.x);break;}
		 }*/
		
		switch(Szene.ausgewaehlterTyp)
		{
		case Szene.NURBSFLAECHE_AUSGEWAEHLT:
		{	
			Szene.ausgewaehlteNURBSFlaeche.position.add(verschiebungSzene);
			break;
		}
		case Szene.LICHTQUELLE_AUSGEWAEHLT:
		{
			Szene.ausgewaehlteLichtquelle.position.add(verschiebungSzene);
			break;
		}
		case Szene.KONTROLLPUNKT_AUSGEWAEHLT:
		{
			// So ein Wust kommt dabei raus wenn nix public ist oder von vector3d erbt:
			// sonst in einer zeile: Szene.ausgewaehlterKontrollpunkt.add(translation);		
			Vector3d translation = new Vector3d(verschiebungSzene);
			if(Szene.ausgewaehlteNURBSFlaeche!=null)
			translation = translationToKontrollPunktTranslation(Szene.ausgewaehlteNURBSFlaeche,translation);

			Szene.ausgewaehlterKontrollpunkt
			.setX(Szene.ausgewaehlterKontrollpunkt.getX()
					+ translation.x);
			Szene.ausgewaehlterKontrollpunkt
			.setY(Szene.ausgewaehlterKontrollpunkt.getY()
					+ translation.y);
			Szene.ausgewaehlterKontrollpunkt
			.setZ(Szene.ausgewaehlterKontrollpunkt.getZ()
					+ translation.z);
			if(Szene.ausgewaehlteNURBSFlaeche!=null) Szene.ausgewaehlteNURBSFlaeche.update();
			else Szene.ausgewaehlteNURBSKurve.update();
			break;
		}
		
		}
	}
	
	
	
	private void ebenenfenster_Objekt_Skalieren_LMB() {
		perspektive_Objekt_Skalieren();
	}
	
	private void ebenenfenster_Objekt_Skalieren_RMB() {
		// Skalierung nicht unter Mindestwert gehen lassen
		switch(Szene.ausgewaehlterTyp)
		{
		case Szene.NURBSFLAECHE_AUSGEWAEHLT:
		{
			NURBS3D nurbs = Szene.ausgewaehlteNURBSFlaeche;
			//double skalierung = (verschiebungSzene.x+verschiebungSzene.y)/50;
			switch(typ)
			{
			case OpenGLFrame.XY_EBENE:
			{
				nurbs.skalierung.x*=(1+verschiebungSzene.x/20);
				nurbs.skalierung.y/=(1+verschiebungSzene.y/20);
				break;
			}
			//case OpenGLFrame.XZ_EBENE:	{nurbs.skalierung.z*=(1+skalierung);break;}
			//case OpenGLFrame.YZ_EBENE:	{nurbs.skalierung.y*=(1+skalierung);break;}
			}
			final double MIN_SKALIERUNG = 0.01;
			nurbs.skalierung.x=Math.max(nurbs.skalierung.x,MIN_SKALIERUNG);
			nurbs.skalierung.y=Math.max(nurbs.skalierung.y,MIN_SKALIERUNG);
			nurbs.skalierung.z=Math.max(nurbs.skalierung.z,MIN_SKALIERUNG);
			break;
		}
		}
		
	}
	
	private void ebenenfenster_Objekt_Drehen() {
		
		Matrix4d rotation = new Matrix4d();
		
		switch(typ)
		{
		case OpenGLFrame.XY_EBENE:
		{rotation.rotZ(-getWinkelAenderung());break;}
		case OpenGLFrame.XZ_EBENE:
		{rotation.rotY(-getWinkelAenderung());break;}
		case OpenGLFrame.YZ_EBENE:
		{rotation.rotX(-getWinkelAenderung());break;}
		}
		
		rotation.mul(Szene.ausgewaehlteNURBSFlaeche.rotationsMatrix);
		Szene.ausgewaehlteNURBSFlaeche.rotationsMatrix=rotation;
//		Szene.ausgewaehlteNURBSFlaeche.rotationsMatrix.mul(rotation);
		
	}
	
	private void ebenenfenster_mouseDragged()
	{
		switch(Szene.bearbeitungsModus)
		{
		case Szene.KAMERA_MODUS:
		{
			//System.out.println("Kameramodus erwartet"+Szene.bearbeitungsModus);
			switch(Szene.bearbeitungsFunktion)
			{
			case Szene.VERSCHIEBEN_FUNKTION:
			{
				if(lmb) ebenenfenster_Kamera_Verschieben_LMB();
				else 	ebenenfenster_Kamera_Verschieben_RMB();
				break;
			}
			case Szene.SKALIEREN_FUNKTION:	{ebenenfenster_Kamera_Skalieren();break;}
			//case Szene.DREHEN_FUNKTION:	{ebenenfenster_Kamera_Drehen();break;}
			}
			break;
		}
		case Szene.OBJEKT_MODUS:
		{
			if(Szene.ausgewaehlterTyp==Szene.NURBSFLAECHE_AUSGEWAEHLT)
				switch(Szene.bearbeitungsFunktion)
				{
				case Szene.VERSCHIEBEN_FUNKTION:	{ebenenfenster_ObjektoderNURBS_Verschieben();break;}
				case Szene.SKALIEREN_FUNKTION:
				{
					if(lmb)ebenenfenster_Objekt_Skalieren_LMB();
					else ebenenfenster_Objekt_Skalieren_RMB();
					break;
				}
				case Szene.DREHEN_FUNKTION:			{ebenenfenster_Objekt_Drehen();break;}
				}
			if(Szene.ausgewaehlterTyp==Szene.LICHTQUELLE_AUSGEWAEHLT)
				switch(Szene.bearbeitungsFunktion)
				{
				case Szene.VERSCHIEBEN_FUNKTION:	{ebenenfenster_ObjektoderNURBS_Verschieben();break;}
				}
			break;
		}
		case Szene.NURBS_MODUS:
		{
			if(Szene.ausgewaehlterTyp==Szene.KONTROLLPUNKT_AUSGEWAEHLT)
				switch(Szene.bearbeitungsFunktion)
				{
				case Szene.VERSCHIEBEN_FUNKTION:
				{
					ebenenfenster_ObjektoderNURBS_Verschieben();
					break;
				}
				}
		}
		break;
		
		}
	}
	
	
	public void mouseDragged(MouseEvent e)
	{
		update(e);
		switch(typ)
		{
		// Die 3 orthogonalen Ebenenfenster
		case OpenGLFrame.XY_EBENE:
		case OpenGLFrame.XZ_EBENE:
		case OpenGLFrame.YZ_EBENE:
		{
			ebenenfenster_mouseDragged();
			break;
		}
		
		// Das Perspektivenfenster
		case OpenGLFrame.PERSPEKTIVE:
		{
			perspektive_mouseDragged();
			break;
		}
		}
		
		
		//System.out.println("it drags me by "+verschiebung.x+" "+verschiebung.y);
		renderer.setViewUpdated();
		
		
	}
	
	public void mouseMoved(MouseEvent e) {
		update(e);
	}
	
	
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(Szene.ausgewaehlterTyp==Szene.KONTROLLPUNKT_AUSGEWAEHLT&&Szene.bearbeitungsModus==Szene.NURBS_MODUS)
		{
			Szene.ausgewaehlterKontrollpunkt.gewicht*=(1-(double)(e.getScrollAmount())*e.getWheelRotation()/15);
			Szene.ausgewaehlterKontrollpunkt.gewicht=Math.max(Szene.ausgewaehlterKontrollpunkt.gewicht,0.01);
			if(Szene.ausgewaehlteNURBSFlaeche!=null) Szene.ausgewaehlteNURBSFlaeche.update();
			else Szene.ausgewaehlteNURBSKurve.update();
		} else
			switch(typ)
			{
			// Ebenenfenster : Sichtaussschnitt ändern
			case 0:{Szene.kamera.breiteXY*=(1+(double)(e.getScrollAmount())*e.getWheelRotation()/15);break;}
			case 1:{Szene.kamera.breiteXZ*=(1+(double)(e.getScrollAmount())*e.getWheelRotation()/15);break;}
			case 2:{Szene.kamera.breiteYZ*=(1+(double)(e.getScrollAmount())*e.getWheelRotation()/15);break;}
//			Perspektivfenster: Nach vorne/hinten bewegen
			case 3:{Szene.kamera.kameraVor((double)(e.getScrollAmount())*e.getWheelRotation()/2);break;}
			}
		
		renderer.setViewUpdated();
	}
	
	
}