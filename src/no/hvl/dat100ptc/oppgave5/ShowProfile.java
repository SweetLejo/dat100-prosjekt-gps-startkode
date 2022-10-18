package no.hvl.dat100ptc.oppgave5;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

import javax.swing.JOptionPane;

public class ShowProfile extends EasyGraphics {

	private static final int MARGIN = 50;  // margin on the sides 
	
	private static int MAXBARHEIGHT = 500; // assume no height above 500 meters
	
	private GPSPoint[] gpspoints;

	public ShowProfile() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		GPSComputer gpscomputer =  new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		int N = gpspoints.length; // number of data points

		makeWindow("Height profile",2 * MARGIN + 3 * N, 3 * MARGIN + MAXBARHEIGHT);

		// top margin + height of drawing area
		showHeightProfile(MARGIN + MAXBARHEIGHT); 
	}

	public void showHeightProfile(int ybase) {

		// ybase indicates the position on the y-axis where the columns should start
	
		int x = MARGIN,y;
<<<<<<< HEAD

		String timeStr =      "Time        : ";
		int currenttime = drawString(timeStr,MARGIN,MARGIN);
=======
>>>>>>> refs/heads/master
		
		int timescale = Integer.parseInt(getText("Tidskalering: "));
<<<<<<< HEAD
		
		for (int i = 0; i < gpspoints.length; i++) {
			setVisible(currenttime, false);
			setColor(0,0,0);
			timeStr =      "Time        : " + GPSUtils.formatTime(gpspoints[i].getTime());
			currenttime = drawString(timeStr,MARGIN,MARGIN);
			
			setColor(0,0,255);
			int elevation = (int)gpspoints[i].getElevation();
			if (elevation>0) 
				drawLine(x,ybase + MARGIN,x,elevation + MARGIN);
=======

		setColor(0,0,255);
>>>>>>> refs/heads/master

<<<<<<< HEAD
			x = x + 2;
=======
		for (int i = 0; i < gpspoints.length; i++) {
			int elevation = (int)gpspoints[i].getElevation();
>>>>>>> refs/heads/master
			
<<<<<<< HEAD
			if (i < gpspoints.length - 1) {
				int pausetime = (gpspoints[i + 1].getTime() - gpspoints[i].getTime()) * 1000 / timescale;
				pause(pausetime);	
			}	
=======
			if (elevation>0) 
				drawLine(x,ybase,x,elevation);
			x++;
			
			if (i < gpspoints.length - 1) {
				int pausetime = (gpspoints[i + 1].getTime() - gpspoints[i].getTime()) * 10 / timescale;
				pause(pausetime);
			}
>>>>>>> refs/heads/master
		}
<<<<<<< HEAD
=======
	
>>>>>>> refs/heads/master
	}
}
