package container;

import java.util.Hashtable;

import util.*;

public class ClientContainer
{	
	private static final Hashtable<String, ClientInfo> objClientTable = new Hashtable<String, ClientInfo>();
	private static final Byte markTable = new Byte((byte)1);
	
	public ClientContainer()throws Exception
	{	    
	}

	/*
	 * ����
	 */
	public static ClientInfo Find(String pKey)
	{
		ClientInfo cacheBean = null;
		try
		{			
			synchronized(markTable)
			{
				if(!objClientTable.isEmpty() && objClientTable.containsKey(pKey))
				{
					cacheBean = (ClientInfo) objClientTable.get(pKey);
				}
				
			}
		}
		catch(Exception exp)
		{		
			exp.printStackTrace();	
		}
		return cacheBean;
	}
	/*
	 * ɾ��
	 */
	public static ClientInfo Remove(String pKey)
	{
		ClientInfo cacheBean = null;
		try
		{			
			synchronized(markTable)
			{
				if(!objClientTable.isEmpty() && objClientTable.containsKey(pKey))
				{
					cacheBean = (ClientInfo) objClientTable.get(pKey);
					objClientTable.remove(pKey);		//�ڹ�ϣ�����Ƴ�
				}
			}
		}
		catch(Exception exp)
		{		
			exp.printStackTrace();	
		}
		return cacheBean;
	}
	
	/*
	 * ����
	 */
	public static void Insert(String pKey, ClientInfo cacheBean)
	{
		try
		{			
			synchronized(markTable)					
			{
				if(objClientTable.containsKey(pKey))
				{
					CommUtil.PRINT("Key[" + pKey + "] Already Exist!");
					objClientTable.remove(pKey);		//�ڹ�ϣ�����Ƴ��ͻ���
				}		
				objClientTable.put(pKey , cacheBean);
			}
		}
		catch(Exception exp)
		{		
			exp.printStackTrace();	
		}
	}
	
	public static String GetClientId(String pKey)
	{
		String ret = "";
		try
		{			
			synchronized(markTable)
			{
				if(!objClientTable.isEmpty() && objClientTable.containsKey(pKey))
				{
					ClientInfo cacheBean = (ClientInfo) objClientTable.get(pKey);
					ret = cacheBean.getId();
				}
				
			}
		}
		catch(Exception exp)
		{		
			exp.printStackTrace();	
		}
		return ret;
	}
	

}