.PHONY: help build test clean run docker-build docker-run docker-stop

help: ## Show this help message
	@echo "Available commands:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

build: ## Compile the project
	mvn clean compile

test: ## Run tests
	mvn test

clean: ## Clean the project
	mvn clean

run: ## Run the application
	mvn spring-boot:run

package: ## Create application JAR
	mvn clean package -DskipTests

docker-build: ## Build Docker image
	docker build -t race-manager-api .

docker-run: ## Run application with Docker Compose
	docker-compose up -d

docker-stop: ## Stop Docker application
	docker-compose down

docker-logs: ## Show Docker logs
	docker-compose logs -f

install: ## Install dependencies
	mvn clean install -DskipTests

coverage: ## Generate coverage report
	mvn jacoco:report

swagger: ## Open Swagger documentation
	@echo "Access: http://localhost:8080/api/v1/swagger-ui.html"

db-reset: ## Reset database (careful!)
	@echo "WARNING: This will delete all data!"
	@read -p "Are you sure? (y/N): " confirm && [ "$$confirm" = "y" ] || exit 1
	mvn spring-boot:run -Dspring.jpa.hibernate.ddl-auto=create-drop

dev: ## Development mode
	mvn spring-boot:run -Dspring-boot.run.profiles=dev

prod: ## Production mode
	mvn spring-boot:run -Dspring-boot.run.profiles=prod
