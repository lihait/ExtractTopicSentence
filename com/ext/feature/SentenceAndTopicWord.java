package com.ext.feature;

import java.util.ArrayList;
import java.util.HashSet;

import com.ext.conf.StopWords;
import com.ext.seg.word.SegWord;
import com.ext.topic.word.ExtractTopicWord;
/**
 * 句子与关键词集重合度
 * @author srcb04161
 *
 */
public class SentenceAndTopicWord {

	public static ArrayList<ArrayList<String>> sentenceAndTopicWord(ArrayList<String> lableList, String reviewContent, String title) throws Exception{
		
		HashSet<String> keywordSet = ExtractTopicWord.getKeyWords(reviewContent, lableList);
		
		int size = keywordSet.size();
		
		if(size == 0){
			
			size = 1;
			
		}
		
		ArrayList<ArrayList<String>> featureResult = SenAndTitleSimilarityRatio.getSenAndTitleSimilarityRatio(reviewContent, title);
		
		for(ArrayList<String> sentenceList : featureResult){
			
			String[] word = SegWord.segmentByJieba(sentenceList.get(0));
			
			ArrayList<String> wordlist = StopWords.cutStopWords(word);
			
			String[] wordclean = new String[wordlist.size()];
			
			for(int i = 0;i < wordlist.size();i++){
				
				wordclean[i] = wordlist.get(i);
				
			}
			
			int count = 0;
			
			for(int j = 0;j < wordclean.length;j++){
				
				for(String keyword : keywordSet){
					
					if(wordclean[j].equals(keyword)){
						
						count++;
						
					}
					
				}
				
			}
			
			double sentenceWord = (double)count/size;
			
			sentenceList.add(String.valueOf(sentenceWord));
			
		}
		return featureResult;
		
	}
	
}
