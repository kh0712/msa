package study.userservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * Feign Client Global Error Handler
 */

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                break;
            case 404:
                if(methodKey.contains("getOrders")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            response.reason());
                    // 하드코딩 대신에 프로퍼티 파일에서 가져올 수도 있다.
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
