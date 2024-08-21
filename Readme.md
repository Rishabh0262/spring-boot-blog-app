
### spring-boot-starter-data-jpa dependency
automatically create the data source & it will refer the **MySQL database properties** from _"src/main/resource/**application.properties**"_


### SB project Architecture

**[postman-Client]** <-------_(GET, POST, PUT, DELETE)_------> **[CONTROLLER(api-layer)]** <----------> **[SERVICE(business-logic)]** <--------> **[DAO-repository(persistence-logic)]** <-------_(JDBC Driver & URL, Username, password)_------> **[DB]**

## Annotations
@RequestBody : Converts the request JSON obj into Java obj.




#### Create Controller package
src/main/java/com.gtech. ... _api/ => 
1. "**contorller**" : spring MVC Controller classes.
2. "**entity**" : keeps all the JPA entities (entity/model/domain).
3. "**service**" : to keep all the Service Interfaces.
   1. (pkg) "**impl**" : Service classes .
4. "**utils**" : 
5. **"repository"** : keep all the Spring Data JPA repos
6. **"exception"** : for custom exceptions
7. **"payload"** : for sending Entity response as DTO. 


## Configure MySQL database

1. Open MySQL workbench.
   1. create database myblog;
   2. set DB-myblog as default Schema : By right clicking on "myblog"

## Creating JPA entity
#### Entity means the SB will create the Table of that Entity-Name, in MySQL workbench. We'll not require to go to MySQL workbench and create in manually.
create .java files in entity package.

@Data : [@Getter, @Setter, @RequiredArgsConstructor @ToString, @EqualsAndHashCode]  
@AllArgsConstructor :  
@NoArgsConstructor :  
// JPA annotations to map over JPA entity with MySQL database(DB) table.  
@Entity : to specify the class as JPA Entity  
@Table : to give a name to table  
@Id : to specify primary key to our entity  
@GeneratedValue : to specify primary key generation strategy.

---


   
      @Data
      @AllArgsConstructor
      @NoArgsConstructor

      @Entity
      @Table(
         name="classname-diff", // if we ignore mentioning "name", SB automatically takes the Entity-name[ClassName] as the Table-name 
         uniqueConstraints = {@UniqueConstraints(columnNames={"title"})})
      ClassName {
         @Id
         @GeneratedValue ( strategy - GenerationType.IDENTITY)
         private Long varName1; // this will be used as primary key

         @column(name="var2", nullable=false)    // name to be taken as the col-name in the Table.
         varName2;

         @column( nullable=false)	// if name is not specified. It'll auto consider the varName
         varName3;
         .
         .
         .
      }
---
## Creating JPA repository for PostRepository
#### Create '.java' **interface** files in repository package.  

all we need to do is **_Extend the interface with JpaRepository<Post, Long>_**  
##### Note : we don't need to use @Repository here, bcz **JpaRepository** > SimpleJpaRepository > @Repository & @Transactional ... internally as it all
      interface RepositoryName extends JpaRepository<Table-name/Entity-name, typeof_primary-key> {    // type of entity we want to use(i.e, Post) , type of etity's primary-key 

         //    We don't need to write any code here.
         // JpaRepository<Post, Long> impicitly has all the required code 
    
      }
---

## Creating custom exceptions
before creating REST-API for our Post-resource, we will create custom exceptions 

### ResourceNotFoundException

Create a ResourceNotFoundException.java in exception package.  refer the file... for better understanding.

extent it to **RuntimeException**


##### @ResponseStatus 
// this cause SB to respond with the specified HTTP status code whenever this exception is thrown from the controller.
      
Inside **ResourceNotFoundException-constructor**, we've used **_super_** keyword  
      
      super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue)); 
      // we need to pass msg to superclass constructor using the super keyword.

---
## Creating DTO class
_ref. pic-4_DTO_SB_  
### Using DTO's in SB Application


[postman-Client] <-------(DTO's)-----(json)------> [CONTROLLER(api-layer),(PostDto(@RequestBody))] <----------> [SERVICE(business-logic)] <--------> [DAO-repository(persistence-logic)] <----> [DB]

      (json) : { "title" : "My new post",
                  "description" : "post descripton",
                  "content" : "This is my new post"
               }


#### 1st approach

client send the Req. to REST-API(controller)=> sevice => DAO => DB  
Here REST-API, basically sends JPA Entity as a response to client.  

##### cons :
As exposing JPA entities via REST endpoints leads to _security issues_


#### 2nd approach [preferred]

client send the Req. to REST-API(controller)=> sevice => DAO => DB
Here REST-API, convert JPA Entity into DTO obj.  response to client.

Pros : 
helps in hiding implementation details of JPA entities.

1. Creating new pkg as "payload"
2. .java as DTOs (for now we'll use **_PostDto.java_**)
3. Inside it, we'll just use all the variables we used in Post-entity
4. @Data , for getter,setter, constructor, etc...


---

[postman req] => json -> DTO =>   
[SB-REST-API] => DTO -> entity =>   
[save to DB] => entity -> DTO =>  
[res. to postman] => DTO -> json  

---
## CRUD
### Annotations : @PostMapping, @GetMapping, @PutMapping, @DeleteMapping
respectively

## Create post REST-API
_ref. pic-5_create_post_rest-api_
#### service-pkg
1. create an **interface** _PostService.java_, in _**service**_ (parallel to "impl" package).  
**ref.** : service/PostService.java  


2. create an implementation class file. **PostServiceImpl.java** in "service/impl/". Which implements PostService.java(IF)  
**ref.** : PostServiceImpl.java

   1. **Note :**  since it has only 1 Constructor, we can omit usage of **@Autowired** (which we use with constructor declaration).  
   2. Create a createPost() - method
      1. Convert DTO to entity.
      2. Save newly created entity(from above).
      3. Convert entity to DTO.
      
#### Controller-pkg
3. Create a **controller** for Post.
   1. Create a respective-service object. Like here, PostService - bcz we need to use the postService.createPost().
   2. constructor generation.
      1. **NOTE:** If you are configuring a class as a Spring-bean &  it has only 1 constructor.
         then @Autowired is not required.
   3. @PostMapping  
   **ref.** : PostController.java


## GET All posts REST-API
_ref. pic-6-get_all_posts_rest-api_

First we'll go to service layer, then we'll go to controller layer.

1. In PostService.java => we declare getAllPost() method
2. in PostServiceImpl.java => @Override getAllPost() method
   1. use PostRepository obj. for fetching the data.
   2. use **return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());**  
      to convert entity-to-DTO.
3. In PostController.java 
   1.  @GetMapping : getAllPost()
   2. return the response as ResponseEntity

---

## GET post by ID REST-API
_ref. pic-7-get_post_by_id_rest-api_

1. In PostService.java => we declare getPostByID(long id) method

2. in PostServiceImpl.java => @Override getPostByID(long id) method
   1. use PostRepository obj. for fetching the data.
   2. postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
   3. use **return PostDto-obj**  ,  using the convert method.
      to convert entity-to-DTO.
   
3. In PostController.java
   1.  @GetMapping("/{id}") : getPostById()
   2. return the response as ResponseEntity

---

## UPDATE post REST-API
_ref. pic-8-update_post_rest-api_

1. In PostService.java => we declare updatePost(long id) method

2. in PostServiceImpl.java => @Override updatePost(long id) method
   1. use PostRepository obj. for fetching the data.
   2. postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
   3. use **return PostDto-obj**  ,  using the convert method.
      to convert entity-to-DTO.

3. In PostController.java
   1.  @GetMapping("/{id}") : updatePost()
   2. return the response as ResponseEntity

---

## DELETE post REST-API
_ref. pic-9-delete_post_rest-api_

1. In PostService.java => we declare "**void deletePost(long id)**" method

2. in PostServiceImpl.java => @Override deletePost(long id) method
   1. use PostRepository obj. for fetching the data.
   2. Post _post_ = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
   3. use **postRepository.delete(_post_)**
      this will delete an entity from DB(as if it has reference of it).

3. In PostController.java
   1.  @DeleteMaping("/{id}") : deletePost()
   2. return a Msg as ResponseEntity


## Pagination Support for GET all Posts REST-API : paging & sorting
_ref. pic-10-pagination_get-all-post_rest-api_
for URL : http://localhost:8080/api/posts?pageSize=5&pageNo=1

1. PostController.java => we'll modify getAllPost()
   1. getAllPosts(
         @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
      )
2. PostService.java => Modify the same as getAllPosts(int pageNo, int pageSize)
3. PostServiceImpl.java => Modify getAllPosts() as getAllPosts(int pageNo, int pageSize)
   1. Pageable pageable = PageRequest.of(pageNo, pageSize);
   2. pass **pageable** , in .findAll(_pageabel_), change the return-type to Page<Post>,
   3. to get List<Post>, we'll use posts.getContent();
   4. return the List<post> obj.




Pagination requires :

1. Payload => PostResponse.java  (+ @Data, @AllArgsConstructor)
   1. private List<PostDto) content, int pageNo, int pageSize, long totalElements, totalPages, boolean last.
   
2. PostServiceImpl.java : contruct the response.
   1. getAllPost(...)  returnType -> PostResponse
   2. PostResponse obj. , map all its attributes from Page<Post> obj.(posts)
   
3. PostController.java : change the (getAllPosts) GetRequest's returntype 


### sorting
http://localhost:8080/api/posts?pageSize=5&pageNo=1&sortBy=title     (title/id/description/content)... any of the attributes can be used for sorting.

1. PostController.java
   1. Add another Req. Param. for the **sortBy**.
   2. Add the sortBy var. as argument in _getAllposts(...)_

2. PostService.java : include sortBy
3. PostServiceImpl : inlcude sortBy-string in method args.
   1. pass the 3rd parameter as Sort.by(...)  // Sort from "...data.domain", use ".by()" method having String as a passing arg.
   2. Pageable pageable = PageRequest.of(pageNo, pageSize, **Sort.by(sortBy)**);
   3. for DESC : **Sort.Order.desc()** OR **Sort.by(sortBy).descending()**
   


### Sorting in Desc. dynamically

1. PostController.java
   1. Add another Req. Param. for the **sortDir**.
   2. Add the sortDir var. as argument in _getAllposts(...)_
2. respective changes in _PostService.java_ & _PostServiceImpl.java_
3. created a Sort obj. using ternary operator.


## Defining all the Hardcoded values in util-pkg : AppConstant.java
1. Defined Request_parameter values.





-----






## Sec. 9 : Building CRUD REST API's for Comment Resounrce (One to many)

### Creating JPA Entity - Comment
means DB-table creation.  

1. entity-pkg : Comment.java 
   1. id, name, email, body; @Data, @AllArgsConstructor, @NoArgsConstructor
   2. @Entity, @Table(name = "comments")   :  create A table in DB
   3. Inside class Comment 
      1. @id, @GenratedValue(strategy = GenerationType.IDENTITY)
      2. @Column(...) is not needed, as we are using the variable name as same as col. name.
      4. To Mark **many-to-one** realtionship   for multiple Comments belong to 1 post.
         1. **@ManyToOne**(fetch = FetchType.LAZY)           // create a bi-directional relationship b/w  (N)Comments & (1)Post.  
         2. @JoinColumn(name = "post_id", nullable = false)  // _this way we have made the below "Post" obj. as "post_id as **Foreign key** in Comments table._  
            **_private Post post;_**
            1. FetchType.LAZY tells Hibernate to only fetch the related enetities from the DB when we use the relationship.

2. entity-pkg : Post.java
   1. use **@OneToMany** , means **1 post** has **many comments**
   2. Arguments - 
      1. mappedBy : "post"
      2. cascade : CascadeType.ALL
      3. orphanRemoval : true  // If post is deleted, associated comments will be deleted as well. 
   3. private Set<Comment> comments = new HashSet<>();

### Creating JPA Repository - CommentRepository
#### Create '.java' **interface** files in repository package.

`for more details, refer Post-entity's JPA-Repository`

1. repository pkg : 
   1. Create CommentRepository.java _interface_
   2. Extends **JpaRepository** : access to all the CRUD methods for Comment-entity
      1. All the SQL query methods.
      2. Paging & sorting
      3. CRUD methods : CrudRepository



### Creating DTO class - CommentDto

1. payload pkg : 
   1. CommentDto class
      1. long id;
      2. String name,email, body;



---

### Create Comment REST-API : service
_ref. pic-5_create_comment_rest-api_

1. pkg service
   1. CommentService - interface
   2. 
2. service/impl 
   1. CommentServiceImpl-class : 
      1. Created a CommentRepository - obj. 
      2. Generate a Constructor of it. **can Omit @Autowired** as this class has only one data member/var.
      3. Entity-to-DTO & DTO-to-entity functions for mapping.
      4. retrieve _post-entity_ by ID.
         1. For which, we need to use created PostRepository-obj as well.
         2. Include it into Constructor as well.
         3. fetch the post by ID. using the post-repo-obj
      5. Set post to comment entity.
      6. Save Comment-entity to DB.

##### Ref. : Controller-pkg line of [Create Post REST-API]
3. contoller-pkg
   1. CommentController-class
      1. Obj. of CommentService is created.
      2. Constructor with agr is created with the same.
      3. Now follow the REST-API Mapping routine.
      4. 


---

## Get all comments by post

1. repository : create custom query method to fetch post-by-id.
`List<Comment> findByPostId(long id);`

2. service, serviceImpl : 
   1.create the required method declaration.
   2. Implement the method in Impl.
      1. used the custom repo. method[findByPostId] to get the 

3. controller : 


---

## Get comment by ID

1. serice, serviceImpl
   1. Impl => we'll use _BlogAPIException_  if **comment's=>post_id != postID**

#### BlogAPIException 
We throw this exception whenever we write some business logic or validate request parameter.

2. exception-pkg : BlogAPIException _extends_ RuntimeException
   1. vars : HttpStatus-> status, string-> msg
   2. Generate respective : getters, constructors with all Args(), with all Args(string)
      1. choosing 2 superclass constructor... : with no args, with 1 msg Args.
3. 



---  

## Update comment by ID
1. serice, serviceImpl

2. Fetched both, comment & post from DB using IDs
3. change the  values of current comment using setter with the passed values.
4. commentRepo's .save()    & this method will send the updated Response.
5. return the new updated commment.
6. 

---

## Delete comment by ID

Similar step.  
except we'll use commentRepo.delete(commentEntity);

---


## Section 10 : Using ModelMapper - Map Entity to DTO and Vice-versa
      it helps in mapping from 1 obj. to another obj. automatically.
Google : "model mapper maven" => latest release => **Copy** Maven code. paste it into POM.xml
1. Add ModelMapper Library dependency.
2. Define the ModelMapper Bean in our Spring Config.
   1. SpringBootBlogRestApiApplication.java -> 
      1. ModelMapper **@Bean**  : returns a ModelMapper-obj.
      
3. Inject & use ModelMapper : to convert 1 object to another object. (e.g, DTO <-> Entity)
   1. Service-Impl : ModelMapper-obj
   2. Use it in the service-constructor, just like we create all-args-constructor. [constructor based dependency-injection]
   3. E.g. : We need to convert PostDto => Post, using mapToEntity().
      4. post = **mapper.map(postDto, Post.class);**      // choosing : .map(obj src, obj dest.)
      5. This way we won't be needing to write a mapping part of each attribute.


### Map Post Entity to Post DTO using ModelMapper


### Map Comment Entity to Comment DTO using ModelMapper

### Refactoring GetPostById & GetAllPosts REST API




























---  

for more details refer , check **application.properties** for project configuration.
























