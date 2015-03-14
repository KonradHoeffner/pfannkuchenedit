package gui;
import java.util.*;
/** Created on 31.05.2006 */

public class MyAnimator extends Thread {
	
	boolean multimode = false;
	OpenGLFrame frame;
	Vector<OpenGLFrame> frames = new Vector<OpenGLFrame>();
	int sleeptime = 50;	
	
	MyAnimator(OpenGLFrame frame)
	{
		this.frame = frame;
	}

	MyAnimator(Vector<OpenGLFrame> frames)
	{
		multimode=true;
		this.frames = frames;
	}

	public synchronized void run()
	{
		while(true)
		{	
			//if(i%5==0) Szene.mainWindow.updateSzene();
			//Szene.testFlaeche.kontrollnetz[5][4].setZ((double)i/10);
			
			try {
					sleep(sleeptime);
				} catch (InterruptedException e) {
					// TODO Automatisch erstellter Catch-Block
					e.printStackTrace();
				}

			if(!multimode)
			{				
				//long davor = (new Date()).getTime();
				if(frame.isVisible()&&!(frame.isIcon())) frame.canvas.display();
				yield();
//				long danach = (new Date()).getTime();
//				long zwischendrin = (danach-davor);
//				System.out.println(zwischendrin);
//				sleeptime = (int)zwischendrin+20;
			}
			else
			{
				Iterator<OpenGLFrame> it = frames.iterator();
				while(it.hasNext()) 
					{
						frame = it.next();
						if (!frame.isIcon()) frame.canvas.display();
					
					}
			}
		}
	}

}
