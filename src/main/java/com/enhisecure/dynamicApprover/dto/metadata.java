package com.enhisecure.dynamicApprover.dto;
import java.util.*;
public class metadata {
    private String callbackURL;
	private String responseMode;
	private String secret;
	private String triggerId;
	private String triggerType;
	
	public void setCallbackURL(String callbackURL)
    {
        this.callbackURL=callbackURL;
    }
    public String getCallbackURL()
    {
        return callbackURL;
    }
	public void setResponseMode(String responseMode)
    {
        this.responseMode=responseMode;
    }
    public String getResponseMode()
    {
        return responseMode;
    }
	public void setSecret(String secret)
    {
        this.secret=secret;
    }
    public String getSecret()
    {
        return secret;
    }
	public void setTriggerId(String triggerId)
    {
        this.triggerId=triggerId;
    }
    public String getTriggerId()
    {
        return triggerId;
    }
	public void setTriggerType(String triggerType)
    {
        this.triggerType=triggerType;
    }
    public String getTriggerType()
    {
        return triggerType;
    }
   

}
