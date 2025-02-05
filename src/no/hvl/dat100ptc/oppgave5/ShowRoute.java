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
		
		int timescale = Integer.parseInt(getText("Tidskalering: "));
		
		setColor(0,255,0);
		
		//løkke som tegner ruten med linjer	
		for (int i = 0; i < gpspoints.length - 1; i++) {
			double lon1 = MARGIN + (xstep() * (gpspoints[i].getLongitude() - GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints))));
			double lon2 = MARGIN + (xstep() * (gpspoints[i + 1].getLongitude() - GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints))));
			double lat1 = ybase - (ystep() * (gpspoints[i].getLatitude() - GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints))));	
			double lat2 = ybase - (ystep() * (gpspoints[i + 1].getLatitude() - GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints))));
		
			drawLine((int)lon1,(int)lat1,(int)lon2,(int)lat2);
		}
		
		setColor(0,0,255);
		int currentpoint = fillCircle((int)gpspoints[0].getLongitude(),(int)gpspoints[0].getLatitude(), 4);
		
		//løkke tegner hvor man er på ruten
		for (int i = 0; i < gpspoints.length - 1; i++) {
			setColor(0,0,0);
			String timeStr =      "Time        : " + GPSUtils.formatTime(gpspoints[i].getTime());
			int currenttime = drawString(timeStr,MARGIN,MARGIN);
			
			double lon = MARGIN + (xstep() * (gpspoints[i].getLongitude() - GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints))));
			double lat = ybase - (ystep() * (gpspoints[i].getLatitude() - GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints))));
			
			moveCircle(currentpoint,(int)lon,(int)lat);
			
			int pausetime = (gpspoints[i + 1].getTime() - gpspoints[i].getTime()) * 1000 / timescale;
			pause(pausetime);	
			setVisible(currenttime, false);
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
