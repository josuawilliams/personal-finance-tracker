# Load .env file so make targets can use variables (e.g. pgadmin-url)
ifneq (,$(wildcard ./.env))
    include .env
    export
endif

.PHONY: up up-build down down-volumes build logs app-logs db-logs pgadmin-logs db-shell pgadmin-url clean rebuild test help

COMPOSE = docker compose

## Start all services in detached mode
up:
	$(COMPOSE) up -d

## Start all services and rebuild images
up-build:
	$(COMPOSE) up -d --build

## Stop all services
down:
	$(COMPOSE) down

## Stop all services and remove volumes (DESTRUCTIVE — deletes DB data)
down-volumes:
	$(COMPOSE) down -v

## Build or rebuild the app image
build:
	$(COMPOSE) build app

## Follow logs from all services
logs:
	$(COMPOSE) logs -f

## Follow app logs only
app-logs:
	$(COMPOSE) logs -f app

## Follow database logs only
db-logs:
	$(COMPOSE) logs -f db

## Follow pgAdmin logs only
pgadmin-logs:
	$(COMPOSE) logs -f pgadmin

## Open an interactive PostgreSQL shell inside the DB container
db-shell:
	$(COMPOSE) exec db sh -c 'psql -U $$POSTGRES_USER -d $$POSTGRES_DB'

## Show pgAdmin access credentials and URL
pgadmin-url:
	@echo "pgAdmin URL: http://localhost:$(PGADMIN_PORT)"
	@echo "Email:    $(PGADMIN_DEFAULT_EMAIL)"
	@echo "Password: $(PGADMIN_DEFAULT_PASSWORD)"

## Remove containers, networks, images AND volumes (DESTRUCTIVE)
clean:
	$(COMPOSE) down -v --rmi all

## Full rebuild from scratch (stop, wipe volumes, build, start)
rebuild: down-volumes up-build

## Run local Maven tests
test:
	./mvnw test

## Show available make targets
help:
	@echo "Available make targets:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'
