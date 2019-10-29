ozcowms: product user gateway miniapp

product: common
	mvn -f src/backend/product clean install -DskipTests && cp src/backend/product/target/*.jar apps

user: common
	mvn -f src/backend/user clean install -DskipTests && cp src/backend/user/target/*.jar apps

gateway: common
	mvn -f src/backend/gateway clean install -DskipTests && cp src/backend/gateway/target/*.jar apps

miniapp: common
	mvn -f src/backend/miniapp clean install -DskipTests && cp src/backend/miniapp/target/*.jar apps

common: init
	mvn -f src/backend/common clean package -DskipTests

init:
	mkdir -p apps

clean:
	rm -rf apps

deploy: deploy-product deploy-user deploy-gateway deploy-miniapp
	ssh root@106.12.31.181 "docker-compose up -d"

deploy-product: product
	scp apps/product-*.jar root@106.12.31.181:/root/apps
	ssh root@106.12.31.181 "docker-compose restart product"

deploy-user: user
	scp apps/user-*.jar root@106.12.31.181:/root/apps
	ssh root@106.12.31.181 "docker-compose restart user-center"

deploy-gateway: gateway
	scp apps/gateway-*.jar root@106.12.31.181:/root/apps
	ssh root@106.12.31.181 "docker-compose restart gateway"

deploy-miniapp: miniapp
	scp apps/miniapp-*.jar root@106.12.31.181:/root/apps
	ssh root@106.12.31.181 "docker-compose restart miniapp"
