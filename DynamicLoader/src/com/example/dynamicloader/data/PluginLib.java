package com.example.dynamicloader.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.example.dynamicloader.PluginConstant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

public class PluginLib {

	private final String TAG = "PluginLib";
	public static final String CPU_ARMEABI = "armeabi";
	public static final String CPU_X86 = "x86";
	public static final String CPU_MIPS = "mips";

	private ExecutorService mSoExecutor = Executors.newCachedThreadPool();
	private String mNativeLibDir = "";

	private boolean mHasInstalled = false;

	public PluginLib(Context context, String path) {
		mNativeLibDir = context.getDir("pluginlib", Context.MODE_PRIVATE).getAbsolutePath();
		mHasInstalled = PluginConstant.isInstalled(context, path);
		if (!mHasInstalled) {
			copyPluginSoLib(context, path);
		}
	}

	private void postCopy(Context context, String path) {
		// TODO Auto-generated method stub

	}

	/**
	 * get cpu name, according cpu type parse relevant so lib
	 * 
	 * @return ARM、ARMV7、X86、MIPS
	 */
	private String getCpuName() {
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			br.close();
			String[] array = text.split(":\\s+", 2);
			if (array.length >= 2) {
				return array[1];
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressLint("DefaultLocale")
	private String getCpuArch() {
		String cpuName = getCpuName();
		String cpuArchitect = CPU_ARMEABI;
		if (cpuName.toLowerCase().contains("arm")) {
			cpuArchitect = CPU_ARMEABI;
		} else if (cpuName.toLowerCase().contains("x86")) {
			cpuArchitect = CPU_X86;
		} else if (cpuName.toLowerCase().contains("mips")) {
			cpuArchitect = CPU_MIPS;
		}

		return cpuArchitect;
	}

	/**
	 * copy so lib to specify directory(/data/data/host_pack_name/pluginlib)
	 * 
	 * @param dexPath
	 *            plugin path
	 * @param nativeLibDir
	 *            nativeLibDir
	 */
	public void copyPluginSoLib(Context context, String dexPath) {
		String cpuArchitect = getCpuArch();
		Log.d(TAG, "cpuArchitect: " + cpuArchitect);
		long start = System.currentTimeMillis();
		try {
			ZipFile zipFile = new ZipFile(dexPath);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) entries.nextElement();
				if (zipEntry.isDirectory()) {
					continue;
				}
				String zipEntryName = zipEntry.getName();
				if (zipEntryName.endsWith(".so") && zipEntryName.contains(cpuArchitect)) {
					mSoExecutor.execute(new CopySoTask(context, zipFile, zipEntry));
				}
			}
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			return;
		}
		PluginConstant.setInstalled(context, dexPath);
		long end = System.currentTimeMillis();
		Log.d(TAG, "### copy so time : " + (end - start) + " ms");
	}

	private class CopySoTask implements Runnable {

		private String mSoFileName;
		private ZipFile mZipFile;
		private ZipEntry mZipEntry;

		CopySoTask(Context context, ZipFile zipFile, ZipEntry zipEntry) {
			mZipFile = zipFile;
			mZipEntry = zipEntry;
			mSoFileName = parseSoFileName(zipEntry.getName());
		}

		private final String parseSoFileName(String zipEntryName) {
			return zipEntryName.substring(zipEntryName.lastIndexOf("/") + 1);
		}

		private void writeSoFile2LibDir() throws IOException {
			InputStream is = null;
			FileOutputStream fos = null;
			is = mZipFile.getInputStream(mZipEntry);
			fos = new FileOutputStream(new File(mNativeLibDir, mSoFileName));
			copy(is, fos);
			mZipFile.close();
		}

		/**
		 * 输入输出流拷贝
		 * 
		 * @param is
		 * @param os
		 */
		public void copy(InputStream is, OutputStream os) throws IOException {
			if (is == null || os == null)
				return;
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int size = getAvailableSize(bis);
			byte[] buf = new byte[size];
			int i = 0;
			while ((i = bis.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, i);
			}
			bos.flush();
			bos.close();
			bis.close();
		}

		private int getAvailableSize(InputStream is) throws IOException {
			if (is == null)
				return 0;
			int available = is.available();
			return available <= 0 ? 1024 : available;
		}

		@Override
		public void run() {
			try {
				writeSoFile2LibDir();
				Log.d(TAG, "copy so lib success: " + mZipEntry.getName());
			} catch (IOException e) {
				Log.e(TAG, "copy so lib failed: " + e.toString());
				e.printStackTrace();
			}

		}

	}

}
