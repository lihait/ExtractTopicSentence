package com.ext.conf;

import java.util.ArrayList;

import com.ext.dict.WordDict;

public class StopWords {

	public static ArrayList<String> cutStopWords(String[] word){
		
		ArrayList<String> wordlist = new ArrayList<String>();
		
		if(word != null){
			
			for(int i = 0;i < word.length;i++){
				
				if(WordDict.stopDict.contains(word[i])){
					
					continue;
					
				}
				
				wordlist.add(word[i]);
			}
			return wordlist;
			
		}
		
		return null;
	}
	
}
