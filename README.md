```
Создать spring boot 2 приложение:

В нём реализовать метод в rest-контроллере, принимающий на вход:
1) operationId, являющийся частью урла запроса
2) параметры post-запроса, прилетающие с некой html-формы
3) в ответ отдать json-объект вида:

{
  "status": "success",
  "result": [
    {
      "signature": "signature_value",
    }
  ]
}

, где signature_value - Hmac SHA256 hash от строки вида:

name1=value1&name2=value2...

, где nameN=valueN - полученные из запроса имя и значение параметров формы, отсортированных по имени параметра

В приложении реализовать предобработку для данного контроллера, проверяющую, что в http-заголовках запроса есть заголовок Token со значением, равным значению настройки из конфига.
При отсутствии данного http-заголовка или другом его значении выдавать 403 http-код.
```


## Запуск
* Скомпилировать jar-ник и запустить его;
* Или запустить докер-контейнер, описанный в Dockerfile.

В application.properties можно переопределить некоторые свойства, например, ожидаемый заголовок Token.