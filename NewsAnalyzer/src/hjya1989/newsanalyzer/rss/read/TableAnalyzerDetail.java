package hjya1989.newsanalyzer.rss.read;

public class TableAnalyzerDetail {
	
	
	private String keyword;
	private int count;
	
	public TableAnalyzerDetail()
	{
		
	}
	
	public void setKeyword(String keyword)
	{
		this.keyword = keyword;

	}
	public String getKeyword(){
		return this.keyword;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	public int getCount()
	{
		return this.count;
	}
	
}
