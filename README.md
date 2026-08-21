# AI Quote & Background Web App

A full-stack web application that generates a daily quote using AI and displays it with a related background image.

The application also includes user registration, email verification, login, and data management.

## Key Features

- Daily AI quote generation
- Related image search using Pexels
- User registration and login
- Email verification using SMTP
- Session-based login
- Data save and retrieval
- Responsive web interface

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.4
- MyBatis
- Spring Data JPA
- MariaDB
- Lombok
- Gradle

### Frontend
- React
- Redux Toolkit
- Bootstrap
- JavaScript

### API & Integration
- OpenAI API
- Pexels API
- OkHttp
- Gson
- Spring Mail (SMTP)

## Architecture

**React Frontend** ↔ **REST API** ↔ **Spring Boot Backend** ↔ **MariaDB**

**External Services**
- OpenAI API
- Pexels API
- Spring Mail (SMTP)

The frontend and backend are separated and communicate through REST APIs.

## Implementation

- REST API development with Spring Boot
- Database access with MyBatis and Spring Data JPA
- Session-based login and email verification
- OpenAI and Pexels API integration
- External API communication using OkHttp
- JSON data processing using Gson
- Frontend state management with Redux Toolkit

## Project Structure

- `portfolio-front-app` — React frontend
- `portfolio-back-app` — Spring Boot backend

*This project is for portfolio purposes, and commercial use is prohibited.
