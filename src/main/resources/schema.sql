-- Product.category is a free-form catalog code (product_categories.category_code),
-- not the legacy ProductCategory enum. Hibernate ddl-auto=update does not drop
-- leftover Postgres check constraints created when the column was an enum.
ALTER TABLE IF EXISTS products DROP CONSTRAINT IF EXISTS products_category_check;
