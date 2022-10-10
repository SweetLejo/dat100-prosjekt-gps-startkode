package no.hvl.dat100ptc.oppgave2;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;

public class GPSData {

	private GPSPoint[] gpspoints;
	protected int antall = 0;

	public GPSData(int n) {

		this.gpspoints = new GPSPoint[n];
		this.antall = 0;
	}

	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}

	protected boolean insertGPS(GPSPoint gpspoint) {

		if(this.antall >= this.gpspoints.length){
			return false;
		}
		gpspoints[antall] = gpspoint;
		antall++;
		return true;
	}

	public boolean insert(String time, String latitude, String longitude, String elevation) {

		GPSPoint gpspoint;
		int timeInt = GPSDataConverter.toSeconds(time);
		double latitudeDouble = Double.parseDouble(latitude);
		double longitudeDouble = Double.parseDouble(longitude);
		double elevationDouble = Double.parseDouble(elevation);

		gpspoint = new GPSPoint(timeInt, latitudeDouble,longitudeDouble,elevationDouble);

		return insertGPS(gpspoint);
	}

	public void print() {

		System.out.println("====== Konvertert GPS Data - START ======");

		for(GPSPoint i : gpspoints){
			System.out.println(i.toString());
		}


		System.out.println("====== Konvertert GPS Data - SLUTT ======");

	}
}
