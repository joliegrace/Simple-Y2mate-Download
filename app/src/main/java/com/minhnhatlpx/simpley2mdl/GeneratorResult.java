package com.minhnhatlpx.simpley2mdl;

public class GeneratorResult
{
	private String title;
	private String thumbnail;
	private String videoId;
	private String id;

	public GeneratorResult(){}
	
	public GeneratorResult(String title, String thumbnail, String videoId, String id)
	{
		this.title = title;
		this.thumbnail = thumbnail;
		this.videoId = videoId;
		this.id = id;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	public String getThumbnail()
	{
		return thumbnail;
	}

	public void setVideoId(String videoId)
	{
		this.videoId = videoId;
	}

	public String getVideoId()
	{
		return videoId;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}
}
