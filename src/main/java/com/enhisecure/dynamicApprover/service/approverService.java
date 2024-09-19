package com.enhisecure.dynamicApprover.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enhisecure.dynamicApprover.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.stereotype.Service;
import com.enhisecure.dynamicApprover.dto.Approver;
import org.springframework.web.client.RestTemplate;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

@Service
public class approverService
{
    public Approver getApprover(com.enhisecure.dynamicApprover.dto.Request request) throws IOException
    {
        String userId=request.getRequestedFor().getId();
        String managerName = getManagerName(userId);
        System.out.println(approval(request));
		metadata metaData = request.getMetadata();
		String secret =metaData.getSecret();
		if(managerName==null || approval(request) || privileged(request)==false)
		{
			return new Approver(new output("","",""),secret);
		}
        String managerId=getManagerId(managerName);
        System.out.println(managerName);
        if(managerId==null || managerId.isBlank())
        {
            return new Approver(new output("","",""),secret);
        }
        return new Approver(new output(managerId,managerName,"IDENTITY"),secret);
    }
	public boolean privileged(com.enhisecure.dynamicApprover.dto.Request request) throws IOException
	{
		String privileged ="";
			List<RequestedItems> requestedItems= request.getRequestedItems();
		    for(int j=0;j<requestedItems.size();j++)
			{
				RequestedItems requestedItems1 =requestedItems.get(j);
				String requestedItemId=requestedItems1.getId();
				String access_token= getAccessToken();
            final OkHttpClient client = new OkHttpClient();
            String apiUrl = "https://partner9058.api.identitynow-demo.com/beta/access-profiles/"+requestedItemId;
            Request request1 = new Request.Builder().url(apiUrl).addHeader("Authorization", access_token)
                    .build();
            ResponseBody response = client.newCall(request1).execute().body();
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            JsonNode jsonNode = mapper.readTree(response.string());
			ArrayNode arrayNode = (ArrayNode) mapper.readTree(jsonNode.get("entitlements").traverse());

            List<JsonNode> list = new ArrayList<>();
            arrayNode.forEach(list::add);

            list.forEach(node -> System.out.println(node.toString()));
			for(int i=0;i<list.size();i++)
			{
				           
				JsonNode ele = list.get(i);
				String entitlementId = ele.get("id").asText();
				String entUrl = "https://partner9058.api.identitynow-demo.com/beta/entitlements/"+entitlementId;
				Request entrequest = new Request.Builder().url(entUrl).addHeader("Authorization", access_token)
                    .build();
				ResponseBody entresponse = client.newCall(entrequest).execute().body();
				ObjectMapper mapperent = new ObjectMapper();
				mapperent.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				JsonNode entNode = mapperent.readTree(entresponse.string());
				privileged = entNode.get("privileged").asText();
			if(privileged.equals("true"))
			{
				return true;
			}
			System.out.println(entNode);
		
			}
        
			
			}
			return false;
       
	}
    public boolean approval(com.enhisecure.dynamicApprover.dto.Request request) throws IOException {
        List<RequestedItems> requestedItems= request.getRequestedItems();
        for(int i=0;i<requestedItems.size();i++)
        {
            RequestedItems requestedItems1 =requestedItems.get(i);
            String requestedItemId=requestedItems1.getId();
            String access_token= getAccessToken();
            final OkHttpClient client = new OkHttpClient();
            String apiUrl = "https://partner9058.api.identitynow-demo.com/beta/access-profiles/"+requestedItemId;
            Request request1 = new Request.Builder().url(apiUrl).addHeader("Authorization", access_token)
                    .build();
            ResponseBody response = client.newCall(request1).execute().body();
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            JsonNode jsonNode = mapper.readTree(response.string());
            JsonNode managerNode = mapper.readTree(jsonNode.get("accessRequestConfig").toString());
            ArrayNode arrayNode = (ArrayNode) mapper.readTree(managerNode.get("approvalSchemes").traverse());

            List<JsonNode> list = new ArrayList<>();
            arrayNode.forEach(list::add);

            list.forEach(node -> System.out.println(node.toString()));
            if(list.size()==0)
                return true;

        }
        return false;
    }
    public String getManagerId(String name) throws IOException {
       String access_token=getAccessToken();
        final OkHttpClient client = new OkHttpClient();
        String apiUrl = "https://partner9058.api.identitynow-demo.com/beta/identities?filters=name eq \""+name+"\"";
        Request request = new Request.Builder().url(apiUrl).addHeader("Authorization", access_token)
                .build();
        ResponseBody response = client.newCall(request).execute().body();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        TypeReference<List<Manager>> mapType = new TypeReference<List<Manager>>() {};

		System.out.println(name);
        String managerId="";
        List<Manager> accounts = mapper.readValue(response.string(), mapType);
		Manager manager = accounts.get(0);
		System.out.println(manager.getId());
		managerId=manager.getId();
        return managerId;
		

    }
    public String getManagerName(String id) throws IOException {
        String URI = "https://partner9058.api.identitynow-demo.com/beta/identities/"+id;
        String access_token=getAccessToken();
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URI).addHeader("Authorization", access_token)
                .build();
        ResponseBody response = client.newCall(request).execute().body();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JsonNode jsonNode = mapper.readTree(response.string());
        System.out.println(jsonNode);
        String managerName = "";
        if(jsonNode.get("attributes")==null)
        {
            return null;
        }
        else {
            JsonNode managerNode = mapper.readTree(jsonNode.get("attributes").toString());

            if (managerNode.get("mgr") == null)
                return null;
            else
                managerName = managerNode.get("mgr").asText();
        }
		if(managerName.equals("Unknown Department Head"))
		{
			return "";
		}
        return managerName;

    }
    public String getAccessToken() throws IOException {
        String URI = "https://partner9058.api.identitynow-demo.com/oauth/token";
        final OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", "277ac88be6b44baa86143c4646bb41fc")
                .add("client_secret","89093af3fdbc69cd011ad11f76390cc298547f5dd1cea9f7bea0767fb1b03452")
                .build();

        Request request = new Request.Builder()
                .url(URI)
                .post(formBody)
                .build();

        ResponseBody response = client.newCall(request).execute().body();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JsonNode jsonNode = mapper.readTree(response.string());
        String access_token = jsonNode.get("access_token").asText();
        if(access_token!=null)
            return "Bearer "+access_token;
        else
            return "Cannot retrieve Access Token";    }
}
