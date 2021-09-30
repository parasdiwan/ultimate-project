package ai.ultimate.project

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Profile("test")
@TestConfiguration
class TestDataConfig: AbstractMongoClientConfiguration() {

    private lateinit var mongodExecutable: MongodExecutable

    override fun getDatabaseName(): String {
        return "ultimate"
    }

    @PostConstruct
    fun startMongo() {
        val ip = "127.0.0.1"
        val port = 27018
        val mongodConfig = MongodConfig
            .builder()
            .version(Version.V4_0_12)
            .net(Net(ip, port, Network.localhostIsIPv6()))
            .userName("root")
            .password("")
            .build()
        val starter = MongodStarter.getDefaultInstance()
        mongodExecutable = starter.prepare(mongodConfig)
        mongodExecutable.start()
    }

    override fun mongoClient(): MongoClient {
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString("mongodb://localhost:27018"))
            .build()
        return MongoClients.create(settings)
        return super.mongoClient()
    }

    @PreDestroy
    fun tearDown() {
        mongodExecutable.stop()
    }
}