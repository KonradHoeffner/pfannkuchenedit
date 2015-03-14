/** Created on 02.07.2006 */
package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtensionFilter extends FileFilter {
	
	String extension;
	
	/*
	 * Get the extension of a file.
	 */  
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		
		if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}
	public ExtensionFilter(String extension)
	{
		this.extension = extension;
	}
	public boolean accept(File f) {
		String extension = getExtension(f);
		if(extension==null) return false;
		if(extension.equals(this.extension)) return true;
		return false;
	}
	
	public String getDescription() {
		return "Dateien des Typs *."+extension+".";
	}
	
}
