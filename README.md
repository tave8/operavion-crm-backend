# Run the app 

You can run this app with and without Docker. 

This has implications on where the data is stored.

The current behavior is that when you run the app with Docker, the postgres docker container will be used (i.e. the volume on your machine).

Other considerations: 
- When you run the app with Docker, you shouldn't do anything else, because the images (JDK, postgres) will be pulled automatically and then run.
- When you run the app without Docker, you need to have a postgres process running.
- There can be two postgres databases on your machine: One as a postgres container, the other as pure postgres. 
  Depending on how you run the app, your app (i.e. the java server) will connect to one or the other. 

## With Docker 

Here's how to run this java server with Docker.

All you need to do is build the JAR and then run Docker Compose.

Run these commands:

```
./mvnw clean package -DskipTests

docker compose up --build
```

The only likely issues you might encounter is port mapping, specifically: A host port specified in Docker Compose is alread busy on your host.

For that you need to manually change the ***host port only*** in your local Docker Compose file.

## Without Docker

You can also run this app without Docker. Depending on your needs, you might need to:
- Tinker with the host ports so that you find one that is free on your host, for the java server and/or postgres.
- If you had a volume on your host, this volume might need to be exported to your local postgres.



# Troubleshooting - Containerization

Here are issues I encountered and solved, while containerizing the app with Docker.

### Postgres creates an anonymous volume

If you don't specify one, Postgres will create an anonymous volume for you.

Thus, the expected behavior "on container restart, data is lost" is incorrect.

Postgres does create a volume for you. 

### Postgres volume without /data

The container directory to be mapped to the volume directory, is `/var/lib/postgresql`.

When specifying the volume you will thus have: `postgres-data:/var/lib/postgresql`

And of course create the volume: 

```
volumes:
  postgres-data:
```

### Conflicting host ports for database 

Previously, Postgres was at `localhost:5432`.

This was the postgres process, without Docker.

Then postgres was containerized, and I had to assign a new host port to this new postgres container.

Which is why you find the new postgres docker container at `localhost:5433`.

In short:

- At `localhost:5432` you find the pure postgres process (the initial database)
- At `localhost:5433` you find the postgres docker container

You can thus run this app (java server) by connecting to one or the other database.

Of course, you can now access the postgres docker container with a database client like DBeaver, pgAdmin etc.

### Manually build JAR with Maven

Every time you make a change to the project, before running the containers, you must manually run `./mvnw clean package -DskipTests`.

Initially I forgot that there's no automatic build process to automatically build the project into a JAR.


### Rebuild images for each Docker Compose

When you make any change to the docker files, it might not pick up that change.

So you must rebuild the docker files every time.

You do this by specifying the `--build` flag with Compose.

Here's the full command: `docker compose up --build`.

Initially I forgot that Docker caches image layers.

### Database first migration

Previously, the app could not be started properly, because the first database migration was missing.

This problem has been fixed, so you should have no database migration-related problem.

I fixed this by restarting the postgres container and deleting the volume every time, until tinkered until I found the right "first migration" schema that did not cause conflicts.

[See old problem description](#launching-old-problem-reference-only).


## Claude - AI

This was my prompt that instructed Claude to generate the project's overview.

```
analyze this project. explain conventions, how it works, naming & architecture patterns, 
features, project's scope & topic, developer skill level & mentality. 
this shall be a reference to hiring managers evaluating whether to hire me, and to developers looking at the code. 
assess your reasoning against my following  initial goal for the project: building the "reusable core" of a CRM software, 
solve the most common problems and take inspiration of solved problems for future projects. 
take into account that what I've left incomplete are probably things i'm improving at (for example, mocking in testing) or 
that I feel like it's not the right time to refactor or build on top of, so it's okay as it depicts what to improve next. 
include some stats like: number of commits, project's time span, number of files, number of features & APIs and what you think is relevant. 
include some stats about the developer. state the most important things first, less important things last. 
be objective and keep in mind the target audience.
```



## Conclusion


Note: I have **not** tried these solutions. This is a reference to my future self. 

If this project will be continued, you won't even see this message and this configuration impasse will have been fixed.



# Configuration

Run/Edit configuration. Spring must be in "local" profile when in local environment?



# Forgot password

Here I discuss the Forgot Password mechanism.

1. To set a new password, the user needs to provide their email.

2. After they receive an email, they click the link in the email

3. This link will be verified, and if valid, the user will be able to set a new password

```

```


# Cron Job System

The Cron Job System, for now, only supports 1 replica.

With more replicas, the cron jobs would be fired at the same time.

Therefore, in this version, these are the assumptions:

- 1 replica only (1 server)
-




# Stripe API local configuration

Note: If Stripe doesn't seem to work locally, it's likely because you haven't set up
the local listener. Set up the local listener with the Stripe CLI.
This way, Stripe will forward the test events to my localhost, through the CLI.

`stripe listen --forward-to localhost:3001/webhooks/stripe`

`stripe trigger payment_intent.succeeded`



# Railway CLI

`railway login` (then login)

`railway link` (then choose project, environment, service etc.)

`railway logs` (stream service deployment logs)

`railway deployment list` (see list of service deployments)

`railway logs --build`

`railway link --project zerochiamate --environment preview --service server`

`railway link --project zerochiamate --environment production --service server`

`railway agent -p "can you see anything wrong with my deployment for my 'server' service?"`


****

# Endpoints - overview

```

/auth
    POST /signup                        (create new company + user with role admin)
    
    POST /login                         (login for manager)
    POST /login-operator                (login for operator)
    
    POST /reset-password-first-login    (reset password at first login)
    
    GET  /verify-email/:code            (when user clicks link in email - mark the user with this email as verified)
        
    POST /forgot-password/request       (is this email authorized to set a new password?)
    POST /forgot-password/verify        (can this email set a new password right now?)
    POST /forgot-password/reset         (set a new password right now)

/users
    GET /me               (get my profile)
    PUT /me                (update my profile)
    POST /me/avatar-image     (upload my new avatar image)
    GET /                   (get my users, aka my team)
    POST /                  (add a user, only admin authorization)


/clients
    GET /                   (get my clients)
    POST /                  (add a client)
    GET /addresses                (get client addresses of my company)
    GET /:clientId/addresses     (get addresses of this client)
    POST /:clientId/addresses    (add the address to this client - address in body)
    
    
/client-addresses
    POST /:clientAddressId/checklists/:checklistId          (add a checklist to this client address)
    POST /:clientAddressId/contract-expectations               (add contract expectations to client address)


/shifts/operators                    (find operators without shifts)
    ?from
    ?to
    ?hasShifts=true|false
 
 
/operators/me/shifts               (find my shifts)
    ?from
    ?to

/operators/:operatorId/shifts/availability       (get the operator's availability)
    ?date

/operators/:operatorId/shifts/conflicts           (get the operator's conflicts)
    


/tasks-completion
   


/tasks
    GET  /                        (get tasks of my company)
    POST /                       (add a task of my company)
        

/checklists
    GET  /                        (get checklists of my company)
    POST /                        (add a checklist, add checklist entries. the payload contains an array of task id's)

    
/operators/:userId/shifts         (find shifts by operator)
    
    
/client-addresses/:clientAddressId/checklists   (get checklists by client address)
    
    

/shifts                                 # shifts
    ?from
    ?to
    
    GET /:shiftId/operators             # who works in this shift
        ?from
        ?to 

    GET /operators/:userId              # shifts of this operator
        ?from
        ?to

    GET /client-addresses/:clientAddressId   # shifts at this address
        ?from
        ?to

    POST /                              # create a shift
    PUT /:shiftId                       # update a shift (missing)
    DELETE /:shiftId                    # delete a shift (missing)
    POST /:shiftId/operators            # assign operator to shift
    DELETE /:shiftId/operators/:userId  # remove operator from shift



/geocoding           
    GET /autocomplete     (returns a list of addresses given a query)  
    



/notifications
    GET /                  (get my notifications)    
    PATCH /read                   (mark many notifications as read)    
    PATCH /:notificationId/read     (mark notification as read)


/ai
    /extract           
        POST /cv            (extract a CV)
        POST /contract-expectations      (extract expectations from contract)             
        

    
/articles
    GET /                  (get my articles)
    GET /:articleId        (get article, requires that it's my article)
    POST /                  (add article as my profile)
    PUT /:articleId         (updates an article, requires that it's my article)
    DELETE /:articleId       (deletes an article, requires that it's my article)
    
```




# Endpoints

## /auth

### POST /login

Request

```
email: str
password: str
```

Response 

```
accessToken: str
```


### POST /login-operator

Request

```
username: str
password: str
```

Response

```
accessToken: str
```

### POST /signup

Request 

```
legalName: str
email: str
password: str
firstname: str
lastname: str
```

Response

```
userId: str
```


### POST /first-login-password-reset

Request

```
{
    oldPassword: str
    newPassword: str
}
```

Response 

```
{
        
}
```


### POST /forgot-password/request

Can this email request to set a new password?

Request

```
{
    email: str
}
```

Response

```
{
    message: str
}
```

### POST /forgot-password/verify

Is this code authorized to set a new password?

Request

The code to verify. 

```
{
    code: str
}
```

Response

```
{
    message: str
}
```

### POST /forgot-password/reset

Set a new password for the user associated with this code,
if the code is still valid.

Request

The code to verify.

```
{
    code: str
    newPassword: str
}
```

Response

```
{
    message: str
}
```


## /geocoding

### /autocomplete

Request 

search,
lang


## /client-addresses

### POST /:clientAddressId/checklists/:checklistId       


Add a checklist to this client address.

Request 

```
{
    
}
```

Response 

```
{

}
```


## /checklists


### POST /


Request 

```
{
    name: str
    entries: [
        {
            taskId: str
            position: number
        }
    ]
}
```


Response 

```
<no body>
```



## /notifications


## GET /

Query params:

unread: true|false
read: true|false
limit: 0..N
type: <custom notification type>

```
no body
```

Response

```
list of notifications, paginated    
```


## PATCH /read

Request 
```
{
    notificationIds: str[]
}
```

Response 
```
list of updated notifications
```


## PATCH /:notificationId/read

Request
```
no body
```

Response
```
updated notification
```


## /ai


### /extract


### POST /cv

Extract a CV

Request 

```
multipart/form-data

file=...
```

Response

```
json of CV
```






## /users


### GET /me

Response

```
userId: str
email: str 
firstname: str
lastname: str
avatarUrl: str
createdAt: timestamp
```


### PUT /me


Request

```
firstname: str
lastname: str
```

Response

```
userId: str
email: str 
firstname: str
lastname: str
avatarUrl: str
createdAt: timestamp
```

### POST /me/avatar-image


Request 

Multipart/form-data

```
avatar_image
```

Response

```
userId: str
email: str 
firstname: str
lastname: str
avatarUrl: str
createdAt: timestamp
```


## /articles


### GET /


Response

```
content = [
    {
        articleId: str
        title: str
        content: str
        coverUrl: str
        createdAt: timestamp
    }
]
```



### GET /:articleId

Response 

```
articleId: str
title: str
content: str
coverUrl: str
createdAt: timestamp
```

### POST /

Request

```
title: str
content: str
```

Response 

```
articleId: str
title: str
content: str
coverUrl: str
createdAt: timestamp
```


### PUT /:articleId

Request

```
title: str
content: str
```

Response

```
articleId: str
title: str
content: str
coverUrl: str
createdAt: timestamp
```

### DELETE /:articleId



Response

```
no response
```







# Entities

```

User
    userId
    email
    password
    firstName
    lastName
    avatarUrl
    createdAt
    

Article
    articleId
    userId
    title
    content
    coverUrl
    createdAt

```


# Entity cardinality

```

1 user --has--> N articles

1 article --written by--> 1 user

```



```

1 company has 1 legal address
1 client has 1 legal address
1 client has N physical address


Client
    legalAddressId


Company
    legalAddressId


Address
    id
    address
    lat
    lon


ClientAddress
    clientId
    addressId    

```






# Launching (old problem, reference only)

**The problem: Flyway took over. Hibernate is not managing DB.**

Before launching the project, the [DB schema dump](db_schema_dump_on_project_end.sql) should be run against your existing, empty DB.

This is because at first, Hibernate ORM was managing entities & DB.

Then, Flyway (migration library) took over. In fact you can see the migration scripts.

But those migration scripts do **not** capture the DB's schema before Flyway took over;

I forgot to take a DB schema dump of the DB's schema *before* Flyway took over.

This means, you probably need to manually run the schema dump against your existing, empty DB.

If you don't do that, you should get errors saying that some tables don't exist, because Flyway, on startup,

will be executing the migration scripts, not knowing anything about the project's history and assuming your DB already has the correct schema

as it did when it first took over in my project.

Try either of these solutions (either one or the other).

## Solution A

So, before your run the server:

1. Manually create the DB.
2. Run the DB schema dump against your empty DB (execute script). This will create the DB as it is now that I've finished the project.
3. Run the server.

Not guaranteed but this should fix it. If it doesn't work, it's probably because some part of the migration script is trying to

create a table that already exists (or something like that) and will fail.


## Solution B

Temporarily re-activate Hibernate.

In [application.properties](src/main/resources/application.properties) there's a line that says:

```
# Hibernate must not touch the schema, Flyway owns it.

spring.jpa.hibernate.ddl-auto=none
```

You should temporarily re-activate Hibernate, so it creates the first schema directly from the entities.

```
spring.jpa.hibernate.ddl-auto=update
```

Check DB and make sure the schema is created. Then disable Hibernate again.

Now run the server.

Flyway will now be executing all migration scripts one by one.

If any issues comes up, it's likely due to some part of the migration script assuming some DB object that doesn't exist or that already exists.

If that migration script was **not** executed, only then can you modify it directly to fix the issue. 


