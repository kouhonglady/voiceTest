package com.hrg.lucene;
//
//public class CNN {
//	public static void main(String[] args) {
//	}
//}


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

public class CNN {
	private static String path = "/Users/shuubiasahi/Documents/python/credit-tftextclassify-abuse/vocab_cnews.txt";
	private static Map<String, Integer> word_to_id = new HashMap<String, Integer>();
	static {
		try {
			BufferedReader buffer = null;
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			int i = 0;
			String line = buffer.readLine().trim();
			while (line != null) {
				word_to_id.put(line, i++);
				line = buffer.readLine().trim();
			}
			buffer.close();
		} catch (Exception e) {
		}
		System.out.println("word_to_id.size is:" + word_to_id.size());
	}

	public static void main(String[] args) {
		
		byte[] graphDef = readAllBytesOrExit(
				Paths.get("E:\\study\\hrg_project\\bigDataCompetition\\bank\\sentence-similarity\\"
						+ "cnn-sentence-similarity\\runs\\1540455285\\checkpoints", "cnn-sentence-similarity.pb"));
		Graph g = new Graph();
		g.importGraphDef(graphDef);
		Session sess = new Session(g);
		
		
		String text = "艹你麻痹的垃圾店家，劳资点的香干回锅肉套餐，你他麻痹炒个香干炒肉过来凑数，套餐内所有的东西都没看到，还尼玛口口声声说退款？退你麻痹，留着给你家人买棺材用吧，狗日的东西！";
		int[][] arr = gettexttoid(text);
		Tensor input = Tensor.create(arr);
		Tensor x = Tensor.create(1.0f);
		Tensor result = sess.runner().feed("input_x", input).feed("keep_prob", x).fetch("score/pred_y").run().get(0);
		long[] rshape = result.shape();
		/*
		 * 模型为一个二分类模型，故nlabels=2，模型预测一条数据故batchsize=1 预测出来是一个1*2的数组，一条数据有两个概率
		 *
		 */
		int nlabels = (int) rshape[1];
		int batchSize = (int) rshape[0];
		float[][] logits = (float[][]) result.copyTo(new float[batchSize][nlabels]);
		System.out.println("辱骂模型识别的概率为:" + logits[0][1]);
		System.out.println("sucess");
	}

	private static byte[] readAllBytesOrExit(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("Failed to read [" + path + "]: " + e.getMessage());
			System.exit(1);
		}
		return null;
	}

	/*
	 * 序列默人长度为300
	 */
	public static int[][] gettexttoid(String text) {
		int[][] xpad = new int[1][300];
		if (StringUtils.isBlank(text)) {
			return xpad;
		}
		char[] chs = text.trim().toLowerCase().toCharArray();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < chs.length; i++) {
			String element = Character.toString(chs[i]);
			if (word_to_id.containsKey(element)) {
				list.add(word_to_id.get(element));
			}
		}
		if (list.size() == 0) {
			return xpad;
		}
		int size = list.size();
		Integer[] targetInter = (Integer[]) list.toArray(new Integer[size]);
//		int[] target = Arrays.stream(targetInter).mapToInt().toArray();
		int[] target = new int[10];
		if (size <= 300) {
			System.arraycopy(target, 0, xpad[0], xpad[0].length - size, target.length);
		} else {
			System.arraycopy(target, size - xpad[0].length, xpad[0], 0, xpad[0].length);
		}
		return xpad;
	}

	/*
	 * 自定义长度
	 */
	public static int[][] gettexttoid(String text, int maxlen) {
		if (maxlen < 1) {
			throw new IllegalArgumentException("maxlen长度必须大于等于1");
		}
		int[][] xpad = new int[1][maxlen];
		if (StringUtils.isBlank(text)) {
			return xpad;
		}
		char[] chs = text.trim().toLowerCase().toCharArray();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < chs.length; i++) {
			String element = Character.toString(chs[i]);
			if (word_to_id.containsKey(element)) {
				list.add(word_to_id.get(element));
			}
		}
		if (list.size() == 0) {
			return xpad;
		}
		int size = list.size();
		Integer[] targetInter = (Integer[]) list.toArray(new Integer[size]);
//		int[] target = Arrays.stream(targetInter).mapToInt(Integer::valueOf).toArray();
		int[] target = new int[10];
		if (size <= maxlen) {
			System.arraycopy(target, 0, xpad[0], xpad[0].length - size, target.length);
		} else {
			System.arraycopy(target, size - xpad[0].length, xpad[0], 0, xpad[0].length);
		}
		return xpad;
	}
}
