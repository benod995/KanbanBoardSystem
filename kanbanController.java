package Controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Model.Kanban;
import Model.Lane;
import Model.Ticket;
/**
 * @version  1.0
 * 
 */
public class kanbanController {
	private Kanban kanbanBoard;

	public kanbanController(Kanban bored){
		kanbanBoard = bored;
	}
	/**
	 * Increments the ID and returns an Id to the user 
	 * this is used whenever creating a ticket
	 * @return
	 */
	// @ TODO test
	public float incrementID(){
		float id = kanbanBoard.getUniqueID();
		id++;
		kanbanBoard.setUniqueID(id);
		return id;
	}

	/**
	 * Gets a lane with a given name
	 * @param name The name of the lane you want to get
	 * @return A lane
	 */
	public Lane getALane(String name){
		ArrayList<Lane> arr = kanbanBoard.getLaneArray();
		int size = arr.size();
		boolean found = false;
		Lane x = null;
		for (int i=0; i<size && found ==false; i++){
			x = arr.get(i);
			if (x.getName().equals(name)){
				found = true;
			}
		}
		return x;
	}
	/**
	 * Creates a default kanban with four lanes
	 */ 
	public void createDefaultKanban(){
		ArrayList<Lane> arr = kanbanBoard.getLaneArray();
		Lane backlog = new Lane ();
		Lane ready = new Lane ();
		Lane inProgress = new Lane ();
		Lane done = new Lane ();
		LaneController controlLane = new LaneController(backlog);
		controlLane.createLane("backlog");
		arr.add(backlog);
		controlLane.changeControlTo(ready);
		controlLane.createLane("ready");
		arr.add(ready);
		controlLane.changeControlTo(inProgress);
		controlLane.createLane("in progress");
		arr.add(inProgress);
		controlLane.changeControlTo(done);
		controlLane.createLane("done");
		arr.add(done);
		kanbanBoard.setLaneArray(arr);
	}

	/**
	 * Creates a user generated lane at a given point
	 * provided it does not have the same name as another a lane
	 * and provided it does not have a position less than zero or greater the the final 
	 * element
	 * @param name The name of the lane your going to add
	 * @param pos Where you want to place it
	 * @return if outcome false operation failed
	 */
	// @ TODO test
	public boolean createAUserLane(String name, int pos){
		boolean outcome = false;
		boolean badName = false;
		ArrayList<Lane> arr = kanbanBoard.getLaneArray();
		for (int i=0; i<arr.size() && badName == false; i++){
			Lane check = arr.get(i);
			if (check.getName().toLowerCase().equals(name.toLowerCase())){
				badName = true;
				outcome = false; 
			}
		}
		if (pos  > 0 || pos >= arr.size()-1){
			Lane adderLane = new Lane();
			LaneController lc = new LaneController(adderLane);
			lc.createLane(name);
			arr.add(pos, adderLane);
			kanbanBoard.setLaneArray(arr);
			outcome = true;
		}
		else{
			outcome = false;
		}

		return outcome;
	}
	/**
	 * Removes a lane by the name provided its a user created lane
	 * @param x
	 * @return
	 */
	// @ TODO test
	public boolean removeALane(String name){
		boolean outcome = false;
		ArrayList<Lane> arr = kanbanBoard.getLaneArray();
		for (int i=0; i<arr.size() && outcome == false; i++){
			Lane l = arr.get(i);
			if (l.getBooleanArr()[1] == true && l.getLaneArr().size()<1){
				if (l.getName().equals(name)){
					arr.remove(i);
					kanbanBoard.setLaneArray(arr);
					outcome = true;
				}
			}
		}
		return outcome;
	}
	//@ TODO TEST please seriously
	public boolean moveATicket(Ticket t){
		boolean outcome = false;
		boolean found = false;
		ArrayList<Lane> arr = kanbanBoard.getLaneArray();
		for (int i=0; i<arr.size() && found == false; i++){
			Lane lane = arr.get(i);
			ArrayList <Ticket> tickArr = lane.getLaneArr();
			int index = tickArr.indexOf(t);
			if (index >= 0 && i< arr.size()-1){
				//Setting found to true so it will exit the loop
				found = true;
				//Removing the ticket from its home array
				tickArr.remove(index);
				//Returning the lane with the changes made
				lane.setLaneArr(tickArr);
				//Getting the lane one step in front
				lane = arr.get(i+1);
				//Getting its ticket array
				tickArr = lane.getLaneArr();
				//Adding the ticket to it
				tickArr.add(t);
				//Returing this ticket array
				lane.setLaneArr(tickArr);
				outcome = true;
			}
			//Checking to see if it is inside sub-lane
			else{
				tickArr = lane.getSubLaneArr();
				index = tickArr.indexOf(t);
				if (index >= 0 && i<= arr.size()-1){
					found = true;
					tickArr.remove(index);
					lane.setSubLaneArr(tickArr);
					lane = arr.get(i+1);
					tickArr = lane.getLaneArr();
					tickArr.add(t);
					lane.setLaneArr(tickArr);
					outcome = true;
				}
			}
		}
		return outcome;
	}
	/**
	 * Pass in an argument with a tickt of the same ID and it 
	 * will be replaced by ticket passed in
	 * 
	 * @param ticket
	 * @return The outcome of the operation
	 */
	public boolean findATicketAndReplace(Ticket ticket){
		boolean outcome = false;
		boolean found = false;
		ArrayList<Lane> listOfLane = kanbanBoard.getLaneArray();
		for (int i=0; i<listOfLane.size() && found == false; i++){
			Lane lane = listOfLane.get(i);

			ArrayList<Ticket> listOfTicket = lane.getLaneArr();
			for (int y=0; y<listOfTicket.size() && found == false; y++){
				Ticket t = listOfTicket.get(y);
				if (t.getTicketID() == ticket.getTicketID()){
					found = true;
					LaneController lc = new LaneController(lane);
					listOfTicket.remove(y);
					listOfTicket.add(y,ticket);
					lane.setLaneArr(lc.sortByPriority(listOfTicket));
					outcome = true;
				}
			}
			//If not found in the main array then check sub
			//First check if sub exists
			if (lane.getBooleanArr()[3] == true){
				listOfTicket = lane.getSubLaneArr();
				for (int o=0; o<listOfTicket.size(); o++){
					Ticket t = listOfTicket.get(o);
					if (t.getTicketID() == ticket.getTicketID()){
						found = true;
						LaneController lc = new LaneController(lane);
						listOfTicket.remove(o);
						listOfTicket.add(o,ticket);
						lane.setSubLaneArr(lc.sortByPriority(listOfTicket));
						outcome = true;
					}
				}
			}
		}
		return outcome;
	}
	/**
	 * Finds a lane matching the name passed in
	 * and returns it if it 
	 * 
	 * @param nameOfLane
	 * @return 
	 */
	public Lane findALane(String nameOfLane){
		ArrayList<Lane> arrayOfLanes = kanbanBoard.getLaneArray();
		Lane lane = null;
		boolean found = false;
		for (int i=0; i<arrayOfLanes.size() && found == false; i++){
			lane = arrayOfLanes.get(i);
			if (lane.getName().equals(nameOfLane)){
				found = true;
			}
		}
		return lane;
	}
	/**
	 * Checks if a lane has a ticket and will return this lane
	 * If no lane containing the ticket can be found it will 
	 * return a null value
	 * @param t The ticket that you want to search with
	 * @return
	 */
	public Lane laneHasTicket(Ticket t){
		ArrayList<Lane> listOfLane = kanbanBoard.getLaneArray();
		Lane lane = null;
		boolean found = false;
		for (int i=0; i<listOfLane.size() && found == false; i++){
			lane = listOfLane.get(i);
			if (lane.getLaneArr().contains(t)) {
				found = true;
			}
			else if(lane.getSubLaneArr().contains(t)) {
				found = true;
			}
		}

		return lane;
	}
	/**
	 * Adds a ticket to a local done lane
	 * Will only work if the ticket is in a 
	 * lane that has a local done lane 
	 * @param ticket the ticket that is to be moved
	 * @return the outcome of the operation
	 */
	public boolean moveTicketToSub(Ticket ticket){
		boolean outcome= false;
		Lane lane = laneHasTicket(ticket);
		if (lane.getBooleanArr()[3] == true){
			LaneController lc = new LaneController(lane);
			lc.addATicketToSub(ticket);
			replaceALane(lane);
			outcome = true;
		}
		return outcome;
	}
	public boolean replaceALane(Lane lane){
		boolean outcome = false;
		ArrayList<Lane> listOfLanes = kanbanBoard.getLaneArray();
		boolean found = false;
		for (int i=0; i<listOfLanes.size() && found == false; i++){
			Lane x = listOfLanes.get(i);
			if (x.getName().equals(lane.getName())){
				listOfLanes.remove(i);
				listOfLanes.add(i,lane);
				kanbanBoard.setLaneArray(listOfLanes);
				found = true;
				outcome = true;
			}
		}
		return outcome;
	}
	/**
	 * removes a ticket from the subLAne and places it in the main lane
	 * will only work if already in an local done lane
	 * @param ticket
	 * @return
	 */
	public boolean removeFromSub(Ticket ticket){
		boolean outcome = false;
		Lane lane = laneHasTicket(ticket);
		if (lane != null){
			LaneController lc =  new LaneController(lane);
			outcome = lc.moveATicketFromSubToMain(ticket);
			if (outcome == true) {
				replaceALane(lane);
			}
		}
		return outcome;
	}
	public boolean saveKanban(File file){
		boolean outcome = false;
		try {
			FileOutputStream fileOut =
					new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(kanbanBoard);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in "+file.getName());
			outcome = true;
		}catch(IOException i) {
			i.printStackTrace();
		}

		return outcome;
	}
}
