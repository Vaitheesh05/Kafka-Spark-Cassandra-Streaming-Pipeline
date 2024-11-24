// Import necessary libraries
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.common.serialization.StringDeserializer
import com.datastax.spark.connector._
import com.datastax.spark.connector.streaming._

// Spark Configuration
val sparkConf = new SparkConf()
  .setAppName("KafkaSparkCassandraPipeline")
  .setMaster("local[*]") // Use local mode for testing
  .set("spark.cassandra.connection.host", "127.0.0.1") // Cassandra host
  .set("spark.cassandra.connection.port", "9042") // Cassandra port

// Create StreamingContext with a batch interval of 20 seconds
val ssc = new StreamingContext(sparkConf, Seconds(20))

// Kafka Configuration
val kafkaParams = Map[String, Object](
  "bootstrap.servers" -> "localhost:9092", // Kafka broker
  "key.deserializer" -> classOf[StringDeserializer],
  "value.deserializer" -> classOf[StringDeserializer],
  "group.id" -> "sparkgroup", // Consumer group
  "auto.offset.reset" -> "latest", // Read only new messages
  "enable.auto.commit" -> (false: java.lang.Boolean) // Disable auto commit
)

// Kafka topic to consume from
val topics = Array("mytopic")

// Create Kafka Direct Stream
val kafkaStream = KafkaUtils.createDirectStream[String, String](
  ssc,
  LocationStrategies.PreferConsistent,
  ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
)

// Process Kafka records
val processedStream = kafkaStream.map(record => record.value).map { line =>
  // Split the incoming CSV line
  val arr = line.split(",")
  (arr(0), arr(1), arr(2), arr(3), arr(4)) // Extract fields
}

// Save processed data to Cassandra
processedStream.saveToCassandra(
  "sparkdata",        // Keyspace name
  "cust_data",        // Table name
  SomeColumns("fname", "lname", "url", "product", "cnt") // Column mapping
)

// Start the StreamingContext
ssc.start()

// Wait for termination
ssc.awaitTermination()
