# Docker Networking

Docker Networking enables containers to communicate:

- With other containers
- With the host machine
- With external networks (Internet)

Containers are isolated by default. Docker networking provides mechanisms that allow containers to communicate securely while maintaining isolation.

---

## List Available Networks

```bash
docker network ls
```

Example output:

```bash
NETWORK ID     NAME      DRIVER    SCOPE
144a47e5a979   bridge    bridge    local
e2a8948b64a1   host      host      local
51647490bf06   none      null      local
```

Docker provides several network drivers:

- **Bridge**
- **Host**
- **Overlay**
- **Macvlan**
- **None**

---

# 1. Bridge Networking

Bridge networking is Docker's default networking mode.

Docker creates a virtual bridge interface called **docker0** on the host machine. Containers connected to the default bridge communicate through this bridge.

## Characteristics

- Default network mode
- Containers get private IP addresses
- Containers can communicate with each other
- Containers can communicate with the host
- Network Address Translation (NAT) enables internet access

---

## Default Bridge Network

When containers are launched without specifying a network, they join the default bridge network.

```bash
docker run -d --name login nginx:latest
docker run -d --name logout nginx:latest
```

Inspect container IP addresses:

```bash
docker inspect login
docker inspect logout
```

Example:

```json
login:
"IPAddress": "172.17.0.2"

logout:
"IPAddress": "172.17.0.3"
```

Both containers belong to the same subnet:

```text
172.17.0.0/16
```

---

## Why Custom Bridge Networks?

Although containers on the default bridge can communicate with each other, applications often require network isolation.

Custom bridge networks provide:

- Better isolation
- Automatic DNS resolution
- Improved security
- Easier service discovery

---

## Create a Custom Bridge Network

```bash
docker network create -d bridge my_bridge
```

Verify:

```bash
docker network ls
```

Example:

```bash
NETWORK ID     NAME        DRIVER
xxxxxxx        bridge      bridge
xxxxxxx        my_bridge   bridge
```

---

## Run Container on Custom Network

```bash
docker run -d --name finance --network my_bridge nginx:latest
```

Inspect:

```bash
docker inspect finance
```

Example:

```json
"IPAddress": "172.18.0.2"
```

Notice that Docker allocates a different subnet:

```text
172.18.0.0/16
```

Containers on different bridge networks are isolated and cannot communicate unless explicitly connected.

---

## Connect Existing Container to Network

```bash
docker network connect my_bridge login
```

Disconnect:

```bash
docker network disconnect my_bridge login
```

---

## Remove Network

```bash
docker network rm my_bridge
```

> Note: Docker's default networks (`bridge`, `host`, `none`) cannot be removed.

---

# Bridge Network Architecture

```text
                 Host Machine
        +---------------------------+
        |          docker0          |
        |                           |
        |  login     logout         |
        | 172.17.0.2 172.17.0.3     |
        +---------------------------+
```

Custom bridge:

```text
                my_bridge
         +-------------------+
         | finance 172.18.0.2|
         +-------------------+
```

---

# 2. Host Networking

Host networking allows a container to share the host's network stack.

In this mode:

- No separate container IP
- No NAT translation
- Container uses host network directly
- Better performance
- Less isolation

---

## Run Container Using Host Network

```bash
docker run -d --name host-demo --network host nginx:latest
```

Inspect:

```bash
docker inspect host-demo
```

Output:

```json
"IPAddress": ""
```

The container uses the host's IP address directly.

---

## Host Networking Architecture

```text
Host Machine
+--------------------------------+
| Host Network Stack             |
|                                |
| Nginx Container                |
| (shares host network)          |
+--------------------------------+
```

---

## Advantages

- Very low network overhead
- High performance
- Useful for monitoring tools

## Disadvantages

- Reduced isolation
- Security risks
- Port conflicts may occur

---

# 3. Overlay Networking

Overlay networking enables communication across multiple Docker hosts.

Used mainly in:

- Docker Swarm
- Multi-host container deployments

Containers running on different machines can communicate as if they are on the same network.

---

## Create Overlay Network

```bash
docker network create \
-d overlay \
my_overlay
```

---

## Architecture

```text
Host A ---------------- Host B
   |                       |
Container A           Container B
        \             /
         \           /
          Overlay Network
```

---

# 4. Macvlan Networking

Macvlan allows containers to appear as physical devices on the network.

Each container receives:

- Its own MAC address
- Its own IP address

The container behaves like an independent machine on the network.

---

## Use Cases

- Legacy applications
- Network appliances
- Applications requiring direct LAN access

---

# 5. None Networking

The `none` driver disables networking completely.

Container has:

- No network interface
- No internet access
- Complete isolation

---

## Create Container with No Networking

```bash
docker run -d --network none nginx
```

---

# Important Docker Networking Commands

## List Networks

```bash
docker network ls
```

## Inspect Network

```bash
docker network inspect bridge
```

## Create Network

```bash
docker network create my_bridge
```

## Remove Network

```bash
docker network rm my_bridge
```

## Connect Container to Network

```bash
docker network connect my_bridge login
```

## Disconnect Container from Network

```bash
docker network disconnect my_bridge login
```

## Inspect Container Network

```bash
docker inspect <container_id>
```

---

# Key Interview Points

### Why do we create custom bridge networks?

- To isolate applications
- Improve security
- Enable automatic DNS resolution
- Prevent unnecessary container communication

### What is docker0?

- A virtual bridge interface created by Docker
- Used by the default bridge network

### Why is Host networking faster?

- No NAT translation
- Direct access to host network stack

### Can default networks be deleted?

❌ No

The following Docker networks are pre-defined:

- bridge
- host
- none

### Which network driver is used for multi-host communication?

✅ Overlay Network

---

# Conclusion

Docker networking provides secure and flexible communication between containers, hosts, and external systems. Choosing the correct network driver depends on the application's requirements for isolation, performance, and scalability.
