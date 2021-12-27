# Task1_Weather_API_Test

<b> Project Installation Guide</b>\
Environment: JDK1.8.0_311 \
IDE: IntelliJ IDEA \
Dependencies: Rest Assured, TestNG, JSON, hamcrest

The project should work just by cloning the repository and running on your IDE. 

<b> Potential issue with the HTTP certificate.</b> \
You may need to: add the certificate to JDK cacerts keystore \
Certificate file is already saved in the project in: \src\main\resources \
Locate the cacert file in: $JAVA_HOME/lib/security \ 
Run KeyTool on command prompt as user with permission \
keytool -import -storepass changeit -noprompt -alias test -keystore cacerts -trustcacerts -file [copy certificate file path] \
