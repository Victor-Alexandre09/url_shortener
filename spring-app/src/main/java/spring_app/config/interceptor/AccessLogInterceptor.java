package spring_app.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import spring_app.model.AccessLog;
import spring_app.respository.AccessLogRepository;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class AccessLogInterceptor implements HandlerInterceptor {

    private final AccessLogRepository accessLogRepository;

    public AccessLogInterceptor(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public boolean preHandle() throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
        if (response.getStatus() == HttpServletResponse.SC_FOUND) {
            Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String shortUrlId = pathVariables.get("id");

            if (shortUrlId != null) {
                AccessLog log = new AccessLog();
                log.setShortUrlId(shortUrlId);
                log.setAccessTime(LocalDateTime.now());
                log.setUserAgent(request.getHeader("User-Agent"));

                String ipAddress = request.getHeader("X-Forwarded-For");
                if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                }
                log.setIpAddress(ipAddress);

                accessLogRepository.save(log);
            }
        }
    }
}
