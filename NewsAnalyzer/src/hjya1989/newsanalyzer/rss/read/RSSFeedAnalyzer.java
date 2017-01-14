package hjya1989.newsanalyzer.rss.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import hjya1989.newsanalyzer.*;
import hjya1989.newsanalyzer.util.Log;

public class RSSFeedAnalyzer implements Runnable{

	NewsAnaMain newsAnaMain;
	RSSFeedRowData rssFeedRowData;
	Date currentDateToParsing;
	Date startDateToParsing;
	Date endDateToParsing;
	
	Date doneStartDateToParsing;
	Date doneEndDateToParsing;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Vector<File> vecFiles;
	Vector<Object> vecObject;
	public RSSFeedAnalyzer(NewsAnaMain newsAnaMain)
	{
		this.newsAnaMain = newsAnaMain;
		
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
	
	public void removeTableAt(int row)
	{
		try{
			this.newsAnaMain.newsAnaWindow.model.fireTableRowsDeleted(row,row);
			this.newsAnaMain.newsAnaWindow.model.fireTableDataChanged();
		}catch(Exception ex){
			Log.errorLog(this, "removeTableAt() :: " + ex);
		}		
	}
	
	public void removeTableAll(){
		try{
			int max = this.newsAnaMain.newsAnaWindow.model.getRowCount();
			if( max > 0 ){
				for( int row = 0 ; row < max ; row++ )
				{
					//System.out.println( this.newsAnaMain.newsAnaWindow.model.getValueAt(0, 1) );
					this.newsAnaMain.newsAnaWindow.model.removeRow(0);
					
				}
				this.newsAnaMain.newsAnaWindow.model.fireTableDataChanged();
			}
		}catch(Exception ex){
			Log.errorLog(this, "removeTableAll() :: " + ex);
		}		
	}
	
	public void addTable()
	{
		removeTableAll();
		try{
			int row = 0;
			
			for( Object obj : this.vecObject)
			{
				RSSFeedRowData rssFeedRowData = (RSSFeedRowData)obj;
				
				for( Object detail : rssFeedRowData.getVec() )
				{
					//System.out.println( " " + String.valueOf(row+1) + " : " +  ((RSSFeedRowDataDetail)detail).getTitle());
					
					this.newsAnaMain.newsAnaWindow.model.addRow( new String[]{ " " + String.valueOf(row+1), ((RSSFeedRowDataDetail)detail).getTitle(),((RSSFeedRowDataDetail)detail).getLink()} );
					row++;
				}
			}
			this.newsAnaMain.newsAnaWindow.table.setRowHeight(25);
			this.newsAnaMain.newsAnaWindow.table.changeSelection(this.newsAnaMain.newsAnaWindow.model.getRowCount()-1, 0, false, false);
		}catch(Exception ex){
			Log.errorLog(this, "addTable() :: " + ex);
		}
			
	}
	
	public void addRssTree()
	{
		try{
			int cnt = 0;
			DefaultMutableTreeNode node = null;
			DefaultMutableTreeNode childnode = null;
			
			//Vector vec = new Vector();

			for( Object obj : this.vecObject)
			{
				
				RSSFeedRowData rssFeedRowData = (RSSFeedRowData)obj;
				//node = this.newsAnaMain.addTreeGrand("조선일보", this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				//System.out.println( this.newsAnaMain.newsAnaWindow.rssTreeModel.getPathToRoot( root )[1] );
				
				
				
				//#################################################
				/********************************
				RSSFeedRowDataDetail mdetail = (RSSFeedRowDataDetail)rssFeedRowData.getVec(cnt);
				System.out.println( mdetail.getTitle() );
				/********************************/
				//#################################################
				if( this.newsAnaMain.returnSearchNode(  ((File)vecFiles.elementAt(cnt)).getName() ) == null ) {
					node = this.newsAnaMain.addTreeGrand(((File)vecFiles.elementAt(cnt)).getName(), this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				}else{
					node = this.newsAnaMain.returnSearchNode(  ((File)vecFiles.elementAt(cnt)).getName() );
				}
				/***************
				if( cnt == 0 )
				{
					node = this.newsAnaMain.addTreeGrand(((File)vecFiles.elementAt(cnt)).getName(), this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				}else if( cnt == 1)
				{
					node = this.newsAnaMain.addTreeGrand("동아일보", this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				}else if( cnt == 2)
				{
					node = this.newsAnaMain.addTreeGrand("한겨레신문", this.newsAnaMain.newsAnaWindow.rssTreeModel,this.newsAnaMain.newsAnaWindow.rssTree);
				}
				/***************/
				cnt++;
				String temp = "";

				for( Object detail : rssFeedRowData.getVec())
				//for( int count = this.rssFeedRowData.getVec().size()-1; count >= 0 ; count-- )////reverse parsing
				{
					//Object detail = rssFeedRowData.getVec().elementAt(count);//reverse parsing
					if( this.newsAnaMain.returnSearchNode(  ((RSSFeedRowDataDetail)detail).getTitle() ) == null ) {
						childnode = this.newsAnaMain.addTreeMother(node, ((RSSFeedRowDataDetail)detail).getTitle(), this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
					}else{
						childnode = this.newsAnaMain.returnSearchNode(  ((RSSFeedRowDataDetail)detail).getTitle() );
					}
					
					//childnode = this.newsAnaMain.addTreeMother(node, ((RSSFeedRowDataDetail)detail).getTitle(), this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
					
					//temp = ((RSSFeedRowDataDetail)detail).getTitle();
					//this.newsAnaMain.addTreeChild(childnode, temp , this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
					if( !this.newsAnaMain.searchNode(  ((RSSFeedRowDataDetail)detail).getDescription()) ){
						
						temp = ((RSSFeedRowDataDetail)detail).getDescription();
						this.newsAnaMain.addTreeChild(childnode, temp , this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
						temp = ((RSSFeedRowDataDetail)detail).getLink();
						this.newsAnaMain.addTreeChild(childnode, temp , this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);
						temp = ((RSSFeedRowDataDetail)detail).getDate();
						this.newsAnaMain.addTreeChild(childnode, temp , this.newsAnaMain.newsAnaWindow.rssTreeModel, this.newsAnaMain.newsAnaWindow.rssTree);	
						
					}

				}
				
			}//for( Object obj : this.vecObject)

						
			
		}catch(Exception ex)
		{
			Log.errorLog(this, "addRssTree :: " + ex.toString());
		}
	}
	
	
	public void run()
	{
		Analyzer();
	}
	
	public void Analyzer()
	{
		
		File[] files = null;
		Date currFileDate = null;
		//vecFiles = new Vector();
		if( vecFiles != null ){
			vecFiles.removeAllElements();
		}else{
			vecFiles = new Vector();
		}
		
		try{
			files = new File(Log.MAINDIR + Log.RESDIR + Log.RSSFILESDIR).listFiles();
		}catch(Exception ex){
			Log.errorLog(this,"Analyzer Files[] = " + ex.toString());
		}
		
		try{
			for( File file : files){
				
				currFileDate = df.parse( file.getName().split("\\_")[0] );
				
				//System.out.println(file.getName().split("\\_")[0] + " : " + currFileDate + " :: " + currFileDate.after( this.startDateToParsing ) + " :: " + currFileDate.before( this.endDateToParsing));
				
				if( currFileDate.after( this.startDateToParsing ) && currFileDate.before( this.endDateToParsing) )
				{
					vecFiles.addElement(file);
					
				}
				
			}
		}catch(Exception ex)
		{
			Log.errorLog(this, "Analyzer File = " + ex.toString());
		}
		
		detailParser(vecFiles);
		
		//this.newsAnaMain.setWebEngine_local("file:///"+Log.MAINDIR+Log.CONFDIR+Log.REPORTHTMLFILE);
		this.newsAnaMain.setPieChart();
		
	}
	
	private void detailParser(Vector<File> vecFiles)
	{
		BufferedReader buff = null;
		StringBuffer strbuff = null;
		String temp = "";
		String[] parseString = null;
		try{
			
			
			
			vecObject = new Vector<Object>();
			try{
				for( Object file : vecFiles)
				{
					try{
						int cnt = Integer.valueOf(((File)file).getName().split("\\_")[1]);
						
						strbuff = new StringBuffer();
						buff = new BufferedReader(new InputStreamReader(new FileInputStream( (File)file ),"euc-kr"));
						
						while( ( temp = buff.readLine() ) != null )
						{
							strbuff.append(temp);
						}
						
						rssFeedRowData = new RSSFeedRowData();
						rssFeedRowData.removeAll();
						
						parseString = strbuff.toString().split("\\@@@");
						
						for( String parser : parseString ){
							rssFeedRowData.addVec(new RSSFeedRowDataDetail(parser));
						}
						vecObject.addElement(rssFeedRowData);
					}catch(Exception ex)
					{
						Log.errorLog(this, "detailparser2 :: 0 :: " + ex);
					}
				}
			}catch(Exception ex)
			{
				Log.errorLog(this, "detailparser2 :: " + ex);
			}
			/*************************
			for( int count =0 ; count < vecObject.size() ; count++ ){
				RSSFeedRowData rssFeedRowData = (RSSFeedRowData)vecObject.elementAt(count);
				for( int line = 0 ; line < rssFeedRowData.getVec().size() ; line++ ){
					System.out.println("RSSFeedRowDataDetail :: " + ((RSSFeedRowDataDetail)rssFeedRowData.getVec(line)).getDate() + " :: " +((RSSFeedRowDataDetail)rssFeedRowData.getVec(line)).getTitle() );
				}
			}
			/*************************/
			try{
				addRssTree();
				addTable();
			}catch(Exception ex)
			{
				Log.errorLog(this, "detailparser1 :: " + ex);
			}
			
			try{
				this.newsAnaMain.tableAnalyzer.removeAll();
				this.newsAnaMain.tableAnalyzer.setTable(this.newsAnaMain.newsAnaWindow.table);
				this.newsAnaMain.tableAnalyzer.tableAnalyer();
			}catch(Exception ex)
			{
				Log.errorLog(this, "detailparser1 :: " + ex);
			}
			
		}catch(Exception ex){
			Log.errorLog(this, " detailParser :: " + ex.toString());
		}
		
	}

	
}
