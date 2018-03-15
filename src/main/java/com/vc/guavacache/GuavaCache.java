package com.vc.guavacache;

import static spark.Spark.get;
import static spark.Spark.port;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GuavaCache {
	public static CacheLoader<Integer, ArrayList<Integer>> loader ;
	public static void main(String[] args) {
		loader = new CacheLoader<Integer, ArrayList<Integer>>() {
			@Override
			public ArrayList<Integer> load(Integer key) throws Exception {
				System.out.println("tinh lai chu khong dung cache");
				return numbersPrime(key);
			}
		};
		LoadingCache<Integer, ArrayList<Integer>> cache;
		cache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.SECONDS).build(loader);
		port(8080);
		get("/prime", (req, res) -> {
			int key = Integer.parseInt(req.queryParams("n"));
			System.out.println("co 1 query moi, key="+key);
			cache.getUnchecked(key);
			//System.out.println(cache.);
			return cache.get(key);
			
		});
	}
	public static ArrayList<Integer> getPrimes(int key){
		return numbersPrime(key);
	}
	public static ArrayList<Integer> numbersPrime(int n) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < n; i++) {
			if (isPrimeNumber(i)) {
				list.add(i);
			}
		}
		return list;

	}

	public static boolean isPrimeNumber(int n) {

		if (n < 2) {
			return false;
		}
		int squareRoot = (int) Math.sqrt(n);
		for (int i = 2; i <= squareRoot; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
}
