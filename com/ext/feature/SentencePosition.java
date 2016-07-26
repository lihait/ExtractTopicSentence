package com.ext.feature;

import java.util.ArrayList;

import com.ext.predeal.CutTextIntoSentence;
/**
 * 句子位置特征
 */
public class SentencePosition {

	public static ArrayList<ArrayList<String>> sentencePosition(ArrayList<String> lableList, String reviewContent, String title) throws Exception{
		
		ArrayList<ArrayList<String>> featureResult = SentenceAndTopicWord.sentenceAndTopicWord(lableList, reviewContent, title);
		
		ArrayList<String> sentenceIdx = CutTextIntoSentence.cutTextIntoSentences(reviewContent,2);
		
		for(ArrayList<String> sentenceList : featureResult){
			
			for(String sentenceidx : sentenceIdx){
				
				String[] sentence = sentenceidx.split(":");
				
				String senContent = sentence[0];
				//获取段落号
				String paragraphIdx = sentence[1];
				//获取句子号
				String idx = sentence[2];
				//获取句子在该段落中的位置号
				String idx2 = sentence[3];
				
				double score = 0.0;
				
				if(sentenceList.get(0).equals(senContent)){
					//对于一篇文章中的第i条句子，如果i<3或者该句是段首句
					if(Integer.parseInt(idx) < 3 || Integer.parseInt(idx2)==1){
						
						score = 1.0;
						
					}else{
						
						int sentenceNum = sentenceIdx.size();
						
						score = 1 - (double)Math.log(Integer.parseInt(idx))/Math.log(sentenceNum);
						
					}
					
				}
				
				sentenceList.add(String.valueOf(score));
				
			}
			
		}
		return featureResult;
		
	}
	
}
