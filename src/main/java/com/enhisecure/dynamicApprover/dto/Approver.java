package com.enhisecure.dynamicApprover.dto;

public class Approver
{
    private output outPut;
    private String secret;

    public Approver(output outPut,String secret)
    {
        this.outPut=outPut;
        this.secret=secret;
    }
    public output getOutput()
    {
        return outPut;
    }
    public void setOutput(output outPut) {
        this.outPut = outPut;
    }
    public void setSecret(String secret)
    {
        this.secret =secret;
    }
    public String getSecret()
    {
        return secret;
    }

}