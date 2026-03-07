-- ============================================================
-- 부산 Demo 프로젝트 - 전체 테이블 생성 SQL
-- PostgreSQL 기준 (pgAdmin Query Tool에서 실행)
-- ============================================================

-- ① 기존 테이블 초기화 (순서 주의: 외래키 참조 테이블 먼저 삭제)
DROP TABLE IF EXISTS boards CASCADE;
DROP TABLE IF EXISTS members CASCADE;

-- ② 회원 테이블
CREATE TABLE members (
    id         BIGSERIAL    PRIMARY KEY,
    name       VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    phone      VARCHAR(20)  NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ③ 게시판 테이블 (자유게시판 FREE / Q&A QNA / 관광불편신고 REPORT 통합)
CREATE TABLE boards (
    id          BIGSERIAL    PRIMARY KEY,
    board_type  VARCHAR(20)  NOT NULL,          -- FREE | QNA | REPORT
    title       VARCHAR(200) NOT NULL,
    content     TEXT         NOT NULL,
    member_id   BIGINT       NOT NULL REFERENCES members(id),
    views       INT          NOT NULL DEFAULT 0,
    qna_status  VARCHAR(20),                    -- QNA 전용: 답변대기 | 답변완료
    report_step VARCHAR(20),                    -- REPORT 전용: 접수 | 조사중 | 완료
    secret      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ④ 인덱스 (게시판 유형별 조회 성능)
CREATE INDEX idx_boards_type ON boards(board_type);
CREATE INDEX idx_boards_member ON boards(member_id);

-- ============================================================
-- 테스트 데이터 삽입
-- ============================================================

-- 관리자 계정 (비밀번호: admin1234 → BCrypt 암호화 필요, 아래는 예시 해시)
-- Spring Boot 실행 후 /signup 에서 직접 생성하거나, 아래 INSERT 사용
-- 아래 해시는 BCrypt("admin1234") 예시값입니다.
INSERT INTO members (name, email, password, phone, role, created_at)
VALUES (
    '관리자',
    'admin@busan.go.kr',
    '$2a$10$N.zmdr9zkzoGtM9jEZkT2.vjEMrHqB6LFgV3d.XJzMrW0jmBmJTrO',
    '051-120-0000',
    'ADMIN',
    NOW()
);
-- 주의: 위 BCrypt 해시는 예시입니다. 아래 '직접 가입' 방법을 권장합니다.
-- 1. Spring Boot 앱 실행
-- 2. /signup 페이지에서 admin@busan.go.kr 로 회원가입
-- 3. pgAdmin에서 아래 쿼리로 ADMIN 권한 부여:
--    UPDATE members SET role = 'ADMIN' WHERE email = 'admin@busan.go.kr';

-- 자유게시판 샘플 데이터 (member_id=1 은 위에 삽입한 관리자)
INSERT INTO boards (board_type, title, content, member_id, views, created_at)
VALUES
  ('FREE', '부산 역사 탐방 서포터즈 모집 공고', '부산 역사 탐방 서포터즈를 모집합니다. 많은 관심 부탁드립니다.', 1, 450, NOW() - INTERVAL '10 days'),
  ('FREE', '광안대교 야경 명당 추천해주세요!', '광안대교 야경을 찍을 수 있는 명당 포인트 추천 부탁드려요!', 1, 142, NOW() - INTERVAL '1 day');

-- Q&A 샘플 데이터
INSERT INTO boards (board_type, title, content, member_id, qna_status, created_at)
VALUES
  ('QNA', '해운대 공영주차장 위치 문의', '해운대 공영주차장이 어디에 있는지 알려주세요.', 1, '답변완료', NOW() - INTERVAL '5 days'),
  ('QNA', '박물관 정기 휴관일이 궁금합니다.', '부산시립박물관 정기 휴관일을 알려주세요.', 1, '답변대기', NOW() - INTERVAL '1 day');

-- 관광불편신고 샘플 데이터
INSERT INTO boards (board_type, title, content, member_id, report_step, secret, created_at)
VALUES
  ('REPORT', '해운대 인근 불법 주차 관련 신고합니다.', '해운대 해수욕장 근처 인도에 불법 주차된 차량이 많습니다. 단속 부탁드립니다.', 1, '접수', TRUE, NOW() - INTERVAL '1 day'),
  ('REPORT', '관광지 주변 바가지 요금 단속 요청', '자갈치 시장 근처 식당에서 관광객에게 과도한 요금을 받고 있습니다.', 1, '조사중', TRUE, NOW() - INTERVAL '1 day');

-- ============================================================
-- 확인 쿼리
-- ============================================================
SELECT * FROM members;
SELECT * FROM boards ORDER BY created_at DESC;

-- ============================================================
-- 관리자 권한 부여 (회원가입 후 실행)
-- ============================================================
-- UPDATE members SET role = 'ADMIN' WHERE email = 'admin@busan.go.kr';
