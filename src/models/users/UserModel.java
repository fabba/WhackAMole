package models.users;

/**
 * Model for a user playing this game, just contains data.
 */
public class UserModel {

	private int id;
	private String name;
	
	public UserModel(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
}
