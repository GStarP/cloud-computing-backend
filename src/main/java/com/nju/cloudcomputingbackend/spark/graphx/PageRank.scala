package com.nju.cloudcomputingbackend.spark.graphx

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.GraphLoader

object PageRank {
  val jsonPattern = "{\"rankList\":[%s]}"
  val rankPattern = "{\"name\":\"%s\",\"rank\":%s}"

  def run(): Unit = {
    val conf = new SparkConf().setAppName("PageRank").setMaster("local[8]")
    val sc = new SparkContext(conf)

    // Load the edges as a graph
    val followsGraph = GraphLoader.edgeListFile(sc, "data/follows_list.txt")
    // Run PageRank
    val ranks = followsGraph.pageRank(0.000000000001).vertices

    val universityHashCodeTable = sc.textFile("data/university_hash_code.txt").map { line =>
      val fields = line.split("\\s+")
      (fields(0).toLong, (fields(1), fields(2)))
    }

    val ranksByUsername = universityHashCodeTable.join(ranks).map {
      case (id, ((uid, uname), rank)) => (rank, uname)
    }

    val rankedResult = ranksByUsername.sortBy(item => item._1, ascending = false)
      .map { case (rank, name) => String.format(rankPattern, name, rank.toString) }
      .collect().mkString(",")
    val pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/my/json/graphx/follows_pagerank.json")), true)
    pw.println(String.format(jsonPattern, rankedResult))
    pw.close()
  }
}
