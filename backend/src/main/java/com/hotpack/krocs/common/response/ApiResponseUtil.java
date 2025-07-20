package com.hotpack.krocs.common.response;

import com.hotpack.krocs.common.response.code.BaseCode;
import com.hotpack.krocs.common.response.code.resultCode.ErrorStatus;
import com.hotpack.krocs.common.response.code.resultCode.SuccessStatus;

/**
 * API 응답 생성을 위한 유틸리티 클래스
 */
public class ApiResponseUtil {

    /**
     * 성공 응답 생성 (데이터 포함)
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.onSuccess(data);
    }

    /**
     * 성공 응답 생성 (데이터 없음)
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.onSuccess();
    }

    /**
     * 성공 응답 생성 (커스텀 코드)
     */
    public static <T> ApiResponse<T> success(BaseCode code, T data) {
        return ApiResponse.of(code, data);
    }

    /**
     * 실패 응답 생성 (기본 에러)
     */
    public static <T> ApiResponse<T> failure(ErrorStatus errorStatus) {
        return ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage());
    }

    /**
     * 실패 응답 생성 (커스텀 메시지)
     */
    public static <T> ApiResponse<T> failure(ErrorStatus errorStatus, String customMessage) {
        return ApiResponse.onFailure(errorStatus.getCode(), customMessage);
    }

    /**
     * 실패 응답 생성 (데이터 포함)
     */
    public static <T> ApiResponse<T> failure(ErrorStatus errorStatus, T data) {
        return ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), data);
    }

    /**
     * 실패 응답 생성 (커스텀 코드와 메시지)
     */
    public static <T> ApiResponse<T> failure(String code, String message) {
        return ApiResponse.onFailure(code, message);
    }

    /**
     * 리소스 없음 응답
     */
    public static <T> ApiResponse<T> notFound() {
        return failure(ErrorStatus.RESOURCE_NOT_FOUND);
    }

    /**
     * 중복 리소스 응답
     */
    public static <T> ApiResponse<T> conflict() {
        return failure(ErrorStatus.DUPLICATE_RESOURCE);
    }

    /**
     * 검증 실패 응답
     */
    public static <T> ApiResponse<T> validationError() {
        return failure(ErrorStatus.VALIDATION_ERROR);
    }

    /**
     * 비즈니스 로직 오류 응답
     */
    public static <T> ApiResponse<T> businessError() {
        return failure(ErrorStatus.BUSINESS_LOGIC_ERROR);
    }
} 