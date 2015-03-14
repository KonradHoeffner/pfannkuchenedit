package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.vecmath.*;

import nurbs.*;

import szene.*;


public class MainWindow implements ActionListener{
	
	// Fenster und Container
	JFrame frame = new JFrame("PfannkuchenEdit");
	OpenGLFrame[] openGLFrames;
	MaterialFrame materialFrame;
	
	// Menü
	static JMenuBar menuBar = new JMenuBar();
	static JMenu dateiMenue = new JMenu("Datei");
	static JMenuItem dateiBeenden = new JMenuItem("Beenden",'B');
	static JMenuItem dateiSzeneLaden = new JMenuItem("Szene laden",'l');
	static JMenuItem dateiSzeneSpeichern = new JMenuItem("Szene speichern",'s');
	static JMenuItem dateiSzeneExportieren = new JMenuItem("Szene als XML exportieren",'i');
	static JMenuItem dateiTriangulierungExportieren = new JMenuItem("Triangulierung als XML exportieren",'e');	
	
	static JMenu nurbsMenue = new JMenu("NURBS");
	static JMenuItem nurbsKnotenVektorAendern = new JMenuItem("Knotenvektor ändern",'K');
	
	static JMenu fensterMenue = new JMenu("Fenster");
	static JMenuItem fensterStandardLayout = new JMenuItem("Standardlayout");	
	
	static JMenuItem fensterVorschau = new JMenuItem("Vorschau");	
	static JMenu hilfeMenue = new JMenu("Hilfe");
	static JMenuItem hilfeInfo = new JMenuItem("Info",'I');
	
	// Toolbar mit Buttons
	JToolBar toolBar = new JToolBar("Toolbar"); // Immer angedockt, deshalb Titel nicht sichtbar
	
	ClassLoader cl = this.getClass().getClassLoader(); 
	 
	ImageIcon iconKnotenVektor = new ImageIcon(cl.getResource("gui/icons/knotenvektor.jpg"));
	
	ImageIcon iconButtonKameraModus = new ImageIcon(cl.getResource("gui/icons/kameramodus.jpg"));
	JButton buttonKameraModus = new JButton(iconButtonKameraModus);
	ImageIcon iconButtonObjektModus = new ImageIcon(cl.getResource("gui/icons/objektmodus.jpg"));
	JButton buttonObjektModus = new JButton(iconButtonObjektModus);
	ImageIcon iconButtonNURBSModus = new ImageIcon(cl.getResource("gui/icons/nurbsmodus.jpg"));
	JButton buttonNURBSModus = new JButton(iconButtonNURBSModus);
	
	ImageIcon iconButtonAuswahl = new ImageIcon(cl.getResource("gui/icons/auswahl.jpg"));
	JButton buttonAuswahl = new JButton(iconButtonAuswahl);
	ImageIcon iconButtonVerschieben = new ImageIcon(cl.getResource("gui/icons/verschieben.jpg"));
	JButton buttonVerschieben = new JButton(iconButtonVerschieben);
	ImageIcon iconButtonSkalieren = new ImageIcon(cl.getResource("gui/icons/skalieren.jpg"));
	JButton buttonSkalieren = new JButton(iconButtonSkalieren);
	ImageIcon iconButtonDrehen = new ImageIcon(cl.getResource("gui/icons/drehen.jpg"));
	JButton buttonDrehen = new JButton(iconButtonDrehen);
	ImageIcon iconButtonDuplizieren = new ImageIcon(cl.getResource("gui/icons/duplizieren.jpg"));
	JButton buttonDuplizieren = new JButton(iconButtonDuplizieren);
	ImageIcon iconButtonLoeschen = new ImageIcon(cl.getResource("gui/icons/loeschen.jpg"));
	JButton buttonLoeschen = new JButton(iconButtonLoeschen);
	ImageIcon iconButtonRendern = new ImageIcon(cl.getResource("gui/icons/rendern.jpg"));
	JButton buttonRendern = new JButton(iconButtonRendern);
	ImageIcon iconButtonNURBSKurve = new ImageIcon(cl.getResource("gui/icons/nurbskurve.jpg"));
	JButton buttonNURBSKurve = new JButton(iconButtonNURBSKurve);
	ImageIcon iconButtonNURBSFlaeche = new ImageIcon(cl.getResource("gui/icons/nurbsflaeche.jpg"));
	JButton buttonNURBSFlaeche = new JButton(iconButtonNURBSFlaeche);
	ImageIcon iconButtonSweepRotation = new ImageIcon(cl.getResource("gui/icons/sweeprotation.jpg"));
	JButton buttonSweepRotation = new JButton(iconButtonSweepRotation);
	ImageIcon iconButtonSweepVerschiebung = new ImageIcon(cl.getResource("gui/icons/sweepverschiebung.jpg"));
	JButton buttonSweepVerschiebung = new JButton(iconButtonSweepVerschiebung);
	ImageIcon iconButtonLichtquelle = new ImageIcon(cl.getResource("gui/icons/lichtquelle.jpg"));
	JButton buttonLichtquelle = new JButton(iconButtonLichtquelle);
	
	
	// weitere globale Variablen
	boolean isVorschau = false;
	// Gibt an ob gerade die Kamera, ein Objekt oder dessen NURBSStruktur bearbeitet werden soll
	
	Kamera kamera = new Kamera();
	
	public void setBearbeitungsModus(int modus)
	{
		Szene.bearbeitungsModus=modus;
		buttonKameraModus.setBackground(buttonLichtquelle.getBackground());
		buttonObjektModus.setBackground(buttonLichtquelle.getBackground());
		buttonNURBSModus.setBackground(buttonLichtquelle.getBackground());
		buttonNURBSKurve.setBackground(buttonLichtquelle.getBackground());
		switch(modus)
		{
		case Szene.KAMERA_MODUS:  {buttonKameraModus.setBackground(new Color(0.4f,0.4f,0.7f));break;}
		case Szene.OBJEKT_MODUS:  {buttonObjektModus.setBackground(new Color(0.4f,0.4f,0.7f));break;}
		case Szene.NURBS_MODUS:  {buttonNURBSModus.setBackground(new Color(0.4f,0.4f,0.7f));break;}
		case Szene.NURBSKURVE_ERZEUGEN_MODUS : {buttonNURBSKurve.setBackground(new Color(0.4f,0.4f,0.7f));break;}
		}
	}
	
	public void setBearbeitungsFunktion(int modus)
	{
		Szene.bearbeitungsFunktion=modus;
		buttonAuswahl.setBackground(buttonLichtquelle.getBackground());
		buttonVerschieben.setBackground(buttonLichtquelle.getBackground());
		buttonDrehen.setBackground(buttonLichtquelle.getBackground());
		buttonSkalieren.setBackground(buttonLichtquelle.getBackground());
		switch(modus)
		{
		case Szene.AUSWAHL_FUNKTION:  {buttonAuswahl.setBackground(new Color(0.4f,0.4f,0.7f));break;}
		case Szene.VERSCHIEBEN_FUNKTION:  {buttonVerschieben.setBackground(new Color(0.4f,0.4f,0.7f));break;}
		case Szene.SKALIEREN_FUNKTION:  {buttonSkalieren.setBackground(new Color(0.4f,0.4f,0.7f));break;}
		case Szene.DREHEN_FUNKTION:  {buttonDrehen.setBackground(new Color(0.4f,0.4f,0.7f));break;}
		
		}
	}
	
	
	public void setupSzene()
	{
		//Szene.objekte.add(Objekt3D.ErzeugePyramide(new Color3f(1,0,0),new Vector3d(0,2,0)));
		//Szene.objekte.add(Objekt3D.ErzeugePyramide(new Color3f(0,1,0),new Vector3d(2,0,0)));
		//Szene.objekte.add(Objekt3D.ErzeugePyramide(new Color3f(0,0,1),new Vector3d(0,0,2)));
		//Beispiel bsp = new Beispiel();
		SektGlas glas = new SektGlas(1);
		Szene.nurbsKurven.add(new Kurve3D(glas));
		
		//Kreis kreis = new Kreis();
		//SweepRotation sweep = new SweepRotation(glas, kreis);
		//HuegelLandschaft std = new HuegelLandschaft();
		//Szene.testFlaeche = std;
		//Szene.testFlaeche = sweep;
		//Szene.testKurve = glas;
		//int maxi = Szene.testFlaeche.getLastNode_k()-1;
		//int maxj = Szene.testFlaeche.getLastNode_l()-1;
		//NURBS3D sweepObjekt = new NURBS3D(sweep);
		
		//StandardFlaeche standardFlaeche = new StandardFlaeche(10);
		//sweepObjekt2.nurbsFlaeche.kontrollnetz[4][4].setZ(3);
		//sweepObjekt2.position.add(new Vector3d(-3,0,0));
		//standardFlaeche.kontrollnetz[4][4].setZ(3);
		//Szene.nurbsFlaechen.add(new NURBS3D(standardFlaeche));
		//Szene.nurbsFlaechen.add(sweepObjekt);
		
		
//		Lichtquelle l = new Lichtquelle(new Vector3d(0,6,0),10);
//		Szene.lichtquellen.add(l);
		//Szene.nurbsFlaechen.add(new NURBS3D(sweepObjekt));
		//Szene.testPunkte = new Point3d[100][100];
		//Szene.setAusgewaehltesObjekt(Szene.nurbsFlaechen.firstElement());
		//Szene.setAusgewaehltesObjekt(Szene.nurbsFlaechen.firstElement().nurbsFlaeche.getKontrollpunkt(3,3));
	}
	
	public void updateSzene()
	{
//		for(int i=0;i<25;i++)
//		for(int j=0;j<25;j++)
//		
//		{
//		DeBoorPunkt3d punkt = Szene.testFlaeche.auswerten_skaliert((double)(i)/25,(double)(j)/25);
//		Szene.testPunkte[i][j] = new Point3d(punkt.getX(),punkt.getY(),punkt.getZ());
//		}
	}
	
	
	MainWindow()
	{
		setupSzene();
		
		// Variablen an Szene übergeben
		Szene.kamera = kamera;
		Szene.mainWindow=this;
		// Frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setBounds(0,0,10000,10000);
		frame.setBounds(0,0,1500,1100);
		
		// Menü initialisieren
		// Dateimenü
		
		dateiMenue.add(dateiSzeneLaden);
		dateiSzeneLaden.setActionCommand("dateiszeneladen");
		dateiSzeneLaden.addActionListener(this);
		
		dateiMenue.add(dateiSzeneSpeichern);
		dateiSzeneSpeichern.setActionCommand("dateiszenespeichern");
		dateiSzeneSpeichern.addActionListener(this);
		
		dateiMenue.add(dateiSzeneExportieren);
		dateiSzeneExportieren.setActionCommand("dateiszeneexportieren");
		dateiSzeneExportieren.addActionListener(this);
		
		dateiMenue.add(dateiTriangulierungExportieren);
		dateiTriangulierungExportieren.setActionCommand("dateitriangulierungexportieren");
		dateiTriangulierungExportieren.addActionListener(this);
		
		dateiMenue.add(dateiBeenden);
		dateiBeenden.setActionCommand("dateibeenden");
		dateiBeenden.addActionListener(this);
		
		menuBar.add(dateiMenue);
		
		// Fenstermenü
		nurbsMenue.add(nurbsKnotenVektorAendern);
		nurbsKnotenVektorAendern.setActionCommand("nurbsknotenvektoraendern");											    
		nurbsKnotenVektorAendern.addActionListener(this);
		menuBar.add(nurbsMenue);
		

		// Fenstermenü
		fensterMenue.add(fensterStandardLayout);
		fensterStandardLayout.setActionCommand("fensterstandardlayout");											    
		fensterStandardLayout.addActionListener(this);
		fensterStandardLayout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
		
		fensterMenue.add(fensterVorschau);
		fensterVorschau.setActionCommand("fenstervorschau");
		fensterVorschau.addActionListener(this);
		fensterVorschau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,0));
		menuBar.add(fensterMenue);
		
		
		// Hilfemenü
		
		hilfeMenue.add(hilfeInfo);
		hilfeInfo.setActionCommand("hilfeinfo");
		hilfeInfo.addActionListener(this);
		hilfeInfo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
		menuBar.add(hilfeMenue);
		
		// Panel
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		frame.setContentPane(panel);
		panel.setSize(50,50);
		panel.setBackground(Color.BLUE);
		frame.setJMenuBar(menuBar);
		
		// Toolbar oben
		toolBar.setFloatable(false);
		toolBar.setOrientation(JToolBar.HORIZONTAL);
		toolBar.setRollover(true);
		toolBar.setSize(100,100);
		panel.add(toolBar,BorderLayout.PAGE_START);
		
		// Buttons
		
		buttonKameraModus.setActionCommand("buttonkameramodus");
		buttonKameraModus.addActionListener(this);
		buttonKameraModus.setToolTipText("Kameramodus");
		
		buttonObjektModus.setActionCommand("buttonobjektmodus");
		buttonObjektModus.addActionListener(this);
		buttonObjektModus.setToolTipText("Objektmodus");
		
		buttonNURBSModus.setActionCommand("buttonnurbsmodus");
		buttonNURBSModus.addActionListener(this);
		buttonNURBSModus.setToolTipText("NURBS - Modus");
		
		buttonAuswahl.setActionCommand("buttonauswahl");
		buttonAuswahl.addActionListener(this);
		buttonAuswahl.setToolTipText("Auswahl");
		
		buttonVerschieben.setActionCommand("buttonverschieben");
		buttonVerschieben.addActionListener(this);
		buttonVerschieben.setToolTipText("Verschieben");
		
		buttonSkalieren.setActionCommand("buttonskalieren");
		buttonSkalieren.addActionListener(this);
		buttonSkalieren.setToolTipText("Skalieren");
		
		buttonDrehen.setActionCommand("buttondrehen");
		buttonDrehen.addActionListener(this);
		buttonDrehen.setToolTipText("Drehen");
		
		buttonDuplizieren.setActionCommand("buttonduplizieren");
		buttonDuplizieren.addActionListener(this);
		buttonDuplizieren.setToolTipText("Duplizieren");
		
		buttonLoeschen.setActionCommand("buttonloeschen");
		buttonLoeschen.addActionListener(this);
		buttonLoeschen.setToolTipText("Loeschen");
		
		buttonRendern.setActionCommand("buttonrendern");
		buttonRendern.addActionListener(this);
		buttonRendern.setToolTipText("Rendern");
		
		buttonNURBSKurve.setActionCommand("buttonnurbskurve");
		buttonNURBSKurve.addActionListener(this);
		buttonNURBSKurve.setToolTipText("NURBS - Kurve erzeugen");
		
		buttonNURBSFlaeche.setActionCommand("buttonnurbsflaeche");
		buttonNURBSFlaeche.addActionListener(this);
		buttonNURBSFlaeche.setToolTipText("Offene NURBS - Fläche erzeugen");
		
		buttonSweepRotation.setActionCommand("buttonsweeprotation");
		buttonSweepRotation.addActionListener(this);
		buttonSweepRotation.setToolTipText("Sweep - Rotationsfläche erzeugen");
		
		buttonSweepVerschiebung.setActionCommand("buttonsweepverschiebung");
		buttonSweepVerschiebung.addActionListener(this);
		buttonSweepVerschiebung.setToolTipText("Sweep - Verschiebefläche erzeugen");
		
		buttonLichtquelle.setActionCommand("buttonlichtquelle");
		buttonLichtquelle.addActionListener(this);
		buttonLichtquelle.setToolTipText("Lichtquelle hinzufügen");
		
		toolBar.add(buttonKameraModus);
		toolBar.add(buttonObjektModus);
		toolBar.add(buttonNURBSModus);
		
		toolBar.add(buttonAuswahl);
		toolBar.add(buttonVerschieben);
		toolBar.add(buttonSkalieren);
		
		toolBar.add(buttonDrehen);
		toolBar.add(buttonDuplizieren);
		toolBar.add(buttonLoeschen);
		
		toolBar.add(buttonRendern);
		toolBar.add(buttonNURBSKurve);
		toolBar.add(buttonNURBSFlaeche);
		
		toolBar.add(buttonSweepRotation);
		toolBar.add(buttonSweepVerschiebung);
		toolBar.add(buttonLichtquelle);
		
		setBearbeitungsModus(Szene.bearbeitungsModus);
		setBearbeitungsFunktion(Szene.bearbeitungsFunktion);
		
		//while(icon1.getImageLoadStatus()!=MediaTracker.COMPLETE) if(icon1.getImageLoadStatus()==MediaTracker.ABORTED||icon1.getImageLoadStatus()==MediaTracker.ERRORED) {System.out.println("Fehler beim Laden vom Icon");System.exit(0);}
		//toolBar.add(button1);
		
		// Desktop mitte
		JDesktopPane desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);		
		/*Dimension d = new Dimension(100,100);
		 desktop.setPreferredSize(new Dimension(100,100));
		 desktop.setSize(100,100);*/
		desktop.setBackground(new Color(200,200,255));
		panel.add(desktop,BorderLayout.CENTER);
		
		// Internal Frames
		openGLFrames = new OpenGLFrame[4];
		for(int typ=0;typ<4;typ++)
		{
			openGLFrames[typ]= new OpenGLFrame(typ);
			desktop.add(openGLFrames[typ]);
			
			// setVisible wird bereits im Konstruktor von OpenGLFrame gesetzt und Position/Größe wird in standardLayout() festgelegt deswegen hier nur Initialisierung
		}
		materialFrame = new MaterialFrame();
		materialFrame.setLocation(frame.getWidth()-materialFrame.getWidth()-10,0);
		desktop.add(materialFrame);
		
		//JTextArea output = new JTextArea();
		//output.setBounds(100,100,1000,1000);
		//desktop.add(output);
		standardLayout();
		
		
		kamera.position = new Vector3d(0,4,20);
		kamera.ziel = new Vector3d(0,0,5);
		
		//Rotierer rotierer = new Rotierer(kamera);
		//rotierer.start();
	}
	
	public static void main(String[] args) {
		Runnable runnable = new Runnable()
		{
			public void run() { new MainWindow();}	
		};
		SwingUtilities.invokeLater(runnable);
//		try {SwingUtilities.invokeLater(runnable);}catch(Exception e){};		
	}
	
	// Die Funktionen, die vom Menü aufgerufen werden:
	
	
	// Dateimenü
	
//	public void ascImportieren(String dateiName)
//	{
//		// wird von ascImportieren() aufgerufen
//	}
//	
//	/**
//	 * Lässt Benutzer eine Textdatei angeben und lädt daraus die triangulierte Szene (Polygone+Materialien+Lichtquellen)  
//	 */
//	public void ascImportieren()
//	{
//		JOptionPane.showMessageDialog(frame,"Hier kommt das Importieren hin. Möchten sie wirklich die Szene verwerfen ");
//	}
//	
//	public void ascExportieren(String dateiName)
//	{
//		// wird von ascExportieren() aufgerufen		
//	}
//	
//	/**
//	 * Lässt Benutzer eine Textdatei angeben und speichert darin die triangulierte Szene (Polygone+Materialien+Lichtquellen)  
//	 */
//	public void ascExportieren()
//	{
//		JOptionPane.showMessageDialog(frame,"Hier kommt das Exportieren hin. Möchten sie wirklich überschreiben?");
//	}
	
	// Fenstermenü
	
	public void standardLayout()
	{
		for(int typ=0;typ<4;typ++)
		{
			// Höhe vom Desktop wird nicht richtig ausgelesen, deshalb wird die vom Frame genommen :-(
			openGLFrames[typ].setBounds(0,0,frame.getWidth()*2/5,frame.getHeight()*2/5);
			//openGLFrames[typ].setBounds(0,0,300,300);
			//openGLFrames[typ].setVisible(true);
			// Offsetberechnung Pascal - Style - warum einfach, wenns auch kompliziert geht :-)
			openGLFrames[typ].setLocation(typ%2*frame.getWidth()*2/5,typ/2*frame.getHeight()*2/5);
			/*			try {
			 // doppelter Aufruf, falls Fenster minimiert (es gibt keine Methode setMinimum() )
			  openGLFrames[typ].setMaximum(true);
			  openGLFrames[typ].setMaximum(false);
			  } catch (PropertyVetoException e) {
			  // TODO Automatisch erstellter Catch-Block
			   e.printStackTrace();
			   }*/
		}
		isVorschau=false;
	}
	
	
	/**
	 * Maximiert das Perspektivfenster um einen Voreindruck des gerenderten Bildes zu erhalten.
	 * @throws PropertyVetoException 
	 * @throws PropertyVetoException 
	 */
	public void vorschau()
	{
		isVorschau=!isVorschau;	
		try {openGLFrames[3].setMaximum(isVorschau);}			 catch (PropertyVetoException e) {System.out.println("Fehler beim Maximieren.");} 
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		System.out.println(command);
		// Komischerweise gehts auch ohne command.equals() einfach nur mit == 
		// Menüitems
		if(command=="dateibeenden") System.exit(0);
		if(command=="dateiszeneladen") {ImportExport.szeneLaden(frame);return;}
		if(command=="dateiszenespeichern") {ImportExport.szeneSpeichern(frame);return;}
		if(command=="dateiszeneexportieren") {ImportExport.exportSzeneXML(frame);return;}
		if(command=="dateitriangulierungexportieren") {ImportExport.exportTriangulationXML(frame);return;}
		if(command=="nurbsknotenvektoraendern") {knotenVektorAendern();return;}
		if(command=="fensterstandardlayout") {standardLayout();return;}
		if(command=="fenstervorschau") {vorschau();return;}
		if(command=="hilfeinfo") {
			JOptionPane.showMessageDialog(frame,
				    "Sie bedienen PfannkuchenEdit. Ein ganz tolles Programm by Marcus Daum & Konrad Höffner 2006.",
				    "PfannkuchenEdit",JOptionPane.INFORMATION_MESSAGE,
				    iconButtonAuswahl);
		}
		// Buttons
		if(command=="buttonkameramodus") setBearbeitungsModus(Szene.KAMERA_MODUS);
		if(command=="buttonobjektmodus") setBearbeitungsModus(Szene.OBJEKT_MODUS);
		if(command=="buttonnurbsmodus") setBearbeitungsModus(Szene.NURBS_MODUS);
		
		if(command=="buttonauswahl") setBearbeitungsFunktion(Szene.AUSWAHL_FUNKTION);
		if(command=="buttonverschieben") setBearbeitungsFunktion(Szene.VERSCHIEBEN_FUNKTION);
		if(command=="buttonskalieren") setBearbeitungsFunktion(Szene.SKALIEREN_FUNKTION);
		if(command=="buttondrehen") setBearbeitungsFunktion(Szene.DREHEN_FUNKTION);		
		
		if(command=="buttonduplizieren") duplizieren();		
		if(command=="buttonloeschen") loeschen();
		if(command=="buttonrendern") rendern();
		if(command=="buttonnurbskurve") nurbsKurve();
		if(command=="buttonnurbsflaeche") nurbsFlaeche();
		if(command=="buttonsweeprotation") sweepRotation();
		if(command=="buttonsweepverschiebung") sweepVerschiebung();
		if(command=="buttonlichtquelle") lichtquelle();
	}
	
	private String knotenVektorToString(double[] knotenVektor)
	{
		String s="";
		for(int i=0;i<knotenVektor.length;i++)
		{
			s+=knotenVektor[i];
			if(i<(knotenVektor.length-1)) s+=" , ";else s+=".";
		}
		return s;
	}

	private double[] stringToKnotenVektor(String s)
	{
		// Trennzeichen : das Semikolion ";", das Komma "," und das "|"
		Pattern trennzeichenPattern = Pattern.compile("(,|\\||;)");
		String [] eintraege = trennzeichenPattern.split (s);
		double[] knotenVektor = new double[eintraege.length];
		for (int i = 0; i < eintraege.length; i++) knotenVektor[i] = Double.parseDouble(eintraege[i]);
//		}
		return knotenVektor;	
	}

	
	private void nurbsFlaecheKnotenVektorAendern() {
		NURBSFlaeche nurbsFlaeche = Szene.ausgewaehlteNURBSFlaeche.nurbsFlaeche;
		double[] alterKnotenVektorL = nurbsFlaeche.getKnotenvektor_L();			
		double[] alterKnotenVektorK = nurbsFlaeche.getKnotenvektor_K();
		String knotenVektorKString = (String)JOptionPane.showInputDialog(
				frame,
				"Knotenvektor k ="+knotenVektorToString(alterKnotenVektorK)+"\n"+
				alterKnotenVektorK.length+" Einträge, durch Kommata getrennt, Format: 'a.b,c.d,...')",
				"Neuen Knotenvektor k eingeben",
				JOptionPane.QUESTION_MESSAGE);
		if(knotenVektorKString==null) return;
		double[] neuerKnotenVektorK;
		try
		{
		neuerKnotenVektorK = stringToKnotenVektor(knotenVektorKString);
		} catch(Exception e)
		{
			JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe.");
			return;
		}
		if(neuerKnotenVektorK.length!=alterKnotenVektorK.length)
		{
			JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe. Anzahl der Einträge stimmt nicht überein.");
			return;				
		}
			try {
				nurbsFlaeche.setKnotenvektor_K(neuerKnotenVektorK);
			} catch (KnotenvektorException e) {
				JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe. Anzahl der Einträge stimmt aber Monotonie ist nicht gegeben.");
				return;					
		}
		

	String knotenVektorLString = (String)JOptionPane.showInputDialog(
			frame,
			"Knotenvektor l ="+knotenVektorToString(alterKnotenVektorL)+"\n"+
			alterKnotenVektorL.length+" Einträge, durch Kommata getrennt, Format: 'a.b,c.d,...')",
			"Neuen Knotenvektor l eingeben",
			JOptionPane.QUESTION_MESSAGE);
	if(knotenVektorLString==null) return;
	double[] neuerKnotenVektorL;
	try
	{
	neuerKnotenVektorL = stringToKnotenVektor(knotenVektorLString);
	} catch(Exception e)
	{
		JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe.");
		return;
	}
	if(neuerKnotenVektorL.length!=alterKnotenVektorL.length)
	{
		JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe. Anzahl der Einträge stimmt nicht überein.");
		return;				
	}
			try {
				nurbsFlaeche.setKnotenvektor_L(neuerKnotenVektorL);
			} catch (KnotenvektorException e) {
				JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe. Anzahl der Einträge stimmt aber Monotonie ist nicht gegeben.");
				return;					
			}
			Szene.ausgewaehlteNURBSFlaeche.update();
	}
	
	private void nurbsKurveKnotenVektorAendern()
	{
		NURBSKurve nurbsKurve = Szene.ausgewaehlteNURBSKurve.nurbsKurve;
		double[] alterKnotenVektor = nurbsKurve.getKnotenvektor();			
		String knotenVektorString = (String)JOptionPane.showInputDialog(
				frame,
				"Knotenvektor ="+knotenVektorToString(alterKnotenVektor)+"\n"+
				alterKnotenVektor.length+" Einträge, durch Kommata getrennt, Format: 'a.b,c.d,...')",
				"Neuen Knotenvektor eingeben",
				JOptionPane.QUESTION_MESSAGE);
		if(knotenVektorString==null) return;
		double[] neuerKnotenVektor;
		try
		{
		neuerKnotenVektor = stringToKnotenVektor(knotenVektorString);
		} catch(Exception e)
		{
			JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe.");
			return;
		}
		if(neuerKnotenVektor.length!=alterKnotenVektor.length)
		{
			JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe. Anzahl der Einträge stimmt nicht überein.");
			return;				
		}
			try {
				nurbsKurve.setKnotenvektor(neuerKnotenVektor);
			} catch (KnotenvektorException e) {
				JOptionPane.showMessageDialog(frame,"Fehler bei der Eingabe. Anzahl der Einträge stimmt aber Monotonie ist nicht gegeben.");
				return;					
		}	
			Szene.ausgewaehlteNURBSKurve.update();
	}

	private void knotenVektorAendern() {
		if(Szene.ausgewaehlterTyp==Szene.NURBSFLAECHE_AUSGEWAEHLT)
			nurbsFlaecheKnotenVektorAendern();
		if(Szene.ausgewaehlterTyp==Szene.NURBSKURVE_AUSGEWAEHLT)
			nurbsKurveKnotenVektorAendern();
		
		

		}
		
		
			
		
		
	

	private void sweepRotation() {
		if(Szene.ausgewaehlterTyp==Szene.NURBSKURVE_AUSGEWAEHLT)
		{
			NURBS3D sweep = new NURBS3D(new SweepRotation(Szene.ausgewaehlteNURBSKurve.nurbsKurve, new Kreis()));
			Szene.nurbsFlaechen.add(sweep);
			Szene.setAusgewaehltesObjekt(sweep);
		}
	}

	private void sweepVerschiebung() {
		if(Szene.ausgewaehlterTyp==Szene.NURBSKURVE_AUSGEWAEHLT)
		{
			NURBS3D sweep = new NURBS3D(new SweepVerschiebung(Szene.ausgewaehlteNURBSKurve.nurbsKurve));
			Szene.nurbsFlaechen.add(sweep);
			Szene.setAusgewaehltesObjekt(sweep);
		}
	}

	private void lichtquelle() {
		Lichtquelle l = new Lichtquelle();
		Szene.lichtquellen.add(l);
		Szene.setAusgewaehltesObjekt(l);
	}

	public int getOrdnung(String zusatzText,String titel,Icon icon)
	{
		return getOrdnung(zusatzText,titel,icon,true,true,true);
	}

	public int getOrdnung(String zusatzText,String titel,Icon icon,boolean ordnung2,boolean ordnung3,boolean ordnung4)
	{
		Vector<String> possibilitiesVector = new Vector<String>();
		if(ordnung2) possibilitiesVector.add("Ordnung 2");
		if(ordnung3) possibilitiesVector.add("Ordnung 3");
		if(ordnung4) possibilitiesVector.add("Ordnung 4");
		Object[] possibilities = possibilitiesVector.toArray();		
		String s = (String)JOptionPane.showInputDialog(
		                    frame,
		                    "Bitte wählen sie die Ordnung "+zusatzText,
		                    titel,
		                    JOptionPane.PLAIN_MESSAGE,
		                    icon,
		                    possibilities,
		                    "Ordnung 3");
		if ((s != null) && (s.length() > 0))
		{
			if(s.equals("Ordnung 2")) return 2;
			if(s.equals("Ordnung 3")) return 3;
			if(s.equals("Ordnung 4")) return 4;
		}
		return -1;
		
	}
	
	private void nurbsFlaeche() {
		
		int ordnungK = getOrdnung("k","Offene NURBS - Fläche erzeugen",iconButtonNURBSFlaeche,true,true,true);
		if(ordnungK==-1) return;
		
		int ordnungL = getOrdnung("l","Offene NURBS - Fläche erzeugen",iconButtonNURBSFlaeche);
		if(ordnungL==-1) return;

		NURBS3D standard = new NURBS3D(new StandardFlaeche(6,ordnungK,ordnungL));
		Szene.nurbsFlaechen.add(standard);
		Szene.setAusgewaehltesObjekt(standard);
		
	}
	
	private void nurbsKurve() {
		setBearbeitungsModus(Szene.NURBSKURVE_ERZEUGEN_MODUS);
		
		
		
	}
	
	private void loeschen() {
		switch(Szene.ausgewaehlterTyp)
		{
		case Szene.NURBSFLAECHE_AUSGEWAEHLT:
		{
			if(Szene.ausgewaehlteNURBSFlaeche!=null)
			{
				Szene.nurbsFlaechen.remove(Szene.ausgewaehlteNURBSFlaeche);
				Szene.abwaehlen();
				break;
			}
		}
		case Szene.NURBSKURVE_AUSGEWAEHLT:
		{
			if(Szene.ausgewaehlteNURBSKurve!=null)
			{
				Szene.nurbsKurven.remove(Szene.ausgewaehlteNURBSKurve);
				Szene.abwaehlen();
				break;
			}
		}
		case Szene.LICHTQUELLE_AUSGEWAEHLT:
		}
		if(Szene.ausgewaehlteLichtquelle!=null)
		{
			Szene.lichtquellen.remove(Szene.ausgewaehlteLichtquelle);
			Szene.abwaehlen();		
		}
	}
	
	
	
	private void rendern() {
		JOptionPane.showMessageDialog(frame,"Hier könnte ihr Raytracer steh'n!");
	}
	
	private void duplizieren() {
		if(Szene.ausgewaehlteNURBSFlaeche!=null)
		{
			NURBS3D nurbs = new NURBS3D(Szene.ausgewaehlteNURBSFlaeche);
			Szene.nurbsFlaechen.add(nurbs); 
		}
	}
	
}