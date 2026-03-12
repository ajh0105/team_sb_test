-- 기존 members 테이블이 있다면 삭제 (컬럼 구조가 맞지 않을 수 있으므로)
DROP TABLE IF EXISTS members CASCADE;

-- members 테이블 생성
CREATE TABLE members (
    id         BIGSERIAL       PRIMARY KEY,
    name       VARCHAR(50)     NOT NULL,
    email      VARCHAR(255)    NOT NULL UNIQUE,
    password   VARCHAR(255)    NOT NULL,
    phone      VARCHAR(255)    NOT NULL,
    role       VARCHAR(20)     NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- 테이블 구조 확인
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_name = 'members'
ORDER BY ordinal_position;

-- 데이터 없는지 확인
SELECT * FROM members;

UPDATE members SET role = 'ADMIN' WHERE email = 'admin@busan.go.kr';

