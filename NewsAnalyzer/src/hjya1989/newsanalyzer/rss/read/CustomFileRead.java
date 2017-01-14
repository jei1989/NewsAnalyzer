package hjya1989.newsanalyzer.rss.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import hjya1989.newsanalyzer.util.Log;

public class CustomFileRead {

	File mfile;
	String[] list;
	
	public CustomFileRead()
	{
		
	}
	
	public String[] getListFile()
	{
		try{
			return this.list;
		}catch(Exception ex)
		{
			Log.errorLog(this, "getListFile :: " + ex); 
		}
		return null;
	}
	
	public void readFile()
	{
		try{
			mfile = new File(Log.MAINDIR + Log.CONFDIR + Log.CUSTOMFILE);
			if( mfile.exists() )
			{
				BufferedReader buff = new BufferedReader( new FileReader(mfile) );
				String temp = "";
				String imsi = "";
				while( (temp = buff.readLine()) != null )
				{
					
						imsi = imsi + temp + "|";
	
					
					
				}//while( (String temp = buff.readLine()) != null )
				
				list = imsi.split("\\|");
				
			}
		}catch(Exception ex)
		{
			Log.errorLog(this, "readFile :: " + ex); 
		}
	}
}
