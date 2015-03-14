package gui;

import java.nio.IntBuffer;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import nurbs.DeBoorPunkt3d;
import nurbs.NURBSFlaeche;
import nurbs.NURBSKurve;
import szene.*;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLPipelineFactory;
import com.jogamp.opengl.glu.GLU;

/** Created on 29.05.2006 */

public class Renderer implements GLEventListener
{

	private GL2 gl;
	private GLDrawable glDrawable;
	private GLU glu;
	private int height;
	private int width;
	// double count=0;
	int typ = 3;
	// private Kamera kamera;
	private boolean viewUpdated;

	// Startnummern fürs Picking (Beispiel. : Nurbsflächen werden mit Nummern von
	// 1000-2000 identifiziert)
	private final int PICKING_NURBSFLAECHEN_OFFSET = 1000;
	private final int PICKING_NURBSKURVEN_OFFSET = 2000;
	private final int PICKING_NURBS_KONTROLLPUNKTE_OFFSET = 3000;
	private final int PICKING_KURVE_KONTROLLPUNKTE_OFFSET = 4000;
	private final int PICKING_LICHTQUELLEN_OFFSET = 5000;

	// Gr��e für den Picking - Buffer
	private final int BUFFSIZE = 512;

	private boolean picking = false;
	private Vector2d mausPosition = null;

	/**
	 * @param typ
	 */
	public Renderer(int typ) {
		this.typ = typ;
	}

	void pick(Vector2d mausPosition)
	{
		this.mausPosition = mausPosition;
		picking = true;
	}

	private void setMaterial(Material material)
	{
		float mat_ambient[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float mat_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float mat_shininess = 2;

		if (material != null) {
			mat_ambient = material.ambient.getComponents(null);
			// for(float f:mat_ambient) System.out.println(f);
			mat_diffuse = material.diffus.getComponents(null);
			mat_specular = material.spiegelnd.getComponents(null);
			mat_shininess = material.glanz;
		}
		/*
		 * float mat_ambient[] =
		 * {material.ambient.getRed(),material.ambient.getGreen
		 * (),material.ambient.getBlue(),material.ambient.getAlpha()}; float
		 * mat_diffuse[] =
		 * {material.diffus.getRed(),material.diffus.getGreen(),material
		 * .diffus.getBlue(),material.diffus.getAlpha()}; float mat_specular[] =
		 * {material
		 * .spiegelnd.getRed(),material.spiegelnd.getGreen(),material.spiegelnd
		 * .getBlue(),material.spiegelnd.getAlpha()};
		 */
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, mat_ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, mat_diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, mat_specular, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, mat_shininess);

	}

	private void LichtquelleSpitzlicht(float[] position, int lichtnummer)
	{
		gl.glEnable(lichtnummer);
		// float lpos[] = { 10.0f, 10.0f, 10.0f, 1.0f}; // Koordinaten
		float ambi[] = { 0.0f, 0.0f, 0.0f, 0.0f }; // RGBA ambient
		float diff[] = { 0.3f, 0.3f, 0.3f, 1.0f }; // RGBA diffus
		float spec[] = { 1.0f, 1.0f, 1.0f, 1.0f }; // RGBA spiegelnd

		gl.glLightfv(lichtnummer, GL2.GL_POSITION, position, 0);
		gl.glLightfv(lichtnummer, GL2.GL_AMBIENT, ambi, 0);
		gl.glLightfv(lichtnummer, GL2.GL_DIFFUSE, diff, 0);
		gl.glLightfv(lichtnummer, GL2.GL_SPECULAR, spec, 0);

		gl.glLighti(lichtnummer, GL2.GL_CONSTANT_ATTENUATION, 1);
		gl.glLighti(lichtnummer, GL2.GL_LINEAR_ATTENUATION, 0);
		gl.glLighti(lichtnummer, GL2.GL_QUADRATIC_ATTENUATION, 0);
	}

	private void LichtquelleWeichesLicht(float[] position, int lichtnummer)
	{
		gl.glEnable(lichtnummer);
		// float lpos[] = { 10.0f, 10.0f, 10.0f, 1.0f}; // Koordinaten
		float ambi[] = { 0.1f, 0.1f, 0.1f, 1.0f }; // RGBA ambient
		float diff[] = { 0.5f, 0.5f, 0.5f, 1.0f }; // RGBA diffus
		float spec[] = { 0.0f, 0.0f, 0.0f, 1.0f }; // RGBA spiegelnd
		gl.glLightfv(lichtnummer, GL2.GL_POSITION, position, 0);
		gl.glLightfv(lichtnummer, GL2.GL_AMBIENT, ambi, 0);
		gl.glLightfv(lichtnummer, GL2.GL_DIFFUSE, diff, 0);
		gl.glLightfv(lichtnummer, GL2.GL_SPECULAR, spec, 0);

		gl.glLighti(lichtnummer, GL2.GL_CONSTANT_ATTENUATION, 1);
		gl.glLighti(lichtnummer, GL2.GL_LINEAR_ATTENUATION, 0);
		gl.glLighti(lichtnummer, GL2.GL_QUADRATIC_ATTENUATION, 0);
	}

	private void LichtquelleStandard(float[] position, int lichtnummer)
	{
		gl.glEnable(lichtnummer);
		// float lpos[] = { 10.0f, 10.0f, 10.0f, 1.0f}; // Koordinaten
		float ambi[] = { 0.5f, 0.5f, 0.5f, 1.0f }; // RGBA ambient
		float diff[] = { 0.5f, 0.5f, 0.5f, 1.0f }; // RGBA diffus
		float spec[] = { 0.5f, 0.5f, 0.5f, 1.0f }; // RGBA spiegelnd
		gl.glLightfv(lichtnummer, GL2.GL_POSITION, position, 0);
		gl.glLightfv(lichtnummer, GL2.GL_AMBIENT, ambi, 0);
		gl.glLightfv(lichtnummer, GL2.GL_DIFFUSE, diff, 0);
		gl.glLightfv(lichtnummer, GL2.GL_SPECULAR, spec, 0);

		gl.glLighti(lichtnummer, GL2.GL_CONSTANT_ATTENUATION, 1);
		gl.glLighti(lichtnummer, GL2.GL_LINEAR_ATTENUATION, 0);
		gl.glLighti(lichtnummer, GL2.GL_QUADRATIC_ATTENUATION, 0);
	}

	private void lichtAn()
	{
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glShadeModel(GL2.GL_SMOOTH); // Gouraud Shading verwenden.
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, 1);
		// Eine Lichtquelle definieren (mehrere Lichtquellen sind moegl.glich).
		// LichtquelleSpitzlicht(new float[] {0,1,5,1},GL2.GL_LIGHT0);
		// LichtquelleWeichesLicht(new float[] {0,5,-1,1},GL2.GL_LIGHT1);
		LichtquelleStandard(new float[] { 0, 5, -1, 0 }, GL2.GL_LIGHT1);
		// Lichtquelle(new float[] {0,1,-5},GL2.GL_LIGHT0);
	}

	private void setup()
	{
		lichtAn();

		if (typ == 3)
			gl.glClearColor(0.36f, 0.36f, 0.36f, 1);
		else
			gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
	}

	public void init(GLAutoDrawable drawable)
	{
		glDrawable = drawable;
		GL2 gltemp = drawable.getGL().getGL().getGL2();
		this.gl = gltemp;
//		gl = GLPipelineFactory.create("javax.media.opengl.Debug", null, gltemp, null).getGL2();
		glu = new GLU();
		drawable.setGL(gltemp);
		setup();
	}

	// private void showNURBSKurve(int skalierung) {
	// gl.glPushMatrix();
	// gl.glScaled(skalierung,skalierung,skalierung);
	// gl.glBegin(GL2.GL_LINES);
	// final int anzahl = 100;
	// DeBoorPunkt3d punkt = Szene.testKurve.auswerten_skaliert(0);
	// DeBoorPunkt3d alterPunkt;
	// for(int i=1;i<=anzahl;i++)
	// {
	// alterPunkt = punkt;
	// punkt = Szene.testKurve.auswerten_skaliert((double)i/anzahl-0.001);
	// gl.glVertex3d(alterPunkt.getX(),alterPunkt.getY(),alterPunkt.getZ());
	// gl.glVertex3d(punkt.getX(),punkt.getY(),punkt.getZ());
	// }
	//
	// gl.glEnd();
	// //gl.glScaled(1/skalierung,1/skalierung,1/skalierung);
	// gl.glPopMatrix();
	//
	// }

	// private void showNURBSFlaechePunkte(int skalierung) {
	// gl.glPushMatrix();
	// gl.glScaled(skalierung,skalierung,skalierung);
	// gl.glBegin(GL2.GL_POINTS);
	// for(int i=0;i<Szene.testPunkte.length;i++)
	// for(int j=0;j<Szene.testPunkte[0].length;j++)
	// {
	// gl.glVertex3d(Szene.testPunkte[i][j].x,Szene.testPunkte[i][j].y,Szene.testPunkte[i][j].z);
	// }
	//
	// gl.glEnd();
	// //gl.glScaled(1/skalierung,1/skalierung,1/skalierung);
	// gl.glPopMatrix();
	//
	// }

	// private void showNURBSFlaechePolygone(int skalierung) {
	// gl.glPushMatrix();
	// gl.glScaled(skalierung,skalierung,skalierung);
	// gl.glBegin(GL2.GL_QUADS);
	// for(int i=0;i<Szene.testPunkte.length-1;i++)
	// for(int j=0;j<Szene.testPunkte[0].length-1;j++)
	// {
	// gl.glVertex3d(Szene.testPunkte[i][j].x,Szene.testPunkte[i][j].y,Szene.testPunkte[i][j].z);
	// gl.glVertex3d(Szene.testPunkte[i+1][j].x,Szene.testPunkte[i+1][j].y,Szene.testPunkte[i+1][j].z);
	// gl.glVertex3d(Szene.testPunkte[i+1][j+1].x,Szene.testPunkte[i+1][j+1].y,Szene.testPunkte[i+1][j+1].z);
	// gl.glVertex3d(Szene.testPunkte[i][j+1].x,Szene.testPunkte[i][j+1].y,Szene.testPunkte[i][j+1].z);
	// }
	//
	// gl.glEnd();
	// //gl.glScaled(1/skalierung,1/skalierung,1/skalierung);
	// gl.glPopMatrix();
	//
	// }

	// private void showNURBSFlaechePolygoneDreiecke(int skalierung) {
	// gl.glPushMatrix();
	// gl.glScaled(skalierung,skalierung,skalierung);
	// gl.glBegin(GL2.GL_TRIANGLES);
	// for(int i=0;i<Szene.testPunkte.length-1;i++)
	// for(int j=0;j<Szene.testPunkte[0].length-1;j++)
	// {
	// gl.glVertex3d(Szene.testPunkte[i][j].x,Szene.testPunkte[i][j].y,Szene.testPunkte[i][j].z);
	// gl.glVertex3d(Szene.testPunkte[i+1][j].x,Szene.testPunkte[i+1][j].y,Szene.testPunkte[i+1][j].z);
	// gl.glVertex3d(Szene.testPunkte[i+1][j+1].x,Szene.testPunkte[i+1][j+1].y,Szene.testPunkte[i+1][j+1].z);
	// //gl.glVertex3d(Szene.testPunkte[i][j+1].x,Szene.testPunkte[i][j+1].y,Szene.testPunkte[i][j+1].z);
	// }
	//
	// gl.glEnd();
	// //gl.glScaled(1/skalierung,1/skalierung,1/skalierung);
	// gl.glPopMatrix();
	//
	// }

	// private void showNURBSFlaecheKontrollpunkte(int skalierung)
	// {
	//
	// gl.glPushMatrix();
	// gl.glColor3f(1f,1f,1f);
	// gl.glScaled(skalierung,skalierung,skalierung);
	// gl.glBegin(GL2.GL_QUADS);
	// for(int i=0;i<Szene.testFlaeche.kontrollnetz.length;i++)
	// for(int j=0;j<Szene.testFlaeche.kontrollnetz[0].length;j++)
	// {
	// gl.glVertex3d(Szene.testFlaeche.kontrollnetz[i][j].getX(),Szene.testFlaeche.kontrollnetz[i][j].getY(),Szene.testFlaeche.kontrollnetz[i][j].getZ());
	// gl.glVertex3d(Szene.testFlaeche.kontrollnetz[i][j].getX()+0.05,Szene.testFlaeche.kontrollnetz[i][j].getY(),Szene.testFlaeche.kontrollnetz[i][j].getZ());
	// gl.glVertex3d(Szene.testFlaeche.kontrollnetz[i][j].getX()+0.05,Szene.testFlaeche.kontrollnetz[i][j].getY()+0.05,Szene.testFlaeche.kontrollnetz[i][j].getZ());
	// gl.glVertex3d(Szene.testFlaeche.kontrollnetz[i][j].getX(),Szene.testFlaeche.kontrollnetz[i][j].getY()+0.05,Szene.testFlaeche.kontrollnetz[i][j].getZ());
	// }
	//
	// gl.glEnd();
	// //gl.glScaled(1/skalierung,1/skalierung,1/skalierung);
	// gl.glPopMatrix();
	//
	// }

	/*
	 * prints out the contents of the selection array.
	 */
	private void processHits(int hits, int buffer[])
	{
		int names, ptr = 0;
		System.out.println("-----------------------------");
		System.out.println("hits = " + hits);
		// ptr = (GLuint *) buffer;
		for (int i = 0; i < hits; i++) { /* for each hit */
			names = buffer[ptr];
			System.out.println(" number of names for hit = " + names);
			ptr++;
			System.out.println("  z1 is " + buffer[ptr]);
			ptr++;
			System.out.println(" z2 is " + buffer[ptr]);
			ptr++;
			System.out.print("\n   the name is ");
			for (int j = 0; j < names; j++) { /* for each name */
				System.out.println("" + buffer[ptr]);
				ptr++;
			}
		}
		System.out.println("-----------------------------");
	}

	/**
	 * Liefert den Namen (den int - Wert), den das erste gepickte Objekt besitzt
	 */
	public int getFirstPickedName(int buffer[])
	{
		return buffer[3];
	}

	public void setAusgewaehlt(int name)
	{
		int kategorie = (name / 1000) * 1000;
		switch (kategorie) {
		case PICKING_NURBSFLAECHEN_OFFSET:
		{
			Szene.setAusgewaehltesObjekt(Szene.nurbsFlaechen.get(name - kategorie));
			break;
		}
		case PICKING_NURBSKURVEN_OFFSET:
		{
			Szene.setAusgewaehltesObjekt(Szene.nurbsKurven.get(name - kategorie));
			break;
		}
		case PICKING_NURBS_KONTROLLPUNKTE_OFFSET:
		{
			NURBSFlaeche nurbsFlaeche = Szene.ausgewaehlteNURBSFlaeche.nurbsFlaeche;
			int nummer = name - kategorie;
			int h = nurbsFlaeche.kontrollnetz.length;
			// System.out.println(nummer);
			// System.out.println(h);
			Szene.setAusgewaehltesObjekt(nurbsFlaeche.kontrollnetz[nummer / h][nummer
			                                                                   % h]);
			break;
		}
		case PICKING_KURVE_KONTROLLPUNKTE_OFFSET:
		{
			NURBSKurve nurbsKurve = Szene.ausgewaehlteNURBSKurve.nurbsKurve;
			int nummer = name - kategorie;
			Szene.setAusgewaehltesObjekt(nurbsKurve.kontrollpolygon[nummer]);
			break;
		}

		case PICKING_LICHTQUELLEN_OFFSET:
		{
			Szene.setAusgewaehltesObjekt(Szene.lichtquellen.get(name - kategorie));
			break;
		}
		}
	}

	/**
	 * @param picking
	 *          false - normal zeichnen, true - picking mode
	 */
	public void display(GLAutoDrawable drawable)
	{
		viewUpdate();
		int[] hitArray = null;
		IntBuffer hitBuffer = null;
		if (picking) {
			hitArray = new int[BUFFSIZE];
			hitBuffer = Buffers.newDirectIntBuffer(BUFFSIZE);

			gl.glSelectBuffer(BUFFSIZE, hitBuffer);
			gl.glRenderMode(GL2.GL_SELECT);

			gl.glInitNames();
			gl.glPushName(-1);
		} else {
			gl.glRenderMode(GL2.GL_RENDER);
		}

		// int numberOfHits = gl.glRenderMode(GL2.GL_RENDER);
		// System.out.println("Selection Hits: "+numberOfHits);

		// altes jogl : gl.glEnable(GL2.GL_MULTISAMPLE_ARB);
		gl.glEnable(GL2.GL_MULTISAMPLE);
		if (viewUpdated) {
			viewUpdate();
			viewUpdated = false;
		}
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glColor3f(1.0f, 0.0f, 0.0f);

		setViewPos();
		// showNURBSFlaechePolygone(5);
		// showNURBSFlaecheKontrollpunkte(5);

		showNeueNURBSKurve();
		showNURBSKurven();
		showNURBS();
		showLichtquellen();
		if (!picking)
			showGitter();
		// showWuerfel();

		// showNURBSFlaechePunkte(5);

		// showNURBSKurve(5);

		/*
		 * gl.glBegin( GL2.GL_TRIANGLES); gl.glVertex3f( 0.0f, 0.0f, 0.0f );
		 * gl.glVertex3f( 1.0f, 0.0f, 0.0f ); gl.glVertex3f( 1.0f, 1.0f, 0.0f );
		 * gl.glEnd();
		 */

		gl.glFlush();
		if (picking) {
			// int numberOfHits = gl.glRenderMode(GL2.GL_RENDER);
			hitBuffer.get(hitArray);
			// processHits(numberOfHits, hitArray);
			setAusgewaehlt(getFirstPickedName(hitArray));
			// System.out.println("Hits: "+numberOfHits);
			picking = false;
		}

	}

	private void showNeueNURBSKurve()
	{
		gl.glColor3f(1f, 1f, 0f);
		for (DeBoorPunkt3d p : Szene.neueNURBSKurve)
			showWuerfel(new Vector3d(p.x, p.y, p.z), new Vector3d(0.1, 0.1, 0.1));
	}

	private void showNURBSKurveKontrollpunkte(Kurve3D kurve)
	{
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glColor3f(1f, 1f, 0f);
		for (int i = 0; i < kurve.nurbsKurve.kontrollpolygon.length; i++) {
			DeBoorPunkt3d p = kurve.nurbsKurve.kontrollpolygon[i];
			if (Szene.ausgewaehlterTyp == Szene.KONTROLLPUNKT_AUSGEWAEHLT
					&& Szene.ausgewaehlterKontrollpunkt == p)
				gl.glColor3f(1, 1, 0);
			else
				gl.glColor3f(1, 1, 1);

			gl.glLoadName(PICKING_KURVE_KONTROLLPUNKTE_OFFSET + i);
			showWuerfel(new Vector3d(p.x, p.y, p.z), new Vector3d(0.1, 0.1, 0.1));
		}
		gl.glLoadName(-1);
		gl.glEnable(GL2.GL_LIGHTING);
	}

	private void showNURBSKurven()
	{
		gl.glColor3f(1f, 1f, 0f);
		Kurve3D[] kurven = Szene.nurbsKurven.toArray(new Kurve3D[0]);
		for (int i = 0; i < kurven.length; i++) {
			if (kurven[i] == Szene.ausgewaehlteNURBSKurve
					&& (Szene.ausgewaehlterTyp == Szene.NURBSKURVE_AUSGEWAEHLT || Szene.ausgewaehlterTyp == Szene.KONTROLLPUNKT_AUSGEWAEHLT)) {
				if (Szene.bearbeitungsModus == Szene.NURBS_MODUS)
					showNURBSKurveKontrollpunkte(kurven[i]);
			}

			if (Szene.ausgewaehlterTyp == Szene.NURBSKURVE_AUSGEWAEHLT
					&& kurven[i] == Szene.ausgewaehlteNURBSKurve)
				gl.glColor3f(1f, 1f, 0f);
			else
				gl.glColor3f(1f, 1f, 1f);
			gl.glLoadName(i + PICKING_NURBSKURVEN_OFFSET);
			showLinien(kurven[i].getDrahtgitter());
		}
		gl.glLoadName(-1);
	}

	private void setViewPos()
	{

		gl.glLoadIdentity();

		gl.glMatrixMode(GL2.GL_MODELVIEW);

		switch (typ) {
		case OpenGLFrame.XY_EBENE:
		{

			glu.gluLookAt(Szene.kamera.posXY.x, Szene.kamera.posXY.y, 10,
					Szene.kamera.posXY.x, Szene.kamera.posXY.y, 0, 0, 1, 0);
			gl.glRotated(Szene.kamera.winkelXY * 180 / Math.PI, 0, 0, 1);
			break;
		}
		case OpenGLFrame.XZ_EBENE:
		{
			glu.gluLookAt(Szene.kamera.posXZ.x, 10, Szene.kamera.posXZ.z,
					Szene.kamera.posXZ.x, 0, Szene.kamera.posXZ.z, 0, 0, -1);
			gl.glRotated(Szene.kamera.winkelXZ * 180 / Math.PI, 0, 1, 0);
			break;
		}
		case OpenGLFrame.YZ_EBENE:
		{
			glu.gluLookAt(10, Szene.kamera.posYZ.y, Szene.kamera.posYZ.z, 0,
					Szene.kamera.posYZ.y, Szene.kamera.posYZ.z, 0, 1, 0);
			gl.glRotated(Szene.kamera.winkelYZ * 180 / Math.PI, 1, 0, 0);
			break;
		}
		case OpenGLFrame.PERSPEKTIVE:
		{
			glu.gluLookAt(Szene.kamera.position.x, Szene.kamera.position.y,
					Szene.kamera.position.z, Szene.kamera.ziel.x, Szene.kamera.ziel.y,
					Szene.kamera.ziel.z, Szene.kamera.oben.x, Szene.kamera.oben.y,
					Szene.kamera.oben.z);
			break;
		}
		}

	}

	private void showPolygon(Polygon3D polygon)
	{
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glNormal3d(polygon.normale.x, polygon.normale.y, polygon.normale.z);

		if (polygon.punktNormalen != null && polygon.punktNormalen[0] != null)
			gl.glNormal3d(polygon.punktNormalen[0].x, polygon.punktNormalen[0].y,
					polygon.punktNormalen[0].z);
		gl.glVertex3d(polygon.punkte[0].x, polygon.punkte[0].y, polygon.punkte[0].z);

		if (polygon.punktNormalen != null && polygon.punktNormalen[1] != null)
			gl.glNormal3d(polygon.punktNormalen[1].x, polygon.punktNormalen[1].y,
					polygon.punktNormalen[1].z);
		gl.glVertex3d(polygon.punkte[1].x, polygon.punkte[1].y, polygon.punkte[1].z);

		if (polygon.punktNormalen != null && polygon.punktNormalen[2] != null)
			gl.glNormal3d(polygon.punktNormalen[2].x, polygon.punktNormalen[2].y,
					polygon.punktNormalen[2].z);
		gl.glVertex3d(polygon.punkte[2].x, polygon.punkte[2].y, polygon.punkte[2].z);
		gl.glEnd();
	}

	private void showPolygone(Polygon3D[] polygone)
	{
		for (Polygon3D polygon : polygone)
			showPolygon(polygon);
	}

	/**
	 * Wie showPolygon aber ohne GLBegin/End und ohne normale
	 */
	private void showPolygonOnly(Polygon3D polygon)
	{
		gl.glVertex3d(polygon.punkte[0].x, polygon.punkte[0].y, polygon.punkte[0].z);
		gl.glVertex3d(polygon.punkte[1].x, polygon.punkte[1].y, polygon.punkte[1].z);
		gl.glVertex3d(polygon.punkte[2].x, polygon.punkte[2].y, polygon.punkte[2].z);
	}

	private void showLinie(Linie3D linie)
	{
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3d(linie.punkt1.x, linie.punkt1.y, linie.punkt1.z);
		gl.glVertex3d(linie.punkt2.x, linie.punkt2.y, linie.punkt2.z);
		gl.glEnd();
	}

	private void showLinien(Linie3D[] linien)
	{
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glBegin(GL2.GL_LINES);
		for (Linie3D linie : linien) {
			gl.glVertex3d(linie.punkt1.x, linie.punkt1.y, linie.punkt1.z);
			gl.glVertex3d(linie.punkt2.x, linie.punkt2.y, linie.punkt2.z);
		}
		gl.glEnd();
		gl.glEnable(GL2.GL_LIGHTING);
	}

	private void showNURBS()
	{

		NURBS3D[] nurbs = (NURBS3D[]) Szene.nurbsFlaechen.toArray(new NURBS3D[0]);
		// System.out.println("Anzahl NURBS - Flächen: "+nurbs.length);
		for (int i = 0; i < nurbs.length; i++) {
			// if(Szene.ausgewaehlterTyp)
			gl.glLoadName(i + PICKING_NURBSFLAECHEN_OFFSET);
			gl.glPushMatrix();
			gl.glTranslated(nurbs[i].position.x, nurbs[i].position.y,
					nurbs[i].position.z);
			gl.glScaled(nurbs[i].skalierung.x, nurbs[i].skalierung.y,
					nurbs[i].skalierung.z);
			gl.glMultMatrixd(VektorMethoden.MatrixToArray(nurbs[i].rotationsMatrix),
					0);
			gl.glColor3f(0f, 0f, 0f);

			if (!(picking && Szene.bearbeitungsModus == Szene.NURBS_MODUS)) {
				setMaterial(nurbs[i].material);
				// setMaterial setzt bis jetzt nur die specular farbeigenschaften, daher
				// hier nochmal farbe setzten
				if (nurbs[i].material != null) {
					// System.out.println(nurbs[i].material.ambient);
					// System.out.println(nurbs[i].material.diffus);
					// System.out.println(nurbs[i].material.spiegelnd);
					gl.glColor3f(
							(float) (nurbs[i].material.ambient.getRed() + nurbs[i].material.diffus
									.getRed()) / 512,
									(float) (nurbs[i].material.ambient.getGreen() + nurbs[i].material.diffus
											.getGreen()) / 512,
											(float) (nurbs[i].material.ambient.getBlue() + nurbs[i].material.diffus
													.getBlue()) / 512);
				} else
					gl.glColor3d(0.5, 0.5, 0.0);
				showPolygone(nurbs[i].getTriangulierung());
			}

			gl.glLoadName(-1);
			if (nurbs[i] == Szene.ausgewaehlteNURBSFlaeche
					&& (Szene.ausgewaehlterTyp == Szene.NURBSFLAECHE_AUSGEWAEHLT || Szene.ausgewaehlterTyp == Szene.KONTROLLPUNKT_AUSGEWAEHLT)) {
				gl.glColor3f(1f, 1f, 0f);
				showLinien(nurbs[i].getDrahtgitter());
				if (Szene.bearbeitungsModus == Szene.NURBS_MODUS)
					showNURBSKontrollpunkte(nurbs[i]);
			}

			gl.glPopMatrix();
		}

	}

	private void showLichtquellen()
	{
		gl.glDisable(GL2.GL_LIGHTING);
		Lichtquelle[] lichtquellen = Szene.lichtquellen.toArray(new Lichtquelle[0]);

		Lichtquelle lichtquelle;
		for (int i = 0; i < lichtquellen.length; i++) {
			gl.glLoadName(i + PICKING_LICHTQUELLEN_OFFSET);
			lichtquelle = lichtquellen[i];
			if (Szene.ausgewaehlterTyp == Szene.LICHTQUELLE_AUSGEWAEHLT
					&& Szene.ausgewaehlteLichtquelle == lichtquelle)
				gl.glColor3d(1, 1, 0);
			else
				gl.glColor3d(1, 1, 1);
			gl.glPushMatrix();
			gl.glTranslated(lichtquelle.position.x, lichtquelle.position.y,
					lichtquelle.position.z);
			gl.glScaled(0.3, 0.3, 0.3);
			glu.gluSphere(glu.gluNewQuadric(), 1, 10, 20);
			gl.glPopMatrix();
		}
		gl.glLoadName(-1);
		gl.glEnable(GL2.GL_LIGHTING);
	}

	private void showGitter()
	{
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glColor3f(0f, 0f, 0f);
		gl.glBegin(GL2.GL_LINES);

		switch (typ) {
		// Gitter in XY Ebene
		case 0:
		{
			for (int i = -20; i <= 20; i++) {
				gl.glVertex3f((float) -20, i, 0f);
				gl.glVertex3f((float) 20, i, 0f);
			}
			for (int i = -20; i <= 20; i++) {
				gl.glVertex3f((float) i, -20f, 0);
				gl.glVertex3f((float) i, 20f, 0);
			}
			break;
		}

		case 3:
			// gl.glColor3f(.8f, .8f, 1f );
		case 1:
		{

			// Gitter in XZ Ebene
			for (int i = -20; i <= 20; i++) {
				if (i != 0) {

					gl.glVertex3f((float) i, 0f, 20f);
					gl.glVertex3f((float) i, 0f, -20f);
				}
			}
			for (int i = -20; i <= 20; i++) {
				if (i != 0) {
					gl.glVertex3f((float) -20, 0f, i);
					gl.glVertex3f((float) 20, 0f, i);
				}
			}
			break;
		}

		case 2:

		{

			// Gitter in YZ Ebene
			for (int i = -20; i <= 20; i++) {
				gl.glVertex3f((float) 0, i, -20f);
				gl.glVertex3f((float) 0, i, 20f);
			}
			for (int i = -20; i <= 20; i++) {
				gl.glVertex3f((float) 0, -20f, i);
				gl.glVertex3f((float) 0, 20f, i);
			}
		}
		}
		gl.glEnd();

		// Koordinatenachsen
		gl.glColor3d(1, 0, 0);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(-10, 0, 0);
		gl.glVertex3f(10, 0, 0);
		gl.glVertex3f(0, -10, 0);
		gl.glVertex3f(0, 10, 0);
		gl.glVertex3f(0, 0, -10);
		gl.glVertex3f(0, 0, 10);

		gl.glEnd();
		gl.glEnable(GL2.GL_LIGHTING);
	}

	public void setViewUpdated()
	{
		viewUpdated = true;
	}

	private void viewUpdate()
	{
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		if (picking) {
			int viewport[] = new int[4];
			gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);

			glu.gluPickMatrix((double) mausPosition.x,
					(double) (viewport[3] - mausPosition.y), //
					3.0, 3.0, viewport, 0);
		}

		switch (typ) {
		case 0:
		{
			gl.glOrtho(-Szene.kamera.breiteXY / 2, Szene.kamera.breiteXY / 2,
					-Szene.kamera.breiteXY / 2 * height / width, Szene.kamera.breiteXY
					/ 2 * height / width, -1000, 1000);
			break;
		}
		case 1:
		{
			gl.glOrtho(-Szene.kamera.breiteXZ / 2, Szene.kamera.breiteXZ / 2,
					-Szene.kamera.breiteXZ / 2 * height / width, Szene.kamera.breiteXZ
					/ 2 * height / width, -1000, 1000);
			break;
		}
		case 2:
		{
			gl.glOrtho(-Szene.kamera.breiteYZ / 2, Szene.kamera.breiteYZ / 2,
					-Szene.kamera.breiteYZ / 2 * height / width, Szene.kamera.breiteYZ
					/ 2 * height / width, -1000, 1000);
			break;
		}

		case 3:
		{
			glu.gluPerspective(Szene.kamera.fovy, (double) (width) / height,
					Szene.kamera.clippingNear, Szene.kamera.clippingFar);
			break;
		}
		}
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height)
	{
		// GLU glu = drawable.getGLU();
		this.width = width;
		this.height = height;
		viewUpdated = true;
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged)
	{
		// in JOGL wohl noch nicht implementiert
	}

	/**
	 * Zeichnet einen W�rfel der von -1/-1/-1 bis +1/+1/+1
	 */
	private void showWuerfel(Vector3d position, Vector3d skalierung)
	{
		gl.glPushMatrix();
		gl.glTranslated(position.x, position.y, position.z);
		gl.glScaled(skalierung.x, skalierung.y, skalierung.z);
		gl.glBegin(GL2.GL_QUADS);
		// Vorne
		// gl.glColor3f(1.0f, 0.0f, 0.0f );
		gl.glVertex3f(-1.0f, -1.0f, 1f);
		gl.glVertex3f(-1.0f, 1.0f, 1f);
		gl.glVertex3f(1.0f, 1.0f, 1f);
		gl.glVertex3f(1.0f, -1.0f, 1f);
		// Hinten
		// gl.glColor3f(1.0f, 0.0f, 0.0f );
		gl.glVertex3f(-1.0f, -1.0f, -1f);
		gl.glVertex3f(-1.0f, 1.0f, -1f);
		gl.glVertex3f(1.0f, 1.0f, -1f);
		gl.glVertex3f(1.0f, -1.0f, -1f);
		// Oben
		// gl.glColor3f(0.0f, 0.0f, 1.0f );
		gl.glVertex3f(-1.0f, 1.0f, 1f);
		gl.glVertex3f(-1.0f, 1.0f, -1f);
		gl.glVertex3f(1.0f, 1.0f, -1f);
		gl.glVertex3f(1.0f, 1.0f, 1f);
		// Unten
		// gl.glColor3f(0.0f, 0.0f, 1.0f );
		gl.glVertex3f(-1.0f, -1.0f, 1f);
		gl.glVertex3f(-1.0f, -1.0f, -1f);
		gl.glVertex3f(1.0f, -1.0f, -1f);
		gl.glVertex3f(1.0f, -1.0f, 1f);
		// Links
		// gl.glColor3f(0.0f, 1.0f, 0.0f );
		gl.glVertex3f(-1.0f, -1.0f, -1f);
		gl.glVertex3f(-1.0f, 1.0f, -1f);
		gl.glVertex3f(-1.0f, 1.0f, 1f);
		gl.glVertex3f(-1.0f, -1.0f, 1f);
		// Rechts
		// gl.glColor3f(0.0f, 1.0f, 0.0f );
		gl.glVertex3f(1.0f, -1.0f, -1f);
		gl.glVertex3f(1.0f, 1.0f, -1f);
		gl.glVertex3f(1.0f, 1.0f, 1f);
		gl.glVertex3f(1.0f, -1.0f, 1f);
		// Ende
		gl.glEnd();
		gl.glPopMatrix();
	}

	private void showNURBSKontrollpunkte(NURBS3D nurbs)
	{
		gl.glDisable(GL2.GL_LIGHTING);
		for (int i = 0; i < nurbs.nurbsFlaeche.kontrollnetz.length; i++)
			for (int j = 0; j < nurbs.nurbsFlaeche.kontrollnetz[0].length; j++) {
				if (Szene.ausgewaehlterTyp == Szene.KONTROLLPUNKT_AUSGEWAEHLT
						&& Szene.ausgewaehlterKontrollpunkt == nurbs.nurbsFlaeche.kontrollnetz[i][j])
					gl.glColor3f(1f, 1f, 0f);
				else
					gl.glColor3f(1f, 1f, 1f);
				gl.glLoadName(i * nurbs.nurbsFlaeche.kontrollnetz.length + j
						+ PICKING_NURBS_KONTROLLPUNKTE_OFFSET);
				showWuerfel(new Vector3d(nurbs.nurbsFlaeche.kontrollnetz[i][j].x,
						nurbs.nurbsFlaeche.kontrollnetz[i][j].y,
						nurbs.nurbsFlaeche.kontrollnetz[i][j].z), new Vector3d(0.1, 0.1,
								0.1));
			}
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glLoadName(-1);
	}

	@Override
	public void dispose(GLAutoDrawable drawable)
	{
		System.err.println("TODO: implement Renderer.dispose()");		
	}

}