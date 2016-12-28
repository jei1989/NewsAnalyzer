package hjya1989.newsanalyzer;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import hjya1989.newsanalyzer.util.Log;

public class NewsAnaMain {

	NewsAnaWindow newsAnaWindow;
	TwitterAnalyzer twitterAnalyzer;
	public NewsAnaMain()
	{
		Log.MAINDIR = System.getProperty("user.dir");
		init();
	}
	
	public void init(){
		
		newsAnaWindow = new NewsAnaWindow();
		twitterAnalyzer = new TwitterAnalyzer(this);
		
		//twitterAnalyzer.setcurrentDateToParsing(date);
		twitterAnalyzer.setStartDateToParsing("20161228 08:00:00");
		twitterAnalyzer.setEndDateToParsing("20161228 10:00:00");
		
		new Thread(twitterAnalyzer).start();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new NewsAnaMain();
		
	}


	
	/************************/
	public DefaultMutableTreeNode addTreeGrand(String message){
		
		DefaultMutableTreeNode grand = addObject((DefaultMutableTreeNode)this.newsAnaWindow.treeModel.getRoot(),(Object)message,true);
		
		
		//this.newsAnaWindow.tree.setModel(this.newsAnaWindow.defaultTreeModel);
		//this.newsAnaWindow.tree.revalidate();
		this.newsAnaWindow.treeModel.reload();
		//this.newsAnaWindow.tree.repaint();
		return grand;
	}
	
	public DefaultMutableTreeNode addTreeMother(DefaultMutableTreeNode grand, String message){
		
		DefaultMutableTreeNode mother = addObject((DefaultMutableTreeNode)grand,(Object)message,true);
		
		
		//this.newsAnaWindow.tree.setModel(this.newsAnaWindow.defaultTreeModel);
		//this.newsAnaWindow.tree.revalidate();
		this.newsAnaWindow.treeModel.reload();
		//this.newsAnaWindow.tree.repaint();
		return mother;
	}
	
	public void addTreeChild(DefaultMutableTreeNode mother, String message)
	{
		addObject((DefaultMutableTreeNode)mother,(Object)message,true);
		this.newsAnaWindow.treeModel.reload();

		//TreePath path = tree.getSelectionPath();
		//DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        //node.add(new DefaultMutableTreeNode(textField.getText()));
		//this.newsAnaWindow.tree.expandPath(); //지금 경로까지 트리를 편다
	}
	/***********************/
	
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
			Object child,
			boolean shouldBeVisible)
	{
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
		try{
			if( parent == null)
			{
				parent = this.newsAnaWindow.rootTreeNode;
			}
			
			this.newsAnaWindow.treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
			this.newsAnaWindow.tree.setModel(this.newsAnaWindow.treeModel);
			
			if( shouldBeVisible)
			{
				this.newsAnaWindow.tree.scrollPathToVisible(new TreePath(childNode.getPath()));
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
