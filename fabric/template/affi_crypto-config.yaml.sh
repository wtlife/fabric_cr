#!/bin/bash
ORG_DOMAIN="$1"
ORG_PEER_COUNT="$2"



echo "PeerOrgs:"
echo "  - Name: Org2"
echo "    Domain: ${ORG_DOMAIN}"
echo "    EnableNodeOUs: true"
echo "    Template:"
echo "      Count: ${ORG_PEER_COUNT}"
echo "    Users:"
echo "      Count: 1"
exit 0 