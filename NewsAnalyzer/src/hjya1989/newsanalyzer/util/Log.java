package hjya1989.newsanalyzer.util;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

	public static String MAINDIR = "";
	public static String LIBDIR = "\\lib"; 
	public static String NLPDIR = "\\nlp";
	public static String LOGDIR ="\\logs";
	public static String RESDIR = "\\result";
	public static String CONFDIR = "\\conf";
	public static String TWITTERFILESDIR = "\\twitter";
	public static String FACEBOOKFILESDIR = "\\facebook";
	public static String RSSFILESDIR = "\\rss";
	//public static String BINGRSSURL = "https://www.bing.com/news?format=RSS&nvaug=%5bNewsVertical+Category%3d%22rt_Politics%22%5d&FORM=NWRFSH";
	public static String RSSURLFILE = "\\rssurl";
	public static String RSSURL[] = {"http://www.chosun.com/site/data/rss/politics.xml","http://rss.donga.com/politics.xml","http://www.hani.co.kr/rss/politics/"};
	public static void log(Object obj, String ex)
	{
		System.out.println(obj + " : " + ex);
		commonWrite(obj, ex, "log");
	}
	
	public static void errorLog(Object obj, String ex)
	{
		System.out.println(obj + " : " + ex);
		commonWrite(obj, ex, "error");
	}
	
	public static void commonWrite(Object obj, String message, String type)
	{
		existDirCHK();	
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		message = "["+df.format(cal.getTime())+"] " + message;
		
		SimpleDateFormat dfname = new SimpleDateFormat("yyyy-MM-dd");
		FileWriter fos = null;
		try{
			if( type.equals("log")){
				fos = new FileWriter(MAINDIR+LOGDIR+ "\\" + String.valueOf(dfname.format(cal.getTime()))+"_log",true );
			}else if( type.equals("error")){
				fos = new FileWriter(MAINDIR+LOGDIR+ "\\" + String.valueOf(dfname.format(cal.getTime()))+"_error",true );
			}
					
			fos.write(message+"\r\n");
			fos.close();
		}catch(Exception ex){
			System.out.println("LOG : " + ex);
		}
		
		
	}
	
	public static void existDirCHK()
	{
		File mfile = new File(MAINDIR+LOGDIR);
		if( !mfile.exists() )
		{
			mfile.mkdir();
		}
	}	
	
}
