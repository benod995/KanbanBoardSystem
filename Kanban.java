package Model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 * 
 * @version 1
 *
 */
public class Kanban implements Serializable{
	private float uniqueID;
	private String name;
	private ArrayList <Lane> laneArray;
	private ArrayList <User> userArray;
	public Kanban(String name) {
		this.name = name;
		uniqueID = 0;
		laneArray = new ArrayList <Lane>();
		userArray = new ArrayList <User>();
	}
	public float getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(float uniqueID) {
		this.uniqueID = uniqueID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Lane> getLaneArray(){
		return laneArray;
	}
	public void setLaneArray(ArrayList<Lane> arr){
		this.laneArray = arr;
	}
	public ArrayList<User> getUserArray(){
		return userArray;
	}
	public void setUserArray(ArrayList<User> arr){
		this.userArray = arr;
	}

}
