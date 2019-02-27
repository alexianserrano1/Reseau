#include <stdio.h> 
#include <sys/types.h> 
#include <netinet/in.h>
#include <sys/socket.h> 
#include <string.h> 
#include <unistd.h> 
#include <stdlib.h>

int inet_pton(int af, const char *src, void *dst);

int main(int argc,char *argv[]) {
	char* server_name = argv[1];
	short server_port = atoi(argv[2]);
	
	struct sockaddr_in server_address;
	server_address.sin_family = AF_INET;
	server_address.sin_port = htons(server_port);
	server_address.sin_addr.s_addr = INADDR_ANY;
	//inet_pton(AF_INET, server_name, &server_address.sin_addr);
	
	int sockID = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP);
	if(sockID < 0) {
		printf("Erreur lors de la création du socket\n");
		exit(1);
	}
	
	char data[25];
	while(fgets(data, 25, stdin) != NULL) {
		sendto(sockID, data, strlen(data), 0, 
			(struct sockaddr*)&server_address, sizeof(server_address));

		char inPacket[25];
		recvfrom(sockID, inPacket, 25, 0, 
			(struct sockaddr *) &server_address, NULL);
		printf("%s\n", inPacket);
		memset(inPacket, 0, sizeof(inPacket));
	}
	
	close(sockID);
	return 0;
}

