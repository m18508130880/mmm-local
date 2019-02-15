package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONObject;

/**
 * ����ͼת��������
 */

public class CoordConv
{
	public static void main(String [] args)
	{
		BD_09ToGCJ_02("120.202048", "30.276481", bd09ll, gcj02);
	}
	
	private static String BD_09	= "http://api.map.baidu.com/geoconv/v1/?";
	private static String GCJ_02	= "http://api.map.baidu.com/geoconv/v1/?";
	private static String coord	= "";
	public static int wgs84 = 1;			// 1��GPS�豸��ȡ�ĽǶ����꣬wgs84����;
	public static int sogou = 2;			// 2��GPS��ȡ���������ꡢsogou��ͼ��������;
	public static int gcj02 = 3;			// 3��google��ͼ��soso��ͼ��aliyun��ͼ��mapabc��ͼ��amap��ͼ�������꣬����֣�gcj02������;
	public static int gcj02_mc = 4;		// 4��3���б��ͼ�����Ӧ����������;
	public static int bd09ll = 5;			// 5���ٶȵ�ͼ���õľ�γ������;
	public static int bd09mc = 6;			// 6���ٶȵ�ͼ���õ���������;
	public static int mapbar = 7;			// 7��mapbar��ͼ����;
	public static int _51 = 8;				// 8��51��ͼ����
	private static String ak = "84PQWnNC1Ol9hHyawPSHYjVGwTZGpUVV";
	public static String [] BD_09ToGCJ_02(String Lng, String Lat, int from, int to)
	{
		coord = Lng + "," + Lat;
		String result = "";// ���ʷ��ؽ��
		String [] coords = new String [2];
		BufferedReader read = null;// ��ȡ���ʽ��
		try
		{
			// ����url
			URL realurl = new URL(BD_09 + "coords=" + coord + "&from=" + from + "&to=" + to + "&ak=" + ak);
			// ������
			URLConnection connection = realurl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ��������
			connection.connect();
			
			// ���� BufferedReader����������ȡURL����Ӧ		
			read = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;// ѭ����ȡ
			while ((line = read.readLine()) != null)
			{
				result += line;
			}
			JSONObject jsonObject = JSONObject.parseObject(result).getJSONArray("result").getJSONObject(0);
			coords[0] = jsonObject.getString("x");
			coords[1] = jsonObject.getString("y");
			//System.out.println(coords[0] + "," + coords[1]);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (read != null)
			{// �ر���
				try
				{
					read.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return coords;
	}
}
