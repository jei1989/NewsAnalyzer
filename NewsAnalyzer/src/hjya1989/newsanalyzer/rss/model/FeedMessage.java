package hjya1989.newsanalyzer.rss.model;
//package de.vogella.rss.model;

public class FeedMessage {

		String title;
		String description;
		String link;
		String author;
		String guid;
		String pubDate;
		String date;
		
		public String getTitle()
		{
			return title;
		}
		
		public void setTitle(String title)
		{
			this.title = title;
		}
		
		public String getDescription()
		{
			return description;
		}
		
		public void setDescription(String description)
		{
			this.description = description;
		}
		
		public String getLink()
		{
			return link;
		}
		
		public void setLink(String link)
		{
			this.link = link;
		}
		
		public String getAuthor()
		{
			return author;
		}
		
		public void setAuthor(String author)
		{
			this.author = author;
		}
		
		public String getGuid()
		{
			return guid;
		}
		
		public void setGuid(String guid)
		{
			this.guid = guid;
		}
		
		public String getpubDate() {
			if (pubDate == null || pubDate.equals("") )
			{
				return getDate();
			}else{
				return pubDate;
			}			
		   
		}
	
		public void setpubDate(String pubDate) {
		  this.pubDate = pubDate;
		}		
	
		public String getDate() {
			if (date == null || date.equals("") )
			{
				return getpubDate();
			}else{
				return date;
			}			

		}
	
		public void setDate(String date) {
		  this.date = date;
		}
			
		public String toString()
		{
			return "FeedMessage [title = " + title + ", description = " + description 
											+ ", link = " + link + ", author = " + author + ", guid = " + guid
											+ ", pubDate=" + pubDate
											+ ", date=" + date 
											+"]";
		}
}
