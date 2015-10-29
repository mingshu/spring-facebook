package org.springframework.social.showcase.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories(basePackages = { "org.springframework.social.showcase.repository" })
public class MongoDbConfig extends AbstractMongoConfiguration {

	@Value("${mongo.db.databaseName}")
	private String databaseName;

	@Value("${mongo.db.address}")
	private String mongodbAddress;

	@Value("${mongo.db.port}")
	private int port;

	@Value("${mongo.db.username}")
	private String username;

	@Value("${mongo.db.password}")
	private String password;

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		ServerAddress serverAddress = new ServerAddress(mongodbAddress, port);
		// List<MongoCredential> mongoCredentials =
		// Arrays.asList(MongoCredential
		// .createMongoCRCredential(username, databaseName,
		// password.toCharArray()));
		// return new MongoClient(serverAddress, mongoCredentials);
		return new MongoClient(serverAddress);
	}
}
