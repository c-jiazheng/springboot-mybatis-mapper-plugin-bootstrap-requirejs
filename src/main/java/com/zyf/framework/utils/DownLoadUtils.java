package com.zyf.framework.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DownLoadUtils {

	public static void download(HttpServletResponse response, String fileAliaName, String path) {
		String fileName = null;
		try {
			fileName = new String(fileAliaName.getBytes("gbk"), "iso-8859-1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
		try {
			InputStream inputStream = new FileInputStream(new File(path));
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			// 这里主要关闭。
			os.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
