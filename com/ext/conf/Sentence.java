package com.ext.conf;

public class Sentence {

	//句子内容
	private String content = null;
	//句子权重
	private double weight = 0.0;
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setWeight(double weight){
		this.weight = weight;
	}
	
	public double getWeight(){
		return weight;
	}
}
