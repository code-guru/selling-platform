#!/usr/bin/env bash
# This is supposed to be used with the provided Dockerfile expecting an install distribution with bin/ and libs/ folder

project_root=$(dirname $(dirname $(realpath $0 )))
start_script=$project_root/bin/selling-platform
wait_script=$project_root/scripts/wait-for-it.sh

echo $start_script
$wait_script -s -t 10 -h ${DB_HOST:-localhost} -p ${DB_PORT:-5432} -- $start_script
