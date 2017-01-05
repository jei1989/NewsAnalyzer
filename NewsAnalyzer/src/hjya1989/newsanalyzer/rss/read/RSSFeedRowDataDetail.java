package hjya1989.newsanalyzer.rss.read;

public class RSSFeedRowDataDetail {
	
	String[] strValue;	
	
	String date;
	String title;
	String description;
	String link;
	String author;	
	String dcDate;
	
	public RSSFeedRowDataDetail(String str)
	{
		strValue = str.split("\\##");
		this.date = strValue[1];
		this.title = strValue[2];
		this.description = strValue[3];
		this.link = strValue[4];
		//this.dcDate = strValue[5];
	}
	
	public String[] getValue()
	{
		return this.strValue;
	}
	
	public String getDate()
	{
		return this.date;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public String getLink()
	{
		return this.link;
	}
	
	public String getAuthor()
	{
		return this.author;
	}
	
	
}
