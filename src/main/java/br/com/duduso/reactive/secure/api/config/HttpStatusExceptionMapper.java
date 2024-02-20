package br.com.duduso.reactive.secure.api.config;

import br.com.duduso.reactive.secure.api.exception.JwtTokenValidationException;
import br.com.duduso.reactive.secure.api.exception.UnauthorizedException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class HttpStatusExceptionMapper {

    private Optional<HttpStatus> httpStatus;
    private Map<Class<? extends Exception>, HttpStatus> exceptionHttpStatusMap;

    @PostConstruct
    public void postConstruct() {
        this.exceptionHttpStatusMap = new HashMap<>();
        this.exceptionHttpStatusMap.put(ConversionNotSupportedException.class, INTERNAL_SERVER_ERROR);
        this.exceptionHttpStatusMap.put(TypeMismatchException.class, BAD_REQUEST);
        this.exceptionHttpStatusMap.put(HttpMessageNotReadableException.class, BAD_REQUEST);
        this.exceptionHttpStatusMap.put(HttpMessageNotWritableException.class, INTERNAL_SERVER_ERROR);
        this.exceptionHttpStatusMap.put(MethodArgumentNotValidException.class, BAD_REQUEST);
        this.exceptionHttpStatusMap.put(BindException.class, BAD_REQUEST);
        this.exceptionHttpStatusMap.put(AsyncRequestTimeoutException.class, SERVICE_UNAVAILABLE);
        this.exceptionHttpStatusMap.put(JwtException.class, BAD_REQUEST);
        this.exceptionHttpStatusMap.put(JwtTokenValidationException.class, BAD_REQUEST);
        this.exceptionHttpStatusMap.put(MethodArgumentTypeMismatchException.class, BAD_REQUEST);
        this.exceptionHttpStatusMap.put(NoResourceFoundException.class, NOT_FOUND);
        this.exceptionHttpStatusMap.put(UnauthorizedException.class, UNAUTHORIZED);
    }

    public HttpStatus getHttStatus(Throwable ex) {
        this.httpStatus = this.exceptionHttpStatusMap.entrySet()
                .stream()
                .filter(e -> e.getKey().isAssignableFrom(ex.getClass()))
                .map(Map.Entry::getValue)
                .findFirst();
        return this.httpStatus.orElse(INTERNAL_SERVER_ERROR);
    }

    public boolean showStackTrace() {
        return this.httpStatus.isEmpty();
    }

}

