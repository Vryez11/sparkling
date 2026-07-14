# API 목록표

## 설계 기준

`API를 나눈 기준`: API가 어떤 **자원**을 이용하는지를 기준으로 (테이블 기준) 기능 API를 나눴다.

`인증 여부 판단 기준`: 서버가 요청자를 **식별**해야만 올바른 응답을 만들 수 있는가로 나눴다.

`인가 여부 판단 기준`: 로그인만으로는 부족하고, 자원의 **작성자 본인**인지까지 확인해야 하는 API는 "인증(본인)"으로 표기했다.

## 공통 규칙

### 인증 방식

로그인 성공 시 응답으로 액세스 토큰을 발급한다. 인증이 필요한 API는 요청 헤더에 토큰을 담아 보낸다.

```
Authorization: Bearer {accessToken}
```

### 공통 응답 형식

모든 API는 아래 구조를 따른다. `date`는 서버가 응답을 만든 시각이다.

성공:

```json
{
  "success": true,
  "data": {},
  "date": "2026-07-10T21:36:37"
}
```

실패:

```json
{
  "success": false,
  "code": "에러 코드 (예: INVALID_PASSWORD)",
  "message": "사람이 읽을 수 있는 이유",
  "date": "2026-07-10T21:36:37"
}
```

- `success`는 JSON boolean(`true`/`false`)이다. 문자열 `"true"`가 아니다.
- 요청 시각은 클라이언트가 보내지 않는다. 생성/수정 시각은 서버가 기록한다.

### 상태 코드

| 코드 | 의미 |
|---|---|
| 200 OK | 조회/수정/삭제 성공 |
| 201 Created | 리소스 생성 성공 (회원가입, 게시글 등록, 댓글 작성) |
| 400 Bad Request | 요청 형식 오류 (필수 값 누락 등) |
| 401 Unauthorized | 인증 실패 (토큰 없음/만료, 로그인 실패) |
| 403 Forbidden | 인가 실패 (작성자 본인이 아님) |
| 404 Not Found | 자원 없음 (존재하지 않는 게시글/댓글) |

## 사용자 (users)

| 메서드  | URI           | 설명 | 인증 |
|------|---------------|---|---|
| POST | `/auth/login` | 로그인 | 공개 |
| POST | `/users`      | 회원가입 | 공개 |
| GET  | `/users/me`   | 내 정보 조회 | 인증 |

## 게시글 (posts)

| 메서드 | URI | 설명 | 인증 |
|---|---|---|----|
| GET | `/posts` | 게시글 목록 조회 | 공개 |
| POST | `/posts` | 게시글 등록 | 인증 |
| GET | `/posts/{postId}` | 상세 게시글 조회 | 공개 |
| PATCH | `/posts/{postId}` | 게시글 수정 | 인증(본인) |
| DELETE | `/posts/{postId}` | 게시글 삭제 | 인증(본인) |

> `GET /posts?author=me`는 별도 API가 아니라 게시글 목록 조회의 필터 옵션이다. `author=me`를 사용할 때만 인증이 필요하다.

## 댓글 (comments)

댓글은 특정 게시글에 속하므로 조회/작성은 게시글 하위 경로로 표현한다. 삭제는 `commentId`가 전역 유일하므로 댓글 ID만으로 지정한다.

| 메서드 | URI | 설명 | 인증 |
|---|---|---|---|
| GET | `/posts/{postId}/comments` | 댓글 조회 | 공개 |
| POST | `/posts/{postId}/comments` | 댓글 작성 | 인증 |
| DELETE | `/comments/{commentId}` | 댓글 삭제 | 인증(본인) |

# API 요청/응답 JSON

실패 응답은 모든 API가 [공통 응답 형식](#공통-응답-형식)의 실패 구조를 따르므로 생략한다.

## 1. 로그인

### `POST /auth/login`

### 요청 JSON

```json
{
  "email": "이메일",
  "password": "비밀번호"
}
```

### 응답 JSON — 200 OK

이후 인증이 필요한 요청은 `accessToken`을 `Authorization` 헤더에 담아 보낸다.

```json
{
  "success": true,
  "data": {
    "accessToken": "발급된 액세스 토큰"
  },
  "date": "2026-07-10T21:36:37"
}
```

## 2. 회원가입

### `POST /users`

### 요청 JSON

```json
{
  "email": "이메일",
  "password": "비밀번호",
  "nickname": "닉네임"
}
```

### 응답 JSON — 201 Created

```json
{
  "success": true,
  "data": {
    "userId": 1
  },
  "date": "2026-07-10T21:36:37"
}
```

## 3. 내 정보 조회

### `GET /users/me`

### 응답 JSON — 200 OK

```json
{
  "success": true,
  "data": {
    "userId": 1,
    "email": "이메일",
    "nickname": "닉네임"
  },
  "date": "2026-07-10T21:36:37"
}
```

## 4. 게시글 목록 조회

### `GET /posts`

`author=me` 쿼리 파라미터를 붙이면 내 게시글만 조회한다(인증 필요).

### 응답 JSON — 200 OK

```json
{
  "success": true,
  "data": {
    "posts": [
      {
        "postId": 1,
        "title": "게시글 제목",
        "author": "작성자 닉네임",
        "createdAt": "2026-07-10T21:37:05"
      }
    ]
  },
  "date": "2026-07-10T22:12:43"
}
```

## 5. 게시글 등록

### `POST /posts`

### 요청 JSON

```json
{
  "title": "게시글 제목",
  "content": "게시글 내용",
  "hashtags": ["해시태그1", "해시태그2", "해시태그3"]
}
```

### 응답 JSON — 201 Created

생성된 게시글의 ID를 반환해 클라이언트가 바로 상세 조회로 이동할 수 있게 한다.

```json
{
  "success": true,
  "data": {
    "postId": 1
  },
  "date": "2026-07-10T21:36:37"
}
```

## 6. 상세 게시글 조회

### `GET /posts/{postId}`

### 응답 JSON — 200 OK

```json
{
  "success": true,
  "data": {
    "postId": 1,
    "title": "게시글 제목",
    "content": "게시글 내용",
    "hashtags": ["해시태그1", "해시태그2"],
    "author": "작성자 닉네임",
    "createdAt": "2026-07-10T21:37:05"
  },
  "date": "2026-07-10T22:12:43"
}
```

## 7. 게시글 수정

### `PATCH /posts/{postId}`

수정할 필드만 담아 보낸다.

### 요청 JSON

```json
{
  "title": "수정된 제목",
  "content": "수정된 내용",
  "hashtags": ["수정된 해시태그"]
}
```

### 응답 JSON — 200 OK

```json
{
  "success": true,
  "data": {
    "postId": 1
  },
  "date": "2026-07-10T21:36:37"
}
```

## 8. 게시글 삭제

### `DELETE /posts/{postId}`

### 응답 JSON — 200 OK

```json
{
  "success": true,
  "data": null,
  "date": "2026-07-10T21:36:37"
}
```

## 9. 댓글 조회

### `GET /posts/{postId}/comments`

### 응답 JSON — 200 OK

`commentId`를 포함해 클라이언트가 삭제 요청 시 사용할 수 있게 한다.

```json
{
  "success": true,
  "data": {
    "comments": [
      {
        "commentId": 1,
        "content": "댓글1",
        "author": "작성자1",
        "createdAt": "2026-07-10T22:10:01"
      },
      {
        "commentId": 2,
        "content": "댓글2",
        "author": "작성자2",
        "createdAt": "2026-07-10T22:11:30"
      }
    ]
  },
  "date": "2026-07-10T22:12:43"
}
```

## 10. 댓글 작성

### `POST /posts/{postId}/comments`

### 요청 JSON

```json
{
  "content": "댓글 내용"
}
```

### 응답 JSON — 201 Created

```json
{
  "success": true,
  "data": {
    "commentId": 1
  },
  "date": "2026-07-10T22:10:01"
}
```

## 11. 댓글 삭제

### `DELETE /comments/{commentId}`

### 응답 JSON — 200 OK

```json
{
  "success": true,
  "data": null,
  "date": "2026-07-10T22:15:00"
}
```
