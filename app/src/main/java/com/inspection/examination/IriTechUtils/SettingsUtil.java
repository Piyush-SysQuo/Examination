package com.inspection.examination.IriTechUtils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class SettingsUtil {

	// Environment.getExternalStorageDirectory()/iritech/config.ini
	private static final String CONFIG_FILENAME = "config.ini";

	public static String getConfigFolderName(Context ctx) {
		return "iritech";
	}

	public static String getConfigFolderPath(Context ctx) {
		return Environment.getExternalStorageDirectory() + "/" + SettingsUtil.getConfigFolderName(ctx);
	}

	public static Properties getSettings(Context ctx) {
		File myProperties = null;
		Properties fileProps = new Properties();
		try{
			myProperties = new File(Environment.getExternalStorageDirectory(),
					SettingsUtil.getConfigFolderName(ctx) + "/" + CONFIG_FILENAME);

			fileProps.load(new FileInputStream(myProperties));
			//fileProps.load(ctx.getResources().openRawResource(R.raw.settings));
		} catch(Exception e){
			fileProps = null;
		} finally{
		}
		return fileProps;
	}

	public static Properties readSettings(Context ctx) {
		File myProperties = null;
		Properties fileProps = new Properties();
		try{
			myProperties = new File(Environment.getExternalStorageDirectory(),
					SettingsUtil.getConfigFolderName(ctx) + "/" + CONFIG_FILENAME);

			fileProps.load(new FileInputStream(myProperties));
			//fileProps.load(ctx.getResources().openRawResource(R.raw.settings));
		}
		catch(Exception e){}
		finally{
		}
		return fileProps;
	}

	public static byte[] readFile(String file, Context ctx)
	{
		File myfile = null;
		FileInputStream fis = null;
		try{
			myfile = new File(Environment.getExternalStorageDirectory(),
					SettingsUtil.getConfigFolderName(ctx) + "/" + file);
			fis = new FileInputStream(myfile);
			int length = (int) myfile.length();
			byte[] data = new byte[length];
			fis.read(data);
			return data;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(fis != null)
			{
				try
				{
					fis.close();
				}catch(Exception ex ) {
					ex.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void writeSettings(Properties properties, Context ctx) {

		File myfile = null;
		FileOutputStream fos = null;
		try{
			myfile = new File(Environment.getExternalStorageDirectory(),
					SettingsUtil.getConfigFolderName(ctx) + "/" + CONFIG_FILENAME);
			fos = new FileOutputStream(myfile);
//	    	fos.write(data.getBytes());
			properties.save(fos, "new setting");
			fos.close();;
		}
		catch(Exception e){}
		finally{
		}
	}
}