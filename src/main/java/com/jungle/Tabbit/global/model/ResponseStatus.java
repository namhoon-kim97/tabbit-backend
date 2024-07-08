package com.jungle.Tabbit.global.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseStatus {

    // Success Status
    SUCCESS_OK("요청이 성공적으로 처리되었습니다.", HttpStatus.OK),
    SUCCESS_LOGIN("로그인이 성공적으로 처리되었습니다.", HttpStatus.OK),
    SUCCESS_UPDATE("수정이 성공적으로 처리되었습니다", HttpStatus.OK),
    SUCCESS_DELETE("삭제가 성공적으로 처리되었습니다", HttpStatus.OK),
    SUCCESS_CREATE("요청이 성공적으로 처리되어 새로운 리소스가 생성되었습니다.", HttpStatus.CREATED),

    // Failed Status
    FAIL_BAD_REQUEST("클라이언트의 요청이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    FAIL_UNAUTHORIZED("클라이언트가 인증되지 않았습니다.", HttpStatus.UNAUTHORIZED),
    FAIL_FORBIDDEN("클라이언트가 요청한 리소스에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FAIL_NOT_FOUND("클라이언트가 요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_METHOD_NOT_ALLOWED("클라이언트가 요청한 HTTP 메소드가 허용되지 않았습니다.", HttpStatus.METHOD_NOT_ALLOWED),

    // Valid Failed Status
    FAIL_INVALID_PARAMETER("파라미터 값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // Member Failed Status
    FAIL_MEMBER_DUPLICATED("클라이언트가 요청한 회원정보가 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    FAIL_MEMBER_NOT_FOUND("클라이언트가 요청한 소유자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_MEMBER_ROLE_NOT_FOUND("클라이언트가 요청한 소유자의 권한을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_MEMBER_EMAIL_DUPLICATED("클라이언트가 요청한 이메일이 중복되었습니다.", HttpStatus.BAD_REQUEST),
    FAIL_MEMBER_USERNAME_DUPLICATED("클라이언트가 요청한 닉네임이 중복되었습니다.", HttpStatus.BAD_REQUEST),
    FAIL_MEMBER_PASSWORD_NOT_MATCHED("클라이언트가 입력한 비밀번호가 소유자의 비밀번호와 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    FAIL_MEMBER_USERNAME_INVALID("최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 이루어져있지 않습니다.", HttpStatus.BAD_REQUEST),
    FAIL_MEMBER_PASSWORD_INVALID("최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 이루어져있지 않습니다.", HttpStatus.BAD_REQUEST),
    FAIL_MEMBER_ROLE_INVALID("클라이언트의 권한이 없습니다.", HttpStatus.BAD_REQUEST),
    FAIL_PASSWORD_NOT_MATCH("현재 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    // Post
    FAIL_BOARD_NOT_FOUND("클라이언트가 요청한 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_BOARD_PASSWORD_NOT_MATCHED("클라이언트가 입력한 비밀번호가 게시글의 비밀번호와 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    // Reply
    FAIL_REPLY_NOT_FOUND("클라이언트가 요청한 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_REPLY_PASSWORD_NOT_MATCHED("클라이언트가 입력한 비밀번호가 댓글의 비밀번호와 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    // Login Failed Status
    FAIL_LOGIN_NOT_SUCCESS("로그인이 되지 않았습니다. 재시도 해주세요.", HttpStatus.BAD_REQUEST),

    // Token Failed Status
    FAIL_TOKEN_NOT_FOUND("클라이언트가 요청한 토큰 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    FAIL_REFRESHTOKEN_NOT_FOUND("클라이언트가 요청한 RefreshToken을 찾을 수 없습니다.(만료)", HttpStatus.NOT_FOUND),

    // Category
    FAIL_CATEGORY_NOT_FOUND("클라이언트가 요청한 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Address
    FAIL_ADDRESS_NOT_SUCCESS("클라이언트가 요청한 주소가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // Nfc
    FAIL_NFC_NOT_FOUND("등록된 NFC ID가 없습니다.", HttpStatus.NOT_FOUND),

    // Waiting
    FAIL_MEMBER_WAITING_DUPLICATED("이미 이 레스토랑의 대기자 명단에 등록되어 있습니다.", HttpStatus.BAD_REQUEST),
    FAIL_GET_CURRENT_WAIT_POSITION("해당 대기순번을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_NOT_OWNER("해당 기능은 레스토랑의 점주만 접근할 수 있습니다.", HttpStatus.BAD_REQUEST),

    // Restaurant
    FAIL_RESTAURANT_NOT_FOUND("클라이언트가 요청한 맛집을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_RESTAURANT_DETAIL_NOT_FOUND("클라이언트가 요청한 맛집 상세정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Notification
    FAIL_NOTIFICATION_NOT_FOUND("클라이언트가 요청한 알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Badge
    FAIL_BADGE_NOT_FOUND("해당 칭호를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_BADGE_NOT_EARNED("획득하지 않은 칭호입니다.", HttpStatus.BAD_REQUEST),

    // File
    FAIL_FILE_SIZE("5MB 이하 파일만 업로드 할 수 있습니다.", HttpStatus.PAYLOAD_TOO_LARGE),
    FAIL_FILE_MIME("이미지 파일만 업로드할 수 있습니다.", HttpStatus.BAD_REQUEST),
    FAIL_FILE_UPLOAD("파일을 저장하는 도중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_FILE_LOAD("파일을 불러오는 도중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_FILE_NOT_FOUND("해당 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_FILE_PATH("해당 파일 경로가 비어 있습니다.", HttpStatus.BAD_REQUEST),
    FAIL_FILE_DELETE("파일을 삭제하는 도중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // Guestbook
    FAIL_GUESTBOOK_NOT_FOUND("해당 방명록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Menu
    FAIL_MENU_NOT_FOUND("해당 메뉴를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_CATEGORY_DELETE("해당 카테고리 메뉴가 존재합니다. 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),
    FAIL_CATEGORY_CREATE("해당 카테고리가 존재합니다.", HttpStatus.BAD_REQUEST),

    // Order
    FAIL_ORDER_NOT_FOUND("주문 항목을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_ORDER_MEMBER_EQUAL("주문한 유저만이 수정 가능합니다.", HttpStatus.BAD_REQUEST),

    // delete
    FAIL_MEMBER_DELETE_WAITING("웨이팅을 취소해야 탈퇴가 가능합니다.", HttpStatus.BAD_REQUEST);

    private String message;

    private HttpStatus statusCode;
}
