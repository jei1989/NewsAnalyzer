package hjya1989.newsanalyzer.nlp;

//import kr.co.shineware.nlp.komoran.*;
//import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
//import kr.co.shineware.util.common.model.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import hjya1989.newsanalyzer.*;
import hjya1989.newsanalyzer.util.Log;

public class NLPWriter implements Runnable{

	private String message;
	private String retmessage;
	
	public void setNLPMessage(String msg){
		this.message = msg;
	}
	
	public String getNLPRequestMsg()
	{
		return this.retmessage;
	}
	
	public void run()
	{
		NLPParsing();
	}
	
	public void NLPParsing()
	{
		/*******************
		Komoran komoran = new Komoran(Log.MAINDIR+Log.LIBDIR);
		
		List<List<Pair<String,String>>> result = komoran.analyze(this.message);
		StringBuffer strbuff = new StringBuffer();
		for( List<Pair<String, String>> eojeolResult : result ){
			for( Pair<String, String> wordMorph : eojeolResult ){
				//System.out.println(wordMorph);
				if( checkSecond(wordMorph.getSecond())){
					//String temp = wordMorph.toString().split(",")[0].split("=")[1];
					//System.out.println(wordMorph.getFirst() + " :: " + wordMorph);
					strbuff.append(wordMorph.getFirst());
					strbuff.append(" ");
					
				}
				
			}
		}
		/*******************/
		/*******************/
		//String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
		try{
			
			this.message = message.replace('/', ' ');
			this.message = message.replace('*', ' ');
			this.message = message.replace('!', ' ');
			this.message = message.replace('@', ' ');					
			this.message = message.replace('#', ' ');
			this.message = message.replace('$', ' ');
			this.message = message.replace('%', ' ');
			this.message = message.replace('^', ' ');
			this.message = message.replace('&', ' ');					
			this.message = message.replace('*', ' ');
			this.message = message.replace('(', ' ');
			this.message = message.replace(')', ' ');
			this.message = message.replace('\\', ' ');
			this.message = message.replace('"', ' ');					
			this.message = message.replace('{', ' ');
			this.message = message.replace('}', ' ');
			this.message = message.replace('_', ' ');
			this.message = message.replace('[', ' ');
			this.message = message.replace(']', ' ');					
			this.message = message.replace('|', ' ');
			this.message = message.replace('\\', ' ');
			this.message = message.replace('?', ' ');					
			this.message = message.replace('/', ' ');
			this.message = message.replace('<', ' ');
			this.message = message.replace('>', ' ');
			this.message = message.replace(',', ' ');
			this.message = message.replace('.', ' ');					
			this.message = message.replace('"', ' ');
			this.message = message.replace('¡°', ' ');
			this.message = message.replace('¡®', ' ');
			this.message = message.replace('¡±', ' ');
			this.message = message.replace('¡¯', ' ');
			this.message = message.replace('¡¦', ' ');
			this.message = message.replace("\r\n", " ");
			this.message = message.replace("  ", " ");
			this.message = message.replace("   ", " ");
			
		}catch(Exception ex){
			Log.errorLog(this, "substring char :: " + ex.toString());
		}

		/*******************/
		
		
		this.retmessage =  this.message;//strbuff.toString();
		messageWriter(retmessage);
		
	}
/*****************	
	public boolean checkSecond(String chk)
	{
		//boolean isChk = false;
		String[] comp = {"NNB", "NNG", "SN"};
		for( String str : comp){
			if( chk.contains(str)){
				return true;
			}
		}
		return false;
	}
/*****************/	
	public void messageWriter(String msg){
		
		File file = new File( Log.MAINDIR + Log.NLPDIR );
		if( !file.exists() )
		{
			file.mkdir();
		}
		
		String[] mmsge = msg.split("\\ ");
		BufferedWriter wbuff = null;
		BufferedReader rbuff = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for( int cnt = 0 ; cnt < mmsge.length ; cnt++ )
		{
			boolean readFileExist = true;
			boolean isDuplicated = false;
			int linecount = 1;
			StringBuffer strbuff = new StringBuffer();
			
			if( mmsge[cnt].toString().equals(""))
			{
				continue;
			}
			
			File preFile = new File(Log.MAINDIR + Log.NLPDIR + "\\"+ mmsge[cnt]);
			File tempFile = new File(Log.MAINDIR + Log.NLPDIR + "\\"+ mmsge[cnt] + ".tmp");
			
			try {
				
				rbuff = new BufferedReader(new FileReader( preFile ));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "messageWriter :: rbuff :: " + e.toString());
				readFileExist = false;
			}
			try {
				
				wbuff = new BufferedWriter(new FileWriter( tempFile,true));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "messageWriter :: wbuff :: " + e.toString());
			}
			try{
			if( readFileExist ){
			
				try{
					String temp = "";
					
					try{
						while( (temp = rbuff.readLine().trim() ) != null )
						{
							if( cnt < mmsge.length )
							{
								String[] tempstr = temp.split("\\|");
								//System.out.println(tempstr[0] + "|" + (Integer.parseInt(tempstr[1])) + "|" + mmsge[cnt] + "|" + mmsge[cnt+1] );
								if(  tempstr[0].trim().equals(mmsge[cnt+1].trim()) )
								{
									//if( tempstr[0].equals("´ë¼±")){
									//	System.out.println(tempstr[0] + "|" + (Integer.parseInt(tempstr[1])) + "|" + mmsge[cnt] + "|" + mmsge[cnt+1] );
									//}
									
									//wbuff.write(temp.substring(0, temp.length()-1));
									//wbuff.flush();
									//if( !tempstr[0].trim().equals("") ){
										////////strbuff.append(tempstr[0].trim() + "|" + (Integer.parseInt(tempstr[1].trim())+1) );
										////////strbuff.append("\r\n");
									
									//}
									if( !mmsge[cnt+1].trim().equals("") ){
										cal = Calendar.getInstance();
										
										String tstr_0 = "";
										
										try{
											tstr_0 = mmsge[cnt-1].trim();
										}catch(Exception ex){
											
										}
										
										wbuff.write(tempstr[0].trim() + "|" + (Integer.parseInt(tempstr[1].trim())+1) + "|" + linecount++ + "|" + tempstr[3] + "|" + sdf.format(cal.getTime()) + "|" + tstr_0);
										wbuff.write("\r\n");
										wbuff.flush();
										//System.out.println("file = " + mmsge[cnt] + " :: " + "true  :: " + tempstr[0] + " :: " + mmsge[cnt+1].trim() + " :: | " + (Integer.parseInt(tempstr[1])+1) );
										//System.out.println("00000 :: " + tempstr[0].trim() + "|" + (Integer.parseInt(tempstr[1].trim())+1));
									}		
										isDuplicated = true;
									
									//break;
								}else{
	
									//////////////strbuff.append(temp.trim());
									//////////////strbuff.append("\r\n");
									
									//System.out.println("file = " + mmsge[cnt] + " :: " + "false :: " + mmsge[cnt+1] + " :: " + mmsge[cnt+1].trim() + " :: " + "|1" );
									wbuff.write(tempstr[0].trim() + "|" + tempstr[1].trim() + "|" + linecount++ + "|" + tempstr[3] + "|" + tempstr[4] + "|" + tempstr[5] );
									wbuff.write("\r\n");
									wbuff.flush();
									
									//System.out.println("11111 :: " + temp.trim() );
								}
							}
						}//while( (temp = rbuff.readLine() ) != null )
					}catch(Exception ex)
					{
						Log.errorLog(this, "while :: " + ex.toString());
					}
					if( !isDuplicated )
					{
						
						//if( !mmsge[cnt+1].trim().equals("") ){
							//////////strbuff.append(mmsge[cnt+1].trim() +"|1");
							//////////strbuff.append("\r\n");
						//}
						if( !mmsge[cnt+1].trim().equals("") ){
							
							cal = Calendar.getInstance();
							
							String tstr_0 = "";
							
							try{
								tstr_0 = mmsge[cnt-1].trim();
							}catch(Exception ex){
								
							}
							
							
							wbuff.write(mmsge[cnt+1].trim() +"|1" + "|" + linecount++ + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(cal.getTime()) + "|" + tstr_0);
							wbuff.write("\r\n");
							wbuff.flush();
							//System.out.println("22222 :: " + mmsge[cnt+1] +"|1");
							
						}
					}
					//Log.log(this, "############################");
					//Log.log(this,strbuff.toString());
					//Log.log(this, "############################");
					
					///////////wbuff.write(strbuff.toString().trim());
					///////////wbuff.flush();
					//tempFile.renameTo(preFile);
					
				}catch(Exception ex){
					//ex.printStackTrace();
					Log.errorLog(this, "messageWriter :: BufferedReader :: " + mmsge[cnt] + " :: " + mmsge[cnt+1] + " :: " + ex.toString());

				}
			}//if( readFileExist )
			else{
				
				/************/
				try{
					if( cnt < mmsge.length ){
						//if( !isDuplicated )
						{
							//System.out.println("33333 :: " + mmsge[cnt+1] +"|1" );
							if(  !mmsge[cnt+1].trim().equals("") ){
								
								cal = Calendar.getInstance();
								
								String tstr_0 = "";
								
								try{
									tstr_0 = mmsge[cnt-1].trim();
								}catch(Exception ex){
									
								}

								wbuff.write(mmsge[cnt+1].trim() +"|1" + "|" + linecount++ + "|" + sdf.format(cal.getTime()) + "|" + sdf.format(cal.getTime())  + "|" + tstr_0);
								wbuff.write("\r\n");
								wbuff.flush();
								
							}
						}
					}
				} catch (IOException e) {
					Log.errorLog(this, "messageWriter :: isDuplicated wbuff.write() :: " + e.toString());
				}
				/************/				
			}////if( readFileExist ) else
			}catch(Exception ex){
				Log.errorLog(this, "messageWriter :: if( readFileExist ) else :: " + ex.toString());
			}finally{
				try {
					wbuff.close();
					if( readFileExist ){
						rbuff.close();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.errorLog(this, "messageWriter :: IOException finally() :: " + e.toString());
				}
				try {
					if( preFile.exists() ){
						preFile.delete();
					}
					tempFile.renameTo(preFile);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.errorLog(this, "messageWriter :: IOException finally 2() :: " + e.toString());
				}
			}//finally{

		}//for( int cnt = 0 ; cnt < mmsge.length ; cnt++ )
		Log.log(this, "############################");
	}
	
}
