/*
 Copyright 2013 Cory Dissinger

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at 

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cd.reddit.http;

import java.io.IOException;
import java.net.*;
import java.util.List;
import com.cd.reddit.RedditException;
import com.cd.reddit.http.util.RedditRequestInput;
import com.cd.reddit.http.util.RedditRequestResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Necessary implementation of HttpURLConnection and friends.
 * <br/> 
 * <br/>
 * Needs to have SSL/HTTPS support as well as further analysis of other needs.
 * 
 * @author <a href="https://github.com/corydissinger">Cory Dissinger</a>
 * @author <a href="https://github.com/cybergeek94">Austin Bonander</a>
 */
public class RedditRequestor {
	//TODO: Add ability to connect to ssl.reddit.com because credentials should not be in plain-text!
	private static final String HOST = "www.reddit.com";
	
	private final String userAgent;
	
	private String modhash = null;
    private String session = null;

    public RedditRequestor(String userAgent) {
        this.userAgent = userAgent;
    }    

	public void setModhashHeader(String modhash) {
		this.modhash = modhash;
	}
	
    public void setSession(String session) {
        this.session = session;
    }

    public RedditRequestResponse executeGet(RedditRequestInput input) throws RedditException {
    	HttpRequestBase httpget = new HttpGet(generateURI(input.getPathSegments(), input.getQueryParams()));
    	return executeRequest(httpget, input);
    }

    public RedditRequestResponse executePost(final RedditRequestInput input) throws RedditException {    	 	
    	HttpRequestBase httppost = new HttpPost(generateURI(input.getPathSegments(), input.getQueryParams()));
    	return executeRequest(httppost, input);
    	
    }
    
    private RedditRequestResponse executeRequest(HttpRequestBase request, final RedditRequestInput input) throws RedditException {
    	CloseableHttpClient httpclient = HttpClients.createDefault();  
    	
    	// set the headers:    	
    	request.setHeader(HttpHeaders.USER_AGENT, userAgent);    	
        if(session != null)
        	request.setHeader("Cookie", "reddit_session=" + session);
        if(modhash != null)
            request.setHeader("X-Modhash", modhash);
    	
        // set the body params if this request is a POST:        
        if (request.getMethod() == "POST") {
        	UrlEncodedFormEntity body = new UrlEncodedFormEntity(input.getBodyParams(), Consts.UTF_8);    	
        	((HttpPost) request).setEntity(body);
        }
    	    	
    	// handle responses:    	
    	ResponseHandler<RedditRequestResponse> rh = new ResponseHandler<RedditRequestResponse>() {
    		@Override
    		public RedditRequestResponse handleResponse(
    	            final HttpResponse response) throws IOException {
    			int statusCode = response.getStatusLine().getStatusCode();
    	        HttpEntity entity = response.getEntity();    	        
        		if (entity == null) {
    	            throw new ClientProtocolException("Response contains no content");
    	        }
        		String responseBody = EntityUtils.toString(entity);
    	        if (statusCode != HttpStatus.SC_OK) {
    	        	throw new RedditException(generateErrorString(statusCode, input, responseBody));
    	        }    	        
    	        return new RedditRequestResponse(statusCode, responseBody);
    	    }
    	};
    	
    	// execute request
    	RedditRequestResponse rrr = null;
    	try {
    		if (request.getMethod().equals("POST")) {
    			rrr = httpclient.execute((HttpPost) request, rh);
    		} else {
    			rrr = httpclient.execute((HttpGet) request, rh);
    		}
    	} catch (IOException e ) {
    		throw new RedditException(e.getMessage());
    	}
    	
    	return rrr;
    }

    private URI generateURI(List<String> pathSegments, List<NameValuePair> queryParams) throws RedditException {
    	String path = "";
    	if (pathSegments != null) {
    		path += "/" + StringUtils.join(pathSegments, "/");
    	}
    	
    	// NOTE: URIBuilder encodes the URL automatically
    	URIBuilder ub = new URIBuilder();
    	ub.setScheme("http").setHost(HOST);
    	
    	if (pathSegments != null) {
    		ub.setPath(path);
    	}
    	
    	if (queryParams != null) {
    		ub.setParameters(queryParams);
    	}
    	
    	URI uri = null;
    	try {
    		uri = ub.build();
    	} catch (URISyntaxException e) {
    		throw new RedditException(e.getMessage());
    	}
    	
    	return uri;
    }
        	
    private String generateErrorString(final int statusCode,
            						   final RedditRequestInput input,
            						   final String responseBody) {
    	
        String nl = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();

        builder.append("--- STATUS CODE ---");
        builder.append(nl);
        builder.append(statusCode);
        builder.append(nl);
        builder.append("--- REQUEST INPUT---");
        builder.append(nl);
        builder.append(input.toString());
        builder.append(nl);
        builder.append("--- RESPONSE BODY---");
        builder.append(nl);
        builder.append(responseBody);
        builder.append(nl);

        return builder.toString();
    }
}
