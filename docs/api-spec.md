# API 목록표

## 설계 기준

`API를 나눈 기준`: API가 어떤 **자원**을 이용하는지를 기준으로 (테이블 기준) 기능 API를 나눴다.

`인증 여부 판단 기준`: 서버가 요청자를 **식별**해야만 올바른 응답을 만들 수 있는가로 나눴다.

`인가 여부 판단 기준`: 로그인만으로는 부족하고, 자원의 **작성자 본인**인지까지 확인해야 하는 API는 "인증(본인)"으로 표기했다.

`구현 컬럼`: 실제 서버에 구현된 API는 ✅, 설계만 된 API는 미구현으로 표기한다.

## 공통 규칙

### 인증 방식

로그인 성공 시 응답으로 액세스 토큰(30분)과 리프레시 토큰(14일)을 발급한다. 인증이 필요한 API는 요청 헤더에 액세스 토큰을 담아 보낸다.

```
Authorization: Bearer {accessToken}
```

### 응답 형식

성공 응답은 별도 래핑 없이 **데이터 객체를 직접 반환**하고, 성공/실패는 HTTP 상태 코드로 구분한다.

실패 응답의 형식은 실패 원인에 따라 다르다.

- 인증 실패(토큰 없음/만료/위조): 401 + `WWW-Authenticate` 헤더에 실패 사유 (Spring Security resource server 표준 응답)
- 요청 검증 실패(`@Valid`): 400 + Spring 기본 에러 본문
- 비즈니스 오류(게시글 없음, 작성자 아님 등): **현재 500** — 전역 예외 핸들러(`@RestControllerAdvice`)가 아직 없다. 핸들러 도입 시 아래 상태 코드 표대로 404/403 등으로 매핑하고 에러 본문 형식(`code`/`message`)을 표준화할 예정이다.

### 상태 코드

| 코드 | 의미 | 구현 |
|---|---|---|
| 200 OK | 조회/수정/삭제 성공 | ✅ |
| 201 Created | 리소스 생성 성공 (회원가입, 게시글 등록, 댓글 작성) | ✅ |
| 400 Bad Request | 요청 형식 오류 (필수 값 누락 등) | ✅ |
| 401 Unauthorized | 인증 실패 (토큰 없음/만료, 로그인 실패) | ✅ (토큰 검증에 한함) |
| 403 Forbidden | 인가 실패 (작성자 본인이 아님) | 미구현 (현재 500) |
| 404 Not Found | 자원 없음 (존재하지 않는 게시글/댓글) | 미구현 (현재 500) |

## 사용자 (users)

| 메서드  | URI           | 설명 | 인증 | 구현 |
|------|---------------|---|---|---|
| POST | `/auth/login` | 로그인 | 공개 | ✅ |
| POST | `/auth/refresh` | 액세스 토큰 재발급 | 공개 | ✅ |
| POST | `/users`      | 회원가입 | 공개 | ✅ |
| GET  | `/users/me`   | 내 정보 조회 | 인증 | ✅ |
| GET  | `/users/{userId}` | 사용자 프로필 조회 | 공개 | 미구현 |
| GET  | `/users?keyword=닉네임` | 사용자 검색 | 공개 | 미구현 |

## 게시글 (posts)

| 메서드 | URI | 설명 | 인증 | 구현 |
|---|---|---|----|---|
| GET | `/posts` | 게시글 목록 조회 | 공개 | ✅ |
| POST | `/posts` | 게시글 등록 | 인증 | ✅ |
| GET | `/posts/{postId}` | 상세 게시글 조회 | 공개 | ✅ |
| PATCH | `/posts/{postId}` | 게시글 수정 | 인증(본인) | ✅ |
| DELETE | `/posts/{postId}` | 게시글 삭제 | 인증(본인) | ✅ |
| POST | `/posts/{postId}/likes` | 게시글 좋아요 등록 | 인증 | 미구현 |
| DELETE | `/posts/{postId}/likes` | 게시글 좋아요 취소 | 인증 | 미구현 |

> `GET /posts?author=me`는 별도 API가 아니라 게시글 목록 조회의 필터 옵션이다. `author=me`를 사용할 때만 인증이 필요하다.
>
> 검색도 게시글 목록 조회의 쿼리 파라미터로 처리한다: `GET /posts?category={title|content|hashtag}&keyword=검색어`. 해시태그 검색은 제목/내용 검색과 분리된 별도 카테고리다. 사용자 검색은 게시글 검색에 섞지 않고 별도 API(`GET /users?keyword=`)로 분리한다.

## 댓글 (comments) — 전체 미구현

댓글은 특정 게시글에 속하므로 조회/작성은 게시글 하위 경로로 표현한다. 삭제는 `commentId`가 전역 유일하므로 댓글 ID만으로 지정한다.

| 메서드 | URI | 설명 | 인증 | 구현 |
|---|---|---|---|---|
| GET | `/posts/{postId}/comments` | 댓글 조회 | 공개 | 미구현 |
| POST | `/posts/{postId}/comments` | 댓글 작성 | 인증 | 미구현 |
| DELETE | `/comments/{commentId}` | 댓글 삭제 | 인증(본인) | 미구현 |
| POST | `/comments/{commentId}/likes` | 댓글 좋아요 등록 | 인증 | 미구현 |
| DELETE | `/comments/{commentId}/likes` | 댓글 좋아요 취소 | 인증 | 미구현 |

## 채팅 (chat) — 미구현

1:1 채팅은 REST API가 아니라 **WebSocket**으로 처리한다.

- 온라인 상태는 별도 폴링 없이 WebSocket 연결 여부로 판단한다. 채팅 구조상 WebSocket이 필수이고, 온라인 상태의 정확성이 중요하기 때문이다.
- 채팅 상대는 게시글/댓글 응답의 `authorId`로 특정한다.
- 오프라인 상대와는 채팅방 연결이 되지 않는다. 연결 요청 후 30초 안에 상대방이 수락하지 않으면 타임아웃으로 연결 시도를 완전히 종료한다.
- 채팅 세션은 유저 단위가 아니라 **디바이스 단위**로 관리한다. 채팅 요청 시 상대방의 연결된 모든 디바이스에 요청 메시지를 푸시하고, 최초로 연결한 디바이스와 채팅을 연결한다.
- 채팅 **메시지 내용**은 서버에 저장하지 않는다 (온라인 기반 실시간 중계만). 단, 오프라인 상대에게 전달할 **대기 중인 채팅 요청 상태**는 상대가 온라인이 될 때까지 저장한다.

상세 흐름은 `user-scenarios.md`의 시나리오 5 참고.

# API 요청/응답 JSON

구현된 API의 응답 JSON은 실제 서버 응답과 일치한다. 미구현 API는 설계안이다.

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

이후 인증이 필요한 요청은 `accessToken`을 `Authorization` 헤더에 담아 보낸다. `refreshToken`은 액세스 토큰 만료(30분) 후 재발급에 사용한다.

```json
{
  "accessToken": "발급된 액세스 토큰",
  "refreshToken": "발급된 리프레시 토큰",
  "userId": 1,
  "email": "이메일"
}
```

### `POST /auth/refresh` — 액세스 토큰 재발급

리프레시 토큰으로 새 토큰 쌍을 발급받는다. 리프레시 토큰도 매번 새로 발급되며(회전), 이전 리프레시 토큰은 즉시 무효화된다. 이미 회전된 토큰을 다시 제시하면 탈취 의심으로 간주해 저장된 리프레시 토큰 자체를 폐기하므로, 재로그인해야 한다.

### 요청 JSON

```json
{
  "refreshToken": "로그인/재발급 시 받은 리프레시 토큰"
}
```

### 응답 JSON — 200 OK

```json
{
  "accessToken": "새 액세스 토큰",
  "refreshToken": "새 리프레시 토큰"
}
```

실패(서명 불일치/만료/회전된 토큰 재사용/액세스 토큰 제시)는 무효한 리프레시 토큰으로 거절한다. (현재는 전역 핸들러 부재로 500, 핸들러 도입 시 401로 매핑 예정)

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
  "id": 1,
  "nickname": "닉네임",
  "email": "이메일"
}
```

## 3. 내 정보 조회

### `GET /users/me`

### 응답 JSON — 200 OK

```json
{
  "id": 1,
  "nickname": "닉네임",
  "email": "이메일",
  "createdAt": "2026-07-10T21:36:37"
}
```

## 4. 사용자 프로필 조회 — 미구현

### `GET /users/{userId}`

게시글/댓글 응답의 `authorId`로 작성자 프로필을 조회한다. 1:1 채팅 진입 시 상대방을 특정할 때도 사용한다.

### 응답 JSON — 200 OK

```json
{
  "userId": 2,
  "nickname": "닉네임"
}
```

## 5. 사용자 검색 — 미구현

### `GET /users?keyword=닉네임`

닉네임으로 사용자 프로필을 검색한다. 검색 결과의 프로필에서 1:1 채팅으로 진입할 수 있다. 결과가 없으면 오류가 아니라 빈 목록(`"users": []`)을 200 OK로 반환한다.

### 응답 JSON — 200 OK

```json
{
  "users": [
    {
      "userId": 2,
      "nickname": "닉네임"
    }
  ]
}
```

## 6. 게시글 목록 조회

### `GET /posts`

- 작성일 **내림차순(최신순)** 으로 정렬한다.
- 삭제된 게시글(soft delete)은 목록에서 제외한다.
- 결과가 없는 경우 오류가 아니라 빈 목록(`"posts": []`)을 200 OK로 반환한다. 클라이언트는 alert 대신 빈 게시글 목록을 보여준다.

쿼리 파라미터 (**미구현**, 설계안):

| 파라미터 | 설명 |
|---|---|
| `author=me` | 내 게시글만 조회 (인증 필요) |
| `category` | 검색 카테고리: `title`(제목), `content`(내용), `hashtag`(해시태그) |
| `keyword` | 검색어. `category`와 함께 사용한다 |

### 응답 JSON — 200 OK

```json
{
  "posts": [
    {
      "postId": 1,
      "title": "게시글 제목",
      "nickname": "작성자 닉네임",
      "createdAt": "2026-07-10T21:37:05"
    }
  ]
}
```

> 좋아요·해시태그 기능 구현 시 `authorId`, `likeCount`, `liked` 필드가 추가될 예정이다. `liked`는 요청자가 그 게시글에 좋아요를 눌렀는지 여부로, 비로그인 조회 시 항상 `false`다. (상세 조회 응답도 동일)

## 7. 게시글 등록

### `POST /posts`

### 요청 JSON

```json
{
  "title": "게시글 제목 (필수, 최대 100자)",
  "content": "게시글 내용 (필수)"
}
```

> **해시태그 파싱은 미구현.** 설계안: 해시태그는 별도 필드로 보내지 않고 내용 안에 `#` 으로 작성하며, 서버가 저장 시 내용에서 해시태그를 파싱해 게시글과 매핑한다. (클라이언트 파싱 결과는 신뢰할 수 없고, 해시태그를 실제로 사용하는 쪽이 서버이기 때문)

### 응답 JSON — 201 Created

생성된 게시글의 ID를 반환해 클라이언트가 바로 상세 조회로 이동할 수 있게 한다. `Location: /posts/{postId}` 헤더를 함께 내려준다.

```json
{
  "postId": 1
}
```

## 8. 상세 게시글 조회

### `GET /posts/{postId}`

삭제된 게시글은 없는 것으로 처리한다.

### 응답 JSON — 200 OK

```json
{
  "postId": 1,
  "title": "게시글 제목",
  "content": "게시글 내용",
  "nickname": "작성자 닉네임",
  "createdAt": "2026-07-10T21:37:05"
}
```

> 좋아요·해시태그 기능 구현 시 `hashtags`, `authorId`, `likeCount`, `liked` 필드가 추가될 예정이다.

## 9. 게시글 수정

### `PATCH /posts/{postId}`

수정할 필드만 담아 보낸다(부분 수정). `null`이거나 공백뿐인 필드는 무시된다. `title`은 최대 100자.

### 요청 JSON

```json
{
  "title": "수정된 제목",
  "content": "수정된 내용"
}
```

### 응답 JSON — 200 OK

수정이 반영된 게시글 상세를 반환한다(상세 조회와 동일한 형식).

```json
{
  "postId": 1,
  "title": "수정된 제목",
  "content": "수정된 내용",
  "nickname": "작성자 닉네임",
  "createdAt": "2026-07-10T21:37:05"
}
```

## 10. 게시글 삭제

### `DELETE /posts/{postId}`

실제 행 삭제가 아니라 `deleted_at`을 기록하는 soft delete다. 삭제된 게시글은 목록·상세·수정에서 모두 제외된다.

### 응답 JSON — 200 OK

```json
{
  "postId": 1,
  "deletedAt": "2026-07-10T22:12:43"
}
```

## 11. 댓글 조회 — 미구현

### `GET /posts/{postId}/comments`

### 응답 JSON — 200 OK

`commentId`를 포함해 클라이언트가 삭제·좋아요·대댓글 요청 시 사용할 수 있게 한다. 대댓글은 부모 댓글의 `replies` 배열에 중첩해 반환한다(depth 1 고정이므로 중첩은 1단까지). `liked`는 요청자가 그 댓글에 좋아요를 눌렀는지 여부로, 비로그인 조회 시 항상 `false`다.

```json
{
  "comments": [
    {
      "commentId": 1,
      "content": "댓글1",
      "authorId": 2,
      "author": "작성자1",
      "likeCount": 3,
      "liked": true,
      "createdAt": "2026-07-10T22:10:01",
      "replies": [
        {
          "commentId": 5,
          "content": "대댓글1",
          "authorId": 4,
          "author": "작성자3",
          "likeCount": 0,
          "liked": false,
          "createdAt": "2026-07-10T22:11:02"
        }
      ]
    },
    {
      "commentId": 2,
      "content": "댓글2",
      "authorId": 3,
      "author": "작성자2",
      "likeCount": 0,
      "liked": false,
      "createdAt": "2026-07-10T22:11:30",
      "replies": []
    }
  ]
}
```

## 12. 댓글 작성 — 미구현

### `POST /posts/{postId}/comments`

### 요청 JSON

대댓글은 `parentCommentId`에 부모 댓글 ID를 담아 보낸다. 필드를 생략하면 일반 댓글이다. 부모가 이미 대댓글인 경우(depth 초과)는 400 Bad Request로 거절한다.

```json
{
  "content": "댓글 내용",
  "parentCommentId": 1
}
```

### 응답 JSON — 201 Created

```json
{
  "commentId": 1
}
```

## 13. 댓글 삭제 — 미구현

### `DELETE /comments/{commentId}`

### 응답 JSON — 200 OK

응답 본문 형식은 게시글 삭제와 같은 방식(`commentId` + `deletedAt`)으로 맞춘다.

```json
{
  "commentId": 1,
  "deletedAt": "2026-07-10T22:15:00"
}
```

## 14. 좋아요 등록 / 취소 (게시글 · 댓글) — 미구현

### `POST /posts/{postId}/likes` · `DELETE /posts/{postId}/likes`
### `POST /comments/{commentId}/likes` · `DELETE /comments/{commentId}/likes`

게시글과 댓글 모두 같은 방식이다. 등록과 취소 모두 **멱등**이다 — 이미 좋아요를 누른 상태에서 다시 등록해도 개수는 변하지 않고, 누르지 않은 상태에서 취소해도 오류가 아니다. 요청 본문은 없다.

### 응답 JSON — 200 OK

등록(`POST`)과 취소(`DELETE`) 모두 반영된 최신 상태를 반환한다. 댓글 좋아요의 예시이며, 게시글 좋아요는 `commentId` 대신 `postId`를 반환한다.

```json
{
  "commentId": 1,
  "likeCount": 4,
  "liked": true
}
```
