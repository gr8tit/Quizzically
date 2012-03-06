package com.backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.backend.DBObject;
import com.models.Activity;
import com.models.Quiz;
import com.models.User; // only for getting recent friend activities

public class ActivityManager extends DBObject {
	/*
	 * Methods for retrieving list of activity:
	 *  - by userid
	 *  - by most recent overall
	 *  - on a given day
	 *  - sorted by top performing
	 * 
	 */
	public ActivityManager() {
		/* empty initializer */
		debug = false;
	}
	
	private String base = "select * from " + DBObject.activityTable;
	/**TODO: NOT TESTED
	 * primary retrieval method; converts ResultSet to ordered
	 * list of Activities. Select columns from base string must match
	 * the ones being parsed here.
	 */
	private List<Activity>convertToList(ResultSet r) throws SQLException {
		List<Activity> result = new ArrayList<Activity>();
		while(r.next()) {
			result.add(new Activity(r.getInt("id"),
									r.getInt("user_id"), 
									r.getInt("quiz_id"),
									r.getInt("score"),
									r.getTimestamp("timestamp"),
									r.getDouble("timeTaken")
								   ));
		}
		return result;
	}
	
	boolean debug;
	// nDEBUG
	
	/**TODO: NOT TESTED
	 * specific - gets Activity for this user
	 */
	public List<Activity>getbyUserId(int userid) {
		if(!this.conPrepare(base + filter + limit)) return null;
		try {
			// filter requires a param name and a value
			prepStatement.setString(1, "user");
			prepStatement.setInt(2, userid);
			
			if(debug) System.out.println(prepStatement.toString());
			ResultSet r = this.getPrepared();
			
			return convertToList(r);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** TODO: NOT TESTED
	 * generic - gets recent activity across all sites
	 * @return
	 */
	public List<Activity>getRecent() {
		if(!this.conPrepare(base + sorted_desc + limit)) return null;
		try {
			// sorted_desc requires a column name
			prepStatement.setString(1, "TIMESTAMP");
			
			if(debug) System.out.println(prepStatement.toString());
			ResultSet r = this.getPrepared();
			return convertToList(r);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** TODO: NOT TESTED
	 * gets all activity for today
	 * @return
	 */
	public List<Activity>getToday() {
		if(!this.conPrepare(base + today + limit)) return null;
		try {
			ResultSet r = this.getPrepared();
			return convertToList(r);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** TODO: NOT TESTED
	 * gets a specific column of user scores. 
	 * Uses the "extended" constructor for an activities
	 * class, which takes in an extra username field.
	 * Makes it very convenient to get a list activites,
	 * scores, and usernames ready for display
	 * 
	 *  query: 
	 *  select * from Quiz_activities where quiz_id = quizid order by score desc
	 *  	INNER JOIN Quiz_activites ON
	 *  	Quiz_user.username where id = Quiz_activities.user_id 
	 */
	private String topScore_complex_query = "select * from Quiz_activities where quiz_id = ? order by score desc " +
			" INNER JOIN " + DBObject.activityTable +" ON " + "Quiz_user.username " +
			" WHERE id = Quiz_activities.user_id " + limit;
	
	public List<Activity> getTopScores(int quizid) {
		if(! this.conPrepare(topScore_complex_query)) return null;
		try {
			// where user_id = userid
			prepStatement.setString(1, "quiz_id");
			prepStatement.setInt(2, quizid);
			prepStatement.setString(3,"score");
			ResultSet r = prepStatement.executeQuery();
			List<Activity> result = new ArrayList<Activity>();
			while(r.next()) {
				result.add(new Activity(r.getInt("id"),
										r.getInt("user_id"), 
										r.getInt("quiz_id"),
										r.getInt("score"),
										r.getTimestamp("timestamp"),
										r.getDouble("timeTaken"),
										r.getString("username")
									   ));
			}
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String averageScore = "select AVG(score) from " + DBObject.activityTable;
	
	/**
	 * returns average score for a given user
	 * @param quizid
	 * @return
	 */
	public double getAverageScoreUser(int userid) {
		if(! this.conPrepare(averageScore + filter)) return 0;
		try {
			// where user_id = userid
			prepStatement.setString(1, "user_id");
			prepStatement.setInt(2, userid);
			ResultSet r = prepStatement.executeQuery();
			return r.getDouble("AVG(score");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * returns average score for a given quiz
	 * @param quizid
	 * @return
	 */
	public double getAverageScoreQuiz(int quizid) {
		if(! this.conPrepare(averageScore + filter)) return 0;
		try {
			// where user_id = userid
			prepStatement.setString(1, "quiz_id");
			prepStatement.setInt(2, quizid);
			ResultSet r = prepStatement.executeQuery();
			return r.getDouble("AVG(score");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**TODO: NOT TESTED
	 * generic - gets top activity across whole site
	 */
	public List<Activity>getTopActivity() {
		if(!this.conPrepare(base + sorted_desc + limit)) return null;
		try {
			// sorted_desc requires a column name
			prepStatement.setString(1, "SCORE");
			if(debug) System.out.println(prepStatement.toString());
			ResultSet r = this.getPrepared();
			return convertToList(r);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**TODO: NOT TESTED
	 * specific - gets top activity for this user
	 * @param userid
	 * @return
	 */
	public List<Activity>getTopActivity(int userid) {
		if(!this.conPrepare(base + filter + sorted_desc + limit)) return null;
		try {
			// set the filter of user_id to useid
			prepStatement.setString(1, "user_id");
			prepStatement.setInt(2, userid);
			
			// find top scores
			prepStatement.setString(3, "SCORE");
			if(debug) System.out.println(prepStatement.toString());
			ResultSet r = this.getPrepared();
			return convertToList(r);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** TODO: NOT TESTED
	 * specific - get friends' top activity specific to this user
	 */
	// parts copied from UserManager.java ; not shared
	private String recentFriendActivity_complex_string =
		base + 
		"WHERE id IN " + 
		"((SELECT user1_id from Quiz_friendship WHERE friendType = 3 AND user2_id = ?) " +
		"UNION" +
		"(SELECT user2_id from Quiz_friendship WHERE friendType = 3 AND user1_id = ?))" +
		recent;
	
	public List<Activity>getRecentFriendActivity(int userid, List<User>friends) {
		if(!this.conPrepare(recentFriendActivity_complex_string)) return null;
		try {
			// sorted_desc requires a column name
			prepStatement.setInt(1, userid);
			prepStatement.setInt(1, userid);
			if(debug) System.out.println(prepStatement.toString());
			ResultSet r = this.getPrepared();
			return convertToList(r);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
