# Docker Bind Mounts and Volumes

## Introduction

Docker containers are lightweight and portable, but by default they use **ephemeral storage**. This means that any data created inside a container exists only for the lifetime of that container. Once the container is removed, all its data is lost.

To solve this problem, Docker provides **Mounts**, mainly:

* **Bind Mounts**
* **Docker Volumes**

These mechanisms help persist data and enable data sharing between containers and the host system.

---

# Problems with Containers Without Mounts

## 1. Ephemeral Storage

Containers store data inside their writable layer.

When a container is:

* stopped and removed
* recreated
* rebuilt from an image

all runtime data is lost.

---

## 2. Data Loss

Important application data may disappear, including:

* Database files
* Logs
* User uploads
* Generated reports
* Configuration changes

---

## 3. Containers Are Stateless

Docker containers are designed to be **stateless**.

Application code and persistent data should be kept separate to ensure portability and reliability.

---

## 4. Rebuilding Images Removes Runtime Data

Rebuilding an image creates a new container filesystem.

As a result:

* previous logs vanish
* uploaded files disappear
* database contents are lost

---

## 5. No Easy Data Sharing

Without mounts, containers cannot easily share files with:

* the host machine
* other containers

---

# Why Docker Introduced Mounts

Docker needed a mechanism to:

* Persist data beyond container lifecycle
* Share data between host and container
* Share data among multiple containers
* Separate application code from persistent data

This requirement led to **Bind Mounts** and **Volumes**.

---

# Bind Mount

## Definition

A **Bind Mount** directly maps a specific directory or file from the host machine into a container.

The host machine controls the storage location.

---

## Syntax

```bash
docker run -v /host/path:/container/path image_name
```

---

## Example

```bash
docker run -v /home/ubuntu/data:/app/data nginx
```

### Mapping

```text
Host Directory                 Container Directory
/home/ubuntu/data      --->    /app/data
```

Any changes made in one location immediately appear in the other.

---

## Characteristics of Bind Mounts

* Uses existing host directories
* Host controls storage location
* Suitable for development environments
* Easy file editing from the host machine

---

# Docker Volumes

## Definition

A **Docker Volume** is Docker-managed persistent storage.

Docker automatically decides where and how the data is stored.

Volumes are independent of the container lifecycle.

Even if the container is deleted, the volume remains.

---

## Default Storage Location (Linux)

```bash
/var/lib/docker/volumes/
```

Example:

```bash
/var/lib/docker/volumes/kumar/_data
```

---

## Advantages of Volumes

* Persistent storage
* Better portability
* Easier backup and migration
* Data sharing among containers
* Preferred for production systems

---

# Volume Commands

## Create a Volume

```bash
docker volume create kumar
```

---

## List Volumes

```bash
docker volume ls
```

---

## Inspect a Volume

```bash
docker volume inspect kumar
```

### Sample Output

```json
[
  {
    "CreatedAt": "2026-01-12T15:52:17Z",
    "Driver": "local",
    "Mountpoint": "/var/lib/docker/volumes/kumar/_data",
    "Name": "kumar",
    "Scope": "local"
  }
]
```

---

## Meaning of Important Fields

| Field      | Description                   |
| ---------- | ----------------------------- |
| Driver     | Storage driver used by Docker |
| Mountpoint | Actual host path storing data |
| Name       | Volume name                   |
| Scope      | Local or shared scope         |

---

# Mapping a Volume to a Container

## Using `-v`

```bash
docker run -it -v kumar:/app volume-demo
```

---

## Using `--mount` (Recommended)

```bash
docker run -it \
--mount source=kumar,target=/app \
volume-demo
```

`--mount` is preferred because it is:

* more readable
* explicit
* easier to understand

---

# Data Persistence Example

Create a file inside the container:

```bash
echo "Hello Docker" > /app/file.txt
```

Exit and remove the container:

```bash
docker rm -f container_id
```

Run a new container using the same volume:

```bash
docker run -it -v kumar:/app volume-demo
```

Read the file:

```bash
cat /app/file.txt
```

Output:

```text
Hello Docker
```

The data persists because it is stored in the volume rather than inside the container.

---

# Dockerfile and Volumes

Volumes are attached to **containers**, not directly to images.

However, an image developer can specify a suggested mount point using:

```dockerfile
FROM ubuntu:latest

WORKDIR /app

VOLUME ["/app/data"]

CMD ["bash"]
```

Build the image:

```bash
docker build -t volume-demo .
```

Inspect the image:

```bash
docker image inspect volume-demo
```

Output:

```json
"Volumes": {
    "/app/data": {}
}
```

This indicates that `/app/data` is intended for persistent storage.

---

# Running a Container with a Named Volume

```bash
docker run -it \
-v kumar:/app/data \
volume-demo
```

or

```bash
docker run -it \
--mount source=kumar,target=/app/data \
volume-demo
```

---

# Anonymous Volume

If an image contains a `VOLUME` instruction and no volume is specified during runtime:

```bash
docker run -it volume-demo
```

Docker automatically creates an **anonymous volume**.

---

# Bind Mount vs Docker Volume

| Feature          | Bind Mount     | Docker Volume  |
| ---------------- | -------------- | -------------- |
| Managed By       | Host OS        | Docker         |
| Storage Location | User specified | Docker managed |
| Easy Host Access | Yes            | Limited        |
| Portability      | Less           | High           |
| Best Use Case    | Development    | Production     |
| Data Persistence | Yes            | Yes            |

---

# Real-World Use Cases

| Application              | Persistent Directory       |
| ------------------------ | -------------------------- |
| MySQL                    | `/var/lib/mysql`           |
| PostgreSQL               | `/var/lib/postgresql/data` |
| MongoDB                  | `/data/db`                 |
| Nginx Logs               | `/var/log/nginx`           |
| File Upload Applications | `/uploads`                 |

---

# Important Interview Question

**Q: Are volumes attached to images or containers?**

**Answer:**

Volumes are attached to **containers** during runtime.

Images can only suggest mount points using the `VOLUME` instruction.

Actual volume mapping occurs when the container is started using:

```bash
docker run -v myvolume:/path image_name
```

---

# Workflow

```text
Dockerfile
     ↓
Build Image
     ↓
Create Volume
     ↓
Run Container
     ↓
Attach Volume
     ↓
Persistent Data Storage
```

---

# Conclusion

Docker containers are ephemeral by nature, making persistent storage essential for real-world applications. **Bind Mounts** provide direct access to host files and are commonly used during development, while **Docker Volumes** offer Docker-managed persistent storage and are preferred for production environments.

Using mounts effectively ensures:

* Data persistence
* Portability
* Scalability
* Better container management
* Reliable application deployment

