# MySQL

Getting a local MySQL database connection set up can be a hassle. So I containerized it.

## Getting Started

The following commands should all be run in this directory, since it contains the `docker-compose.yml` file.

1. First, create a `data/` directory in this directory. This will be used as the mounted volume to the container, so that it persists the data and you don't lose your information when you `rm` the container and `run` another instance.
2. The `init.sql` file contains a starter script that will create the necessary tables, and add in a few rows to them to get you started. The script is only run the first time around.
3. Simply run `docker-compose up -d`, and you'll be able to connect to it. I've mapped it to port 3306.
	- `d`, short for `--detach`, makes it run in the background.
4. To interact with the database on the command line, run `docker-compose run client`.
	- You can also connect to it via MySQLWorkbench.
5. When you're done, run `docker-compose down` to stop the containers.

## Acknowledgement

[This tutorial](https://www.youtube.com/watch?v=q5J3rtAGGNU) on YouTube helped me out a lot.
