# avito-backend


Чтобы запустить приложение:

```shell
docker-compose up --build -d
```

проверка эндпоинтов по адресу http://localhost:8080/api/

например:

```shell 
curl -sX GET "http://localhost:8080/api/ping"
```