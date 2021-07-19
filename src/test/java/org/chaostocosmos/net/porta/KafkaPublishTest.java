package org.chaostocosmos.net.porta;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaPublishTest {

	final String menuJson = 
			"{\r\n" + 
			"	\"id\": \"0001\",\r\n" + 
			"	\"type\": \"donut\",\r\n" + 
			"	\"name\": \"Cake\",\r\n" + 
			"	\"ppu\": 0.55,\r\n" + 
			"	\"batters\":\r\n" + 
			"		{\r\n" + 
			"			\"batter\":\r\n" + 
			"				[\r\n" + 
			"					{ \"id\": \"1001\", \"type\": \"Regular\" },\r\n" + 
			"					{ \"id\": \"1002\", \"type\": \"Chocolate\" },\r\n" + 
			"					{ \"id\": \"1003\", \"type\": \"Blueberry\" },\r\n" + 
			"					{ \"id\": \"1004\", \"type\": \"Devil's Food\" }\r\n" + 
			"				]\r\n" + 
			"		},\r\n" + 
			"	\"topping\":\r\n" + 
			"		[\r\n" + 
			"			{ \"id\": \"5001\", \"type\": \"None\" },\r\n" + 
			"			{ \"id\": \"5002\", \"type\": \"Glazed\" },\r\n" + 
			"			{ \"id\": \"5005\", \"type\": \"Sugar\" },\r\n" + 
			"			{ \"id\": \"5007\", \"type\": \"Powdered Sugar\" },\r\n" + 
			"			{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },\r\n" + 
			"			{ \"id\": \"5003\", \"type\": \"Chocolate\" },\r\n" + 
			"			{ \"id\": \"5004\", \"type\": \"Maple\" }\r\n" + 
			"		]\r\n" + 
			"}";
	
	final String tstJson = "This is kafka test!!!";
	
    private final static String TOPIC = "Topic";
    //private final static String BOOTSTRAP_SERVERS = "192.168.1.152:8092";
    private final static String BOOTSTRAP_SERVERS = "localhost:8092";
    private long sendMessageCount = 10000;

	public KafkaPublishTest() throws Exception {
	      final Producer<String, String> producer = createProducer();
	      long time = System.currentTimeMillis();
	      try {
	    	  int cnt = 0;
        	  ObjectMapper om = new ObjectMapper();
	          for (long index = 0; index < sendMessageCount; index++) {
	        	  Map map = om.readValue(menuJson, Map.class);
	        	  map = replaceValue(map, "id", index+"");
	        	  String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(map);
	        	  //System.out.println(json.toString());
	              ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC, (tstJson+"  "+(index+1)));
	              RecordMetadata metadata = producer.send(record).get();
	              long elapsedTime = System.currentTimeMillis() - time;
	              if(cnt % 100 == 0) {
		              System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d) time=%d\n", record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);
	              }
	              cnt++;
	          }
	          System.out.println("COUNT: "+cnt);
	          long elapse = System.currentTimeMillis() - time;
	          
	          long hours = TimeUnit.MILLISECONDS.toHours(elapse);
	          elapse -= TimeUnit.HOURS.toMillis(hours);
	          long minutes = TimeUnit.MILLISECONDS.toMinutes(elapse);
	          elapse -= TimeUnit.MINUTES.toMillis(minutes);
	          long seconds = TimeUnit.MILLISECONDS.toSeconds(elapse);

			  System.out.println("Elapse time: "+hours+" hours - "+minutes+" minutes - "+seconds);
	      } finally {
	          producer.flush();
	          producer.close();
	      }
	}
	
    private Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        KafkaProducer producer = new KafkaProducer(props);
        return producer;
    }

	public Map replaceValue(Map obj, String key, String value) {
		for(Object entry : obj.entrySet()) {
			Map.Entry e = (Map.Entry)entry;
			if(e.getKey().equals(key)) {
				e.setValue(value);
			} else if(e.getValue() instanceof List) { 
				for(Object v : (List)e.getValue()) {
					if(v instanceof Map) {
						replaceValue((Map)v, key, value);
					}
				}
			} else if(e.getValue() instanceof Map) {
				replaceValue((Map)e.getValue(), key, value);
			}
		}
		return obj;
	}

    public static void main(String[] args) throws Exception {
		new KafkaPublishTest();
    }    
}
