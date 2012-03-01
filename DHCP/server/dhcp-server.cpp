 
#include "dhcp-server.h"

int main()
{
	server serverObject;
	FILE * pFile1;
	pFile1 = fopen("dhcp-server.log", "w");
	fclose(pFile1);	
	
	serverObject.start();
	close(serverObject.serverInUDPfd);
	close(serverObject.serverOutUDPfd);
}

void server::start()
{
	readConfig();
	createServerUDPSocket();
	start_Receiving_Messages();
}

void server::write_log_file(int type, uint32_t assignedAddr, char *macAddr)
{
	char fileData[1000];
	struct in_addr client;
	char clientAddr[32];

	client.s_addr = assignedAddr;
	strcpy(clientAddr,inet_ntoa(client));

	if(type==1)
	{
		FILE * pFile1;
		pFile1 = fopen("dhcp-server.log", "a");
		sprintf(fileData," IP address was Assigned:\n Machine: %02X:%02X:%02X:%02X:%02X:%02X \n IP: %s\n\n",macAddr[0],macAddr[1],macAddr[2],macAddr[3],macAddr[4],macAddr[5],clientAddr);
		fputs(fileData,pFile1);
		fclose(pFile1);	
	}
	else if(type ==2)
	{
		FILE * pFile1;
		pFile1 = fopen("dhcp-server.log", "a");
		sprintf(fileData," IP address was Renewed:\n Machine: %02X:%02X:%02X:%02X:%02X:%02X \n IP: %s\n\n",macAddr[0],macAddr[1],macAddr[2],macAddr[3],macAddr[4],macAddr[5],clientAddr);
		fputs(fileData,pFile1);
		fclose(pFile1);	
	}	
}

void server::readConfig()
{
	/*char start_IP[100];
   	char end_IP[100];
	char subnet[100];*/
	int lease;
	
	string filename="dhcp-server.config";
	
	ifstream inConfig(filename.c_str());
	int j=0,k=0;
	
	if(inConfig.is_open()) 
	{
	
		string sLine;
		while(getline(inConfig, sLine)) 
		{
			string::size_type i = sLine.find_first_not_of(" \t\n\v");
			
			// Commented line: ignore
			if(i != string::npos && sLine[i] == '#')
			continue;
		
			// Process non-comment line
			if(i != string::npos)
			{
				sLine.c_str();
				istringstream str(sLine.c_str());
				str>>start_IP;
				str>>end_IP;
				str>>subnet;
				str>>lease;
			}
		}	 
	}
	else
	{
		perror("Config open:");
		exit(0);
	}
	start_ip_range=inet_addr(start_IP);
	end_ip_range=inet_addr(end_IP);
	subnet_mask=inet_addr(subnet);
	leaseTime=lease;
	next_ip_to_assign=0;
	cout<<"Start IP: "<<start_IP<<"\n"<<"End IP: "<<end_IP<<"\n"<<"Subnet: "<<subnet<<"\n"<<"Lease: "<<lease<<" seconds "<<endl; 
	inConfig.close();
	validate_config_file(); // validate the config file 
}

void server::validate_config_file()
{
	struct sockaddr_in antelope;
	if(inet_aton(start_IP, &antelope.sin_addr)!=1)
	{
		cout <<"Start IP Range Is Invalid... exiting ..."<<endl;
		exit(0);
	}
	
	if(inet_aton(end_IP, &antelope.sin_addr)!=1)
	{
		cout <<"End IP Range Is Invalid... exiting ..."<<endl;
		exit(0);
	}
	if(inet_aton(subnet, &antelope.sin_addr)!=1)
	{
		cout <<"Subnet Is Invalid... exiting ..."<<endl;
		exit(0);
	}
	if(ntohl(start_ip_range)>ntohl(end_ip_range))	
	{
		cout <<"Start IP Range Is Greater Than End IP Range ... exiting ..."<<endl;
		exit(0);
	}
}

void server::start_Receiving_Messages()
{
	int numBytes;

	int fromlen = sizeof(ServerInComingAddr);

	//cout <<"Entering receive loop in server"<<endl;
	while(1)
	{	
		char receiveBuffer[236+24];
		int receiveBufferSize = 236+24;
		if((numBytes = recvfrom(serverInUDPfd, receiveBuffer, receiveBufferSize, 0,(struct sockaddr *) &ServerInComingAddr,  (socklen_t *)&fromlen))==-1)
			{
				perror("DEBUG : Receive From IN Server ");		
			}
		//cout << "Write a function to process the received data from a client"<<endl;
		procecss_Received_Message(receiveBuffer);		
	}
	
}

void server::get_ip_to_assign()
{
	if(next_ip_to_assign==0)
	{
		next_ip_to_assign = start_ip_range;
		ip_in_host_byte = ntohl(start_ip_range);
		
	//	cout <<" here "<<next_ip_to_assign <<" and "<<ip_in_host_byte<<endl;
	}
	else
	{	
		//cout <<" ip in host byte "<<ip_in_host_byte<<endl;
		//cout <<" end ip in host byte "<<ntohl(end_ip_range)<<endl;
		if(ip_in_host_byte < ntohl(end_ip_range))
		{
			struct in_addr in;
			
			ip_in_host_byte++;
			in.s_addr=htonl(ip_in_host_byte); // convert IP back to host byte order 
			next_ip_to_assign = in.s_addr;
		}
		else
		{
			cout<<" Server is out of ip addresses to assign.."<<endl;
			cout<<" Now exiting.."<<endl;
			exit(0);
		}	
	}
	//cout <<"left"<<endl;	
}

void server::procecss_Received_Message(char *receiveBuff)
{
	//cout <<"Came in processing messages "<<endl;
	struct optionsStruct displayOption[4];
	struct messageStruct message;
	
	memmove(&message.op,receiveBuff,1);
	memmove(&message.htype,receiveBuff+1,1);
	memmove(&message.hlen,receiveBuff+2,1);
	memmove(&message.hops,receiveBuff+3,1);
	memmove(&message.xid,receiveBuff+4,4);
	memmove(&message.secs,receiveBuff+8,2);
	memmove(&message.flags,receiveBuff+10,2);
	memmove(&message.ciaddr,receiveBuff+12,4);
	memmove(&message.yiaddr,receiveBuff+16,4);
	memmove(&message.siaddr,receiveBuff+20,4);
	memmove(&message.giaddr,receiveBuff+24,4);
	memmove(&message.chaddr,receiveBuff+28,16);
	memmove(&message.sname,receiveBuff+44,64);
	memmove(&message.file,receiveBuff+108,128);
	memmove(&displayOption[0].optionType,receiveBuff+236,4);
	memmove(&displayOption[0].optionVal,receiveBuff+240,4); //message type
	memmove(&displayOption[1].optionType,receiveBuff+244,4);
	memmove(&displayOption[1].optionVal,receiveBuff+248,4);// ip requested
	memmove(&displayOption[2].optionType,receiveBuff+252,4);
	memmove(&displayOption[2].optionVal,receiveBuff+256,4); // server's ip
	
/*	
	cout <<"message op     :: "<<(int)message.op<<endl;
	cout <<"message htype  :: "<<(int)message.htype<<endl;
	cout <<"message hlen   :: "<<(int)message.hlen<<endl;
	cout <<"message hops   :: "<<(int)message.hops<<endl;
	cout <<"message xid    :: "<<message.xid<<endl;
	cout <<"message secs   :: "<<message.secs<<endl;
	cout <<"message flags  :: "<<message.flags<<endl;
	cout <<"message ciaddr :: "<<message.ciaddr<<endl;
	cout <<"message yiaddr :: "<<message.yiaddr<<endl;
	cout <<"message siaddr :: "<<message.siaddr<<endl;
	cout <<"message giaddr :: "<<message.giaddr<<endl;
	cout <<"message chaddr :: "<<message.chaddr<<endl;
	cout <<"message sname  :: "<<message.sname<<endl;
	cout <<"message file   :: "<<message.file<<endl;
	cout <<"message optionType 0 :: "<<displayOption[0].optionType<<endl;
	cout <<"message optionVal  0 :: "<<displayOption[0].optionVal<<endl;
	cout <<"message optionType 1 :: "<<displayOption[1].optionType<<endl;
	cout <<"message optionVal  1 :: "<<displayOption[1].optionVal<<endl;
	cout <<"message optionType 2 :: "<<displayOption[2].optionType<<endl;
	cout <<"message optionVal  2 :: "<<displayOption[2].optionVal<<endl;
*/	
	bool present =  false;
	int presentAt = 0;
	uint16_t clientStatus = 0;
	
	for(int i=0; i<clients.size(); i++)
	{
		//cout<<"memcmp(clients[i].clientIdentifier,message.chaddr,6) and i is "<<strncmp(clients[i].clientIdentifier,message.chaddr,6)<<" I "<<i<<endl;
		if(memcmp(clients[i].clientIdentifier,message.chaddr,6)==0)
		{
			present = true;
			presentAt = i;
			clientStatus = clients[i].clientStatus;
			//cout <<" client is there at "<<presentAt<<endl;
			//printf("from message: %02X:%02X:%02X:%02X:%02X:%02X",message.chaddr[0],message.chaddr[1],message.chaddr[2],message.chaddr[3],message.chaddr[4],message.chaddr[5]);
			//printf("from vector:: %02X:%02X:%02X:%02X:%02X:%02X",clients[i].clientIdentifier[0],clients[i].clientIdentifier[1],clients[i].clientIdentifier[2],clients[i].clientIdentifier[3],clients[i].clientIdentifier[4],clients[i].clientIdentifier[5]);
			break;
		}
	}
	
	if(present==false && displayOption[0].optionType==53 && displayOption[0].optionVal==0)
	{
		//struct clientInfo client;
		get_ip_to_assign();
		client.ip_addr=next_ip_to_assign; // assign IP here
		memcpy(client.clientIdentifier,message.chaddr,6);
		//printf("\n %X",client.clientIdentifier[1]);
		client.clientStatus=0;
		//cout <<" assigned 3 "<<clients.size()<<endl;
		//cout << client.ip_addr << "\t" << client.clientIdentifier << "\t" << client.clientStatus << endl;
		clients.push_back(client);
		struct in_addr in;
		in.s_addr = client.ip_addr;
		//cout <<" server will assign "<<inet_ntoa(in)<<endl;
		
		char sendBuffer[236+32];
		int sendBufferSize = 236+32;
		message.yiaddr=client.ip_addr;
		struct optionsStruct tempOption[4]; // instance of the optionsStruct for temporary use
		int numBytes;
		
		memmove(sendBuffer,&message.op,1);
		memmove(sendBuffer+1,&message.htype,1);
		memmove(sendBuffer+2,&message.hlen,1);
		memmove(sendBuffer+3,&message.hops,1);
		memmove(sendBuffer+4,&message.xid,4);
		memmove(sendBuffer+8,&message.secs,2);
		memmove(sendBuffer+10,&message.flags,2);
		memmove(sendBuffer+12,&message.ciaddr,4);
		memmove(sendBuffer+16,&message.yiaddr,4);
		memmove(sendBuffer+20,&message.siaddr,4);
		memmove(sendBuffer+24,&message.giaddr,4);
		memmove(sendBuffer+28,&message.chaddr,16);
		memmove(sendBuffer+44,&message.sname,64);
		memmove(sendBuffer+108,&message.file,128);
	
		// set the options 
		tempOption[0].optionType=53;
		tempOption[0].optionVal=1; // DHCPOFFER Message
		tempOption[1].optionType=1;
		tempOption[1].optionVal=subnet_mask; // assign Subnet Mask
		tempOption[2].optionType=51;
		tempOption[2].optionVal=leaseTime; // assign Lease Time
		tempOption[3].optionType=54;
		tempOption[3].optionVal=server_ip_address; // Pass Server's IP Address
		
		memmove(sendBuffer+236,&tempOption[0].optionType,4);
		memmove(sendBuffer+240,&tempOption[0].optionVal,4);
		memmove(sendBuffer+244,&tempOption[1].optionType,4);
		memmove(sendBuffer+248,&tempOption[1].optionVal,4);
		memmove(sendBuffer+252,&tempOption[2].optionType,4);
		memmove(sendBuffer+256,&tempOption[2].optionVal,4);
		memmove(sendBuffer+260,&tempOption[3].optionType,4);
		memmove(sendBuffer+264,&tempOption[3].optionVal,4);
		
		//Now send the message
		if((numBytes=sendto(serverOutUDPfd, sendBuffer, sendBufferSize, 0, (struct sockaddr *)&ServerOutGoingAddr, sizeof(struct sockaddr)))==-1)
		{
			perror("DEBUG: SendTo DHCP OFFER");
		}
	}
	else if(present == true && displayOption[0].optionType==53 && displayOption[0].optionVal==2 && message.ciaddr!=clients[presentAt].ip_addr)
	{
		//verify if the ip requested is same as the ip assigned , if not send dhcpnack else send dhacp ack
		
		//cout<<" client is in the vector at "<<presentAt<<endl;
		if(displayOption[1].optionVal == clients[presentAt].ip_addr)
		{
			//cout <<" Requesting same as offered "<<endl;
			//send DHCPACK Message
			
			char sendBuffer[236+32];
			int sendBufferSize = 236+32;
			message.yiaddr=clients[presentAt].ip_addr;
			struct optionsStruct tempOption[4]; // instance of the optionsStruct for temporary use
			int numBytes;
			
			memmove(sendBuffer,&message.op,1);
			memmove(sendBuffer+1,&message.htype,1);
			memmove(sendBuffer+2,&message.hlen,1);
			memmove(sendBuffer+3,&message.hops,1);
			memmove(sendBuffer+4,&message.xid,4);
			memmove(sendBuffer+8,&message.secs,2);
			memmove(sendBuffer+10,&message.flags,2);
			memmove(sendBuffer+12,&message.ciaddr,4);
			memmove(sendBuffer+16,&message.yiaddr,4);
			memmove(sendBuffer+20,&message.siaddr,4);
			memmove(sendBuffer+24,&message.giaddr,4);
			memmove(sendBuffer+28,&message.chaddr,16);
			memmove(sendBuffer+44,&message.sname,64);
			memmove(sendBuffer+108,&message.file,128);
		
			// set the options 
			tempOption[0].optionType=53;
			tempOption[0].optionVal=3; // DHCPACK Message
			tempOption[1].optionType=1;
			tempOption[1].optionVal=subnet_mask; // assign Subnet Mask
			tempOption[2].optionType=51;
			tempOption[2].optionVal=leaseTime; // assign Lease Time
			tempOption[3].optionType=54;
			tempOption[3].optionVal=server_ip_address; // Pass Server's IP Address
			
			memmove(sendBuffer+236,&tempOption[0].optionType,4);
			memmove(sendBuffer+240,&tempOption[0].optionVal,4);
			memmove(sendBuffer+244,&tempOption[1].optionType,4);
			memmove(sendBuffer+248,&tempOption[1].optionVal,4);
			memmove(sendBuffer+252,&tempOption[2].optionType,4);
			memmove(sendBuffer+256,&tempOption[2].optionVal,4);
			memmove(sendBuffer+260,&tempOption[3].optionType,4);
			memmove(sendBuffer+264,&tempOption[3].optionVal,4);
			
			//Now send the message
			if((numBytes=sendto(serverOutUDPfd, sendBuffer, sendBufferSize, 0, (struct sockaddr *)&ServerOutGoingAddr, sizeof(struct sockaddr)))==-1)
			{
				perror("DEBUG: SendTo DHCPACK");
				exit(0);
			}
			write_log_file(1,message.yiaddr,clients[presentAt].clientIdentifier);
		}
		else
		{
			char sendBuffer[236+32];
			int sendBufferSize = 236+32;
			message.yiaddr=0;
			struct optionsStruct tempOption[4]; // instance of the optionsStruct for temporary use
			int numBytes;
			
			memmove(sendBuffer,&message.op,1);
			memmove(sendBuffer+1,&message.htype,1);
			memmove(sendBuffer+2,&message.hlen,1);
			memmove(sendBuffer+3,&message.hops,1);
			memmove(sendBuffer+4,&message.xid,4);
			memmove(sendBuffer+8,&message.secs,2);
			memmove(sendBuffer+10,&message.flags,2);
			memmove(sendBuffer+12,&message.ciaddr,4);
			memmove(sendBuffer+16,&message.yiaddr,4);
			memmove(sendBuffer+20,&message.siaddr,4);
			memmove(sendBuffer+24,&message.giaddr,4);
			memmove(sendBuffer+28,&message.chaddr,16);
			memmove(sendBuffer+44,&message.sname,64);
			memmove(sendBuffer+108,&message.file,128);
		
			// set the options 
			tempOption[0].optionType=53;
			tempOption[0].optionVal=4; // DHCPNACK Message
			
			memmove(sendBuffer+236,&tempOption[0].optionType,4);
			memmove(sendBuffer+240,&tempOption[0].optionVal,4);
			
			//Now send the message
			if((numBytes=sendto(serverOutUDPfd, sendBuffer, sendBufferSize, 0, (struct sockaddr *)&ServerOutGoingAddr, sizeof(struct sockaddr)))==-1)
			{
				perror("DEBUG: SendTo DHCPNACK");
			}
			
		}
	}
	else if(present == true && displayOption[0].optionType==53 && displayOption[0].optionVal==2 && message.ciaddr==clients[presentAt].ip_addr)
	{
		//cout<<"client wants to renew the ip "<<endl;
		char sendBuffer[236+32];
		int sendBufferSize = 236+32;
		message.yiaddr=message.ciaddr;
		struct optionsStruct tempOption[4]; // instance of the optionsStruct for temporary use
		int numBytes;
		
		memmove(sendBuffer,&message.op,1);
		memmove(sendBuffer+1,&message.htype,1);
		memmove(sendBuffer+2,&message.hlen,1);
		memmove(sendBuffer+3,&message.hops,1);
		memmove(sendBuffer+4,&message.xid,4);
		memmove(sendBuffer+8,&message.secs,2);
		memmove(sendBuffer+10,&message.flags,2);
		memmove(sendBuffer+12,&message.ciaddr,4);
		memmove(sendBuffer+16,&message.yiaddr,4);
		memmove(sendBuffer+20,&message.siaddr,4);
		memmove(sendBuffer+24,&message.giaddr,4);
		memmove(sendBuffer+28,&message.chaddr,16);
		memmove(sendBuffer+44,&message.sname,64);
		memmove(sendBuffer+108,&message.file,128);
	
		// set the options 
		tempOption[0].optionType=53;
		tempOption[0].optionVal=3; // DHCPACK Message
		tempOption[1].optionType=1;
		tempOption[1].optionVal=subnet_mask; // assign Subnet Mask
		tempOption[2].optionType=51;
		tempOption[2].optionVal=leaseTime; // assign Lease Time
		tempOption[3].optionType=54;
		tempOption[3].optionVal=server_ip_address; // Pass Server's IP Address
		
		memmove(sendBuffer+236,&tempOption[0].optionType,4);
		memmove(sendBuffer+240,&tempOption[0].optionVal,4);
		memmove(sendBuffer+244,&tempOption[1].optionType,4);
		memmove(sendBuffer+248,&tempOption[1].optionVal,4);
		memmove(sendBuffer+252,&tempOption[2].optionType,4);
		memmove(sendBuffer+256,&tempOption[2].optionVal,4);
		memmove(sendBuffer+260,&tempOption[3].optionType,4);
		memmove(sendBuffer+264,&tempOption[3].optionVal,4);
		
		//Now send the message
		if((numBytes=sendto(serverOutUDPfd, sendBuffer, sendBufferSize, 0, (struct sockaddr *)&ServerOutGoingAddr, sizeof(struct sockaddr)))==-1)
		{
			perror("DEBUG: SendTo DHCPACK while RENEWING");
		}
		write_log_file(2,message.yiaddr,clients[presentAt].clientIdentifier);
	}
	else if(present==true && displayOption[0].optionType==53 && displayOption[0].optionVal==0)
	{
		cout <<" client send more than once DHCPDISCOVER eventhough i sent the dhcpoffer"<<endl;
	}
}


void server::createServerUDPSocket()
{
	//cout << "Server Started"<<endl;

	int sin_size;
	struct sockaddr_in serverAddr;
	int broadcast = 1;
	struct hostent *h;
	unsigned long IP; //my ip address
	
	//open UDP Port
	serverInUDPfd = socket(PF_INET, SOCK_DGRAM, 0);

	//Bind to incoming Port
	serverAddr.sin_family = AF_INET;
	serverAddr.sin_port = 67;
	serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
	sin_size = sizeof( struct sockaddr_in);

	if (bind(serverInUDPfd, (struct sockaddr *) &serverAddr, sizeof(sockaddr)) != 0)
	{
		perror("DEBUG: ERROR in binding in Server");
		exit(1);
	}
	if((getsockname(serverInUDPfd, (struct sockaddr *)&serverAddr, (socklen_t *)&sin_size)) == -1) 
	{	
		perror("DEBUG: getsockname in Server");
		exit(1);
	}
	
	//cout <<"Server's In Port Number is "<<ntohs(serverAddr.sin_port)<<endl;

	//Create OutGoing Socket
	serverOutUDPfd = socket(PF_INET, SOCK_DGRAM, 0);

	//Bind to incoming Port
	ServerOutGoingAddr.sin_family = AF_INET;
	ServerOutGoingAddr.sin_port = 68;
	ServerOutGoingAddr.sin_addr.s_addr = INADDR_BROADCAST;
	
	//cout <<"Server's Out  Port Number is "<<ntohs(ServerOutGoingAddr.sin_port)<<endl;

	if (setsockopt(serverOutUDPfd, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast)) == -1)
	{
		perror("setsockopt (SO_BROADCAST) in server");
		exit(1);
	}

	h=gethostbyname("localhost");
	server_ip_address=inet_addr(inet_ntoa(*((struct in_addr *)h->h_addr)));

	cout<<"server host name is "<<"mainServer"<<" and IP address is "<<inet_ntoa(*((struct in_addr *)h->h_addr))<<endl; 
	
}

