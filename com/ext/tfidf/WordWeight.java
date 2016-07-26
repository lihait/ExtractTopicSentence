package com.ext.tfidf;

import java.io.*;
import java.util.*;

import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ext.seg.word.SegWord;

public class WordWeight {

	/**
	 * @param args
	 */
	private static ArrayList<String> FileList = new ArrayList<String>(); // 获取所有文件

	// 获取当前目录及子目录下的所有文件
	public static List<String> readDirs(String filepath)
			throws FileNotFoundException, IOException {
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("输入的[]");
				System.out.println("filepath:" + file.getAbsolutePath());
			} else {
				String[] flist = file.list();
				for (int i = 0; i < flist.length; i++) {
					File newfile = new File(filepath + "\\" + flist[i]);
					if (!newfile.isDirectory()) {
						FileList.add(newfile.getAbsolutePath());
					} else if (newfile.isDirectory()) // if file is a directory,
														// call ReadDirs
					{
						readDirs(filepath + "\\" + flist[i]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return FileList;
	}

	// 读取文件内容
	public static String readFile(String file) throws FileNotFoundException,
			IOException {
		StringBuffer strSb = new StringBuffer(); // String is constant，
													// StringBuffer can be
													// changed.
		InputStreamReader inStrR = new InputStreamReader(new FileInputStream(
				file), "utf8"); // byte streams to character streams
		BufferedReader br = new BufferedReader(inStrR);
		String line = br.readLine();
		while (line != null) {
			strSb.append(line).append("\r\n");
			line = br.readLine();
		}

		return strSb.toString();
	}

	// 分词
	public static ArrayList<String> cutWords(String file) throws IOException {

		ArrayList<String> words = new ArrayList<String>();
		String text = WordWeight.readFile(file);
		IKAnalyzer analyzer = new IKAnalyzer(true);
		words = analyzer.split(text);
		for (int i = 0; i < words.size(); i++) {
			System.out.println(i + ":" + words.get(i));
		}		
		return words;
	}

	// 统计每篇文档中单词的个数
	public static HashMap<String, Integer> normalTF(ArrayList<String> cutwords) {
		HashMap<String, Integer> resTF = new HashMap<String, Integer>();

		for (String word : cutwords) {
			if (resTF.get(word) == null) {
				resTF.put(word, 1);
				System.out.println(word);
			} else {
				resTF.put(word, resTF.get(word) + 1);
				System.out.println(word.toString());
			}
		}
		return resTF;
	}

	// 计算每篇文档的词频
	public static HashMap<String, Float> tf(ArrayList<String> cutwords) {
		HashMap<String, Float> resTF = new HashMap<String, Float>();

		int wordLen = cutwords.size();
		HashMap<String, Integer> intTF = WordWeight.normalTF(cutwords);

		Iterator iter = intTF.entrySet().iterator(); // iterator for that get
														// from TF
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			resTF.put(entry.getKey().toString(),
					Float.parseFloat(entry.getValue().toString()) / wordLen);
			System.out.println(entry.getKey().toString() + " = "
					+ Float.parseFloat(entry.getValue().toString()) / wordLen);
		}
		return resTF;
	}

	// 统计当前目录下所有文件中词出现的次数
	public static HashMap<String, HashMap<String, Integer>> normalTFAllFiles(
			String dirc) throws IOException {
		HashMap<String, HashMap<String, Integer>> allNormalTF = new HashMap<String, HashMap<String, Integer>>();
		List<String> filelist = WordWeight.readDirs(dirc);
		
		for (String file : filelist) {
			HashMap<String, Integer> dict = new HashMap<String, Integer>();
			ArrayList<String> cutwords = WordWeight.cutWords(file); // get cut
																	// word for
																	// one file

			dict = WordWeight.normalTF(cutwords);
			allNormalTF.put(file, dict);
		}
		return allNormalTF;
	}

	// 计算当前目录下所有文件中的词频
	public static HashMap<String, HashMap<String, Float>> tfAllFiles(String dirc)
			throws IOException {
		HashMap<String, HashMap<String, Float>> allTF = new HashMap<String, HashMap<String, Float>>();
		List<String> filelist = WordWeight.readDirs(dirc);

		for (String file : filelist) {
			HashMap<String, Float> dict = new HashMap<String, Float>();
			ArrayList<String> cutwords = WordWeight.cutWords(file); // get cut
																	// words for
																	// one file

			dict = WordWeight.tf(cutwords);
			allTF.put(file, dict);
		}
		return allTF;
	}

	// 计算IDF
	public static HashMap<String, Float> idf(
			HashMap<String, HashMap<String, Float>> all_tf) {
		HashMap<String, Float> resIdf = new HashMap<String, Float>();
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		int docNum = FileList.size();

		for (int i = 0; i < docNum; i++) {
			HashMap<String, Float> temp = all_tf.get(FileList.get(i));
			Iterator iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String word = entry.getKey().toString();
				if (dict.get(word) == null) {
					dict.put(word, 1);
				} else {
					dict.put(word, dict.get(word) + 1);
				}
			}
		}
		System.out.println("IDF for every word is:");
		Iterator iter_dict = dict.entrySet().iterator();
		while (iter_dict.hasNext()) {
			Map.Entry entry = (Map.Entry) iter_dict.next();
			float value = (float) Math.log(docNum
					/ Float.parseFloat(entry.getValue().toString()));
			resIdf.put(entry.getKey().toString(), value);
			System.out.println(entry.getKey().toString() + " = " + value);
		}
		return resIdf;
	}

	// 计算TF-IDF
	public static void tf_idf(HashMap<String, HashMap<String, Float>> all_tf,
			HashMap<String, Float> idfs) {
		HashMap<String, HashMap<String, Float>> resTfIdf = new HashMap<String, HashMap<String, Float>>();

		int docNum = FileList.size();
		for (int i = 0; i < docNum; i++) {
			String filepath = FileList.get(i);
			HashMap<String, Float> tfidf = new HashMap<String, Float>();
			HashMap<String, Float> temp = all_tf.get(filepath);
			Iterator iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String word = entry.getKey().toString();
				Float value = (float) Float.parseFloat(entry.getValue()
						.toString()) * idfs.get(word);
				tfidf.put(word, value);
			}
			resTfIdf.put(filepath, tfidf);
		}
		System.out.println("TF-IDF for Every file is :");
		DisTfIdf(resTfIdf);
	}

	public static void DisTfIdf(HashMap<String, HashMap<String, Float>> tfidf) {
		Iterator iter1 = tfidf.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry entrys = (Map.Entry) iter1.next();
			System.out.println("FileName: " + entrys.getKey().toString());
			System.out.print("{");
			HashMap<String, Float> temp = (HashMap<String, Float>) entrys
					.getValue();
			Iterator iter2 = temp.entrySet().iterator();
			while (iter2.hasNext()) {
				Map.Entry entry = (Map.Entry) iter2.next();
				System.out.print(entry.getKey().toString() + " = "
						+ entry.getValue().toString() + ", ");
			}
			System.out.println("}");
		}

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String file = "D:\\NLPIR\\NLPIR2015\\test\\test";

		HashMap<String, HashMap<String, Float>> all_tf = tfAllFiles(file);
		System.out.println();
		HashMap<String, Float> idfs = idf(all_tf);
		System.out.println();
		tf_idf(all_tf, idfs);

	}

}
