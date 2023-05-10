# run in background connection to remote kubernetess
# by assumption remote username is same as local user
ssh -fNT -L 5432:localhost:5432 -L 16443:localhost:16443 $USER@raport.sin.net.pl

# onlexnet-sinnet-app-prd01 subscription contains also shared configuration for all prod environments
az account set --subscription onlexnet-sinnet-app-prd01
