package net.gplatform.sudoor.server.social.autoconfig;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.GenericConnectionStatusView;
import org.springframework.web.servlet.View;

@Configuration
@ConditionalOnClass(value = { SocialConfigurerAdapter.class }, name = "org.springframework.social.weibo.connect.WeiboConnectionFactory")
@ConditionalOnProperty(prefix = "spring.social.weibo.", value = "app-id")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WeiboAutoConfiguration {

	@Configuration
	@EnableSocial
	@ConditionalOnWebApplication
	protected static class WeiboAutoConfigurationAdapter extends SocialConfigurerAdapter implements EnvironmentAware {
		private static final Logger LOG = LoggerFactory.getLogger(WeiboAutoConfigurationAdapter.class);
		public static final String WEIBO_CONNECTION_FACTORY_NAME = "org.springframework.social.weibo.connect.WeiboConnectionFactory";
		public static final String WEIBO_API_NAME = "org.springframework.social.weibo.api.Weibo";
		private RelaxedPropertyResolver properties;

		@Override
		public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
			configurer.addConnectionFactory(createConnectionFactory(this.properties));
		}

		@Override
		public void setEnvironment(Environment environment) {
			this.properties = new RelaxedPropertyResolver(environment, getPropertyPrefix());
		}

		protected String getPropertyPrefix() {
			return "spring.social.weibo.";
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected ConnectionFactory<?> createConnectionFactory(RelaxedPropertyResolver properties) {
			try {
				Class factoryClass = Class.forName(WEIBO_CONNECTION_FACTORY_NAME);
				Constructor constructor = factoryClass.getConstructor(String.class, String.class);
				ConnectionFactory factory = (ConnectionFactory) constructor.newInstance(properties.getRequiredProperty("app-id"),
						properties.getRequiredProperty("app-secret"));
				return factory;
			} catch (Exception e) {
				LOG.error("Error when createConnectionFactory", e);
				return null;
			}
		}

		@Bean
		@ConditionalOnMissingBean(name = WEIBO_API_NAME)
		@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Object weibo(ConnectionRepository repository) {
			try {
				Class apiClass = Class.forName(WEIBO_API_NAME);
				Connection connection = repository.findPrimaryConnection(apiClass);
				return connection != null ? connection.getApi() : null;
			} catch (Exception e) {
				LOG.error("Error when create weibo api", e);
				return null;
			}
		}

		@Bean(name = { "connect/weiboConnect", "connect/weiboConnected" })
		@ConditionalOnProperty(prefix = "spring.social.", value = "auto-connection-views")
		public View weiboConnectView() {
			return new GenericConnectionStatusView("weibo", "Weibo");
		}

	}
}