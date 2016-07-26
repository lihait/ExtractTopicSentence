package com.ext.feature;

import java.util.ArrayList;
import java.util.HashSet;

import com.ext.conf.StopWords;
import com.ext.seg.word.SegWord;
import com.ext.topic.word.ExtractTopicWord;

public class TitleClassification {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static int titleClassification(String title, String filepath, ArrayList<String> lableList){
		
		int category = 0;
		
		String[] titleword = SegWord.segmentByJieba(title);
		
		ArrayList<String> titleList = StopWords.cutStopWords(titleword);
		
		try {
			HashSet<String> keywords = ExtractTopicWord.getKeyWords(filepath, lableList);
			
			int count = 0;
			//判断标题词与关键词集的重合度，若相同的词数大于等于1则该标题具有提示性
			for(int i = 0;i < titleList.size();i++){
				
				if(keywords.contains(titleList.get(i))){
					
					count++;
					
				}
				
			}
			
			if(count >= 1){
				
				category = 1;
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return category;

	}

}
