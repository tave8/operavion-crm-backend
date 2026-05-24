package giuseppetavella.zero_chiamate.config;

import giuseppetavella.zero_chiamate.domain.business.auth.AuthEmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.ProblemsEmailService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Autowired
    private ProblemsEmailService problemsEmailService;

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            
            LOGGER.error("Uncaught async exception in method: '{}'. Error: {}",
                    method.getName(), throwable.getMessage());

            problemsEmailService.alertDev(
                    "Uncaught error during async operation.",
                    method.getName(),
                    throwable.getMessage(),
                    ExceptionUtils.getStackTrace(throwable)
            );
            
        };
    }
    
}
