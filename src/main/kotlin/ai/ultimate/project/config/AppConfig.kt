package ai.ultimate.project.config

import ai.ultimate.project.config.AIClientConstants.AUTH_TOKEN
import ai.ultimate.project.config.AIClientConstants.TIMEOUT_MINUTES
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.time.Duration

@Profile("prod")
@Configuration
@ComponentScan
@EnableWebMvc
class AppConfig {

    @Bean
    fun okHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .addHeader("authorization", AUTH_TOKEN)
                .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build()

            chain.proceed(request)
    }
        clientBuilder.connectTimeout(Duration.ofMinutes(TIMEOUT_MINUTES))
        return clientBuilder.build()
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper
    }
}

object AIClientConstants {
    const val AUTH_TOKEN = "825765d4-7f8d-4d83-bb03-9d45ac9c27c0"
    const val TIMEOUT_MINUTES: Long = 10
}