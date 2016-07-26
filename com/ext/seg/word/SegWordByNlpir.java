package com.ext.seg.word;

import java.io.UnsupportedEncodingException;

import com.ext.topic.utils.SystemParas;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class SegWordByNlpir {

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"D:\\NLPIR\\bin\\NLPIR", CLibrary.class);

		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);		

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

	public static String segWordByNlpir(String sentence) throws Exception {
		String argu = "D:\\NLPIR";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;

		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String word = null;
		
		if (0 == init_flag) {
			word = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is " + word);
			return null;
		}

		String sInput = "屌丝，一个字头的诞生，屌丝";

		// String nativeBytes = null;
		try {
			word = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 2);
			
			
			System.out.println("分词结果为： " + word);

			// CLibrary.Instance.NLPIR_AddUserWord("要求美方加强对输 n");
			// CLibrary.Instance.NLPIR_AddUserWord("华玉米的产地来源 n");
			// nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput,
			// 1);
			// System.out.println("增加用户词典后分词结果为： " + nativeBytes);
			//
			// CLibrary.Instance.NLPIR_DelUsrWord("要求美方加强对输");
			// nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput,
			// 1);
			// System.out.println("删除用户词典后分词结果为： " + nativeBytes);

			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return word;

	}

	public static void main(String[] args) throws Exception {

		String sentence = "屌丝，一个字头的诞生.TXT";

		segWordByNlpir(sentence);

	}
}

