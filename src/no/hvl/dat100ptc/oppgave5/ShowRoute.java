package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowRoute extends EasyGraphics {

	private static int MARGIN = 50;
	private static int MAPXSIZE = 800;
	private static int MAPYSIZE = 800;

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;
	
	public ShowRoute() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(MARGIN + MAPYSIZE);
		
		showStatistics();
	}

	// antall x-pixels per lengdegrad
	public double xstep() {

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		double xstep = MAPXSIZE / (Math.abs(maxlon - minlon)); 

		return xstep;
	}

	// antall y-pixels per breddegrad
	public double ystep() {
		
		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		
		double ystep = MAPYSIZE / (Math.abs(maxlat - minlat));
		
		return ystep;	
	}

	public void showRouteMap(int ybase) {
		
		int size = ((int)xstep() * (int)ystep()) / 2; 
		
		setColor(0,255,0);
		
		for (int i = 0; i < gpspoints.length; i++) {
			int lon1 = ((int)xstep() * (int)gpspoints[i-1].getLongitude()) + MARGIN;
			int lat1 = ((int)ystep() * (int)gpspoints[i-1].getLatitude()) + ybase;		
			int lon2 = ((int)xstep() * (int)gpspoints[i].getLongitude()) + MARGIN;
			int lat2 = ((int)ystep() * (int)gpspoints[i].getLatitude()) + ybase;	
				
			drawLine(lon1,lat1,lon2,lat2);
		}
		
		setColor(0,0,255);
		
		int lon = ((int)xstep() * (int)gpspoints[0].getLongitude()) + MARGIN;
		int lat = ((int)ystep() * (int)gpspoints[0].getLatitude()) + ybase;	
		
		int pos = fillCircle(lon,lat,size);
		
		for (int i = 1; i < gpspoints.length; i++) {
			lon = ((int)xstep() * (int)gpspoints[i].getLongitude()) + MARGIN;
			lat = ((int)ystep() * (int)gpspoints[i].getLatitude()) + ybase;	
			
			moveCircle(pos,lon,lat);
		}
	}

	public void showStatistics() {

		int TEXTDISTANCE = 20;

		setColor(0,0,0);
		setFont("Courier",12);
		
		
		double weight = 80.00;
		
		String timeStr =      "Total time     : " + GPSUtils.formatTime(gpscomputer.totalTime());; 
		String distanceStr =  "Total distance : " + GPSUtils.formatDouble(gpscomputer.totalDistance()) + " km";
		String elevationStr = "Total elevation: " + GPSUtils.formatDouble(gpscomputer.totalElevation()) + " m";
		String maxspeedStr =  "Max speed      : " + GPSUtils.formatDouble(gpscomputer.maxSpeed()) + " km/t";
		String avgspeedStr =  "Average speed  : " + GPSUtils.formatDouble(gpscomputer.averageSpeed()) + "kmt/t";
		String energyStr =    "Energy         : " + GPSUtils.formatDouble(gpscomputer.totalKcal(weight)) + "kcal";		
		
		drawString(timeStr,MARGIN,MAPYSIZE - TEXTDISTANCE);
		drawString(distanceStr,MARGIN,MAPYSIZE - (TEXTDISTANCE * 2));
		drawString(elevationStr,MARGIN,MAPYSIZE - (TEXTDISTANCE * 3));
		drawString(maxspeedStr,MARGIN,MAPYSIZE - (TEXTDISTANCE * 4));
		drawString(avgspeedStr,MARGIN,MAPYSIZE - (TEXTDISTANCE * 5));
		drawString(energyStr,MARGIN,MAPYSIZE - (TEXTDISTANCE * 6));
		
	}

}
