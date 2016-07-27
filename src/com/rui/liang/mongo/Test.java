package com.rui.liang.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class Test {

	private static final String mongoURL = "mongodb://localhost:27017/db";

	public static void main(String args[]) {
		MongoClient m = new MongoClient(new MongoClientURI(mongoURL));
		
	}

}
