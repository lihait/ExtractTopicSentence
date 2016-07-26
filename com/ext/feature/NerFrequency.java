package com.ext.feature;

import java.util.ArrayList;

import com.ext.ner.ExtractNer;
import com.ext.seg.word.SegWord;
/**
 * 3 — 命名实体特征
 * @author srcb04161
 *
 */
public class NerFrequency {

	public static ArrayList<ArrayList<String>> getNerFrequency(String reviewContent){
		
		ArrayList<ArrayList<String>> featureResult = SentenceLengh.getSentenceLength(reviewContent);
		
		for(ArrayList<String> sentenceList : featureResult){
			
			int length = sentenceList.get(0).length();
			
			if(length == 0){
				
				length = 1;
				
			}
			
			ArrayList<String> nerResult = ExtractNer.getNerResultByJieba(sentenceList.get(0));
			
			String[] word = SegWord.segmentByJieba(sentenceList.get(0));
			
			int count = 0;
			
			for(int i = 0;i < word.length;i++){
				
				if(word[i].length() < nerResult.get(i).length()){
					
					count++;
					
				}
				
			}
			
			double NerScore = (double)count/length;
			
			sentenceList.add(String.valueOf(NerScore));
			
		}
		return featureResult;
		
	}
	
}
