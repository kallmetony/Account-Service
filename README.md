
# Account Service

REST service for company to handle employees' salaries and authorities.  

### Info
All regitred emails must end with @acme.com, this can be changed in   
User entity at field email   
Service also has breached passwords table in the database. 
## Tech Stack

**Spring Boot**  

**Spring Security**

**Spring Data JPA**

**Project lombok**

**MySQL database**

## Requests and authorization

Security requirements based on the **ASVS**

|                             | Anonymous | User | Accountant | Administrator | Auditor |
| :-------------------------- | :-------- | :--- | :--------- | :------------ | :------ |
| `POST api/auth/signup`      | +         | +    | +          | +             | -       |
| `POST api/auth/changepass`  | -         | +    | +          | +             | -       |
| `GET api/empl/payment`      | -         | +    | +          | -             | -       |
| `POST api/acct/payments`    | -         | -    | +          | -             | -       |
| `PUT api/acct/payments`     | -         | -    | +          | -             | -       |
| `GET api/admin/user`        | -         | -    | -          | +             | -       |
| `DELETE api/admin/user`     | -         | -    | -          | +             | -       |
| `PUT api/admin/user/role`   | -         | -    | -          | +             | -       |
| `PUT api/admin/user/access` | -         | -    | -          | +             | -       |
| `GET api/security/events`   | -         | -    | -          | -             | +       |



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
        "salary": "<long value>"
    },
    {
        "employee": "<user1 email>",
        "period": "<mm-YYYY>",
        "salary": "<long value>"
    },
    ...
    {
        "employee": "<userN email>",
        "period": "<mm-YYYY>",
        "salary": "<long value>"
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
    "salary": "<long value>"
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
  PUT api/admin/user/role
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


### Lock/unlock user

```
  PUT api/admin/user/access
```
#### Request body
```json
{
   "user": "<String value>",
   "operation": "<[LOCK, UNLOCK]>" 
}
```

#### Description
Locks or unlocks specified user account


### Get all logs

```
  GET api/security/events
```

#### Description
Returns a list of all logs.

## Requirements 
* Java 11 or higher
## Run

#### 1. Download .jar file from releases

#### 2. Open cmd and navigate to downloaded .jar file

```
cd <path>
```

#### 3. Run jar

```
java -jar account-service.jar
```