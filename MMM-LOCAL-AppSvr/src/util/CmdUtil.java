package util;
public class CmdUtil
{
//	==================ͨ��========================================================
	public static final int COMM_LOGON 					= 0x00000001;	//�����½
	public static final int COMM_LOGOUT 				= 0x00000002;	//��ֹ��½
	public static final int COMM_ACTIVE_TEST 			= 0x00000003;	//���Ӳ���
	public static final int COMM_SUBMMIT 				= 0x00000004;	//�ͻ����ύ
	public static final int COMM_DELIVER 				= 0x00000005;	//�������ɷ�
	public static final int COMM_RESP 					= 0x80000000; 	//��Ӧ���

	
//	==================��������=====================================================	
	public static final int RECV_BUFFER_SIZE  = 2048; //���ջ���������
	public static final int MSGHDRLEN         = 20;   //��ͷ����
	public static final int ACTIVE_TEST_START = 3;    //���԰����Կ�ʼ����
	public static final int ACTIVE_TEST_END   = 6;    //���԰����Խ�������
	
//	==================DeCode�����ķ���ֵ===========================================	
	public static final byte CODEC_CMD       = 0;
	public static final byte CODEC_RESP      = 1;
	public static final byte CODEC_TRANS     = 2;
	public static final byte CODEC_NEED_DATA = 3;
	public static final byte CODEC_ERR       = 4;
}
