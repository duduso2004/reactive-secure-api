package br.com.duduso.reactive.secure.api.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.all;

@Slf4j
@Component
public class GlobalControllerExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final HttpStatusExceptionMapper httpStatusExceptionMapper;

    public GlobalControllerExceptionHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext,
                                            ServerCodecConfigurer serverCodecConfigurer,
                                            HttpStatusExceptionMapper httpStatusExceptionMapper) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        this.httpStatusExceptionMapper = httpStatusExceptionMapper;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        final var error = getError(request);
        final var httpStatus = this.httpStatusExceptionMapper.getHttStatus(error);
        return ServerResponse
                .status(httpStatus)
                .contentType(APPLICATION_JSON)
                .bodyValue(createErrorBody(error, httpStatus, request));
    }

    @Data
    @Builder
    @JsonInclude(NON_NULL)
    public static class ErrorBody {

        @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String messageCode;
        private String messageOrigin;
        private String message;
        private String path;
        private String projectName;
        private String rootBean;
        private List<InvalidArgument> invalidArguments;

    }

    @Data
    @Builder
    @JsonInclude(NON_NULL)
    public static class InvalidArgument {

        private String fieldName;
        private String errorMessage;

    }

    private ErrorBody createErrorBody(Throwable ex, HttpStatus httpStatus, ServerRequest request) {
       return ErrorBody.builder()
                .timestamp(now())
                .message(ex.getMessage())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .path(request.path())
                .build();
    }

}
