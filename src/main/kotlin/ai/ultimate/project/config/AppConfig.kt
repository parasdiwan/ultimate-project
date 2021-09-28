package ai.ultimate.project.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@ComponentScan
@EnableWebMvc
class AppConfig {

    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}