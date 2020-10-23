
package needroll;

import needroll.calculator.Calculator;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.regex.Matcher;                                                 // for RegEx
import java.util.regex.Pattern;                                                 // for RegEx
import java.util.Locale;
import java.util.Optional;                                                      // for tip
import javafx.application.*;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color; 
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert.AlertType;                                    // for tip
import javafx.stage.*; 
import javafx.scene.text.Font;                                                  
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;                                            
import javafx.scene.text.Text;                                                  
import javafx.scene.image.*;

public class NeedRoll extends Application {
    
    static String selectUsedAreaName = "";
    static double selectUsedAreaValue;
    static double roomFieldWidth, roomFieldLength, roomFieldHeight;
    static double windowFieldWidth, windowFieldHeight;
    static double doorFieldWidth, doorFieldHeight;
    static int windowFieldOfNum, doorFieldOfNum;
    double wallpaperFieldWidth, wallpaperFieldLength, wallpaperFieldRapport;
    static double addFieldWidth, addFieldHeight;
    static double delFieldWidth, delFieldHeight;
    static int addFieldNumOf, delFieldNumOf = 0;
    
    double roll;
    double roll_bruteforce;
    double areaTotal;
    static double areaGlue;
    double areaNotGlue;
    double areaTotalWallpaper;
    static double areaWallpaper;
    double areaWallpaperRapport;
    double stripRoom;
    int stripWallpaper;
    static double areaPieChartWindowAndDoor;
    static double areaPieChartDel;
    
    static double windowArea=0.0;
    static double doorArea=0.0;
    static double addArea=0.0;
    static double delArea=0.0;
    static double spaceArea=0.0;
    
    public void start(Stage primaryStage) throws Exception {
        
        primaryStage.setResizable(false);                                       
        GridPane root = new GridPane();	
        root.setGridLinesVisible(false);                                        
        root.setHgap(10);
        root.setVgap(10);
        
	Scene scene = new Scene(root,900,700);		
	
        PieChart piechart = new PieChart();
        
        Circle circleRoom = new Circle(); 
        circleRoom.setRadius(5);  
        circleRoom.setFill(Color.GRAY); 
	Circle circleWindow = new Circle(); 
        circleWindow.setRadius(5);  
        circleWindow.setFill(Color.GRAY);
        Circle circleDoor = new Circle(); 
        circleDoor.setRadius(5);  
        circleDoor.setFill(Color.GRAY);
        Circle circleAdd = new Circle(); 
        circleAdd.setRadius(5);  
        circleAdd.setFill(Color.GRAY);
        Circle circleDel = new Circle(); 
        circleDel.setRadius(5);  
        circleDel.setFill(Color.GRAY);
        Circle circleWallpaper = new Circle(); 
        circleWallpaper.setRadius(5);  
        circleWallpaper.setFill(Color.GRAY);
        
        Button btnCalc = new Button ();
               
        // Room Parameter
	Text roomParam = setTextStyle_forHeader("Room parameter");
        roomParam.setFont(Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,22));  
        GridPane.setHalignment(roomParam, HPos.CENTER);
        
        Text roomWidth = setTextStyle_forNameParameter("width:");
        GridPane.setHalignment(roomWidth, HPos.RIGHT);
        
        Text roomLength = setTextStyle_forNameParameter("length:");
        GridPane.setHalignment(roomLength, HPos.RIGHT);
        
        Text roomHeight = setTextStyle_forNameParameter("height:");
        GridPane.setHalignment(roomHeight, HPos.RIGHT);
        
        Text windowParam = setTextStyle_forNameSpace("Window parameter");
        GridPane.setHalignment(windowParam, HPos.LEFT);
        
        Text windowWidth = setTextStyle_forNameParameter("width:");
        GridPane.setHalignment(windowWidth, HPos.RIGHT);
        
        Text windowHeight = setTextStyle_forNameParameter("height:");
        GridPane.setHalignment(windowHeight, HPos.RIGHT);
        
        Text windowOfNum = setTextStyle_forNameParameter("number:");
        GridPane.setHalignment(windowOfNum, HPos.RIGHT);
        
        Text doorParam = setTextStyle_forNameSpace("Door Parameter:");
        GridPane.setHalignment(doorParam, HPos.LEFT);
        
        Text doorWidth = setTextStyle_forNameParameter("width:");
        GridPane.setHalignment(doorWidth, HPos.RIGHT);
        
        Text doorHeight = setTextStyle_forNameParameter("height:");
        GridPane.setHalignment(doorHeight, HPos.RIGHT);
        
        Text doorOfNum = setTextStyle_forNameParameter("number:");
        GridPane.setHalignment(doorOfNum, HPos.RIGHT);
        
        Text addParam = setTextStyle_forNameSpace("Add area:");
        GridPane.setHalignment(addParam, HPos.LEFT);
    
        Text addWidth = setTextStyle_forNameParameter("width:");
        GridPane.setHalignment(addWidth, HPos.RIGHT);
        
        Text addHeight = setTextStyle_forNameParameter("height:");
        GridPane.setHalignment(addHeight, HPos.RIGHT);
        
        Text addOfNum = setTextStyle_forNameParameter("number:");
        GridPane.setHalignment(addOfNum, HPos.RIGHT);
        
        Text delParam = setTextStyle_forNameSpace("Delete area:");
        GridPane.setHalignment(delParam, HPos.LEFT);
        
        Text delWidth = setTextStyle_forNameParameter("width:");
        GridPane.setHalignment(delWidth, HPos.RIGHT);
        
        Text delHeight = setTextStyle_forNameParameter("height:");
        GridPane.setHalignment(delHeight, HPos.RIGHT);
        
        Text delOfNum = setTextStyle_forNameParameter("number:");
        GridPane.setHalignment(delOfNum, HPos.RIGHT);
        
        Text wallpaperParam = setTextStyle_forHeader("  Wallpaper parameter:");
        GridPane.setHalignment(wallpaperParam, HPos.CENTER);    
    
        Text wallpaperWidth = setTextStyle_forNameParameter("width:");
        GridPane.setHalignment(wallpaperWidth, HPos.RIGHT);
        
        Text wallpaperLength = setTextStyle_forNameParameter("length:");
        GridPane.setHalignment(wallpaperLength, HPos.RIGHT);
        
        Text wallpaperRapport = setTextStyle_forNameParameter("rapport:");
        GridPane.setHalignment(wallpaperRapport, HPos.RIGHT);
    
        Text resultAreaTotal = setTextStyle_forTotalInfo("Total area:");
        Text resultAreaGlue = setTextStyle_forTotalInfo("Glue area:");
        Text resultAreaNotGlue = setTextStyle_forTotalInfo("Do not glue area:");
        Text resultAreaWallpaper = setTextStyle_forTotalInfo("Wallpaper area:");
        Text resultAreaWallpaperRapport = setTextStyle_forTotalInfo("Rapport area:");
        Text resultAreaWallpaperTotal = setTextStyle_forTotalInfo("Used area:");
        Text resultStripOfRoom = setTextStyle_forNameParameter("Strips on Room:");
        Text resultStripOfWallpaper = setTextStyle_forNameParameter("Strips from Roll:");
        Text resultTotal = setTextStyle_forResult("Number of Roll:");
        
        Label resultAreaTotalValue;
        Label resultAreaGlueValue;
        Label resultAreaNotGlueValue;
        Label resultAreaWallpaperValue;
        Label resultAreaWallpaperRapportValue;
        Label resultAreaWallpaperTotalValue;
        Label resultTotalValueRoll;
        Label resultTotalValueRoll_bruteforce;
        Label resultStripOfRoomValue;
        Label resultStripOfWallpaperValue;
        resultStripOfRoomValue = new Label("");
        resultStripOfWallpaperValue = new Label("");
        resultTotalValueRoll = new Label("");
        resultTotalValueRoll_bruteforce = new Label("");
        resultAreaTotalValue = new Label("");
        resultAreaGlueValue = new Label("");
        resultAreaNotGlueValue = new Label("");
        resultAreaWallpaperTotalValue = new Label("");
        resultAreaWallpaperValue = new Label("");
        resultAreaWallpaperRapportValue = new Label("");
        
        Pattern patUserInput;                                                   // Pattern User Input
        //Matcher matUserInput;
        patUserInput = Pattern.compile("[\\|\\~\\`\\!\\?\\*\\-\\+\\,a-zA-Z/\\\\]+");
        
        //TextField tfSelectUsedAreaName = new TextField();                     // ComboBox
        TextField tfSelectUsedAreaValue = new TextField();                                              
            tfSelectUsedAreaValue.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfSelectUsedAreaValue.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                        selectUsedAreaValue = Double.parseDouble(tfSelectUsedAreaValue.getText());
                }
            });                                                                                         
	TextField tfRoomWidth = new TextField();                                                        
            tfRoomWidth.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfRoomWidth.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                       roomFieldWidth = Double.parseDouble(tfRoomWidth.getText());					
                }
            });                                                                                         
        TextField tfRoomLength = new TextField();                                                       
            tfRoomLength.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfRoomLength.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                       roomFieldLength = Double.parseDouble(tfRoomLength.getText());                    
                }
            });                                                                                         
        TextField tfAddWidth = new TextField();                                                           
            tfAddWidth.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                        Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfAddWidth.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                       addFieldWidth = Double.parseDouble(tfAddWidth.getText());					
                }
            });                                                                                           
	TextField tfAddHeight = new TextField();                                                            
            tfAddHeight.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfAddHeight.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                       addFieldHeight = Double.parseDouble(tfAddHeight.getText());					
                }
            });                                                                                                 
	TextField tfAddOfNum = new TextField();                                                                                    
            tfAddOfNum.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfAddOfNum.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                        addFieldNumOf = Integer.parseInt(tfAddOfNum.getText());					
                }
            });                                                                                                    
        TextField tfDelWidth = new TextField();                                                                    
            tfDelWidth.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfDelWidth.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                        delFieldWidth = Double.parseDouble(tfDelWidth.getText());					
                }
            });                                                                                                    
        TextField tfDelHeight = new TextField();                                                                   
            tfDelHeight.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfDelHeight.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                        delFieldHeight = Double.parseDouble(tfDelHeight.getText());					
                }
            });                                                                                                                
        TextField tfDelOfNum = new TextField();                                                                 
            tfDelOfNum.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    Matcher matUserInput;
                    matUserInput = patUserInput.matcher(tfDelOfNum.getText());
                    if(matUserInput.find()){                                    
                        //pattern match
                        showWarning_incorrectEnter();
                    }
                    else
                        //pattern does not match
                        delFieldNumOf = Integer.parseInt(tfDelOfNum.getText());					
                }
            });
                                                                                                                       
        Text choiceNameAreaVariable_Default = setTextStyle_forInfo("Select the options above to calculate the area of the room");
        GridPane.setHalignment(choiceNameAreaVariable_Default, HPos.CENTER);
        
        Text choiceNameAreaVariable_Area = setTextStyle_forInfo("Please enter an area value in the field above");
        GridPane.setHalignment(choiceNameAreaVariable_Area, HPos.CENTER);
        
        ObservableList<String> selectArea = FXCollections.observableArrayList( "perimeter", "room`s area", "glue area");
        ComboBox<String> cbSelectUsedArea = new ComboBox<String> (selectArea);		
            cbSelectUsedArea.setValue("options");				// show default value
            cbSelectUsedArea.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent ae){
                    selectUsedAreaName = cbSelectUsedArea.getValue(); 
                    if(selectUsedAreaName.equals("perimeter")){
                        root.add(roomWidth, 1, 4);
                        root.add(tfRoomWidth,  2, 4);
                        root.add(roomLength, 3, 4);
                        root.add(tfRoomLength, 4, 4);		
                        root.getChildren().remove(tfSelectUsedAreaValue);       // hiden field
                        root.getChildren().remove(choiceNameAreaVariable_Default);
                        root.getChildren().remove(choiceNameAreaVariable_Area);
                        
                    }
                    if(selectUsedAreaName.equals("room`s area")){
                        root.add(tfSelectUsedAreaValue, 4, 3);
                        root.getChildren().remove(roomWidth);
                        root.getChildren().remove(tfRoomWidth);
                        root.getChildren().remove(roomLength);
                        root.getChildren().remove(tfRoomLength);
                        root.getChildren().remove(choiceNameAreaVariable_Default);
                        root.add(choiceNameAreaVariable_Area, 1,4, 6,1);
                        
                    }
                    if(selectUsedAreaName.equals("glue area")){ 
                        root.add(tfSelectUsedAreaValue, 4, 3);
                        root.getChildren().remove(roomWidth);
                        root.getChildren().remove(tfRoomWidth);
                        root.getChildren().remove(roomLength);
                        root.getChildren().remove(tfRoomLength);
                        root.getChildren().remove(choiceNameAreaVariable_Default);
                        root.add(choiceNameAreaVariable_Area, 1,4, 6,1);
                        
                    }
                }			
            });
                
        ObservableList<String> roomH = FXCollections.observableArrayList( "2.5", "2.7", "3.0", "3.10");
        ComboBox<String> cbRoomHeight = new ComboBox<String> (roomH);		
            cbRoomHeight.setEditable(true);                                                                                
        	cbRoomHeight.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbRoomHeight.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                roomFieldHeight = Double.parseDouble( cbRoomHeight.getValue() );				
                    }			
		});
            
        ObservableList<String> windowW = FXCollections.observableArrayList( "1.5", "2.0");
            ComboBox<String> cbWindowWidth = new ComboBox<String> (windowW);		
		cbWindowWidth.setValue("1.5");													
		cbWindowWidth.setEditable(true);                                                                            
        	cbWindowWidth.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbWindowWidth.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                windowFieldWidth = Double.parseDouble( cbWindowWidth.getValue() );				
			}			
		});
                
        ObservableList<String> windowH = FXCollections.observableArrayList( "1.3", "1.5");
            ComboBox<String> cbWindowHeight = new ComboBox<String> (windowH);		
		cbWindowHeight.setValue("1.3");													
		cbWindowHeight.setEditable(true);                                                                               
        	cbWindowHeight.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbWindowHeight.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                windowFieldHeight = Double.parseDouble( cbWindowHeight.getValue() );				
			}			
		});
                
        ObservableList<String> wOfNum = FXCollections.observableArrayList( "1", "2", "3");
            ComboBox<String> cbWindowOfNum = new ComboBox<String> (wOfNum);		
		cbWindowOfNum.setValue("1");													
		cbWindowOfNum.setEditable(true);                                                                                
        	cbWindowOfNum.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbWindowOfNum.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                windowFieldOfNum = Integer.parseInt( cbWindowOfNum.getValue() );				
			}			
		});
                
        ObservableList<String> doorW = FXCollections.observableArrayList( "0.7", "0.9", "1.3");
            ComboBox<String> cbDoorWidth = new ComboBox<String> (doorW);		
		cbDoorWidth.setValue("0.9");													
		cbDoorWidth.setEditable(true);                                                                              
        	cbDoorWidth.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbDoorWidth.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                doorFieldWidth = Double.parseDouble( cbDoorWidth.getValue() );				
			}			
		});
                
        ObservableList<String> doorH = FXCollections.observableArrayList( "2.0", "2.3");
            ComboBox<String> cbDoorHeight = new ComboBox<String> (doorH);		
		cbDoorHeight.setValue("2.0");													
		cbDoorHeight.setEditable(true);                                                                             
        	cbDoorHeight.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbDoorHeight.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                doorFieldHeight = Double.parseDouble( cbDoorHeight.getValue() );				
			}			
		});
                
        ObservableList<String> dOfNum = FXCollections.observableArrayList( "1", "2", "3");
            ComboBox<String> cbDoorOfNum = new ComboBox<String> (dOfNum);		
		cbDoorOfNum.setValue("1");													
		cbDoorOfNum.setEditable(true);                                                                              
        	cbDoorOfNum.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbDoorOfNum.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                doorFieldOfNum = Integer.parseInt( cbDoorOfNum.getValue() );				
			}			
		});
                
        ObservableList<String> wallpWidth = FXCollections.observableArrayList( "0.53", "0.70", "1.06");
            ComboBox<String> cbWallpaperWidth = new ComboBox<String> (wallpWidth);		
		cbWallpaperWidth.setEditable(true);                                                                     
        	cbWallpaperWidth.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbWallpaperWidth.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                wallpaperFieldWidth = Double.parseDouble( cbWallpaperWidth.getValue() );				
			}			
		});
                
        ObservableList<String> wallpLength = FXCollections.observableArrayList( "10.05", "15.05", "25.05");
            ComboBox<String> cbWallpaperLength = new ComboBox<String> (wallpLength);		
		cbWallpaperLength.setEditable(true);                                                                            
        	cbWallpaperLength.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbWallpaperLength.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                wallpaperFieldLength = Double.parseDouble( cbWallpaperLength.getValue() );				
			}			
		});
                                                                                                                                        
        ObservableList<String> wRapport = FXCollections.observableArrayList( "0.0", "0.10", "0.32", "0.64", "0.80");
            ComboBox<String> cbWallpaperRapport = new ComboBox<String> (wRapport);		
		cbWallpaperRapport.setEditable(true);                                                                   
        	cbWallpaperRapport.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
                            Matcher matUserInput;
                            matUserInput = patUserInput.matcher(cbWallpaperRapport.getValue());
                            if(matUserInput.find()){                                    
                                //pattern match
                                showWarning_incorrectEnter();
                            }
                            else
                                //pattern does not match
                                wallpaperFieldRapport = Double.parseDouble( cbWallpaperRapport.getValue() );				
			}			
		});
                
        
        CheckBox checkAddField = new CheckBox();                                // CheckBox 
        GridPane.setHalignment(checkAddField, HPos.RIGHT);
        Text infoAddField = setTextStyle_forInfo("Select this item to add an area");
        GridPane.setHalignment(infoAddField, HPos.CENTER);
        
        checkAddField.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent ae){
                if(checkAddField.isSelected()){
                    root.getChildren().remove(infoAddField);
                    root.add(addWidth, 1, 10);		
                    root.add(tfAddWidth,  2, 10);
                    root.add(addHeight, 3, 10);		
                    root.add(tfAddHeight, 4, 10);
                    root.add(addOfNum, 5, 10);
                    root.add(tfAddOfNum, 6, 10);
                    root.add(circleAdd, 7, 10);
                }
                else{
                    root.getChildren().remove(addWidth);
                    root.getChildren().remove(tfAddWidth);
                    root.getChildren().remove(addHeight);
                    root.getChildren().remove(tfAddHeight);
                    root.getChildren().remove(addOfNum);
                    root.getChildren().remove(tfAddOfNum);
                    root.getChildren().remove(circleAdd);
                    
                    root.add(infoAddField, 1, 10, 6, 1);
                }
                    
            }
        });
        
        CheckBox checkDelField = new CheckBox();
        GridPane.setHalignment(checkDelField, HPos.RIGHT);
        Text infoDelField = setTextStyle_forInfo("Select this item to delete an area");
        GridPane.setHalignment(infoDelField, HPos.CENTER);
        
        checkDelField.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent ae){
                if(checkDelField.isSelected()){
                    root.getChildren().remove(infoDelField);
                    root.add(delWidth, 1, 12);		
                    root.add(tfDelWidth,  2, 12);
                    root.add(delHeight, 3, 12);		
                    root.add(tfDelHeight, 4, 12);
                    root.add(delOfNum, 5, 12);
                    root.add(tfDelOfNum, 6, 12);
                    root.add(circleDel, 7, 12);
                }
                else{
                    root.getChildren().remove(delWidth);
                    root.getChildren().remove(tfDelWidth);
                    root.getChildren().remove(delHeight);
                    root.getChildren().remove(tfDelHeight);
                    root.getChildren().remove(delOfNum);
                    root.getChildren().remove(tfDelOfNum);
                    root.getChildren().remove(circleDel);
                    
                    root.add(infoDelField, 1, 12, 6, 1);
                }
                    
            }
        });
        
        Text notBruteInfo = setTextStyle_forInfo("Function \"bruteforce\" can not be performed in \"glue area\" mode.");
        GridPane.setHalignment(notBruteInfo, HPos.CENTER);
        
        CheckBox checkBruteForce = new CheckBox("bruteforce");
        GridPane.setHalignment(checkBruteForce, HPos.LEFT);
        checkBruteForce.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent ae){
                if(checkBruteForce.isSelected()){
                     if(selectUsedAreaName.equals("glue area")){ 
                        root.add(notBruteInfo, 1, 25, 5,1);
                     }
                    else{
                        root.add(resultTotalValueRoll_bruteforce, 3,24);
                        root.add(resultStripOfRoom, 4,24);
                        root.add(resultStripOfWallpaper, 4,25);
                        root.add(resultStripOfRoomValue, 5,24);
                        root.add(resultStripOfWallpaperValue, 5,25);
                        
                    }
                }            
                else{
                    root.getChildren().remove(notBruteInfo);
                    root.getChildren().remove(resultStripOfRoom);
                    root.getChildren().remove(resultTotalValueRoll_bruteforce);
                    root.getChildren().remove(resultStripOfWallpaper);
                    root.getChildren().remove(resultStripOfRoomValue);
                    root.getChildren().remove(resultStripOfWallpaperValue);
                }                    
            }
        });
                                                                                        
        Button btnDiscard = new Button("Discard");
        btnDiscard.setMaxWidth(100);
        GridPane.setHalignment(btnDiscard, HPos.RIGHT);
        btnDiscard.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Reset values");
                alert.setHeaderText("Are you sure you want to reset all values?");
                alert.setContentText("Click \"ok\" to reset all entered values");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    tfSelectUsedAreaValue.clear();                              
                    selectUsedAreaValue=0.0;
                    tfRoomWidth.clear();
                    roomFieldWidth=0.0;
                    tfRoomLength.clear();
                    roomFieldLength=0.0;
                    tfAddWidth.clear();
                    addFieldWidth=0.0;
                    tfAddHeight.clear();
                    addFieldHeight=0.0;
                    tfAddOfNum.clear();
                    addFieldNumOf=0;
                    tfDelWidth.clear();
                    delFieldWidth=0.0;
                    tfDelHeight.clear();
                    delFieldHeight=0;
                    tfDelOfNum.clear();
                    delFieldNumOf=0;
                    cbRoomHeight.setValue("0.0");
                    roomFieldHeight=0.0;
                    cbWindowWidth.setValue("0.0");
                    windowFieldWidth=0.0;
                    cbWindowHeight.setValue("0.0");
                    windowFieldHeight=0.0;
                    cbWindowOfNum.setValue("0");
                    windowFieldOfNum=0;
                    cbDoorWidth.setValue("0.0");
                    doorFieldWidth=0.0;
                    cbDoorHeight.setValue("0.0");
                    doorFieldHeight=0.0;
                    cbDoorOfNum.setValue("0");
                    doorFieldOfNum=0;
                    cbWallpaperWidth.setValue("0.0");
                    wallpaperFieldWidth=0.0;
                    cbWallpaperLength.setValue("0.0");
                    wallpaperFieldLength=0.0;
                    cbWallpaperRapport.setValue("0.0");
                    wallpaperFieldRapport=0.0;
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            }
        });
////////////////////////////////////////////////////////////////////////////////
//////////////////////CALCULATOR////////////////////////////////////////////////
        btnCalc = new Button ("Calculate");                              
        btnCalc.setMaxWidth(100);
        GridPane.setHalignment(btnCalc, HPos.LEFT);
        btnCalc.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent ae){ 
                switch(selectUsedAreaName){
                        case "perimeter":
                            selectUsedAreaValue = 0.0;                            
                        break;
                        case "room`s area":                            
                            roomFieldLength = 3;
                            roomFieldWidth = (selectUsedAreaValue / roomFieldLength);
                            selectUsedAreaValue = 0.0;
                        break;
                        case "glue area":
                            roomFieldWidth = 0.0;
                            roomFieldLength = 0.0; 
                        break;
                    }
                Calculator objCalc = new Calculator(roomFieldWidth, 
					roomFieldLength, 
					roomFieldHeight,
					windowFieldOfNum,
                                        windowFieldWidth,
					windowFieldHeight, 
					doorFieldOfNum,
					doorFieldWidth, 
					doorFieldHeight,
                                        addFieldNumOf,
					addFieldWidth,
					addFieldHeight,
                                        delFieldNumOf,
					delFieldWidth,
					delFieldHeight,
                                        wallpaperFieldWidth,
                                        wallpaperFieldLength,			
                                        wallpaperFieldRapport,
					selectUsedAreaValue
					);
///////////////////////////Circle Indicator/////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
            switch(selectUsedAreaName){
            case "":
                circleRoom.setFill(Color.RED);
            break;
            case "perimeter":
                if(roomFieldWidth==0 || roomFieldHeight==0)
                    circleRoom.setFill(Color.RED);
                else
                    circleRoom.setFill(Color.ORANGE);
            break;
            case "room`s area":
                if(roomFieldWidth==0 || roomFieldHeight==0)
                    circleRoom.setFill(Color.RED);
                else
                    circleRoom.setFill(Color.ORANGE);
            break;
            case "glue area":
                if(selectUsedAreaValue==0 || roomFieldHeight==0)
                    circleRoom.setFill(Color.RED);
                else
                    circleRoom.setFill(Color.ORANGE);
            break;
        }
        
        if(windowFieldWidth==0 || windowFieldHeight==0)
            circleWindow.setFill(Color.RED);
        else
            circleWindow.setFill(Color.ORANGE);
        
        if(doorFieldWidth==0 || doorFieldHeight==0)
            circleDoor.setFill(Color.RED);
        else
            circleDoor.setFill(Color.ORANGE);
        
        if(addFieldWidth==0 || addFieldHeight==0)
            circleAdd.setFill(Color.RED);
        else
            circleAdd.setFill(Color.ORANGE);
        
        if(delFieldWidth==0 || delFieldHeight==0)
            circleDel.setFill(Color.RED);
        else
            circleDel.setFill(Color.ORANGE);
        
        if(wallpaperFieldWidth==0 || wallpaperFieldLength==0)
            circleWallpaper.setFill(Color.RED);
        else
            circleWallpaper.setFill(Color.ORANGE);
///////////////////////////end Circle Indicator/////////////////////////////////
                areaTotal = objCalc.areaTotalRoom();
                areaNotGlue = objCalc.areaTotalDel();
                areaWallpaper = objCalc.areaWallp();
                areaWallpaperRapport = objCalc.areaWallpRapport();
                areaPieChartWindowAndDoor = objCalc.areaOnlyWindowAndDoor();
                areaPieChartDel = objCalc.areaOnlyDel();
                areaTotalWallpaper = areaWallpaper - areaWallpaperRapport;                
                areaGlue = (areaTotal - areaNotGlue);                
                roll = (areaGlue / areaTotalWallpaper);
                roll_bruteforce = objCalc.bruteforce();
                stripRoom = objCalc.stripOfRoom();
                stripWallpaper = objCalc.stripOfWallpaper();
                windowArea=objCalc.areaOnlyWindow();
                doorArea=objCalc.areaOnlyDoor();
                addArea=objCalc.areaOnlyAdd();
                delArea=objCalc.areaOnlyDel();
                spaceArea=objCalc.areaOnlyWindowAndDoor();
                
                piechart.setData(getChartData());
                
                DecimalFormat decimalFormat = new DecimalFormat( "#.##" );
                String roll_decimal = decimalFormat.format(roll);
                String roll_bruteforce_decimal = decimalFormat.format(roll_bruteforce);
                String areaTotal_decimal = decimalFormat.format(areaTotal);
                String areaGlue_decimal = decimalFormat.format(areaGlue);
                String areaNotGlue_decimal = decimalFormat.format(areaNotGlue);
                String areaWallpaper_decimal = decimalFormat.format(areaWallpaper);
                String areaWallpaperRapport_decimal = decimalFormat.format(areaWallpaperRapport);
                String areaTotalWallpaper_decimal = decimalFormat.format(areaTotalWallpaper);
                String stripRoom_decimal = decimalFormat.format(stripRoom);
                String stripWallpaper_decimal = decimalFormat.format(stripWallpaper);
                
                resultTotalValueRoll.setText(roll_decimal);
                resultTotalValueRoll_bruteforce.setText(roll_bruteforce_decimal);
                resultAreaTotalValue.setText(areaTotal_decimal);
                resultAreaGlueValue.setText(areaGlue_decimal);
                resultAreaNotGlueValue.setText(areaNotGlue_decimal);
                resultAreaWallpaperValue.setText(areaWallpaper_decimal);
                resultAreaWallpaperRapportValue.setText(areaWallpaperRapport_decimal);
                resultAreaWallpaperTotalValue.setText(areaTotalWallpaper_decimal);
                resultStripOfRoomValue.setText(stripRoom_decimal);
                resultStripOfWallpaperValue.setText(stripWallpaper_decimal);
                
                root.add(checkBruteForce, 2,24);
            }
        }); // end button hundler
       
//////////////////////END CALCULATOR////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
///////////////////////button///////////////////////////////////////////////////
        
        
        Button btnViewLog = new Button ("Details");                              
        btnViewLog.setMaxWidth(100);
        GridPane.setHalignment(btnViewLog, HPos.LEFT);
            btnViewLog.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent event) {
                
                Stage newWindow = new Stage();
                newWindow.setResizable(false);
                
                GridPane logWindow = new GridPane();
                
                logWindow.setHgap(10);
                logWindow.setVgap(5);		
                logWindow.setGridLinesVisible(false);  
                
                PieChart piechartLog = new PieChart();
                piechartLog.setData(getChartData());
    
                DecimalFormat decimalFormat = new DecimalFormat( "#.##" );
                String roll_decimal = decimalFormat.format(roll);
                String roll_bruteforce_decimal = decimalFormat.format(roll_bruteforce);
                String areaTotal_decimal = decimalFormat.format(areaTotal);
                String windowArea_decimal = decimalFormat.format(windowArea);
                String doorArea_decimal = decimalFormat.format(doorArea);
                String addArea_decimal = decimalFormat.format(addArea);
                String delArea_decimal = decimalFormat.format(delArea);
                String areaGlue_decimal = decimalFormat.format(areaGlue);
                String spaceArea_decimal = decimalFormat.format(spaceArea);
                String areaNotGlue_decimal = decimalFormat.format(areaNotGlue);
                String areaWallpaper_decimal = decimalFormat.format(areaWallpaper);
                String areaWallpaperRapport_decimal = decimalFormat.format(areaWallpaperRapport);
                String areaTotalWallpaper_decimal = decimalFormat.format(areaTotalWallpaper);
                String stripRoom_decimal = decimalFormat.format(stripRoom);
                String stripWallpaper_decimal = decimalFormat.format(stripWallpaper);
                                
                Label StripOfRoomValue = new Label("");
                Label StripOfWallpaperValue = new Label("");
                Label TotalValueRoll = new Label("");
                Label TotalValueRoll_bruteforce = new Label("");
                Label AreaTotalValue = new Label("");
                Label AreaGlueValue = new Label("");
                Label AreaNotGlueValue = new Label("");
                Label doorAreaValue = new Label("");
                Label windowAreaValue = new Label("");
                Label addAreaValue = new Label("");
                Label delAreaValue = new Label("");
                Label delAreaValue_2 = new Label("");
                Label spaceAreaValue = new Label("");
                Label AreaWallpaperTotalValue = new Label("");
                Label areaWallpaperValue = new Label("");
                Label AreaWallpaperValue = new Label("");
                Label AreaWallpaperRapportValue = new Label("");
                
                TotalValueRoll.setText(roll_decimal);
                TotalValueRoll_bruteforce.setText(roll_bruteforce_decimal);
                AreaTotalValue.setText(areaTotal_decimal);
                AreaGlueValue.setText(areaGlue_decimal);
                AreaNotGlueValue.setText(areaNotGlue_decimal);
                windowAreaValue.setText(windowArea_decimal);
                doorAreaValue.setText(doorArea_decimal);
                addAreaValue.setText(addArea_decimal);
                delAreaValue.setText(delArea_decimal);
                delAreaValue_2.setText(delArea_decimal);
                spaceAreaValue.setText(spaceArea_decimal);
                areaWallpaperValue.setText(areaWallpaper_decimal);
                AreaWallpaperValue.setText(areaWallpaper_decimal);
                AreaWallpaperRapportValue.setText(areaWallpaperRapport_decimal);
                AreaWallpaperTotalValue.setText(areaTotalWallpaper_decimal);
                StripOfRoomValue.setText(stripRoom_decimal);
                StripOfWallpaperValue.setText(stripWallpaper_decimal);
    
                Label headerLog = new Label("  ");
                GridPane.setHalignment(headerLog, HPos.CENTER);
                Label headerLog_bottom = new Label("  ");
                
                Text numberOfLabel = setTextStyleLogWindow_forTopPrameter("num");                
                Text areaLabel = setTextStyleLogWindow_forTopPrameter("area");                
                Text widthLabel = setTextStyleLogWindow_forTopPrameter("width");                
                Text lengthLabel = setTextStyleLogWindow_forTopPrameter("length");                
                Text heightLabel = setTextStyleLogWindow_forTopPrameter("height");
                
                Text roomLabel = setTextStyleLogWindow_forHeader("[ Room ]");                
                Text windowLabel = setTextStyleLogWindow_forNameSpace("< window >");                
                Text doorLabel = setTextStyleLogWindow_forNameSpace("< door >");
                Text addLabel = setTextStyleLogWindow_forNameSpace("< add area >");
                Text delLabel = setTextStyleLogWindow_forNameSpace("< del area >");
                Text wallpaperLabel = setTextStyleLogWindow_forHeader("[ Wallpaper ]");
                Text roomLabelTotalInfo = setTextStyleLogWindow_forHeader("ROOM");                
                    roomLabelTotalInfo.setFill(Color.BROWN);// setting colour of the text to blue           
                Text roomLabelTotalInfo_total = setTextStyleLogWindow_forTopPrameter("Total");
                Text roomLabelTotalInfo_glue = setTextStyleLogWindow_forTopPrameter("Glue");
                Text roomLabelTotalInfo_notGlue = setTextStyleLogWindow_forTopPrameter("NotGlue");
                Text roomLabelTotalInfo_space = setTextStyleLogWindow_forTopPrameter("Space");
                Text roomLabelTotalInfo_del = setTextStyleLogWindow_forTopPrameter("Del area");
                Text wallpaperLabelTotalInfo = setTextStyleLogWindow_forHeader("WALLPAPER");                
                    wallpaperLabelTotalInfo.setFill(Color.BROWN);
                Text wallpaperLabelTotalInfo_total = new Text();
                wallpaperLabelTotalInfo_total.setFont(Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,15));  
                wallpaperLabelTotalInfo_total.setFill(Color.GREEN);
                wallpaperLabelTotalInfo_total.setStroke(Color.BLACK); 
                wallpaperLabelTotalInfo_total.setStrokeWidth(0.5); 
                wallpaperLabelTotalInfo_total.setText("Total");
                Text wallpaperLabelTotalInfo_glue = new Text();
                wallpaperLabelTotalInfo_glue.setFont(Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,15));  
                wallpaperLabelTotalInfo_glue.setFill(Color.GREEN);
                wallpaperLabelTotalInfo_glue.setStroke(Color.BLACK);
                wallpaperLabelTotalInfo_glue.setStrokeWidth(0.5); 
                wallpaperLabelTotalInfo_glue.setText("Glue");
                Text wallpaperLabelTotalInfo_rapport = new Text();
                wallpaperLabelTotalInfo_rapport.setFont(Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,15));  
                wallpaperLabelTotalInfo_rapport.setFill(Color.GREEN);
                wallpaperLabelTotalInfo_rapport.setStroke(Color.BLACK); 
                wallpaperLabelTotalInfo_rapport.setStrokeWidth(0.5);
                wallpaperLabelTotalInfo_rapport.setText("Rapport");
                Text wallpaperLabelTotalInfo_stripR = new Text();
                wallpaperLabelTotalInfo_stripR.setFont(Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,15));  
                wallpaperLabelTotalInfo_stripR.setFill(Color.GREEN);
                wallpaperLabelTotalInfo_stripR.setStroke(Color.BLACK); 
                wallpaperLabelTotalInfo_stripR.setStrokeWidth(0.5); 
                wallpaperLabelTotalInfo_stripR.setText("Strip R");
                Text wallpaperLabelTotalInfo_stripW = new Text();
                wallpaperLabelTotalInfo_stripW.setFont(Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,15));  
                wallpaperLabelTotalInfo_stripW.setFill(Color.GREEN);
                wallpaperLabelTotalInfo_stripW.setStroke(Color.BLACK); 
                wallpaperLabelTotalInfo_stripW.setStrokeWidth(0.5);
                wallpaperLabelTotalInfo_stripW.setText("Strip W");
                Text totalRollLabelInfo = setTextStyleLogWindow_forHeader("[ Need Roll ]");
                Text totalRollLabel_usingArea = setTextStyleLogWindow_forNameSpace("< using glue area >");
                Text totalRollLabel_usingStrip = setTextStyleLogWindow_forNameSpace("< using perimeter >");
    
                Label roomLabel_numOf = new Label("1");
                Label areaRoom_value = new Label(tfSelectUsedAreaValue.getText());
                Label widthRoom_value = new Label(tfRoomWidth.getText());
                Label lengthRoom_value = new Label(tfRoomLength.getText());
                Label heightRoom_value = new Label(cbRoomHeight.getValue());
                Label windowLabel_numOf = new Label(cbWindowOfNum.getValue());
                Label widthWindow_value = new Label(cbWindowWidth.getValue());
                Label heightWindow_value = new Label(cbWindowHeight.getValue());
                Label doorLabel_numOf = new Label(cbDoorOfNum.getValue());
                Label widthDoor_value = new Label(cbDoorWidth.getValue());
                Label heightDoor_value = new Label(cbDoorHeight.getValue());
                Label addLabel_numOf = new Label(tfAddOfNum.getText());
                Label widthAdd_value = new Label(tfAddWidth.getText());
                Label heightAdd_value = new Label(tfAddHeight.getText());
                Label delLabel_numOf = new Label(tfDelOfNum.getText());
                Label widthDel_value = new Label(tfDelWidth.getText());
                Label heightDel_value = new Label(tfDelHeight.getText());
                Label widthWallpaper_value = new Label(cbWallpaperWidth.getValue());
                Label lengthWallpaper_value = new Label(cbWallpaperLength.getValue());
                Label rapportWallpaper_value = new Label(cbWallpaperRapport.getValue());
                Label totalRollValue_usingArea = new Label();
                totalRollValue_usingArea.setText(roll_decimal);
                Label totalRollValue_usingStrip = new Label();
                totalRollValue_usingStrip.setText(roll_bruteforce_decimal);
                
                Button btnSaveLog = new Button("Save");                              
                btnSaveLog.setMinWidth(100);
                GridPane.setHalignment(btnSaveLog, HPos.CENTER);
                
                Button btnSendLog = new Button("Send");                              
                btnSendLog.setMinWidth(100);
                GridPane.setHalignment(btnSendLog, HPos.CENTER);
                
                Button btnCloseLog = new Button("Close");                              
                btnCloseLog.setMinWidth(100);
                GridPane.setHalignment(btnCloseLog, HPos.CENTER);
                btnCloseLog.setOnAction(new EventHandler<ActionEvent>() { 
                    public void handle(ActionEvent event) {
                        newWindow.close();
                    }
                });
            
                logWindow.add(headerLog, 0,0);
                logWindow.add(numberOfLabel, 3,1);
                logWindow.add(areaLabel, 4,1);
                logWindow.add(widthLabel, 5,1);
                logWindow.add(lengthLabel, 6,1);
                logWindow.add(heightLabel, 7,1);
                
                logWindow.add(roomLabel, 1,2, 2,1);
                logWindow.add(roomLabel_numOf, 3,2);
                logWindow.add(areaRoom_value, 4,2);                
                logWindow.add(widthRoom_value, 5,2);                
                logWindow.add(lengthRoom_value, 6,2);               
                logWindow.add(heightRoom_value, 7,2);                
                
                logWindow.add(windowLabel, 2,3);
                logWindow.add(windowLabel_numOf, 3,3);
                logWindow.add(windowAreaValue, 4,3);
                logWindow.add(widthWindow_value, 5,3);
                logWindow.add(heightWindow_value, 7,3);
                
                logWindow.add(doorLabel, 2,4);
                logWindow.add(doorLabel_numOf, 3,4);
                logWindow.add(doorAreaValue, 4,4);
                logWindow.add(widthDoor_value, 5,4);                
                logWindow.add(heightDoor_value, 7,4);
                
                
                logWindow.add(addLabel, 2,5);
                logWindow.add(addLabel_numOf, 3,5);
                logWindow.add(addAreaValue, 4,5);
                logWindow.add(widthAdd_value, 5,5);
                logWindow.add(heightAdd_value, 7,5);
                
                logWindow.add(delLabel, 2,6);
                logWindow.add(delLabel_numOf, 3,6);
                logWindow.add(delAreaValue, 4,6);
                logWindow.add(widthDel_value, 5,6);
                logWindow.add(heightDel_value, 7,6);
                
                logWindow.add(wallpaperLabel, 1,7, 2,1);
                logWindow.add(widthWallpaper_value, 5,7);
                logWindow.add(lengthWallpaper_value, 6,7);
                logWindow.add(rapportWallpaper_value, 3,7);
                logWindow.add(areaWallpaperValue, 4,7);
                
                
                logWindow.add(piechartLog, 1,8, 8,1);
                
                logWindow.add(roomLabelTotalInfo, 1,9, 2,1);
                logWindow.add(roomLabelTotalInfo_total, 2,10);
                logWindow.add(AreaTotalValue, 2,11);
                logWindow.add(roomLabelTotalInfo_glue, 3,10);
                logWindow.add(AreaGlueValue, 3,11);
                logWindow.add(roomLabelTotalInfo_notGlue, 4,10);
                logWindow.add(AreaNotGlueValue, 4,11);
                logWindow.add(roomLabelTotalInfo_space, 5,10);
                logWindow.add(spaceAreaValue, 5,11);
                logWindow.add(roomLabelTotalInfo_del, 6,10);
                logWindow.add(delAreaValue_2, 6,11);
                
                logWindow.add(wallpaperLabelTotalInfo, 1,13, 2,1);
                logWindow.add(wallpaperLabelTotalInfo_total, 2,14);
                logWindow.add(AreaWallpaperValue, 2,15);
                logWindow.add(wallpaperLabelTotalInfo_glue, 3,14);
                logWindow.add(AreaWallpaperTotalValue, 3,15);
                logWindow.add(wallpaperLabelTotalInfo_rapport, 4,14);
                logWindow.add(AreaWallpaperRapportValue, 4,15);
                logWindow.add(wallpaperLabelTotalInfo_stripR, 5,14);
                logWindow.add(StripOfRoomValue, 5,15);
                logWindow.add(wallpaperLabelTotalInfo_stripW, 6,14);
                logWindow.add(StripOfWallpaperValue, 6,15);
                
                logWindow.add(totalRollLabelInfo, 1,17, 2,1);
                logWindow.add(totalRollLabel_usingArea, 2,18, 2,1);
                logWindow.add(totalRollValue_usingArea, 5,18);
                logWindow.add(totalRollLabel_usingStrip, 2,19, 2,1);
                logWindow.add(totalRollValue_usingStrip, 5,19);
                
                logWindow.add(btnSaveLog, 2,21);
                logWindow.add(btnSendLog, 3,21, 2,1);
                logWindow.add(btnCloseLog, 6,21, 2,1);
                
                logWindow.add(headerLog_bottom, 0,22);
                
                Scene secondScene = new Scene(logWindow, 550, 700);
 
                newWindow.setTitle("Second Stage");
                newWindow.setScene(secondScene);
               
                newWindow.setX(primaryStage.getX() + 50);
                newWindow.setY(primaryStage.getY() + 0);
 
                newWindow.show();
            }
        });
        
        Button btnExit = new Button ("Exit");                              
        btnExit.setMaxWidth(100);
        GridPane.setHalignment(btnExit, HPos.LEFT);        
        btnExit.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent event) {
 
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setHeaderText("Are You sure want to exit?");
                alert.setContentText("Click \"ok\" to exit the application.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    System.exit(0);
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            }
        });
////////////////////////////////////////////////////////////////////////////////
////////////////////end button//////////////////////////////////////////////////

//------------------------------------------------------------------------------
Menu mnFile = new Menu("File");
// menu items creation
MenuItem mif1 = new MenuItem("New Pattern");
MenuItem mif2 = new MenuItem("Open Pattern");
MenuItem mif3 = new MenuItem("Save as");
MenuItem mif4 = new MenuItem("Print to HTML");
MenuItem mif5 = new MenuItem("Exit\t\t\t\t\tAlt-F4");

// Adding menu items
mnFile.getItems().add(mif1);
mnFile.getItems().add(mif2);
mnFile.getItems().add(mif3);
mnFile.getItems().add(mif4);
mnFile.getItems().add(mif5);

Menu mnEdit = new Menu("Edit");
// menu items creation
MenuItem mie1 = new MenuItem("Undo\t\tCtrl-Z");
MenuItem mie2 = new MenuItem("Redo\t\tCtrl-Y");
MenuItem mie3 = new MenuItem("Cut\t\t\tCtrl-X");
MenuItem mie4 = new MenuItem("Copy\t\tCtrl-C");
MenuItem mie5 = new MenuItem("Paste\t\tCtrl-V");
MenuItem mie6 = new MenuItem("Delete\t\tDelete");
// Adding menu items
mnEdit.getItems().add(mie1);
mnEdit.getItems().add(mie2);
mnEdit.getItems().add(mie3);
mnEdit.getItems().add(mie4);
mnEdit.getItems().add(mie5);
mnEdit.getItems().add(mie6);

Menu mnView = new Menu("View");
// menu items creation
MenuItem miv1 = new MenuItem("Flipkart");
MenuItem miv2 = new MenuItem("Myntra");
MenuItem miv3 = new MenuItem("Amazon");
MenuItem miv4 = new MenuItem("Club factory");
// Adding menu items
mnView.getItems().add(miv1);
mnView.getItems().add(miv2);
mnView.getItems().add(miv3);
mnView.getItems().add(miv4);

Menu mnHelp = new Menu("Help");
// menu items creation
MenuItem mih1 = new MenuItem("Online Docs and Support");
MenuItem mih2 = new MenuItem("Check for Updates");
MenuItem mih3 = new MenuItem("Start Page");
MenuItem mih4 = new MenuItem("About");
// Adding menu items
mnHelp.getItems().add(mih1);
mnHelp.getItems().add(mih2);
mnHelp.getItems().add(mih3);
mnHelp.getItems().add(mih4);

// menubar creation
MenuBar mb = new MenuBar();
// Adding Menubar
mb.getMenus().addAll(mnFile, mnEdit, mnView, mnHelp);
//------------------------------------------------------------------------------
    
                root.add(mb, 0,0,9,1);
                root.add(roomParam, 0,3, 3,1);
                root.add(cbSelectUsedArea, 3, 3);
                root.add(roomHeight, 5, 3);
		root.add(cbRoomHeight, 6, 3);
                root.add(circleRoom, 7, 3);
                root.add(choiceNameAreaVariable_Default, 1,4, 6,1);
                
		root.add(windowParam, 2, 5);
		root.add(windowWidth, 1, 6);		
		root.add(cbWindowWidth,  2, 6);
		root.add(windowHeight, 3, 6);		
		root.add(cbWindowHeight, 4, 6);
		root.add(windowOfNum, 5, 6);
		root.add(cbWindowOfNum, 6, 6);
                root.add(circleWindow, 7, 6);
                
		root.add(doorParam, 2, 7);
		root.add(doorWidth, 1, 8);		
		root.add(cbDoorWidth,  2, 8);
		root.add(doorHeight, 3, 8);		
		root.add(cbDoorHeight, 4, 8);
		root.add(doorOfNum, 5, 8);
		root.add(cbDoorOfNum, 6, 8);
                root.add(circleDoor, 7, 8);
                
		root.add(checkAddField, 1, 9);
                root.add(addParam, 2, 9);
                root.add(infoAddField, 0, 10, 7, 1);
                
		root.add(checkDelField, 1, 11);
                root.add(delParam, 2, 11);
                root.add(infoDelField, 0, 12, 7, 1);
                
		root.add(wallpaperParam, 0,13, 3,1);
		root.add(wallpaperWidth, 1, 14);		
		root.add(cbWallpaperWidth,  2, 14);
		root.add(wallpaperLength, 3, 14);		
		root.add(cbWallpaperLength, 4, 14);
		root.add(wallpaperRapport, 5, 14);
		root.add(cbWallpaperRapport, 6, 14);
                root.add(circleWallpaper, 7, 14);
                		
		root.add(btnCalc, 4, 17);
                root.add(btnViewLog, 2, 26);
                root.add(btnDiscard, 3, 26);
                root.add(btnExit, 4, 26);
                
                root.add(piechart, 5, 17, 3, 10);		
                
                root.add(resultAreaTotal, 2, 19);
                root.add(resultAreaTotalValue, 3, 19);
                root.add(resultAreaWallpaper, 4, 19);
                root.add(resultAreaWallpaperValue, 5, 19);
                root.add(resultAreaGlue, 2, 20);
                root.add(resultAreaGlueValue, 3, 20);
                root.add(resultAreaWallpaperRapport, 4, 20);
                root.add(resultAreaWallpaperRapportValue, 5, 20);
                root.add(resultAreaNotGlue, 2, 21);
                root.add(resultAreaNotGlueValue, 3, 21);                
                root.add(resultAreaWallpaperTotal, 4, 21);
                root.add(resultAreaWallpaperTotalValue, 5, 21);
                root.add(resultTotal, 2, 23);
                root.add(resultTotalValueRoll, 3, 23);
                
		primaryStage.setTitle("Needroll");
                primaryStage.setScene(scene);
		
		primaryStage.show();
	}

//////////////////////////////////////////////////////////////////////////////
public static void main(String[] args) {
		launch(args);
	}
////////////////////////////////////////////////////////////////////////////////    
    
    double valuePieChart(double val){ return ((val*100)/areaGlue); }    
    private ObservableList<Data> getChartData() {    
	ObservableList<Data> list = FXCollections.observableArrayList();    
	list.addAll(new PieChart.Data("Glue", valuePieChart(areaGlue)), 
				new PieChart.Data("Space", valuePieChart(areaPieChartWindowAndDoor)), 
				new PieChart.Data("Delete", valuePieChart(areaPieChartDel)) );    
	return list;
    }

    public Text setTextStyle_forNameParameter(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,15));  
            roomParam.setFill(Color.BLACK);
            roomParam.setStroke(Color.GRAY);
            roomParam.setStrokeWidth(0.5); 
            roomParam.setText(str);
        return roomParam;
    }
        
    public Text setTextStyle_forHeader(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,20));  
            roomParam.setFill(Color.ORANGE);
            roomParam.setStroke(Color.BLACK);
            roomParam.setStrokeWidth(0.5); 
            roomParam.setText(str);
        return roomParam;
    }
    
    public Text setTextStyle_forNameSpace(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,17));  
            roomParam.setFill(Color.GREEN);
            roomParam.setStroke(Color.GRAY);
            roomParam.setStrokeWidth(0.5);
            roomParam.setText(str);
        return roomParam;
    }

    public Text setTextStyle_forTotalInfo(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,15));  
            roomParam.setFill(Color.BLACK);
            //roomParam.setStroke(Color.BLACK); 
            //roomParam.setStrokeWidth(0.5); 
            roomParam.setText(str);
        return roomParam;
    }
    
    public Text setTextStyle_forInfo(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,15));  
            roomParam.setFill(Color.GRAY);
            //roomParam.setStroke(Color.BLACK); 
            //roomParam.setStrokeWidth(0.5); 
            roomParam.setText(str);
        return roomParam;
    }
    
    public Text setTextStyle_forResult(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,17));  
            roomParam.setFill(Color.GREEN);
            roomParam.setStroke(Color.GRAY); 
            roomParam.setStrokeWidth(0.5);
            roomParam.setText(str);
        return roomParam;
    }
    
    public Text setTextStyleLogWindow_forTopPrameter(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,15));  
            roomParam.setFill(Color.GREEN);
            roomParam.setStroke(Color.BLACK);
            roomParam.setStrokeWidth(0.5); 
            roomParam.setText(str);
        return roomParam;
    }
    
    public Text setTextStyleLogWindow_forHeader(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,17));  
            roomParam.setFill(Color.ORANGE);
            roomParam.setStroke(Color.BLACK);
            roomParam.setStrokeWidth(0.5); 
            roomParam.setText(str);
        return roomParam;
    }
    
    public Text setTextStyleLogWindow_forNameSpace(String str){
        Text roomParam = new Text();
            roomParam.setFont(Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,17));  
            roomParam.setFill(Color.GREEN);
            roomParam.setStroke(Color.GRAY);
            roomParam.setStrokeWidth(0.5); 
            roomParam.setText(str);
        return roomParam;
    }
    
    public void showWarning_incorrectEnter(){
        Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid value entered");
            alert.setContentText("Value must not contain literal and special character\nCareful with the next step!");

            alert.showAndWait();
    }
    
} // end


