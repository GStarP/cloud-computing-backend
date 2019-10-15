package com.nju.cloudcomputingbackend.spark.graphx

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.GraphLoader

object TriangleCount {
  val jsonPattern = "{\"rankList\":[%s]}"
  val rankPattern = "{\"name\":\"%s\",\"rank\":%s}"

  def run(): Unit = {
    val conf = new SparkConf().setAppName("TriangleCount").setMaster("local[8]")
    val sc = new SparkContext(conf)

    val universityHashCode = sc.textFile("data/university_hash_code.txt").map { line =>
      val fields = line.split("\\s+")
      //      (fields(0), (fields(1), fields(2)))
      (fields(0), fields(2))
    }.collectAsMap()

    // Load the edges as a graph
    val atGraph = GraphLoader.edgeListFile(sc, "data/at_relation_hash.txt")
    val atResult = atGraph.triangleCount().vertices.sortBy(_._2, ascending = false)
      .map { case (id, rank) => String.format(rankPattern, universityHashCode(id.toString), rank.toString) }.collect()
      .mkString(",")
    val at_pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("output/graphX/at_triangle_count.json")), true)
    at_pw.println(String.format(jsonPattern, atResult))
    at_pw.close()

    val followsGraph = GraphLoader.edgeListFile(sc, "data/follows_list.txt")
    val followsResult = followsGraph.triangleCount().vertices.sortBy(_._2, ascending = false)
      .map { case (id, rank) => String.format(rankPattern, universityHashCode(id.toString), rank.toString) }.collect()
      .mkString(",")
    val pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/my/json/graphX/follows_triangle_count.json")), true)
    pw.println(String.format(jsonPattern, followsResult))
    pw.close()
  }
}
