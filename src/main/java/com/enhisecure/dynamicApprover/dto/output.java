package com.enhisecure.dynamicApprover.dto;
import java.util.*;
public class output {
    private String id;
	private String name;
	private String type;
	
	public output(String id, String name, String type)
	{
		this.id=id;
		this.name=name;
		this.type=type;
	}
	public void setId(String id)
    {
        this.id=id;
    }
    public String getId()
    {
        return id;
    }
	public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
	public void setType(String type)
    {
        this.type=type;
    }
    public String getType()
    {
        return type;
    }
   

}
