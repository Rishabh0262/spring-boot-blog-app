
### spring-boot-starter-data-jpa dependency
automatically create the data source & it will refer the **MySQL database properties** from _"src/main/resource/**application.properties**"_


### SB project Architecture

**[postman-Client]** <-------_(GET, POST, PUT, DELETE)_------> **[CONTROLLER(api-layer)]** <----------> **[SERVICE(business-logic)]** <--------> **[DAO-repository(persistence-logic)]** <-------_(JDBC Driver & URL, Username, password)_------> **[DB]**




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
// JPA annotations to map over JPA entity with MySQL database table.  
@Entity : to specify the class as Entity  
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
####
Create '.java' **interface** files in repository package.  

all we need to do is **_Extend the interface with JpaRepository<Post, Long>_**  
##### Note : we don't need to use @Repository here, bcz **JpaRepository** > SimpleJpaRepository > @Repository & @Transactional ... internally as it all
      interface RepositoryName extends JpaRepository<Table-name/Entity-name, Long> {

         //    We don't need to write any code here.
         // JpaRepository<Post, Long> impicitly has all the required code 
    
      }
---

## Creating custom expecptions
before creating REST-API for our Post-resource, we will create custom expecptions 

### ResourceNotFoundException

Create a ResourceNotFoundException.java in exception package.  refer the file... for better understanding.

extent it to **RuntimeException**


##### @ResonseStatus 
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


## Create post REST-API
_ref. pic-5_create_post_rest-api_

1. create an **interface** _PostService.java_, in _**service**_ (parallel to "impl" package).  
**ref.** : service/PostService.java  


2. create an implementation class file. **PostServiceImpl.java** in "service/impl/". Which implements PostService.java(IF)  
**ref.** : PostServiceImpl.java

   1. **Note :**  since it has only 1 Constructor, we can omit usage of **@Autowired** (which we use with constructor declaration).  
   2. Create a createPost() - method
      1. Convert DTO to entity.
      2. Save newly created entity(from above).
      3. Convert entity to DTO.
   
3. Create a **controller** for Post.
   1. Create a respective-service object. Like here, PostService - bcz we need to use the postService.createPost().
   2. constructor generation.
      1. **NOTE:** If you are configuring a class as a Spring-bean &  it has only 1 constructor.
         then @Autowired is not required.
   3. @PostMapping  
   **ref.** : post 


## GET All posts REST-API
_ref. pic-get_all_posts_rest-api_

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
_ref. pic-get_post_by_id_rest-api_

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

---  

for more details refer , check **application.properties** for project configuration.
























