package com.hotpack.krocs.global.common.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hotpack.krocs.global.common.response.code.BaseCode;
import com.hotpack.krocs.global.common.response.code.resultCode.ErrorStatus;
import com.hotpack.krocs.global.common.response.code.resultCode.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> success(T result){
        return new ApiResponse<>(true, SuccessStatus._OK.getCode() , SuccessStatus._OK.getMessage(), result);
    }

    /**
     * 성공 응답 생성 (데이터 없음)
     */
    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>(true, SuccessStatus._OK.getCode() , SuccessStatus._OK.getMessage(), null);
    }

    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponse<T> failure(String code, String message){
        return new ApiResponse<>(false, code, message, null);
    }

    /**
     * 실패 응답 생성 (데이터 포함)
     */
    public static <T> ApiResponse<T> failure(String code, String message, T data){
        return new ApiResponse<>(false, code, message, data);
    }

    // ApiResponseUtil에서 통합된 메서드들

    /**
     * 성공 응답 생성 (데이터 포함) - ApiResponseUtil 호환성
     */
    public static <T> ApiResponse<T> onSuccess(T data) {
        return success(data);
    }

    /**
     * 성공 응답 생성 (데이터 없음) - ApiResponseUtil 호환성
     */
    public static <T> ApiResponse<T> onSuccess() {
        return success();
    }

    /**
     * 성공 응답 생성 (커스텀 코드)
     */
    public static <T> ApiResponse<T> of(BaseCode code, T data) {
        return new ApiResponse<>(true, code.getReason().getCode(), code.getReason().getMessage(), data);
    }

    /**
     * 실패 응답 생성 (기본 에러) - ApiResponseUtil 호환성
     */
    public static <T> ApiResponse<T> onFailure(ErrorStatus errorStatus) {
        return failure(errorStatus.getCode(), errorStatus.getMessage());
    }

    /**
     * 실패 응답 생성 (커스텀 메시지)
     */
    public static <T> ApiResponse<T> onFailure(ErrorStatus errorStatus, String customMessage) {
        return failure(errorStatus.getCode(), customMessage);
    }

    /**
     * 실패 응답 생성 (데이터 포함) - ApiResponseUtil 호환성
     */
    public static <T> ApiResponse<T> onFailure(ErrorStatus errorStatus, T data) {
        return failure(errorStatus.getCode(), errorStatus.getMessage(), data);
    }

    /**
     * 실패 응답 생성 (커스텀 코드와 메시지) - ApiResponseUtil 호환성
     */
    public static <T> ApiResponse<T> onFailure(String code, String message) {
        return failure(code, message);
    }

    /**
     * 실패 응답 생성 (에러 포인트 포함)
     */
    public static <T> ApiResponse<T> onFailure(String code, String message, String errorPoint) {
        return new ApiResponse<>(false, code, message + (errorPoint != null ? " - " + errorPoint : ""), null);
    }

    /**
     * 실패 응답 생성 (에러 인자 포함)
     */
    public static <T> ApiResponse<T> onFailure(String code, String message, Map<String, String> errorArgs) {
        return new ApiResponse<>(false, code, message, (T) errorArgs);
    }

    /**
     * 리소스 없음 응답
     */
    public static <T> ApiResponse<T> notFound() {
        return failure(ErrorStatus.RESOURCE_NOT_FOUND.getCode(), ErrorStatus.RESOURCE_NOT_FOUND.getMessage());
    }

    /**
     * 중복 리소스 응답
     */
    public static <T> ApiResponse<T> conflict() {
        return failure(ErrorStatus.DUPLICATE_RESOURCE.getCode(), ErrorStatus.DUPLICATE_RESOURCE.getMessage());
    }

    /**
     * 검증 실패 응답
     */
    public static <T> ApiResponse<T> validationError() {
        return failure(ErrorStatus.VALIDATION_ERROR.getCode(), ErrorStatus.VALIDATION_ERROR.getMessage());
    }

    /**
     * 비즈니스 로직 오류 응답
     */
    public static <T> ApiResponse<T> businessError() {
        return failure(ErrorStatus.BUSINESS_LOGIC_ERROR.getCode(), ErrorStatus.BUSINESS_LOGIC_ERROR.getMessage());
    }
}