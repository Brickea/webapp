package zixiaowangfall2020.webapp;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@Configuration
public class AWSTopicConfig {

    @Value("${aws.topic.arn}")
    private static String topicArn;

    @Value("${aws.topic.region}")
    private String region;

    @Bean
    public AmazonSNS getAmazonS3Cient(){
        return AmazonSNSClientBuilder
                .standard()
                .withRegion(region)
                .build();
    }

    public static PublishRequest createCurrentTopicPublishRequest(String msg,String email){

        return new PublishRequest(topicArn,msg,email);
    }


}
