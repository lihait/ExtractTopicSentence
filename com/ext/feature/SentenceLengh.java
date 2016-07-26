package com.ext.feature;

import java.util.ArrayList;
/**
 * 2 - 句子长度特征
 * @author srcb04161
 *
 */
public class SentenceLengh {

	public static ArrayList<ArrayList<String>> getSentenceLength(String reviewContent){
		
		ArrayList<ArrayList<String>> frequencyResult = WordFrequencyToSen.getWordFrequencyToSen(reviewContent);
		
		for(ArrayList<String> senList : frequencyResult){
			
			int senLength = senList.get(0).length();
			
			if(senLength > 16){
				
				senList.add("1.0");
				
			}else{
				
				senList.add("0.0");
			}
			
		}
		return frequencyResult;
		
	}
	
}
