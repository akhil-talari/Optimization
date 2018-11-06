package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import static application.Scatter2DChart.slider_max;

public class Main extends Application {

	Label l;
	Group grp;
	Group grp_sim;
	Group grp_img;
	File file1;
	HBox hb=new HBox();
	public static HashMap<String,Color> areas;
	public static Node mynode = null;

	String months_simulate ;
	String months_conn_simulate;

	ImageView imgView, imgview_sim;
	Line currentLine;
	XSSFSheet sheet, sheet_conn;
	XSSFWorkbook workbook,workbook_conn;
	public static TextField Manhours,NoofWorkers,NoofHours,Hookup_MCrew,Hookup_MCrewPM,Install_SPMT,
	pcost,ecost,icost,NoofCranes,NoofCranes1,NoofCranes2,NoofCranes3,NoofCranes4,pMH,eMH,iMH,
	Manhours1,NoofWorkers1,NoofHours1;
	public static ListView<MyObject> list, list2;
	public static ListView<String> list1,list3;
	int rect_no = 0, rows = 0,rows_conn=0;
	double orgSceneX, orgSceneY;
	double orgTranslateX, orgTranslateY;
	public static ArrayList<Rectangle> module = new ArrayList<Rectangle>();
	public static HashMap<String, MyConn> connection = new HashMap<String, MyConn>();
	public int conn_no = 1;
	public static HashMap<Integer, MyObject> module_label = new HashMap<Integer, MyObject>();
	public static TableColumn<AreaMS,String> AreaName, AreaDL;
	public static TableView<AreaMS> Area_MStones;
	public static HashMap<Integer, String> ZoneDirection = new HashMap<Integer, String>();

	//NOV5
	public static SplitPane splitPane_sim;
	public static Tab Simulate;
	public static Slider opacityLevel;
	//NOV5

	Rectangle rect_sim;
	Timeline timeline, timeline2;
	Duration timepoint, timepoint2;
	Duration pause;
	int countttt = 0;
	List<Line> trackline = new ArrayList<>();
	HashMap<Rectangle, Label> rect_ani = new HashMap<Rectangle, Label>();
	Multimap<String, Rectangle> install_ani = HashMultimap.create();
	Multimap<String, Line> conn_ani =  HashMultimap.create();
	Multimap<Double, String> Module_under = HashMultimap.create();
	HashMap<Rectangle, Label> trackrect = new HashMap<Rectangle, Label>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage primaryStage) {
		try {
			
 			TabPane tabs = new TabPane();
			Tab project_Mod = new Tab();
			Tab Calc_Mod = new Tab();
			Simulate = new Tab(); //NOV5
			Tab Optimize = new Tab();
			
			project_Mod.setText("Project Modeling");
			Calc_Mod.setText("Calculation Modeling");
			Simulate.setText("Simulate");
			Optimize.setText("Optimize");
			
			tabs.getTabs().add(project_Mod);
			tabs.getTabs().add(Calc_Mod);
			tabs.getTabs().add(Optimize);
			tabs.getTabs().add(Simulate);

			
			BorderPane root = new BorderPane(tabs);

			Scene scene = new Scene(root, 800, 600);		    
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.show();
			
//*****************************************Simulate************************************************************		

			final ToggleGroup group = new ToggleGroup();

			RadioButton rb1 = new RadioButton("Install Schedule");
			rb1.setToggleGroup(group);
			rb1.setSelected(true);
			rb1.setUserData("Install Schedule");

			RadioButton rb2 = new RadioButton("Hookup Schedule");
			rb2.setToggleGroup(group);
			rb2.setUserData("Hookup Schedule");

			RadioButton rb3 = new RadioButton("Overall Schedule");
			rb3.setToggleGroup(group);
			rb3.setUserData("Overall Schedule");

			Rectangle rec_constructed = new Rectangle(20, 20, 20, 20);
			rec_constructed.setFill(Color.BLUE);
			Rectangle rec_under = new Rectangle(20, 20, 20, 20);
			rec_under.setFill(Color.RED);

			Line line_const = new Line(rec_constructed.getX(), rec_constructed.getY()+20,
					rec_constructed.getX() + 40, rec_constructed.getY()+20);
			line_const.setStroke(Color.BLACK);
			line_const.setStrokeWidth(2);

			Line line_const_d = new Line(rec_constructed.getX(), rec_constructed.getY()+20,
					rec_constructed.getX() + 40, rec_constructed.getY()+20);
			line_const_d.setStroke(Color.GREEN);
			line_const_d.setStrokeWidth(6);

			Line line_under = new Line(rec_constructed.getX(), rec_constructed.getY()+20,
					rec_constructed.getX() + 40, rec_constructed.getY()+20);
			line_under.setStroke(Color.RED);
			line_under.setStrokeWidth(2);

			Line line_under_double = new Line(rec_constructed.getX(), rec_constructed.getY()+20,
					rec_constructed.getX() + 40, rec_constructed.getY()+20);
			line_under_double.setStroke(Color.RED);
			line_under_double.setStrokeWidth(6);

//			Line line = new Line(rectangle.getX(), rectangle.getY()+30,
//					rectangle.getX() + 50, rectangle.getY()+30);
//			line.setStroke(Color.BLACK);
//			line.setStrokeWidth(5);

			opacityLevel = new Slider(29, 55, 1); //NOV5
			final Label opacityCaption = new Label("Navigation bar:");
			final Label opacityValue = new Label("Current view: month ");
			final Label opacityValue_chnage = new Label(Double.toString(opacityLevel.getValue()));

			final Label under = new Label("Module Under installation:");
			final Label constructed = new Label("Module Installed:");
			//final Label future = new Label("To be constructed: Invisible");
			final Label under_conn = new Label("Hookup Under Construction: Single");
			final Label under_conn_double = new Label("Hookup Under Construction: Double");
			final Label constructed_conn = new Label("Hookup Completed: Single");
			final Label constructed_conn_d = new Label("Hookup Completed: Double");

			final Label modules_under = new Label("Modules Under Installation:");
//			final Label modules_text = new Label("");
			TextArea modules_text = new TextArea();

			opacityLevel.setShowTickMarks(true);
			opacityLevel.setShowTickLabels(true);
			opacityLevel.setMajorTickUnit(1f);
			opacityLevel.setBlockIncrement(1f);

			/*RadioButton rb3 = new RadioButton("Install Resource Variation");
			rb3.setToggleGroup(group);
			rb3.setUserData("Install Resource Variation");

			RadioButton rb4 = new RadioButton("Hookup Resource Variation");
			rb4.setToggleGroup(group);
			rb4.setUserData("Hookup Resource Variation");
			*/

			Button reset = new Button("Reset");
			Button play = new Button("Auto-Play");
			/*Sim_install.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					if(group.getSelectedToggle().getUserData().equals("Install Schedule")){
						GanttChart.start_GanttChart(1);
					}
					else if(group.getSelectedToggle().getUserData().equals("Hookup Schedule")){
						GanttChart.start_GanttChart(2);
					}
					else if(group.getSelectedToggle().getUserData().equals("Install Resource Variation")){

						//Histogram.start_Hist(1);
					}
					else if(group.getSelectedToggle().getUserData().equals("Hookup Resource Variation")){

						//Histogram.start_Hist(2);
					}

				}

			});*/

			GridPane grid44 = new GridPane();
			grid44.setVgap(4);
			grid44.setPadding(new Insets(1, 1, 1, 1));

			//VBox sim = new VBox();
			//sim.setPadding(new Insets(20, 20, 20, 20));
			//sim.setSpacing(10);

			//sim.getChildren().add(rb2);
			grid44.add(opacityCaption, 0,1);
			grid44.add(opacityLevel, 0, 2);
			grid44.add(opacityValue, 0, 3);
			grid44.add(opacityValue_chnage, 1, 3);
			grid44.add(under, 0, 4);
			grid44.add(rec_under, 1, 4);
			grid44.add(constructed, 0, 5);
			grid44.add(rec_constructed, 1, 5);
			grid44.add(under_conn, 0, 6);
			grid44.add(line_under, 1, 6);
			grid44.add(under_conn_double, 0,7);
			grid44.add(line_under_double,1,7);

			grid44.add(constructed_conn, 0, 8);
			grid44.add(line_const, 1, 8);
			grid44.add(constructed_conn_d, 0, 9);
			grid44.add(line_const_d, 1, 9);

			grid44.add(rb1, 0, 10);
			grid44.add(rb2, 0, 11);
			grid44.add(rb3, 0, 12);
			grid44.add(reset, 0, 13);
			grid44.add(play, 0, 14);
			grid44.add(modules_under, 0, 15);
			grid44.add(modules_text,0,16);
			modules_text.setMaxSize(190, 80);
			modules_text.setWrapText(true);

			// scrollpane for rightside of splitpane
			ScrollPane scrollPane_sim = new ScrollPane();
			scrollPane_sim.setFitToHeight(true);
			scrollPane_sim.setFitToWidth(true);
			scrollPane_sim.setHbarPolicy(ScrollBarPolicy.ALWAYS);
			scrollPane_sim.setVbarPolicy(ScrollBarPolicy.ALWAYS);

			Pane leftPane1 = new Pane();
			leftPane1.getChildren().add(grid44);



			grp_sim = new Group();

			grp_img = new Group();
			//scrollPane_sim.setContent(imgview_sim);
			scrollPane_sim.setContent(new Group(grp_sim));

			//grp_sim.getChildren().addAll(imgview_sim);


			splitPane_sim = new SplitPane(); //NOV5
			splitPane_sim.getItems().addAll(leftPane1, scrollPane_sim);
			splitPane_sim.setDividerPositions(0.30);
			leftPane1.maxWidthProperty().bind(
					splitPane_sim.widthProperty().multiply(0.28));
			splitPane_sim.prefHeightProperty().bind(scene.heightProperty());
			Simulate.setContent(splitPane_sim);

			opacityLevel.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov,
									Number old_val, Number new_val) {
					//System.out.println(Math.round(new_val.doubleValue()));
					opacityValue_chnage.setText(String.format("%d", Math.round((Double) new_val)));
					Double d = Double.valueOf(opacityValue_chnage.getText());
					//display_remove(d);
					if(group.getSelectedToggle().getUserData().equals("Install Schedule")) {
						for (Map.Entry<Rectangle, Label> entry : trackrect.entrySet()) {
							grp_sim.getChildren().removeAll(entry.getKey(), entry.getValue());
						}
						display(d);
						String p = "";
						Collection<String> mo = Module_under.get(d);
						for (String s : mo){
							s = s.substring(0, s.length() - 2);
							p = p + s + ",";
						}
						modules_text.setText(p);
					}

					else if(group.getSelectedToggle().getUserData().equals("Hookup Schedule")) {
						for(int i = 0; i < trackline.size(); i++){
							grp_sim.getChildren().remove(trackline.get(i));
						}
						display_conn(d);
					}

					else if(group.getSelectedToggle().getUserData().equals("Overall Schedule")) {
						for (Map.Entry<Rectangle, Label> entry : trackrect.entrySet()) {
							grp_sim.getChildren().removeAll(entry.getKey(), entry.getValue());
						}
						for(int i = 0; i < trackline.size(); i++){
							grp_sim.getChildren().remove(trackline.get(i));
						}
						display(d);
						String p = "";
						Collection<String> mo = Module_under.get(d);
						for (String s : mo){
							s = s.substring(0, s.length() - 2);
							p = p + s + ",";
						}
						modules_text.setText(p);
						display_conn(d);
					}
				}
			});


			reset.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					//if(group.getSelectedToggle().getUserData().equals("Install Schedule")){}
					grp_sim.getChildren().clear();
					grp_sim.getChildren().addAll(imgview_sim);
					Double d = Double.valueOf(opacityValue_chnage.getText());
//					if(Animation.Status.RUNNING == timeline.getStatus())
//					{timeline.stop();}
					modules_text.setText("");
					try{timeline.stop();}catch (NullPointerException ex){}
				}
			});

			play.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					//if(group.getSelectedToggle().getUserData().equals("Install Schedule")){}
					grp_sim.getChildren().clear();
					grp_sim.getChildren().addAll(imgview_sim);
					timeline = new Timeline();
					timepoint = Duration.ZERO;
					pause = Duration.seconds(1);
					display_animation(55);
					display_animation_conn(55);
					Set<String> keys = install_ani.keySet();
					Set<String> sorted_modules= new TreeSet(keys);

					Set<String> keys1 = conn_ani.keySet();
					Set<String> sorted_connection= new TreeSet(keys1);

					for (String keyprint : sorted_modules) {
						//System.out.println(keyprint);
						Collection<Rectangle> values = install_ani.get(keyprint);
						for (Rectangle value : values) {
							timepoint = timepoint.add(pause);
							KeyFrame keyFrame = new KeyFrame(timepoint, rh -> grp_sim.getChildren().addAll(value, rect_ani.get(value)));
							timeline.getKeyFrames().add(keyFrame);
						}
						if (sorted_connection.contains(keyprint)) {
							Collection<Line> values1 = conn_ani.get(keyprint);
							for (Line value1 : values1) {
								timepoint = timepoint.add(pause);
								KeyFrame keyFrame = new KeyFrame(timepoint, rh -> grp_sim.getChildren().add(value1));
								timeline.getKeyFrames().add(keyFrame);
								sorted_connection.remove(keyprint);
							}
						}
					}
					//System.out.println(sorted_connection);
					Set<String> sorted_connection1= new TreeSet(sorted_connection);
					for (String keyprint : sorted_connection1) {
						Collection<Line> values2 = conn_ani.get(keyprint);
						for (Line value2 : values2) {
							timepoint = timepoint.add(pause);
							KeyFrame keyFrame = new KeyFrame(timepoint, rh -> grp_sim.getChildren().add(value2));
							timeline.getKeyFrames().add(keyFrame);
							sorted_connection.remove(keyprint);
						}
					}
					//System.out.println(timeline.getTotalDuration());
					timeline.play();
					//System.out.println(timeline.getTotalDuration());
				}});

			/*play.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					//if(group.getSelectedToggle().getUserData().equals("Install Schedule")){}
					grp_sim.getChildren().clear();
					grp_sim.getChildren().addAll(imgview_sim);
					timeline = new Timeline();
					timeline2 = new Timeline();
					timepoint = Duration.ZERO ;
					timepoint2 = Duration.ZERO ;
					pause = Duration.seconds(1);
                    display_animation(55);
                    display_animation_conn(55);
                    for (Map.Entry< Rectangle, Label> entry : rect_ani.entrySet()) {
                        timepoint = timepoint.add(pause);
                        KeyFrame keyFrame = new KeyFrame(timepoint, rh -> grp_sim.getChildren().addAll(entry.getKey(), entry.getValue()));
                        timeline.getKeyFrames().add(keyFrame);
                    }

					for (Map.Entry< Line, String> entry : conn_ani.entrySet()) {
						timepoint = timepoint.add(pause);
						KeyFrame keyFrame = new KeyFrame(timepoint, rh -> grp_sim.getChildren().addAll(entry.getKey()));
						timeline.getKeyFrames().add(keyFrame);
					}
                    System.out.println(timeline.getTotalDuration());
                    timeline.play();
				}
			});*/




//*****************************************Optimize************************************************************
			Button btn_opt = new Button("Optimize");
		//	Button ros_opt = new Button("ROS-Optimize");
			VBox vopt = new VBox();
			vopt.setSpacing(10);
			vopt.setPadding(new Insets(20,20,20,20));
			vopt.getChildren().addAll(btn_opt);
			
			btn_opt.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					
					try {
						try {
							GA_Main.run(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			});
		/*	ros_opt.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					
					try {
						try {
							GA_Main.run(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			});
			
	*/
			Optimize.setContent(vopt);
			
//*******************************************Calculation Modeling*************************************************
	
			String latex = "Cost\\small{(\\$)}=\\frac {cost}{\\mbox{crew x hr}} \\mbox{\\frac{(\\$)}{(crew.hr)}}  \\mbox{x Number of crew\\small{ (crew)} x} \\frac{\\mbox{Number of working hours}}{day} \\mbox{\\frac{(hr)}{(day)}} \\mbox{x duration \\small{(day)}}";
			
			TeXFormula tex = new TeXFormula(latex);
			java.awt.Image awtImage = tex.createBufferedImage(TeXConstants.STYLE_DISPLAY, 20, java.awt.Color.BLACK, null);
			Image fxImage = SwingFXUtils.toFXImage((BufferedImage) awtImage, null);
			ImageView view = new ImageView(fxImage);

			
			pcost = new TextField("300");
			pMH = new TextField("10.25");
			ecost = new TextField("162");
			eMH = new TextField("5.25");
			icost = new TextField("137");
			iMH = new TextField("4.25");
			

			Label cost = new Label("Piping Cost:");
			cost.setFont(Font.font(null, FontWeight.BOLD, 13));
			Label cost1 = new Label("Electrical Cost:");
			cost1.setFont(Font.font(null, FontWeight.BOLD, 13));
			Label cost2 = new Label("Instrumentation Cost:");
			cost2.setFont(Font.font(null, FontWeight.BOLD, 13));
			
			GridPane cost_grid1 = new GridPane();
			cost_grid1.setVgap(10);
			cost_grid1.setPadding(new Insets(5, 5, 5, 5));
			cost_grid1.add(view,0,0);
			
			GridPane cost_grid = new GridPane();
			cost_grid.setVgap(10);
			cost_grid.setPadding(new Insets(5, 5, 5, 5));
			cost_grid.add(cost, 0, 1);
			cost_grid.add(new Label("Piping Cost/Crew/hour:	"), 0, 2);
			cost_grid.add(pcost, 2, 2);
			cost_grid.add(new Label("Piping Manhours/Crew/hour:	"), 0, 3);
			cost_grid.add(pMH, 2, 3);
			cost_grid.add(cost1, 0, 4);
			cost_grid.add(new Label("Electrical Cost/Crew/hour:	"), 0, 5);
			cost_grid.add(ecost, 2, 5);
			cost_grid.add(new Label("Electrical Manhours/Crew/hour: "), 0, 6);
			cost_grid.add(eMH, 2, 6);
			cost_grid.add(cost2,0,7);
			cost_grid.add(new Label("Instrumentation/Crew/hour: "), 0, 8);
			cost_grid.add(icost, 2, 8);
			cost_grid.add(new Label("Instrumentation Manhours/Crew/hour:	"), 0, 9);
			cost_grid.add(iMH, 2, 9);
			
			VBox v_cost = new VBox();
			v_cost.setPadding(new Insets(5,5,5,5));
			v_cost.setSpacing(10);
			v_cost.getChildren().add(cost_grid1);
			v_cost.getChildren().add(cost_grid);
			
			TitledPane cost_TP = new TitledPane();
			cost_TP.setAnimated(true);
			cost_TP.setText("COST");
			cost_TP.setContent(v_cost);
			cost_TP.setExpanded(true);

			Accordion cost_accordion = new Accordion();
			cost_accordion.getPanes().add(cost_TP);
			
			String latex1 = "Duration\\small{(day)}=\\frac {\\mbox{Weight\\small{(ton)} x }  \\mbox{\\frac{\\mbox{Manhours}} {\\mbox{ton}} \\frac{(hr)}{(ton)}}}{\\mbox{\\frac{\\mbox{Number of workers}} {\\mbox{crew}} \\frac{(1)}{(crew)} x }  \\mbox{Number of crews \\small{(crew)} x }   \\mbox{\\frac{\\mbox{Number of working hours}} {\\mbox{worker x day}}\\frac{(hr)}{(day)}}}";
			

			TeXFormula tex1 = new TeXFormula(latex1);
			java.awt.Image awtImage1 = tex1.createBufferedImage(TeXConstants.STYLE_DISPLAY, 20, java.awt.Color.BLACK, null);
			Image fxImage1 = SwingFXUtils.toFXImage((BufferedImage) awtImage1, null);
			ImageView view1 = new ImageView(fxImage1);
			
			NoofWorkers = new TextField("10");
			NoofHours = new TextField("10");
			Manhours = new TextField("3");
			NoofWorkers1 = new TextField("10");
			NoofHours1 = new TextField("10");
			Manhours1 = new TextField("3");
			
			Label single = new Label("Single Connection:");
			single.setFont(Font.font(null, FontWeight.BOLD, 13));
			Label Dou = new Label("Double Connection:");
			Dou.setFont(Font.font(null, FontWeight.BOLD, 13));
			
			GridPane Duration_grid1 = new GridPane();
			Duration_grid1.setVgap(10);
			Duration_grid1.setPadding(new Insets(5, 5, 5, 5));
			Duration_grid1.add(view1,0,0);
			
			GridPane Duration_grid = new GridPane();
			Duration_grid.setVgap(10);
			Duration_grid.setPadding(new Insets(5, 5, 5, 5));
			Duration_grid.add(single, 0, 1);
			Duration_grid.add(new Label("Maximum Number of Workers/Crew: "), 0, 2);
			Duration_grid.add(NoofWorkers, 2, 2);
			Duration_grid.add(new Label("Maximun Number of Hours/Day/Person: "), 0, 3);
			Duration_grid.add(NoofHours, 2, 3);
			Duration_grid.add(new Label("Manhours/Ton: "), 0, 4);
			Duration_grid.add(Manhours, 2, 4);
			Duration_grid.add(Dou, 0, 5);
			Duration_grid.add(new Label("Maximum Number of Workers/Crew: "), 0, 6);
			Duration_grid.add(NoofWorkers1, 2, 6);
			Duration_grid.add(new Label("Maximun Number of Working Hours/Day x Person: "), 0, 7);
			Duration_grid.add(NoofHours1, 2, 7);
			Duration_grid.add(new Label("Manhours/Ton: "), 0, 8);
			Duration_grid.add(Manhours1, 2, 8);
			
			VBox v_dur = new VBox();
			v_dur.setPadding(new Insets(5,5,5,5));
			v_dur.setSpacing(10);
			v_dur.getChildren().add(Duration_grid1);
			v_dur.getChildren().add(Duration_grid);
			
			TitledPane Duration_TP = new TitledPane();
			Duration_TP.setAnimated(true);
			Duration_TP.setText("DURATION");
			Duration_TP.setContent(v_dur);
			Duration_TP.setExpanded(true);

			Accordion Duration_accordion = new Accordion();
			Duration_accordion.getPanes().add(Duration_TP);
			
			Hookup_MCrew = new TextField("3");
			Hookup_MCrewPM = new TextField("20");
			Install_SPMT = new TextField("700");
			NoofCranes = new TextField("1");
			NoofCranes1 = new TextField("1");
			NoofCranes2 = new TextField("1");
			NoofCranes3 = new TextField("1");
			NoofCranes4 = new TextField("1");
			
			GridPane Constraint_grid = new GridPane();
			Constraint_grid.setVgap(10);
			Constraint_grid.setPadding(new Insets(5, 5, 5, 5));
			Constraint_grid.add(new Label("Maximum Number of Crews/Connection:	"), 0, 0);
			Constraint_grid.add(Hookup_MCrew, 2, 0);
			Constraint_grid.add(new Label("Maximum Number of Crews/Month:	"), 0, 1);
			Constraint_grid.add(Hookup_MCrewPM, 2, 1);
			Constraint_grid.add(new Label("Maximum SPMT/Month:	"), 0, 2);
			Constraint_grid.add(Install_SPMT, 2, 2);
			Constraint_grid.add(new Label("Number of  1350 ton Cranes/Month:	"), 0, 3);
			Constraint_grid.add(NoofCranes, 2, 3);
			Constraint_grid.add(new Label("Number of  600 ton Cranes/Month:	"), 0, 4);
			Constraint_grid.add(NoofCranes1, 2, 4);
			Constraint_grid.add(new Label("Number of  600 ton Tailing Cranes/Month:	"), 0, 5);
			Constraint_grid.add(NoofCranes2, 2, 5);
			Constraint_grid.add(new Label("Number of  250 ton Cranes/Month:	"), 0, 6);
			Constraint_grid.add(NoofCranes3, 2, 6);
			Constraint_grid.add(new Label("Number of  250 ton Tailing Cranes/Month:	"), 0, 7);
			Constraint_grid.add(NoofCranes4, 2, 7);
			
			TitledPane Constraint_TP = new TitledPane();
			Constraint_TP.setAnimated(true);
			Constraint_TP.setText("CONSTRAINTS");
			Constraint_TP.setContent(Constraint_grid);
			Constraint_TP.setExpanded(true);

			Accordion Constarint_accordion = new Accordion();
			Constarint_accordion.getPanes().add(Constraint_TP);
			
			VBox cal_v = new VBox();
			cal_v.setPadding(new Insets(10, 10,10,10));
			cal_v.setSpacing(10);
			cal_v.getChildren().add(cost_accordion);
			cal_v.getChildren().add(Duration_accordion);
			cal_v.getChildren().add(Constarint_accordion);
			
			Calc_Mod.setContent(cal_v);
//********************************************Project Modeling*****************************************************************
			MenuBar menuBar = new MenuBar();

			// --- Menu File
			Menu newproject = new Menu();
			Label newFile = new Label("New Project");

			// --- Menu Edit
			Menu openproject = new Menu();
			Label edit = new Label("Open Project");

			newproject.setGraphic(newFile);
			openproject.setGraphic(edit);
			menuBar.getMenus().addAll(newproject, openproject);

			// buttons
			Button btn1 = new Button();
			btn1.setText("  Zoom In                                ");
			Button btn2 = new Button();
			btn2.setText("  Zoom Out                             ");
			Button btn3 = new Button();
			btn3.setText("  Rotate Image                        ");
			Button btn5 = new Button();
			btn5.setText("  Save                                     ");

			// module shape
			Rectangle rectangle = new Rectangle(20, 20, 20, 20);
			rectangle.setFill(Color.CORNFLOWERBLUE);

			// list of module numbers
			list = new ListView<>();
			list.setPrefWidth(70);
			list.setPrefHeight(200);

			// grid inside titledpane
			GridPane grid = new GridPane();
			grid.setVgap(4);
			grid.setPadding(new Insets(5, 5, 5, 5));
			grid.add(new Label("Number: "), 0, 0);
			grid.add(list, 1, 0);
			grid.add(new Label("Module:    "), 0, 1);
			grid.add(rectangle, 1, 1);

			// titlepane for expand and collapse
			TitledPane titledPane = new TitledPane();
			titledPane.relocate(10, 70);
			titledPane.setAnimated(true);
			titledPane.setText("Modules");
			titledPane.setMaxWidth(70.0);
			titledPane.setContent(grid);
			titledPane.setExpanded(true);

			Accordion accordion = new Accordion();
			accordion.getPanes().add(titledPane);

			Line line = new Line(rectangle.getX(), rectangle.getY()+30,
					rectangle.getX() + 50, rectangle.getY()+30);
			line.setStroke(Color.BLACK);
			line.setStrokeWidth(5);
			
			Line line2 = new Line(rectangle.getX(), rectangle.getY(),
					rectangle.getX() + 50, rectangle.getY());
			line2.setStroke(Color.RED);
			line2.setStrokeWidth(10);

	
			// list of conn
			list1 = new ListView<>();
			list1.setPrefWidth(70);
			list1.setPrefHeight(200);

			// grid inside titledpane
			GridPane grid1 = new GridPane();
			grid1.setVgap(4);
			grid1.setPadding(new Insets(5, 5, 5, 5));
			grid1.add(new Label("Number: "), 0, 0);
			grid1.add(list1, 1, 0);
			grid1.add(new Label("Single:    "), 0, 1);
			grid1.add(line, 1, 1);
			grid1.add(new Label("Double:    "), 0, 2);
			grid1.add(line2, 1, 2);

			// titlepane for expand and collapse
			TitledPane titledPane1 = new TitledPane();
			titledPane1.setAnimated(true);
			titledPane1.setText("Connections");
			titledPane1.setMaxWidth(70.0);
			titledPane1.setContent(grid1);
			titledPane1.setExpanded(true);

			Accordion accordion1 = new Accordion();
			accordion1.getPanes().add(titledPane1);
			
			Spinner<Integer> sp = new Spinner<Integer>(1,100,1);
			sp.setPrefWidth(70);
			sp.setEditable(true);
			
			//total number of modules 
			list2 = new ListView<>();
			
			//list of modules in in that zone
			list3 = new ListView<>();
			list3.setPrefWidth(70);
			list3.setPrefHeight(200);
			
			ComboBox dir = new ComboBox();
			dir.getItems().addAll(
				    "Left",
				    "Right",
				    "Random"	    
				);
			dir.getSelectionModel().select(2);
			GridPane grid2 = new GridPane();
			grid2.setVgap(4);
			grid2.setPadding(new Insets(5, 5, 5, 5));
			grid2.add(new Label("Zone: "), 0, 0);
			grid2.add(sp, 1, 0);
			grid2.add(new Label("Direction: "), 0, 1);
			grid2.add(dir,1,1);
			grid2.add(new Label("Modules: "), 0, 2);
			grid2.add(list3, 1, 2);	
			
			TitledPane titledPane2 = new TitledPane();
			titledPane2.setAnimated(true);
			titledPane2.setText("Zones");
			titledPane2.setMaxWidth(70.0);
			titledPane2.setContent(grid2);
			titledPane2.setExpanded(true);

			Accordion accordion2 = new Accordion();
			accordion2.getPanes().add(titledPane2);
			
			//list of areas
			Area_MStones = new TableView<AreaMS>();
			Area_MStones.setMaxWidth(180);
			AreaName = new TableColumn<AreaMS,String>("Area");
			AreaName.setCellValueFactory(
	                new PropertyValueFactory<AreaMS,String>("area"));
	        AreaName.setMaxWidth(100);
	        AreaDL = new TableColumn<AreaMS,String>("Project Month");
	        AreaDL.setCellValueFactory(
	                new PropertyValueFactory<AreaMS,String>("milestone"));
	        AreaDL.setCellFactory(TextFieldTableCell.forTableColumn());
	        AreaDL.setMaxWidth(80);
	        Area_MStones.getColumns().addAll(AreaName, AreaDL);
	        Area_MStones.setEditable(true);
	            
			TitledPane titledPane3 = new TitledPane();
			titledPane3.setAnimated(true);
			titledPane3.setText("Project Milestone");
			titledPane3.setMaxWidth(70.0);
			titledPane3.setContent(Area_MStones);
			titledPane3.setExpanded(true);

			Accordion accordion3 = new Accordion();
			accordion3.getPanes().add(titledPane3);	
		
			// vertical box inside leftpane
			VBox vbox = new VBox();
			vbox.getChildren().add(btn1);
			vbox.getChildren().add(btn2);
			vbox.getChildren().add(btn3);
			vbox.getChildren().add(btn5);
			vbox.getChildren().addAll(accordion);
			vbox.getChildren().addAll(accordion1);
			vbox.getChildren().addAll(accordion2);
			vbox.getChildren().addAll(accordion3);
						
			Pane leftPane = new Pane();
			leftPane.getChildren().add(vbox);

			grp = new Group();

			// scrollpane for rightside of splitpane
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
			scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
			scrollPane.setContent(new Group(grp));

			SplitPane splitPane = new SplitPane();
			splitPane.getItems().addAll(leftPane, scrollPane);
			splitPane.setDividerPositions(0.30);
			leftPane.maxWidthProperty().bind(
					splitPane.widthProperty().multiply(0.17));
			splitPane.prefHeightProperty().bind(scene.heightProperty());
			
			
			VBox v  = new VBox();
			v.getChildren().add(menuBar);
			v.getChildren().add(splitPane);
			project_Mod.setContent(v);
			// pdf chooser
			newFile.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					
					module.clear();
					connection.clear();
					module_label.clear();
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("STEP-1");
					alert.setContentText("Choose the Plot Plan");
					alert.showAndWait();
					FileChooser fileChooser = new FileChooser();

					// Set Initial Directory to Desktop and set image in
					// rightpane
					fileChooser.setInitialDirectory(new File(System
							.getProperty("user.home") + "\\Desktop"));
					
					//show on primarystage
					File file = fileChooser.showOpenDialog(primaryStage);
					Image image = new Image(file.toURI().toString());
					imgView = new ImageView(image);
					imgView.setImage(image);
					imgView.setSmooth(true);
					grp.getChildren().add(imgView);


					imgview_sim = new ImageView(image);
					imgview_sim.setImage(image);
					imgview_sim.setSmooth(true);
					hb.getChildren().add(imgview_sim);

					alert.setTitle("STEP2");
					alert.setContentText("Choose Module List Excel");
					alert.showAndWait();
				
					// Set Initial Directory to Desktop and read the excel
					fileChooser.setInitialDirectory(new File(System
							.getProperty("user.home") + "\\Desktop"));
					file1 = fileChooser.showOpenDialog(primaryStage);
					list.getItems().clear();
					list1.getItems().clear();
					read_excel(file1);
					
					list.setCellFactory(new Callback<ListView<MyObject>, ListCell<MyObject>>() {

						@Override
						public ListCell<MyObject> call(ListView<MyObject> p) {

							ListCell<MyObject> cell = new ListCell<MyObject>() {

								@Override
								protected void updateItem(MyObject t,
										boolean bln) {

									super.updateItem(t, bln);
									if (t != null) {

										setText(Integer.toString((int) Double
												.parseDouble(t.name)));
									} else {

										setText("");
									}
								}
							};

							return cell;
						}
					});
					
					//update_zones("1","Random");

				}
			});
		
			edit.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					
					module.clear();
					connection.clear();
					module_label.clear();
					
					DirectoryChooser chooser = new DirectoryChooser();
					chooser.setTitle("Open Projects");
					chooser.setInitialDirectory(new File(System
							.getProperty("user.home") + "\\Desktop"));
					File selectedDirectory = chooser.showDialog(primaryStage);
					
					// return file name if the selected file is an image i.e, if true
					if(selectedDirectory.isDirectory()){
						File[] png = selectedDirectory.listFiles(new FilenameFilter() {
						    public boolean accept(File dir, String name) {
						        return name.toLowerCase().endsWith(".png");
						    }
						});
						Image image = new Image(png[0].toURI().toString());
						imgView = new ImageView(image);
						imgView.setImage(image);
						imgView.setSmooth(true);
						grp.getChildren().add(imgView);


						imgview_sim = new ImageView(image);
						imgview_sim.setImage(image);
						imgview_sim.setSmooth(true);
						grp_sim.getChildren().add(imgview_sim);
					}	
					
					if(selectedDirectory.isDirectory()){
						File[] xlsxfile = selectedDirectory.listFiles(new FilenameFilter() {
						    public boolean accept(File dir, String name) {
						        return name.toLowerCase().endsWith("new.xlsx");
						    }
						});
						file1 = xlsxfile[0];
						list.getItems().clear();
						
						if (read_excel(file1) == 0) {
							
							list.setCellFactory(new Callback<ListView<MyObject>, ListCell<MyObject>>() {

								@Override
								public ListCell<MyObject> call(ListView<MyObject> p) {

									ListCell<MyObject> cell = new ListCell<MyObject>() {

										@Override
										protected void updateItem(MyObject t,boolean bln) {

											super.updateItem(t, bln);
											if (t != null) {

												setText(Integer.toString((int) Double
														.parseDouble(t.name)));
											} else {

												setText("");
											}
										}
									};

									return cell;
								}
							});
							
						} else {
							update_list();
						}

					}

					//changed
					if(selectedDirectory.isDirectory()){
						File[] xlsxfile1 = selectedDirectory.listFiles(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.endsWith("newPD.xlsx");
							}
						});
						list1.getItems().clear();
						read_conn_excel(xlsxfile1[0]);

						if(ZoneDirection.containsKey(1)){
							update_zones("1",ZoneDirection.get(1));
						}else{
							update_zones("1","Random");
						}

					}
					
				}
				
			});

			// zoom in
			btn1.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					grp.setScaleX(grp.getScaleX() * 1.5);
					grp.setScaleY(grp.getScaleY() * 1.5);
				}
			});

			// zoom out
			btn2.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					grp.setScaleX(grp.getScaleX() / 1.5);
					grp.setScaleY(grp.getScaleY() / 1.5);
				}
			});

			// rotate
			btn3.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					grp.setRotate(grp.getRotate() + 90);
				}
			});

			// save
			btn5.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					
					write_toExcel(module_label);
				}
			});

			list.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					l = new Label();
					l.setText(Integer.toString((int) Double.parseDouble(list
							.getSelectionModel().getSelectedItem().name)));
				}

			});

			// do this when a drag is detected on the module on leftpane
			rectangle.setOnDragDetected(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					Dragboard dragboard = rectangle
							.startDragAndDrop(TransferMode.COPY);
					ClipboardContent content = new ClipboardContent();
					content.putString("module");
					dragboard.setContent(content);
					event.consume();
				}
			});

			// do this when module is being dragged onto rightpane
			grp.setOnDragOver(new EventHandler<DragEvent>() {

				@Override
				public void handle(DragEvent event) {

					event.acceptTransferModes(TransferMode.COPY);
					orgSceneX = event.getX();
					orgSceneY = event.getY();
					event.consume();
				}
			});

			line.setOnDragDetected(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					Dragboard dragboard = rectangle
							.startDragAndDrop(TransferMode.COPY);
					ClipboardContent content = new ClipboardContent();
					content.putString("line");
					dragboard.setContent(content);
					event.consume();
				}
			});
			line2.setOnDragDetected(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					Dragboard dragboard = rectangle
							.startDragAndDrop(TransferMode.COPY);
					ClipboardContent content = new ClipboardContent();
					content.putString("line2");
					dragboard.setContent(content);
					event.consume();
				}
			});
			
	
			// when the module is dropped set the location of module
			grp.setOnDragDropped(new EventHandler<DragEvent>() {

				@Override
				public void handle(DragEvent event) {

					Dragboard dragboard = event.getDragboard();

					if (dragboard.getString().equals("module")) {

						Rectangle rect = new Rectangle(orgSceneX, orgSceneY, 20, 20);

						try {
							// module gets placed on the image
							grp.getChildren().addAll(rect, l);
							rect.setFill(Color.CORNFLOWERBLUE);
							//double click on rectangle
							ReadProps.run(rect);
							DragResizer.makeResizable(rect);
							rect.setId("" + rect_no);
							module.add(rect);
							l.relocate(orgSceneX, orgSceneY - 15);
							module_label.put(rect_no, list.getSelectionModel()
									.getSelectedItem());
							rect_no++;
							update_list();
							read_prop(rect, list.getSelectionModel()
									.getSelectedItem());

						} catch (Exception e) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("ERROR");
							alert.setContentText("The module is already created!");
							alert.showAndWait();
						}
						event.setDropCompleted(true);
					}
				
				if (dragboard.getString().equals("line")) {

						Line line = new Line(orgSceneX, orgSceneY,
								orgSceneX + 18, orgSceneY);
						line.setFill(Color.BLACK);
						line.setStrokeWidth(3);

						try {

							grp.getChildren().add(line);
							
							line.setId(conn_no + "");
							list1.getItems().add("Conn-" + line.getId());
							conn_no++;
							MyConn conn = new MyConn(line);
							DragLine.makeResizable(line,conn);
							getmodforconn(line,conn);
							
						} catch (Exception e) {

							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("ERROR");
							alert.setContentText("The connection is already created!");
							alert.showAndWait();
						}
						event.setDropCompleted(true);
					}
				if (dragboard.getString().equals("line2")) {

					Line line = new Line(orgSceneX, orgSceneY,
							orgSceneX + 18, orgSceneY);
					line.setStroke(Color.RED);
					line.setStrokeWidth(6);

					try {

						grp.getChildren().add(line);
						
						line.setId(conn_no + "");
						list1.getItems().add("Conn-" + line.getId());
						conn_no++;
						MyConn conn = new MyConn(line);
						DragLine.makeResizable(line,conn);
						getmodforconn(line,conn);
						
					} catch (Exception e) {

						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setContentText("The connection is already created!");
						alert.showAndWait();
					}
					event.setDropCompleted(true);
				}
					event.consume();
				}

			});

			// drag is done
			grp.setOnDragDone(new EventHandler<DragEvent>() {

				@Override
				public void handle(DragEvent event) {

					event.consume();
				}
			});
			
			sp.valueProperty().addListener((obs, oldValue, newValue) -> {
				 if (!"".equals(newValue)) {
			        	String newzone = Integer.toString(newValue);
			        	if(ZoneDirection.containsKey(newValue)){
			        		update_zones(newzone,ZoneDirection.get(newValue));
			        	}else{
			        		update_zones(newzone,"Random");
			        		ZoneDirection.put(newValue,"Random");
			        	}
			        	dir.getSelectionModel().select(ZoneDirection.get(newValue));
			        	
			        }  
		    });
			sp.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
		        if (!"".equals(newValue)) {
		        	int zone = Integer.parseInt(newValue);
		        	if(ZoneDirection.containsKey(zone)){
		        		
		        		update_zones(newValue,ZoneDirection.get(zone));
		        	}else{
		        		update_zones(newValue,"Random");
		        		ZoneDirection.put(zone,"Random");
		        	}
		        	
		        	dir.getSelectionModel().select(ZoneDirection.get(zone));
		        	
		        } 
		    });
			
			dir.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
		          
				int zone = sp.getValue();
				String direction = (String)newValue;
				ZoneDirection.put(zone, direction);
				update_zones(zone+"", direction);
		    }
		    ); 
						 
			AreaDL.setOnEditCommit(new EventHandler<CellEditEvent<AreaMS,String>>(){

				@Override
				public void handle(CellEditEvent<AreaMS, String> t) {
					((AreaMS) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setMilestone(t.getNewValue());

				}

			} ); 
			
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void update_zones(String newzone, String direction) {
		
		list3.getItems().clear();
		if(direction.equals("Left")||direction.equals("Random")){
			for(int m=0;m<module_label.size();m++){
				
				MyObject t = module_label.get(m);
				Rectangle temp = module.get(m);
				
				if(t.zone.equals(newzone)){	
					temp.setFill(Color.RED);
					list3.getItems().add(Integer.toString((int)Double.parseDouble(t.name)));
				}else{
					temp.setFill(areas.get(t.area));
				}
			}
		}else if(direction.equals("Right")){
			
			for(int m=module_label.size()-1;m>=0;m--){
				
				MyObject t = module_label.get(m);
				Rectangle temp = module.get(m);
				
				if(t.zone.equals(newzone)){
					temp.setFill(Color.RED);
					list3.getItems().add(Integer.toString((int)Double.parseDouble(t.name)));
				}else{
					temp.setFill(areas.get(t.area));
				}
			}
		}
		
	}

	//changed this function
	protected void read_conn_excel(File file) {
		int flag=0;
		try {

			workbook_conn = new XSSFWorkbook(file);
			sheet_conn = workbook_conn.getSheetAt(0);
			rows_conn = sheet_conn.getPhysicalNumberOfRows();

			for (int r = 0; r <= rows_conn; r++) {
				Row row = sheet_conn.getRow(r);
				if (row != null) {
					// for(int c = 0; c < cols; c++) {
					Cell cell = row.getCell(0);

					if (cell != null ) {

						Line line = new Line(row.getCell(3).getNumericCellValue(),row.getCell(4).getNumericCellValue(),row.getCell(5).getNumericCellValue(),row.getCell(6).getNumericCellValue());
						line.setId(cell.getStringCellValue());

						Cell cell1 = row.getCell(1);

						if(cell1.getStringCellValue().equals("Single")) {
							line.setStroke(Color.BLACK);
							line.setStrokeWidth(3);
						}else{
							line.setStroke(Color.RED);
							line.setStrokeWidth(6);
						}
						MyConn temp = new MyConn(line);
						grp.getChildren().add(line);
						temp.Conn_Type = row.getCell(1).getStringCellValue();
						temp.conn_between = row.getCell(2).getStringCellValue();
						connection.put(line.getId(), temp);
						ReadConnProps.run(line);
						conn_no = Integer.parseInt(line.getId());
						list1.getItems().add(temp.Name);
						flag=1;
					}
					// }
				}
			}
			if(flag==1){
				conn_no++;
			}
			XSSFSheet sheet_PD = workbook_conn.getSheetAt(1);
			int rowsPD = sheet_PD.getPhysicalNumberOfRows();
			int zonedetails = 0;
			int milestones = 0;

			ArrayList<AreaMS> data1 = new ArrayList<AreaMS>();

			for (int r = 0; r <= rowsPD; r++) {
				Row row = sheet_PD.getRow(r);
				if (row != null) {
					// for(int c = 0; c < cols; c++) {
					Cell cell = row.getCell(0);

					if(cell != null && milestones == 1){

						Cell cell1 = row.getCell(1);
						String area = cell.getStringCellValue();
						String MS = cell1.getStringCellValue();
						data1.add(new AreaMS(area, MS));
					}

					if(cell!=null && cell.getCellTypeEnum()== CellType.STRING){
						if(cell != null && cell.getStringCellValue().equals("Project Milestones")){

							zonedetails =0;
							milestones = 1;
						}
					}
					if(cell != null && zonedetails == 1){

						Cell cell1 = row.getCell(1);
						int zone = (int) cell.getNumericCellValue();
						//		System.out.println("Zone: "+zone);
						String direction = cell1.getStringCellValue();

						ZoneDirection.put(zone, direction);
					}
					if(cell!=null && cell.getCellTypeEnum()==CellType.STRING){
						if (cell != null && cell.getStringCellValue().equals("Zone Direction")) {

							zonedetails =1;
						}
					}

				}
			}
			final ObservableList<AreaMS> data = FXCollections.observableArrayList(data1);
			Area_MStones.setItems(data);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static void getmodforconn(Line line,MyConn conn) {
	
		String conn_between="";
		int start=0,end=0;
		
		for(int i=0;i<module.size();i++){
			
			if(module.get(i).contains(conn.startx,conn.starty)){
				start=1;
				conn_between = conn_between+(int)Double.parseDouble(module_label.get(Integer.parseInt(module.get(i).getId())).name)+", ";
				
			}else if(module.get(i).contains(conn.endx,conn.endy)){
				end=1;
				conn_between = conn_between+(int)Double.parseDouble(module_label.get(Integer.parseInt(module.get(i).getId())).name)+" ";
				
			}
		}
		if(start==1 && end==1){
			conn.conn_between = conn_between;
		}
		else{
			conn.conn_between = "Invalid Connection";
		}
		if(conn.conn_between.equals("Invalid Connection")){
			
		}
		else
		{
			connection.put(line.getId(), conn);
			ReadConnProps.run(line);
		}
		
	}

	public void update_list() {

		list.setCellFactory(new Callback<ListView<MyObject>, ListCell<MyObject>>() {

			@Override
			public ListCell<MyObject> call(ListView<MyObject> p) {

				ListCell<MyObject> cell = new ListCell<MyObject>() {

					@Override
					protected void updateItem(MyObject t, boolean bln) {

						super.updateItem(t, bln);
						if (t != null) {

							setText(Integer.toString((int) Double
									.parseDouble(t.name)));

							if (l.getText().equals(
									Integer.toString((int) Double
											.parseDouble(t.name)))) {

								if (!getStyleClass().contains("mystyleclass")) {
									getStyleClass().add("mystyleclass");
								}
								t.color = 1;

							} else if (t.color == 1) {
								if (!getStyleClass().contains("mystyleclass")) {
									getStyleClass().add("mystyleclass");
								}
							} else {
								getStyleClass().remove("mystyleclass");
							}
						} else {
							setText("");
						}
					}

				};

				return cell;
			}
		});
	}

	//changed this function
	public void write_toExcel(HashMap<Integer, MyObject> module_label)  {
		String folder = "";
		int count = 0;
		try {

			FileInputStream file = new FileInputStream(file1);

			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			// XSSFCell cell = null;

			int rows = sheet.getPhysicalNumberOfRows();
			FormulaEvaluator evaluator = workbook.getCreationHelper()
					.createFormulaEvaluator();
			// Update the value of cell

			for (int r = 0; r <= rows && count < module_label.size(); r++) {
				Row row = sheet.getRow(r);
				if (row != null) {
					// for(int c = 0; c < cols; c++) {
					XSSFCell cell = (XSSFCell) row.getCell(2);
					if (cell != null && !cell.toString().equals("Module No.")) {
						;
						CellValue cellvalue = evaluator.evaluate(cell);
						for (int i = 0; i < module_label.size(); i++) {

							if (module_label.get(i).name.equals(Double
									.toString(cellvalue.getNumberValue()))) {

								cell = (XSSFCell) row.getCell(0);

								writeallcells(cell, row,module_label.get(i).id, 0);
								cell = (XSSFCell) row.getCell(1);
								writeallcells(cell, row,
										module_label.get(i).area, 1);
								cell = (XSSFCell) row.getCell(3);
								writeallcells(cell, row,
										module_label.get(i).cat, 3);
								cell = (XSSFCell) row.getCell(4);
								writeallcells(cell, row,
										module_label.get(i).des, 4);
								cell = (XSSFCell) row.getCell(5);
								writeallcells(cell, row,
										module_label.get(i).width, 5);
								cell = (XSSFCell) row.getCell(6);
								writeallcells(cell, row,
										module_label.get(i).length, 6);
								cell = (XSSFCell) row.getCell(7);
								writeallcells(cell, row,
										module_label.get(i).height, 7);
								cell = (XSSFCell) row.getCell(8);
								writeallcells(cell, row,
										module_label.get(i).ton, 8);
								cell = (XSSFCell) row.getCell(9);
								writeallcells(cell, row,
										module_label.get(i).weight_mt, 9);
								cell = (XSSFCell) row.getCell(10);
								writeallcells(cell, row,
										module_label.get(i).land, 10);
								cell = (XSSFCell) row.getCell(11);
								writeallcells(cell, row,
										module_label.get(i).axe, 11);
								cell = (XSSFCell) row.getCell(12);
								writeallcells(cell, row,
										module_label.get(i).lift, 12);
								cell = (XSSFCell) row.getCell(13);
								writeallcells(cell, row,
										module_label.get(i).crane, 13);
								cell = (XSSFCell) row.getCell(14);
								writeallcells(cell, row,
										module_label.get(i).ROS, 14);
								cell = (XSSFCell) row.getCell(15);
								writeallcells(cell, row,
										module_label.get(i).set_date, 15);
								cell = (XSSFCell) row.getCell(16);
								writeallcells(cell, row,
										module_label.get(i).remarks, 16);
								cell = (XSSFCell) row.getCell(17);
								writeallcells(cell, row, module_label.get(i).x,
										17);
								cell = (XSSFCell) row.getCell(18);
								writeallcells(cell, row, module_label.get(i).y,
										18);
								cell = (XSSFCell) row.getCell(19);
								writeallcells(cell, row, Double.toString(module
										.get(i).getHeight()), 19);
								cell = (XSSFCell) row.getCell(20);
								writeallcells(cell, row, Double.toString(module
										.get(i).getWidth()), 20);
								cell = (XSSFCell) row.getCell(21);
								writeallcells(cell, row, module_label.get(i).zone, 21);
								count++;
								break;
							}

						}

					}
					// }
				}
			}

			file.close();


			boolean success = new File(System.getProperty("user.home") + "\\Desktop\\SavedProject").mkdirs();
			if(success){
				folder = System.getProperty("user.home") + "\\Desktop\\SavedProject";
			}
			FileOutputStream outFile = new FileOutputStream(new File(folder+"\\new.xlsx"));
			workbook.write(outFile);
			outFile.close();
			workbook.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Connection Details Excel
		XSSFWorkbook workbook_conn = new XSSFWorkbook();
		XSSFSheet sheet_conn = workbook_conn.createSheet("Connections");

		int rowCount = 0;

		for (int i=1;i<=connection.size();i++) {
			MyConn temp = connection.get(i+"");
			Row row = sheet_conn.createRow(rowCount++);

			int columnCount = 0;
			Cell cell = row.createCell(columnCount++);
			cell.setCellValue((String) temp.Name);
			cell = row.createCell(columnCount++);
			cell.setCellValue((String) temp.Conn_Type);
			cell = row.createCell(columnCount++);
			cell.setCellValue((String) temp.conn_between);
			cell = row.createCell(columnCount++);
			cell.setCellValue((Double) temp.startx);
			cell = row.createCell(columnCount++);
			cell.setCellValue((Double) temp.starty);
			cell = row.createCell(columnCount++);
			cell.setCellValue((Double) temp.endx);
			cell = row.createCell(columnCount++);
			cell.setCellValue((Double) temp.endy);
			cell = row.createCell(columnCount++);
			cell.setCellValue((Integer) temp.orientation);

		}


   /*     FileOutputStream outputStream;
		try {
			folder = System.getProperty("user.home") + "\\Desktop\\SavedProject";
			outputStream = new FileOutputStream(new File(folder+"\\newconn.xlsx"));
			workbook_conn.write(outputStream);
			outputStream.close();
	        workbook_conn.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		*/
		//Project Details excel
		//	XSSFWorkbook workbook_projectDetails = new XSSFWorkbook();
		XSSFSheet sheet_projectDetails = workbook_conn.createSheet("ProjectDetails");

		//Zone Direction Details
		int rowCount1 = 0;
		int columnCount1 = 0;

		//Set Zone direction heading
		Row rowheader = sheet_projectDetails.createRow(rowCount1++);
		Cell cell = rowheader.createCell(columnCount1);
		cell.setCellValue("Zone Direction");

		Set<Integer> zones = ZoneDirection.keySet();
		for(int zone : zones){

			columnCount1 = 0;
			Row rowzones = sheet_projectDetails.createRow(rowCount1++);
			cell = rowzones.createCell(columnCount1++);
			cell.setCellValue((Integer)zone);
			cell = rowzones.createCell(columnCount1++);
			cell.setCellValue((String)ZoneDirection.get(zone));
		}

		//Set Project Milestone heading
		columnCount1 = 0;
		Row rowheader2 = sheet_projectDetails.createRow(rowCount1++);
		cell = rowheader2.createCell(columnCount1);
		cell.setCellValue((String)"Project Milestones");

		for (AreaMS o : Main.Area_MStones.getItems()) {

			String area1 = Main.AreaName.getCellData(o);
			String MS = Main.AreaDL.getCellData(o);

			columnCount1 = 0;
			Row rowMS = sheet_projectDetails.createRow(rowCount1++);
			cell = rowMS.createCell(columnCount1++);
			cell.setCellValue((String)area1);
			cell = rowMS.createCell(columnCount1++);
			cell.setCellValue((String)MS);
		}

		FileOutputStream outputStream1;
		try {
			folder = System.getProperty("user.home") + "\\Desktop\\SavedProject";
			outputStream1 = new FileOutputStream(new File(folder+"\\newPD.xlsx"));
			workbook_conn.write(outputStream1);
			outputStream1.close();
			workbook_conn.close();
		} catch (IOException e) {

			e.printStackTrace();
		}


	}

	@SuppressWarnings("deprecation")
	public void writeallcells(XSSFCell cell, Row row, String str, int cellno) {

		if (cell == null) {
			cell = (XSSFCell) row.createCell(cellno);
		}

		// System.out.println(cell.getCellType());

		if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {

			cell.setCellValue(str);

		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_ERROR) {

			cell.setCellValue(str);

		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {

			cell.setCellValue(str);

		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			
			cell.setCellValue(Double.parseDouble(str));
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {

			cell.setCellValue(str);
		}
	}

	// read excel and copy the module numbers to listview
	public int read_excel(File file12) {

		list2.getItems().clear();
		areas = new HashMap<String,Color>();

		ArrayList<AreaMS> data1 = new ArrayList<AreaMS>();

		int flag = 0;
		try {

			workbook = new XSSFWorkbook(file12);
			sheet = workbook.getSheetAt(0);
			rows = sheet.getPhysicalNumberOfRows();

			FormulaEvaluator evaluator = workbook.getCreationHelper()
					.createFormulaEvaluator();

			for (int r = 0; r <= rows; r++) {
				Row row = sheet.getRow(r);
				if (row != null) {
					// for(int c = 0; c < cols; c++) {
					Cell cell = row.getCell(2);
					//start NOV5
					if (cell != null && !cell.toString().equals("Module No.")) {
						if(!areas.containsKey(row.getCell(1).getStringCellValue())){

							areas.put(row.getCell(1).getStringCellValue(),Color.color(Math.random(), Math.random(), Math.random()));

							//changed
							if(row.getCell(17)==null){
								data1.add(new AreaMS(row.getCell(1).getStringCellValue(),""));
							}
						}

						//end NOV5
						CellValue cellvalue = evaluator.evaluate(cell);

						MyObject temp = new MyObject(Double.toString(cellvalue
								.getNumberValue()));

						cell = row.getCell(17);
						if (cell != null) {

							flag = 1;

							read_prop_excel(row, temp, null);
							Rectangle rect = new Rectangle(Double.parseDouble(temp.x), Double.parseDouble(temp.y),
									Double.parseDouble(temp.objwidth),
									Double.parseDouble(temp.objheight));
							l = new Label(Integer.toString((int) Double
									.parseDouble(temp.name)));
							rect.setId(Integer.toString(rect_no));
							rect.setFill(areas.get(temp.area));
							l.relocate(Double.parseDouble(temp.x),
									Double.parseDouble(temp.y)-15);
							grp.getChildren().addAll(rect, l);
							module.add(rect);
							module_label.put(rect_no, temp);
							ReadProps.run(rect);
							rect_no++;
							temp.color = 1;

						}
						list.getItems().add(temp);
						list2.getItems().add(temp);
					}
					// }
				}
			}

		final ObservableList<AreaMS> data = FXCollections.observableArrayList(data1);
	    Area_MStones.setItems(data);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return flag;
	}

	public static void main(String[] args) {
		launch(args);
	}

	// read props from excel and call pop window
	public void read_prop(Rectangle m, MyObject temp) {
		
	FormulaEvaluator evaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();


		try {
			
			for (int r = 0; r <= rows; r++) {
				
				Row row = sheet.getRow(r);
				
				if (row != null) {
				
					Cell cell = row.getCell(2);
					CellValue cellvalue = evaluator.evaluate(cell);
					
					if (cell != null) {
						
						if (Double.toString(cellvalue
								.getNumberValue()).equals(temp.name)) {
							
							read_prop_excel(row, temp, m);
							break;
						}
					}
					
				}
			}
		} catch (Exception ioe) {

			ioe.printStackTrace();
		}
	}

	public void read_prop_excel(Row row, MyObject temp, Rectangle m) {
		
		FormulaEvaluator evaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		temp.id = row.getCell(0).toString();
		temp.area = row.getCell(1).toString();
	//	temp.name = row.getCell(2).toString();
		temp.cat = row.getCell(3).toString();
		temp.des = row.getCell(4).toString();
		CellValue cellvalue3 = evaluator.evaluate(row.getCell(5));
		temp.width = Double.toString(cellvalue3.getNumberValue());
		CellValue cellvalue4 = evaluator.evaluate(row.getCell(6));
		temp.length = Double.toString(cellvalue4.getNumberValue());
		temp.height = row.getCell(7).toString();
		CellValue cellvalue = evaluator.evaluate(row.getCell(8));	
		temp.ton = Double.toString(cellvalue.getNumberValue());
		CellValue cellvalue1 = evaluator.evaluate(row.getCell(9));
		temp.weight_mt = Double.toString(cellvalue1.getNumberValue());
		temp.land = row.getCell(10).toString();
		temp.axe = row.getCell(11).toString();
		temp.lift = row.getCell(12).toString();
		temp.crane = row.getCell(13).toString();
		CellValue cellvalue2 = evaluator.evaluate(row.getCell(14));
		temp.ROS = Double.toString(cellvalue2.getNumberValue());
		temp.set_date = row.getCell(15).toString();
		temp.remarks = row.getCell(16).toString();
		if (m != null) {

			temp.x = Double.toString(m.getX());
			temp.y = Double.toString(m.getY());
			m.setFill(areas.get(temp.area));
			temp.zone = Integer.toString(1);
		} else {

			temp.x = row.getCell(17).toString();
			temp.y = row.getCell(18).toString();
			temp.objheight = row.getCell(19).toString();
			temp.objwidth = row.getCell(20).toString();
			temp.zone = row.getCell(21).toString();
		}
		
	}

	public void read_prop_excel_simulate(Row row, MyObject temp, Rectangle m, double d) {

		FormulaEvaluator evaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();
		months_simulate = (row.getCell(2)).toString();
		if(Double.valueOf(months_simulate) <= d){

			temp.area = row.getCell(1).toString();
			temp.name = row.getCell(0).toString();

			temp.x = row.getCell(3).toString();
			temp.y = row.getCell(4).toString();
			temp.objheight = row.getCell(5).toString();
			temp.objwidth = row.getCell(6).toString();
		}
	}


	public int read_excel_simulate(String file12, double d) {
		areas = new HashMap<String,Color>();
		int flag = 0;
		try {

			//InputStream inp = new FileInputStream(file12);
			//Workbook wb = WorkbookFactory.create(inp);
			workbook = new XSSFWorkbook(new File(file12));
			sheet = workbook.getSheetAt(0);
			rows = sheet.getPhysicalNumberOfRows();

			FormulaEvaluator evaluator = workbook.getCreationHelper()
					.createFormulaEvaluator();

			for (int r = 0; r <= rows; r++) {
				Row row = sheet.getRow(r);
				if (row != null) {
					// for(int c = 0; c < cols; c++) {
					Cell cell = row.getCell(0);

					if (cell != null ) {
						if(!areas.containsKey(row.getCell(1).getStringCellValue())){

							areas.put(row.getCell(1).getStringCellValue(),Color.color(Math.random(), Math.random(), Math.random()));
						}

						CellValue cellvalue = evaluator.evaluate(cell);

						MyObject temp = new MyObject(Double.toString(cellvalue
								.getNumberValue()));

						cell = row.getCell(3);
						if (cell != null) {

							flag = 1;

							read_prop_excel_simulate(row, temp, null, d);
							try {

								rect_sim = new Rectangle(Double.parseDouble(temp.x), Double.parseDouble(temp.y),
										Double.parseDouble(temp.objwidth),
										Double.parseDouble(temp.objheight));
								l = new Label(Integer.toString((int) Double
										.parseDouble(temp.name)));
								rect_sim.setId(Integer.toString(rect_no));
								if(Double.valueOf(months_simulate) == d){
									rect_sim.setFill(Color.RED);
									//System.out.println(temp.name);
									Module_under.put(d, temp.name);
									//rect_sim.setStroke(Color.BLACK);

								}else{
									rect_sim.setFill(Color.BLUE);
								}

								//rect_sim.setFill(areas.get(temp.area));}
								//rect.relocate(Double.parseDouble(temp.x) + 20,Double.parseDouble(temp.y) + 20);
								l.relocate(Double.parseDouble(temp.x),
										Double.parseDouble(temp.y) - 15);

								trackrect.put(rect_sim, l);//dont add here.. sort and add
								grp_sim.getChildren().addAll(rect_sim, l);
								//module.add(rect);
								//conn_label.put(rect_no, temp);
								ReadProps.run(rect_sim);
								rect_no++;
								temp.color = 1;

							}catch (NullPointerException ex){
							}
						}

					}
					// }
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return flag;
	}

	public int read_excel_simulate_animation(String file12, double d) {
		areas = new HashMap<String,Color>();
		int flag = 0;
		try {

			//InputStream inp = new FileInputStream(file12);
			//Workbook wb = WorkbookFactory.create(inp);
			workbook = new XSSFWorkbook(new File(file12));
			sheet = workbook.getSheetAt(0);
			rows = sheet.getPhysicalNumberOfRows();

			FormulaEvaluator evaluator = workbook.getCreationHelper()
					.createFormulaEvaluator();

			for (int r = 0; r <= rows; r++) {
				Row row = sheet.getRow(r);
				if (row != null) {
					// for(int c = 0; c < cols; c++) {
					Cell cell = row.getCell(0);

					if (cell != null ) {
						if(!areas.containsKey(row.getCell(1).getStringCellValue())){

							areas.put(row.getCell(1).getStringCellValue(),Color.color(Math.random(), Math.random(), Math.random()));
						}

						CellValue cellvalue = evaluator.evaluate(cell);

						MyObject temp = new MyObject(Double.toString(cellvalue
								.getNumberValue()));

						cell = row.getCell(3);
						if (cell != null) {

							flag = 1;

							read_prop_excel_simulate(row, temp, null, d);
							try {

								rect_sim = new Rectangle(Double.parseDouble(temp.x), Double.parseDouble(temp.y),
										Double.parseDouble(temp.objwidth),
										Double.parseDouble(temp.objheight));
								l = new Label(Integer.toString((int) Double
										.parseDouble(temp.name)));
								rect_sim.setId(Integer.toString(rect_no));
								if(Double.valueOf(months_simulate) == d){
									rect_sim.setFill(Color.RED);
									//rect_sim.setStroke(Color.BLACK);

								}else{
									rect_sim.setFill(Color.BLUE);}
								//rect.relocate(Double.parseDouble(temp.x) + 20,Double.parseDouble(temp.y) + 20);
								l.relocate(Double.parseDouble(temp.x),
										Double.parseDouble(temp.y) - 15);

								rect_ani.put(rect_sim, l);
								//System.out.println(months_simulate);
								//System.out.println(rect_sim);
								install_ani.put(months_simulate, rect_sim);
								countttt++;
								//System.out.println(countttt);
								ReadProps.run(rect_sim);
								rect_no++;
								temp.color = 1;

							}catch (NullPointerException ex){
							}
						}

					}
					// }
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return flag;
	}


	protected void read_conn_excel_simulate_animation(String file, Double d) {
		int flag=0;
		try {

			workbook_conn = new XSSFWorkbook(file);
			sheet_conn = workbook_conn.getSheetAt(0);
			rows_conn = sheet_conn.getPhysicalNumberOfRows();

			for (int r = 0; r <= rows_conn; r++) {
				Row row = sheet_conn.getRow(r);
				if (row != null) {

					// for(int c = 0; c < cols; c++) {
					Cell cell = row.getCell(0);

					if (cell != null ) {
						months_conn_simulate = (row.getCell(2)).toString();
						if (Double.valueOf(months_conn_simulate) <= d) {

							Line line = new Line(row.getCell(4).getNumericCellValue(), row.getCell(5).getNumericCellValue(), row.getCell(6).getNumericCellValue(), row.getCell(7).getNumericCellValue());
							line.setId(cell.getStringCellValue());

							Cell cell1 = row.getCell(1);

							if (cell1.getStringCellValue().equals("Single")) {
								if(Double.valueOf(months_conn_simulate) < d){
									line.setStroke(Color.BLACK);
									line.setStrokeWidth(3);}
								else{
									line.setStroke(Color.RED);
									line.setStrokeWidth(3);
								}
							} else {

								if(Double.valueOf(months_conn_simulate) < d){
									line.setStroke(Color.GREEN);
									line.setStrokeWidth(6);}
								else{
									line.setStroke(Color.RED);
									line.setStrokeWidth(6);
								}
							}

							MyConn temp = new MyConn(line);
							conn_ani.put(months_conn_simulate, line);
							trackline.add(line);
							//grp_sim.getChildren().add(line);
						/*temp.Conn_Type = row.getCell(1).getStringCellValue();
						temp.conn_between = row.getCell(2).getStringCellValue();
						connection.put(line.getId(), temp);
						ReadConnProps.run(line);
						conn_no = Integer.parseInt(line.getId());
						list1.getItems().add(temp.Name);*/
							flag = 1;
						}
					}
					// }
				}
			}
			if(flag==1){
				conn_no++;
			}


		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	protected void read_conn_excel_simulate(String file, Double d) {
		int flag=0;
		try {

			workbook_conn = new XSSFWorkbook(file);
			sheet_conn = workbook_conn.getSheetAt(0);
			rows_conn = sheet_conn.getPhysicalNumberOfRows();

			for (int r = 0; r <= rows_conn; r++) {
				Row row = sheet_conn.getRow(r);
				if (row != null) {

					// for(int c = 0; c < cols; c++) {
					Cell cell = row.getCell(0);

					if (cell != null ) {
						months_conn_simulate = (row.getCell(2)).toString();
						if (Double.valueOf(months_conn_simulate) <= d) {

							Line line = new Line(row.getCell(4).getNumericCellValue(), row.getCell(5).getNumericCellValue(), row.getCell(6).getNumericCellValue(), row.getCell(7).getNumericCellValue());
							line.setId(cell.getStringCellValue());

							Cell cell1 = row.getCell(1);

							if (cell1.getStringCellValue().equals("Single")) {
								if(Double.valueOf(months_conn_simulate) < d){
									line.setStroke(Color.BLACK);
									line.setStrokeWidth(3);}
								else{
									line.setStroke(Color.RED);
									line.setStrokeWidth(3);
								}
							} else {

								if(Double.valueOf(months_conn_simulate) < d){
									line.setStroke(Color.GREEN);
									line.setStrokeWidth(6);}
								else{
									line.setStroke(Color.RED);
									line.setStrokeWidth(6);
								}
							}

							trackline.add(line);
							grp_sim.getChildren().add(line);
						/*temp.Conn_Type = row.getCell(1).getStringCellValue();
						temp.conn_between = row.getCell(2).getStringCellValue();
						connection.put(line.getId(), temp);
						ReadConnProps.run(line);
						conn_no = Integer.parseInt(line.getId());
						list1.getItems().add(temp.Name);*/
							flag = 1;
						}
					}
					// }
				}
			}
			if(flag==1){
				conn_no++;
			}


		} catch (Exception e) {

			e.printStackTrace();
		}

	}


	public void display_conn(double d){

		String xlsxfile =("simulate_conn_info.xlsx");

		read_conn_excel_simulate(xlsxfile, d);

	}
	public void display_animation_conn(double d){

		String xlsxfile =("simulate_conn_info.xlsx");

		read_conn_excel_simulate_animation(xlsxfile, d);

	}


	public void display_animation (double d){
		//File[] xlsxfile = "simulate-info.xlxs";
		File selectedDirectory;
		String xlsxfile =("simulate_info.xlsx");

		if (read_excel_simulate_animation(xlsxfile, d) == 0) {

			list.setCellFactory(new Callback<ListView<MyObject>, ListCell<MyObject>>() {

				@Override
				public ListCell<MyObject> call(ListView<MyObject> p) {

					ListCell<MyObject> cell = new ListCell<MyObject>() {

						@Override
						protected void updateItem(MyObject t,boolean bln) {

							super.updateItem(t, bln);
							if (t != null) {

								setText(Integer.toString((int) Double
										.parseDouble(t.name)));
							} else {

								setText("");
							}
						}
					};

					return cell;
				}
			});
		} else {
			update_list();
		}
	}


	public void display (double d){
		//File[] xlsxfile = "simulate-info.xlxs";
		File selectedDirectory;
		String xlsxfile =("simulate_info.xlsx");

		if (read_excel_simulate(xlsxfile, d) == 0) {

			list.setCellFactory(new Callback<ListView<MyObject>, ListCell<MyObject>>() {

				@Override
				public ListCell<MyObject> call(ListView<MyObject> p) {

					ListCell<MyObject> cell = new ListCell<MyObject>() {

						@Override
						protected void updateItem(MyObject t,boolean bln) {

							super.updateItem(t, bln);
							if (t != null) {

								setText(Integer.toString((int) Double
										.parseDouble(t.name)));
							} else {

								setText("");
							}
						}
					};

					return cell;
				}
			});
		} else {
			update_list();
		}
	}

}
 
