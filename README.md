![Architecture](https://github.com/user-attachments/assets/6821795d-616e-4132-a968-e8599a1ebb7d)

!!This project demonstrates a real-time streaming data pipeline using Apache Kafka, Apache Spark Streaming, and Apache Cassandra. The pipeline ingests data from a Kafka topic, processes it using Spark Streaming, and stores the processed data in a Cassandra database

1.Real-Time Data Ingestion:
  Kafka is used as a distributed messaging system to produce and consume messages.
  Producers send streaming data to a Kafka topic, and consumers process the data.
2.Stream Processing:
  Spark Streaming reads data from Kafka topics, processes it in micro-batches, and maps the data into structured formats.
3.Data Storage:
  Processed data is stored in Cassandra, a NoSQL database designed for high scalability and availability.
4.End-to-End Integration:
  Demonstrates seamless integration of Kafka, Spark, and Cassandra for real-time big data solutions.
