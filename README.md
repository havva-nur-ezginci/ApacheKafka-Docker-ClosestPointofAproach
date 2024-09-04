# CPA (Closest Point of Aproach)
Bu proje, belirli bir alan içerisinde hareket eden gemilerin birbirlerine olan en yakın mesafelerini ve bu yakınlaşmanın gerçekleşeceği zamanı hesaplayan bir sistem geliştirmeyi amaçlamaktadır. Proje, gemilerden alınan kinematik verileri işleyerek, her iki gemi arasında en yakın mesafeyi hesaplar ve sonuçları bildirir.

<details>
    <summary><h2>Yakınlaşma Hesap Uygulaması (CPA Uygulaması)</h2></summary>

  ### Kinematik Bilgi Karşılaştırması: 
Bir gemiden kinematik bilgi alındığında, bu bilgi diğer tüm gemilerin kinematik bilgileri ile karşılaştırılır. Her iki gemi çifti için, bu iki geminin gelecekte birbirine en yakın olduğu an ve bu anda aralarındaki mesafe hesaplanır.

### Sonuçların Yayınlanması: 
Hesaplanan mesafe ve zaman bilgisi anında yayınlanır.

### Veri Yokluğu Durumu: 
Eğer herhangi bir gemiden 10 saniye boyunca veri alınmazsa, bu gemi için yapılmış hesaplamaların sonuçları iptal edilir.

### Haberleşme: 
Uygulamalar arası haberleşme için Apache Kafka kullanılmaktadır. 
</details>


### 1. Zookeeper'ı Ayağa Kaldırma
```sh
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
### 2. Apache Kafka'yı Ayağa Kaldırma
```sh
.\bin\windows\kafka-server-start.bat .\config\server.properties
```
### 3. Topic Konfigürasyon Ayarları ve Oluşturma
```sh
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic javainuse-topic
```
### 4. Oluşturulan Topiclerin Detaylı Görünümüne Bakma
```sh
.\bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --all-groups --all-topics
```
### 5. Producer ve Consumer ile Mesajlaşma
#### Producer ile Mesaj Yazma:
```sh
.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic javainuse-topic
```
#### Consumer ile Mesaj Okuma:
```sh
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic javainuse-topic --from-beginning
```
