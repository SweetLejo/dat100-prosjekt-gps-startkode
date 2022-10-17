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
		
		ybase -= GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		
		setColor(0,255,0);
		
		for (GPSPoint i : gpspoints) {
			double lon = (xstep() * (i.getLongitude() - gpspoints[0].getLongitude())) + MARGIN;
			double lat = (ystep() * (i.getLatitude() - gpspoints[0].getLatitude())) - MARGIN + ybase;
			
			fillCircle((int)lon,(int)lat, 4);
			pause(10);
		}
		
	}

	public void showStatistics() {

		int TEXTDISTANCE = 20;

		setColor(0,0,0);
		setFont("Courier",12);
		
		double weight = 80.00;
		
		String timeStr =      "Total time        : " + GPSUtils.formatTime(gpscomputer.totalTime());
		String distanceStr =  "Total distance    : " + GPSUtils.formatDouble(gpscomputer.totalDistance()) + " km";
		String elevationStr = "Total elevation   : " + GPSUtils.formatDouble(gpscomputer.totalElevation()) + " m";
		String maxspeedStr =  "Max speed         : " + GPSUtils.formatDouble(gpscomputer.maxSpeed()) + " km/t";
		String avgspeedStr =  "Average speed     : " + GPSUtils.formatDouble(gpscomputer.averageSpeed()) + " kmt/t";
		String energyStr =    "Energy            : " + GPSUtils.formatDouble(gpscomputer.totalKcal(weight)) + " kcal";		
		
		drawString(timeStr,MARGIN,TEXTDISTANCE);
		drawString(distanceStr,MARGIN,TEXTDISTANCE * 2);
		drawString(elevationStr,MARGIN,TEXTDISTANCE * 3);
		drawString(maxspeedStr,MARGIN,TEXTDISTANCE * 4);
		drawString(avgspeedStr,MARGIN,TEXTDISTANCE * 5);
		drawString(energyStr,MARGIN,TEXTDISTANCE * 6);
		
	}

}
