#Makefile

server: dhcp-server.o
	g++ -o dhcp-server dhcp-server.o

dhcp-server.o: dhcp-server.cpp dhcp-server.h
	g++ -c dhcp-server.cpp

clean: 
	rm dhcp-server dhcp-server.o