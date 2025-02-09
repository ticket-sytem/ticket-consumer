#!/bin/bash

# Base URL and common parameters
BASE_URL="http://10.111.177.4/oper/buyTicket"
USER_ID="46e24856-b173-4b05-88bf-0cf519628c7d"
CAMPAIGN="camp1"

# Function to send request
send_request() {
    local area=$1
    local row=$2
    local col=$3
    
    url="${BASE_URL}?userId=${USER_ID}&campaignName=${CAMPAIGN}&area=${area}&row=${row}&column=${col}"
    echo "Sending request: $url"
    curl -s "$url"
    echo -e "\n"
    sleep 0.5  # Add small delay between requests
}

# Test Area A (10x10)
echo "Testing Area A tickets..."
for row in {1..5}; do
    for col in {1..5}; do
        send_request "A" $row $col
    done
done

# Test Area B (100x100)
echo "Testing Area B tickets..."
for row in {1..5}; do
    for col in {1..5}; do
        send_request "B" $row $col
    done
done

# Test Area C (500x500)
echo "Testing Area C tickets..."
for row in {1..5}; do
    for col in {1..5}; do
        send_request "C" $row $col
    done
done