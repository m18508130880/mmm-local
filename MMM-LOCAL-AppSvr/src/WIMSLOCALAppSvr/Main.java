package WIMSLOCALAppSvr;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.PropertyConfigurator;

import container.ActionContainer;
import net.*;
import util.*;

public class Main extends Thread 
{
	private static Main objMain = null;
	
	private DBUtil m_DBUtil = null;				//���ݿ�
	private TcpSvr m_TcpSvr = null;				//Svr	
	private MsgCtrl m_MsgCtrl = null;			//Svr��Ϣ����
	public static void main(String[] args) 
	{
		objMain = new Main();
		objMain.init();
	}

	public void init() 
	{		
		try 
		{
			PropertyConfigurator.configure("log4j.properties");//����properties�ļ�
			//ָ��ظ�����
			if(!ActionContainer.Initialize())
			{
				System.exit(-1);
			}
			
			//���ݿ��ʼ��
			m_DBUtil = new DBUtil();
			if(!m_DBUtil.init())
			{
				System.exit(-1);
			}
			//TCPSVR��ʼ��
			m_TcpSvr = new TcpSvr(m_DBUtil);
			if(!m_TcpSvr.init())
			{
				System.out.println("m_TcpSvr Failed======");
				System.exit(-3);
			}
			//��Ϣ���Ƴ�ʼ��
			m_MsgCtrl = new MsgCtrl(m_TcpSvr, m_DBUtil);
			if(!m_MsgCtrl.Initialize())
			{
				System.out.println("m_MsgCtrl Failed======");
				System.exit(0);
			}

			this.start();
			Runtime.getRuntime().addShutdownHook(new Thread(){
				public void run() {
					System.gc();
				}
			});
		} catch (Exception e) 
		{
			e.printStackTrace();
			Runtime.getRuntime().exit(0);
		}

	}

	public void run() 
	{
		System.out.println("Start..........................................");
		while (!interrupted()) 
		{
			try {
				sleep(1000);	
				if(true)
					continue;
				@SuppressWarnings("unused")
				String inputCmd = new BufferedReader(new InputStreamReader(System.in)).readLine().toLowerCase();
				System.out.println();
				
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}	
	}
}
