package course.web.controller;

import java.util.Map;

public class NearestUIData {
	String word;
	Map<String, Float> nearestWords;

	public NearestUIData() {

	}

	public NearestUIData(String word, Map<String, Float> nearestWords) {
		this.word = word;
		this.nearestWords = nearestWords;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Map<String, Float> getNearestWords() {
		return nearestWords;
	}

	public void setNearestWords(Map<String, Float> nearestWords) {
		this.nearestWords = nearestWords;
	}
}
