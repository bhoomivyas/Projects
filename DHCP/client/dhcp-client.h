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
#include <sys/ioctl.h>
#include <net/if.h>
#include <ifaddrs.h>
using namespace std;

#include "timers.hh"

class client;

class TestTimer2: public TimerCallback {
public:
        TestTimer2(client *app, int count) : app_(app), count_(count) {};
        ~TestTimer2() {};
		client *app_;
        int count_;
	int Expire();
};



class client
{
	public:
		int clientInUDPfd,clientOutUDPfd;
		struct sockaddr_in clientOutGoingAddr,clientInComingAddr;
		void start();
		void startListen();
		void createClientUDPSocket();
		void processReceive();
		void intialize_Message_Parameters();
		void send_DHCP_Message();
		void process_Received_DHCP_Message(char *);
		void get_client_mac_addr();
		void write_output_file();
		int client_State;
		int client_Expected_State;
		char client_ID[16];
		int message_sent_counter;
		// if client_state == 0 send DHCPDISCOVER MESSAGE
		// if client_state == 1 receive DHCPOFFER MESSAGE
		// if client_state == 2 send DHCPREQUEST MESSAGE
		// if client_state == 3 receive DHCPACK OR DHCPNACK MESSAGE
		// if client_state == 4 send DHCPRENEW i.e DHCPREQUEST MESSAGE
		uint32_t offered_ip_addr, offered_subnet, offered_lease_time, server_ip_addr;

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
			//vector<optionsStruct> options;		 
		} message;
	protected:
		Timers *timersManager_;
};

