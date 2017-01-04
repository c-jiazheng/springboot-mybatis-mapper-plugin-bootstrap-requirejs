package com.zyf.framework.utils.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressor {
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
		for (int i = 0; i < files.length; i++) {
			/** 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
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