# MualaFuel - Online Store

## General project requirements:

- Data stored in a relational database.
- Access to data via a pool of connections configured on the application server.
- Division into layers (separate components for the data layer - DAO, logic and presentation pattern, communication between layers using interfaces), data and logic layer components should run in the application server container (EJB/CDI), the presentation layer can be a web application or a client application (console or with a GUI running as a stand-alone client application in the client application container).
- Authentication support
- Unit tests.
- Event log *.

## Functionality:

- Viewing/adding/editing/removing products.
- Ability to place an order for selected products (shopping cart).
- Ability to view placed orders.
- Sending messages about placed orders via e-mail.
- Authentication.

## Sample Screenshots
![image1](https://github.com/user-attachments/assets/b77bff37-75ed-4fba-9800-0b97085e29a1)
![image2](https://github.com/user-attachments/assets/4341fe77-96ea-4a27-9300-dbac3d614a4a)
![image3](https://github.com/user-attachments/assets/66683e1b-c9a6-44e1-9318-958a93eed0e6)
![image4](https://github.com/user-attachments/assets/a8a0532b-db2e-4ec1-87d1-54f05fa0252f)
![image5](https://github.com/user-attachments/assets/50d61e80-ebed-490d-bb6a-c7cee4d3e5d5)
![image6](https://github.com/user-attachments/assets/fc8a974f-1dfa-45fe-b433-b43d47f9ed52)
![image7](https://github.com/user-attachments/assets/7aec9686-21e8-43d2-bf5c-5b1e514ff60f)


## How to run the backend
### Install Maven dependencies:
- In the terminal in the project directory run the command:
```
mvn clean install
```
### Run the Spring Boot application:
- In the terminal, run the command:
```
mvn spring-boot:run
```
- make sure you have jdk 21 installed
## How to run the Frontend
### Install node Modules:
- In the terminal in the project directory run the command:
```
npm install
```
### Run the React application:
- In the terminal, run the command:
```
npm run dev
```
