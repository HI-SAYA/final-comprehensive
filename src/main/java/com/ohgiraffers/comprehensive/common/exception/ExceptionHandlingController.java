package com.ohgiraffers.comprehensive.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/* @Controller
   @RestController -> @ResponseBody 가 붙어 있는 컨트롤러 */
@RestControllerAdvice
public class ExceptionHandlingController {

    /* 200번대 성공
     * 400번대 클라이언트 에러
     * 500번대 서버 에러
     * */

    /* 400 : Bad Request */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestException(BadRequestException e) {

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(e.getCode(), e.getMessage());

        return ResponseEntity.badRequest().body(exceptionResponse);
        // 400번 상태 코드 + { code : , message : } 가 바디 영역으로 넘어 간다.
    }

    /* 401 : UnAuthorized 인증 오류 => 이미 처리 되어 있음 */

    /* 403 : Forbidden 인가 오류 => 이미 처리 되어 있음 */

    /* 404 : Not Found 클라이언트가 틀린 건 없는데 찾아 보려고 하니 그런 리소스가 없을 때 */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundException(NotFoundException e) {

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(e.getCode(), e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    /* 409 : Conflict => 충돌. 논리적으로 해당 기능을 수행할 수 없는 경우 처리 */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> conflictException(ConflictException e) {

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(e.getCode(), e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    /* 500 : Server Errors */
    @ExceptionHandler(ServerInternalException.class)
    public ResponseEntity<ExceptionResponse> serverInternalException(ServerInternalException e) {

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(e.getCode(), e.getMessage());

        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    /* Validation Exception */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodValidException(MethodArgumentNotValidException e) {

        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(9000, defaultMessage);

        return ResponseEntity.badRequest().body(exceptionResponse);
    }


}
