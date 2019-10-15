package com.nju.cloudcomputingbackend.spark.graphx

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.GraphLoader

object InDegree {
  val jsonPattern = "{\"rankList\":[%s]}"
  val rankPattern = "{\"name\":\"%s\",\"rank\":%s}"

  def run(): Unit = {
    val conf = new SparkConf().setAppName("InDegree").setMaster("local[8]")
    val sc = new SparkContext(conf)

    val universityHashCode = sc.textFile("data/university_hash_code.txt").map { line =>
      val fields = line.split("\\s+")
      (fields(0), fields(2))
    }.collectAsMap()

    // Load the edges as a graph
    val atGraph = GraphLoader.edgeListFile(sc, "data/at_relation_hash.txt")
    val atResult = atGraph.inDegrees.map { case (id, degree) => (degree, universityHashCode(id.toString)) }
      .sortBy { case (degree, name) => -degree }
      .map { case (degree, name) => String.format(rankPattern, name, degree.toString) }
      .collect().mkString(",")
    val at_pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/my/json/graphx/at_inDegree.json")), true)
    at_pw.println(String.format(jsonPattern, atResult))
    at_pw.close()

    val followsGraph = GraphLoader.edgeListFile(sc, "data/follows_list.txt")
    val followResult = followsGraph.inDegrees.map { case (id, degree) => (degree, universityHashCode(id.toString)) }
      .sortBy { case (degree, name) => -degree }
      .map { case (degree, name) => String.format(rankPattern, name, degree.toString) }
      .collect().mkString(",")
    val pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/my/json/graphx/follows_inDegree.json")), true)
    pw.println(String.format(jsonPattern, followResult))
    pw.close()
  }
}
