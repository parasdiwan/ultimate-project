package ai.ultimate.project.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Profile("prod")
@Configuration
@EnableMongoRepositories(basePackages = ["ai.ultimate.project.data"])
class DataConfig : AbstractMongoClientConfiguration() {
    private val DB_NAME = "ultimate"
    @Value("\${mongodb.username}")
    lateinit var dbUsername: String
    @Value("\${mongodb.password}")
    lateinit var dbPassword: String
    @Value("\${mongodb.dbName}")
    lateinit var dbName: String
    @Value("\${mongodb.host}")
    lateinit var dbHost: String

    override fun getDatabaseName(): String {
        return DB_NAME
    }

    override fun mongoClient(): MongoClient {
        val connectionString =
            ConnectionString("mongodb+srv://" +
                    dbUsername + ":" +
                    dbPassword + "@" +
                    dbHost + "/" +
                    dbName +
                    "?retryWrites=true&w=majority")
        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(settings)
    }
}