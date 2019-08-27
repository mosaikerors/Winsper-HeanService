package com.mosaiker.heanservice.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeohashTest {

  @Test
  public void encode() {
    System.out.println(new Geohash().encode(45, 125));
    System.out.println("\n");
    System.out.println(new Geohash().encode(45.0, 125.0));
    System.out.println("\n");
    System.out.println(new Geohash().encode(45.00, 125.00));
    System.out.println("\n");
    System.out.println(new Geohash().encode(45.00001,125.000001));
    System.out.println("\n");
    System.out.println(new Geohash().encode(1,2));
    System.out.println("\n");
    System.out.println(new Geohash().encode(1.1,2.1));
    System.out.println("\n");
    System.out.println(new Geohash().encode(1.01,2.01));
  }
}