UPDATE gymmate_member
SET
  address = '서울특별시 송파구 올림픽로 300',
  x_field = 127.102502,
  y_field = 37.513068
WHERE
  x_field IS NULL OR y_field IS NULL;