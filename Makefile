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
