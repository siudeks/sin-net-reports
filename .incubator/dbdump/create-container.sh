#!/bin/bash

### variables used by the script
DB_ENV=prod
PGPASSWORD=*****
ACR_NAME=*****
###

PGHOST=localhost
PGUSER=postgres

DUMP_NAME=bvs.pgdump
INIT_SQL_FILENAME=01-init.INIT_SQL_FILENAMEDOCKER_INIT_SCRIPT_NAME=02-init_docker_postgres.sh
FINALIZE_SQL_FILENAME=03-finalize.FINALIZE_SQL_FILENAMEIMAGE_NAME=postgres-$DB_ENV
ACR_IMAGE_NAME=$ACR_NAME.azurecr.io/sinnet/dbdump/$IMAGE_NAME:v-$(date + "%Y%m%d.%H%M%S")

# Creates dump file from the environment
dump_database() {
    PG_USER=$PGUSER \
    PGPASSWORD=$PGPASSWORD \
    PGHOST=$PGHOST \
    pg_dump -Fc -v --dbname=sinnet -f $DUMP_NAME
}

# Paste a temp script to be put into docker image
create_init_script() {
    echo "CREATE SCHEMA sinnet;
CREATE ROLE sinnet;
" > $INIT_SQL_FILENAME
    echo "ALTER ROLE postgres SET search_path TO sinnet;
" > $FINALIZE_SQL_FILENAME
    echo "#!/bin/bash
pg_restore --no-owner -v --username=postgres --dbname=postgres /tmp/$DUMP_NAME
echo 'Finished restoring database'
exit 0
" > $DOCKER_INIT_SCRIPT_NAME
    chmod a+x $DOCKER_INIT_SCRIPT_NAME
}

# REmoves interediate files
cleanup() {
    rm $DUMP_NAME
    rm $INIT_SQL_FILENAME
    rm $FINALIZE_SQL_FILENAME
    rm $DOCKER_INIT_SCRIPT_NAME
}

push_image_to_acr() {
    echo "Sending image to ACR ..."
    sudo az acr login --name $ACR_NAME
    sudo docker tag $IMAGE_NAME:latest $ACR_IMAGE_NAME
    sudo docker push $ACR_IMAGE_NAME
    if [ $? -eq 0]
    then
        echo "Image $ACR_IMAGE_NAME pushed to ACR"
    fi
}

### Entry point
dump_database
create_init_script
sudo docker build -t $IMAGE_NAME .
echo "IMage $IMAGE_NAE:latest created."
push_image_to_acr
cleanup