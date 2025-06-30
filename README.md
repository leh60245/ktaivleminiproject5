# 

## Model
www.msaez.io/#/55617010/storming/7352a2188abfc29fe347d24bd5042633

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- AuthorManagement
- SubscriptionManagement
- librarymanagement
- pointmanagement
- ManuscriptPublication


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- AuthorManagement
```
 http :8088/authors authorId="authorId"name="name"createdAt="createdAt"updatedAt="updatedAt"portfolio="portfolio"approvedAt="approvedAt"
```
- SubscriptionManagement
```
 http :8088/subscriptions subscriptionId="subscriptionId"userId="userId"startDate="startDate"endDate="endDate"isSubscription="isSubscription"
```
- librarymanagement
```
 http :8088/books bookId="bookId"authorId="authorId"title="title"content="content"category="category"summary="summary"coverImageUrl="coverImageUrl"readCount="readCount"bestsellerBadge="bestsellerBadge"publishedDate="publishedDate"
```
- pointmanagement
```
 http :8088/users userId="userId"name="name"ktCustomer="ktCustomer"points="points"createdAt="createdAt"
```
- ManuscriptPublication
```
 http :8088/manuscripts manuscriptId="manuscriptId"title="title"content="content"createdDate="createdDate"authorId="authorId"
 http :8088/publicationRequests publicationRequestId="publicationRequestId"publishedDate="publishedDate"createdDate="createdDate"category="category"summary="summary"coverImageUrl="coverImageUrl"
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```
