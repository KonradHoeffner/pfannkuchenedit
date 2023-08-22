/** Created on 02.07.2006 */
package gui;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.vecmath.Matrix4d;
import javax.vecmath.Tuple3d;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import szene.Kamera;
import szene.Lichtquelle;
import szene.Material;
import szene.NURBS3D;
import szene.Polygon3D;
import szene.Szene;
import szene.VektorMethoden;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

public class ImportExport {
	
	/**
	 * Lässt den Benutzer eine Datei angeben und erzeugt diese neu. Falls sie schon existiert, wird nachgefragt ob sie überschrieben werden darf.
	 */
	public static File chooseFile(JFrame frame,String extension)
	{
//		Create a file chooser
		JFileChooser fc = new JFileChooser(new File ("."));
		FileFilter filter = new ExtensionFilter(extension);
		fc.setFileFilter(filter);
		File file;
		int returnVal = fc.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			file = fc.getSelectedFile();
			return file;
		}
		return null;   
	}

	/**
	 * Lässt den Benutzer eine existierende Datei zum öffnen angeben und gibt ein File - Objekt darauf zurück.
	 */
	
	public static File chooseOpenFile(JFrame frame,String extension)
	{
//		Create a file chooser
		JFileChooser fc = new JFileChooser(new File ("."));
		FileFilter filter = new ExtensionFilter(extension);
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		return null;
	}
	
	private static void domParse(JFrame frame, DOMParser dom,String filename)
	{
		try {
			dom.parse(filename);
		} catch (SAXException e) {
			JOptionPane.showMessageDialog(frame,"SAX - Fehler beim XML - Export. "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame,"IO - Fehler beim XML - Export. "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void exportSzeneXML(JFrame frame)
	{
		File file = chooseFile(frame,"xml");
		DOMParser dom = new DOMParser();
		domParse(frame, dom,"vorlageszene.xml");
		Document doc = dom.getDocument();
		Element root = doc.getDocumentElement();
		
		Element kamera = (Element) root.getElementsByTagName("kamera").item(0);
		Element beleuchtung = (Element) root.getElementsByTagName("beleuchtung").item(0);
		
		Element position = (Element) kamera.getElementsByTagName("position").item(0);
		Element ziel = (Element) kamera.getElementsByTagName("ziel").item(0);
		Element fovy = (Element) kamera.getElementsByTagName("fovy").item(0);
		
		position.setAttribute("x",Double.toString(Szene.kamera.position.x));
		position.setAttribute("y",Double.toString(Szene.kamera.position.y));
		position.setAttribute("z",Double.toString(Szene.kamera.position.z));
		ziel.setAttribute("x",Double.toString(Szene.kamera.ziel.x));
		ziel.setAttribute("y",Double.toString(Szene.kamera.ziel.y));
		ziel.setAttribute("z",Double.toString(Szene.kamera.ziel.z));
		fovy.setAttribute("winkel",Double.toString(Szene.kamera.fovy));
		
		Iterator<Lichtquelle> it = Szene.lichtquellen.iterator();
		while(it.hasNext())
		{
			Lichtquelle lichtquelle = it.next();
			Element lichtquelleElement = doc.createElement("lichtquelle");
			beleuchtung.appendChild(lichtquelleElement);
			Element positionElement = doc.createElement("position");
			lichtquelleElement.appendChild(positionElement);
			Element farbeElement = doc.createElement("farbe");
			lichtquelleElement.appendChild(farbeElement);
			
			positionElement.setAttribute("x",Double.toString(lichtquelle.position.x));
			positionElement.setAttribute("y",Double.toString(lichtquelle.position.y));
			positionElement.setAttribute("z",Double.toString(lichtquelle.position.z));

			farbeElement.setAttribute("r",Double.toString(((double)lichtquelle.farbe.getRed())/255));
			farbeElement.setAttribute("g",Double.toString(((double)lichtquelle.farbe.getGreen())/255));
			farbeElement.setAttribute("b",Double.toString(((double)lichtquelle.farbe.getBlue())/255));
			
		}
		
		xmlSerialize(frame,file,doc);
}

	private static String intColorValueToDoubleString(int i)
	{
		return Double.toString(((double)i)/255);
	}
	
	public static void exportTriangulationXML(JFrame frame)
	{
		File file = chooseFile(frame,"xml");
		DOMParser dom = new DOMParser();
		domParse(frame, dom,"vorlagetriangulation.xml");
		Document doc = dom.getDocument();
		Element rootElement = doc.getDocumentElement();
		
		Collection<Material> materialien =  Szene.materialien.values();
		Iterator<Material> itMaterial = materialien.iterator();
		while(itMaterial.hasNext())
		{
			Material material = itMaterial.next();
			Element materialElement = doc.createElement("material");
			rootElement.appendChild(materialElement);
			
			materialElement.setAttribute("name",material.name);
			materialElement.setAttribute("glanzwert",Byte.toString(material.glanz));		
			
			materialElement.appendChild(createElementFromColor(doc,"ambient",material.ambient));
			materialElement.appendChild(createElementFromColor(doc,"diffus",material.diffus));
			materialElement.appendChild(createElementFromColor(doc,"spiegelnd",material.spiegelnd));			
		}
		
		Iterator<NURBS3D> itNURBSFlaeche = Szene.nurbsFlaechen.iterator();
		while(itNURBSFlaeche.hasNext())
		{
			NURBS3D nurbs = itNURBSFlaeche.next();
			Polygon3D[] polygone = nurbs.getTriangulierung();
			
			Matrix4d translation = new Matrix4d();
			translation.setIdentity();
			translation.setTranslation(nurbs.position);
			Matrix4d skalierung = new Matrix4d();
			skalierung.setIdentity();
			skalierung.setElement(0,0,nurbs.skalierung.x);
			skalierung.setElement(1,1,nurbs.skalierung.y);
			skalierung.setElement(2,2,nurbs.skalierung.z);
			
			Matrix4d transformation = new Matrix4d(translation);
			transformation.mul(skalierung);
			transformation.mul(nurbs.rotationsMatrix);
			
			// Koordinaten im Objekt müssen zu Weltkoordinaten umgerechnet werden
			for(Polygon3D polygonImObjekt:polygone)
			{
				Polygon3D polygon = new Polygon3D(polygonImObjekt);
				
				// getestet: scheint zu klappen
				for(int i=0;i<3;i++)
				{
					Matrix4d neuerPunktMatrix = new Matrix4d(transformation);
					Matrix4d neueNormaleMatrix = new Matrix4d(transformation);
					Matrix4d alterPunktMatrix = VektorMethoden.VectorToMatrix(polygon.punkte[i]);
					Matrix4d alteNormaleMatrix = VektorMethoden.VectorToMatrix(polygon.punktNormalen[i]);
					neuerPunktMatrix.mul(alterPunktMatrix);
					neueNormaleMatrix.mul(alteNormaleMatrix);
					polygon.punkte[i] = VektorMethoden.MatrixToVector(neuerPunktMatrix);
					polygon.punktNormalen[i] = VektorMethoden.MatrixToVector(neueNormaleMatrix);
				}
				
				Element dreieckElement = doc.createElement("dreieck");
				rootElement.appendChild(dreieckElement);
				dreieckElement.setAttribute("material",nurbs.material.name);
				dreieckElement.appendChild(createElementFromVector3d(doc,"punkt1",polygon.punkte[0]));
				dreieckElement.appendChild(createElementFromVector3d(doc,"punkt2",polygon.punkte[1]));
				dreieckElement.appendChild(createElementFromVector3d(doc,"punkt3",polygon.punkte[2]));
				dreieckElement.appendChild(createElementFromVector3d(doc,"normale1",polygon.punktNormalen[0]));
				dreieckElement.appendChild(createElementFromVector3d(doc,"normale2",polygon.punktNormalen[1]));
				dreieckElement.appendChild(createElementFromVector3d(doc,"normale3",polygon.punktNormalen[2]));				
			}			
		}
		xmlSerialize(frame,file,doc);

	}
	
	private static Element createElementFromVector3d(Document doc, String name,Tuple3d tuple3d)
	{
		Element element = doc.createElement(name);
		element.setAttribute("x",Double.toString(tuple3d.x));
		element.setAttribute("y",Double.toString(tuple3d.y));
		element.setAttribute("z",Double.toString(tuple3d.z));
		return element;
	}

	private static Element createElementFromColor(Document doc, String name,Color color)
	{
		Element element = doc.createElement(name);
		element.setAttribute("r",intColorValueToDoubleString(color.getRed()));
		element.setAttribute("g",intColorValueToDoubleString(color.getGreen()));
		element.setAttribute("b",intColorValueToDoubleString(color.getBlue()));
		element.setAttribute("a",intColorValueToDoubleString(color.getAlpha()));
		return element;
	}
	
	private static void xmlSerialize(JFrame frame,File file, Document doc)
	{
		XMLSerializer serializer = null;
		try {
			serializer = new XMLSerializer(new FileOutputStream(file),new OutputFormat(doc));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame,"Fehler beim XML - Export. "+e.getMessage());
			e.printStackTrace();
		}
		try {
			serializer.serialize(doc);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame,"Fehler beim XML - Export. "+e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Lässt Benutzer eine Binärdatei im pfk Format angeben und lädt daraus die gesamte Szene (NURBS Flächen+Kurven+Materialien+Lichtquellen)  
	 */
	public static void szeneLaden(JFrame frame)
	{
		try
		{
			File file = chooseOpenFile(frame,"pfk");
			if(file!=null)
			{			
				System.out.println(file.getPath());
				FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
				ObjectInputStream o = new ObjectInputStream( fileInputStream );
				Szene.materialien = (HashMap<String,Material>)o.readObject();
				Szene.mainWindow.materialFrame.update();
				Szene.nurbsFlaechen=(Vector<NURBS3D>) o.readObject();
				for(NURBS3D nurbs:Szene.nurbsFlaechen) nurbs.update();
				Szene.lichtquellen = (Vector<Lichtquelle>) o.readObject();
				//				Szene.nurbsKurven=(Vector<Kurve3D>) o.readObject();
				Szene.kamera=(Kamera) o.readObject();
				System.out.println("Kamera:");
				System.out.println(Szene.kamera.position.x);
				System.out.println(Szene.kamera.position.y);
				System.out.println(Szene.kamera.position.z);
		
				o.close();
			}
			
		} catch(Exception e) {e.printStackTrace();}
		
	}
	
	/**
	 * Lässt Benutzer eine Datei angeben und speichert dann gesamte Szene mittels Serialisierung dort hinein (NURBS Flächen+Kurven+Materialien+Lichtquellen)  
	 */
	
	public static void szeneSpeichern(JFrame frame)
	{
		try
		{
			File file = chooseFile(frame,"pfk");
			if(file!=null)
			{	
			FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
			ObjectOutputStream o = new ObjectOutputStream( fileOutputStream );  
			o.writeObject  (Szene.materialien);
			for(NURBS3D nurbs:Szene.nurbsFlaechen) nurbs.loescheTriangulierung();	
			o.writeObject  (Szene.nurbsFlaechen);          
			o.writeObject  (Szene.lichtquellen);
			//			o.writeObject  (Szene.nurbsKurven);
			System.out.println("Kamera:");
			System.out.println(Szene.kamera.position.x);
			System.out.println(Szene.kamera.position.y);
			System.out.println(Szene.kamera.position.z);

			o.writeObject  (Szene.kamera);          
			o.close();
			}
		} catch(Exception e) {/**/}   
		// wird von szeneSpeichern() aufgerufen		
	}
	
	
}
