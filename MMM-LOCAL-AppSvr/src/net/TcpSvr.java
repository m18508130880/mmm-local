package net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import util.CmdUtil;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;
import container.ClientContainer;

public class TcpSvr extends Thread
{
	public static final int  STATUS_CLIENT_ONLINE  = 0;
	public static final int  STATUS_CLIENT_OFFLINE = 1;
	public static final String TYPE_OPERATOR	   = "0";
	
	public Hashtable<String, ClientSocket> objClientTable = null;//��½�ͻ����б�
	public Hashtable<String, ClientSocket> getObjClientTable() {
		return objClientTable;
	}
	
	public Hashtable<String, String> objTelStaTable = null;//�������ر�
	
	//TCP������
	private ServerSocket objTcpSvrSock = null;
	private static Byte markClientTable = new Byte((byte)1);
	
	//���������б�,���ڿͻ������ݽ���
	public LinkedList<Object> recvMsgList = null;
	public Byte markRecv = new Byte((byte)1);
	
	private int m_Seq = 0;
	private int m_iPort = 0;
	private int m_iStatus  = 0;
	private int m_iTimeOut = 0;
	TcpClient m_TcpClient = null;
	
	DBUtil m_DbUtil = null;
	//��ȡ�����ļ�����
	public TcpSvr(DBUtil dbUtil)throws Exception
	{
		SAXReader reader  = new SAXReader();
		Document document = reader.read(new FileInputStream("Config.xml"));
		Element root = document.getRootElement();
		m_iPort      = Integer.parseInt(root.element("app_server").element("server_prot").getText());
		m_iTimeOut   = Integer.parseInt(root.element("app_server").element("server_timeout").getText());
		m_iStatus    = Integer.parseInt(root.element("app_client").element("client_sta").getText());
		m_DbUtil     = dbUtil;
		
		if(1 == m_iStatus)
		{
			m_TcpClient = new TcpClient(m_DbUtil);
			m_TcpClient.init();
		}
	}
	
	//��ʼ��Socket
	public boolean init()
	{
		try
		{
			objTcpSvrSock = new ServerSocket(m_iPort);
			if(null == objTcpSvrSock) 
			{
				return false;
			}	
			recvMsgList = new LinkedList<Object>();		
			objClientTable = new Hashtable<String, ClientSocket>(); 
			objTelStaTable = new Hashtable<String, String>(); 
			this.start();                  
			return true;
		}
		catch (IOException ioExp)
		{
			ioExp.printStackTrace();
			return false;
		}		
	}
	
	//����Socket����
	public void run()
	{	
		while (true)
		{  
			try
			{
				Socket objClient = objTcpSvrSock.accept();
				objClient.setSoTimeout(m_iTimeOut*1000);
				
				DataInputStream RecvChannel = new DataInputStream(objClient.getInputStream());
				byte[] Buffer = new byte[1024];
				
				int RecvLen = RecvChannel.read(Buffer);
				
				CommUtil.PRINT("Send Original:");
				CommUtil.printMsg(Buffer, RecvLen);
				
				if(20 > RecvLen)
				{
					objClient.close();
					objClient = null;
					continue;
				}
				
				//������֤
				String Pid = null;
				if(null == (Pid = CheckClient(Buffer, objClient)))
					continue;
				
				//����ظ�
				DataOutputStream SendChannel = new DataOutputStream(objClient.getOutputStream());
				
				//RMI����
				if(0 ==Integer.parseInt(Pid.trim()))
				{
					System.out.println();
					SendChannel.write(new String(Buffer, 0, 44).getBytes());
				}
				//DTU����
				else
				{
					//��ʱ
					String RespBuf = new String(Buffer, 0, 20);
					RespBuf += CommUtil.StrBRightFillSpace("", 20);		//������
					RespBuf += CommUtil.StrBRightFillSpace("0000", 4);		//�����״̬
					RespBuf += CommUtil.StrBRightFillSpace("3002", 4);		//����ָ��
					RespBuf += CommUtil.StrBRightFillSpace(Pid, 10);		//DTU��ID
					RespBuf += CommUtil.StrBRightFillSpace("00010002", 8);			//���͵�ָ��
					RespBuf += CommUtil.StrBRightFillSpace("AppSvr", 10);		//�����û�
					RespBuf += CommUtil.StrBRightFillSpace(CommUtil.getTime(), 14);		//ָ������
					
					//System.out.println("Login Resp[" + new String(Buffer, 0, 44) + "]");
					//System.out.println("Login RespTime[" + RespBuf + "]");
					//SendChannel.write(new String(Buffer, 0, 44).getBytes());
					SendChannel.write(RespBuf.getBytes());
				}
				SendChannel.flush();
				objClient.setSoTimeout(0);
				ClientStatusNotify(Pid, STATUS_CLIENT_ONLINE);
		 		continue;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				continue;
			}
		}//while
	}
	//������֤
	protected String CheckClient(byte[] Buffer, Socket objClient)
	{

		String ret = null;
		try
		{
			DataInputStream DinStream = new DataInputStream(new ByteArrayInputStream(Buffer));
			DinStream.readInt();
			int Cmd = CommUtil.converseInt(DinStream.readInt());
			if(Cmd_Sta.COMM_LOGON != Cmd)
			{
				objClient.close();
				objClient = null;
				return null;
			}
			
			//������֤
			String Status = new String(Buffer, 20, 4);
			String PId = new String(Buffer, 24, 20);
			String TimeStamp = new String(Buffer, 44, 14);
			String strMd5 = new String(Buffer, 58, 32);
			String checkResult = checkClient(Status, PId, TimeStamp, strMd5);
			if(!checkResult.substring(0, 4).equalsIgnoreCase("0000"))
			{
				objClient.close();
				objClient = null;
				return null;
			}
			ret = PId;
			
			//��֤�Ƿ��Ѵ���
			if(objClientTable.containsKey(PId))
			{
				CommUtil.PRINT("Id Already Exist!" + PId);
				ClientClose(PId);
			}
			
			//�½�ͨ��
			ClientSocket objChannel= new ClientSocket();	
			if(!objChannel.init(objClient, PId))
			{
				CommUtil.LOG("ClientId [" + PId + "] ClientSocket init failed!");
			}
			synchronized(markClientTable)
			{
				objClientTable.put(PId , objChannel);
			}
			
			//����ͨ��IP
//			CommUtil.LOG("CPM_IP:" + objClient.getInetAddress().toString());
//			String pSql = "update device_detail t set t.link_url = '"+ objClient.getInetAddress().toString().substring(1) +"' where t.id = '"+ PId.trim() +"'";
//			m_DbUtil.doUpdate(pSql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return ret;
	
	}
	
	public String checkClient(String strStatus, String strId, String strTimestamp, String strOriginalMd5)
	{
		String ret = "3006";
		String password = m_DbUtil.APC(CommUtil.StrRightFillSpace(strId, 40)+ strStatus + "0001");
		String strData = strId + strTimestamp + password;
		System.out.println("strData[" + strData + "]");
		String Temp = CommUtil.BytesToHexString(new Md5().encrypt(strData.getBytes()), 16);
		System.out.println("Temp[" + Temp + "]");
		CommUtil.LOG("Client[" + strId + "] TimeStamp[" + strTimestamp + "] OldMd5[" + strOriginalMd5 + "] NewMd5[" + Temp + "] DbMsg[" + password + "]");
		
		if(Temp.equalsIgnoreCase(strOriginalMd5))
		{
			ret = "0000";
		}
		return ret;
	}
	
	public byte[] GetActiveTestBuf()
	{
		byte[] byteData = null;
		try
		{
			ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
			DataOutputStream doutStream = new DataOutputStream(boutStream);
			doutStream.writeInt(CommUtil.converseInt(CmdUtil.MSGHDRLEN));
			doutStream.writeInt(CommUtil.converseInt(CmdUtil.COMM_ACTIVE_TEST));
			doutStream.writeInt(0);
			doutStream.writeInt(CommUtil.converseInt(GetSeq()));
			doutStream.writeInt(0);
			byteData = boutStream.toByteArray();
			doutStream.close();
			boutStream.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			byteData = null;
		}
		return byteData; 
	}
	
	public void ClientStatusNotify(String strClientKey, int iStatus)
	{
		String sql = "";
		String onoff = "";
		switch(iStatus)
		{
			case STATUS_CLIENT_ONLINE:
			{
				//CPM���ػָ�����
				sql = "INSERT INTO device_alert(id, ctime, des) VALUES('" + strClientKey + "', +date_format('"+ CommUtil.getDateTime() +"', '%Y-%m-%d %H-%i-%S'), '���ػָ�����')";
				onoff = "1";
				break;
			}
			case STATUS_CLIENT_OFFLINE:
			{
				//CPM��������
				sql = "INSERT INTO device_alert(id, ctime, des) VALUES('" + strClientKey + "', +date_format('"+ CommUtil.getDateTime() +"', '%Y-%m-%d %H-%i-%S'), '��������')";
				onoff = "0";
				break;
			}
		}
		if(!strClientKey.equals("0000000000") && !strClientKey.equals("0000000001"))
		{
			m_DbUtil.doUpdate(sql);
		}
		// �޸��������
		sql = "update device_detail set onoff = '" + onoff + "' where id = '" + strClientKey + "'";
		m_DbUtil.doUpdate(sql);
	}
	
	//����յ��ر�ָ��͹ر�SOCKET���ͷ���Դ
	public synchronized void ClientClose(String pClientKey)
	{
		try
		{			
			if(!objClientTable.isEmpty() && objClientTable.containsKey(pClientKey))
			{
				synchronized(markClientTable)
				{
					ClientSocket objChannel = (ClientSocket) objClientTable.get(pClientKey);
					if(null != objChannel.objSocket && !objChannel.objSocket.isClosed())
					{
						objChannel.objSocket.close();		//�ص�SOCKET����
						objChannel.objSocket = null;
						ClientStatusNotify(pClientKey, STATUS_CLIENT_OFFLINE);
					}
					objClientTable.remove(pClientKey);		//�ڹ�ϣ�����Ƴ��ͻ���
					ClientContainer.Remove(pClientKey);
				//	m_DbUtil.Update_Client_Status(pClientKey, "0");
				}
			}
		}
		catch(Exception exp)
		{		
			exp.printStackTrace();	
		}
	}
	
	//ȡ�ý����߳������б�
	public byte[] GetRecvMsgList()
	{
		byte[] data = null;
		synchronized(markRecv)
		{
			if(!recvMsgList.isEmpty())
			{	
				data = (byte[])recvMsgList.removeFirst();
			}
		}
		return data;
	}
	
	//���ý����߳��б�
	public void SetRecvMsgList(Object  object)
	{
		synchronized(markRecv)
		{
			recvMsgList.addLast(object);
		}
	}	
	
	public boolean DisPatch(int msgCode, String clientKey, String pData)
	{
		boolean ret = false;
		try
		{
			synchronized(markClientTable)
			{
				//System.out.println("["+!objClientTable.isEmpty()+"] && ["+objClientTable.containsKey(clientKey)+"]");
				if(!objClientTable.isEmpty() && objClientTable.containsKey(clientKey))
				{
					CommUtil.LOG("Succee DisPatch Client[" + clientKey + "] Data[" + pData + "]");
					ClientSocket objChannel = (ClientSocket) this.objClientTable.get(clientKey);	
					objChannel.SendMsg(msgCode, pData);
					ret = true;
				}
				else
				{
					CommUtil.LOG("Failed DisPatch Client[" + clientKey + "] Data[" + pData + "]");
				}
			}
		}
		catch(Exception e)
		{	
		}
		return ret;
	}

	//�������к�
	public int GetSeq()
	{
		if(m_Seq++ == 0xffffff)
			m_Seq = 0;
		return m_Seq;
	}
	
	//���ؽ����б��С
	public long GetRecvMsgListLength()
	{
		return recvMsgList.size();
	}
	
	public byte[] EnCode(int msgCode, String pData)
	{
		byte[] byteData = null;
		try
		{
			ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
			DataOutputStream doutStream = new DataOutputStream(boutStream);
			{
				doutStream.writeInt(CommUtil.converseInt(CmdUtil.MSGHDRLEN + pData.getBytes().length));//����
				doutStream.writeInt(CommUtil.converseInt(msgCode));
				doutStream.writeInt(CommUtil.converseInt(0));
				doutStream.writeInt(CommUtil.converseInt(GetSeq()));
				doutStream.writeInt(CommUtil.converseInt(0));
				doutStream.write(pData.getBytes());
			}
			byteData = boutStream.toByteArray();
			boutStream.close();
			doutStream.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return byteData;
	}
	
	/************************************ClientSocket*****************************************/	
	//��ÿ���ͻ������Ӧ�ķ���ˣ�ͬ���ڿͻ���
	public class ClientSocket extends Thread
	{	
		public Socket objSocket = null;	
		private RecvThrd objRecvThrd = null;
		private SendThrd objSendThrd = null;
		
		private LinkedList<Object> sendMsgList = null;
		private byte[] markSend = new byte[1];
		public String m_ClientKey = "";
		private int m_TestSta = 0;

		//��ʼ��SOCKET
		public boolean init(Socket objClient, String pClientKey)
		{		
			try
			{
				m_ClientKey = pClientKey;
				objSocket = objClient;
				objSocket.setSoTimeout(0);
							
				sendMsgList = new LinkedList<Object>();	
				
				objRecvThrd = new RecvThrd(objSocket);
				objRecvThrd.start();
				
				objSendThrd = new SendThrd(objClient);
				objSendThrd.start();
						
				this.start();			
			}
			catch(Exception exp)
			{
				exp.printStackTrace();	
				return false;
			}
			return true;
		}
		
		public void run()
		{
			int testTime= (int)(new java.util.Date().getTime()/1000);
			int nowTime = testTime;
			int dTime = 0;
			
			//Active Test
			while(true)
			{
				try
				{
					sleep(2000);
					if(null == objSocket || objSocket.isClosed())
					{
						CommUtil.LOG("socket is closed " + m_ClientKey);
						ClientClose(m_ClientKey);
						break;
					}
					nowTime = (int)(new java.util.Date().getTime()/1000);  //getTime����Ǻ�����
					dTime = nowTime - testTime;
					if(dTime > m_iTimeOut)
					{
						m_TestSta++;
						if(m_TestSta > CmdUtil.ACTIVE_TEST_END)
						{		
							CommUtil.LOG("m_TestSta > CmdUtil.ACTIVE_TEST_END " + m_ClientKey);
							ClientClose(m_ClientKey);
						}
						else
						{
							if(m_TestSta >= CmdUtil.ACTIVE_TEST_START)
							{
								byte[] byteData = GetActiveTestBuf();
								if(null != byteData)
								{
									SetSendMsgList(byteData);    //���뷢���б�			
									CommUtil.LOG("Send Active Test..");	
								}
							}
						}
						testTime = nowTime;
					}				
				}
				catch(Exception ex)
				{
					CommUtil.LOG("TcpSvr/Run:Active Test Error.............\n");
					ex.printStackTrace();
					continue;
				}
			}
		}
		
		//����Ϣ�͵����Ͷ���
		public void SendMsg(int msgCode, String pData)
		{
			SetSendMsgList(EnCode(msgCode, pData));
		}
		private void SetSendMsgList(Object  object)
		{
			synchronized(markSend)
			{
				sendMsgList.addLast(object);
			}
		}
		//�ӷ��Ͷ���ȡһ����Ϣ
		private byte[] getSendMsgList()
		{
			byte[] data = null;			
			synchronized(markSend)
			{
				if(null != sendMsgList && !sendMsgList.isEmpty())
				{	
					data = (byte[]) sendMsgList.removeFirst();
				}
			}
			return data;
		}	
	
		/************************************�����߳�*****************************************/
		private class RecvThrd extends Thread
		{
			private DataInputStream RecvChannel = null;
			public RecvThrd(Socket pSocket)throws Exception
			{
				RecvChannel = new DataInputStream(pSocket.getInputStream());
			}
			public void run()
			{
				Vector<Object> data = new Vector<Object>();
				int nRecvLen = 0;
				int nRcvPos = 0;
				int nCursor = 0;
				byte ctRslt = 0;
				boolean bContParse = true;
				byte[] cBuff = new byte[Cmd_Sta.CONST_MAX_BUFF_SIZE];
				
				while (true)
				{
					try
					{
						if(null == objSocket || objSocket.isClosed())
						{
							ClientClose(m_ClientKey);
							break;
						}
						nRecvLen = RecvChannel.read(cBuff, nRcvPos, (Cmd_Sta.CONST_MAX_BUFF_SIZE - nRcvPos));
						if(nRecvLen <= 0)
						{ 
							ClientClose(m_ClientKey);
							CommUtil.LOG("closed the socket in TcpSvr Recvs" + m_ClientKey);
							break;
						}
						m_TestSta = 0;
						nRcvPos += nRecvLen;
						nRecvLen = 0;
						nCursor = 0;
						int nLen = 0;	
						bContParse = true;					
				
						while (bContParse)
						{
							nLen = nRcvPos - nCursor;
							if(0 >= nLen) 
							{
								break;
							}
							data.clear();
							
							CommUtil.PRINT("Client  Recv [" + m_ClientKey + "]");
							CommUtil.printMsg(cBuff, nLen);
							
							data.insertElementAt(new Integer(nLen),0);
							data.insertElementAt(new Integer(nCursor),1);
							ctRslt = DeCode(cBuff, data);
							nLen = ((Integer)data.get(0)).intValue();
							switch(ctRslt)
							{
								case CmdUtil.CODEC_CMD:
									byte [] Resp = ((byte[])data.get(1));					
									if(null != Resp && Resp.length > 0)
									{
										SetSendMsgList(Resp);
									}		
							
									byte[] transData = (byte[])data.get(2);
									if(null != transData && transData.length >= Cmd_Sta.CONST_MSGHDRLEN)
									{
										SetRecvMsgList(transData);
									}
									nCursor += nLen;
									break;
								case CmdUtil.CODEC_RESP:
									nCursor += nLen;
									break;
								case CmdUtil.CODEC_NEED_DATA:
									bContParse = false;
									break;						
								case CmdUtil.CODEC_ERR:		
									nRcvPos = 0;							
									bContParse = false;
									break;
								default:
									break;
							}
						}//bContParse
						if(0 != nRcvPos)
						{
							System.arraycopy(cBuff, nCursor, cBuff, 0, nRcvPos - nCursor);
							nRcvPos -= nCursor;
						}
					}
					catch(SocketException Ex1)
					{
						Ex1.printStackTrace();
						ClientClose(m_ClientKey);
						break;
					}
					catch(Exception Ex)
					{
						Ex.printStackTrace();
						continue;
					}
				}//while		
			}
			
			private byte DeCode(byte[] pMsg, Vector<Object> vectData)
			{
				byte RetVal = CmdUtil.CODEC_ERR;
				int nUsed = ((Integer)vectData.get(0)).intValue();//���е����ݳ���
				int nCursor = ((Integer)vectData.get(1)).intValue();//��ʲô�ط���ʼ
				try
				{
					DataInputStream DinStream= new DataInputStream(new ByteArrayInputStream(pMsg));
					if(nUsed < (int)CmdUtil.MSGHDRLEN )
					{
						return CmdUtil.CODEC_NEED_DATA;
					}
					DinStream.skip(nCursor); 

					int unMsgLen = CommUtil.converseInt(DinStream.readInt());
					int unMsgCode = CommUtil.converseInt(DinStream.readInt());
					int unStatus = CommUtil.converseInt(DinStream.readInt());
					int unMsgSeq = CommUtil.converseInt(DinStream.readInt());
					int unReserve = CommUtil.converseInt(DinStream.readInt());
					//System.out.println("DeCode:" + new String(pMsg));
					if(unMsgLen < CmdUtil.MSGHDRLEN || unMsgLen > CmdUtil.RECV_BUFFER_SIZE)
					{				
						CommUtil.LOG("unMsgLen < CmdUtil.MSGHDRLEN " + unMsgLen);
						return CmdUtil.CODEC_ERR;
					}
			
					if(nUsed < unMsgLen)
					{
						return CmdUtil.CODEC_NEED_DATA;
					}
	
					vectData.insertElementAt(new Integer(unMsgLen), 0);//nUsed = unMsgLen;			
					if((unMsgCode & CmdUtil.COMM_RESP) != 0)//��Ӧ���
					{
						return CmdUtil.CODEC_RESP;
					}
			
					DinStream.close();

					//CommUtil.printMsg(pMsg, unMsgLen);		
					ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
					DataOutputStream doutStream = new DataOutputStream(boutStream);
					//��Ӧ���
					doutStream.writeInt(CommUtil.converseInt(CmdUtil.MSGHDRLEN));
					doutStream.writeInt(CommUtil.converseInt(unMsgCode|CmdUtil.COMM_RESP));
					doutStream.writeInt(CommUtil.converseInt(unStatus));//Sta
					doutStream.writeInt(CommUtil.converseInt(unMsgSeq));//seq
					doutStream.writeInt(CommUtil.converseInt(unReserve));//
					vectData.insertElementAt(boutStream.toByteArray(), 1);
					boutStream.close();
					doutStream.close();	 
    	
					vectData.insertElementAt(null,2);
					switch(unMsgCode)
					{				
						case CmdUtil.COMM_ACTIVE_TEST:			// ��Ӧ���							
							vectData.insertElementAt(null,2);
							RetVal = CmdUtil.CODEC_CMD;
							break;			
						case CmdUtil.COMM_SUBMMIT:
						case CmdUtil.COMM_DELIVER:
						{
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							DataOutputStream dout = new DataOutputStream(bout);
							dout.write(CommUtil.StrRightFillSpace(m_ClientKey, 20).getBytes());
							dout.write(pMsg, nCursor, unMsgLen);
							vectData.insertElementAt(bout.toByteArray(), 2);
							dout.close();
							bout.close();	
							RetVal = Cmd_Sta.CODEC_CMD;
							break;
						}
						default:
							break;				
					}  	
				}
				catch (Exception exp)
				{
					exp.printStackTrace();
				}	
				return RetVal;
			}
		}
		
		/************************************�����߳�*****************************************/
		private class SendThrd extends Thread
		{
			private DataOutputStream SendChannel = null;
			public SendThrd(Socket pSocket)throws Exception
			{
				SendChannel = new DataOutputStream(pSocket.getOutputStream());
			}
			public void run()
			{
				while(true)
				{
					try
					{
						if(null == objSocket || objSocket.isClosed())
						{
							ClientClose(m_ClientKey);
							break;
						}
				
						byte[] data = getSendMsgList();
						if(null == data )
						{
							sleep(10);
							continue;
						}
						if(data.length > 20)
						{
							//CommUtil.printMsg(data,  data.length);
						}
						SendChannel.write(data);
						SendChannel.flush();
					
					}
					catch(SocketException Ex)
					{
						ClientClose(m_ClientKey);
						break;
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}					
				}				
			}	
		}//SendThrd
	}//ClientSocket
}//TcpSvrCls