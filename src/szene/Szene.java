package szene;

import gui.MainWindow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import nurbs.DeBoorPunkt3d;

/** Datenhaltungsklasse für alle Elemente der Szene (Materialien, Kamera, Lichtquellen, Objekte, NURBS...)
 * Ziel ist, dass alle Klassen auf das zugreifen können was sie gerade brauchen von der Szene.  
 * @author konrad */
public class Szene implements Serializable{

	public static MainWindow mainWindow;

	public static final int KAMERA_MODUS = 0;
	public static final int OBJEKT_MODUS = 1;
	public static final int NURBS_MODUS = 2;
	public static final int NURBSKURVE_ERZEUGEN_MODUS = 3;
	
	public static final int AUSWAHL_FUNKTION = 0;
	public static final int VERSCHIEBEN_FUNKTION = 1;
	public static final int SKALIEREN_FUNKTION = 2;
	public static final int DREHEN_FUNKTION = 3;

	public static int bearbeitungsModus=KAMERA_MODUS;
	public static int bearbeitungsFunktion=VERSCHIEBEN_FUNKTION;

	public static Kamera kamera;
	// 	Damit die ComboBox auch ihre Materialien findet brauchen wir eine Map
	public static HashMap<String,Material> materialien = new HashMap<String,Material>();
	
	public static Vector <NURBS3D> nurbsFlaechen = new Vector<NURBS3D>();
	public static Vector <Lichtquelle> lichtquellen = new Vector<Lichtquelle>();
	public static Vector <Kurve3D> nurbsKurven = new Vector<Kurve3D>();
	
	public static Vector<DeBoorPunkt3d> neueNURBSKurve = new Vector<DeBoorPunkt3d>();
	
	public static NURBS3D ausgewaehlteNURBSFlaeche=null;
	public static DeBoorPunkt3d ausgewaehlterKontrollpunkt=null;
	public static Lichtquelle ausgewaehlteLichtquelle=null;
	public static Kurve3D ausgewaehlteNURBSKurve = null; 
	
	public static final int NICHTS_AUSGEWAEHLT = 0;
	public static final int NURBSFLAECHE_AUSGEWAEHLT = 1;
	public static final int NURBSKURVE_AUSGEWAEHLT = 2;
	public static final int KONTROLLPUNKT_AUSGEWAEHLT = 3;
	public static final int LICHTQUELLE_AUSGEWAEHLT = 4;
	
	public static int ausgewaehlterTyp=NICHTS_AUSGEWAEHLT;
	
	public static final int QUALITY_ULTRA_LOW = 5;
	public static final int QUALITY_LOW = 25;
	public static final int QUALITY_MEDIUM = 50;
	public static final int QUALITY_HIGH = 100;
	public static int quality = QUALITY_MEDIUM;
	
	public static void abwaehlen()
	{
		ausgewaehlterTyp = NICHTS_AUSGEWAEHLT;
		ausgewaehlteNURBSFlaeche = null;
		ausgewaehlteLichtquelle = null;
		ausgewaehlterKontrollpunkt = null;
		ausgewaehlteNURBSKurve = null;
	}
	
	
	//public static NURBSFlaeche testFlaeche;
	//public static NURBSKurve testKurve;
	//public static Point3d[][] testPunkte;
	
	public static void setAusgewaehltesObjekt(NURBS3D nurbs)
	{
		neueNURBSKurve.removeAllElements();
		abwaehlen();
		ausgewaehlteNURBSFlaeche = nurbs;
		ausgewaehlterTyp = NURBSFLAECHE_AUSGEWAEHLT;
	}
	
	public static void setAusgewaehltesObjekt(Kurve3D kurve) {
		neueNURBSKurve.removeAllElements();
		abwaehlen();
		ausgewaehlteNURBSKurve = kurve;
		ausgewaehlterTyp = NURBSKURVE_AUSGEWAEHLT;
	}


	public static void setAusgewaehltesObjekt(Lichtquelle lichtquelle)
	{
		neueNURBSKurve.removeAllElements();
		abwaehlen();
		ausgewaehlteLichtquelle = lichtquelle;
		ausgewaehlterTyp = LICHTQUELLE_AUSGEWAEHLT;
	}

	public static void setAusgewaehltesObjekt(DeBoorPunkt3d kontrollpunkt)
	{
		neueNURBSKurve.removeAllElements();
		//abwaehlen();
		ausgewaehlterKontrollpunkt = kontrollpunkt;
		ausgewaehlterTyp = KONTROLLPUNKT_AUSGEWAEHLT;
	}

	public static void materialLoeschen(String materialName)
	{
		// Falls Material benutzt wird auf diesen Objekten material auf null setzen  
		Material material = materialien.get(materialName);
		for(NURBS3D objekt:Szene.nurbsFlaechen) {if(objekt.material==material) objekt.material=null;}		
		materialien.remove(materialName);
	}

	public static void materialUmbenennen(String alterName,String neuerName)
	{
		// Umbenennen...
		materialien.get(alterName).name=neuerName;
		// und an neuer Stelle in der HashMap einfügen
		materialien.put(neuerName,Szene.materialien.remove(alterName));
		
	}
}
 