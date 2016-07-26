package com.ext.feature;

import java.util.ArrayList;
/**
 * 计算句子与标题的相似度特征
 * @author srcb04161
 *
 */
public class SenAndTitleSimilarityRatio {

	public static ArrayList<ArrayList<String>> getSenAndTitleSimilarityRatio(String reviewContent, String title){
		
		ArrayList<ArrayList<String>> featureResult = SentenceAndTitle.getSimilarityWithSenAndTitle(reviewContent, title);
		
		for(ArrayList<String> sentenceList : featureResult){
			
			double similarityRatio = GetSimilarityRatio.getSimilarityRatio(sentenceList.get(0), title);
			
			sentenceList.add(String.valueOf(similarityRatio));
			
		}
		return featureResult;
		
	}
	
}
