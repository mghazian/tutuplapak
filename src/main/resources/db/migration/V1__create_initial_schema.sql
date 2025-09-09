CREATE TABLE IF NOT EXISTS "images" (
  "id" bigserial PRIMARY KEY,
  "owner_id" bigint,
  "uri" varchar not null,
  "thumbnail_uri" varchar not null,
  "created_at" timestamp default current_timestamp,
  "updated_at" timestamp default current_timestamp
);
CREATE INDEX IF NOT EXISTS "idx_images_owner_id" ON "images" ("owner_id");

CREATE TABLE IF NOT EXISTS "users" (
  "id" bigserial PRIMARY KEY,
  "phone" varchar,
  "email" varchar,
  "password" varchar not null,
  "image_id" bigint,
  "bank_account_name" varchar not null default '',
  "bank_account_holder" varchar not null default '',
  "bank_account_number" varchar not null default '',
  "created_at" timestamp default current_timestamp,
  "updated_at" timestamp default current_timestamp,

  FOREIGN KEY ("image_id") REFERENCES "images" ("id") ON UPDATE CASCADE ON DELETE SET NULL
);
CREATE INDEX IF NOT EXISTS "idx_users_image_id" ON "users" ("image_id");

ALTER TABLE "images" ADD FOREIGN KEY ("owner_id") REFERENCES "users" ("id") ON UPDATE CASCADE ON DELETE SET null;

CREATE TABLE IF NOT EXISTS "products" (
  "id" bigserial PRIMARY KEY,
  "owner_id" bigint,
  "category" varchar not null,
  "qty" int not null,
  "price" int not null,
  "sku" varchar not null,
  "image_id" bigint,
  "created_at" timestamp default current_timestamp,
  "updated_at" timestamp default current_timestamp,

  FOREIGN KEY ("owner_id") REFERENCES "users" ("id") ON UPDATE CASCADE ON DELETE SET NULL,
  FOREIGN KEY ("image_id") REFERENCES "images" ("id") ON UPDATE CASCADE ON DELETE SET NULL
);
CREATE INDEX IF NOT EXISTS "idx_products_image_id" ON "products" ("image_id");

CREATE TABLE IF NOT EXISTS "purchases" (
  "id" bigserial PRIMARY KEY,
  "sender_name" varchar,
  "sender_contact_type" varchar,
  "sender_contact_detail" varchar,
  "created_at" timestamp default current_timestamp,
  "updated_at" timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS "purchase_items" (
  "id" bigserial PRIMARY KEY,
  "purchase_id" bigint,
  "product_id" bigint,
  "name" varchar,
  "category" varchar,
  "qty" int,
  "price" int,
  "sku" varchar,
  "image_id" bigint,
  "image_uri" varchar,
  "image_thumbnail_uri" varchar,
  "created_at" timestamp default current_timestamp,
  "updated_at" timestamp default current_timestamp
);
CREATE INDEX IF NOT EXISTS "idx_purchase_items_purchase_id" ON "purchase_items" ("purchase_id");