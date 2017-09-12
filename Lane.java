package Model;

import java.util.ArrayList;
/**
 * 
 * @author Version 1.5
 * Has SubLane Function
 */
public class Lane implements java.io.Serializable{
	//The array list for all the tickets
		private ArrayList <Ticket> LaneArr; 
		private ArrayList <Ticket> SubLaneArr; 
		
		private String name;
		/*
		 * 1. Can make a sub Lane
		 * 2. Can edit the name after created 
		 * 3. Can add a ticket
		 * 4. The Lane HAS a sub
		 */
		private boolean booleanArr [];
		private final int NUM_BOOLEANS = 4;
		
		public Lane(){
			LaneArr = new ArrayList<Ticket>();
			SubLaneArr= new ArrayList<Ticket>();
			booleanArr = new boolean[NUM_BOOLEANS];
		}
		public ArrayList<Ticket> getLaneArr() {
			return LaneArr;
		}
		public void setLaneArr(ArrayList<Ticket> laneArr) {
			LaneArr = laneArr;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean[] getBooleanArr() {
			return booleanArr;
		}
		public void setBooleanArr(boolean[] booleanArr) {
			this.booleanArr = booleanArr;
		}
		public ArrayList<Ticket> getSubLaneArr() {
			return SubLaneArr;
		}
		public void setSubLaneArr(ArrayList<Ticket> sublaneArr) {
			SubLaneArr = sublaneArr;
		}
		
}