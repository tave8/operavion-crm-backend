

# Endpoints - overview

```

/auth
    POST /login                         (login)
    POST /register                      (create new profile/account)
    GET  /verify-email/:code            (mark the account with this email as verified)
    POST /forgot-password/request       (is this email authorized to set a new password?)
    POST /forgot-password/verify  (can this email set a new password right now?)
    POST /forgot-password/reset         (set a new password right now)

/users
    GET /me               (get my profile)
    PUT /me                (update my profile)
    POST /me/avatar-image     (upload my new avatar image)


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

### POST /register

Request 

```
email: str
password: str
firstname: str
lastname: str
```

Response

```
userId: str
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



# Custom Authorization System (multi-tenant)


```

companies

admins

normal users

custom roles 

pages

actions
 
----- 

1 company --has--> N users

1 user --associated to--> 1 company

1 admin --creates--> N custom roles

1 custom role --assigned to--> N normal users

1 normal user --is assigned--> 1 custom role

1 page --has--> N actions

1 action --associated to--> 1 page

1 custom role --associated to--> N pages

1 custom role --associated to--> N actions


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