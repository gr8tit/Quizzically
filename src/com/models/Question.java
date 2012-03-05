package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.backend.DBObject;


public class Question extends DBObject{
	
	
	public enum Type{
		QuestionResponse {
			public String toString() {
				return "question-response";
			}
		},
		FillInTheBlank {
			public String toString() {
				return "fill-in-the-blank";
			}
		},
		MultiAnswer {
			public String toString(){
				return "multi-answer";
			}
		},
		PictureResponse{
			public String toString(){
				return "picture-response";
			}
		},
		MultiChoice{
			public String toString(){
				return "multi-choice";
			}
		},
		MultiChoiceMultiAnswer{
			public String toString(){
				return "multi-choice-multi-answer";
			}
		},
		Matching{
			public String toString(){
				return "matching";
			}
		},
		AutoGenerated {
			public String toString(){
				return "auto-generated";
			}
		},
		Graded {
			public String toString(){
				return "graded";
			}
		}
		

	}
	private int id;
	private ArrayList<String> texts;
	private ArrayList<String> answers;
	private String url;
	private Type type;
	private String DELIMITER = "#";
	public Question(){
		super();
	}
	public Question(int id, ArrayList<String>texts, ArrayList<String>answers,String url,String type) 
	{
		this.id = id;
		this.texts = texts;
		this.answers = answers;
		this.url = url;

		for(Type item: Type.values()){
			if(type.equals(item.toString()))
				this.type = item;
		}
	}
	
	private String convertTextsToString(ArrayList<String> Texts){
		StringBuilder strBuilder = new StringBuilder();
		for(String item:Texts){
			strBuilder.append(item + DELIMITER);	
		}
		return strBuilder.toString();
	}
	private ArrayList<String> convertStringToTexts(String str){
		StringTokenizer tokens = new StringTokenizer(str,DELIMITER);
		ArrayList<String> list = new ArrayList<String>();
		while(tokens.hasMoreTokens()){
			list.add(tokens.nextToken());
		}
			
		return list;
	}
	public void addQuestion(int quiz_id, Question q){
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO " + DBObject.questionTable + " ");
		query.append("VALUE (null," + q.getType() + "," +convertTextsToString(q.getTexts())+"," + convertTextsToString(q.getAnswers()) +", " + quiz_id + "," + q.getUrl());
		updateTable(query.toString());	
		
	}
	public ArrayList<Question> getQuestions(int quiz_id) throws SQLException{
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM " + DBObject.questionTable + " ");
		query.append("WHERE quiz_id = " + quiz_id);
		ResultSet rs = getResults(query.toString());
		ArrayList<Question> qList = new ArrayList<Question>();
		while(rs.next()){
			Question newQuestion = new Question(rs.getInt("id"),convertStringToTexts(rs.getString("question")), convertStringToTexts(rs.getString("answers")),rs.getString("url"),
					rs.getString("type"));
			qList.add(newQuestion);
		}
		return qList;
	}
	/*
	 * depending on the question type, Answer's
	 * index can mean either:
	 * - the index of hte question with which to associate
	 * - 
	 */
	/*
	public class Answer {
		String url;
		String text;
		int index;
		public Answer(String url, String text, int index) {
			this.index =index;
			this.url = url;
			this.text = text;
		}
		
		@Override
		public boolean equals(Object other) {
		    // Not strictly necessary, but often a good optimization
		    if (this == other)
		      return true;
		    if (!(other instanceof Answer))
		      return false;
		    Answer otherA = (Answer) other;
		    return 
		      (url.equals(otherA.url) &&
		    	text.equals(otherA.text) &&
		    	index == otherA.index);
		}
		@Override
		public int hashCode() { 
		   int hash = 1;
		   hash = hash * 31 + text.hashCode();
		   return hash;
		}
	}
	*/
	
	/**
	 * check our answer with given input; does so 
	 * by sorting answers by index and then checking
	 * for equality among all answers.
	 * Raises question of how we are going to represent
	 * answers on the front-end
	 * 
	 * @param answers
	 * @return
	 */
	/*
	public boolean checkAnswer(List<Answer>answers) {
		// sort the answer by a certain heuristic 
		Collections.sort(answers, new Comparator<Answer>() {
			@Override
			public int compare(Answer arg0, Answer arg1) {
				if(arg0.index == arg1.index) return 0;
				if(arg0.index < arg1.index) return -1;
				return 1;
			}
		});
		if(answers.size() != Ans.size()) return false;
		for(int i =0; i < answers.size(); i++) {
			if(!answers.get(i).equals(Ans.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	*/

	/**
	 * @return the texts
	 */
	public ArrayList<String> getTexts() {
		return texts;
	}
	/**
	 * @param texts the texts to set
	 */
	public void setTexts(ArrayList<String> texts) {
		this.texts = texts;
	}
	/**
	 * @return the answers
	 */
	public ArrayList<String> getAnswers() {
		return answers;
	}
	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
			this.type = type;
	}
}