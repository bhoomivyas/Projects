#include<iostream>
#include<fstream>
#include<string>
#include<stdlib.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<sys/time.h>
#include<sys/types.h>
#include<unistd.h>
#include<vector>
#include<sstream>
#include<netdb.h>
using namespace std;

class server
{
	public:
		int serverInUDPfd,serverOutUDPfd;
		struct sockaddr_in ServerInComingAddr, ServerOutGoingAddr;
		void createServerUDPSocket();
		void start();
		void start_Receiving_Messages();
		void procecss_Received_Message(char*);
		void readConfig();
		void get_ip_to_assign();
		void validate_config_file();
		void write_log_file(int,uint32_t,char *);

		char start_IP[100];
		char end_IP[100];
		char subnet[100];
		
		uint32_t start_ip_range, end_ip_range, subnet_mask, leaseTime, next_ip_to_assign, ip_in_host_byte;
		uint32_t server_ip_address;

		struct optionsStruct
		{
			uint32_t optionType;
			uint32_t optionVal;
		} option;
		
		struct messageStruct
		{
			uint8_t op;
			uint8_t htype;
			uint8_t hlen;
			uint8_t hops;
			uint32_t xid;
			uint16_t secs;
			uint16_t flags;
			uint32_t ciaddr;
			uint32_t yiaddr;
			uint32_t siaddr;
			uint32_t giaddr;
			char chaddr[16];
			char sname[64];
			char file[128];
					 
		};
		
		struct clientInfo
		{
			uint32_t ip_addr;
			char clientIdentifier[16];//MAC Address of the client
			int clientStatus;
		}client;
		
		vector <struct clientInfo> clients;

		
		
};
 
