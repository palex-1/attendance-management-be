$IMGS = Get-ChildItem -Filter *.tar
foreach($IMG in $IMGS) {
    docker load --input $IMG
}