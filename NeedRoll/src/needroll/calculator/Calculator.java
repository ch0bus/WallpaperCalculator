package needroll.calculator;

interface AreaCalc{
	
	default double areaCalc(double width, double height, double ...length){ 		 
		
		double temp=0;
		for(double x : length)
			temp += x;
		
		if( temp != 0.0 )	// for room area
			return ( (width*height)+(temp*height) )*2;
		else
			return (width*height);  
	}
}

public class Calculator implements AreaCalc {
    
        double areaRoom;
	double areaDoor;
	double areaWindow;
	double areaAdd;					
	double areaDel;
        
	public static double areaWallp;
	public static double areaNumOfRapport;
	
	double heightRoom;
        double lengthRoom;        
        double widthRoom;
        double windowNumOf;
        double widthWindow;
        double heightWindow;
        double doorNumOf;
        double widthDoor;
        double heightDoor;
        int addNumOf;
        double widthAdd;
        double heightAdd;
        int delNumOf;
        double widthDel;
        double heightDel;
	double lengthWallpaper;
	double widthWallpaper;
	double rapportWallpaper;
        double selectAreaRoom;
	
	public Calculator(  double widthR, 
                            double lengthR, 
                            double heightR, 
                            double numOfWindow,	
                            double widthWin, 
                            double heightWin, 
                            double numOfDoor,	
                            double widthD, 
                            double heightD, 
                            int numOfAdd,
                            double addWidth,	
                            double addHeight,
                            int numOfDel,
                            double delWidth,	
                            double delHeight,	
                            double widthWallp,
                            double lengthWallp,
                            double rapport,
                            double areaGlue
                            )
	{   	        
                widthRoom = widthR;
                lengthRoom = lengthR;
                heightRoom = heightR;
                windowNumOf = numOfWindow;
                widthWindow = widthWin;
                heightWindow = heightWin;
                doorNumOf = numOfDoor;
                widthDoor = widthD;
                heightDoor = heightD;
                addNumOf = numOfAdd;
                widthAdd = addWidth;                
                heightAdd = addHeight;
                delNumOf = numOfDel;
                widthDel = delWidth;
                heightDel = delHeight;
                lengthWallpaper = lengthWallp;
		widthWallpaper = widthWallp;
		rapportWallpaper = rapport;
                selectAreaRoom = areaGlue;               
        }
        
       public double areaTotalRoom(){
            areaAdd = (areaCalc(widthAdd, heightAdd))*addNumOf;
            if(selectAreaRoom != 0.0)
		return areaRoom = (selectAreaRoom + areaAdd);
            else{
		areaRoom = areaCalc(widthRoom, heightRoom, lengthRoom);                
                return (areaRoom + areaAdd);
            }
        }
        
        public double areaTotalDel(){
            if(doorNumOf > 1)
		areaDoor = ( areaCalc(widthDoor, heightDoor) )*doorNumOf;		
            else
		areaDoor = areaCalc(widthDoor, heightDoor);            
            if(windowNumOf > 1)
		areaWindow = ( areaCalc(widthWindow, heightWindow) )*windowNumOf;		
            else
		areaWindow = areaCalc(widthWindow, heightWindow);
            
            areaDel = (areaCalc(widthDel, heightDel))*delNumOf;
            
            return (areaDel + areaDoor + areaWindow); 
        }
        
        public double areaOnlyWindow(){
            return (widthWindow*heightWindow)*windowNumOf;
        }
        
        public double areaOnlyDoor(){
            return (widthDoor*heightDoor)*doorNumOf;
        }
        
        public double areaOnlyWindowAndDoor(){
            if(doorNumOf > 1)
		areaDoor = ( areaCalc(widthDoor, heightDoor) )*doorNumOf;		
            else
		areaDoor = areaCalc(widthDoor, heightDoor);            
            if(windowNumOf > 1)
		areaWindow = ( areaCalc(widthWindow, heightWindow) )*windowNumOf;		
            else
		areaWindow = areaCalc(widthWindow, heightWindow);
            
            return (areaDoor + areaWindow);
        }
        
        public double areaOnlyDel(){ return areaDel = (areaCalc(widthDel, heightDel))*delNumOf; }
        
        public double areaOnlyAdd(){ return areaAdd = (areaCalc(widthAdd, heightAdd))*addNumOf; }
        
        public double areaWallp(){ return areaWallp = widthWallpaper * lengthWallpaper; }
        
        public double areaWallpRapport(){            
            if(rapportWallpaper!=0.0){
                double strip = stripOfWallpaper();                        
                return (widthWallpaper * rapportWallpaper)*strip;
            }
            else
                return rapportWallpaper;
        }
        
        public double bruteforce(){
            double strip = stripOfWallpaper();
        
            return ( ((widthRoom+lengthRoom)*2)+(areaOnlyAdd()/heightRoom)-(areaOnlyDel()/heightRoom)-(widthWindow*windowNumOf)-(widthDoor*doorNumOf) )/widthWallpaper/strip; // total number of roll with bruteforce
          
        }
        
        public double stripOfRoom(){
            return ( (((widthRoom+lengthRoom)*2)+(areaOnlyAdd()/heightRoom)-(areaOnlyDel()/heightRoom)-(widthWindow*windowNumOf)-(widthDoor*doorNumOf) )/widthWallpaper );
        }
        
        public int stripOfWallpaper(){
            if(rapportWallpaper!=0.0)
                return (int)( (lengthWallpaper) / (heightRoom+rapportWallpaper) );    
            else
                return (int)( (lengthWallpaper) / heightRoom );
        }
        
}
