package ai.ultimate.project

import okhttp3.OkHttpClient
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@TestConfiguration
@Profile("test")
class TestConfig {

    @Primary
    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}