### 개요
스프링 프레임워크에서 제공하는 [AbstractRoutingDataSource](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/datasource/lookup/AbstractRoutingDataSource.html)를 사용해 코드수정 없이 읽기전용 트랜잭션을 Slave 데이터베이스에서 처리하도록 한다.

참고
* [dynamic-datasource-routing](http://spring.io/blog/2007/01/23/dynamic-datasource-routing/)
* [Anyframe Routing DataSource Plugin](http://dev.anyframejava.org/docs/anyframe/plugin/optional/routingdatasource/1.0.0/reference/htmlsingle/routingdatasource.html)

### 동작방식

[MultitenantDataSourceRouter](https://github.com/iyboklee/boot-multitenant-datasource/blob/master/src/main/java/com/github/iyboklee/config/mtds/MultitenantDataSourceRouter.java)는 Service 객체에서 @Transactional 어노테이션을 사용한는 public 메소드에 대한 Around Advice를 구현한다.

* MultitenantDataSourceRouter를 다른 어떤 AOP 설정보다 먼저 적용하기 위해 HIGHEST_PRECEDENCE 정렬순서를 지정한다.
* @Transactional 어노테이션이 읽기전용 속성이라면, 트랜잭션이 Slave 노드에서 처리될 수 있도록 데이터베이스 선택 정보를 ThreadLocal 저장소에 설정한다. 
* 그 외 모든 트랜잭션은 쓰기 가능한 Master 노드에서 처리된다.
* Service 메소드가 리턴할 때, ThreadLocal 저장소에 저장된 데이터베이스 선택 정보를 초기화한다.

### 테스트

customers라는 테이블을 지니는 간단한 데이터베이스를 Master, Slave 노드에 생성한다.

```
CREATE TABLE `customers` (
  `seq` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `regtime` datetime NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

```

테스트를 위해 Master 노드와 Slave 노드에 **이메일을 제외하고 동일한 값**을 갖는 row를 각각 insert한다. 이것은 테스트를 위해 동일한 데이터베이스 스키마를 갖는 2개의 데이터베이스를 대상으로 Master, Slave 구조를 흉내내는 것이다. 실제 운영환경에서는 Master 노드의 값이 Slave 노드에 자동으로 복제되며, Slave 노드에서 임의로 값을 수정할 수 없다.

```
-- Master 노드에서
INSERT INTO `customers` VALUES ('1', 'tester1', 'tester1@gmail.com', '2017-07-01 00:00:00');
-- Slave 노드에서
INSERT INTO `customers` VALUES ('1', 'tester1', 'tester1@hotmail.com', '2017-07-01 00:00:00');
```

* 쓰기 속성을 지닌 트랜잭션을 실행하면 (customerService.findOne 메소드) 이메일값은 **tester1@gmail.com** 이다.
* 읽기 속성을 지닌 트랜잭션을 실행하면 (customerService.findOneWithReadOnly 메소드) 이메일값은 **tester1@hotmail.com** 이다.

@Transactional 어노테이션의 읽기전용 속성에 따라 Master, Slave 노드가 정확하게 선택되는 것을 확인할 수 있다.

### 제약사항

* AbstractRoutingDataSource를 통해 통합되는 DataSource는 동일한 데이터베이스 스키마를 가져야한다.
  * 일반적으로 Master, Slave 노드는 동일한 데이터베이스를 지니기 때문에 큰 문제는 아니다.
* 한번 트랜잭션이 시작되면 중간에 데이터베이스를 변경할 수 없다. 즉, 트랜잭션이 읽기 전용으로 Slave 노드에서 시작되었다면, 중간에 쓰기연산을 위해 Master 노드로 변경할 수 없다.
* 데이터베이스 노드 선택을 위한 정보를 ThreadLocal 저장소에 저장하기 때문에 모든 작업 종료 후 반드시 ThreadLocal 저장소를 초기화 해야한다.