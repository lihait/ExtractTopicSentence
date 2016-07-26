package com.ext.word.weight;

import java.util.ArrayList;

import com.ext.conf.StopWords;
import com.ext.predeal.CutTextIntoSentence;
import com.ext.seg.word.SegWord;

public class GetAllEntropy {

	public static String[] getAllEntropy(String reviewContent) {

		ArrayList<String> sentenceList = CutTextIntoSentence
				.cutTextIntoSentences(reviewContent,0);

		ArrayList<String> words = new ArrayList<String>();

		for (int i = 0; i < sentenceList.size(); i++) {

			String[] word = SegWord.segmentByJieba(sentenceList.get(i));

			for (int j = 0; j < word.length; j++) {

				words.add(word[j]);

			}

		}
		
		String[] wordStr = new String[words.size()];
		
		for(int i = 0;i < words.size();i++){
			
			wordStr[i] = words.get(i);
			//System.out.print(wordStr[i] + "\t");
			
		}

		//System.out.println();
		
		ArrayList<String> wordClean = StopWords.cutStopWords(wordStr);
		
		String[] wordResult = new String[wordClean.size()];
		
		for(int k = 0;k < wordClean.size();k++){
			
			wordResult[k] = wordClean.get(k);
			//System.out.print(wordResult[k] + "\t");
			
		}
		
		// 获取该篇文档每个词的权重
		String[] WordEntropy = GetWordEntropy.CalculateWordEntropy(wordResult);
		
		return WordEntropy;

	}

}
