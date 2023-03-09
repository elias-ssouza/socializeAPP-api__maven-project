package DarkGreen.socializeAPPapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration {

    protected fun configure(http: HttpSecurity) {
        http.httpBasic()
    }

    @Bean
    public fun userDetailsService(): UserDetailsService {
        val elias = User.withDefaultPasswordEncoder()
            .username("elias")
            .password("123")
            .roles("USER")
            .build()

        val admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("ADMIN")
            .build()

        return InMemoryUserDetailsManager(elias, admin)
    }
}

