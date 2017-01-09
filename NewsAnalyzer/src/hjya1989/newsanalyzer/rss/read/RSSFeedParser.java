package hjya1989.newsanalyzer.rss.read;
//package de.vogella.rss.model;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

import hjya1989.newsanalyzer.*;
import hjya1989.newsanalyzer.rss.model.Feed;
import hjya1989.newsanalyzer.rss.model.FeedMessage;
import hjya1989.newsanalyzer.util.Log;

public class RSSFeedParser implements Runnable{
	
	static final String TITLE = "title";
	static final String DESCRIPTION = "description";
	static final String CHANNEL = "channel";
	static final String LANGUAGE = "language";
	static final String COPYRIGHT = "copyright";
	static final String LINK = "link";
	static final String AUTHOR = "author";
	static final String ITEM = "item";
	static final String PUB_DATE = "pubDate";
	static final String GUID = "guid";
	static final String DCDATE = "date";//"['http://purl.org/dc/elements/1.1/']:dc:date";
	
	private URL url;
	private String feedURL[]; 
	
	public boolean isTrue = true;
	
	//private StringBuffer strbuff;

	public RSSFeedParser()
	{
		Log.MAINDIR = System.getProperty("user.dir");
		//setRSSFeedURL();
		//new Thread(this).start();
	}
	
	public static void main(String args[])
	{
		RSSFeedParser rssFeedParser = new RSSFeedParser();
		rssFeedParser.setRSSFeedURL();
		new Thread(rssFeedParser).start();
	}
	
	public void setRSSFeedURL()
	{
		BufferedReader buff = null;
		String temp = "";
		Vector<String> vec = new Vector();
		try{
			buff = new BufferedReader( new InputStreamReader( new FileInputStream(Log.MAINDIR+Log.CONFDIR+Log.RSSURLFILE) , "UTF-8"));
			while( ( temp = buff.readLine()) != null ){
				
				vec.addElement( temp.split("\\|")[0] );
				
			}//while( ( temp = buff.readLine()) != null ){
			buff.close();
			
			feedURL = new String[vec.size()];
			int cnt = 0;
			for( Object obj : vec ){
				feedURL[cnt] = obj.toString();
				cnt++;
			}
			setRSSFeedURL(feedURL);
			
		}catch(Exception ex)
		{
			Log.errorLog(this, "setRSSFeedURL :: " + ex.toString());
		}
	}
	
	public void setRSSFeedURL(String[] feedURL)
	{
		try{
			this.feedURL = feedURL;
			//this.url = new URL(feedURL);
			
		}catch(Exception ex){
			Log.errorLog(this, "RSSFeed URL = " + ex);
		}
	}
	
	public void run()
	{
		while(isTrue){
			try{
				
				int company = 0;
				for( String furl : feedURL){
					
					this.url = new URL(furl);
					Feed feed = this.readFeed();
					//System.out.println(feed);
					Log.log(this, feed.toString());
					//System.out.println(feed);
					for( int count = feed.getMessage().size()-1 ; count >= 0 ; count-- )
					//for (FeedMessage message : feed.getMessage()) {//message.getDescription() + " :: " +
					{
						//FeedMessage message = feed.getMessage().get(count);

			        	feedDataWriter(feed, feed.getMessage().get(count), company);
			        	
			        	//System.out.println( "message.getPubDate() = " + message.getpubDate());
			            //System.out.println(cnt++ + " :: " +  message.getTitle() +" :: " +  message.getLink() + " :: " + message.getAuthor() );
			
			        }
			        company++;
				}
			}catch(Exception ex)
			{
				Log.errorLog(this, "feed run :: " + ex.toString()); 
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "feed run While Thread Sleep :: " + e.toString()); 
			}
			
		}//while(true){
        //System.out.println("+++++++++++++++++++++++++++++++++++++++");
	}
	
	private void feedDataWriter(Feed feed, FeedMessage message, int company)
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
		String filename = fo.format(cal.getTime());		
		SimpleDateFormat fos = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfEng = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		File mfile = new File(Log.MAINDIR+Log.RESDIR+Log.RSSFILESDIR+"\\" + filename+"_"+company);
		
		if( !feedDataDuplicatedCheck( message.getTitle().trim() , company) )
		{
			//Log.log(this, "mfile exits() = " + mfile.getName() + " : " + mfile.exists() );
			try {
				FileWriter fwriter = new FileWriter(Log.MAINDIR+Log.RESDIR+Log.RSSFILESDIR+"\\" + filename +"_"+company,true);
				
				try{
					String[] temp = message.getDate().split("T");					
					//System.out.println( message.getTitle() + " :: " +  frmat.format( frmat.parse( temp[0]+ " " + temp[1])) );
					
					//fwriter.write(strbuff.toString());				
					//String regex = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
					if( temp.length > 1 ){
						
						if( filename.equals( (fo.format( fos.parse( temp[0]+ " " + temp[1] ))).toString()) ){//if current date is feed date are same
							
							fwriter.write("##" + fos.format( fos.parse( temp[0]+ " " + temp[1] ))  + "##" + message.getTitle().trim() + "##" + message.getDescription().trim() + "##" + message.getLink().trim() + "##"  + message.getAuthor().trim() + "@@@");
							fwriter.write("\r\n");
						
						}
					}else{
						if( message.getDate().equals("") || message.getDate().equals(null)){
							if( filename.equals( (fo.format( dfEng.parse(message.getpubDate()) )).toString()) ){//if current date is feed date are same
								
								fwriter.write("##" + fos.format( dfEng.parse(message.getpubDate()) )  + "##" + message.getTitle().trim() + "##" + message.getDescription().trim() + "##" + message.getLink().trim() + "##"  + message.getAuthor().trim() + "@@@");
								fwriter.write("\r\n");
								
							}
						}else{
							if( filename.equals( (fo.format( dfEng.parse(message.getDate()) )).toString()) ){//if current date is feed date are same
								
								fwriter.write("##" + fos.format( dfEng.parse(message.getDate()) )  + "##" + message.getTitle().trim() + "##" + message.getDescription().trim() + "##" + message.getLink().trim() + "##"  + message.getAuthor().trim() + "@@@");
								fwriter.write("\r\n");
							
							}
						}
					}
						
				}catch(Exception ex){
					Log.errorLog(this, "feedDataWriter() :: temp[] :: " + ex.toString());
				}
				
				fwriter.close();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "feedDataWriter() :: " + ex.toString());
			}
			
		}
		
		/************************
		if( !(new File(Log.MAINDIR+Log.RESDIR+Log.RSSFILESDIR+"\\" + filename+"_"+company).exists()) ){//file not found
			try {
				FileWriter fwriter = new FileWriter(Log.MAINDIR+Log.RESDIR+Log.RSSFILESDIR+"\\" + filename +"_"+company,false);
				String[] temp = message.getDate().split("T");						
				//System.out.println( message.getTitle() + " :: " +  frmat.format( frmat.parse( temp[0]+ " " + temp[1])) );
				
				//fwriter.write("##" + fos.format( fos.parse( temp[0]+ " " + temp[1] ))  + "##" + message.getTitle().trim() + "##" + message.getDescription().trim() + "##" + message.getLink().trim() + "##"  + message.getAuthor().trim() + "@@@");
				//fwriter.write("\r\n");
				
				fwriter.write(strbuff.toString());
				
				fwriter.close();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "feedDataWriter() :: " + ex.toString());
			}			
		}
		/************************/
	}
	
	private boolean feedDataDuplicatedCheck(String title, int company)
	{
		boolean isDuplicated = false;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
		String filename = fo.format(cal.getTime());
		//strbuff = new StringBuffer();
		
		File file = new File(Log.MAINDIR+Log.RESDIR+Log.RSSFILESDIR+"\\" + filename+"_"+company);
		if( !file.exists()){
			 new File(file.getParent()).mkdir();
		}
			try{
				
					FileInputStream in = new FileInputStream(Log.MAINDIR+Log.RESDIR+Log.RSSFILESDIR+"\\"+filename+"_"+company); 
					BufferedReader buff = new BufferedReader( new InputStreamReader( in ,"euc-kr"  ));
					String temp = "";
					while( (temp = buff.readLine()) != null )
					{
						//System.out.println(temp.contains(title) + " title = " + title + " :::: temp :: " + temp);
						if( temp.contains(title)){
							isDuplicated = true;
							//System.out.println(isDuplicated + " title = " + title);
							break;
						}
						//strbuff.append(temp);
						//strbuff.append("\r\n");
						
					}
					buff.close();
					in.close();
					
			}catch(Exception ex)
			{
				Log.errorLog(this, "feedDataDuplicatedCheck() :: "+ ex.toString()); 
			}
		
		return isDuplicated;
	}
	
	public Feed readFeed(){
		Feed feed = null;
		
		try{
			boolean isFeedHeader = true;
			
			String description = "";
			String title = "";
			String link = "";
			String language = "";
			String copyright = "";
			String author = "";
			String pubdate = "";
			String guid = "";
			String dcdate = "";
		
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = read();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			SimpleDateFormat frmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while( eventReader.hasNext() ){
				
				XMLEvent event = eventReader.nextEvent();
				
				if( event.isStartElement() )
				{
					String localPart = event.asStartElement().getName().getLocalPart();
					
					switch( localPart )
					{
					
					case ITEM:
						if( isFeedHeader )
						{
							isFeedHeader = false;
							feed = new Feed(title, link, description, language, copyright, pubdate, dcdate);
						}
						
						event = eventReader.nextEvent();
						break;
					
					case TITLE:
						title = getCharacterData(event, eventReader);
						break;
						
					case DESCRIPTION:
						description = getCharacterData(event, eventReader);
						break;
						
					case LINK:
						link = getCharacterData(event, eventReader);
						break;
						
					case GUID:
						guid = getCharacterData(event, eventReader);
						break;
						
					case LANGUAGE:
						language = getCharacterData(event, eventReader);
						break;
						
					case AUTHOR:
						author = getCharacterData(event, eventReader);
						break;
						
					case PUB_DATE:
						pubdate = getCharacterData(event, eventReader);
						break;
						
					case COPYRIGHT:
						copyright = getCharacterData(event, eventReader);
						break;
						
					case DCDATE:
						dcdate = getCharacterData(event,eventReader);
						break;
					}//switch( localPart )
				}//if( event.isStartElement() )
				else if ( event.isEndElement() )
				{
					
					if( event.asEndElement().getName().getLocalPart() == (ITEM) )
					{
						
						FeedMessage message = new FeedMessage();
						message.setAuthor(author);
						message.setDescription(description);
						message.setGuid(guid);
						message.setLink(link);
						message.setTitle(title);
						message.setpubDate(pubdate);
						message.setDate(dcdate);
						
						feed.getMessage().add(message);
						event = eventReader.nextEvent();

						//DateTimeFormatter fomat = DateTimeFormatter.ISO_DATE_TIME;
						//System.out.println( message.getTitle() + " :: " +  (  frmat.parse( DateTime. message.getDcDate() ) )  );
						
						
						continue;
						
					}//if( event.asEndElement().getName().getLocalPart() == (ITEM) )
				}
				
				
				
				
			}//while( eventReader.hasNext() ){
			
			
		}catch(Exception ex){
			Log.errorLog(this, "readFeed = " + ex);
		}
		
		return feed;
	}//public Feed readFeed(){
	
	private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException
	{
		String result = "";
		event = eventReader.nextEvent();
		if( event instanceof Characters)
		{
			result = event.asCharacters().getData();
		}
		return result;
	}
	
	private InputStream read()
	{
		try{
			return url.openStream();
		}catch(Exception ex){
			Log.errorLog(this, "read = " + ex);
			return null;
		}
	}

}
