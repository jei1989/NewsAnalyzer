package hjya1989.newsanalyzer.nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import hjya1989.newsanalyzer.*;
import hjya1989.newsanalyzer.util.Log;

public class NLPAnalyzer implements Runnable{

	NewsAnaMain newsAnaMain;
	//RSSFeedRowData rssFeedRowData;
	Date currentDateToParsing;
	Date startDateToParsing;
	Date endDateToParsing;
	
	Date doneStartDateToParsing;
	Date doneEndDateToParsing;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date lastCheckTime;
	Calendar cal;
	
	Vector<File> vecFiles;
	Vector<Object> vecObject;
	
	public boolean isTrue = true;
	
	public NLPAnalyzer(NewsAnaMain newsAnaMain)
	{
		this.newsAnaMain = newsAnaMain;
		
		vecObject = new Vector();
		//vecObject.elementAt(0) //chosun
		//vecObject.elementAt(1) //donga
		//vecObject.elementAt(2) //hankyoreh
	}
	public void setStartDateToParsing(String date)
	{
		try {
			startDateToParsing = df.parse(date);
			startDateToParsing.setTime( df.parse(date).getTime() - ((long) 1000 * 60 *60 * 24) );;
		} catch (ParseException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
	}
	public void setEndDateToParsing(String date)
	{
		try {
			endDateToParsing = df.parse(date);
			endDateToParsing.setTime( df.parse(date).getTime() + ((long) 1000 * 60 *60 * 24) );;
		} catch (ParseException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
	}	
	
	public void setLastCheckTime(Date date){
		this.lastCheckTime = date;
	}
	
	public Date getLastCheckTime(){
		return this.lastCheckTime;
	}
/*****************	
	public void addRssTree()
	{
		try{
			int cnt = 0;
			DefaultMutableTreeNode node = null;
			DefaultMutableTreeNode childnode = null;
			
			for( Object obj : this.vecObject)
			{
				
				RSSFeedRowData rssFeedRowData = (RSSFeedRowData)obj;
				//node = this.newsAnaMain.addTreeGrand("조선일보", this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				
				
				if( cnt == 0 )
				{
					node = this.newsAnaMain.addTreeGrand("조선일보", this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				}else if( cnt == 1)
				{
					node = this.newsAnaMain.addTreeGrand("동아일보", this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				}else if( cnt == 2)
				{
					node = this.newsAnaMain.addTreeGrand("한겨레신문", this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				}
				
				cnt++;
				String temp = "";
				for( Object detail : rssFeedRowData.getVec())
				//for( int count = this.rssFeedRowData.getVec().size()-1; count >= 0 ; count-- )////reverse parsing
				{
					//Object detail = rssFeedRowData.getVec().elementAt(count);//reverse parsing
					
					childnode = this.newsAnaMain.addTreeMother(node, ((RSSFeedRowDataDetail)detail).getTitle(), this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
					
					//temp = ((RSSFeedRowDataDetail)detail).getTitle();
					//this.newsAnaMain.addTreeChild(childnode, temp , this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
					temp = ((RSSFeedRowDataDetail)detail).getDescription();
					this.newsAnaMain.addTreeChild(childnode, temp , this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
					temp = ((RSSFeedRowDataDetail)detail).getLink();
					this.newsAnaMain.addTreeChild(childnode, temp , this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
					temp = ((RSSFeedRowDataDetail)detail).getDate();
					this.newsAnaMain.addTreeChild(childnode, temp , this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
					
				}
				
			}
						
			
		}catch(Exception ex)
		{
			Log.errorLog(this, "addRssTree :: " + ex.toString());
		}
	}
	/*****************/	
	
	public void run()
	{
		Analyzer();
	}
	
	public void Analyzer()
	{
		while(isTrue){

			File[] files = null;
			Date currFileDate = null;
			vecFiles = new Vector();
			
			try{
				files = new File(Log.MAINDIR + Log.RESDIR + Log.RSSFILESDIR).listFiles();
			}catch(Exception ex){
				Log.errorLog(this,"Analyzer Files[] = " + ex.toString());
			}
			
			try{
				for( File file : files){
					
					currFileDate = df.parse( file.getName().split("\\_")[0] );
					
					//System.out.println(file.getName().split("\\_")[0] + " : " + currFileDate + " :: " + currFileDate.after( this.startDateToParsing ) + " :: " + currFileDate.before( this.endDateToParsing));
					
					if( !newsAnaMain.newsAnaWindow.chkboxDateCurrent.isSelected() ){//chkbox is not selected , need to EndDate
						
						if( currFileDate.after( this.startDateToParsing ) && currFileDate.before( this.endDateToParsing) )
						{
							detailParser(file);
						}
						
					}else if( newsAnaMain.newsAnaWindow.chkboxDateCurrent.isSelected() ){//chkbox is selected , EndDate is need current date
						if( currFileDate.after( this.startDateToParsing ) )
						{
							detailParser(file);
						}
					}
					
				}
			}catch(Exception ex)
			{
				Log.errorLog(this, "Analyzer File = " + ex.toString());
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "doCheckUI :: " + e.toString());
			}
			
		}//while
		
	}
	
	private void detailParser(File file)
	{
		BufferedReader buff = null;
		StringBuffer strbuff = null;
		String temp = "";
		String[] parseString = null;
		try{
			
			int cnt = Integer.valueOf(((File)file).getName().split("\\_")[1]);
			
			strbuff = new StringBuffer();
			buff = new BufferedReader(new InputStreamReader(new FileInputStream( (File)file ),"euc-kr"));
			
			while( ( temp = buff.readLine() ) != null )
			{
				strbuff.append(temp);
			}
			
			//rssFeedRowData = new RSSFeedRowData();
			
			parseString = strbuff.toString().split("\\@@@");
			
			for( String parser : parseString ){
				//rssFeedRowData.addVec(new RSSFeedRowDataDetail(parser));
				if( lastCheckTime != null )
				{

					if( (lastCheckTime).before( dfm.parse( parser.toString().trim().split("\\##")[1]) ))
					{
						
						NLPWriter nlpwriter = new NLPWriter();
						nlpwriter.setNLPMessage( parser.toString().trim().split("\\##")[2] );
						nlpwriter.NLPParsing();
						//nlpwriter.setNLPMessage( parser.toString().trim().split("\\##")[3] );
						//nlpwriter.NLPParsing();
						
						lastCheckTime = dfm.parse( parser.toString().trim().split("\\##")[1]);
					}
				}else{

					NLPWriter nlpwriter = new NLPWriter();
					nlpwriter.setNLPMessage( parser.toString().trim().split("\\##")[2] );
					nlpwriter.NLPParsing();
					//nlpwriter.setNLPMessage( parser.toString().trim().split("\\##")[3] );
					//nlpwriter.NLPParsing();					
					
					lastCheckTime = dfm.parse( parser.toString().trim().split("\\##")[1]);
				}
				
			}
			//vecObject.addElement(rssFeedRowData);
			
		}catch(Exception ex){
			Log.errorLog(this, " detailParser :: " + ex.toString());
		}
		
	}
	
	/**********************
	private void detailParser(Vector vecFiles)
	{
		BufferedReader buff = null;
		StringBuffer strbuff = null;
		String temp = "";
		String[] parseString = null;
		try{
			for( Object file : vecFiles)
			{
				int cnt = Integer.valueOf(((File)file).getName().split("\\_")[1]);
				
				strbuff = new StringBuffer();
				buff = new BufferedReader(new InputStreamReader(new FileInputStream( (File)file ),"euc-kr"));
				
				while( ( temp = buff.readLine() ) != null )
				{
					strbuff.append(temp);
				}
				
				//rssFeedRowData = new RSSFeedRowData();
				
				parseString = strbuff.toString().split("\\@@@");
				
				for( String parser : parseString ){
					//rssFeedRowData.addVec(new RSSFeedRowDataDetail(parser));
				}
				//vecObject.addElement(rssFeedRowData);
			}
			
			//addRssTree();
			
		}catch(Exception ex){
			Log.errorLog(this, " detailParser :: " + ex.toString());
		}
		
	}
	/*******************/
	
}
