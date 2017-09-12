package Controller;

import java.util.ArrayList;
import Model.Lane;
import Model.Ticket;
/**
 * 
 * @version 1.5
 * Added split lane functionality
 */
public class LaneController {
	private Lane lane;

	public LaneController(Lane lame){
		lane = lame;
	}
	/**
	 * If a user enters "backlog" a backlog lane will be created
	 * If a user enters "ready" it will be set to a ready lane
	 * If a user enters "in progress" an In Progress lane would be created
	 * If a user enters "done" it will be set to done
	 * If the name does not match any of these parameters it will be
	 * deemed to be a user lane and have its name set to what was passed as
	 * an argument  
	 * @param name
	 */
	public void createLane(String name){
		if (name.equals("backlog")){
			boolean permission[] = lane.getBooleanArr();
			permission[0] = false;
			permission[1] = false;
			permission[2] = true;
			lane.setBooleanArr(permission);
			lane.setName("Backlog");
		}
		else if (name.equals("ready")){
			boolean permission[] = lane.getBooleanArr();
			permission[0] = true;
			permission[1] = false;
			permission[2] = false;
			lane.setBooleanArr(permission);
			lane.setName("Ready");
		}
		else if(name.equals("in progress")){
			boolean permission [] = lane.getBooleanArr();
			permission[0] = true;
			permission[1] = false;
			permission[2] = false;
			lane.setBooleanArr(permission);
			lane.setName("In Progress");
		}
		else if (name.equals("done")){
			boolean permission[] = lane.getBooleanArr();
			lane.setName("Done");
			permission[0] = true;
			permission[1] = false;
			permission[2] = false;
			lane.setBooleanArr(permission);
			lane.setName("Done");
		}
		else {
			boolean permission[] = lane.getBooleanArr();
			permission[0] = true;
			permission[1] = true;
			permission[2] = false;
			lane.setBooleanArr(permission);
			lane.setName(name);
		}
	}
	/**
	 * Allows the control of a different lane
	 * @param lane
	 */
	public void changeControlTo(Lane lane){
		this.lane = lane;
	}
	public ArrayList<Ticket> sortByPriority(ArrayList<Ticket> arr){
		//bubble sort
		Ticket t1,t2;
		int p1,p2;
		boolean swapped;
		do{
			swapped = false;
			for(int i=0; i<lane.getLaneArr().size()-1; i++){
				t1 = arr.get(i);
				p1 = t1.getPriority();

				t2 = arr.get(i+1);
				p2 = t2.getPriority();

				if(p1<p2){
					arr.remove(i);
					arr.add(i, t2);
					arr.remove(i+1);
					arr.add(i+1, t1);
					swapped = true;
				}
			}
		}while(swapped == true);
		return arr;
	}
	/**
	 * Adds a ticket and sorts the list by priority after being added
	 * Ticket will only be added if the lane has adding enabled
	 * @param ticket that is to be added
	 * @return the outcome of the operation if false the op failed
	 */
	public boolean addATicket(Ticket ticket){
		boolean outcome = false;
		//Checking if a ticket can be added
		boolean booleanArr [] = lane.getBooleanArr();
		if (booleanArr[2] == true){
			//Getting the lane array
			ArrayList <Ticket> arr  = lane.getLaneArr();
			boolean found = false;
			for (int i=0; i<arr.size() && found == false; i++){
				Ticket t = arr.get(i);
				if (t.getTicketID() == ticket.getTicketID()){
					found = true;
				}
			}
			if(found == false){
				//Adding the ticket 
				arr.add(ticket);
				//Sorting the array and returning it
				lane.setLaneArr(sortByPriority(arr));
				outcome = true;
			}
		}
		return outcome;		
	}
	/**
	 * Removes a ticket from the main lane, just pass in the ticket 
	 * that needs to be remove. If the ticket does not exist in 
	 * this lane the method will return false 
	 * @param ticket The ticket that needs to be removed
	 * @return A boolean outcome of the operation
	 */
	public boolean removeATicket(Ticket ticket){
		boolean outcome = false;
		ArrayList<Ticket> arr = lane.getLaneArr();
		for (int i=0; i<arr.size(); i++){
			Ticket t = arr.get(i);
			if (t.getTicketID() == ticket.getTicketID()){
				arr.remove(i);
				sortByPriority(arr);
				lane.setLaneArr(arr);
				outcome = true;
			}
		}
		return outcome;
	}
	/**
	 * Splits the lane provided it has the correct permissions
	 * @return If it returns the operation was a success
	 */
	// @ TODO test
	public boolean splitALane(){
		boolean outcome = false;
		boolean arr [] = lane.getBooleanArr();
		if (arr[0] == true){
			arr[3] = true;
			lane.setBooleanArr(arr);
			outcome = true;
		}
		return outcome;
	}
	
	public boolean unSplitLane(){
		boolean outcome = false;
		if (lane.getBooleanArr()[3] != false){
			if (lane.getSubLaneArr().size()>0){
				
			}else{
				lane.getBooleanArr()[3] = true;
				outcome = true;
			}
		}
		return outcome;
	}
	/**
	 * Will add a ticket to a local done lane provided that ticket
	 * already exists in the main lane  and that lane has already 
	 * has been split. Also The ticket cannot be added if it is 
	 * already in there
	 * @param t the ticket that needs to be added
	 * @return The outcome of the operation if true was success 
	 */
	// @ TODO test
	public boolean addATicketToSub(Ticket t){
		boolean outcome = false;
		boolean booleanArr [] = lane.getBooleanArr();
		if (booleanArr[0] == true && booleanArr[3]== true){
			ArrayList<Ticket> subArray = lane.getSubLaneArr();
			ArrayList<Ticket> mainArray = lane.getLaneArr();
			if (mainArray.contains(t)){
				//Adding it to the sub array
				subArray.add(t);
				//Removing it from the main array
				mainArray.remove(t);
				//Sorting the sub array 
				sortByPriority(subArray);
				//Returning the lanes
				lane.setSubLaneArr(subArray);
				lane.setLaneArr(mainArray);
				outcome = true;
			}
		}
		return outcome;
	}
	/**
	 * Moves a ticket out of the local done lane into the sub lane
	 * 
	 * @param The ticket to be moved
	 * @return The outcome of the operation 
	 */
	// @ TODO test 
	public boolean moveATicketFromSubToMain(Ticket t){
		boolean outcome = false;
		boolean booleanArr [] = lane.getBooleanArr();
		if (booleanArr[0] == true && booleanArr[3]== true){
			ArrayList<Ticket> subArray = lane.getSubLaneArr();
			ArrayList<Ticket> mainArray = lane.getLaneArr();
			if (subArray.contains(t)){
				//Adding it to the main array
				mainArray.add(t);
				//Removing it from the sub array
				subArray.remove(t);
				//Sorting the sub array 
				sortByPriority(mainArray);
				//Returning the lanes
				lane.setSubLaneArr(subArray);
				lane.setLaneArr(mainArray);
				outcome = true;
			}
		}	
		return outcome;
		
	}
	/**
	 * Deletes a ticket from a local done lane provided it exists 
	 * and the lane has a local done lane
	 * @param The ticket that needs to be deleted
	 * @return The outcome of the operation
	 */
	// @ TODO test
	public boolean removeATicketFromASub(Ticket t){
		boolean outcome = false;
		boolean booleanArr [] = lane.getBooleanArr();
		if (booleanArr[1] == true && booleanArr[3]== true){
			ArrayList<Ticket> subArray = lane.getSubLaneArr();
			subArray.remove(t);
			lane.setSubLaneArr(subArray);
			outcome = true;
		}
		return outcome;
	}
	
}