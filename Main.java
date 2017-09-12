import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import Controller.kanbanController;
import Controller.LaneController;
import Model.Kanban;
import Model.Lane;
import Model.Ticket;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application{
	Stage window;
	static kanbanController kc;
	static LaneController lc;
	static Kanban kanban;
	public BorderPane layout = new BorderPane();
	public static GridPane mainGrid;
	//Height and width of tickets
	final int TICKET_HEIGHT = 150;
	final static int TICKET_WIDTH = 150;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		kc = new kanbanController(null);
		//Creating the main Grid Pane
		mainGrid = new GridPane();
		mainGrid.setPadding(new Insets(0,TICKET_WIDTH,0,TICKET_WIDTH));
		mainGrid.setId("ggg");
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		window = primaryStage;
		window.setTitle("Kanban Board");
		layout.setCenter(mainGrid);


		//File Menu
		Menu fileMenu = new Menu("File");

		//Menu Items
		MenuItem newKanban = new MenuItem("New Kanban Board...");
		MenuItem save = new MenuItem("Save Kanban Board...");
		MenuItem load =  new MenuItem("Load Kanban Board");
		
		fileMenu.getItems().add(newKanban);
		fileMenu.getItems().add(load);
		load.setOnAction(loader ->{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			fileChooser.setInitialDirectory(new File("C:\\Users\\chris\\Documents\\College\\Eclipse\\ICS\\Saves"));
			File file = fileChooser.showOpenDialog(window);
			Kanban e = null;
		      try {
		         FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         e = (Kanban) in.readObject();
		         in.close();
		         fileIn.close();
		      }catch(IOException i) {
		         i.printStackTrace();
		         return;
		      }catch(ClassNotFoundException c) {
		         System.out.println("Kanban class not found");
		         c.printStackTrace();
		         return;
		      }
		      kanban = null;
		      kanban = e;
		      updateBoard();
		});
		fileMenu.getItems().add(save);
		save.setOnAction(saver -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save File");
			fileChooser.setInitialDirectory(new File("C:\\Users\\chris\\Documents\\College\\Eclipse\\ICS\\Saves"));
			fileChooser.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("SER", "*.ser")
	            );
			File file = fileChooser.showSaveDialog(window);
			kc.saveKanban(file);
		});
		

		//Edit Menu
		Menu editMenu = new Menu("Edit");
		//Adding sub items to this menu
		MenuItem addTicket = new MenuItem("Add a ticket...");
		MenuItem createNewLane = new MenuItem("Create New Lane");
		MenuItem deleteALane = new MenuItem("Delete A Lane");
		MenuItem splitLane = new MenuItem("Split A Lane");
		MenuItem deleteSubLane = new MenuItem("Delete A Local Done Lane");
		editMenu.getItems().add(addTicket);
		editMenu.getItems().add(createNewLane);
		editMenu.getItems().add(deleteALane);
		editMenu.getItems().add(splitLane);
		editMenu.getItems().add(deleteSubLane);

		//Main Menu Bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(editMenu);

		//Insering this into the layout
		layout.setTop(menuBar);

		//Create the scene
		Scene scene = new Scene(layout,740,300);
		scene.getStylesheets().add("CSS.css");
		window.setScene(scene);
		window.show();

		// when "New kanban Board" is clicked
		newKanban.setOnAction(e-> {
			createKanbanBoard();
		});

		//when "Add ticket is clicked"
		addTicket.setOnAction(e->{
			window = new Stage();
			window.setTitle("Add Ticket");

			//Create a label called task
			Label task = new Label("Task: ");
			//Create a textfield called input
			TextField taskInput = new TextField();

			//CheckBox blocked = new CheckBox("Blocked");

			//Create a button that when clicked creates the ticket
			Button create = new Button("Add Ticket");

			//Create the grid pane
			GridPane gp = createGridPane();
			gp.add(task, 0, 2);
			gp.add(taskInput, 1, 2);
			gp.add(create, 0, 4);
			Scene s = new Scene(gp,400,200);
			window.setScene(s);
			window.show();

			//when the create ticket button is clicked
			create.setOnAction(e2->{
				Ticket newTicket;
				String ticketTask = taskInput.getText();
				newTicket = new Ticket(0,ticketTask);
				Lane lane = kc.getALane("Backlog");
				newTicket.setTicketID(kc.incrementID());
				lc = new LaneController(lane);
				lc.addATicket(newTicket);
				updateBoard();
				window.close();
			});
		});


		//when "insert lane" is clicked
		createNewLane.setOnAction(e->{
			createNewLane();
		});
		deleteALane.setOnAction(l4->{
			deleteALane();
		});
		splitLane.setOnAction(er ->{
			createSubLane();
		});
		deleteSubLane.setOnAction(op -> {
			deleteSubLane();
		});
	}

	//This method create a grid pane and sets the padding,VGap and HGap, and returns it
	private static GridPane createGridPane(){
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10, 10, 10, 10));
		gp.setVgap(15);
		gp.setHgap(10);
		return gp;
	}
	/**
	 * Method for Creating a default kanban board
	 */
	private void createKanbanBoard(){
		window = new Stage();
		window.setTitle("New kanban Board");

		Label owner = new Label("Kanban Board Creator:");
		TextField ownerInput = new TextField();

		Button create = new Button("Create Kanban Board");

		GridPane gp = createGridPane();
		gp.setId("grid");
		gp.add(owner, 0, 2);
		gp.add(ownerInput, 1, 2);
		gp.add(create, 0, 4);

		Scene s = new Scene(gp,400,200);
		window.setScene(s);
		window.show();

		create.setOnAction(e2->{
			String name = ownerInput.getText();
			//Setting the name of the kanban
			kanban = new Kanban(name);
			//Setting control
			kc = new kanbanController(kanban);
			//Creating default lanes
			kc.createDefaultKanban();
			updateBoard();
			window.close();
			//window.setTitle("Kanban Board - "+name);
		});
	}

	private StackPane createTicket(Ticket t){
		Text task = new Text(t.getTicketName());

		///////////////////////////////////
		////Backing for the rectangles////
		/////////////////////////////////
		Rectangle baseBox = new Rectangle();
		baseBox.setHeight(TICKET_HEIGHT+10);
		baseBox.setWidth(TICKET_WIDTH+10);
		baseBox.setFill(Color.WHITE);

		////////////////////////////
		///Making the Sticky note//
		//////////////////////////
		Rectangle tickBox = new Rectangle();
		tickBox.setHeight(TICKET_HEIGHT);
		tickBox.setWidth(TICKET_WIDTH);

		/*
		 *Setting the colour of the rectangle
		 *If it is in a blocked status it will be red
		 *		
		 */

		if (t.isBlockedStatus()){
			tickBox.setFill(Color.RED);
		}else{
			tickBox.setFill(Color.YELLOW);
		}



		//////////////////////////////////
		//Placing these in a stack pane //
		/////////////////////////////////
		StackPane layout = new StackPane();
		layout.getChildren().addAll(baseBox,tickBox, task);
		layout.setId("TickBox");
		//Creating the context menu
		ContextMenu ticketContext = new ContextMenu();
		MenuItem moveTick = new MenuItem("Move A Ticket");
		//Setting what happens when move a ticket  is clicked
		moveTick.setOnAction(e ->{
			moveATicket(t);
		});
		MenuItem moveTicketSub = new MenuItem("Move To SubLane");
		moveTicketSub.setOnAction(q ->{
			moveToSubLane(t);
		});
		MenuItem editTicket = new MenuItem("Edit A Ticket");
		editTicket.setOnAction(r -> {
			editTicket(t);
		});
		MenuItem removeSub = new MenuItem("Remove From local done lane");
		removeSub.setOnAction(pop ->{
			removeFromSub(t);

		});

		//Adding the items to the context menu
		ticketContext.getItems().addAll(moveTick,editTicket,moveTicketSub,removeSub);

		//Adding the event listener for the context menu to the layout
		layout.addEventHandler(MouseEvent.MOUSE_CLICKED,  (MouseEvent  me) ->  {
			if (me.getButton() == MouseButton.SECONDARY  || me.isControlDown())  {
				ticketContext.show(layout, me.getScreenX(), me.getScreenY());
			}  else  {
				ticketContext.hide();
			}
		});
		return layout;

	}

	private StackPane createTitle(String title, boolean sub){
		StackPane pane = new StackPane();
		Text t = new Text(title);
		if (sub == false){
			t.setFont(Font.font("Aerial", FontWeight.BOLD, 12));
		}else{
			t.setFont(Font.font("Aerial", FontPosture.ITALIC, 12));
		}
		pane.getChildren().addAll(t);
		pane.setMinWidth(TICKET_WIDTH);
		pane.setId("titlePane");
		return pane;
	}

	private void moveATicket(Ticket t){
		boolean outcome = kc.moveATicket(t);
		System.out.println("The move operation for ticket "+t.getTicketName()+" was a: ");
		if (outcome == true){
			System.out.print("success");
			updateBoard();

		}else{
			Alert alert = new Alert(AlertType.ERROR, "Ticket is already in done lane");
			alert.show();
			System.out.print("failure");
		}
	}

	private void moveToSubLane(Ticket t){
		boolean outcome = kc.moveTicketToSub(t);
		if (outcome == false){
			window = new Stage();
			GridPane gp = createGridPane();
			window.setTitle("Error");
			Text windowTitle = new Text("Lane does not have a local done"
					+ "\nWould you like to create one");
			gp.add(windowTitle, 1, 1);
			Button yes =  new Button("Yes");
			gp.add(yes, 2, 3);
			Button no =  new Button("No");
			gp.add(no, 4, 3);

			Scene scene = new Scene(gp,300,130);
			window.setScene(scene);
			window.show();

			yes.setOnAction(e->{
				Lane lane = kc.laneHasTicket(t);
				lc = new LaneController(lane);
				boolean splitOutcome = lc.splitALane();
				if (splitOutcome == true){
					kc.replaceALane(lane);
					window.close();
					Alert alert = new Alert(AlertType.CONFIRMATION, "Success");
					alert.show();
				}
				else{
					window.close();
					Alert alert = new Alert(AlertType.ERROR, "Operation has failed, lane cannot be split");
					alert.show();
				}
			});
			no.setOnAction(r -> {
				window.close();
			});
		}
		updateBoard();
	}

	private void updateBoard(){
		ArrayList<Lane> listOfLanes = kanban.getLaneArray();
		mainGrid.getChildren().clear();
		int countColumn = 0;
		for(int count =0;  count<listOfLanes.size(); count++){

			//Getting the current lane
			Lane lane = listOfLanes.get(count);
			//Setting the title
			//adding it to the top of its column
			StackPane pane = createTitle(lane.getName(), false);
			mainGrid.add(pane,countColumn,0);
			//Getting the array of tickets from the lane
			ArrayList<Ticket> listOfTickets = lane.getLaneArr();
			//Adding in all the tickets
			for (int i =0; i<listOfTickets.size(); i++){				
				Ticket t = listOfTickets.get(i);
				mainGrid.add(createTicket(t), countColumn, 1+i);
			}
			//Part where we add sub lanes
			//Checking to see if it contains a sub lane
			if (lane.getBooleanArr()[3] == true){
				//Incrementing the column count
				countColumn++;
				//Creating a title
				StackPane subPane = createTitle("sub - "+lane.getName(), true);
				//Placing it a row below
				mainGrid.add(subPane, countColumn, 0);
				ArrayList<Ticket> subList = lane.getSubLaneArr();
				//Inserting in all tickets
				for (int y=0; y<subList.size(); y++){
					Ticket ticket = subList.get(y);
					mainGrid.add(createTicket(ticket), countColumn, 1+y);

				}
			}
			countColumn++;

		}

	}


	private void editTicket(Ticket ticket){
		window = new Stage();
		GridPane gp = new GridPane();
		window.setTitle("Edit Ticket");
		Text windowTitle = new Text ("Edit a Ticket");
		gp.add(windowTitle, 0, 0);

		Label task = new Label("Task Name:");
		gp.add(task, 0, 1);
		TextField taskInput = new TextField(ticket.getTicketName());
		gp.add(taskInput, 1, 1);

		Label blocked = new Label("Blocked Status");
		gp.add(blocked, 0, 2);
		CheckBox blockedStatus = new CheckBox();
		blockedStatus.setSelected(ticket.isBlockedStatus());
		gp.add(blockedStatus, 1, 2);

		Label priority = new Label("Set Priority");
		gp.add(priority, 0, 3);
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(100);
		slider.setBlockIncrement(10);
		slider.setValue(ticket.getPriority());
		gp.add(slider,1,3);

		Button done = new Button("Done");
		done.setOnAction(e-> {
			ticket.setTicketName(taskInput.getText());
			System.out.println("Name :"+taskInput.getText());
			ticket.setBlockedStatus(blockedStatus.isSelected());
			System.out.println("Status: " + blockedStatus.isSelected());
			ticket.setPriority((int) slider.getValue());
			System.out.println("Priority: "+slider.getValue());
			kc.findATicketAndReplace(ticket);
			updateBoard();
			window.close();
		});
		gp.add(done, 2, 4);
		Scene scene = new Scene(gp,400,400);
		window.setScene(scene);
		window.show();
	}

	private void createNewLane(){
		window = new Stage();
		window.setTitle("Create New Lane");

		Label laneName = new Label("Lane Name: ");
		TextField nameInput = new TextField();

		Label lanePos= new Label("Lane Position: ");
		TextField posInput = new TextField();

		Button create = new Button("Insert Lane");

		GridPane gp = createGridPane();
		gp.add(laneName, 0, 2);
		gp.add(nameInput, 1, 2);
		gp.add(lanePos, 0, 4);
		gp.add(posInput, 1, 4);
		gp.add(create, 0, 5);
		Scene s = new Scene(gp,400,200);
		window.setScene(s);
		window.show();

		create.setOnAction(e2->{
			String name = nameInput.getText();
			int position = Integer.parseInt(posInput.getText());
			boolean validate = kc.createAUserLane(name, position);

			if (validate == false){
				Alert alert = new Alert(AlertType.ERROR, "You have entered an inavalid name or position");
				alert.show();
			}
			else{
				window.close();
				updateBoard();
			}
		});
	}

	private void deleteALane(){
		window = new Stage();
		window.setTitle("Delete");

		Label laneName = new Label("Lane Name: ");
		TextField nameInput = new TextField();

		Button create = new Button("Delete");

		GridPane gp = createGridPane();
		gp.add(laneName, 0, 2);
		gp.add(nameInput, 1, 2);
		gp.add(create, 0, 4);

		Scene s = new Scene(gp,400,200);
		window.setScene(s);
		window.show();

		create.setOnAction(e2->{
			boolean validate = kc.removeALane(nameInput.getText());

			if (validate == false){
				Alert alert = new Alert(AlertType.ERROR, 
						"You have either entered an invalid name "
								+ "\nor the lane cannot be deleted"
								+ "\nor it contains tickets");
				alert.show();
			}
			else{
				window.close();
				updateBoard();
			}
		});
	}

	private void createSubLane(){
		window = new Stage();
		window.setTitle("Create Sub-Lane");

		Label laneName = new Label("Lane Name: ");
		TextField nameInput = new TextField();

		Button create = new Button("Create Sub");

		GridPane gp = createGridPane();
		gp.add(laneName, 0, 2);
		gp.add(nameInput, 1, 2);
		gp.add(create, 0, 4);

		Scene s = new Scene(gp,400,200);
		window.setScene(s);
		window.show();

		create.setOnAction(e2->{
			Lane lane = kc.findALane(nameInput.getText());
			if (lane!=null){
				lc = new LaneController(lane);
				boolean outcome = lc.splitALane();
				if (outcome == true){
					kc.replaceALane(lane);
					System.out.println("Opeartion of splitting lane "+lane.getName()+ "was a success");
					window.close();
					updateBoard();
				}
				else {
					Alert alert = new Alert(AlertType.ERROR, "You have entered an inavalid name or position");
					alert.show();
				}
			}
		});

	}

	private void removeFromSub(Ticket ticket){
		boolean outcome = kc.removeFromSub(ticket);
		if (outcome == false){
			Alert alert = new Alert(AlertType.ERROR, "Not in a local done lane");
			alert.show();
		}
		updateBoard();
	}


	private void deleteSubLane(){
		window = new Stage();
		window.setTitle("Delete Sub-Lane");

		Label laneName = new Label("Lane Name: ");
		TextField nameInput = new TextField();

		Button delete = new Button("Create Sub");

		GridPane gp = createGridPane();
		gp.add(laneName, 0, 2);
		gp.add(nameInput, 1, 2);
		gp.add(delete, 0, 4);

		Scene s = new Scene(gp,400,200);
		window.setScene(s);
		window.show();
		delete.setOnAction(e ->{
			Lane lane = kc.getALane(nameInput.getText());
			if (lane != null){
				lc = new LaneController(lane);
				boolean splitWorked = lc.unSplitLane();
				if (splitWorked == true){
					kc.replaceALane(lane);
					window.close();
					updateBoard();
				}
				else{
					//Lane has Tickets or not split
					Alert alert = new Alert(AlertType.ERROR, "Lane has tickets or does not have a local done"  );
					alert.show();
				}
			}else{
				//Lane does not Exist
				Alert alert = new Alert(AlertType.ERROR, "Lane by name "+nameInput.getText()+" does not exist");
				alert.show();
			}


		});
	}
}