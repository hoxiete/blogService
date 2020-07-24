#!/bin/bash
cd ./remote_home/
imagename=blogapi
#按tag(时间)排序,选出倒数第三个镜像并删除
images=$(docker images | grep imagename | sort -t ' ' -k 2  -r | head -n 3 | awk 'NR==3 {print $2}')
if  [ $images ]; then
        docker rmi $images
        echo "delete $images"
else
        echo 'images not exist'
fi
cp ./src/main/docker/Dockerfile ./target/Dockerfile
cd target
time=$(date "+%Y_%m_%d_%H_%M")
docker build -t  $imagename:$time  .
docker run -id -p 8088:8088 --network=fastdfs_default  --name=$name $imagename:$time