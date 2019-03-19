#include <stdio.h> 
#include <sys/types.h> 
#include <netinet/in.h>
#include <sys/socket.h> 
#include <string.h> 
#include <unistd.h> 
#include <stdlib.h>
#include <assert.h>



int main(int argc,char *argv[]) {
    if(argc < 2 ) {
        printf("Pas assez d'arguments\n");
        exit(1);
    }

	char* server_name = argv[1];
	short server_port = atoi(argv[2]);
	
	struct sockaddr_in server_address;
	server_address.sin_family = AF_INET;
	server_address.sin_port = htons(server_port);
	server_address.sin_addr.s_addr = INADDR_ANY;
	
	
	int sockID = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
	if(sockID < 0) {
		printf("Erreur lors de la création du socket\n");
		exit(1);
	}
	
    connect(sockID,(struct sockaddr*)&server_address,sizeof(server_address));
	char data[1024];
	while(fgets(data, 1024, stdin) != NULL)
        send(sockID,data,strlen(data),0);
	
	close(sockID);
	return 0;
}
