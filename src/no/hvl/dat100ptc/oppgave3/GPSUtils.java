package no.hvl.dat100ptc.oppgave3;

import static java.lang.Math.*;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;

import java.text.DecimalFormat;

public class GPSUtils {

	public static double findMax(double[] da) {

		double max; 
		
		max = da[0];
		
		for (double d : da) {
			max = max(max, d);
		}
		
		return max;
	}

	public static double findMin(double[] da) {

		double min = da[0];
		for(double itNum : da){
			min = min(min,itNum); // had ```min = itNum < min? itNum : min;``` at first but there is a built in math method
		}
		return min;
	}

	public static double[] getLatitudes(GPSPoint[] gpspoints) {

		double[] latitudeArr = new double[gpspoints.length];

		for(int i = 0; i < gpspoints.length; i++){
			latitudeArr[i] = gpspoints[i].getLatitude();
		}
		return latitudeArr;
	}

	public static double[] getLongitudes(GPSPoint[] gpspoints) {

		double[] longitudeArr = new double[gpspoints.length];

		for(int i = 0; i < gpspoints.length; i++){
			longitudeArr[i] = gpspoints[i].getLongitude();
		}
		return longitudeArr;
	}

	private static int R = 6371000; // jordens radius

	public static double distance(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		double d;
		double latitude1, longitude1, latitude2, longitude2;

		latitude1 = toRadians(gpspoint1.getLatitude());
		latitude2 = toRadians(gpspoint2.getLatitude());
		longitude1 = toRadians(gpspoint1.getLongitude());
		longitude2 = toRadians(gpspoint2.getLongitude());

		double dLat = abs(latitude1-latitude2);
		double dLon = abs(longitude1-longitude2);
		
		double a = pow(sin(dLat / 2), 2) +
                   pow(sin(dLon / 2), 2) *
                   cos(latitude1) *
                   cos(latitude2);
		double c = 2 * atan(sqrt(a));
		return R * c;


	}

	public static double speed(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		int secs;
		double speed;

		double displacement = distance(gpspoint1, gpspoint2);
		double dTime = abs(gpspoint2.getTime() - gpspoint1.getTime());
		double velocity = displacement / dTime;

		return velocity * 3.6;
	}

	public static String formatTime(int secs) {

		String timestr;
		String TIMESEP = ":";

		int hours = secs / 3600;
		int minutes = (secs % 3600) / 60;
		int sec = secs % 60;

		timestr = String.format("%2s%02d:%02d:%02d"," ", hours, minutes, sec);
		
		return timestr;
	}
	private static int TEXTWIDTH = 10;

	public static String formatDouble(double d) {


		String formattedDub = String.format("%.2f", d);
		TEXTWIDTH = TEXTWIDTH - formattedDub.length();
		return String.format("%" +TEXTWIDTH+"s" + formattedDub, " ");
	}
}
