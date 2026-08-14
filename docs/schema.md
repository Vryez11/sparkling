# DB Schema

## users

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 사용자 ID |
| nickname | varchar(100) | UNIQUE | 닉네임 |
| email | varchar(100) | UNIQUE | 이메일 |
| password | varchar(255) | | 비밀번호 (해시 저장) |
| created_at | datetime | | 생성일시 |
| updated_at | datetime | | 수정일시 |

## refresh_tokens

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 리프레시 토큰 ID |
| user_id | bigint | UNIQUE, NOT NULL | 사용자 ID (유저당 1행 — 새 로그인/재발급 시 교체) |
| token | varchar(512) | NOT NULL | 리프레시 토큰 (JWT) |
| created_at | datetime | | 생성일시 |
| updated_at | datetime | | 수정일시 (마지막 회전 시각) |

## posts

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 게시글 ID |
| title | varchar(100) | | 제목 |
| content | text | | 본문 |
| user_id | bigint | FK(users.id), NOT NULL | 작성자 |
| created_at | datetime | | 생성일시 |
| updated_at | datetime | | 수정일시 |

## comments

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 댓글 ID |
| content | text | | 내용 |
| user_id | bigint | FK(users.id) | 작성자 |
| post_id | bigint | FK(posts.id) | 대상 게시글 |
| parent_comment_id | bigint | FK(comments.id), NULL 허용 | 부모 댓글 (대댓글용, 최상위 댓글은 NULL) |
| created_at | datetime | | 생성일시 |

## post_likes

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 좋아요 ID |
| user_id | bigint | FK(users.id) | 누른 사용자 |
| post_id | bigint | FK(posts.id) | 대상 게시글 |
| created_at | datetime | | 생성일시 |

- UNIQUE(`user_id`, `post_id`) — 한 사용자가 같은 게시글에 좋아요 중복 방지

## comment_likes

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 좋아요 ID |
| user_id | bigint | FK(users.id) | 누른 사용자 |
| comment_id | bigint | FK(comments.id) | 대상 댓글 |
| created_at | datetime | | 생성일시 |

- UNIQUE(`user_id`, `comment_id`) — 한 사용자가 같은 댓글에 좋아요 중복 방지

## hashtags

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 해시태그 ID |
| name | varchar(100) | UNIQUE | 태그명 |
| created_at | datetime | | 생성일시 |

## post_hashtag

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 매핑 ID |
| post_id | bigint | FK(posts.id) | 게시글 |
| hashtag_id | bigint | FK(hashtags.id) | 해시태그 |
| created_at | datetime | | 생성일시 |

- UNIQUE(`post_id`, `hashtag_id`) — 같은 게시글에 같은 해시태그 중복 연결 방지

## chat_requests

| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | bigint | PK | 채팅 요청 ID |
| request_user_id | bigint | FK(users.id), NOT NULL | 요청 보낸 사용자 |
| response_user_id | bigint | FK(users.id), NOT NULL | 요청 받은 사용자 |
| status | varchar(100) | | 요청 상태 (예: PENDING / ACCEPTED / REJECTED) |
| created_at | datetime | | 생성일시 |
| updated_at | datetime | | 수정일시 |