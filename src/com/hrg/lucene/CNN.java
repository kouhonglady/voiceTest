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
		
		
		String text = "ܳ����Ե�������ң����ʵ����ɻع����ײͣ�������Գ�����ɳ�������������ײ������еĶ�����û������������ڿ�����˵�˿������ԣ����Ÿ��������ײ��ðɣ����յĶ�����";
		int[][] arr = gettexttoid(text);
		Tensor input = Tensor.create(arr);
		Tensor x = Tensor.create(1.0f);
		Tensor result = sess.runner().feed("input_x", input).feed("keep_prob", x).fetch("score/pred_y").run().get(0);
		long[] rshape = result.shape();
		/*
		 * ģ��Ϊһ��������ģ�ͣ���nlabels=2��ģ��Ԥ��һ�����ݹ�batchsize=1 Ԥ�������һ��1*2�����飬һ����������������
		 *
		 */
		int nlabels = (int) rshape[1];
		int batchSize = (int) rshape[0];
		float[][] logits = (float[][]) result.copyTo(new float[batchSize][nlabels]);
		System.out.println("����ģ��ʶ��ĸ���Ϊ:" + logits[0][1]);
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
	 * ����Ĭ�˳���Ϊ300
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
	 * �Զ��峤��
	 */
	public static int[][] gettexttoid(String text, int maxlen) {
		if (maxlen < 1) {
			throw new IllegalArgumentException("maxlen���ȱ�����ڵ���1");
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
