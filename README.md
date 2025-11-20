学習用です

render.comで起動する時はpostgreSQL、エクリプスで起動する時はH2を使う
postgreSQLは主キーを自動生成しないので、エクリプスで起動する時はH2用に主キーを自動生成する

書き換えるファイルと場所
●build.gradle
	dependencies {
	runtimeOnly 'com.h2database:h2' //エクリプスで動かす時はH2を有効にする
	runtimeOnly 'org.postgresql:postgresql:42.7.3' //rendaer.comで動かす時はpostgreSQLを有効にする
	
●application.properties
	# メモリ型DB、エクリプスで起動する時はH2を有効にするため下の4行を有効にする
	spring.datasource.url=jdbc:h2:mem:testdb
	spring.datasource.driverClassName=org.h2.Driver
	spring.datasource.username=sa
	spring.datasource.password=
	
●DataLoader.java
	エクリプスで起動する時
	@Componentを有効に
	//c.setId(Long.parseLong(arr[0]));　主キーを読み込む部分7ヶ所コメントアウトする
	//r.setId(Long.parseLong(arr[0]));
	//s.setId(Long.parseLong(arr[0]));
	//f.setId(Long.parseLong(arr[0]));
	//c.setId(Long.parseLong(arr[0]));
	//a.setId(Long.parseLong(arr[0]));
private void loadJpAirports()のtry-catch文の //a.setId(Long.parseLong(arr[0].trim()));も無効にする

●TourDataLoader.java
	エクリプスで起動する時
	@Componentを有効に
	//to.setId(Long.parseLong(arr[0]));　主キーを読み込む部分はコメントアウトする
	
●Entityの@GeneratedValue
エクリプスで起動する時は8ヶ所有効にして主キーを自動生成する
	Airport.java
	Country.java
	Food.java
	JpAirport.java
	Region.java
	TouristSpot.java
	TourEntity.java
	Concept.java
	
※起動時に無関係なので触らない
	Favorite.java
	User.java
	FlightBooking.java
	RegionAirport.java
