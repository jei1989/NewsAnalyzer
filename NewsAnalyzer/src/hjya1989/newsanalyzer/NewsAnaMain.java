package hjya1989.newsanalyzer;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import hjya1989.newsanalyzer.util.Log;
import hjya1989.newsanalyzer.rss.read.*;

public class NewsAnaMain {

	public NewsAnaWindow newsAnaWindow;
	TwitterAnalyzer twitterAnalyzer;
	RSSFeedParser rssFeedParser;
	RSSFeedAnalyzer rssFeedAnalyzer;
	
	public NewsAnaMain()
	{
		Log.MAINDIR = System.getProperty("user.dir");
		init();
	}
	
	public void init(){
		
		newsAnaWindow = new NewsAnaWindow(this);
		twitterAnalyzer = new TwitterAnalyzer(this);
		rssFeedParser = new RSSFeedParser();
		rssFeedAnalyzer = new RSSFeedAnalyzer(this);
				
		//twitterAnalyzer.setcurrentDateToParsing(date);
		twitterAnalyzer.setStartDateToParsing("20161230 21:00:00");
		twitterAnalyzer.setEndDateToParsing("20161230 22:00:00");
		
		
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
	
	public void RSSFeedParsingStart(){

		rssFeedParser.setRSSFeedURL();
		new Thread(rssFeedParser).start();
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
