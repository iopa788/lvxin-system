package com.farsunset.lvxin;

import java.lang.reflect.Modifier;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.farsunset.lvxin.mvc.interceptor.RestfulValidateInterceptor;
import com.farsunset.lvxin.mvc.interceptor.ClientFileUploadInterceptor;
import com.farsunset.lvxin.mvc.interceptor.ConsoleFileUploadInterceptor;
import com.farsunset.lvxin.mvc.interceptor.SessionValidateInterceptor;
import com.farsunset.lvxin.mvc.resolver.PageableArgumentResolver;
import com.farsunset.lvxin.mvc.resolver.TokenArgumentResolver;
import com.farsunset.lvxin.service.AccessTokenService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
@EnableAutoConfiguration
public class MVCConfig extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer {

	@Value("${sys.template.multipart.maxSize}")
	private long templateMaxSize;

	@Value("${sys.logo.multipart.maxSize}")
	private int logoMaxSize;

	@Value("${sys.client.multipart.maxSize}")
	private int clientMaxSize;

	@Autowired
	private AccessTokenService accessTokenService;

	@Bean
	@ConditionalOnMissingBean
	public GsonHttpMessageConverter gsonHttpMessageConverter() {
		GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
		Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.PROTECTED | Modifier.TRANSIENT | Modifier.FINAL | Modifier.STATIC)
				.create();
		converter.setGson(gson);
		return converter;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RestfulValidateInterceptor(accessTokenService)).addPathPatterns("/cgi/**");
		registry.addInterceptor(new SessionValidateInterceptor()).addPathPatterns("/console/**");
		registry.addInterceptor(new ClientFileUploadInterceptor(clientMaxSize)).addPathPatterns("/cgi/file/upload.api");

		registry.addInterceptor(new ConsoleFileUploadInterceptor(templateMaxSize)).addPathPatterns("/console/user/**");
		registry.addInterceptor(new ConsoleFileUploadInterceptor(templateMaxSize))
				.addPathPatterns("/console/organization/**");
		registry.addInterceptor(new ConsoleFileUploadInterceptor(logoMaxSize))
				.addPathPatterns("/console/publicAccount/setLogo.action");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new TokenArgumentResolver());
		argumentResolvers.add(new PageableArgumentResolver());
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
		container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));

	}

}