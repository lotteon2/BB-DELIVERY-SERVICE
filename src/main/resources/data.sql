INSERT INTO delivery (delivery_tracking_number, delivery_orderer_name, delivery_orderer_phone_number, delivery_orderer_email, delivery_recipient_name, delivery_road_name, delivery_address_detail, delivery_zipcode, delivery_recipient_phone_number, delivery_request, delivery_cost, delivery_status)
VALUES
    ('TN123456789', '홍길동', '010-1234-5678', 'honggil@example.com', '이순신', '서울시 강남구 테헤란로', '101호', '06236', '010-8765-4321', '문 앞에 놓아주세요', 5000, 'PENDING'),
    ('TN987654321', '김철수', '010-1111-2222', 'chulsoo@example.com', '박영희', '서울시 서대문구 신촌로', '202호', '120-749', '010-3333-4444', '부재시 경비실에 맡겨주세요', 3000, 'PENDING'),
    ('TN123459876', '이영자', '010-5555-6666', 'youngja@example.com', '김민수', '서울시 종로구 종로', '303호', '03142', '010-7777-8888', '조심히 다루어 주세요', 4000, 'PENDING');

INSERT INTO delivery_address (user_id, delivery_recipient_name, delivery_road_name, delivery_address_detail, delivery_zipcode, delivery_recipient_phone_number)
VALUES
    (1, '홍길동', '서울시 강남구 테헤란로', '101호', '06236', '010-1234-5678'),
    (2, '김철수', '서울시 서대문구 신촌로', '202호', '120-749', '010-1111-2222'),
    (3, '이영자', '서울시 종로구 종로', '303호', '03142', '010-5555-6666');
