package journal.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import journal.server.repositories.SQLRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final SQLRepository repository;
    
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
              return repository.findUserByEmail(username)
                .orElseThrow (()-> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Bean
    //data access object responsible to fetch user details and encode password
    public AuthenticationProvider authenticationProvider(){
        //dao = data access object
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //S3 config

    @Value ("${SPACES_ACCESS}")
    private String spacesAccess;

    @Value ("${SPACES_SECRET}")
    private String spacesSecret;

    @Bean
    public AmazonS3 createS3Client(){
        BasicAWSCredentials cred = new BasicAWSCredentials(spacesAccess, spacesSecret);
        EndpointConfiguration epConfig = new EndpointConfiguration ("sgp1.digitaloceanspaces.com", "sgp1");
        return AmazonS3ClientBuilder.standard().withEndpointConfiguration(epConfig).withCredentials(new AWSStaticCredentialsProvider(cred)).build();
    }

}
