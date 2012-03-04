package com.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import com.backend.DBObject;

public class User extends DBObject {

	public static int INVALID_USER = -1;
	private int id;
	private String name;
	private String password;
	private int salt;
	private boolean access;
	private String achievements;
	
	private void setValues(int id, String name, String achievements) {
		this.id = id;
		this.name = name;
		this.achievements = achievements;		
	}

	public User() {
		super(DBObject.userTable);
		id = 2;
		name = "";
		achievements = "";
		setValues(-1, "", "");
	}

	public User(int id) {
		super(DBObject.userTable);
		id = -1;
		StringBuilder query = new StringBuilder();
		query.append("SELECT id, name, achievements FROM " + userTable + " ");
	    query.append("WHERE id = \"" + Integer.toString(id) + "\";");
		
		ResultSet rs = getResults(query.toString());
		try {
			if(rs.next()) setValues(rs.getInt(1), rs.getString(2), rs.getString(3));
			else setValues(-1, "", "");			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public User(int id, String name, String achievements) {
		super(DBObject.userTable);
		setValues(id, name, achievements);
	}

	public User(String name) {
		super(DBObject.userTable);
		id = -1;
		StringBuilder query = new StringBuilder();
		query.append("SELECT id, name, achievements FROM " + userTable + " ");
		query.append("WHERE name = \"" + name + "\";");
		
		System.out.println(query.toString());
		ResultSet rs = getResults(query.toString());
		try {
			if(rs.next()) setValues(rs.getInt(1), rs.getString(2), rs.getString(3));
			else setValues(-1, "", "");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() { return id; }
	public String getUserName() { return name; }
	public String getAchievements() { return achievements; }

	public int getId(String name) {
		return id;
	}

	public static User getUser(String name) {
		return new User(name);
	}
	
	public static User createUser(String name, String password) {
		if(getUser(name).getId() != -1) return null;

		Random random = new Random();
		int salt = random.nextInt();
		password = password + Integer.toString(salt);
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA");
			password = hexToString(md.digest(password.getBytes()));

			StringBuilder query = new StringBuilder("INSERT INTO ");
			query.append(userTable + "(");
			query.append("name, password, salt, access, achievements) ");
			query.append( " VALUES(");
			query.append("\"" + name + "\", ");
			query.append("\"" + password + "\", ");
			query.append(salt + ", ");
			query.append("\"0\", ");
			query.append("\"" + "\"); ");

			System.out.println(query.toString());
			statement.executeUpdate(query.toString());

			return new User(name);
		}
		catch(NoSuchAlgorithmException e) { 
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean authenticateUser(String name, String password) {
		User user = new User(name);
		System.out.println("Password = " + user.password);
		if(user.getId() == -1) return false;
		
		System.out.println("Password = " + user.password);

		/*
		try {
		}
		catch(NoSuchAlgorithmException e) { 
			e.printStackTrace();
		}
*/		
		return true;
	}
	
	
	// taken from Cracker assignment
	private static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}	
	
	public String getName(int id) throws SQLException {
		String query = "SELECT name FROM " + currentTable + " WHERE id = " + id;
		ResultSet rs = super.getResults(query);
		if (rs.next()) return rs.getString("name");
		else return null;
	}

//	DB implemenation in User.java  getUsers(String userFilter);
/*
	try {
		stmt.executeQuery("SELECT * FROM "+ TABLE +" WHERE name LIKE \"%" + name +"%\"" );
		ResultSet rs = stmt.getResultSet();
		
		ArrayList<User> usersList = new ArrayList<User>();
		while(rs.next()){
			User user = new User();
			user.id = rs.getInt("id");
			user.name = rs.getString("name");
			user.password = rs.getString("password");
			user.salt = rs.getInt("salt");
			user.access = rs.getBoolean("access");
			usersList.add(user);
			return usersList;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/

}
 