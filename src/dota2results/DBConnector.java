package dota2results;

import com.mongodb.MongoClient;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

/**
 * Java + MongoDB Hello world Example
 *
 */
public class DBConnector {

	MongoClient mongo;
	MongoCollection<Document> table;

	public DBConnector() {

		mongo = new MongoClient("127.0.0.1", 27017);
		MongoDatabase db = mongo.getDatabase("dota2results");

		boolean collectionExists = db.listCollectionNames()
				.into(new ArrayList<String>()).contains("match");

		if (!collectionExists) {
			db.createCollection("match");

			Document index = new Document("id", 1);
			table = mongo.getDatabase("dota2results").getCollection("match");
			table.createIndex(index, new IndexOptions().unique(true));

		} else {
			table = db.getCollection("match", Document.class);
		}
		
		
		
		

	}

	public void insert(Match m) {
		Document document = new Document();
		document.put("id", m.id);
		document.put("when", m.when);
		document.put("where", m.where);
		document.put("team1", m.team1);
		document.put("team2", m.team2);
		document.put("result", m.result);
		document.put("time", m.time);
		table.insertOne(document);
	}

	public void select() {
		MongoCursor<Document> cursor = table.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}

	}

	public void disconnectFromDB() {
		mongo.close();
	}

}
