package com.example.gromit.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.gromit.base.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess","code","message","result"})
public class BaseResponse<T> {

    private final int code;
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    /**
     * 요청에 성공한 경우
     */
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    public static <T> BaseResponse<T> onSuccess(T result){
        return new BaseResponse<>(200, true, "요청에 성공하였습니다.", result);
    }


    /**
     * 요청에 실패한 경우
     */

    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }

    public static <T> BaseResponse<T> onFailure(int code, String message) {
        return new BaseResponse<>(code, false, message,null);
    }
}
