package Control;

import Objects.Object_DNA;
import Objects.Object_Location;
import Objects.Object_Unit;

public class control_Map {

	Controller Class_Controller;
	//Object_Unit[][] Locations = new Object_Unit[100][100];
	Object_Location[] LocationUnits = new Object_Location[100];
	
	public control_Map( Controller Class_Controller ) {
		System.out.println ( "control_Map " );
		this.Class_Controller = Class_Controller;
	}

	public void ProcessTick() {
		//System.out.println ( "control_Map ProcessTick" );
		for ( int X = 0 ; X < 100 ; X++ ) {
			ProcessLocation( LocationUnits[X] );
		}
	}
	
	private void ProcessLocation( Object_Location Location ) {
		if ( Location != null ) {
			Object_Unit[] UnitList = Location.Get_UnitList();
			for ( int Y = 0 ; Y < UnitList.length ; Y++ ) {
				if ( UnitList[Y] != null ) {
					if ( UnitList[Y].Get_Dead() == true ) {
						UnitList[Y] = UnitList[Y+1];
						UnitList[Y+1] = null;
					} else {
						ProcessUnit(  UnitList[Y], Location );
					}
				}
			}
			Location.SetUnitList( UnitList );
		}	
	}
	
	private void ProcessUnit( Object_Unit Unit, Object_Location Location ) {
		//System.out.println ( "control_Map ProcessUnit Unit="+Unit);
		boolean Processed = SearchNeighbors( Unit, Location );
		if ( Processed == false ) {
			Movement( Unit, Location );
		}
	}
	
	private boolean SearchNeighbors( Object_Unit Interactor, Object_Location Location ) {
		//System.out.println ( "control_Map SearchNeighbors Interactor="+Interactor);
		Object_Unit[] UnitList = Location.Get_UnitList();
		for ( int Y = 0 ; Y < UnitList.length ; Y++ ) {
			if ( UnitList[Y] != null ) {
				if ( UnitList[Y].equals( Interactor ) ) {
					
				} else {
					return InteractUnits( Interactor, UnitList[Y] );
				}
			}
		}
		return false;
	}
	
	private boolean InteractUnits( Object_Unit Interactor, Object_Unit Interacted ) {
		//System.out.println ( "control_Map InteractUnits Interactor="+Interactor+" Interactor.Get_Male()="+Interactor.Get_Male());
		if ( Interactor.Get_Male() == true ) {
			if ( Interacted.Get_Male() == true ) { //fight?
				return true;
			} else { //breed?
				Interacted.Breed( Interactor );
				return true;
			}
		} else { //do nothing
			
		}
		return false;
	}
	
	private void Movement( Object_Unit Unit, Object_Location Location ) { //Location=der unit befinner seg atm
		//System.out.println ( "control_Map Movement x="+x+" y="+y+" "+Unit );
		int DirectionX = RandomInt( -10, 10-(-10) );
		int DirectionY = RandomInt( -10, 10-(-10) );
		Object_Location NewLoc = FindLocationByCoords( (DirectionX+Location.GetCoordX()), (DirectionY+Location.GetCoordY()) );
		while ( NewLoc == null ) {
			DirectionX = RandomInt( -10, 10-(-10) );
			DirectionY = RandomInt( -10, 10-(-10) );
			NewLoc = FindLocationByCoords( (DirectionX+Location.GetCoordX()), (DirectionY+Location.GetCoordY()) );
		}
		//System.out.println ( "control_Map DirectionX="+DirectionX+" DirectionY="+DirectionY+" (DirectionX+Location.GetCoordX())="+(DirectionX+Location.GetCoordX())+" (DirectionY+Location.GetCoordY())="+(DirectionY+Location.GetCoordY()) );
		Location.SendAwayUnit( Unit );
		NewLoc.ReceiveUnit( Unit );
		if ( NewLoc.equals( Location )) {
			
		} else {
			Object_Location Check = FindLocation( Unit );
			if ( Check == null ) {
				System.out.println("- " + new Throwable().fillInStackTrace().getStackTrace()[0]+") <- " + new Throwable().fillInStackTrace().getStackTrace()[1]+") <- " + new Throwable().fillInStackTrace().getStackTrace()[2]+") Burde aldri havne her");
			}
		}
	}
	
	private int RandomInt( int Min, int Max ) {
		double RngD = Math.random();
		double RngD2 = RngD * Max;
		int Rng = Round( RngD2 );
		int Rng2 = (Rng + Min);
		int Rng3 = Round(Rng2/10.0);
		//System.out.println ( "control_Map RandomInt Rng="+RngD+" "+RngD2+" "+Rng+" "+Rng2+" "+Rng3 );
		return Rng3;
	}
	
	private int Round ( double X ) {
		int Multi = 0;
		if ( X > 1 ) {
			Multi = (int) (X / 1);
		} else if ( X < -1 ) {
			Multi = (int) (X / 1);
		}
		//System.out.println ( "control_Map Round X="+X+" Multi="+Multi );
		if ( X > 0.5 ) {
			return Multi+1;
		} else if ( X < -0.5 ) {
			return Multi+(-1);
		} else {
			return Multi;
		}
	}
	
	private Object_Location FindLocationByCoords( int X, int Y ) {
		//System.out.println ( "control_Map FindLocationByCoords Searching for X="+X+" Y="+Y );
		for ( int x = 0 ; x < 100 ; x++ ) {
			if ( LocationUnits[x] != null ) {
				if ( LocationUnits[x].GetCoordX() == X ) {
					if ( LocationUnits[x].GetCoordY() == Y ) {
						return LocationUnits[x];
					}
				}
			}
		}
		//System.out.println ( "control_Map FindLocationByCoords error X="+X+" Y="+Y );
		return null;
	}
	
	public void PlaceNewUnit( Object_Unit newUnit ) {
		//System.out.println ( "control_Map PlaceNewUnit "+newUnit );
		Object_Unit Mother = newUnit.Get_Mother();
		if ( Mother != null ) {
			Movement( newUnit, FindLocation(Mother) );
		} else {
			Movement( newUnit, FindLocationByCoords( 5, 5 ) );
		}
	}

	private Object_Location FindLocation( Object_Unit Unit ) {
		for ( int X = 0 ; X < LocationUnits.length ; X++ ) {
			//System.out.println ( "control_Map FindLocation Unit="+Unit+" X="+LocationUnits[X].GetCoordX()+" Y="+LocationUnits[X].GetCoordY()+" X="+X );
			boolean Found = LocationUnits[X].FindUnit( Unit );
			if ( Found == true ) {
				return LocationUnits[X];
			}
		}
		return null;
	}

	public void LagdNyLoc( Object_Location ny2 ) {
		for ( int X = 0 ; X < LocationUnits.length ; X++ ) {
			if ( LocationUnits[X] == null ) {
				LocationUnits[X] = ny2;
				//System.out.println( "Control_Map LagdNyLoc X="+LocationUnits[X].GetCoordX()+" Y="+LocationUnits[X].GetCoordY() );
				break;
			}
		}
	}

	public int GetLocTemp( int z ) {
		for ( int X = 0 ; X < LocationUnits.length ; X++ ) {
			if ( LocationUnits[X] != null ) {
				if ( LocationUnits[X].GetID() == z ) {
					return LocationUnits[X].GetTemp();
				}
			} else {
				return 0;
			}
		}
		return 0;
	}

	public void SendTemp(int z, int i) {
		for ( int X = 0 ; X < LocationUnits.length ; X++ ) {
			if ( LocationUnits[X] != null ) {
				if ( LocationUnits[X].GetID() == z ) {
					LocationUnits[X].SendTemp(i);
				}
			} else {
				break;
			}
		}
	}

	public int GetLocCoordY(int x) {
		for ( int X = 0 ; X < LocationUnits.length ; X++ ) {
			if ( LocationUnits[X] != null ) {
				if ( LocationUnits[X].GetID() == x ) {
					return LocationUnits[X].GetCoordY();
				}
			} else {
				return 0;
			}
		}
		return 0;
	}

	public int GetLocCoordX(int x) {
		for ( int X = 0 ; X < LocationUnits.length ; X++ ) {
			if ( LocationUnits[X] != null ) {
				if ( LocationUnits[X].GetID() == x ) {
					return LocationUnits[X].GetCoordX();
				}
			} else {
				return 0;
			}
		}
		return 0;
	}

	public int GetUnitsLocated( int x ) {
		int Teller = 0;
		Object_Unit[] UnitList = LocationUnits[x].Get_UnitList();
		for ( int Y = 0 ; Y < UnitList.length ; Y++ ) {
			if ( UnitList[Y] != null ) {
				Teller ++;
			} else {
				//System.out.println( "cotnrol_Map GetUnitsLocated Teller="+Teller+" location#"+x);
				return Teller;
			}
		}
		return 0;
	}
}
