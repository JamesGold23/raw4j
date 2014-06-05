/*
Copyright 2013 Cory Dissinger

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.*/

package com.cd.reddit.http.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

import com.cd.reddit.http.util.RedditRequestInput;

public class RedditRequestInputTest {

	private static final String nl = System.getProperty("line.separator");	
	
	RedditRequestInput obj1;
	RedditRequestInput obj2;
	
	@Before
	public void resetTestObjects(){
		obj1 = null;
		obj2 = null;
	}
	
	@Test
	public void testToString(){
		System.out.println("Testing testToString");
		System.out.println(nl);		
		
		List<String> testSegments1 = createTestSegments1();
		List<NameValuePair> queryParams = createTestQueryParams1();
		List<NameValuePair> formParams = createTestFormParams1();
		obj1 = new RedditRequestInput(testSegments1, queryParams, formParams);
		
		System.out.println(obj1);		
	}
	
	
	@Test
	public void testHashCodeTrue(){
		System.out.println("Testing testHashCodeTrue");
		System.out.println(nl);		
		
		List<String> testSegments1 = createTestSegments1();
		List<NameValuePair> queryParams = createTestQueryParams1();
		List<NameValuePair> formParams = createTestFormParams1();
		obj1 = new RedditRequestInput(testSegments1, queryParams, formParams);
		
		List<String> testSegments2 = createTestSegments1();
		List<NameValuePair> queryParams2 = createTestQueryParams1();
		List<NameValuePair> formParams2 = createTestFormParams1();
		obj2 = new RedditRequestInput(testSegments2, queryParams2, formParams2);
		
		assertEquals(obj1.hashCode(), obj2.hashCode());
	}
	
	@Test
	public void testHashCodeFalse(){
		System.out.println("Testing testHashCodeFalse");
		System.out.println(nl);		
		
		List<String> testSegments1 = createTestSegments1();
		List<NameValuePair> queryParams = createTestQueryParams1();
		List<NameValuePair> formParams = createTestFormParams1();
		obj1 = new RedditRequestInput(testSegments1, queryParams, formParams);
		
		List<String> testSegments2 = createTestSegments2();
		List<NameValuePair> queryParams2 = createTestQueryParams2();
		List<NameValuePair> formParams2 = createTestFormParams2();
		obj2 = new RedditRequestInput(testSegments2, queryParams2, formParams2);
		
		assertNotSame(obj1.hashCode(), obj2.hashCode());
	}
	
	private List<String> createTestSegments1() {
		List<String> testSegments1 = new ArrayList<String>(); 
		testSegments1.add("MYTEST");
		testSegments1.add("MYTEST2");
		return testSegments1;
	}	

	private List<String> createTestSegments2() {
		List<String> testSegments2 = new ArrayList<String>(); 
		testSegments2.add("MYTEST3");
		testSegments2.add("MYTEST4");
		return testSegments2;
	}
	
	private List<NameValuePair> createTestQueryParams1() {
		List<NameValuePair> testQueryParams1 = new ArrayList<NameValuePair>(); 
		testQueryParams1.add(new BasicNameValuePair("MYTEST", "MYTEST2"));
		testQueryParams1.add(new BasicNameValuePair("MYTEST2", "MYTEST3"));
		return testQueryParams1;
	}	

	private List<NameValuePair> createTestQueryParams2() {
		List<NameValuePair> testQueryParams2 = new ArrayList<NameValuePair>(); 
		testQueryParams2.add(new BasicNameValuePair("MYTEST5", "MYTEST6"));
		testQueryParams2.add(new BasicNameValuePair("MYTEST7", "MYTEST8"));
		return testQueryParams2;
	}
	
	private List<NameValuePair> createTestFormParams1() {
		List<NameValuePair> testFormParams1 = new ArrayList<NameValuePair>(); 
		testFormParams1.add(new BasicNameValuePair("MYTEST", "MYTEST2"));
		testFormParams1.add(new BasicNameValuePair("MYTEST2", "MYTEST3"));
		return testFormParams1;
	}	

	private List<NameValuePair> createTestFormParams2() {
		List<NameValuePair> testFormParams2 = new ArrayList<NameValuePair>(); 
		testFormParams2.add(new BasicNameValuePair("MYTEST5", "MYTEST6"));
		testFormParams2.add(new BasicNameValuePair("MYTEST7", "MYTEST8"));
		return testFormParams2;
	}	
}
