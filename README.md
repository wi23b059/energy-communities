# energyCommunities

This project implements a distributed system simulating **energy communities** — associations of participants jointly producing and consuming energy. It demonstrates real-time energy distribution between community producers, community users, and the grid.

## Overview

The system consists of multiple independent components that communicate via a message queue to track and calculate energy production and consumption:

- **Community Energy Producer:**  
  Simulates energy production with semi-random values influenced by weather.

- **Community Energy User:**  
  Simulates energy consumption with time-of-day based variability.

- **Usage Service:**  
  Aggregates minute-level messages into hourly community and grid usage stored in a database.

- **Current Percentage Service:**  
  Calculates the percentage of community vs. grid energy usage for the current hour.

- **REST API:**  
  Provides endpoints to fetch current and historical energy usage data.

- **GUI:**  
  Displays energy distribution data in real-time and via historical queries, fetching data through the REST API.

## Key Concepts

- Messages indicating energy production or usage are sent continuously to a message queue.
- The usage service updates the database with aggregated data.
- When community energy is insufficient, grid energy compensates.
- The current percentage service calculates how much energy comes from the community versus the grid.
- The GUI visualizes this information, showing both current status and historical trends.

## Technology Stack

- RabbitMQ (Message Queue)
- PostgreSQL (Persistent Database)
- Spring Boot (REST API and backend services)
- JavaFX (GUI)
- Docker Compose (Orchestration)

## Running the Project

Use Docker Compose to build and run all components with:

```bash
docker compose up --build
