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


/tasks
    GET  /                        (get tasks of my company)
    POST /                       (add a task of my company)
        

/checklists
    GET  /                        (get checklists of my company)
    POST /                        (add a checklist, add checklist entries. the payload contains an array of task id's)


/geocoding           
    GET /autocomplete     (returns a list of addresses given a query)  
    



/notifications
    GET /                  (get my notifications)    
    PATCH /read                   (mark many notifications as read)    
    PATCH /:notificationId/read     (mark notification as read)


/ai
    /extract           
        POST /cv            (extract a CV)
                    
 
    
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


# Configuration

Run/Edit configuration. Spring must be in "local" profile when in local environment?