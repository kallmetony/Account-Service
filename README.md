
# Account Service

API for company to handle employees, their authorities and payrolls.  
All regitred emails must end with @acme.com  
Service also has breached passwords table in the database.
## Tech Stack

**Spring Boot**  

**Spring Security**

**Spring Data JPA**

**Project lombok**

**MySQL database**



## Requests and authorization

Security requirements based on the **ASVS**

|                             | Anonymous | User | Accountant | Administrator |
| :-------------------------- | :-------- | :--- | :--------- | :------------ |
| `POST api/auth/signup`      | +         | +    | +          | +             |
| `POST api/auth/changepass`  | -         | +    | +          | +             |
| `GET api/empl/payment`      | -         | +    | +          | -             |
| `POST api/acct/payments`    | -         | -    | +          | -             |
| `PUT api/acct/payments`     | -         | -    | +          | -             |
| `GET api/admin/user`        | -         | -    | -          | +             |
| `DELETE api/admin/user`     | -         | -    | -          | +             |
| `PUT api/admin/user/role`   | -         | -    | -          | +             |



## API

### Sign up

```
  POST api/auth/signup
```
#### Request body
```json
{
   "name": "<name>",
   "lastname": "<lastname>",
   "email": "<email>",
   "password": "<password>"
}
```

#### Description
Saves new employee in the database, password must be longer than 12 chars.  
First registred user gets administrator authorities.


### Change password

```
  POST api/auth/changepass
```
#### Request body
```json
{
   "email": "<email>",
   "new_password": "<password>"
}
```
#### Description
Saves new employees password in the database, password must be longer than 12 chars.


### Get payrolls

```
  GET api/empl/payment
```
#### Request parameters
| Parameter | Type     | Description                                  |
| :-------- | :------- | :------------------------------------------- |
| `period`  | `string` | **Not required**. Period of payment to fetch |

#### Description
Returns all payments of user that send the request, if a period is specified returns his payroll.


### Add new payrolls

```
  POST api/acct/payments
```
#### Request body
```json
[
    {
        "employee": "<user email>",
        "period": "<mm-YYYY>",
        "salary": <long>
    },
    {
        "employee": "<user1 email>",
        "period": "<mm-YYYY>",
        "salary": <long>
    },
    ...
    {
        "employee": "<userN email>",
        "period": "<mm-YYYY>",
        "salary": <long>
    }
]
```

#### Description
Adds new payrolls into database, must not be non-repetitive.


### Update payroll

```
  PUT api/acct/payments
```
#### Request body
```json
{
    "employee": "<user email>",
    "period": "<mm-YYYY>",
    "salary": <Long>
}
```

#### Description
Updates the payroll with specified period.


### Get all users and thier roles

```
  GET api/admin/user
```

#### Description
Returns a list of all registred users and thier authorities.

### Delete user

```
  DELETE api/admin/user/{email}
```

#### Request parameters
| Parameter | Type     | Description                         |
| :-------- | :------- | :---------------------------------- |
| `period`  | `@path`  | **Required**. Users email to delete |

#### Description
Deletes from database user with specified email.


### Update user authorities

```
  PUT api/acct/payments
```
#### Request body
```json
{
    "user": "<user email>",
    "role": "<uppercase role to operate with>",
    "operation": "<[GRANT, REMOVE]>"
}
```

#### Description
Updates user roles

## Requirements 
* Java 11 or higher
* Gradle 7.4.1
## Run

#### 1. Download repository files  

#### 2. Open Command Prompt or PowerShell

#### 3. Change directory to project

#### 4. Execute command

```
gradle build
```

#### 5. Navigate to jars

```
cd build/libs
```

#### 6. Run jar

```
java -jar account-service-0.5.jar
```