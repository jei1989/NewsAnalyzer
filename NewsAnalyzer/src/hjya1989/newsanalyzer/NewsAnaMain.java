package hjya1989.newsanalyzer;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.AbstractListModel;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import hjya1989.newsanalyzer.util.Log;
import hjya1989.newsanalyzer.nlp.NLPAnalyzer;
import hjya1989.newsanalyzer.rss.read.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.layout.StackPane;

public class NewsAnaMain implements Runnable {

	public NewsAnaWindow newsAnaWindow;
	TwitterAnalyzer twitterAnalyzer;
	RSSFeedParser rssFeedParser;
	RSSFeedAnalyzer rssFeedAnalyzer;
	public TableAnalyzer tableAnalyzer;
	public CustomFileRead customFile;
	
	NLPAnalyzer nlpAnalyzer;
	
	Thread rssStart;
	Thread nlpThread;
	Properties props = new Properties();
	
	private JFXPanel fxPanel;
	private WebView webView;
	public WebEngine webEngine;
	
	private JFXPanel fxPanel_local;
	private WebView webView_local;
	public WebEngine webEngine_local;
	
	private PieChart pieChart;
	
	public String[] feedURL;
	
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
		
		tableAnalyzer = new TableAnalyzer();
		customFile = new CustomFileRead();
		//twitterAnalyzer.setcurrentDateToParsing(date);
		twitterAnalyzer.setStartDateToParsing("20161230 21:00:00");
		twitterAnalyzer.setEndDateToParsing("20161230 22:00:00");
		
		new Thread(this).start();
		

	    // Returns the page source in its current state, including
	    // any DOM updates that occurred after page load

		//this.newsAnaWindow.btnRSSstart.doClick();
		
		fxPanel = new JFXPanel();
		fxPanel_local = new JFXPanel();
	
		
		newsAnaWindow.scrollPane_5.setViewportView(fxPanel);
		newsAnaWindow.scrollPane_10.setViewportView(fxPanel_local);
		
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				webView = new WebView();
				fxPanel.setScene(new Scene(webView));
				webEngine = webView.getEngine();
				webEngine.load("http://www.google.com");	
								
			}
			
		});
		
		rssFeedParser.setRSSFeedURL();
		this.feedURL = rssFeedParser.getFeedName();
		
		int cnt = 0;
		for( String temp : this.feedURL ){
			try {
				this.newsAnaWindow.comboModel.addElement(cnt + " - " + temp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.errorLog(this, "feedURL :: " + e);
			}
			cnt++;
		}
		
		/************
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
					fxPanel_local.setScene(new Scene(pieChartStart(), 400, 250));
					
					//webView_local = new WebView();
					//fxPanel_local.setScene(new Scene(new StackPane()));
					//webEngine_local = webView_local.getEngine();
					//webEngine_local.load("file:///"+Log.MAINDIR+Log.CONFDIR+Log.REPORTHTMLFILE);//"file:///"+MAINDIR+CONFDIR+	
					//System.out.println(webEngine_local.getLocation());
								
			}
			
		});
		/************/
		
		newsAnaWindow.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
				setWebEngine( newsAnaWindow.table.getModel().getValueAt(newsAnaWindow.table.getSelectedRow(),2).toString() );
				
				
			}
		});
	}
	
	public void setCustomList()
	{
		if( this.newsAnaWindow.list.getModel().getSize() > 0)
		{
			int cnt = this.newsAnaWindow.listModel.getSize();
			for( int count = 0 ; count < cnt ; count ++ )
			{
				this.newsAnaWindow.listModel.remove(0);
			}
		}
		
		this.customFile.readFile();
		
		
		
		for( String str : this.customFile.getListFile()  ){
			this.newsAnaWindow.listModel.addElement(str);
		}
		//AbstractListModel listModel = this.newsAnaWindow.list.getModel();
		//listModel
	}
	
	public void setWebEngine(String url)
	{
		Platform.runLater(() -> {
			webEngine.load(url);
		});
		
		
	}
	
	/***********/
	public void setPieChart()
	{
		try{
		Platform.runLater(() -> {
			fxPanel_local.setScene(new Scene(pieChartStart(), 400, 250));
			//webEngine_local.load(url);
		});
		}catch(Exception ex){
			Log.errorLog(this, "setPieChart :: " + ex);
		}
		
		
	}
	/***********/
	
	public String getProperty(String keyName) {
        String value = null;
  
        try {
            
            FileInputStream fis = new FileInputStream(Log.MAINDIR+Log.PROPERTIES_FILE);
            props.load(new java.io.BufferedInputStream(fis));
            value = props.getProperty(keyName).trim();
            //System.out.println(keyName + " :: " + value);
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
            //System.out.println(keyName + " :: " + value);
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
		rssFeedAnalyzer.setStartDateToParsing( this.newsAnaWindow.dateChooserStart.getText() );
		rssFeedAnalyzer.setEndDateToParsing(this.newsAnaWindow.dateChooserEnd.getText());
		
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
		//treeModel.reload();
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
			
			//####################################################################
			treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
			/***********************
			if( !searchNode(childNode.toString()) ){
				treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
			}else{
				System.out.println(childNode.toString());
				//treeModel.setRoot(imsiNode);
				//imsiNode.add( childNode );
			}
			/***********************/
			//####################################################################
			
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
	
	public boolean searchNode(String nodeStr)
	{
		DefaultMutableTreeNode node = null;
		boolean isDuplicated = false;
		try{
			Enumeration e = this.newsAnaWindow.rootTreeNode.breadthFirstEnumeration();
			
			while( e.hasMoreElements() )
			{
				node = (DefaultMutableTreeNode) e.nextElement();
				if( node.toString().trim().contains(nodeStr.trim())){
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
		
		return isDuplicated;
		/*************
		if( isDuplicated ){
			return node;
		}else{
			return null;
		}
		/*************/
		
	}//
	
	public DefaultMutableTreeNode returnSearchNode(String nodeStr)
	{
		DefaultMutableTreeNode node = null;
		boolean isDuplicated = false;
		try{
			Enumeration e = this.newsAnaWindow.rootTreeNode.breadthFirstEnumeration();
			
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
		

		/*************/
		if( isDuplicated ){
			return node;
		}else{
			return null;
		}
		/*************/
		
	}//	
	
    public StackPane pieChartStart() {
        PieChart pieChart = new PieChart();
        pieChart.setData(getChartData());
        //primaryStage.setTitle("PieChart");

        StackPane root = new StackPane();
        root.getChildren().add(pieChart);
        
        return root;
        
    }

    private ObservableList<Data> getChartData() {
        ObservableList<Data> answer = FXCollections.observableArrayList();
        
        try{
	        String[][] finalArray = this.tableAnalyzer.getFinalArray();
	        if( finalArray != null && finalArray.length > 0 )
	        {
	        	int count = 11;
	        	if( finalArray.length > 11 ){
	        		count = 11;
	        	}else{
	        		count = finalArray.length;
	        	}
	        	int sum = 0;
	        	for( int cnt = finalArray.length-1 ; cnt > finalArray.length - count-1 ; cnt-- )
	        	{
	        		sum =  sum +  Integer.parseInt( finalArray[cnt][0] );
	        	}
	        	
	        	for( int cnt = finalArray.length-1 ; cnt > finalArray.length - count-1 ; cnt-- )
	        	{
	        		String str = finalArray[cnt][1] + "[ " + Math.round((Double.parseDouble(finalArray[cnt][0])/sum)*100) + " %]  " + finalArray[cnt][0] + "건";
	        		double dbl =  Double.parseDouble( String.valueOf((Double.parseDouble(finalArray[cnt][0])/sum)*100));
	        		if( !finalArray[cnt][1].equals("") && !(finalArray[cnt][1].length() == 1)){
	        			answer.add(new PieChart.Data(str, Math.round(dbl)));
	        		}
	        		
	        		//System.out.println(sum + " :: " + dbl + " :: " + Integer.parseInt(finalArray[cnt][0]));
	        	}
	        	
	        	
	        }
        }catch(Exception ex)
        {
        	Log.errorLog(this, "ObservableList :: " + ex);
        }
        
        /************
        answer.addAll(new PieChart.Data("java 17.56", 17.56),
                new PieChart.Data("JavaFx", 31.37),
                new PieChart.Data("JavaFx", 31.37));
        /************/
        return answer;
    }
}
