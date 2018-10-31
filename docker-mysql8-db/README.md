# MySQL

Getting a local MySQL database connection set up can be a hassle. So I containerized it.

## Getting Started

1. First, create a `data/` directory in this directory. This will be used as the mounted volume to the container, so that it persist its data and you don't lose your information when you `rm` the container and `run` another instance.
2. Simply run `docker-compose up -d` in this directory, and you'll be able to connect to it. I've mapped it to port 3308.
3. To interact with the database on the command line, run `docker-compose run client` in this directory.
	- You can also connect to it via MySQLWorkbench.
4. When you're done, run `docker-compose down`, again in this directory.

## Acknowledgement

[This tutorial](https://www.youtube.com/watch?v=q5J3rtAGGNU) on YouTube helped me out a lot.

