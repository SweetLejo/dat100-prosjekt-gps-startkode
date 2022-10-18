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
	private GPSComputer gpscomp2;
	private GPSPoint[] gpspoints2;
	
	private int N = 0;

	private double minlon, minlat, maxlon, maxlat;

	private double xstep, ystep;

	public CycleComputer() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");

		gpscomp = new GPSComputer(filename);
		gpspoints = gpscomp.getGPSPoints();

		filename = JOptionPane.showInputDialog("GPS data filnavn 2: ");
		
		gpscomp2 = new GPSComputer(filename);
		gpspoints2 = gpscomp.getGPSPoints();

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
				2 * MARGIN + ROUTEMAPYSIZE + HEIGHTSIZE + TEXTWIDTH + SPACE);

		bikeRoute();
		
		showStatistics();
		showStatistics();

	}
	
	public void bikeRoute() {
		
		int timescale = 10 * Integer.parseInt(getText("Tidskalering: "));
			
		int ybase = ROUTEMAPYSIZE + HEIGHTSIZE + SPACE + TEXTWIDTH;
		
		int x = MARGIN;
		
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
		int currentpoint2 = fillCircle((int)gpspoints2[0].getLongitude(),(int)gpspoints2[0].getLatitude(), 4);
		
		//løkke som viser ruten i skalert realtid
		for (int i = 1; i < gpspoints.length; i++) {
			String timeStr =      "Time        : " + GPSUtils.formatTime(gpspoints[i].getTime());
			String speedStr =     "Speed p1    : " + GPSUtils.formatDouble(GPSUtils.speed(gpspoints[i - 1], gpspoints[i]));
			String speedStr2 =    "Speed p2    : " + GPSUtils.formatDouble(GPSUtils.speed(gpspoints2[i - 1], gpspoints2[i]));
			
			setColor(0,0,0);
			int currenttime = drawString(timeStr,MARGIN,MARGIN);
			int currentspeed = drawString(speedStr,MARGIN, MARGIN * 2);
			int currentspeed2 = drawString(speedStr2,MARGIN, MARGIN * 3);
			
			//person 1
			double lon = MARGIN + (xstep() * (gpspoints[i].getLongitude() - GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints))));
			double lat = ybase - (ystep() * (gpspoints[i].getLatitude() - GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints))));
			
			moveCircle(currentpoint,(int)lon,(int)lat);
			
			setColor(0,0,255);
			int elevation = (int)gpspoints[i].getElevation();
			if (elevation>0) 
				drawLine(x,HEIGHTSIZE + SPACE - MARGIN + TEXTWIDTH,x,elevation + MARGIN + TEXTWIDTH);
			
			//person 2
			double lon2 = MARGIN + (xstep() * (gpspoints2[i].getLongitude() - GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints2))));
			double lat2 = ybase - (ystep() * (gpspoints2[i].getLatitude() - GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints2))));
			
			moveCircle(currentpoint2,(int)lon2,(int)lat2);
			
			setColor(0,0,255);
			int elevation2 = (int)gpspoints2[i].getElevation();
			if (elevation2>0) 
				drawLine(x + 400,HEIGHTSIZE + SPACE - MARGIN + TEXTWIDTH,x + 400,elevation + MARGIN + TEXTWIDTH);
			
			x = x + 2;
			
			int pausetime = (gpspoints[i].getTime() - gpspoints[i - 1].getTime()) * 1000 / timescale;
			pause(pausetime);	
			
			setVisible(currenttime, false);
			setVisible(currentspeed, false);
			setVisible(currentspeed2, false);
		}
	}
	
	public void showStatistics() {

		int TEXTDISTANCE = 20;

		setColor(0,0,0);
		setFont("Courier",12);
		
		double weight = 80.00;
		
		//person 1
		String timeStr =      "Total time p1     : " + GPSUtils.formatTime(gpscomp2.totalTime());
		String distanceStr =  "Total distance    : " + GPSUtils.formatDouble(gpscomp2.totalDistance()) + " km";
		String elevationStr = "Total elevation   : " + GPSUtils.formatDouble(gpscomp2.totalElevation()) + " m";
		String maxspeedStr =  "Max speed         : " + GPSUtils.formatDouble(gpscomp2.maxSpeed()) + " km/t";
		String avgspeedStr =  "Average speed     : " + GPSUtils.formatDouble(gpscomp2.averageSpeed()) + " kmt/t";
		String energyStr =    "Energy            : " + GPSUtils.formatDouble(gpscomp2.totalKcal(weight)) + " kcal";		
		
		drawString(timeStr,MARGIN,TEXTDISTANCE);
		drawString(distanceStr,MARGIN,TEXTDISTANCE * 2);
		drawString(elevationStr,MARGIN,TEXTDISTANCE * 3);
		drawString(maxspeedStr,MARGIN,TEXTDISTANCE * 4);
		drawString(avgspeedStr,MARGIN,TEXTDISTANCE * 5);
		drawString(energyStr,MARGIN,TEXTDISTANCE * 6);
		
		//person 2
		String timeStr2 =      "Total time p2     : " + GPSUtils.formatTime(gpscomp2.totalTime());
		String distanceStr2 =  "Total distance    : " + GPSUtils.formatDouble(gpscomp2.totalDistance()) + " km";
		String elevationStr2 = "Total elevation   : " + GPSUtils.formatDouble(gpscomp2.totalElevation()) + " m";
		String maxspeedStr2 =  "Max speed         : " + GPSUtils.formatDouble(gpscomp2.maxSpeed()) + " km/t";
		String avgspeedStr2 =  "Average speed     : " + GPSUtils.formatDouble(gpscomp2.averageSpeed()) + " kmt/t";
		String energyStr2 =    "Energy            : " + GPSUtils.formatDouble(gpscomp2.totalKcal(weight)) + " kcal";		
		
		drawString(timeStr2,MARGIN + 400,TEXTDISTANCE);
		drawString(distanceStr2,MARGIN + 400,TEXTDISTANCE * 2);
		drawString(elevationStr2,MARGIN + 400,TEXTDISTANCE * 3);
		drawString(maxspeedStr2,MARGIN + 400,TEXTDISTANCE * 4);
		drawString(avgspeedStr2,MARGIN + 400,TEXTDISTANCE * 5);
		drawString(energyStr2,MARGIN + 400,TEXTDISTANCE * 6);
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
