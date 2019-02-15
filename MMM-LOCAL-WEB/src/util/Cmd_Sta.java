package util;
public class Cmd_Sta 
{
//	=================ͨ��========================================================
	public static final int COMM_LOGON 					= 0x00000001;	//�����½
	public static final int COMM_LOGOUT 				= 0x00000002;	//��ֹ��½
	public static final int COMM_ACTIVE_TEST 			= 0x00000003;	//���Ӳ���
	public static final int COMM_SUBMMIT 				= 0x00000004;	//�ͻ����ύ
	public static final int COMM_DELIVER 				= 0x00000005;	//�������ɷ�
	public static final int COMM_END 					= 0x00000006;	//��������־
	public static final int COMM_RESP 					= 0x80000000; 	//��Ӧ���

//	==================��������=====================================================	
	public static final int CONST_MAX_BUFF_SIZE = 2048;	//���ջ���������
	public static final int CONST_MSGHDRLEN = 20;
	public static final int CONST_TEST_START = 3; //���԰����Կ�ʼ����
	public static final int CONST_TEST_END = 6; //���԰����Կ�ʼ����
	
//	===================DeCode�����ķ���ֵ===========================================	
	public static final byte CODEC_CMD = 0;
	public static final byte CODEC_RESP = 1;
	public static final byte CODEC_TRANS = 2;
	public static final byte CODEC_NEED_DATA = 3;
	public static final byte CODEC_ERR = 4;
	public static final byte CODEC_END = 5;

//	===========Զ�̿���========================================================
	public static final int CMD_DEVICE_CTRL				    = 3002;	//�����·�
	public static final int CMD_DEVICE_SYN				    = 3003;	//Զ��ͬ��

//	====================ϵͳ״̬	
	public static final int STA_SUCCESS						= 0000;	//�ɹ�	
	public static final int STA_SUBMIT_SUCCESS				= 3000;	//�ύ�ɹ�
	public static final int STA_CLIENT_NOT_EXIST			= 1001; //�û�������
	public static final int STA_MD5_CODE_ERROR				= 1002;	//У�������	
	public static final int STA_OLD_PASSWORD_ERROR			= 1003;	//ԭ�������	
	public static final int STA_NET_ERROR					= 9993;	//�������
	public static final int STA_SYSTEM_BUSY					= 9994;	//ϵͳæ
	public static final int STA_DATA_FORMAT_ERROR			= 9995;	//��ʽ����
	public static final int STA_UNKHOWN_DEAL_TYPE			= 9996;	//δ֪ҵ������	
	public static final int STA_DB_ERROR					= 9997; //���ݿ����	
	public static final int STA_SYSTEM_ERROR				= 9998;	//ϵͳæ	
	public static final int STA_UNKHOWN_ERROR				= 9999;	//δ֪����
	
	public static String GetErrorMsg(String strCode)
	{
		String RetVal = "";
		int Code = Integer.parseInt(strCode);
		switch(Code)
		{	
			case STA_SUCCESS:
			 	RetVal = "�ɹ�";	
			 	break;
			case STA_NET_ERROR:
				RetVal = "�������";
				break;
			case STA_SYSTEM_BUSY:
				RetVal = "ϵͳæ";
				break;	
			case STA_DATA_FORMAT_ERROR:
				RetVal = "��ʽ����";
				break;
			case STA_UNKHOWN_DEAL_TYPE:
				RetVal = "δ֪ҵ������";
				break;
			case STA_DB_ERROR:
				RetVal = "���ݿ����";
				break;
			case STA_SYSTEM_ERROR:
				RetVal = "ϵͳæ";
				break;
			case STA_UNKHOWN_ERROR:
				RetVal = "δ֪����";
				break;
			case STA_CLIENT_NOT_EXIST: 
				RetVal = "�û�������";
				break;
			case STA_MD5_CODE_ERROR:	
				RetVal = "У�������";	
				break;
			case STA_OLD_PASSWORD_ERROR:
				RetVal = "ԭ�������";	
				 break;	
			default:
				RetVal = "δ֪����";
				break;
		}
		return RetVal;
	}
}