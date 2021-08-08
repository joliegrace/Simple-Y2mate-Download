package com.minhnhatlpx.simpley2mdl;
import org.jsoup.nodes.*;
import org.jsoup.*;
import java.util.regex.*;
import java.util.*;
import org.jsoup.select.*;
import android.util.*;

public class HtmlParser
{
	public static GeneratorResult parserGenerator(String html)
	{
		GeneratorResult result = new GeneratorResult();
		
		Document doc = Jsoup.parse(html);
		
		//Parser Title,Thumbnail 
		try{
			Element thumb_title = doc.select("div[class=thumbnail cover]").first();

			Element a = thumb_title.getElementsByClass("video-thumbnail").first();

			Element img = a.getElementsByTag("img").first();

			Element div = thumb_title.getElementsByClass("caption text-left").first();

			result.setThumbnail(img.attr("src").toString());

			result.setTitle(div.getElementsByTag("b").text());

			//Parser var script( <script> at the bottom )

			Element script = doc.select("script").last();

			//Regex for parser javascript var 
			//Find v_id value
			Pattern p_vid = Pattern.compile("(?is)k_data_vid = \"(.+?)\""); 
			Matcher m_vid = p_vid.matcher(script.html()); 

			while( m_vid.find() )
			{
				result.setVideoId(m_vid.group(1));
			}
			//Find id value
			Pattern p_id = Pattern.compile("(?is)k__id = \"(.+?)\""); 
			Matcher m_id = p_id.matcher(script.html()); 

			while( m_id.find() )
			{
				result.setId(m_id.group(1));

			}
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
		return result;
	}
	
	public static List<Quality> getMp3Qualitys(String html)
	{
		List<Quality> qualitys = new ArrayList<>();
		
		try{
			Document doc = Jsoup.parse(html);

			Element ul = doc.select("ul[class=list-unstyled m-b-0]").first();

			Elements lis = ul.getElementsByTag("li");

			for(Element li : lis)
			{
				Quality quality = new Quality();

				Element a = li.getElementsByTag("a").first();

				quality.setQualityName(a.text());
				quality.setType("mp3");
				quality.setQuality(a.text().replace("kbps","").trim());

				qualitys.add(quality);

			}
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return qualitys;
	}
	
	public static List<Quality> getMp4Qualitys(String html)
	{
		List<Quality> qualitys = new ArrayList<>();
		try
		{
			Document doc = Jsoup.parse(html);

			Element table = doc.select("table[class=table table-bordered]").first();

			Element tbody = table.getElementsByTag("tbody").first();

			Elements trs = tbody.getElementsByTag("tr");


			for(Element tr : trs)
			{
				Quality quality = new Quality();

				Elements tds = tr.select("td");

				quality.setQualityName(tds.first().getElementsByTag("a").text());
				quality.setSize(tds.prev().prev().next().text());
				quality.setType(tds.last().getElementsByTag("a").attr("data-ftype"));
				quality.setQuality(tds.last().getElementsByTag("a").attr("data-fquality"));	

				qualitys.add(quality);

			}
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		if(qualitys.size()>0)
		{
			qualitys.remove(qualitys.size()-1);
		}
		
		
		return qualitys;
	}
	
	public static String getDownloadUrl(String html)
	{
		String url = null;
		
		try
		{
			
			Document doc = Jsoup.parse(html);
			
			Element div = doc.select("div[class=form-group has-success has-feedback]").first();
			
			Element a = div.getElementsByTag("a").first();
			
			url = a.attr("href");
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return url;
	}
}
