package course.web.controller;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

public class CoNLLSentenceUIData {
	public CoNLLSentence coNLLSentence;

	public CoNLLSentenceUIData(CoNLLSentence coNLLSentence) {
		this.coNLLSentence = coNLLSentence;
	}

	public CoNLLSentence getCoNLLSentence() {
		return coNLLSentence;
	}

	public void setCoNLLSentence(CoNLLSentence coNLLSentence) {
		this.coNLLSentence = coNLLSentence;
	}

	public String getCoNLLSentenceList() {
		return coNLLSentence.toString();
	}

	public String getCoNLLSentenceUIList() {
		final StringBuilder sb = new StringBuilder(coNLLSentence.word.length * 50);
		for (CoNLLWord word : this.coNLLSentence.word) {
			sb.append(word);
			sb.append("<br>");
		}
		return sb.toString();
	}
}
