package application;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ReadProps {

	private final Rectangle region;

	private ReadProps(Rectangle rect) {
		region = rect;
	}

	public static void run(Rectangle rect) {

		final ReadProps prop = new ReadProps(rect);

		// rectangle is doublicked a window will pop up with its properties
		rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getClickCount() == 2){
					prop.mousePressed(event);
				}
			}
		});

	}

	protected void mousePressed(MouseEvent event) {

		pop_window( Main.module_label.get(Integer.parseInt(region.getId())));

	}

	public void pop_window( MyObject temp) {
		
//--------------------------------info button---------------------------------------------
		Image img1 = new Image(getClass().getResourceAsStream("about.jpg"));
		ImageView imageView = new ImageView(img1);
		imageView.setFitWidth(35);
		imageView.setFitHeight(35);
		Button info = new Button("",imageView);
		Tooltip tt = new Tooltip();
		tt.setText("Text on Hover");
		tt.setStyle("-fx-font: normal bold 13 Langdon; "
		+ "-fx-base: #AE3522; "
		+ "-fx-text-fill: orange;");
		info.setTooltip(tt);
		info.setStyle("-fx-background-color:transparent;-fx-padding:0;-fx-background-size:0;-fx-pref-height: 18px; -fx-pref-width: 50px;");
	//---------------------------------------------------------------------------------
		
		TextField id = new TextField(temp.id);
		TextField zone = new TextField(temp.zone);
		TextField area = new TextField(temp.area);
		TextField mod_no = new TextField(temp.name);
		TextField cat = new TextField(temp.cat);
		TextField des = new TextField(temp.des);
		TextField width = new TextField(temp.width);
		TextField length = new TextField(temp.length);
		TextField height = new TextField(temp.height);
		TextField ton = new TextField(temp.ton);
		TextField weight_mt = new TextField(temp.weight_mt);
		TextField land = new TextField(temp.land);
		TextField axe = new TextField(temp.axe);
		TextField lift = new TextField(temp.lift);
		TextField crane = new TextField(temp.crane);
		TextField ROS = new TextField(temp.ROS);
		TextField set_date = new TextField(temp.set_date);
		TextField remarks = new TextField(temp.remarks);
		TextField x = new TextField(temp.x);
		TextField y = new TextField(temp.y);

		ScrollPane sp = new ScrollPane();
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		// insert values from excel to gridpane i.e, to the textfields
		GridPane prop = new GridPane();
		prop.setVgap(4);
		prop.setPadding(new Insets(5, 5, 5, 5));

		prop.add(new Label("Id No: "), 0, 0);
		prop.add(id, 1, 0);
		prop.add(new Label("Area: "), 0, 1);
		prop.add(area, 1, 1);
		prop.add(new Label("Module No: "), 0, 2);
		prop.add(mod_no, 1, 2);
		prop.add(new Label("Category: "), 0, 3);
		prop.add(cat, 1, 3);
		prop.add(new Label("Description: "), 0, 4);
		prop.add(des, 1, 4);
		prop.add(new Label("Width(m): "), 0, 5);
		prop.add(width, 1, 5);
		prop.add(new Label("Length(m): "), 0, 6);
		prop.add(length, 1, 6);
		prop.add(new Label("Height (m): "), 0, 7);
		prop.add(height, 1, 7);
		prop.add(new Label("Typical weight (Ton/m3): "), 0, 8);
		prop.add(ton, 1, 8);
		prop.add(new Label("Weight (mt): "), 0, 9);
		prop.add(weight_mt, 1, 9);
		prop.add(new Label("Land Transport: "), 0, 10);
		prop.add(land, 1, 10);
		prop.add(new Label("Axels: "), 0, 11);
		prop.add(axe, 1, 11);
		prop.add(new Label("Lifting Method: "), 0, 12);
		prop.add(lift, 1, 12);
		prop.add(new Label("Crane Lifting Capacity(mt): "), 0, 13);
		prop.add(crane, 1, 13);
		prop.add(new Label("Recevied on Site(ROS)(Mo): "), 0, 14);
		prop.add(ROS, 1, 14);
		prop.add(new Label("Setting Date(MO): "), 0, 15);
		prop.add(set_date, 1, 15);
		prop.add(new Label("Remarks: "), 0, 16);
		prop.add(remarks, 1, 16);
		prop.add(new Label("X-cord: "), 0, 17);
		prop.add(x, 1, 17);
		prop.add(new Label("Y-cord: "), 0, 18);
		prop.add(y, 1, 18);
		prop.add(new Label("Zone "), 0, 19);
		prop.add(zone, 1, 19);
		prop.add(info,2, 19);

		sp.setContent(prop);
		Stage stage = new Stage();
		stage.setTitle("Module Properties");
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		// set Stage boundaries to the lower right corner of the visible bounds
		// of the main screen
		stage.setX(primaryScreenBounds.getMinX()
				+ primaryScreenBounds.getWidth() - 358);
		stage.setY(primaryScreenBounds.getMinY()
				+ primaryScreenBounds.getHeight() - 680);
		stage.setScene(new Scene(new BorderPane(sp), 350, 450));
		stage.show();
		// Hide this current window (if this is what you want)
		// ((Node)(event.getSource())).getScene().getWindow().hide();

		id.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).id = newValue;
				}
			}

		});
		area.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).area = newValue;
				}
			}

		});
		cat.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).cat = newValue;
				}
			}

		});
		des.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).des = newValue;
				}
			}

		});
		width.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).width = newValue;
				}
			}

		});

		length.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).length = newValue;
				}
			}

		});

		height.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).height = newValue;
				}
			}
		});

		ton.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).ton = newValue;
				}
			}

		});
		weight_mt.textProperty().addListener(
				(observable, oldValue, newValue) -> {

					for (int i = 0; i < Main.list.getItems().size(); i++) {

						if (mod_no.getText()
								.equals(Main.list.getItems().get(i).name)) {

							Main.list.getItems().get(i).weight_mt = newValue;
						}
					}

				});
		land.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).land = newValue;
				}
			}

		});
		axe.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).axe = newValue;
				}
			}

		});
		lift.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).lift = newValue;
				}
			}

		});
		remarks.textProperty().addListener(
				(observable, oldValue, newValue) -> {

					for (int i = 0; i < Main.list.getItems().size(); i++) {

						if (mod_no.getText()
								.equals(Main.list.getItems().get(i).name)) {

							Main.list.getItems().get(i).remarks = newValue;
						}
					}

				});
		crane.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).crane = newValue;
				}
			}

		});
		ROS.textProperty().addListener((observable, oldValue, newValue) -> {

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).ROS = newValue;
				}
			}

		});
		set_date.textProperty().addListener(
				(observable, oldValue, newValue) -> {

					for (int i = 0; i < Main.list.getItems().size(); i++) {

						if (mod_no.getText()
								.equals(Main.list.getItems().get(i).name)) {

							Main.list.getItems().get(i).set_date = newValue;
						}
					}

				});
		zone.textProperty().addListener((observable, oldValue, newValue) -> {
			

			for (int i = 0; i < Main.list.getItems().size(); i++) {

				if (mod_no.getText().equals(Main.list.getItems().get(i).name)) {

					Main.list.getItems().get(i).zone = newValue;
				}
			}

		});

	}

}
