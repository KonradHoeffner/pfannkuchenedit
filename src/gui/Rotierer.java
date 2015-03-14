package gui;

import szene.Kamera;


/** Created on 31.05.2006 */

public class Rotierer extends Thread {

	Kamera kamera;
	int count = 10000; 
	
	Rotierer(Kamera kamera)
	{
		this.kamera=kamera;
	}

	public void run()
	{
		for(int i=1;i<=count;i++)
		{
				try {
					sleep(50);
				} catch (InterruptedException e) {}	
				//kamera.position = rotierePunkt(kamera.position,kamera.ziel,new Vector3d(0,1,0),Math.PI/90);
				//kamera.oben = rotierePunkt(kamera.oben,new Vector3d(0,0,0),new Vector3d(0,1,0),Math.PI/90);
				//kamera.position.z+=-1f;				
		}
	}
}
