ALTER TABLE "users" ADD CONSTRAINT unique_phone UNIQUE ("phone");
ALTER TABLE "users" ADD CONSTRAINT unique_email UNIQUE ("email");

CREATE INDEX IF NOT EXISTS idx_users_phone ON "users" ("phone");
CREATE INDEX IF NOT EXISTS idx_users_email ON "users" ("email");