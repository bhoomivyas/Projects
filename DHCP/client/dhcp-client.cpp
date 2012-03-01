 
#include "dhcp-client.h"

int main()
{
	char fileData[50];
	FILE * pFile1;
	pFile1 = fopen("client-config.log", "w");
	fclose(pFile1);	

	client clientObject;
	clientObject.start();	
	close(clientObject.clientInUDPfd);
	close(clientObject.clientOutUDPfd);
}

void client::start()
{
	client_State=0; //client just started now send DHCPDISCOVER MESSAGE
	client_Expected_State=0;
	message_sent_counter=0;
	get_client_mac_addr();
	createClientUDPSocket();
	intialize_Message_Parameters();
	send_DHCP_Message();
	startListen();
}

void client::write_output_file()
{
	char fileData[1000];
	struct in_addr server;
	struct in_addr client;
	struct in_addr subnet;
	char clientAddr[32];
	char subnetAddr[32];

	server.s_addr = server_ip_addr;
	client.s_addr = offered_ip_addr;
	subnet.s_addr = offered_subnet;
	strcpy(clientAddr,inet_ntoa(client));
	strcpy(subnetAddr,inet_ntoa(subnet));

	FILE * pFile1;
	pFile1 = fopen("client-config.log", "a");
	cout <<"client : "<<clientAddr<<" server: "<<inet_ntoa(server)<<" subnet "<<subnetAddr<<endl;
	sprintf(fileData," Server_IP ADDRESS: %s \n IP_ASSIGNED_TO_ME %s \n SUBNET %s \n LEASE_TIME %d \n",inet_ntoa(server),clientAddr,subnetAddr,offered_lease_time);
	fputs(fileData,pFile1);
	fclose(pFile1);	
	
}

void client::get_client_mac_addr()
{
	int fd;
	int option=1;
	struct ifreq ifr;
	struct sockaddr_in *sa;
	unsigned char *macaddr;
	memset(&ifr, 0, sizeof(ifr));
	
	fd = socket(AF_INET,SOCK_DGRAM,0);
	
	if(fd < 0)
	{
		perror("Socket in mac addr:");
		exit(1); 
	} 
	
	strncpy(ifr.ifr_name, "eth0", sizeof(ifr.ifr_name));	
	if(ioctl(fd, SIOCGIFHWADDR, &ifr)!=0)
	{
		perror("ioctl");
		close(fd);
		exit(1);
	}
	char array[16];
	memcpy(array,(unsigned char *)&(ifr.ifr_hwaddr.sa_data),6);
	close(fd);
	memcpy(client_ID,array,6);
	//printf("mac:: %02X:%02X:%02X:%02X:%02X:%02X",client_ID[0],client_ID[1],client_ID[2],client_ID[3],client_ID[4],client_ID[5]);
}

void client::intialize_Message_Parameters()
{
	//set message structure's parameters to default
	message.op=0; // not configured for BOOTPREQUEST / BOOPREPLY
	message.htype=1;
	message.hlen=6;
	message.hops=0;
	srand((unsigned)time(0));
	message.xid=rand();
	//cout <<" Clients xid is "<<message.xid<<endl;
	message.secs=0;
	message.flags=0;
	message.ciaddr=0;
	message.yiaddr=0;
	message.siaddr=0;
	message.giaddr=0;
	memcpy(message.chaddr,client_ID,6);
	strcpy(message.sname,"\0");
	strcpy(message.file,"\0");
}


void client::send_DHCP_Message()
{
	//Message will be 236 + options in bytes
	int numBytes;

	if(client_State == 0)
	{
		//sending DHCPDISCOVER Message
		//4 bytes for optionType and 4 bytes for optionVal 
		char sendBuffer[236+8];
		int sendBufferSize = 236+8;
		struct optionsStruct tempOption; // instance of the optionsStruct for temporary use

		tempOption.optionType=53;
		tempOption.optionVal = 0; // DHCPDISCOVER MESSAGE
		//message.xid = generate a random number for all the transactions
		
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
		memmove(sendBuffer+236,&tempOption.optionType,4);
		memmove(sendBuffer+240,&tempOption.optionVal,4);
		
		// Now BroadCast this message
		if((numBytes=sendto(clientOutUDPfd, sendBuffer, sendBufferSize, 0, (struct sockaddr *)&clientOutGoingAddr, sizeof(struct sockaddr)))==-1)
		{
			perror("DEBUG: SendTo DHCPDISCOVER ");
			exit(0);
		}
		client_Expected_State = 2;
	}

	if(client_State == 2)
	{
		//sending DHCPREQUEST Message
		char sendBuffer[236+24];
		int sendBufferSize = 236+24;
		struct optionsStruct tempOption[3]; // instance of the optionsStruct for temporary use

		tempOption[0].optionType=53;
		tempOption[0].optionVal = 2; // DHCPREQUEST MESSAGE
		tempOption[1].optionType=50;
		tempOption[1].optionVal = offered_ip_addr; // IP Requested
		tempOption[2].optionType=54;
		tempOption[2].optionVal = server_ip_addr; // Server IP
	
		//message.xid = generate a random number for all the transactions
		
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
		memmove(sendBuffer+236,&tempOption[0].optionType,4);
		memmove(sendBuffer+240,&tempOption[0].optionVal,4); // message Type
		memmove(sendBuffer+244,&tempOption[1].optionType,4);
		memmove(sendBuffer+248,&tempOption[1].optionVal,4); // ip requested
		memmove(sendBuffer+252,&tempOption[2].optionType,4);
		memmove(sendBuffer+256,&tempOption[2].optionVal,4); // server ip
		
		// Now BroadCast this message
		if((numBytes=sendto(clientOutUDPfd, sendBuffer, sendBufferSize, 0, (struct sockaddr *)&clientOutGoingAddr, sizeof(struct sockaddr)))==-1)
		{
			perror("DEBUG: SendTo DHCPREQUEST ");
			exit(0);
		}
		client_Expected_State = 4;
	}

	if(client_State == 4)
	{
		//cout <<"will send renew message "<<endl;
		intialize_Message_Parameters();
		message_sent_counter=0;
		message.ciaddr = offered_ip_addr;
		
		//sending DHCPREQUEST Message
		char sendBuffer[236+24];
		int sendBufferSize = 236+24;
		struct optionsStruct tempOption[3]; // instance of the optionsStruct for temporary use

		tempOption[0].optionType=53;
		tempOption[0].optionVal = 2; // DHCPREQUEST MESSAGE
		tempOption[1].optionType=50;
		tempOption[1].optionVal = offered_ip_addr; // IP Requested
		tempOption[2].optionType=54;
		tempOption[2].optionVal = server_ip_addr; // Server IP
		
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
		memmove(sendBuffer+236,&tempOption[0].optionType,4);
		memmove(sendBuffer+240,&tempOption[0].optionVal,4); // message Type
		memmove(sendBuffer+244,&tempOption[1].optionType,4);
		memmove(sendBuffer+248,&tempOption[1].optionVal,4); // ip requested
		memmove(sendBuffer+252,&tempOption[2].optionType,4);
		memmove(sendBuffer+256,&tempOption[2].optionVal,4); // server ip
		
		// Now BroadCast this message
		// try to unicasst this message 
		//Create Destination Addr Structure for sendo()
		struct sockaddr_in destaddr;
		destaddr.sin_family = AF_INET; // host byte order
		destaddr.sin_port = 67; // short, network byte order
		destaddr.sin_addr.s_addr = server_ip_addr;
		memset(&(destaddr.sin_zero), '\0', 8); // zero the rest of the struct
		//End of structure creation
		if((numBytes=sendto(clientOutUDPfd, sendBuffer, sendBufferSize, 0, (struct sockaddr *)&destaddr, sizeof(struct sockaddr)))==-1)
		{
			perror("DEBUG: SendTo DHCPREQUEST while RENEW");
			exit(0);
		}
		client_Expected_State = 4;
		client_State = 2;
		
	}
	
}

void client::processReceive()
{
	int numBytes;
	char receiveBuffer[236+32];
	int receiveBufferSize = 236+32;	

	int fromlen = sizeof(clientInComingAddr);
	//cout <<"Entering receive loop in client"<<endl;


	if((numBytes = recvfrom(clientInUDPfd, receiveBuffer, receiveBufferSize, 0,(struct sockaddr *) &clientInComingAddr,  (socklen_t *)&fromlen))==-1)
		{
			perror("DEBUG : Receive From IN Client ");
			exit(0);
		}
	//cout << "Write a function to process the received data from a server"<<endl;
	process_Received_DHCP_Message(receiveBuffer);
}

void client::process_Received_DHCP_Message(char *receivedBuffer)
{

	uint32_t tempXid=0; // To validate if the message is for me or not
	uint32_t tempMyIP=0; // IP given to me by the server
	struct messageStruct tempMessage; // instance of the messageStruct for temporary use
	struct optionsStruct tempOption[4]; // instance of the optionsStruct for temporary use
	
	// now unfold the message
	memmove(&tempMessage.op,receivedBuffer,1);
	memmove(&tempMessage.htype,receivedBuffer+1,1);
	memmove(&tempMessage.hlen,receivedBuffer+2,1);
	memmove(&tempMessage.hops,receivedBuffer+3,1);
	memmove(&tempMessage.xid,receivedBuffer+4,4);
	memmove(&tempMessage.secs,receivedBuffer+8,2);
	memmove(&tempMessage.flags,receivedBuffer+10,2);
	memmove(&tempMessage.ciaddr,receivedBuffer+12,4);
	memmove(&tempMessage.yiaddr,receivedBuffer+16,4);
	memmove(&tempMessage.siaddr,receivedBuffer+20,4);
	memmove(&tempMessage.giaddr,receivedBuffer+24,4);
	memmove(&tempMessage.chaddr,receivedBuffer+28,16);
	memmove(&tempMessage.sname,receivedBuffer+44,64); 
	memmove(&tempMessage.file,receivedBuffer+108,128);
	memmove(&tempOption[0].optionType,receivedBuffer+236,4);
	memmove(&tempOption[0].optionVal,receivedBuffer+240,4);
	
	if(tempMessage.xid == message.xid) // Yes, this message is for me
	{
		if(tempOption[0].optionType == 53 && tempOption[0].optionVal == 1) // this message is DHCPOFFER
		{
			//Getting subnet mask and lease info sent by the server
			//cout << " GOT OFFER"<<endl; 
			memmove(&tempOption[1].optionType,receivedBuffer+244,4);
			memmove(&tempOption[1].optionVal,receivedBuffer+248,4); // subnet
			memmove(&tempOption[2].optionType,receivedBuffer+252,4);
			memmove(&tempOption[2].optionVal,receivedBuffer+256,4);// lease 
			memmove(&tempOption[3].optionType,receivedBuffer+260,4);
			memmove(&tempOption[3].optionVal,receivedBuffer+264,4);// server's IP
			
			offered_ip_addr = tempMessage.yiaddr;
			offered_subnet = tempOption[1].optionVal;
			offered_lease_time = tempOption[2].optionVal;
			server_ip_addr = tempOption[3].optionVal;
		
			struct in_addr in;
			in.s_addr = tempMessage.yiaddr;
		
			//cout <<"server gave me IP: "<<inet_ntoa(in)<<endl;
			in.s_addr=tempOption[1].optionVal;
			//cout <<"server gave me Subnet: "<<inet_ntoa(in)<<endl;
			//cout <<"server gave me Lease: "<<tempOption[2].optionVal<<endl;	
			in.s_addr=server_ip_addr;
			//cout <<"server gave me Server IP: "<<inet_ntoa(in)<<endl;
		
			client_State = 2; // Now I will send DHCPREQUEST Message
			send_DHCP_Message();
		}
		
		if(tempOption[0].optionType == 53 && tempOption[0].optionVal == 3) // this message is DHCPACK
		{
			//Getting subnet mask and lease info sent by the server
			memmove(&tempOption[1].optionType,receivedBuffer+244,4);
			memmove(&tempOption[1].optionVal,receivedBuffer+248,4); // subnet
			memmove(&tempOption[2].optionType,receivedBuffer+252,4);
			memmove(&tempOption[2].optionVal,receivedBuffer+256,4);// lease 
			memmove(&tempOption[3].optionType,receivedBuffer+260,4);
			memmove(&tempOption[3].optionVal,receivedBuffer+264,4);// server's IP
			
			offered_ip_addr = tempMessage.yiaddr;
			offered_subnet = tempOption[1].optionVal;
			offered_lease_time = tempOption[2].optionVal;
			server_ip_addr = tempOption[3].optionVal;
			struct in_addr in;
			in.s_addr = offered_ip_addr;
		
			//cout <<"server gave me IP: "<<inet_ntoa(in)<<endl;
			in.s_addr=offered_subnet;
			//cout <<"server gave me Subnet: "<<inet_ntoa(in)<<endl;
			//cout <<"server gave me Lease: "<<tempOption[2].optionVal<<endl;	
			//cout <<"server gave me Server IP: "<<tempOption[3].optionVal<<endl;
			
			client_State = 4; // Now I will send DHCPRENEW Message
			cout << " received dhcp ack..now sleeping "<<endl;
			write_output_file();
			sleep(offered_lease_time);
			//cout<<" client Got up"<<endl;
			send_DHCP_Message();
		
		}
		if(tempOption[0].optionType == 53 && tempOption[0].optionVal == 4) // this message is DHCPNACK
		{
			//restart configuration process
			client_State=0; //client just started now send DHCPDISCOVER MESSAGE
			client_Expected_State=0;
			message_sent_counter=0;
			intialize_Message_Parameters();
			send_DHCP_Message();
			
		}
	}
	
}

void client::startListen()
{
	TimerCallback *tcb;
	struct timeval tv;
	getTime(&tv);

	fd_set read_FDs;
	int newFD,maxFD;;
	int i,j,nbytes;
	fd_set masterFD;
	FD_ZERO(&masterFD);    // clear the master and temp sets
	FD_ZERO(&read_FDs);
	FD_SET(clientInUDPfd,&masterFD);	
	maxFD=clientInUDPfd; // Set this FD as a maxFD 
	
	//fprintf(stderr,"Start time %d.%06d\n",(int)tv.tv_sec, (int)tv.tv_usec);
	// Create the timer event management class
	timersManager_ = new Timers;

	// Create callback classes and set up pointers
	// Add timers to the event queue and specify the timer in ms.
	tcb = new TestTimer2(this, 1);
	timersManager_->AddTimer(1000, tcb);//start time according its config value
	
	struct timeval tmv;
	int status;

	// Change while condition to reflect what is required for Project 1
	// ex: Routing table stabalization. 
	while (1) {
		read_FDs=masterFD;
		timersManager_->NextTimerTime(&tmv);
		if (tmv.tv_sec == 0 && tmv.tv_usec == 0) {
		        // The timer at the head on the queue has expired 
		        timersManager_->ExecuteNextTimer();
			continue;
		}
		if (tmv.tv_sec == MAXVALUE && tmv.tv_usec == 0){
		        // There are no timers in the event queue 
		        break;
		}		  
		/* The select call here will wait for tv seconds before expiring 
		 * You need to  modifiy it to listen to multiple sockets and add code for 
		 * packet processing. Refer to the select man pages or "Unix Network 
		 * Programming" by R. Stevens Pg 156.
		 */
		status = select(maxFD+1, &read_FDs, NULL, NULL, &tmv);
		
		if (status < 0){
			// This should not happen
			fprintf(stderr, "Select returned %d\n", status);
		}else{
			if (status == 0){
			
				//cout<<"Timer Expired...Obtaining IP Address..."<<endl;
				
				if(client_Expected_State !=client_State)
				{
					if(message_sent_counter<4)
					{
						send_DHCP_Message();
						message_sent_counter++;
					}
					else
					{
						cout<<"Client is unable to obtain IP from server...Exiting..."<<endl;
						exit(0);
					}
				}
				
				// Timer expired, Hence process it 
				timersManager_->ExecuteNextTimer();
				// Execute all timers that have expired.
				timersManager_->NextTimerTime(&tmv);
				while(tmv.tv_sec == 0 && tmv.tv_usec == 0){
					// Timer at the head of the queue has expired 
					timersManager_->ExecuteNextTimer();
					timersManager_->NextTimerTime(&tmv);					
				}
			}
			if (status > 0){
				// run through the existing connections looking for data to read
				for(i = 0; i <= maxFD; i++) {
					if (FD_ISSET(i, &read_FDs)) { // we got one!!
						processReceive();
					}
				}
			}
		}
	}
}

void client::createClientUDPSocket()
{
	//cout << "Client Started"<<endl;	
	int sin_size;
	int broadcast = 1;	

	//open UDP Port
	clientOutUDPfd = socket(PF_INET, SOCK_DGRAM, 0);
	clientInUDPfd = socket(PF_INET, SOCK_DGRAM, 0);
	struct sockaddr_in clientAddr;
	
	//set Incoming Socket
	clientAddr.sin_family = AF_INET;
	clientAddr.sin_port = 68;
	clientAddr.sin_addr.s_addr = INADDR_ANY;
	sin_size = sizeof( struct sockaddr_in);
	
	//bind to incoming scoket
	if (bind(clientInUDPfd, (struct sockaddr *) &clientAddr, sizeof(sockaddr)) != 0)
	{
		perror("DEBUG: ERROR in binding in client");
		exit(1);
	}

	if((getsockname(clientInUDPfd, (struct sockaddr *)&clientAddr, (socklen_t *)&sin_size)) == -1) 
	{
		perror("DEBUG: getsockname in client");
		exit(1);
	}
	
	
	//cout <<"Client's In Port Number is "<<ntohs(clientAddr.sin_port)<<endl;	
	
	
	//Set OutGoing socket
	clientOutGoingAddr.sin_family = AF_INET;
	clientOutGoingAddr.sin_port = 67;
	clientOutGoingAddr.sin_addr.s_addr = INADDR_BROADCAST;
	//cout <<"Client's Out Port Number is "<<ntohs(clientOutGoingAddr.sin_port)<<endl;

	//Port Created now try sending a boradcast message to the server 	
	//create a message first
	if (setsockopt(clientOutUDPfd, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast)) == -1)
	{
		perror("setsockopt (SO_BROADCAST)");
		exit(1);
	}	
}

int TestTimer2::Expire()
{
	struct timeval tv;
	getTime(&tv);
//	fprintf(stderr, "Timer 2 IN TRACKER  has expired %d times! Time %d.%06d\n",count_, (int)tv.tv_sec,(int)tv.tv_usec);
	count_++;
	fflush(NULL);

	return 0;
}
