#Makefile

CXX       =      g++

FLAGS     = 	${CPPFLAGS} ${AC_DEFS}

client: dhcp-client.o tools.o timers.o 
	g++ -o dhcp-client dhcp-client.o tools.o timers.o -lnsl -lresolv

dhcp-client.o: dhcp-client.cpp dhcp-client.h
	g++ -c dhcp-client.cpp
	
tools.o: tools.cc tools.hh
	$(CXX) $(FLAGS) -c tools.cc

timers.o: timers.cc timers.hh
	$(CXX) $(FLAGS) -c timers.cc

clean:
	rm dhcp-client dhcp-client.o tools.o timers.o

