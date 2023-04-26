这个项目研究trino在windows上的运行与调试

trino需要jvm至少是17版本，而flink需要8或者11版本，因此分开成两个项目

git clone https://github.com/trinodb/trino.git

git checkout 414

export JAVA_HOME=$JAVA17_HOME
mvn package -DskipTests -pl plugin/trino-hive

mvn clean install -DskipTests -pl plugin/trino-hive-hadoop2 -am