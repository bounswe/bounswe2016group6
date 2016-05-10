/**
 * 
 * Object representation of a river entity, which is returned from SPARQL query.
 * @author Erhan Çaðýrýcý
 *
 */

public class River implements Comparable<River> {
	/**
	 * Name of the river
	 */
		public String name ;
		/**
		 * Latitude of the river
		 */
		public double latitude;
		/**
		 * Longtitude of the river
		 */
		public double longtitude;
		/**
		 * Length of the river
		 */
		public int length;
		/**
		 * The river that user has entered as a query.
		 */
		public static River referenceRiver;
		public River(String nm, double lat, double lon,int len ) {
			name = nm;
			latitude = lat;
			longtitude = lon;
			length = len;
		}
		
		/**
		 * Calculates the distance between this river and the given one.
		 * For the calculation haversine formula is used.
		 * @param r1 The river to be compared with
		 * @return The distance between the rivers
		 */
		public double haversineDistance(River r1){
			
			int R = 6371; // Radius of the earth in km
			double dLat = (r1.latitude-this.latitude) * (Math.PI/180); 
			double dLon = (r1.longtitude-this.longtitude) *(Math.PI/180) ; 
			double a = 
					Math.sin(dLat/2) * Math.sin(dLat/2) +
					Math.cos(r1.latitude * (Math.PI/180) ) * Math.cos(this.latitude * (Math.PI/180)) * 
					Math.sin(dLon/2) * Math.sin(dLon/2)
					; 
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
			double d = R * c; // Distance in km
			return d;
		}
		
		
		/**
		 * If the given river name includes the other, it is concluded to be same rivers.
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof River) {
				River r = (River)obj;
				return this.name.toLowerCase().contains(r.name.toLowerCase())
						| r.name.toLowerCase().contains(this.name.toLowerCase());
			}
			return false;
		}
		
		
		/**
		 * Compares two river's  haversine distance with the user-input river.
		 * This constitutes the semantic relation criterion between the rivers.
		 * If a river has a smaller distance to reference river, it is more relevant than the other.
		 */
		@Override
		public int compareTo(River o) {
			double dist = this.haversineDistance(referenceRiver);
			double dist2 = o.haversineDistance(referenceRiver);
			
			if(dist>dist2){
				return 1;
			} else if (dist<dist2){
				return -1;
			}
			return 0;
		}
	}