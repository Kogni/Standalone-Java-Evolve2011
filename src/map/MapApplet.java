package map;
import java.applet.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

import Control.Controller;
import Objects.Object_Location;
import easyIO.*;

public class MapApplet extends Applet implements Runnable, MouseMotionListener, MouseListener { 
	
	Controller Class_Controller;
	
	   int width, height;
	   int i = 0;
	   Thread t = null;
	   boolean threadSuspended;
	   In SquaresIn = new In();
	   In RowsIn = new In();
	   int Squares = 100;
	   
	   int Rows = 10;
	   int Collumns = Squares / Rows;
	   int[] SquareX = new int[Squares+1];
	   int[] SquareY = new int[Squares+1];
	   
	   String[] terrainColor = new String[6];
	   int[] terrainTemp = new int[6];
	   int[] temp = new int[Squares+1];
	   String[] squareColor = new String[Squares+1];
		
	   int curX, curY; 
	   int x = 0;
	   int X = 0;
	   int Y = 0;
	   int Z = 0;
	   int SpawnTimer = 0;
	   int CoordX = 0;
	   int CoordY = 0;
	   
	   int xpos; 
	   int ypos; 
	   int rect1xco,rect1yco,rect1width,rect1height; 
	   boolean mouseEntered; 
	   boolean rect1Clicked; 
		
	   //private TerrainList TerrainSquares;
	   int SquareSize = 20;
	   int NyX = 0;
	   int NyY = 0;
	   int colorpicker = -1;
	   int intervals = 6000/Squares;
	   
	   int SquareChange;
	   int NextSquare = 4;
	   /*int R = 0;
	   int G = 0;
	   int B = 0;*/
	   double R = 0;
	   double G = 0;
	   double B = 0;
	   double TempNyX = 0;
	   double TempNyY = 0;
	   
	   Color Color30 = new Color(255,0,0);
	   Color Color20 = new Color(255,255,0);
	   Color Color10 = new Color(0,255,0);
	   Color Color0 = new Color(255,255,255);
	   int GreenMin = 0;
	   int GreenMax = 20;
	   int RedMin = 20;
	   int RedMax = 30;
	   int BlueMax = -5;
	   int BlueMin = -10;
	   int WhiteMax = 0;
	   int WhiteMin = -5;
	   int RedDiff = 0;
	   int GreenDiff=0;
	   int BlueDiff=0;
	   double ToMax = 0;
	   Color Color_10 = new Color(0,0,255);
	   
	   Graphics bufferGraphics; 
	   Image offscreen; 
	   Dimension dim; 
	
	public MapApplet() {
		System.out.println( "MapApplet startup Squares="+Squares );
		
		Class_Controller = new Controller(); 
		
		for ( int x = 0; x<= Squares; x++ ) { 
			//System.out.println( "MapApplet startup x="+x );
			int TempTemp;
			colorpicker = colorpicker + 1;
			TempNyY = (double)x/(double)Rows;
			TempNyY = (TempNyY+1.0);
			NyY = (int)(TempNyY-1);
			NyX = (x-((NyY-1)*10));
			NyY = NyY*SquareSize;
			NyX = NyX*SquareSize;
			NyX = NyX-200;
			//System.out.println(x + ": Y=" + NyY + " X=" + NyX + " SquareSize=" + SquareSize);
			   
			if (colorpicker > 5) {
				colorpicker = 0;
			}

			TempTemp = (-5) + (int)(Math.random() * 20);
			Object_Location ny2 = new Object_Location( x, (NyX/20), (NyY/20), "white", TempTemp);
			//TerrainSquares.settInn( ny2 );
			//System.out.println( "Applet kommanderer om å lagre location X="+(NyX/20)+" Y="+(NyY/20) );
			Class_Controller.LagdNyLoc( ny2 );
			   
			temp[x] = TempTemp;
		}
		   
		Class_Controller.Startup();
	}
	   
	   public void init() {
	      width = getSize().width;
	      height = getSize().height;
	      setBackground( Color.white );
	      
	      addMouseMotionListener(this); 
	      //TerrainSquares = new TerrainList();
	      
	      SpawnTimer = 0;
          
          Random randomizer = new Random(1L); //remember longs are appended with L
          int randomInt = randomizer.nextInt();
          randomInt = Math.abs(randomInt);
          
          dim = getSize(); 
          addMouseMotionListener(this); 
          setBackground(Color.black); 
          offscreen = createImage(dim.width,dim.height); 
          bufferGraphics = offscreen.getGraphics(); 
		   
          /*
		   for ( x=0; x<=Squares; x++ ) { 
			   int TempTemp;
			   colorpicker = colorpicker + 1;
			   TempNyY = (double)x/(double)Rows;
			   TempNyY = (TempNyY+1.0);
			   NyY = (int)(TempNyY-1);
			   NyX = (x-((NyY-1)*10));
			   NyY = NyY*SquareSize;
			   NyX = NyX*SquareSize;
			   NyX = NyX-200;
			   //System.out.println(x + ": Y=" + NyY + " X=" + NyX + " SquareSize=" + SquareSize);
			   
			   if (colorpicker > 5) {
			   		colorpicker = 0;
		   		}

			   TempTemp = (-5) + (int)(Math.random() * 20);
			   Object_Location ny2 = new Object_Location( x, NyX, NyY, "white", TempTemp);
			   //TerrainSquares.settInn( ny2 );
			   System.out.println( "Applet kommanderer om å lagre location X="+NyX+" Y="+NyY );
			   Class_Controller.LagdNyLoc( ny2 );
			   
			   temp[x] = TempTemp;
		   }
		   */
	    	
	   }

	   public void destroy() {
	   }

	   public void start() {
	      if ( t == null ) {
	         t = new Thread( this );
	         threadSuspended = false;
	         t.start();
	      }
	      else {
	         if ( threadSuspended ) {
	            threadSuspended = false;
	            synchronized( this ) {
	               notify();
	            }
	         }
	      }
	   }

	   public void stop() {
	      threadSuspended = true;
	   }

	public void run() {
		try {
			while (true) {
				++i;
				if ( i == Squares ) {
					i = 0;
				}
				if ( threadSuspended ) {
					synchronized( this ) {
						while ( threadSuspended ) {
							wait();
						}
					}
				}
				SpawnTimer = SpawnTimer + 1;
	            //finn gammmel temp
	            //finn ut varmere\kaldere
	            //plukk random rute og fix
				SquareChange = 0 + (int)(Math.random() * Squares);
				if (SquareChange>Squares) { //401
					SquareChange=0; //0
				}
				Z=SquareChange; //0
				//temp[Z] = TerrainSquares.GetTemp(Z);
				temp[Z] = Class_Controller.GetLocTemp(Z);
				//System.out.println("rute#" +Z + " temp1=" + temp[Z]);
				//for ( x=1; x<=Squares; x++ ) { 
				if (Z<=Squares){
					if ((Z-11) < 0) {
						temp[Z] = (int) (temp[Z]+ (temp[Squares-11]/40.0));
					} else {
						temp[Z] = (int) (temp[Z]+ (temp[Z-11]/40.0));
					}
					if ((Z-10) < 0) {
						temp[Z] = (int) (temp[Z]+ (temp[Squares-10]/20.0));
					} else {
						temp[Z] = (int) (temp[Z]+ (temp[Z-10]/20.0));
					}
					if ((Z-9) < 0) {
						temp[Z] = (int) (temp[Z]+ (temp[Squares-9]/40.0));
					} else {
						temp[Z] = (int) (temp[Z]+ (temp[Z-9]/40.0));
					}
					
					if ((Z-1) < 0) {
						temp[Z] = (int) (temp[Z]+ (temp[Squares-1]/20.0));
					} else {
						temp[Z] = (int) (temp[Z]+ (temp[Z-1]/20.0));
					}
					if ((Z+1) > Squares) {
						temp[Z] = (int) (temp[Z]+ (temp[0+1]/20.0));
					} else {
						temp[Z] = (int) (temp[Z]+ (temp[Z+1]/20.0));
					}
					
					if ((Z+9) > Squares) {
						temp[Z] = (int) (temp[Z]+ (temp[0+9]/40.0));
					} else {
						temp[Z] = ((int) (temp[Z]+ (temp[Z+9]/40.0)));
					}
					if ((Z+10) > Squares) {
						temp[Z] = (int) (temp[Z]+ (temp[0+10]/20.0));
					} else {
						temp[Z] = ((int) (temp[Z]+ (temp[Z+10]/20.0)));
					}
					if ((Z+11) > Squares) {
						temp[Z] = (int) (temp[Z]+ (temp[0+11]/40.0));
					} else {
						temp[Z] = ((int) (temp[Z]+ (temp[Z+11]/40.0)));
					}
					
					if (temp[Z]<-10) {
						temp[Z] = -10;
					}
					if (temp[Z]>30) {
						temp[Z] = 30;
					}
					//random, local, slight temp change
					int TempChange = -1 + (int)(Math.random() * 3);
					//System.out.println( "random, local, slight temp change="+TempChange);
					temp[Z] = temp[Z] + TempChange;
					//TerrainSquares.SendTemp(Z, temp[Z]);
					Class_Controller.SendTemp(Z, temp[Z]);
				}
				repaint();
				t.sleep( intervals );  // interval given in milliseconds
			}
		}
		catch (InterruptedException e) { }
	}

	   public void paint( Graphics g ) {
		   
		   bufferGraphics.clearRect(0,0,dim.width,dim.width); 
		   bufferGraphics.setColor(Color.red); 
		   //bufferGraphics.drawString("Bad Double-buffered",10,10); 
		   g.drawImage(offscreen,0,0,this); 
		   
	    	for ( x=0; x<=(Squares-1); x++ ) { 
	    		//SquareY[x] = TerrainSquares.GetCoordY(x);
	    		//SquareX[x] = TerrainSquares.GetCoordX(x);
	    		//temp[x] = TerrainSquares.GetTemp(x);
	    		SquareY[x] = Class_Controller.GetLocCoordY(x)*20;
	    		SquareX[x] = Class_Controller.GetLocCoordX(x)*20;
	    		temp[x] = Class_Controller.GetLocTemp(x);
	    		
	    		//System.out.println("------");
	    		
	    		//red: if temp>10, R=(255/30)*temp, else 0
	    		//green: if temp>0 && temp<11, G=(255/10)*temp, else 0
	    		//blue: if temp<0 , B=(255/10)*temp, else 0
	    		
	    		   /*
	    		   Color30 = Color(255,0,0);
	    		   ---Color20 = Color(255,255,0);
	    		   Color10 = Color(0,255,0);
	    		   Color0 = Color(255,255,255);
	    		   Color_10 = Color(0,0,255);
	    		   */
	    		
	    		   /*GreenMin = 0;
	    		   GreenMax = 20;
	    		   RedMin = 20;
	    		   RedMax = 30;
	    		   BlueMax = -5;
	    		   BlueMin = -10;*/
	    		
	    		if (temp[x] >=10) {
	    			RedMax = 255;
    				RedMin = 0;
    				//GreenMax = 0;
    				GreenMax = 127;
    				GreenMin = 255;
    				BlueMax = 0;
    				BlueMin = 0;
    				ToMax= (double)temp[x]/20.0;
    				RedDiff = (RedMax - RedMin);
    				RedDiff = RedMin+(int)(ToMax*RedDiff);
    				GreenDiff = (GreenMax - GreenMin);
    				GreenDiff = GreenMin+(int)(ToMax*GreenDiff);
    				BlueDiff = (BlueMax - BlueMin);
    				BlueDiff = BlueMin+(int)(ToMax*BlueDiff);
    				/*GreenDiff = (int)(ToMax*(GreenMax - GreenMin));
    				BlueDiff = (int)(ToMax*(BlueMax - BlueMin));*/
    				/*RedDiff = (RedMax - RedMin);
    				GreenDiff = (GreenMax - GreenMin);
    				BlueDiff = (BlueMax - BlueMin);
    				RedDiff = (RedMin - (int)(ToMax*RedDiff));
    				GreenDiff = (GreenMin - (int)(ToMax*GreenDiff));
    				BlueDiff = (BlueMin - (int)(ToMax*BlueDiff));*/
    				//System.out.println("RedMax=" + RedMax + "RedMin=" + RedMin + " RedDiff=" + RedDiff);
    				//System.out.println("GreenMax=" + GreenMax + "GreenMin=" + GreenMin + " GreenDiff=" + GreenDiff);
    				//System.out.println("BlueMax=" + BlueMax + "BlueMin=" + BlueMin + " BlueDiff=" + BlueDiff);
    				R = RedDiff;
    				G = GreenDiff;
    				B = BlueDiff;
	    		}  else 
		    		   /*
		    		   Color30 = Color(255,0,0);
		    		   ---Color20 = Color(255,255,0);
		    		   Color10 = Color(0,255,0);
		    		   Color0 = Color(255,255,255);
		    		   Color_10 = Color(0,0,255);
		    		   */
	    			if (temp[x] >=0) {
	    			RedMax = 0;
	    			RedMin = 255;
    				GreenMax = 255;
    				GreenMin = 255;
    				BlueMax = 0;
    				BlueMin = 255;
    				ToMax= (double)temp[x]/10.0;
    				RedDiff = (RedMax - RedMin);
    				RedDiff = RedMin+(int)(ToMax*RedDiff);
    				GreenDiff = (GreenMax - GreenMin);
    				GreenDiff = GreenMin+(int)(ToMax*GreenDiff);
    				BlueDiff = (BlueMax - BlueMin);
    				BlueDiff = BlueMin+(int)(ToMax*BlueDiff);
    				/*RedDiff = (RedMax - RedMin);
    				GreenDiff = (GreenMax - GreenMin);
    				BlueDiff = (BlueMax - BlueMin);
    				//System.out.println("RedDiff1=" + RedDiff);
    				RedDiff = (RedMin - (int)(ToMax*RedDiff));
    				GreenDiff = (GreenMin - (int)(ToMax*GreenDiff));
    				BlueDiff = (BlueMin - (int)(ToMax*BlueDiff));*/
    				//System.out.println("RedDiff2=" + RedDiff);
    				//GreenDiff = (int)(ToMax*(GreenMax - GreenMin));
    				//BlueDiff = (int)(ToMax*(BlueMax - BlueMin))*(-1);
    				//System.out.println("RedDiff=" + RedDiff + " GreenDiff=" + GreenDiff + " BlueDiff=" + BlueDiff);
    				//System.out.println("RedMax=" + RedMax + "RedMin=" + RedMin + " RedDiff=" + RedDiff);
    				R = RedDiff;
    				G = GreenDiff;
    				B = BlueDiff;
	    		} else 
	    			/*
		    		   Color30 = Color(255,0,0);
		    		   ---Color20 = Color(255,255,0);
		    		   Color10 = Color(0,255,0);
		    		   Color0 = Color(255,255,255);
		    		   Color_10 = Color(0,0,255);
		    		   */
	    			if (temp[x] <=0 ) {
	    				RedMax = 255;
	    				RedMin = 0;
	    				GreenMax = 255;
	    				GreenMin = 127;
	    				BlueMax = 255;
	    				BlueMin = 255;
	    				ToMax= (double)temp[x]/-10.0;
	    				RedDiff = RedMax - RedMin;
	    				GreenDiff = GreenMax - GreenMin;
	    				BlueDiff = BlueMax - BlueMin;
	    				R = (int)(ToMax * RedDiff)+RedMin;
	    				G = (int)(ToMax * GreenDiff)+GreenMin;
	    				B = (int)(ToMax * BlueDiff)+BlueMin;
	    			} 

	    		/*if (temp[x] >10) {
	    			R = (255/20)*(temp[x]-10);
	    		} else R = 0;
	    		
	    		if (temp[x] > 0) { 
	    			if (temp[x] < 11) {
	    				G = (255/30)*temp[x];
	    			} else G = 255;
	    		} else G = 0;
	    		
	    		if (temp[x] < 0) {
	    			B = (255/10)*((-1)*temp[x]);
	    		} else B = 0;*/
	    		
	    		if (R<0){
	    			R=0;
	    		} else if (R>255){
	    			R=255;
	    		}
	    		if (G<0){
	    			G=0;
	    		} else if (G>255){
	    			G=255;
	    		}
	    		if (B<0){
	    			B=0;
	    		} else if (B>255){
	    			B=255;
	    		}
	    		
	    		int UnitsPresent = Class_Controller.GetUnitsLocated(x);
	    		if ( UnitsPresent > 0 ) {
    				R = 0;
    				G = 0;
    				B = 0;
    				//System.out.println(x+" UnitsPresent="+UnitsPresent+" color="+R+G+B);
	    		}
	    		//System.out.println("Temp=" + temp[x] + " ToMax=" + ToMax + " R=" + R + " G="+G+" B="+B);
	    		Color mixColor= new Color((int)R,(int)G,(int)B);
	    		//g.setColor(mixColor);
	    		bufferGraphics.setColor(mixColor);
	    		//System.out.println(x+" "+mixColor);
	            //g.fillRect(SquareX[x],SquareY[x],SquareSize,SquareSize);
	            bufferGraphics.fillRect( SquareX[x], SquareY[x], SquareSize, SquareSize );
	            //g.drawImage(offscreen,0,0,this); 
	    	}
	    	g.drawImage(offscreen,0,0,this); 
	   }
	   
	public void mouseMoved(MouseEvent evt)  
	{ 
		curX = evt.getX(); 
		curY = evt.getY(); 
		} 
	    
	public void mouseDragged(MouseEvent evt)  
	{ 
	} 
	    
	public void mouseClicked (MouseEvent me) { 
		curX = me.getX(); 
		curY = me.getY(); 
		if (curX > rect1xco && curX < rect1xco+rect1width && curY >rect1yco &&  
	    			  curY < rect1yco+rect1height)  rect1Clicked = true; 
		else  
			rect1Clicked = false; 
			repaint(); 
	} 

	public void mousePressed (MouseEvent me) {
	} 

	public void mouseReleased (MouseEvent me) {
	}  

	public void mouseEntered (MouseEvent me) { 
		mouseEntered = true; 
		repaint(); 
	} 

	public void mouseExited (MouseEvent me) { 
		mouseEntered = false; 
		repaint(); 
	}  
}