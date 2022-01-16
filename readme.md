## Guide d'utilisation 
Pour lancer ce projet, il faut avoir docker d'installé sur votre machine.
###On commence par lancer la base de données et consul avec les commandes suivantes :
```docker-compose up```
<br>
``` docker run  -p 8500:8500 -p 8600:8600/udp --name=consul consul:1.9.13```
###On prépare ensuite les 3 services utilisés par le projet principal :
```mvn -f .\Conversion\ spring-boot:run```
<br>
```mvn -f .\Localisation\ spring-boot:run```
<br>
### Et le projet principal :
```mvn -f .\Banque\ spring-boot:run```
### Pour finir le commerçant qui communiquera avec la banque :
```mvn -f .\Commerce\ spring-boot:run ```

Le projet est maintenant fonctionnel, vous pouvez utiliser le fichier `Banque_SID.postman_collection` 
<br> dans postman pour tester les différentes routes disponibles sur l'API.
<br>
### Documentation Swagger :
http://127.0.0.1:8081/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config