package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.CurrStatus;
import util.MsgBean;

public class ProjectInfoBean extends RmiBean 
{
	public final static long serialVersionUID = RmiBean.RMI_PROJECT_INFO;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public ProjectInfoBean()
	{
		super.className = "ProjectInfoBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0, 25);
		switch(currStatus.getCmd())
		{
			case 10://添加
			case 11://编辑
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
				msgBean = pRmi.RmiExec(0, this, 0, 25);
			case 0://查询
		    	request.getSession().setAttribute("Project_Info_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Project_Info.jsp?Sid=" + Sid);		    
		    	break;
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	}
	
	//项目ID检测
	public void IdCheck(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone)
	{
		try 
		{
			getHtmlData(request);
			currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
			
			PrintWriter outprint = response.getWriter();
			String Resp = "3006";
			
			msgBean = pRmi.RmiExec(2, this, 0, 25);//查找是否有该项目存在
			System.out.println("msgBean.getStatus():" + msgBean.getStatus());
			switch(msgBean.getStatus())
			{
				case 0://已存在
					Resp = "3006";
					break;
				default://可用
					Resp = "0000";
					break;
			}
			
			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
			outprint.write(Resp);
		}
		catch (Exception Ex)
		{
			Ex.printStackTrace();
		}
	}
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://查询
				Sql = " select  t.id, t.cname, t.Longitude, t.Latitude, t.MapLev , t.MapAngle , t.Demo "
						+ " from project_info t order by t.id";
				break;
			case 2://设备ID检测
				Sql = " select  t.id, t.cname " +
					  " from Project_info t " +
					  " where t.id= '"+ id +"'";
				break;
			case 10://添加
				Sql = " insert into project_info( id, cname, Longitude, Latitude, MapLev, MapAngle, Demo)" +
					  " values('"+ id +"', '"+ cname +"', '"+ longitude +"', '"+ latitude +"', '"+ mapLev +"', '"+ mapAngle +"', '"+ demo +"')";
				break;
			case 11://编辑
				Sql = " update project_info t set t.cname= '"+ cname +"', t.longitude= '"+ longitude +"', t.latitude= '"+ latitude +"', t.mapLev= '"+ mapLev +"' , t.mapAngle= '"+ mapAngle +"' , t.dDemo= '"+ demo +"' " +
					  " where t.id = '"+ id +"'";
				break;
		}
		return Sql;
	}
	
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setId(pRs.getString(1));
			setCname(pRs.getString(2));
			setLongitude(pRs.getString(3));
			setLatitude(pRs.getString(4));
			setMapLev(pRs.getString(5));
			setMapAngle(pRs.getString(6));
			setDemo(pRs.getString(7));
		}
		catch (SQLException sqlExp)
		{
			sqlExp.printStackTrace();
		}
		return IsOK;
	}
	
	public boolean getHtmlData(HttpServletRequest request)
	{
		boolean IsOK = true;
		try
		{
			setId(CommUtil.StrToGB2312(request.getParameter("id")));
			setCname(CommUtil.StrToGB2312(request.getParameter("cname")));
			setLongitude(CommUtil.StrToGB2312(request.getParameter("longitude")));
			setLatitude(CommUtil.StrToGB2312(request.getParameter("latitude")));
			setMapLev(CommUtil.StrToGB2312(request.getParameter("mapLev")));
			setMapAngle(CommUtil.StrToGB2312(request.getParameter("mapAngle")));
			setDemo(CommUtil.StrToGB2312(request.getParameter("demo")));
			
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String id;
	private String cname;
	private String longitude;
	private String latitude;
	private String mapLev;
	private String mapAngle;
	private String demo;
	
	private String Sid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getMapLev() {
		return mapLev;
	}

	public void setMapLev(String mapLev) {
		this.mapLev = mapLev;
	}

	public String getMapAngle() {
		return mapAngle;
	}

	public void setMapAngle(String mapAngle) {
		this.mapAngle = mapAngle;
	}

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
	
	
}