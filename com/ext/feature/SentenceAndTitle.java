package com.ext.feature;

import java.util.ArrayList;

import com.ext.conf.StopWords;
import com.ext.seg.word.SegWord;
import com.ext.word.weight.GetAllEntropy;
import com.ext.word.weight.GetWordEntropy;
/**
 * 4 - 句子与标题的重合度特征
 * @author srcb04161
 *
 */
public class SentenceAndTitle {

	public static ArrayList<ArrayList<String>> getSimilarityWithSenAndTitle(String reviewContent, String title){
		
		String[] word = SegWord.segmentByJieba(title);
		
		ArrayList<String> wordClean = StopWords.cutStopWords(word);
		
		double titleEntropy = GetWordEntropy.getAllEntropy(word);
		
		if(titleEntropy == 0.0){
			
			titleEntropy = 1.0;
			
		}
		
		ArrayList<ArrayList<String>> featureResult = NerFrequency.getNerFrequency(reviewContent);
		
		for(ArrayList<String> sentenceList : featureResult){
			
			String[] wordStr = SegWord.segmentByJieba(sentenceList.get(0));
			
			ArrayList<String> wordStrClean = StopWords.cutStopWords(wordStr);
			
			ArrayList<String> strResult = new ArrayList<String>();
			//找出句子与标题相同的词
			for(String str1 : wordStrClean){
				
				for(String str2 : wordClean){
					
					if(str1.equals(str2)){
						
						strResult.add(str1);
						
					}
					
				}
				
			}
			
			String[] allWordEntropy = GetAllEntropy.getAllEntropy(reviewContent);
			
			double frequencySen = 0.0;
			//获取相同词的权重和
			for(int j = 0;j < strResult.size();j++){
				
				for(int m = 0;m < allWordEntropy.length;m++){
					
					String[] worden = allWordEntropy[m].split(":");
					
					if(strResult.get(j).equals(worden[0])){
						
						frequencySen = frequencySen + Double.valueOf(worden[1]);
						
					}
					
				}
				
			}
			
			double similaryWithSenAndTitle = (double)frequencySen/titleEntropy;
			
			sentenceList.add(String.valueOf(similaryWithSenAndTitle));
			
		}
		return featureResult;
		
	}
	
}
