#!/bin/sh
UUID=${1:-__not__set__}
BEARER_TOKEN=${2:-__bearer__token__}

curl -X GET "https://en.proptechos.com/api/realestatecomponent/$UUID" -H 'accept: application/ld+json' -H "Authorization: Bearer $BEARER_TOKEN"
