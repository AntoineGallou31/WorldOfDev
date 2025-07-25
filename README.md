# MDD Project - Monde Du Dév

## Description

The MDD (Monde Du Dév) project is a social network developed as part of a student project on OpenClassrooms.  
The goal of this application is to help developers looking for work by facilitating networking and encouraging collaboration between peers with common interests.

The objective is to create a space where developers can connect, share ideas, and potentially collaborate on projects.  
Ultimately, MDD could become a platform for companies looking for developers with specific skills.

## Technologies Used

- **Backend**: Spring Boot
- **Database**: MySQL
- **Frontend**: Angular
- **Authentication**: JSON Web Token (JWT)
- **Asynchronous data management**: RxJS

## Installation

### Prerequisites

Before you begin, make sure you have the following tools installed:

- Java 23 or higher
- Maven
- Node.js and npm
- Angular CLI

### 1. Clone the repository

Start by cloning this repository to your local machine:

```bash
git clone https://github.com/AntoineGallou31/WorldOfDev.git
```

### 2. Install dependencies

Before running the applications, install the dependencies for both the frontend and backend.

#### Backend

Navigate to the `back` folder and run the following commands to clean and start the Spring Boot application:

```bash
cd back
mvn clean
mvn spring-boot:run
```

#### Frontend

Navigate to the `front` folder and install the Node.js dependencies, then run the Angular development server:

```bash
cd front
npm install
ng serve
```

The frontend will be accessible at `http://localhost:4200`, and the backend at `http://localhost:3001`.

## Usage

Once the application is running, you can:

- Create a developer account
- Log in with your credentials
- View articles published by other users
- Comment on articles
- Follow topics that interest you
- Edit your profile to update your information  
