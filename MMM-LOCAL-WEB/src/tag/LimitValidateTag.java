package tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class LimitValidateTag extends TagSupport 
{
	private static final long serialVersionUID = 1L;
	public int doStartTag() throws JspException 
	{
		//��TagSupport���ж����һ�����ԣ�����javax.servlet.jsp.PageContext�Ķ���
		//HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		JspWriter out = pageContext.getOut();
		try
		{
			switch(ctype)
			{
				case 1://���ܵ�
					if(null != userrole && userrole.trim().length() > 0)
					{
						String Flag = "none";	
						String[] fp_list = userrole.split(",");
						for(int i=0; i<fp_list.length; i++)
						{
							if(fp_list[i].startsWith(fpid))
							{	
								Flag = "";
								break;
							}
						}
						out.print(Flag);
					}
					else
					{
						out.print("none");
					}				
					break;
				case 2://����
					break;
			}	
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//doStartTag()�������� SKIP_BODY ����Ȼ��ԭ�������ǵļ����ڱ��û�����ġ�
		return Tag.SKIP_BODY;
	}
	private String userrole;
	private String fpid;
	private int ctype = 0;
	
	
	public String getUserrole() {
		return userrole;
	}
	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}
	public String getFpid() {
		return fpid;
	}
	public void setFpid(String fpid) {
		this.fpid = fpid;
	}
	public int getCtype() {
		return ctype;
	}
	public void setCtype(int ctype) {
		this.ctype = ctype;
	}
}