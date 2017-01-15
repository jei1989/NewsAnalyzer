package hjya1989.newsanalyzer.rss.read;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import hjya1989.newsanalyzer.util.Log;

public class TableAnalyzer {

	private JTable table;
	private TableModel model;
	private String message;
	private Vector vec = new Vector();
	
	private String[][] finalArray;
	
	public TableAnalyzer()
	{
		
	}
	
	public String[][] getFinalArray()
	{
		return finalArray;
	}
	
	public void removeAll()
	{
		try{
		if( this.vec.size() > 0 ){
			this.vec.removeAllElements();
		}
		}catch(Exception ex)
		{
			Log.errorLog(this, "removeAll() :: " + ex); 
		}
	}
	
	public Vector getVec()
	{
		return this.vec;
	}
	
	public void setTable(JTable table)
	{
		this.table = table;
		this.model = this.table.getModel();
	}
	
	public void tableAnalyer()
	{
		try{
			
			StringBuffer buff = new StringBuffer();
			for( int cnt = 0 ; cnt < this.table.getRowCount() ; cnt++ )
			{
				buff.append( replaceStr( this.model.getValueAt(cnt, 1).toString() ));
			}//for( int cnt = 0 ; cnt < this.table.getRowCount() ; cnt++ )
			
			String[] str = buff.toString().split("\\ ");
			for( int cnt = 0 ; cnt < str.length ; cnt++ )
			{
				boolean isDup = false;
				
				String retstr = charCheck( str[cnt] );
				
				for( int subcnt = 0 ; subcnt < this.vec.size() ; subcnt++ )
				{
					TableAnalyzerDetail detail = (TableAnalyzerDetail)vec.elementAt(subcnt);
					if( retstr.toString().trim().equals( detail.getKeyword().trim() ) &&  !retstr.toString().trim().equals("\\ ") )
					{
						detail.setCount(  detail.getCount() + 1 );
						isDup = true;
						
					}
				}
				
				if( !isDup &&  !retstr.toString().trim().equals("\\ "))
				{
					TableAnalyzerDetail detail = new TableAnalyzerDetail();
					detail.setCount(1);
					detail.setKeyword(retstr.toString().trim());
					this.vec.add(detail);
				}
				
			}//for( int cnt = 0 ; cnt < str.length ; cnt++ )
			
			htmlWriter();
			
		}catch(Exception ex)
		{
			Log.errorLog(this, "tableAnalyzer() :: " + ex);
		}
	}
	
	private String charCheck(String temp)
	{
		String keyword = temp;
		try{
			if( temp.charAt( temp.length()-1 ) =='은' )
			{
				keyword = temp.substring(0, temp.length()-1);
			}else if( temp.charAt( temp.length()-1 ) =='는' )
			{
				keyword = temp.substring(0, temp.length()-1);
			}else if( temp.charAt( temp.length()-1 ) =='이' )
			{
				keyword = temp.substring(0, temp.length()-1);
			}else if( temp.charAt( temp.length()-1 ) =='가' )
			{
				keyword = temp.substring(0, temp.length()-1);
			}else if( temp.charAt( temp.length()-1 ) =='과' )
			{
				keyword = temp.substring(0, temp.length()-1);
			}else if( temp.charAt( temp.length()-1 ) =='와' )
			{
				keyword = temp.substring(0, temp.length()-1);
			}
		}catch(Exception ex){
			
		}		
		return keyword;
	}
	
	private String replaceStr(String temp)
	{
		this.message = temp;
		
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
		this.message = message.replace('“', ' ');
		this.message = message.replace('‘', ' ');
		this.message = message.replace('”', ' ');
		this.message = message.replace('’', ' ');
		this.message = message.replace('…', ' ');
		this.message = message.replace('\'', ' ');
		this.message = message.replace("\r\n", " ");
		this.message = message.replace("  ", " ");
		this.message = message.replace("   ", " ");
		this.message = message.replace("    ", " ");
		this.message = message.replace("[기자수첩]", " ");
		this.message = message.replace("[속보]", " ");		
		
		return this.message;
	}
	
	private void htmlWriter()
	{
		String[][] imsi = new String[this.vec.size()][2]; 
		
		for( int cnt = 0 ; cnt < this.vec.size() ; cnt++ )
		{
			TableAnalyzerDetail detail = (TableAnalyzerDetail)vec.elementAt(cnt);
			imsi[cnt][0] = String.valueOf( detail.getCount() );
			imsi[cnt][1] = detail.getKeyword();
			//System.out.println( detail.getCount() + " :: " + detail.getKeyword() );
		}	

		
		//String temp[][] = new String[this.vec.size()][2];
		
		for( int i=0 ; i < this.vec.size() ; i++)
		{
			for( int j=i+1 ; j < this.vec.size() ; j++)
			{
				if( Integer.parseInt( imsi[i][0] ) > Integer.parseInt( imsi[j][0] ) )
				{
					String temp = imsi[j][0];
					imsi[j][0] = imsi[i][0];
					imsi[i][0] = temp;
					
					String tempmsg = imsi[i][1];
					imsi[i][1] = imsi[j][1];
					imsi[j][1] = tempmsg;
					
				}
			}	
		}
		
		finalArray = imsi;
		
		/*****************
		StringBuffer buff = new StringBuffer();
		String line = "\r\n";
		buff.append( this.getHeader() );
		buff.append( "<table>" );
		buff.append(line);
		
		for( int i=this.vec.size()-1 ; i > 0 ; i--)
		{
			buff.append( "<tr>" );
			buff.append("<td>");
			if( imsi[i][1].toString().trim().equals("") ){
				buff.append("");
			}else{
				buff.append(imsi[i][0]);
			}
			buff.append("</td>");
			buff.append("<td>");
			if( imsi[i][1].toString().trim().equals("") ){
				buff.append("");
			}else{
				buff.append(imsi[i][1]);
			}
			
			buff.append("</td>");
			buff.append(line);
			buff.append( "</tr>" );
			buff.append(line);
			//System.out.println( i + " :: " + imsi[i][0] + " :: " +imsi[i][1] );
		}
		
		buff.append( "</table>" );
		buff.append( this.getTail() );
		
		fileWriter(buff.toString());
		/*****************/
	}
	
	private void fileWriter(String writer)
	{
		/************/
		BufferedWriter wbuff = null;
		try {
			
			wbuff = new BufferedWriter(new FileWriter( new File(Log.MAINDIR+Log.CONFDIR+Log.REPORTHTMLFILE)));
			wbuff.write(writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, "messageWriter :: wbuff :: " + e.toString());
		}finally{
			try {
				wbuff.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "messageWriter :: wbuff :: finally :: " + e.toString());
			}
		}
		/************/
	}
	
	private String getHeader()
	{
		String message = "";
		
		message = message + "<html>"
						  + "<head>"
						  + "<style>"
						  + "	  table {"
						  + "	    width: 100%;"
						  + "	    border-collapse: collapse;"
						  + "	  }"
						  + "	  th, td {"
						  + "	    border-top: 1px solid #bcbcbc;"
						  + "	    border-bottom: 1px solid #bcbcbc;"
						  + "	    padding: 5px 10px;"
						  + "	  }"
						  + "	</style>		"		
						  + "</head>"
						  + "<body>"
						  + "<link rel=\"stylesheet\" type=\"text/css\" href=\"common.css\" />"
						  +"\r\n";
		
		return message;
	}
	
	private String getTail()
	{
		String message = "";
		
		message = message + "\r\n"
					      + "</body>"
						  + "</html>";
				
		return message;		
	}
}
