package com.minhnhatlpx.simpley2mdl;

public class y2mate
{
	private String status;
	private String result;

	public y2mate(){}
	
	public y2mate(String status, String result)
	{
		this.status = status;
		this.result = result;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return status;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public String getResult()
	{
		return result;
	}
}
