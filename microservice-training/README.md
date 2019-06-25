以下是此demo的架构图

 ![image](https://github.com/xgwangdl/microservice/blob/master/images/framework.png)

ELK配置：
1.配置elasticsearch目录下config目录的elasticsearch.yml
    cluster.name: elasticsearch
    node.name: node-1
    network.host: 127.0.0.1
    http.port: 9200
    
  启动elasticsearch.dos命令：进入elasticsearch的bin目录，elasticsearch
    
2.配置logstash目录下bin目录下添加read.conf
    input {
     file{
      path => "C:/microservice/log/*.log"
      start_position => beginning
     }
     }  

    output {  
     elasticsearch { hosts => ["localhost:9200"]    
         index => "microservicelog-%{+YYYY.MM}"
          }
     stdout { codec => rubydebug } 
    }
启动logstash.dos命令：进入logstash的bin目录，logstash -f read.conf

3.配置kibana目录下的config目录下的kibana.yml
    server.port: 5601
    server.host: "localhost"
    elasticsearch.url: "http://localhost:9200"
    kibana.index: ".newkibana"
    
 启动kibana.dos命令：进入kibana的bin目录，kibana
