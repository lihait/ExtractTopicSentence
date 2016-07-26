package com.ext.feature;

import java.util.ArrayList;
import java.util.List;

import com.ext.conf.StopWords;
import com.ext.dict.WordDict;
import com.ext.predeal.CutTextIntoSentence;
import com.ext.seg.word.SegWord;
import com.ext.word.weight.GetAllEntropy;
import com.ext.word.weight.GetWordEntropy;
import com.ext.conf.Sentence;

/**
 * 1 - 句子的相对词频特征
 * @author srcb04161
 *
 */
public class WordFrequencyToSen {

	public static ArrayList<ArrayList<String>> getWordFrequencyToSen(String reviewContent){
		
		ArrayList<String> sentenceList = CutTextIntoSentence.cutTextIntoSentences(reviewContent,0);
		
		//获取该篇文档每个词的权重
		String[] WordEntropy = GetAllEntropy.getAllEntropy(reviewContent);

		ArrayList<String[]> frequencyList = new ArrayList<String[]>();
		
		ArrayList<ArrayList<String>> frequencyResult = new ArrayList<ArrayList<String>>();
		//保存句子内容
		List<Sentence> result = new ArrayList<Sentence>();
		Sentence item;
		//获取并保存每个句子
		for(int i = 0;i < sentenceList.size();i++){
			
			ArrayList<String> senList = new ArrayList<String>();
			
			senList.add(sentenceList.get(i));
			
			frequencyResult.add(senList);
			
			item = new Sentence();
			
			item.setContent(sentenceList.get(i));
			
			result.add(item);
			
		}
		
		double frequency = 0.1;
		
		double[] senFrequency = new double[sentenceList.size()];
		//针对每个句子计算权重
		for(int k = 0; k < sentenceList.size();k++){
			
			String[] word = SegWord.segmentByJieba(sentenceList.get(k));
			
			ArrayList<String> wordlist = StopWords.cutStopWords(word);
			
			String[] wordclean = new String[wordlist.size()];
			
			double frequencySen = 0.0;
			//获取句子的权重和
			for(int j = 0;j < wordlist.size();j++){
				
				for(int m = 0;m < WordEntropy.length;m++){
					
					String[] worden = WordEntropy[m].split(":");
					
					if(wordlist.get(j).equals(worden[0])){
						
						frequencySen = frequencySen + Double.valueOf(worden[1]);
						
					}
					
				}
				
			}
			
			senFrequency[k] = frequencySen;
			//比较得到所有句子中最大的权重
			if(frequency <= frequencySen){
				
				frequency = frequencySen;
				
			}
			
		}
		//计算每个句子的相对词频，并保存
		for(int i = 0;i < frequencyResult.size();i++){
			
			double senRelativeFre = (double)senFrequency[i]/frequency;
			
			frequencyResult.get(i).add(String.valueOf(senRelativeFre));
			
			
		}
		return frequencyResult;
		
	}
	
}
