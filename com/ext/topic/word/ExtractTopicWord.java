package com.ext.topic.word;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import com.ext.topic.utils.SystemParas;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class ExtractTopicWord {

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"D:\\NLPIR\\bin\\NLPIR", CLibrary.class);

		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);

		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);

		public int NLPIR_AddUserWord(String sWord);// add by qp 2008.11.10

		public int NLPIR_DelUsrWord(String sWord);// add by qp 2008.11.10

		public String NLPIR_GetLastErrorMsg();

		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HashSet<String> getKeyWords(String fileContent, ArrayList<String> lableList) throws Exception {
		String argu = "D:\\NLPIR\\NLPIR2016\\20140928";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
        String[] keywords = null;
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;
		String nativeByte = null;
		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is " + nativeBytes);
			return null;
		}

		try {

			nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(fileContent, 20,
					false);			

			System.out.print("关键词提取结果是：" + nativeByte);

			keywords = nativeByte.split("#");

			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
		HashSet<String> lableSet = new HashSet<String>();
		
		if(lableList != null){
			
			//添加标签作为关键词集的一部分
			for(int i = 0;i < lableList.size();i++){
				
				lableSet.add(lableList.get(i));
				
			}
			
		}

		for(int j = 0;j < keywords.length;j++){
			
			if(keywords[j].equals("Line 0")){
				
				continue;
				
			}
			
			lableSet.add(keywords[j]);
			
		}
		
		return lableSet;

	}

	public static void main(String[] args) throws Exception {

		String filepath = "两人打甲流疫苗后死亡 另有15例较严重异常反应";
		
		ArrayList<String> lableList = new ArrayList<String>();
		
		lableList.add("疫苗");
		
		lableList.add("北京");

		HashSet<String> result = getKeyWords(filepath, lableList);
		
		for(String str : result){
			System.out.println(str);
		}

	}
}

