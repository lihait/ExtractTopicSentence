package com.ext.topic.word;

import java.io.UnsupportedEncodingException;

import com.ext.topic.utils.SystemParas;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NlpirTest {

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"D:\\NLPIR\\bin\\NLPIR", CLibrary.class);

		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

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

	public static String getKeyWords(String filepath) throws Exception {
		String argu = "D:\\NLPIR";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;

		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;
		String nativeByte = null;
		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is " + nativeBytes);
			return null;
		}

		String sInput = "据悉，质检总局已将最新有关情况再次通报美方，要求美方加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";

		// String nativeBytes = null;
		try {
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);

			System.out.println("分词结果为： " + nativeBytes);

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

			int nCountKey = 0;
			nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput,
					nativeBytes.length(), true);

			System.out.print("关键词提取结果是：" + nativeByte);

			nativeByte = CLibrary.Instance.NLPIR_GetFileKeyWords(filepath, 10,
					true);

			System.out.print("关键词提取结果是：" + nativeByte);

			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return nativeByte;

	}

	public static void main(String[] args) throws Exception {

		String filepath = "D:\\NLPIR\\NLPIR2015\\test\\屌丝，一个字头的诞生.TXT";

		getKeyWords(filepath);

	}
}
