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
		
		showStatistics();

	}
	
	public void bikeRoute() {
		
		int timescale = 10 * Integer.parseInt(getText("Tidskalering: "));
			
		int ybase = ROUTEMAPYSIZE + HEIGHTSIZE + SPACE;
		
		int x = MARGIN + TEXTWIDTH;
		
		//løkke som tegner ruten med linjer.
		for (int i = 0; i < gpspoints.length - 1; i++) {
			double lon1 = MARGIN + (xstep() * (gpspoints[i].getLongitude() - GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints))));
			double lon2 = MARGIN + (xstep() * (gpspoints[i + 1].getLongitude() - GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints))));
			double lat1 = ybase - (ystep() * (gpspoints[i].getLatitude() - GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints))));	
			double lat2 = ybase - (ystep() * (gpspoints[i + 1].getLatitude() - GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints))));
			
			double ele1 = gpspoints[i].getElevation();
			double ele2 = gpspoints[i + 1].getElevation();
			
			if (ele2 > ele1)
				setColor(255,0,0);
			else
				setColor(0,255,0);
		
			drawLine((int)lon1,(int)lat1,(int)lon2,(int)lat2);
		}
		
		setColor(0,0,255);
		setFont("Courier",12);
		int currentpoint = fillCircle((int)gpspoints[0].getLongitude(),(int)gpspoints[0].getLatitude(), 4);
		
		//løkke som viser ruten i skalert realtid
		for (int i = 1; i < gpspoints.length; i++) {
			String timeStr =      "Time        : " + GPSUtils.formatTime(gpspoints[i].getTime());
			String speedStr =     "Speed       : " + GPSUtils.formatDouble(GPSUtils.speed(gpspoints[i - 1], gpspoints[i]));
			
			setColor(0,0,0);
			int currenttime = drawString(timeStr,MARGIN,MARGIN);
			int currentspeed = drawString(speedStr,MARGIN, MARGIN * 2);
			
			double lon = MARGIN + (xstep() * (gpspoints[i].getLongitude() - GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints))));
			double lat = ybase - (ystep() * (gpspoints[i].getLatitude() - GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints))));
			
			moveCircle(currentpoint,(int)lon,(int)lat);
			
			setColor(0,0,255);
			int elevation = (int)gpspoints[i].getElevation();
			if (elevation>0) 
				drawLine(x,HEIGHTSIZE + SPACE - MARGIN,x,elevation + MARGIN);

			x = x + 2;
			
			int pausetime = (gpspoints[i].getTime() - gpspoints[i - 1].getTime()) * 1000 / timescale;
			pause(pausetime);	
			
			setVisible(currenttime, false);
			setVisible(currentspeed, false);
		}
	}

	public void showStatistics() {

		int TEXTDISTANCE = 20;

		setColor(0,0,0);
		setFont("Courier",12);
		
		double weight = 80.00;
		
		String timeStr =      "Total time        : " + GPSUtils.formatTime(gpscomp.totalTime());
		String distanceStr =  "Total distance    : " + GPSUtils.formatDouble(gpscomp.totalDistance()) + " km";
		String elevationStr = "Total elevation   : " + GPSUtils.formatDouble(gpscomp.totalElevation()) + " m";
		String maxspeedStr =  "Max speed         : " + GPSUtils.formatDouble(gpscomp.maxSpeed()) + " km/t";
		String avgspeedStr =  "Average speed     : " + GPSUtils.formatDouble(gpscomp.averageSpeed()) + " kmt/t";
		String energyStr =    "Energy            : " + GPSUtils.formatDouble(gpscomp.totalKcal(weight)) + " kcal";		
		
		drawString(timeStr,MARGIN,TEXTDISTANCE);
		drawString(distanceStr,MARGIN,TEXTDISTANCE * 2);
		drawString(elevationStr,MARGIN,TEXTDISTANCE * 3);
		drawString(maxspeedStr,MARGIN,TEXTDISTANCE * 4);
		drawString(avgspeedStr,MARGIN,TEXTDISTANCE * 5);
		drawString(energyStr,MARGIN,TEXTDISTANCE * 6);
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
