ozcowms: clean product user-center gateway miniapp weapp

product: common
	mvn -f src/backend/product clean install -DskipTests && cp src/backend/product/target/*.jar dist/apps

user-center: common
	mvn -f src/backend/user clean install -DskipTests && cp src/backend/user/target/*.jar dist/apps

gateway: common
	mvn -f src/backend/gateway clean install -DskipTests && cp src/backend/gateway/target/*.jar dist/apps

miniapp: common
	mvn -f src/backend/miniapp clean install -DskipTests && cp src/backend/miniapp/target/*.jar dist/apps

common: init
	mvn -f src/backend/common clean package -DskipTests

init:
	mkdir -p dist/apps

clean:
	rm -rf dist/*

weapp:
	yarn --cwd src/miniapp install && yarn --cwd ./src/miniapp build
	cp -r src/miniapp/weapp dist/

deploy: deploy-product deploy-user deploy-gateway deploy-miniapp
	ssh root@106.12.31.181 "docker-compose up -d"

deploy-product: product
	scp dist/apps/product-*.jar root@106.12.31.181:/root/apps
	ssh root@106.12.31.181 "docker-compose restart product"

deploy-user: user
	scp dist/apps/user-*.jar root@106.12.31.181:/root/apps
	ssh root@106.12.31.181 "docker-compose restart user-center"

deploy-gateway: gateway
	scp dist/apps/gateway-*.jar root@106.12.31.181:/root/apps
	ssh root@106.12.31.181 "docker-compose restart gateway"

deploy-miniapp: miniapp
	scp dist/apps/miniapp-*.jar root@106.12.31.181:/root/apps
	ssh root@106.12.31.181 "docker-compose restart miniapp"
