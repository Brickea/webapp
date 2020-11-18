package zixiaowangfall2020.webapp;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class MetricsConfig {
    @Value("${server.port}")
    private static int appPort;

    public static final StatsDClient statsd = new NonBlockingStatsDClient("Webapp", "localhost", appPort);
}
