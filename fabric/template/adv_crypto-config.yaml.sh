ORDERER_DOMAIN="$1"
ORDERER_NAME="$2"
ORG_DOMAIN="$3"
ORG_PEER_COUNT="$4"

echo "OrdererOrgs:"
echo "  - Name: Orderer"
echo "    Domain: ${ORDERER_DOMAIN}"
echo "    Specs:"
echo "      - Hostname: ${ORDERER_NAME}"
echo "PeerOrgs:"
echo "  - Name: Org"
echo "    Domain: ${ORG_DOMAIN}"
echo "    EnableNodeOUs: true"
echo "    Template:"
echo "      Count: ${ORG_PEER_COUNT}"
echo "    Users:"
echo "      Count: 0"