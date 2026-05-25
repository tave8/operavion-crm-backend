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

import java.util.Arrays;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Autowired
    private ProblemsEmailService problemsEmailService;

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * Catch async errors.
     * 
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            
            // log 
            LOGGER.error("Uncaught async exception in method: '{}'. Error: {}",
                    method.getName(), throwable.getMessage());

            // alert
            // problemsEmailService.alertDevIfNonLocal(
            //         "Uncaught error during async operation.",
            //         method.getName(),
            //         throwable.getMessage(),
            //         ExceptionUtils.getStackTrace(throwable)
            // );

            // *****************
            // PARAMS
            // *****************

            String subject = "Uncaught error during async operation";

            String details = "Method: '" + method.getName() + "'. " +
                    "Class: '" + method.getDeclaringClass().getSimpleName() + "'. ";

            String exceptionMessage = throwable.getMessage();

            String stackTrace = ExceptionUtils.getStackTrace(throwable);

            // *****************
            // SEND EMAIL
            // *****************
            
            problemsEmailService.alertDevIfNonLocal(subject, details, exceptionMessage, stackTrace);
            
        };
    }
    
}
