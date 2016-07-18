package map;

import Objects.Object_Location;


public class TerrainList {
	private Object_Location første, siste;

	  public TerrainList()
	  {
	    første = null;
	  }

	 public void settInn( Object_Location ny )
	  {
	    if ( første == null ) {
	    	første = siste = ny;
	    }
	    else
	    {
	    	siste.neste = ny;
	    	siste = ny;
	    }
	  }
	 
	 public int GetCoordX(int E)
	 {
		 Object_Location objekt = første;

		   while ( objekt != null )
		   {
			   int ID = objekt.GetID();
			   if (ID == E) {
				   return objekt.GetCoordX();
			   }
		     objekt = objekt.neste;
		   }
		   return -10;
	 }
	 
	 public void SendCoordX(int E, int A) {
		 Object_Location objekt = første;

		   while ( objekt != null )
		   {
			   int ID = objekt.GetID();
			   if (ID == E) {
				   objekt.SendCoordX(A);
			   }
		     objekt = objekt.neste;
		   }
	 }
	 
	 public int GetCoordY(int E)
	 {
		 Object_Location objekt = første;

		   while ( objekt != null )
		   {
			   int ID = objekt.GetID();
			   if (ID == E) {
				   return objekt.GetCoordY();
			   }
		     objekt = objekt.neste;
		   }
		   return -10;
	 }
	 
	 public void SendCoordY(int E, int B) {
		 Object_Location objekt = første;

		   while ( objekt != null )
		   {
			   int ID = objekt.GetID();
			   if (ID == E) {
				   objekt.SendCoordY(B);
			   }
		     objekt = objekt.neste;
		   }
	 }
	 
	 public String GetFargen(int E)
	 {
		 Object_Location objekt = første;

		   while ( objekt != null )
		   {
			   int ID = objekt.GetID();
			   if (ID == E) {
				   return objekt.GetFargen();
			   }
		     objekt = objekt.neste;
		   }
		   return "white";
	 }
	 
	 public void SendFargen(int E, String F) {
		 Object_Location objekt = første;

		   while ( objekt != null )
		   {
			   int ID = objekt.GetID();
			   if (ID == E) {
				   objekt.SendFargen(F);
			   }
		     objekt = objekt.neste;
		   }
	 }
	 
	 public int GetTemp(int E)
	 {
		 Object_Location objekt = første;

		   while ( objekt != null )
		   {
			   int ID = objekt.GetID();
			   if (ID == E) {
				   return objekt.GetTemp();
			   }
		     objekt = objekt.neste;
		   }
		   return 0;
	 }
	 
	 public void SendTemp( int LocID, int LocTemp ) {
		 Object_Location objekt = første;

		   while ( objekt != null ){
			   int ID = objekt.GetID();
			   if ( ID == LocID ) {
				   objekt.SendTemp( LocTemp );
			   }
		     objekt = objekt.neste;
		   }
	 }
}
