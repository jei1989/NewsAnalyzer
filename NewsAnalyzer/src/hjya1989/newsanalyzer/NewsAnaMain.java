package hjya1989.newsanalyzer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import hjya1989.newsanalyzer.util.Log;
import hjya1989.newsanalyzer.nlp.NLPAnalyzer;
import hjya1989.newsanalyzer.rss.read.*;

public class NewsAnaMain implements Runnable{

	public NewsAnaWindow newsAnaWindow;
	TwitterAnalyzer twitterAnalyzer;
	RSSFeedParser rssFeedParser;
	RSSFeedAnalyzer rssFeedAnalyzer;
	
	NLPAnalyzer nlpAnalyzer;
	
	Thread rssStart;
	Thread nlpThread;
	Properties props = new Properties();
	
	public NewsAnaMain()
	{
		Log.MAINDIR = System.getProperty("user.dir");
		init();
	}
	
	public void init(){
		
		newsAnaWindow = new NewsAnaWindow(this);
		nlpAnalyzer = new NLPAnalyzer(this);
		twitterAnalyzer = new TwitterAnalyzer(this);
		rssFeedParser = new RSSFeedParser();
		rssFeedAnalyzer = new RSSFeedAnalyzer(this);
				
		//twitterAnalyzer.setcurrentDateToParsing(date);
		twitterAnalyzer.setStartDateToParsing("20161230 21:00:00");
		twitterAnalyzer.setEndDateToParsing("20161230 22:00:00");
		
		new Thread(this).start();
		
		//this.newsAnaWindow.btnRSSstart.doClick();
		
	}
	
	public String getProperty(String keyName) {
        String value = null;
  
        try {
            
            FileInputStream fis = new FileInputStream(Log.MAINDIR+Log.PROPERTIES_FILE);
            props.load(new java.io.BufferedInputStream(fis));
            value = props.getProperty(keyName).trim();
            System.out.println(keyName + " :: " + value);
            fis.close();
        } catch (java.lang.Exception e) {
        	Log.errorLog(this, "setProperty :: " + e.toString());
        }
        
        return value;
    }
	
	public void setProperty(String keyName, String value) {
        try {
            
            //FileInputStream fis  = new FileInputStream(Log.MAINDIR+Log.PROPERTIES_FILE);
            //props.load(new java.io.BufferedInputStream(fis));
            props.setProperty(keyName, value);
            props.store(new FileOutputStream(Log.MAINDIR+Log.PROPERTIES_FILE), "");
            //fis.close();
            System.out.println(keyName + " :: " + value);
        } catch(java.lang.Exception e) {
            Log.errorLog(this, "setProperty :: " + e.toString());
        }
    }
	
	public void run()
	{
		doCheckUI();
	}
	
	
	public void doCheckUI()
	{
		while(true)
		{
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "doCheckUI :: " + e.toString());
			}
			SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try{
				newsAnaWindow.txtLastChkTime.setText( dfm.format( this.nlpAnalyzer.getLastCheckTime()) );
			}catch(Exception ex){
				//Log.errorLog(this, "doCheckUI_01 :: " + ex.toString());
			}
			/**************
			try{
				newsAnaWindow.doNLPRequest();
				try{
					if( newsAnaWindow.textPaneRequest.getDocument().getLength() > 5000 )
					{
						newsAnaWindow.textPaneRequest.getDocument().remove(5000, newsAnaWindow.textPaneRequest.getDocument().getLength()-5000);
					}
				}catch(Exception ex){
					
				}
				
			}catch(Exception ex){
				
			}
			/**************/
			
		}
	}
	
	public void twitterParsingStart()
	{
		new Thread(twitterAnalyzer).start();
	}
	
	public void RSSFeedAnalyzerStart()
	{
		rssFeedAnalyzer.setStartDateToParsing("2017-01-02");
		rssFeedAnalyzer.setEndDateToParsing("2017-01-03");
		
		new Thread(rssFeedAnalyzer).start();
	}
	
	public void RSSFeedParsingStop(){

		rssFeedParser.isTrue = false;
		rssStart = null;
	}
	
	public void RSSFeedParsingStart(){

		rssFeedParser.isTrue = true;
		rssFeedParser.setRSSFeedURL();
		rssStart = new Thread(rssFeedParser);
		rssStart.start();
	}
	
	public void NLPAnalyzerStart()
	{
		nlpAnalyzer.isTrue = true;
		nlpAnalyzer.setStartDateToParsing( this.newsAnaWindow.dateChooserStart.getText() );
		nlpAnalyzer.setEndDateToParsing( this.newsAnaWindow.dateChooserEnd.getText() );
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			nlpAnalyzer.setLastCheckTime( df.parse(newsAnaWindow.txtLastChkTime.getText()) );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, " NLPAnalyzerStart :: " + e.toString());
		}
		
		nlpThread = new Thread(nlpAnalyzer);
		nlpThread.start();
	}
	
	public void NLPAnalyzerStop()
	{
		nlpAnalyzer.isTrue = false;
		nlpThread = null;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new NewsAnaMain();
		
	}

	public void setExpandTreeNode()
	{
		for( int cnt = 0 ; cnt < this.newsAnaWindow.tree.getRowCount() ; cnt++ )
		{
			this.newsAnaWindow.tree.expandRow( cnt );
		}
	}

	
	/************************/
	public DefaultMutableTreeNode addTreeGrand(String message, DefaultTreeModel treeModel, JTree tree){
		
		DefaultMutableTreeNode grand = addObject((DefaultMutableTreeNode)treeModel.getRoot(),(Object)message,true, treeModel , tree);
		
		
		//this.newsAnaWindow.tree.setModel(this.newsAnaWindow.defaultTreeModel);
		//this.newsAnaWindow.tree.revalidate();
		treeModel.reload();
		//this.newsAnaWindow.tree.repaint();
		return grand;
	}
	
	public DefaultMutableTreeNode addTreeMother(DefaultMutableTreeNode grand, String message, DefaultTreeModel treeModel, JTree tree){
		
		DefaultMutableTreeNode mother = addObject((DefaultMutableTreeNode)grand,(Object)message,true,treeModel ,  tree);
		
		
		//this.newsAnaWindow.tree.setModel(this.newsAnaWindow.defaultTreeModel);
		//this.newsAnaWindow.tree.revalidate();
		treeModel.reload();
		//this.newsAnaWindow.tree.repaint();
		return mother;
	}
	
	public void addTreeChild(DefaultMutableTreeNode mother, String message, DefaultTreeModel treeModel, JTree tree)
	{
		addObject((DefaultMutableTreeNode)mother,(Object)message,true, treeModel , tree);
		treeModel.reload();

		//TreePath path = tree.getSelectionPath();
		//DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        //node.add(new DefaultMutableTreeNode(textField.getText()));
		//this.newsAnaWindow.tree.expandPath(); //지금 경로까지 트리를 편다
	}
	/***********************/
	
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
			Object child,
			boolean shouldBeVisible, DefaultTreeModel treeModel, JTree tree)
	{
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
		try{
			if( parent == null)
			{
				parent = (DefaultMutableTreeNode)treeModel.getRoot();
			}
			
			treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
			tree.setModel(treeModel);
			
			if( shouldBeVisible )
			{
				tree.scrollPathToVisible(new TreePath(childNode.getPath()));
			}
		}catch(Exception ex){
			Log.errorLog(this, " addObject :: " + ex.toString());
		}
		return childNode;
	
	}//public DefaultMutableTreeNode addObject(
	
	public <E> DefaultMutableTreeNode searchNode(String nodeStr)
	{
		DefaultMutableTreeNode node = null;
		boolean isDuplicated = false;
		try{
			Enumeration<E> e = this.newsAnaWindow.rootTreeNode.breadthFirstEnumeration();
			
			while( e.hasMoreElements() )
			{
				node = (DefaultMutableTreeNode) e.nextElement();
				if( node.toString().contains(nodeStr)){
					//System.out.println( nodeStr.toString() );
					//System.out.println();
					//System.out.println( node.toString() );
					isDuplicated = true;
					break;
				}
			}
		}catch(Exception ex){
			Log.errorLog(this, "searchNode::Main :: " +  ex.toString());
		}
		
		if( isDuplicated ){
			return node;
		}else{
			return null;
		}
		
	}//
}
