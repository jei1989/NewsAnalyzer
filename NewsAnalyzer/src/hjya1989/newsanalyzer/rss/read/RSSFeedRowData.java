package hjya1989.newsanalyzer.rss.read;

import java.util.Vector;

public class RSSFeedRowData {

	Vector<RSSFeedRowDataDetail> vec;
	
	public RSSFeedRowData()
	{
		vec = new Vector();
	}
	
	public Vector getVec()
	{
		return this.vec;
	}
	
	public void addVec(Object obj)
	{
		vec.addElement((RSSFeedRowDataDetail)obj);
	}
	
	public Object getVec(int cnt)
	{
		return vec.elementAt(cnt);
	}
	
	public Object getLastVec(){
		return vec.elementAt(vec.size()-1);
	}
	
	public Object getFirstVec()
	{
		return vec.elementAt(0);
	}
	
	public void removeAll()
	{
		this.vec.removeAllElements();
	}
	
}
