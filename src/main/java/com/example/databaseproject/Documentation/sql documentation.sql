/*Проект представляет собой реляционную базу данных, предназначенную для управления заказами автомобилей, информацией о клиентах, компаниях-производителях автомобилей и детализацией заказов. База данных разработана на основе нормализованных таблиц с четко определенными связями между ними.

Структура базы данных
База данных состоит из 6 таблиц:

customers — таблица клиентов
companies — таблица компаний-производителей автомобилей
cars — таблица автомобилей
orders — таблица заказов
order_details — детализированные данные по заказам
login_password_admin — таблица для хранения учетных данных администраторов
Описание таблиц
1. customers
Таблица хранит данные о клиентах, которые размещают заказы.

customer_id: INTEGER PRIMARY KEY — уникальный идентификатор клиента.
name: VARCHAR(255) — имя клиента.
login: VARCHAR(255) — логин клиента (уникальный).
password: VARCHAR(255) — пароль для входа в систему.
phone_number: INTEGER — номер телефона клиента.
Связи:

Используется как внешний ключ в таблице orders.
2. companies
Таблица содержит информацию о компаниях, которые производят автомобили.

company_id: INTEGER PRIMARY KEY — уникальный идентификатор компании.
country: VARCHAR(255) — страна, где зарегистрирована компания.
founded_year: VARCHAR(10) — год основания компании.
companies_name: VARCHAR(255) — название компании.
Связи:

Используется как внешний ключ в таблице cars.
3. cars
Таблица хранит информацию об автомобилях, связанных с компаниями.

car_id: INTEGER PRIMARY KEY — уникальный идентификатор автомобиля.
model: VARCHAR(255) — модель автомобиля.
year: INTEGER — год выпуска автомобиля.
price: INTEGER — стоимость автомобиля.
company_id: INTEGER — внешний ключ, ссылается на companies(company_id).
companies_name: VARCHAR(255) — дублирует имя компании для удобства.
Связи:

Используется как внешний ключ в таблицах orders и order_details.
4. orders
Таблица управляет заказами, связанными с клиентами и автомобилями.

order_id: INTEGER PRIMARY KEY — уникальный идентификатор заказа.
customer_id: INTEGER — внешний ключ, ссылается на customers(customer_id).
order_date: TIMESTAMP — дата и время размещения заказа.
amount: INTEGER — общая сумма заказа.
car_id: INTEGER — внешний ключ, ссылается на cars(car_id).
Связи:

Связывается с customers и cars.
Используется как внешний ключ в таблице order_details.
5. order_details
Таблица содержит детализированную информацию о заказах, включая количество автомобилей.

order_id: INTEGER — внешний ключ, ссылается на orders(order_id).
order_details_id: INTEGER PRIMARY KEY — уникальный идентификатор детали заказа.
car_id: INTEGER — внешний ключ, ссылается на cars(car_id).
quantity: INTEGER — количество автомобилей в заказе.
Связи:

Связывается с orders и cars.
6. login_password_admin
Таблица хранит учетные данные администраторов для обеспечения доступа к системе.

login: VARCHAR(255) — логин администратора.
password: VARCHAR(255) — пароль для входа.
Связи между таблицами
customers ↔ orders
Один клиент может иметь несколько заказов.

orders.customer_id ссылается на customers.customer_id.
companies ↔ cars
Одна компания производит несколько автомобилей.

cars.company_id ссылается на companies.company_id.
cars ↔ orders
Один автомобиль может быть включен в несколько заказов.

orders.car_id ссылается на cars.car_id.
orders ↔ order_details
Один заказ может иметь несколько деталей с конкретным количеством автомобилей.

order_details.order_id ссылается на orders.order_id.
order_details.car_id ссылается на cars.car_id.
Основные сценарии использования
Добавление клиентов
Заполнение таблицы customers данными новых клиентов.

Добавление автомобилей и компаний
В таблицу companies вносятся данные о компаниях-производителях, а затем автомобили добавляются в cars с указанием компании.

Размещение заказов
При добавлении нового заказа:

Данные заносятся в таблицу orders.
Подробности заказа (например, количество автомобилей) добавляются в таблицу order_details.
Администрирование
Доступ к системе ограничен таблицей login_password_admin, где хранятся учетные данные администраторов.
 */


-- Создание таблицы customers
-- Содержит информацию о клиентах
CREATE TABLE customers (
    customer_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number INTEGER NOT NULL
);

-- Создание таблицы companies
-- Содержит информацию о компаниях
CREATE TABLE companies (
    company_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    country VARCHAR(255) NOT NULL,
    founded_year VARCHAR(10),
    companies_name VARCHAR(255) NOT NULL
);

-- Создание таблицы cars
-- Содержит информацию об автомобилях, связанных с компаниями
CREATE TABLE cars (
    car_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    model VARCHAR(255) NOT NULL,
    year INTEGER NOT NULL,
    price INTEGER NOT NULL,
    company_id INTEGER NOT NULL,
    companies_name VARCHAR(255),
    FOREIGN KEY (company_id) REFERENCES companies(company_id)
);

-- Создание таблицы orders
-- Хранит информацию о заказах, связанных с клиентами и автомобилями
CREATE TABLE orders (
    order_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id INTEGER NOT NULL,
    order_date TIMESTAMP NOT NULL,
    amount INTEGER NOT NULL,
    car_id INTEGER NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (car_id) REFERENCES cars(car_id)
);

-- Создание таблицы order_details
-- Подробности о заказах, включая количество автомобилей
CREATE TABLE order_details (
    order_id INTEGER NOT NULL,
    order_details_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    car_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (car_id) REFERENCES cars(car_id)
);

-- Создание таблицы login_password_admin
-- Содержит данные для входа администраторов
CREATE TABLE login_password_admin (
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);


1. Получить список автомобилей и компаний-производителей:

SELECT c.model, c.year, c.price, comp.companies_name, comp.country
FROM cars c
JOIN companies comp ON c.company_id = comp.company_id;

Определить компании с наибольшими продажами:

SELECT comp.companies_name, SUM(od.quantity * c.price) AS total_sales
FROM companies comp
JOIN cars c ON comp.company_id = c.company_id
JOIN order_details od ON c.car_id = od.car_id
GROUP BY comp.companies_name
ORDER BY total_sales DESC;

получить последний заказ

SELECT o.order_id, o.order_date, cust.name, c.model, od.quantity
FROM orders o
JOIN customers cust ON o.customer_id = cust.customer_id
JOIN order_details od ON o.order_id = od.order_id
JOIN cars c ON od.car_id = c.car_id
ORDER BY o.order_date DESC
LIMIT 10;

Рассчитать общий доход от всех продаж:

SELECT SUM(od.quantity * c.price) AS total_revenue
FROM order_details od
JOIN cars c ON od.car_id = c.car_id;

Определить клиентов, которые не сделали ни одного заказа:

SELECT cust.customer_id, cust.name
FROM customers cust
LEFT JOIN orders o ON cust.customer_id = o.customer_id
WHERE o.customer_id IS NULL;
