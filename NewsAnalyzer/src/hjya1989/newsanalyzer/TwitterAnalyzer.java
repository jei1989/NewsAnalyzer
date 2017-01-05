package hjya1989.newsanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import hjya1989.newsanalyzer.util.Log;

public class TwitterAnalyzer implements Runnable{

	Date currentDateToParsing;
	Date startDateToParsing;
	Date endDateToParsing;
	
	Date doneStartDateToParsing;
	Date doneEndDateToParsing;
	
	SimpleDateFormat df;
	SimpleDateFormat dfHour;
	
	NewsAnaMain newsAnaMain;
	
	public TwitterAnalyzer(NewsAnaMain newsAnaMain)
	{
		this.newsAnaMain = newsAnaMain; 
		df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		dfHour = new SimpleDateFormat("yyyyMMdd HH");
		
		init();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		twitterFilesList();
	}
	
	public void setCurrentDateToParsing(String date)
	{
		try {
			currentDateToParsing = df.parse(date);
			//currentDateToParsing.setTime( df.parse(date).getTime() - ((long) 1000 * 60 *60) );
		} catch (ParseException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
	}
	public void setStartDateToParsing(String date)
	{
		try {
			startDateToParsing = df.parse(date);
			startDateToParsing.setTime( df.parse(date).getTime() - ((long) 1000 * 60 *60) );;
		} catch (ParseException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
	}
	public void setEndDateToParsing(String date)
	{
		try {
			endDateToParsing = df.parse(date);
		} catch (ParseException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
	}
	
	private void init()
	{
		
	}
	
	private void twitterFilesList()
	{
		File dfile = null;
		File[] files = null;
		try{
			
			dfile = new File(Log.MAINDIR+Log.RESDIR+Log.TWITTERFILESDIR);
			if( dfile.exists() )
			{
				
				files = dfile.listFiles();
				for( File file : files ){
					twitterDateCheck(file);
				}
				
				this.newsAnaMain.setExpandTreeNode();
			}
			
		}catch(Exception ex)
		{
			Log.errorLog(this, ex.toString());
		}
	}
	
	private void twitterDateCheck(File file)
	{

		String tempFileName = null;
		Date currentFileDate = null;
		int compare = 0;
		
		tempFileName = file.getName().substring(0, file.getName().lastIndexOf("."));

		try {
				//dfHour.parse
			
			currentFileDate = dfHour.parse( tempFileName.split("_")[0] + " " + tempFileName.split("_")[1] );
			
			if( currentFileDate.after( startDateToParsing ) && currentFileDate.before( endDateToParsing )){

				if( this.doneEndDateToParsing == null && this.doneStartDateToParsing == null ){
					
					try {
						this.doneEndDateToParsing = dfHour.parse( tempFileName.split("_")[0] + " " + tempFileName.split("_")[1] );
						this.doneStartDateToParsing = dfHour.parse( tempFileName.split("_")[0] + " " + tempFileName.split("_")[1] );
					} catch (ParseException ex) {
						// TODO Auto-generated catch block
						Log.errorLog(this, ex.toString());
					}
					
				}//if( this.doneEndDateToParsing == null && this.doneStartDateToParsing == null ){				
				
				if( !( currentFileDate.after( this.doneStartDateToParsing )  && currentFileDate.before( this.doneEndDateToParsing )) ){
				
					twitterDataParser(file);
					
					compare = this.doneStartDateToParsing.compareTo( currentFileDate );
					if( compare > 0 ) 
					{
						doneStartDateToParsing = currentFileDate;
						//this.doneStartDateToParsing.setTime( currentFileDate.getTime() + ((long) 1000 * 60 * 60) );;
					}
					
					compare = this.doneEndDateToParsing.compareTo(currentFileDate);
					if( compare < 0 )
					{
						this.doneEndDateToParsing = currentFileDate;
					}
					
					System.out.println(this.doneStartDateToParsing + " :::: " + this.doneEndDateToParsing + " :: " +  startDateToParsing + " :: " + endDateToParsing + " :: " + currentFileDate);
					
					
				}//if( this.doneStartDateToParsing.after( currentFileDate ) && this.doneEndDateToParsing.before( currentFileDate ) )
					
			}//if( currentFileDate.after( this.startDateToParsing ) && currentFileDate.before( this.endDateToParsing ) ){
		} catch (Exception ex) {
				// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		} 
	}
	
	private void twitterDataParser(File file){
		
		FileInputStream fin = null;
		BufferedReader buff = null;		
		try{
			fin = new FileInputStream(file);
		}catch(Exception ex){
			Log.errorLog(this, ex.toString());
		}
		
		try{
			buff = new BufferedReader( new InputStreamReader( fin ) );//, "UTF-8"));
			//buff = new BufferedReader( new InputStreamReader( fin , "UTF-8"));
			
			String temp = "";
			
			while( (temp = buff.readLine()) != null){
				
				if ( !(temp.substring(temp.length()-2, temp.length()).equals("##")) )
				{
					String temp2 = "";
					String total = "";
					while( (temp2 = buff.readLine()) != null ){
						//System.out.println("temp2 :: " + temp2);
						if( temp2.length() >= 2 ){
							if ( !(temp2.substring(temp2.length()-2, temp2.length()).equals("##")) )
							{
								total = total + "\r\n" + temp2;
								//System.out.println("total :: " + total);
							}else{
								temp = temp + total + "\r\n" +  temp2;
								//System.out.println("temp :: " + temp);
								break;
							}
						}else{
							
						}
						
					}
				}
				detailDataParser(temp);
								
			}
			
		}catch(Exception ex){
			Log.errorLog(this, ex.toString());
		}finally{
			try {
				buff.close();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				Log.errorLog(this, ex.toString());
			}
			try {
				fin.close();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				Log.errorLog(this, ex.toString());
			}
		}
		
	}
	
	private void detailDataParser(String line)
	{	
		
		try {
			String[] spliteStr = line.split("##");
		//##20161228_02:24:55##ooamoon17##ina570##2##Wed Dec 28 02:25:27 KST 2016##RT @ooamoon17: 주진우 기자 "조기대선 준비는 되셨나요?"
		//1 - Date, "_" you have to use seperator "_" 
		//2 - maintiwitter
		//3 - retiwitter
		//4 - count
		//5 - writer date
		//6 - content : if you want to only content, you have to use seprator " : " [1]
			if( spliteStr.length > 6 ){
				
				//System.out.println("");
				//System.out.println("===================================================================");
				//System.out.println("===================================================================");
				
				DefaultMutableTreeNode node = searchTreeNode(spliteStr[6]);
				
				//System.out.println("===================================================================");
				//System.out.println("===================================================================");
				//System.out.println("");
				
				if( node == null ){
				
					this.newsAnaMain.newsAnaWindow.grandTreeNode = this.newsAnaMain.addTreeGrand( spliteStr[2], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.newsAnaWindow.motherTreeNode = this.newsAnaMain.addTreeMother((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.grandTreeNode,  spliteStr[3], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.addTreeChild((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.motherTreeNode,  spliteStr[1], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.addTreeChild((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.motherTreeNode,  spliteStr[4], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.addTreeChild((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.motherTreeNode,  spliteStr[5], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.addTreeChild((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.motherTreeNode,  spliteStr[6], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
				
				}else if( node != null ){
					this.newsAnaMain.newsAnaWindow.grandTreeNode = (DefaultMutableTreeNode)node.getParent().getParent();
					this.newsAnaMain.newsAnaWindow.motherTreeNode = this.newsAnaMain.addTreeMother((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.grandTreeNode,  spliteStr[3], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.addTreeChild((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.motherTreeNode,  spliteStr[1], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.addTreeChild((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.motherTreeNode,  spliteStr[4], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.addTreeChild((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.motherTreeNode,  spliteStr[5], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
					this.newsAnaMain.addTreeChild((DefaultMutableTreeNode)this.newsAnaMain.newsAnaWindow.motherTreeNode,  spliteStr[6], newsAnaMain.newsAnaWindow.treeModel, newsAnaMain.newsAnaWindow.tree );
				}
				
				
			}//if( spliteStr.length > 6 ){

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, "detailDataParser :: " + ex.toString());
		}
		
	}
	
	private DefaultMutableTreeNode searchTreeNode(String compare)
	{
		DefaultMutableTreeNode node = null;
		try{
			String compareString = "";
			String[] temp = compare.split(": ");
			if( temp.length < 2)
			{
				compareString = temp[0];
			}else{
				
				compareString = temp[1];
			}
			
			//System.out.println(compareString);
			
			node = this.newsAnaMain.searchNode(compareString);
			
		}catch(Exception ex){
			Log.errorLog(this, "searchTreeNode :: " + ex.toString());
		}finally{
			return node;
		}
		//this.newsAnaMain.newsAnaWindow.treeModel
		
		
		//return null;
	}
/**********	
	private void setManufacture()
	{
		FileInputStream fin = null;
		BufferedReader buff = null;
		Vector imVec = new Vector();
		try {
			fin = new FileInputStream(Log.MAINDIR+Log.ManufactureFILE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
		
		try {
			buff = new BufferedReader( new InputStreamReader( fin,"UTF-8" ));
			String temp = "";
			int clm = 0;
			while( (temp = buff.readLine()) != null){
				clm = temp.split("\\|").length;
				imVec.add(temp);
			}
			
			this.manufacture = new String[imVec.size()][clm];
			//buff = new BufferedReader( new InputStreamReader( fin ));
			//System.out.println("test");
			//while( (temp = buff.readLine()) != null){
			for( int cnt = 0 ; cnt < imVec.size() ; cnt++ ){
				
				for( int scnt = 0 ; scnt < clm ; scnt++ ){
					//this.manufacture[row][cnt] = temp.split("|")[cnt];
					this.manufacture[cnt][scnt] = imVec.elementAt(cnt).toString().split("\\|")[scnt];
					//System.out.println("row = " + row + " clm = " + clm + " this.manufacture[row][scnt] = " + this.manufacture[row][scnt]);
				}
			}
			
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
		
		try{
			buff.close();
			fin.close();
		}
		catch(Exception eex)
		{
			Log.errorLog(this, eex.toString());
		}
		
	}	
	/**********/
}
