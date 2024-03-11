## WebSocket API (STOMP)

### Эндпоинт

Адрес сервера WebSocket: `ws://ip:port/ws`

destination: `/app/sendMessage`

topic: `/topic/messages`

### Диисклеймер
Даалее документация написана chatGPT.

### Зависимость

#### Gradle
```groovy
dependencies {
    implementation 'com.github.NaikSoftware:StompProtocolAndroid:2.1.2'
}
```

#### Maven
```xml
<dependency>
    <groupId>com.github.NaikSoftware</groupId>
    <artifactId>StompProtocolAndroid</artifactId>
    <version>2.1.2</version>
</dependency>
```

Для работы с WebSocket на платформе Android используется библиотека **STOMP Protocol for Android**. Она предоставляет удобный интерфейс для работы с WebSocket и протоколом STOMP (Simple Text Oriented Messaging Protocol), позволяя устанавливать соединение с сервером, отправлять и принимать сообщения.

### Пример отправки сообщения

Для отправки сообщения на сервер с использованием библиотеки **STOMP Protocol for Android** выполните следующие шаги:

1. Создайте экземпляр клиента STOMP:

```java
StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://ip:port/ws");
```

2. Подключитесь к серверу WebSocket:
```java
stompClient.connect();
```

3. Отправьте сообщение на сервер:
```java
JSONObject messageBody = new JSONObject();
try {
    messageBody.put("text", "Привет, мир!");
    messageBody.put("senderId", 1);
    messageBody.put("recipientId", 2);
} catch (JSONException e) {
    e.printStackTrace();
}

stompClient.send("/topic/messages", messageBody.toString())
    .subscribe();
```

4. Закройте соединение с сервером WebSocket после завершения работы:

```java
stompClient.disconnect();
```

### Пример получения сообщения

Пример получения сообщений с сервера WebSocket с использованием библиотеки STOMP Protocol for Android:
```java
Disposable disposable = stompClient.topic("/topic/messages")
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(topicMessage -> {
        // Обрабатываем входящее сообщение
        String text = topicMessage.getPayload();
        Log.d(TAG, "Received message: " + text);
        // Здесь можно выполнить необходимые действия с полученным сообщением
    }, throwable -> {
        // Обрабатываем ошибку подписки
        Log.e(TAG, "Error during subscription", throwable);
    });
```