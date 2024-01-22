package com.example.instagram_diana.src.common.response;

import lombok.Getter;


/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    //custom******************************************************************** made by 현정
    POST_USERS_EMPTY_ID(false, 2018, "이메일 또는 전화번호를 입력해주세요."),
    POST_USERS_EXISTS_PHONE(false,2019,"중복된 전화번호입니다."),
    POST_USERS_EXISTS_NAME(false,2020,"이미 존재하는 유저 네임입니다."),

    // [PATCH] /users
    PATCH_USERS_NULL_NAME(false,2021,"유저 네임은 빈값이 될 수 없습니다."),

    // /app/follow/{userId}
    USER_ID_NOT_EXIST(false,2022,"유저아이디를 정확히 입력해주세요."),

    // /app/follow/{userId}
    USER_ID_FOLLOW_EXISTS(false,2023,"이미 구독한 유저입니다."),
    USER_ID_UNFOLLOW_EXISTS(false,2024,"구독하지않은 유저입니다."),
    USER_ID_CANNOT_FOLLOW(false,2024,"자기 자신은 구독할 수 없습니다"),

    // /app/users/{pageUserId}
    USER_ID_PROFILE_NOT_EXISTS(false,2024,"해당 요청 프로필 페이지는 없습니다."),

    // /app/users/{userId}/posts
    POST_FILE_EMPTY(false,2030,"파일을 입력해주세요."),




    // /app/like/{postId}
    POST_ID_NOT_EXISTS(false,2031,"게시물 아이디가 존재하지 않습니다."),
    POST_CANNOT_MYSELF(false,2032,"자기 자신은 좋아요 할 수 없습니다."),

    // [DELETE] /app/users/posts/{postId}
    POST_CANNOT_DELETE(false,2033,"게시물 삭제 권한이 없습니다."),

    // [PATCH] /app/users/{userId}/email
    PATCH_CAANOT_EXECUTE(false,2034,"수정 권한이 없습니다."),
    PATCH_USERS_INVALID_EMAIL(false,2035,"이메일 형식을 확인해주세요."),
    PATCH_USERS_INVALID_PHONE(false,2036,"전화번호 형식을 확인해주세요."),

    // [POST] /app/block/{userId}
    POST_CANNOT_SELF(false,2037,"자신에게 요청할 수 없습니다."),
    // [POST] /app/unblock/{userId}
    USER_ID_ALREADY_BLOCK(false,2038,"이미 차단한 유저입니다."),
    USER_ID_ALREADY_UNBLOCK(false,2039,"차단하지않은 유저입니다."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    // [POST] /app/post
    FILE_CANNOT_NULL(false,3015,"파일을 첨부해주세요."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
