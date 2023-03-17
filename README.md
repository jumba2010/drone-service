
# Drone Service

A Java-based application for managing drone delivery services.


## API Documentation

####  Retrieves a list of available drones that can be loaded with medications

```http
  GET /api/v1/drones/available-for-loading
```

#####  Response payload
```rest
[
   {
        "id": 100,
        "serialNumber": "SN001",
        "model": "LIGHTWEIGHT",
        "weightLimit": 300.0,
        "batteryCapacity": 100,
        "state": "IDLE",
        "loadedMedications": []
    },
    {
        "id": 101,
        "serialNumber": "SN002",
        "model": "MIDDLEWEIGHT",
        "weightLimit": 400.0,
        "batteryCapacity": 22,
        "state": "IDLE",
        "loadedMedications": []
    }
]
```

#### Loads a drone with a list of medications and updates the drone's state to LOADED

```http
  POST /api/v1/drones/{droneSerialNumber}/medications
```
#### Payload
``` 
{
"medicationIds": [1, 2, 3]
}
```


####  Unloads medications from a drone and updates the drone's state to IDLE when it has no medications left

```http
  DELETE /api/v1/drones/{droneSerialNumber}/medications
```
#### Payload
``` 
{
"medicationIds": [1, 2, 3]
}
```


####  Returns a list of medications currently loaded on the drone identified by the given serial number

```http
  GET /api/v1/drones/{droneSerialNumber}/medications
```

#####  Response payload
```rest
[
    {
        "id": 1,
        "code": "CODE2",
        "name": "Medication 3",
        "weight": 65.0,
        "image": null,
        "drone": null
    },
    {
        "id": 4,
        "code": "CODE5",
        "name": "Medication 6",
        "weight": 45.0,
        "image": null,
        "drone": null
    }
]
```

## How to run the project
### Prerequisites
To run the project, you will need to have the following installed:
- Docker
- Git

### Installation
Clone the project repository from GitHub:
```bash Copy code
git clone https://github.com/jumba2010/drone-service.git

```
Change into the project directory:

```bash Copy code
cd drone-service

```

Build the Docker image:
```bash Copy code
docker build -t drone-service .

```

Run the Docker container:
```bash Copy code css
docker run -p 8085:8085 drone-service

```

Your application will be available on http://localhost:8085 . You can start performing the apis call


## Contributors

- [@Judiao Mbaua](https://github.com/jumba2010)

