# Java Multi-Stage Docker Build

This project demonstrates how to create a Java application using Docker Multi-Stage Builds.

## What is Multi-Stage Build?

A Multi-Stage Build allows us to separate the build environment from the runtime environment.

* **Build Stage:** Compiles the Java source code.
* **Runtime Stage:** Runs only the compiled application.

This reduces image size and improves security by removing unnecessary tools such as compilers.

---

## Project Structure

```text
.
├── Calculator.java
└── Dockerfile
```

---

## Dockerfile

### Build Stage

* Uses a JDK image.
* Compiles `Calculator.java` into `Calculator.class`.

### Runtime Stage

* Uses a lightweight Java runtime image.
* Copies only the compiled class file from the build stage.
* Starts the application using `ENTRYPOINT`.

---

## Build the Docker Image

```bash
docker build -t kumara66/java-calculator:latest .
```

---

## Run the Container

Since the application accepts user input, run it in interactive mode:

```bash
docker run -it kumara66/java-calculator:latest
```

---

## Sample Execution

```text
Hi Kumara Swamy M, I am a calculator app ....

Enter Expression: 10 + 20
Result: 30

Enter Expression: 7 * 8
Result: 56

Enter Expression: exit
```

---

## Advantages of Multi-Stage Builds

* Smaller Docker images
* Faster deployments
* Improved security
* No source code in final image
* No compiler in production image

---

## Image Size Comparison

| Build Type          | Approximate Size |
| ------------------- | ---------------: |
| Without Multi-Stage |       650–800 MB |
| With Multi-Stage    |       250–350 MB |

---

## Key Docker Concepts Used

* `FROM` – Specifies the base image
* `WORKDIR` – Sets the working directory
* `COPY` – Copies files into the image
* `RUN` – Executes commands during build
* `ENTRYPOINT` – Defines the container startup command

This project demonstrates how Docker Multi-Stage Builds help create efficient and production-ready Java applications.

