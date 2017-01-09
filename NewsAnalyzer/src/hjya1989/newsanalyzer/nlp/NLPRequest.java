package hjya1989.newsanalyzer.nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hjya1989.newsanalyzer.util.Log;

public class NLPRequest implements Runnable{

	public String requestMessage;
	public String parentPath;
	public File[] files;
	
	public String parsingParam = "recent";//recent or max
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public NLPRequest()
	{
		this.parentPath = Log.MAINDIR + Log.NLPDIR;
	}
	
	public String requestMessage(String message)
	{
		this.requestMessage = message;
		files = new File(Log.MAINDIR + Log.NLPDIR).listFiles();
		return requestParser();
	}

	public void run()
	{
		requestParser();
	}
	
	private String requestParser()
	{
		
		String[] parse = this.requestMessage.split("\\ ");
		StringBuffer strbuff = new StringBuffer();
		
		for( String message : parse){
		
		String retmsg = "";
		
		BufferedReader buffreader01 = null;
		BufferedReader buffreader02 = null;
		BufferedReader buffreader03 = null;
		strbuff.append("\r\n["+message.trim()+"] ");
		
		for( File mfile : files){
			
			if( mfile.getName().trim().contains( message.trim() ) )
			{
				try {
					int cnt = 0;
					
					while(true){
					
						if( cnt >20 ){
							break;
						}else{
							cnt++;
						}
						
						try {
							buffreader01 = new BufferedReader(new FileReader(mfile));
							String temp01 = "";
							Date isRecent = null;
							int isMax = 0;
							String buffinsertString = "";
							while( (temp01 = buffreader01.readLine()) != null ){
							
								//if( mfile.getName().trim().equals( temp01.split("\\|")[5].trim()  ) ){
									
									if( parsingParam.equals("max") ){
										
										//temp01.split(|)[1]
										if( isMax == 0 ){
											isMax =   Integer.parseInt( temp01.split("\\|")[1] );
											buffinsertString = temp01.split("\\|")[0];
										}else{
											
											if( isMax < Integer.parseInt( temp01.split("\\|")[1] ) ){
												isMax =   Integer.parseInt( temp01.split("\\|")[1] ); 
												buffinsertString = temp01.split("\\|")[0];
											}
											
										}
										
									}else{//isRecent
										
										if( isRecent == null){
											isRecent =   df.parse( temp01.split("\\|")[4] );
											buffinsertString = temp01.split("\\|")[0];
										}else{
											
											if( isRecent.getTime() < df.parse(temp01.split("\\|")[4] ).getTime() ){
												isRecent =   df.parse( temp01.split("\\|")[4] );
												buffinsertString = temp01.split("\\|")[0];
											}
											
										}									
										
												
									}//if( parsingParam.equals("max") ){
								//}//if( mfile.getName().trim().equals( temp01.split("\\|")[5].trim()  ) ){
								
							}//while( (temp01 = buffreader01.readLine()) != null ){
							
							//strbuff.append(temp01.split("\\|")[0] + " ");
							strbuff.append(buffinsertString+ " ");
							mfile = new File(Log.MAINDIR + Log.NLPDIR + "\\" + buffinsertString.trim());
							//mfile = new File(Log.MAINDIR + Log.NLPDIR + "\\" + temp01.split("\\|")[0]);
							
							/*************
							buffreader02 = new BufferedReader(new FileReader(new File(Log.MAINDIR + Log.NLPDIR + "\\" + temp01.split("\\|")[0])));
							try{
								String temp02 = buffreader02.readLine();
								strbuff.append(temp02.split("\\|")[0] + " ");
								
								buffreader03  = new BufferedReader(new FileReader(new File(Log.MAINDIR + Log.NLPDIR + "\\" + temp02.split("\\|")[0])));
								try{
									
									String temp03 = buffreader03.readLine();
									strbuff.append(temp03.split("\\|")[0] + " ");
									
								}catch(Exception ex){
									Log.errorLog(this, "temp03 :: " + ex.toString());
								}
								
								
							}catch(Exception ex){
								Log.errorLog(this, "temp02 :: " + ex.toString());
							}finally{
								try {
									buffreader03.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									Log.errorLog(this, "buffreader03 close() :: " + e.toString());
								}
							}
							/*************/
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Log.errorLog(this, "temp01 :: " + e.toString());
						}finally{
							try {
								buffreader01.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Log.errorLog(this, "buffreader01 close() :: " + e.toString());
							}
						}
					}//while(true)
				} catch (Exception e) {
					Log.errorLog(this, "while() :: " + e.toString());
				}
			}
				
			
		}
		
		}//for( String message : parse){
		return strbuff.toString();
	}
	
	
	
}
