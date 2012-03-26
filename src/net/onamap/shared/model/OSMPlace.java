package net.onamap.shared.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Id;

public class OSMPlace implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long osm_id;
	
	@Embedded
	private OSMAddress address;

	private Double lat;
	private Double lon;
	private Long place_id;
	
	public OSMPlace() {
		
	}
	

	public static class OSMAddress implements Serializable {
		/**
		 * Default Serialization UID
		 */
		private static final long serialVersionUID = 1L;
		private String city;
		private String country;
		private String state;
		
		public OSMAddress() {
			
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
	}

	public Long getOsm_id() {
		return osm_id;
	}

	public void setOsm_id(Long osm_id) {
		this.osm_id = osm_id;
	}

	public OSMAddress getAddress() {
		return address;
	}

	public void setAddress(OSMAddress address) {
		this.address = address;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Long getPlace_id() {
		return place_id;
	}

	public void setPlace_id(Long place_id) {
		this.place_id = place_id;
	}
	
	@Override
	public int hashCode() {
		return osm_id.intValue();
	}

}
