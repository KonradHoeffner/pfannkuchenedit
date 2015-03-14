package gui;
import javax.swing.*;

import szene.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Used by InternalFrameDemo.java. */
public class MaterialFrame extends JInternalFrame implements ActionListener{
	
	static int openFrameCount = 0;
	JTextField[][] farbTextFelder = new JTextField[3][4];
	JTextField glanzTextFeld = new JTextField(2);
	Material selectedMaterial=null;
	
	JComboBox comboBox = new JComboBox();	
	
	
	private void setEditable(boolean editable)
	{
		for(int i=0;i<3;i++)
			for(int j=0;j<4;j++) farbTextFelder[i][j].setEnabled(editable);
		glanzTextFeld.setEnabled(editable);
	}
	
	private void setSelectedMaterial(String materialName)
	{
		selectedMaterial = Szene.materialien.get(materialName);
		farbTextFelder[0][0].setText(String.valueOf(selectedMaterial.ambient.getRed()));
		farbTextFelder[0][1].setText(String.valueOf(selectedMaterial.ambient.getGreen()));			
		farbTextFelder[0][2].setText(String.valueOf(selectedMaterial.ambient.getBlue()));
		farbTextFelder[0][3].setText(String.valueOf(selectedMaterial.ambient.getAlpha()));

		farbTextFelder[1][0].setText(String.valueOf(selectedMaterial.diffus.getRed()));
		farbTextFelder[1][1].setText(String.valueOf(selectedMaterial.diffus.getGreen()));			
		farbTextFelder[1][2].setText(String.valueOf(selectedMaterial.diffus.getBlue()));
		farbTextFelder[1][3].setText(String.valueOf(selectedMaterial.diffus.getAlpha()));		
		
		farbTextFelder[2][0].setText(String.valueOf(selectedMaterial.spiegelnd.getRed()));
		farbTextFelder[2][1].setText(String.valueOf(selectedMaterial.spiegelnd.getGreen()));			
		farbTextFelder[2][2].setText(String.valueOf(selectedMaterial.spiegelnd.getBlue()));
		farbTextFelder[2][3].setText(String.valueOf(selectedMaterial.spiegelnd.getAlpha()));
		glanzTextFeld.setText(String.valueOf(selectedMaterial.glanz));		
	}
	
	private void neuesMaterial()
	{
		Material material = new Material();
		String standardName = "Neues Material";
		String materialName;
		int count = 1;
		while(Szene.materialien.containsKey(standardName+" "+String.valueOf(count))) count++;
		materialName = standardName+" "+String.valueOf(count);
		material.name=materialName;
		Szene.materialien.put(materialName,material);
		setSelectedMaterial(materialName);
		comboBox.addItem(materialName);
		comboBox.setSelectedItem(materialName);
	}
	
	public void update()
	{
		comboBox.removeAllItems();
		
		for(String s:Szene.materialien.keySet()) {comboBox.addItem(s);}
		if(Szene.materialien.keySet().size()!=0) this.setEditable(true);else this.setEditable(false);
	}
	
	private void materialLoeschen(String materialName)
	{
		comboBox.removeItem(materialName);
		Szene.materialLoeschen(materialName);
	}
	
	public MaterialFrame() {
		super("Materialien", 
				false, //resizable
				false, //closable
				false, //maximizable
				true);//iconifiable
		
		//...Create the GUI and put it in the window...
		//Color c;
		//Hauptpanel erzeugen : von oben nach unten
		JPanel hauptPanel = new JPanel();
		hauptPanel.setLayout(new BoxLayout(hauptPanel,BoxLayout.PAGE_AXIS));
		
		setContentPane(hauptPanel);
		
		// Combo Box		
		//comboBox.setEditable(true);
		//comboBox.add()
		comboBox.addActionListener(this);
		hauptPanel.add(comboBox);
		// Buttonpanel
		JPanel buttonPanel1 = new JPanel(new FlowLayout());
		JPanel buttonPanel2 = new JPanel(new FlowLayout());
		hauptPanel.add(buttonPanel1);
		hauptPanel.add(buttonPanel2);		
		// Buttons
		// Neues Material
		JButton neuButton = new JButton("Neu");
		buttonPanel1.add(neuButton);
		neuButton.addActionListener(this);
		neuButton.setActionCommand("neuesmaterial");
		// Material zu Objekt zuweisen
		JButton zuweisenButton = new JButton("Zuweisen");
		buttonPanel1.add(zuweisenButton);
		zuweisenButton.addActionListener(this);
		zuweisenButton.setActionCommand("materialzuweisen");
		// Material l�schen
		JButton loeschenButton = new JButton("L�schen");
		buttonPanel2.add(loeschenButton);
		loeschenButton.addActionListener(this);
		loeschenButton.setActionCommand("materialloeschen");
		// Material umbenennen
		JButton umbenennenButton = new JButton("Umbenennen");
		buttonPanel2.add(umbenennenButton);
		umbenennenButton.addActionListener(this);
		umbenennenButton.setActionCommand("materialumbenennen");
		
		// Eigenschaftspanels für die Eingabe der Ambient,Diffus, Spiegelnd und Glanz - Werte
		JPanel[] eigenschaftsPanels = new JPanel[4];
		String[] eigenschaftsNamen = {"Ambient     ","Diffus       ","Spiegelnd","Glanzwert   "};
		JLabel[] eigenschaftsLabels = new JLabel[4];
		String[] farbNamen = {"R","G","B","A"}; 
		JLabel[] farbLabels= new JLabel[4];
		// Actionlistener für Glanztextfeld
		glanzTextFeld.addActionListener(this);
		glanzTextFeld.setActionCommand("glanztextfeld");
		for(int i=0;i<4;i++)
		{
			eigenschaftsPanels[i] = new JPanel(new FlowLayout());
			eigenschaftsLabels[i] = new JLabel(eigenschaftsNamen[i]);
			
			
			hauptPanel.add(eigenschaftsPanels[i]);
			eigenschaftsPanels[i].add(eigenschaftsLabels[i]);
			
			if(i<3) for(int j=0;j<4;j++)
			{
				farbLabels[j] = new JLabel(farbNamen[j]);
				farbTextFelder[i][j] = new JTextField(3);
				// Action Listener hinzufügen, ActionCommand ist "farbtextfeld#i#j"
				// #i und #j bezeichnen Position im Array
				farbTextFelder[i][j].addActionListener(this);
				farbTextFelder[i][j].setActionCommand("farbtextfeld"+Integer.toString(i)+Integer.toString(j));
				eigenschaftsPanels[i].add(farbLabels[j]);
				eigenschaftsPanels[i].add(farbTextFelder[i][j]);
			} else
			{
				eigenschaftsPanels[i].add(glanzTextFeld);
			}
		}
		
		//konstante Fenstergr��e (im Gegensatz zu den OpenGLFrames
		setSize(300,250);
		setVisible(true);
		setEditable(false);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if(e.getActionCommand()=="neuesmaterial")
		{
			neuesMaterial();
			setEditable(true);
			return;
		}
		if(e.getActionCommand()=="materialzuweisen")
		{
			System.out.println("Material " +comboBox.getSelectedItem() + " zugewiesen");
			if(Szene.ausgewaehlteNURBSFlaeche==null) JOptionPane.showInternalMessageDialog(this,"Kein Objekt ausgew�hlt.");
			else Szene.ausgewaehlteNURBSFlaeche.material=Szene.materialien.get(comboBox.getSelectedItem()); 
			return;
		}
		if(e.getActionCommand()=="materialloeschen")
		{
			String materialName = (String)comboBox.getSelectedItem();
			if(materialName!=null)
			{
				materialLoeschen(materialName);				
				String neuesMaterial = (String)comboBox.getSelectedItem(); 
				if(neuesMaterial!=null)
				{
					setSelectedMaterial(neuesMaterial);
					System.out.println("Material "+materialName+" gel�scht");
				}
				else setEditable(false);
			}
			return;
		}
		if(e.getActionCommand()=="materialumbenennen")
		{
			String alterName = (String)comboBox.getSelectedItem();
			if(alterName==null) return;
			// gew�nschten neuen Namen vom Benutzer einfordern
			String neuerName = (String)JOptionPane.showInputDialog(
					this,
					"Neuer Name für " + alterName + ":",
					"Material umbenennen",
					JOptionPane.QUESTION_MESSAGE);
			// Ist der neue Name valide?
			if(neuerName.equals(alterName)) return;
			if(Szene.materialien.containsKey(neuerName))
			{
				JOptionPane.showMessageDialog(this,"Materialname bereits vergeben.");
				return;
			}
			// Alles ok, es darf umbenannt werden
			Szene.materialUmbenennen(alterName,neuerName);
			comboBox.removeItem(alterName);
			comboBox.addItem(neuerName);
			comboBox.setSelectedItem(neuerName);
			return;
		}
		
		if(e.getActionCommand().contains("farbtextfeld"))
		{
			Material material = Szene.materialien.get(comboBox.getSelectedItem());
			
			try 
			{
				if(e.getActionCommand().contains("farbtextfeld0")) material.ambient
				= new Color(Integer.parseInt(farbTextFelder[0][0].getText()),Integer.parseInt(farbTextFelder[0][1].getText()),Integer.parseInt(farbTextFelder[0][2].getText()),Integer.parseInt(farbTextFelder[0][3].getText()));
				if(e.getActionCommand().contains("farbtextfeld1")) material.diffus
				= new Color(Integer.parseInt(farbTextFelder[1][0].getText()),Integer.parseInt(farbTextFelder[1][1].getText()),Integer.parseInt(farbTextFelder[1][2].getText()),Integer.parseInt(farbTextFelder[1][3].getText()));
				if(e.getActionCommand().contains("farbtextfeld2")) material.spiegelnd
				= new Color(Integer.parseInt(farbTextFelder[2][0].getText()),Integer.parseInt(farbTextFelder[2][1].getText()),Integer.parseInt(farbTextFelder[2][2].getText()),Integer.parseInt(farbTextFelder[2][3].getText()));

			}
			catch(Exception ex) // falls Bl�dsinn eingegeben wird, alten Wert anzeigen
			{
				setSelectedMaterial((String)comboBox.getSelectedItem());
			}
		}
		
		if(e.getActionCommand()=="glanztextfeld")
		{
			Material material = Szene.materialien.get(comboBox.getSelectedItem());
			// Falls Buchstaben statt Zahlen eingegeben werden auf alten Wert setzen 
			try
			{
				byte b = Byte.parseByte(glanzTextFeld.getText());
				if(b<0) throw new Exception();
				material.glanz = b;
			}
			catch(Exception ex) {/* keine Fehlerbehandlung nötig, try reicht schon*/} 
			finally
			{
				// alten Wert anzeigen
				glanzTextFeld.setText(String.valueOf(material.glanz));
			}
			return;
		}
		
		
		if(e.getActionCommand()=="comboBoxChanged")
		{
			if(comboBox.getSelectedItem()!=null) setSelectedMaterial((String)comboBox.getSelectedItem());
			return;
		}
		
	}
}