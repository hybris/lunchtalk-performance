## Agenda

  - Create hystrix command:
    - Set command name.
    - Set command group.
    - Configure thread pool.
  - Setup hystrix.stream endpoint.
  - Setup hystrix dashboard.
  - Connect application to dynatrace.
  - Create dynatrace graph:
    - Memory usage.
    - GC suspension.
  - Create memory dump.

## Project info

It gets status of document repository http://repository-v2.dev.cf.hybris.com/internalstatus and returns ok/nok as json response.
To run project from root directory execute:
	
	mvn tomcat7:run 

## Step by step

 - Clone [GIT Repository](https://github.com/hybris/lunchtalk-performance)
 - Add hystrix maven dependencies [hystrix-metrics-event-stream](http://mvnrepository.com/artifact/com.netflix.hystrix/hystrix-metrics-event-stream/1.4.0-RC5)
 - Create hystrix command:
   - See [https://github.com/Netflix/Hystrix/wiki/How-To-Use](https://github.com/Netflix/Hystrix/wiki/How-To-Use)
 - Add [hystrix metrics stream endpoint](https://github.com/Netflix/Hystrix/tree/master/hystrix-contrib/hystrix-metrics-event-stream) to web.xml.
   - Check if stream displays data at [http://localhost:8080/hystrix.stream](http://localhost:8080/hystrix.stream)
 - Start [hystrix dashboard](https://github.com/Netflix/Hystrix/tree/master/hystrix-dashboard)
   - connect your stream to hystrix dashboard
 - Add dynatrace agent to your application. See [some hints](https://wiki.hybris.com/display/prodandtech/How+to+set+up+DynaTrace+with+application+on+CloudFoundry#HowtosetupDynaTracewithapplicationonCloudFoundry-Hints)
   - Export JAVA_OPTS 
    ```sh
	// please update client path and your name
	export MAVEN_OPTS="-agentpath:lib/libdtagent-6.0.0.so=name=heartbeat-service_YOUR.NAME,server=10.10.70.69,wait=45,transformationmaxavgwait=256,storage=."
	mvn tomcat7:run
	```
  - You should see dynatrace logs in the console
 - login to dynatrace client and check if your agent is connected
 - add dashboards. See [some hints](https://wiki.hybris.com/display/prodandtech/Framefrog+dynaTrace+configuration)
 - create memory dump