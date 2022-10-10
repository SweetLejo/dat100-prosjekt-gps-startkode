package no.hvl.dat100ptc.oppgave6;

import javax.swing.JOptionPane;

import easygraphics.*;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class CycleComputer extends EasyGraphics {

	private static int SPACE = 10;
	private static int MARGIN = 20;
	
	// FIXME: take into account number of measurements / gps points
	private static int ROUTEMAPXSIZE = 800; 
	private static int ROUTEMAPYSIZE = 400;
	private static int HEIGHTSIZE = 200;
	private static int TEXTWIDTH = 200;

	private GPSComputer gpscomp;
	private GPSPoint[] gpspoints;
	
	private int N = 0;

	private double minlon, minlat, maxlon, maxlat;

	private double xstep, ystep;

	public CycleComputer() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");

		gpscomp = new GPSComputer(filename);
		gpspoints = gpscomp.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		N = gpspoints.length; // number of gps points

		minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));

		xstep = xstep();
		ystep = ystep();

		makeWindow("Cycle Computer", 
				2 * MARGIN + ROUTEMAPXSIZE,
				2 * MARGIN + ROUTEMAPYSIZE + HEIGHTSIZE + SPACE);

		bikeRoute();

	}

	
	public void bikeRoute() {
		
		int timescale = 10 * Integer.parseInt(getText("Tidskalering: "));
		int pausetime;
		
		int size = ((int)xstep() * (int)ystep()) / 2; 
		
		int ref = (int)gpspoints[0].getElevation();
		
		int x = MARGIN;
		
		for (int i = 0; i < gpspoints.length; i++) {		
			int lon = ((int)xstep() * (int)gpspoints[i].getLongitude()) + MARGIN;
			int lat = ((int)ystep() * (int)gpspoints[i].getLatitude()) + MARGIN;		

			int elevation = (int)gpspoints[i].getElevation();
			
			if (elevation < ref) 
				setColor(255,0,0);
			else 
				setColor(0,255,0);
			fillCircle(lon,lat,size);	
			ref = elevation;
			
			setColor(0,0,255);
			drawLine(x,ROUTEMAPYSIZE + SPACE,x,elevation);
			x++;
			
			String timeStr =  "Time           : " + GPSUtils.formatTime(gpspoints[i].getTime());		
			drawString(timeStr,MARGIN,ROUTEMAPYSIZE + HEIGHTSIZE - MARGIN);
			
			double[] speeds = gpscomp.speeds();
			String speedStr = "Speed          : " + GPSUtils.formatDouble(speeds[i]);
			drawString(speedStr,MARGIN,ROUTEMAPYSIZE + HEIGHTSIZE - (MARGIN*2));
			
			pausetime = (gpspoints[i + 1].getTime() - gpspoints[i].getTime()) / timescale;
			
			pause(pausetime);
		}
	}

	public double xstep() {

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		double xstep = ROUTEMAPXSIZE / (Math.abs(maxlon - minlon)); 

		return xstep;
	}

	public double ystep() {

		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		
		double ystep = ROUTEMAPYSIZE / (Math.abs(maxlat - minlat));
		
		return ystep;	
	}

}
