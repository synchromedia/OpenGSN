/**
 * Copyright 2009-2011 École de technologie supérieure,
 * Communication Research Centre Canada,
 * Inocybe Technologies Inc. and 6837247 CANADA Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greenstarnetwork.resources.servertechCWG.commandset;

/**
 * Servertech PDU command list
 * @author knguyen <synchromedia.ca>
 *
 */
public class CWGCommandList 
{
	public static final String PROMPT = "Switched CDU:";				//prompt 
	public static final String SUCCESS = "Command successful";			//command successful pattern
	public static final String ERROR_NOT_FOUND = "User/outlet -- name not found";	//Error pattern
	
	public static final String ON = "On";
	public static final String OFF = "Off";
	public static final String REBOOT = "Reboot";
	public static final String STATUS = "Status";
	public static final String ISTAT = "IStat";
	public static final String OSTAT = "OStat";
	public static final String CONNECT = "Connect";
	public static final String LOGIN = "Login";
	public static final String LOGOUT = "Logout";
	public static final String PASSWORD = "Password";
	public static final String QUIT = "Quit";
	public static final String LIST_OUTLETS = "List Outlets";
	public static final String LIST_PORTS = "List Ports";
	public static final String LIST_GROUPS = "List Groups";
	public static final String ADD_GROUPTOUSER = "Add Grouptouser";
	public static final String ADD_OUTLETTOGROUP = "Add Outlettogroup";
	public static final String ADD_OUTLETTOUSER = "Add Outlettouser";
	public static final String ADD_PORTTOUSER = "Add Porttouser";
	public static final String CREATE_GROUP = "Create Group";
	public static final String CREATE_USER = "Create User";
	public static final String DELETE_GROUPFROMUSER = "Delete Groupfromuser";
	public static final String DELETE_OUTLETFROMGROUP = "Delete Outletfromgroup";
	public static final String DELETE_OUTLETFROMUSER = "Delete Outletfromuser";
	public static final String DELETE_PORTFROMUSER = "Delete Portfromuser";
	public static final String LIST_USER = "List User";
	public static final String LIST_USERS = "List Users";
	public static final String REMOVE_GROUP = "Remove Group";
	public static final String REMOVE_USER = "Remove User";
	public static final String RESTART = "Restart";
	public static final String SET_BANNER = "Set Banner";
	public static final String SET_DHCP = "Set DHCP";
	public static final String SET_DNS = "Set DNS";
	public static final String SET_FTP_AUTOUPDATE_DAY = "Set FTP Autoupdate Day";
	public static final String SET_FTP_AUTOUPDATE_HOUR = "Set FTP Autoupdate HOUR";
	public static final String SET_FTP_DIRECTORY = "Set FTP Directory";
	public static final String SET_FTP_FILENAME = "Set FTP Filename";
	public static final String SET_FTP_FILEPATH = "Set FTP Filepath";
	public static final String SET_FTP_HOST = "Set FTP Host";
	public static final String SET_FTP_PASSWORD = "Set FTP Password";
	public static final String SET_FTP_USERNAME = "Set FTP Username";
	public static final String SET_FTP_SERVER = "Set FTP Server";
	public static final String SET_GATEWAY = "Set Gateway";
	public static final String SET_INFEED_NAME = "Set Infeed Name";
	public static final String SET_IP_ADDRESS = "Set IP Address";
	public static final String SET_MODEM_INITS = "Set Modem Inits";
	public static final String SET_MODEM = "Set Modem";
	public static final String SET_OPTION_TEMPSCALE = "Set Option Tempscale";
	public static final String SET_OUTLET_NAME = "Set Outlet Name";
	public static final String SET_OUTLET_POSTONDELAY = "Set Outlet PostOnDelay";
	public static final String SET_OUTLET_SEQINTERVAL = "Set Outlet SeqInterval";
	public static final String SET_OUTLET_WAKEUP = "Set Outlet Wakeup";
	public static final String SET_PORT_NAME = "Set Port Name";
	public static final String SET_PORT_DSRCHK = "Set Port Dsrchk";
	public static final String SET_PORT_SPEED = "Set Port Speed";
	public static final String SET_PORT_TIMEOUT = "Set Port Timeout";
	public static final String SET_SNTP = "Set SNTP";
	public static final String SET_SNTP_GMTOFFSET = "Set SNTP GMTOffset";
	public static final String SET_SUBNET = "Set Subnet";
	public static final String SET_TELNET = "Set Telnet";
	public static final String SET_TELNET_PORT = "Set Telnet Port";
	public static final String SET_TOWER_NAME = "Set Tower Name";
	public static final String SET_USER_ACCESS = "Set User Access";
	public static final String SET_USER_ENVMON = "Set User Envmon";
	public static final String SET_USER_PASSWORD = "Set User Password";
	public static final String SHOW_FTP = "Show FTP";
	public static final String SHOW_INFEEDS = "Show Infeeds";
	public static final String SHOW_NETWORK = "Show Network";
	public static final String SHOW_OPTIONS = "Show Options";
	public static final String SHOW_OUTLETS = "Show Outlets";
	public static final String SHOW_PORTS = "Show Ports";
	public static final String SHOW_SNTP = "Show SNTP";
	public static final String SHOW_SYSTEM = "Show System";
	public static final String SHOW_TOWERS = "Show Towers";
	public static final String VERSION = "Version";
}
