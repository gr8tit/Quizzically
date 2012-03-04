package com.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Question {
	
	public String questionResponse = "question-response";
	public String fillInTheBlank = "fill-in-the-blank";
	public String multiAnswer = "multi-answer";
	public String pictureResponse = "picture-response";
	public String multiChoice = "multiple-choice";
	public String multiChoiceMultiAnswer = "multiple-choice-multiple-answer";
	public String matching = "matching";
	public String autoGenerated = "auto-generated";
	public String graded = "graded-question";
	
	public List<String>text;
	public List<Answer>Ans;
	public String url;
	public Date time;
	public String type;
	
	public Question(List<String>text, List<Answer>Ans, String url,
							String type) 
	{
		this.text = text;
		this.Ans = Ans;
		Collections.sort(Ans, new Comparator<Answer>() {
			@Override
			public int compare(Answer arg0, Answer arg1) {
				if(arg0.index == arg1.index) return 0;
				if(arg0.index < arg1.index) return -1;
				return 1;
			}
		});
		this.url = url;
		this.type = type;
	}
	
	/*
	 * depending on the question type, Answer's
	 * index can mean either:
	 * - the index of hte question with which to associate
	 * - 
	 */
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
	public boolean checkAnswer(List<Answer>answers) {
		/* sort the answer by a certain heuristic */
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
	
	
}
