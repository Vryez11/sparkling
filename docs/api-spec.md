# API 목록표

## 설계 기준

`API를 나눈 기준`: API가 어떤 **자원**을 이용하는지를 기준으로 (테이블 기준) 기능 API를 나눴다.

`인증 여부 판단 기준`: 서버가 요청자를 **식별**해야만 올바른 응답을 만들 수 있는가로 나눴다.

## 사용자 (users)

| 메서드  | URI            | 설명 | 인증 |
|------|----------------|---|---|
| POST | `/users/login` | 로그인 | 공개 |
| POST | `/users`       | 회원가입 | 공개 |
| GET  | `/users/me`    | 내 정보 조회 | 인증 |

## 게시글 (posts)

| 메서드 | URI | 설명 | 인증 |
|---|---|---|----|
| GET | `/posts` | 게시글 조회 | 공개 |
| POST | `/posts` | 게시글 등록 | 인증 |
| GET | `/posts/{postid}` | 상세 게시글 조회 | 공개 |
| PATCH | `/posts/{postid}` | 게시글 수정 | 인증 |
| DELETE | `/posts/{postid}` | 게시글 삭제 | 인증  |
| GET | `/posts?author=me` | 내 게시글 조회 | 인증 |

## 댓글 (comments)

| 메서드 | URI | 설명 | 인증 |
|---|---|---|---|
| GET | `/comments/{postid}` | 댓글 조회 | 공개 |
| POST | `/comments/{postid}` | 댓글 작성 | 인증 |
| DELETE | `/comments/{postid}/{commentid}` | 댓글 삭제 | 인증 |
