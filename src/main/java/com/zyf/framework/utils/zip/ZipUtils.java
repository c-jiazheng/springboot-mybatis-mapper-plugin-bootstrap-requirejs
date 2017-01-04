package com.zyf.framework.utils.zip;

import com.zyf.framework.utils.PathUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Function : 文件压缩成zip
 *
 * @author : lqf
 * @Date : 2015-12-15
 */
public class ZipUtils {

	private ZipUtils(){}

	/**
	 * @param oldFile     待打包的文件夹或文件
	 * @param newFile 打包下载的文件名
	 * @throws IOException
	 */
	public static void zip(File oldFile, File newFile) throws IOException {
		OutputStream outputStream = new FileOutputStream(newFile);
		ZipCompressor zipCompressor = new ZipUtils().new ZipCompressor(outputStream);
		zipCompressor.compress(oldFile);
		outputStream.flush();
		try {
			outputStream.close();
		} catch (Throwable ignored) {
		}
	}

	/**
	 *
	 * @param file 待打包的文件夹或文件
	 * @param fileName 打包下载的文件名
	 * @throws IOException
	 */
	public static void down(File file, String fileName, HttpServletResponse response) throws IOException {
		OutputStream servletOutputStream = response.getOutputStream();
		//response.setContentType("application/vnd.ms-excel");
		//设置响应头，控制浏览器下载该文件
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		ZipCompressor zipCompressor = new ZipUtils().new ZipCompressor(servletOutputStream);
		zipCompressor.compress(file);
		servletOutputStream.write("0\r\n\r\n".getBytes());//Transfer-Encoding: chunked传输结束标记
		servletOutputStream.flush();
		servletOutputStream.close();
		try {
			servletOutputStream.close();
		} catch (Throwable ignored) {
		}
	}

	public static void main(String[] args) throws IOException {
		String path = PathUtils.getPath("static/ehcache/ehcache.xml");
		assert path != null;
		ZipUtils.zip(new File(path), new File("d:/ss.zip"));
	}


	class ZipCompressor {
		static final int BUFFER = 8192;
		private OutputStream outputStream;
		public ZipCompressor(OutputStream outputStream) {
			this.outputStream=outputStream;
		}
		public void compress(File file) {
			if (!file.exists())
				throw new RuntimeException(file.getAbsolutePath() + "不存在！");
			try {
				CheckedOutputStream cos = new CheckedOutputStream(outputStream,
						new CRC32());
				ZipOutputStream out = new ZipOutputStream(cos);
				String basedir = "";
				compress(file, out, basedir);
				out.close();//必须关闭,这样才会写入zip的结束信息,否则zip文件不完整.若想继续写入,可重写outputStream.close()方法
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		private void compress(File file, ZipOutputStream out, String basedir) {
			//判断是目录还是文件
			if (file.isDirectory()) {
				this.compressDirectory(file, out, basedir);
			} else {
				this.compressFile(file, out, basedir);
			}
		}
		// 压缩一个目录
		private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
			if (!dir.exists())
				return;
			File[] files = dir.listFiles();
			if(files != null)
				for (File file : files) {
					/** 递归 */
					compress(file, out, basedir + dir.getName() + "/");
				}
		}
		//压缩一个文件
		private void compressFile(File file, ZipOutputStream out, String basedir) {
			if (!file.exists()) {
				return;
			}
			try {
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file));
				ZipEntry entry = new ZipEntry(basedir + file.getName());
				out.putNextEntry(entry);
				int count;
				byte data[] = new byte[BUFFER];
				while ((count = bis.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				bis.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}