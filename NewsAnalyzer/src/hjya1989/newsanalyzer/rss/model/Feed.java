package hjya1989.newsanalyzer.rss.model;
//package de.vogella.rss.model;

import java.util.List;
import java.util.ArrayList;


public class Feed {

	final String title;
	final String link;
	final String description;
	final String language;
	final String copyright;
	final String pubDate;
	final String dcDate;
	
	final List<FeedMessage> entries = new ArrayList<FeedMessage>(); 
	
	public Feed(String title, String link, String description, String language, String copyright, String pubDate, String dcDate)
	{
		
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.copyright = copyright;
		this.pubDate = pubDate;
		this.dcDate = dcDate;
		
	}
	
	public List<FeedMessage> getMessage()
	{
		return entries;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getLink()
	{
		return link;
	}
	
	public String getDescrition()
	{
		return description;
	}
	
	public String getLanguage()
	{
		return language;
	}
	
	public String getCopyright()
	{
		return copyright;
	}
	
	public String getPubDate()
	{
		if (pubDate == null || pubDate.equals("") )
		{
			return getDcDate();
		}else{
			return pubDate;
		}
	}
	
	public String getDcDate()
	{
		if (dcDate == null || dcDate.equals("") )
		{
			return getPubDate();
		}else{
			return dcDate;
		}

	}
	
	public String toString()
	{
		return "Feed [copyright = " + copyright + ", desciption = " + description 
									+ ", langugae = " + language + " , link = " + link
									+", pubDate = " + pubDate + ", dcDate = " + dcDate +" , title = " + title +"]";
	}
}
