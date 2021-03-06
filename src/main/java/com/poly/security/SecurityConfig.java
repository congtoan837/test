package com.poly.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.poly.services.UserServiceImp;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserServiceImp userServiceImp;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	PasswordEncoder PasswordEncoder() {		
		return new BCryptPasswordEncoder();
	}
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userServiceImp)
            .passwordEncoder(new MyPasswordEncoder());
    }	
    
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().and().csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
			.antMatchers("/signin*").permitAll()
			.antMatchers("/signup*").permitAll()
			.antMatchers("/api/newblog*").hasAuthority("ADMIN")
				.antMatchers("/api/editblog*").hasAuthority("ADMIN")
				.antMatchers("/api/deleteblog*").hasAuthority("ADMIN")
				.antMatchers("/api/newproduct*").hasAuthority("ADMIN")
				.antMatchers("/api/editproduct*").hasAuthority("ADMIN")
				.antMatchers("/api/deleteproduct*").hasAuthority("ADMIN")
				.antMatchers("/api/newcustomer*").hasAuthority("ADMIN")
				.antMatchers("/api/editcustomer*").hasAuthority("ADMIN")
				.antMatchers("/api/deletecustomer*").hasAuthority("ADMIN")
				.antMatchers("/api/newpromo*").hasAuthority("ADMIN")
				.antMatchers("/api/editpromo*").hasAuthority("ADMIN")
				.antMatchers("/api/deletepromo*").hasAuthority("ADMIN")
				.antMatchers("/api/listorderbyadmin*").hasAuthority("ADMIN")
				.antMatchers("/api/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.and()
			.httpBasic();			
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public FilterRegistrationBean customCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}
