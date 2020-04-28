#!/bin/sh
TENNANT_ID=${1:-__not__set__}
client_id=${2:-__not__set__}
client_secret=${3:-__not__set__}
SCOPE=${4:-https%3A%2F%2Fen.proptechos.com%2Fapi%2F.default}
grant_type=client_credentials
curl -X POST -H 'Content-Type: application/x-www-form-urlencoded' \
  -d "client_id=$client_id&scope=$SCOPE&client_secret=$client_secret&grant_type=client_credentials" \
  "https://login.microsoftonline.com/$TENNANT_ID/oauth2/v2.0/token"
