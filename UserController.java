package Controller;
import Model.User;
import Model.Kanban;

public class UserController {
	public void createProductOwner(String name, String password, Kanban board){
		User user = new User();
		kanbanController control = new kanbanController(board);
		float id = control.incrementID();
		String userName = user.PRODUCT_OWNER+Float.valueOf(id);
		user.setUserName(userName);
		user.setPassword(password);
	}
	public void createSoftwareDev(String name, String password, Kanban board){
		User user = new User();
		kanbanController control = new kanbanController(board);
		float id = control.incrementID();
		String userName = user.DEVLOPER+Float.valueOf(id);
		user.setUserName(userName);
		user.setPassword(password);
	}
}
